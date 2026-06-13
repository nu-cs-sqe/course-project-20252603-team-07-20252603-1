package ui.view;

import domain.model.GameModel;
import domain.model.GamePhase;
import domain.model.board.BoardHandler;
import domain.model.board.Port;
import domain.model.developmentcards.DevelopmentCard;
import domain.model.developmentcards.DevelopmentCardDeck;
import domain.model.exceptions.AdjacentNodeAlreadyClaimed;
import domain.model.exceptions.IllegalCityPlacementException;
import domain.model.exceptions.IllegalEdgeClaim;
import domain.model.exceptions.IllegalGamePhaseException;
import domain.model.exceptions.IllegalRoadPlacementException;
import domain.model.exceptions.IllegalSettlementPlacementException;
import domain.model.exceptions.InsufficientResourcesException;
import domain.model.player.Player;
import domain.model.player.PlayerColor;
import domain.model.player.TradeOffer;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import ui.ViewContext;
import ui.view.board.BoardSelectionMode;
import ui.view.board.BoardView;


/** Main game round view showing the board, resources, controls, and dice. */
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"},
    justification = "UI classes intentionally share JavaFX nodes, controllers, and "
        + "models by reference")
public class GameRoundView {

  private static final int SECTION_PADDING_PX = 10;
  private static final int STATUS_ROW_SPACING_PX = 16;
  private static final String GAME_ROOT_CSS = "game-root";
  private static final String SECTION_CSS = "summary-section";
  private static final String CONTROL_BAR_CSS = "control-bar";
  private static final String DICE_READOUT_CSS = "dice-readout";
  private static final String STATUS_CSS = "status";
  private static final String ERROR_CSS = "error";
  private static final String WINNER_CSS = "winner-banner";
  private static final String DIE_CSS = "die";
  private static final String DIE_ROLLING_CSS = "die-rolling";

  // Unicode die faces 1-6 (U+2680..U+2685)
  private static final String[] DIE_FACES = {
      "⚀", "⚁", "⚂", "⚃", "⚄", "⚅"
  };
  private static final int DIE_MAX = DIE_FACES.length;
  private static final int ROLL_TUMBLE_FRAMES = 12;
  private static final int ROLL_FRAME_MILLIS = 55;

  private final RoundNavigator navigator;
  private final GameModel model;
  private final ViewContext context;
  private final BoardHandler board;
  private final DevelopmentCardDeck devCardDeck;

  private final CurrentPlayerBanner banner;
  private final Label lastRollLabel;
  private final Label dieOneLabel;
  private final Label dieTwoLabel;
  private final Random animationRandom = new Random();
  private Timeline rollAnimation;
  private final Label statusLabel;
  private final FlowPane controlBar;
  private final PlayerResourcesPanel resourcesPanel;
  private final BoardView boardView;
  private final BorderPane root;

  // setup phase: each player places two settlement+road pairs in snake order
  private int setupPlacementIndex;
  private boolean setupAwaitingRoad;

  /** Constructs the game round view. */
  public GameRoundView(RoundNavigator navigator,
                       ViewContext context,
                       GameModel model,
                       BoardHandler board,
                       DevelopmentCardDeck devCardDeck) {
    this.navigator = navigator;
    this.model = model;
    this.context = context;
    this.board = board;
    this.devCardDeck = devCardDeck;

    this.banner = new CurrentPlayerBanner(context.labels());
    this.lastRollLabel = buildLastRollLabel();
    this.dieOneLabel = buildDieLabel();
    this.dieTwoLabel = buildDieLabel();
    this.statusLabel = buildStatusLabel();
    this.controlBar = buildControlBar();
    this.resourcesPanel = new PlayerResourcesPanel(context.loop(), model, context.labels());
    this.boardView = new BoardView(board, context.labels());
    this.root = buildLayout();

    refreshAll();
    autoRollIfNeeded();
  }

  public Parent getRoot() {
    return root;
  }

  private BorderPane buildLayout() {
    BorderPane pane = new BorderPane();
    pane.getStyleClass().add(GAME_ROOT_CSS);
    pane.setTop(banner.getRoot());
    pane.setCenter(boardView.getRoot());
    pane.setRight(buildResourcesSection());
    pane.setBottom(buildBottomSection());
    return pane;
  }

  private VBox buildResourcesSection() {
    VBox section = new VBox(new Label(context.labels().getString("round.resources")),
        resourcesPanel.getRoot());
    section.getStyleClass().add(SECTION_CSS);
    section.setPadding(new Insets(SECTION_PADDING_PX));
    return section;
  }

  private VBox buildBottomSection() {
    HBox statusRow = new HBox(dieOneLabel, dieTwoLabel, lastRollLabel, statusLabel);
    statusRow.setAlignment(Pos.CENTER);
    statusRow.setSpacing(STATUS_ROW_SPACING_PX);

    VBox section = new VBox(statusRow, controlBar);
    section.setAlignment(Pos.CENTER);
    return section;
  }

  private static FlowPane buildControlBar() {
    FlowPane bar = new FlowPane();
    bar.getStyleClass().add(CONTROL_BAR_CSS);
    return bar;
  }

  private static Label buildLastRollLabel() {
    Label label = new Label();
    label.getStyleClass().add(DICE_READOUT_CSS);
    return label;
  }

  private static Label buildDieLabel() {
    Label label = new Label();
    label.getStyleClass().add(DIE_CSS);
    return label;
  }

  private static Label buildStatusLabel() {
    Label label = new Label();
    label.getStyleClass().add(STATUS_CSS);
    return label;
  }

  private void refreshAll() {
    banner.update(context.loop().getCurrentPlayer(model));
    resourcesPanel.refresh();
    boardView.refresh();
    refreshForPhase();
  }

  private void refreshForPhase() {
    boardView.setSelectionMode(BoardSelectionMode.INERT);
    controlBar.getChildren().clear();

    GamePhase phase = context.loop().getCurrentPhase(model);
    switch (phase) {
      case SETUP_PHASE:
        armSetupPlacement();
        break;
      case BEFORE_ROLL:
        // normally auto-rolled; the button is a fallback if a roll failed
        controlBar.getChildren().add(buildRollButton());
        break;
      case GENERAL_PLAY:
        controlBar.getChildren().addAll(buildGeneralPlayButtons());
        break;
      case MOVE_ROBBER:
        showInstruction("round.status.pickRobberHex");
        boardView.setSelectionMode(BoardSelectionMode.PICK_HEX);
        boardView.setOnHexSelected(this::onRobberHexPicked);
        break;
      case END_GAME:
        controlBar.getChildren().addAll(buildWinnerBanner(), buildHomeButton());
        break;
      default:
        break;
    }
  }

  // ----- setup phase placement -----

  private void armSetupPlacement() {
    syncSetupCurrentPlayer();
    if (setupAwaitingRoad) {
      showInstruction("round.status.placeSetupRoad");
      boardView.setSelectionMode(BoardSelectionMode.PICK_EDGE);
      boardView.setOnEdgeSelected(this::onSetupRoadPicked);
    } else {
      showInstruction("round.status.placeSetupSettlement");
      boardView.setSelectionMode(BoardSelectionMode.PICK_NODE);
      boardView.setOnNodeSelected(this::onSetupSettlementPicked);
    }
  }

  private void syncSetupCurrentPlayer() {
    int playerCount = model.getTurnOrder().size();
    int playerIndex = setupPlayerIndexFor(setupPlacementIndex, playerCount);
    if (context.loop().getCurrentPlayerIndex(model) != playerIndex) {
      context.loop().setCurrentPlayer(model, playerIndex);
      banner.update(context.loop().getCurrentPlayer(model));
    }
  }

  // snake order: 0..n-1 then n-1..0
  private static int setupPlayerIndexFor(int placementIndex, int playerCount) {
    return placementIndex < playerCount
        ? placementIndex
        : 2 * playerCount - 1 - placementIndex;
  }

  private void onSetupSettlementPicked(int nodeId) {
    runAction(() -> {
      context.loop().attemptBuildSettlement(model, nodeId);
      setupAwaitingRoad = true;
    });
  }

  private void onSetupRoadPicked(int nodeA, int nodeB) {
    runAction(() -> {
      context.loop().attemptBuildRoad(model, nodeA, nodeB);
      setupAwaitingRoad = false;
      setupPlacementIndex++;
      if (setupPlacementIndex >= 2 * model.getTurnOrder().size()) {
        context.loop().completeSetupPhase(model);
      }
    });
    autoRollIfNeeded();
  }

  private Button buildRollButton() {
    Button button = new Button(context.labels().getString("round.roll"));
    button.setOnAction(e -> onRollDice());
    return button;
  }

  private List<Button> buildGeneralPlayButtons() {
    Button buildSettlement = new Button(context.labels().getString("round.buildSettlement"));
    buildSettlement.setOnAction(e -> armNodePick("round.status.pickSettlement",
        nodeId -> runAction(() -> context.loop().attemptBuildSettlement(model, nodeId))));

    Button buildCity = new Button(context.labels().getString("round.buildCity"));
    buildCity.setOnAction(e -> armNodePick("round.status.pickCity",
        nodeId -> runAction(() -> context.loop().attemptBuildCity(model, nodeId))));

    Button buildRoad = new Button(context.labels().getString("round.buildRoad"));
    buildRoad.setOnAction(e -> armEdgePick("round.status.pickRoad",
        (a, b) -> runAction(() -> context.loop().attemptBuildRoad(model, a, b))));

    Button trade = new Button(context.labels().getString("round.trade"));
    trade.setOnAction(e -> onOpenTrade());

    Button portTrade = new Button(context.labels().getString("round.portTrade"));
    portTrade.setDisable(availablePorts().isEmpty());
    portTrade.setOnAction(e -> onOpenPortTrade());

    Button devCards = new Button(context.labels().getString("round.devCards"));
    devCards.setOnAction(e -> onOpenDevCards());

    Button endTurn = new Button(context.labels().getString("round.endTurn"));
    endTurn.setOnAction(e -> {
      runAction(() -> context.loop().endTurn(model));
      autoRollIfNeeded();
    });

    return List.of(buildSettlement, buildCity, buildRoad, trade, portTrade, devCards, endTurn);
  }

  private Label buildWinnerBanner() {
    Label winner = new Label(MessageFormat.format(context.labels().getString("round.winner"),
        context.loop().getCurrentPlayer(model).getName()));
    winner.getStyleClass().add(WINNER_CSS);
    return winner;
  }

  private Button buildHomeButton() {
    Button home = new Button(context.labels().getString("common.backToHome"));
    home.setOnAction(e -> navigator.goToHome());
    return home;
  }

  private void onRollDice() {
    runAction(() -> {
      int roll = context.loop().rollDiceAndDistribute(model, context.dice());
      animateRoll(roll);
    });
  }

  /**
   * Plays a brief dice-tumbling animation that flashes random faces before
   * settling on a pair of dice whose pips add up to the actual roll total.
   * Purely cosmetic: the game state has already advanced by the time this runs.
   */
  private void animateRoll(int total) {
    if (rollAnimation != null) {
      rollAnimation.stop();
    }
    dieOneLabel.getStyleClass().add(DIE_ROLLING_CSS);
    dieTwoLabel.getStyleClass().add(DIE_ROLLING_CSS);
    lastRollLabel.setText(context.labels().getString("round.rolling"));

    Timeline timeline = new Timeline();
    for (int frame = 1; frame <= ROLL_TUMBLE_FRAMES; frame++) {
      Duration when = Duration.millis((double) frame * ROLL_FRAME_MILLIS);
      timeline.getKeyFrames().add(new KeyFrame(when, e -> tumbleDice()));
    }
    timeline.setOnFinished(e -> settleDice(total));
    rollAnimation = timeline;

    tumbleDice();
    timeline.play();
  }

  private void tumbleDice() {
    dieOneLabel.setText(randomFace());
    dieTwoLabel.setText(randomFace());
  }

  private void settleDice(int total) {
    // split the total into two valid die faces (each 1..6) for display
    int low = Math.max(1, total - DIE_MAX);
    int high = Math.min(DIE_MAX, total - 1);
    int first = low + animationRandom.nextInt(high - low + 1);
    int second = total - first;

    dieOneLabel.setText(DIE_FACES[first - 1]);
    dieTwoLabel.setText(DIE_FACES[second - 1]);
    dieOneLabel.getStyleClass().remove(DIE_ROLLING_CSS);
    dieTwoLabel.getStyleClass().remove(DIE_ROLLING_CSS);
    lastRollLabel.setText(MessageFormat.format(context.labels().getString("round.rolled"), total));
  }

  private String randomFace() {
    return DIE_FACES[animationRandom.nextInt(DIE_MAX)];
  }

  private void autoRollIfNeeded() {
    if (context.loop().getCurrentPhase(model) == GamePhase.BEFORE_ROLL) {
      onRollDice();
    }
  }

  private void onRobberHexPicked(int hexId) {
    Player victim = pickVictimOnHex(hexId);
    PlayerColor victimColor = victim == null ? null : victim.getColor();
    runAction(() -> context.loop().moveRobberAndSteal(model, hexId, victimColor));
  }

  private Player pickVictimOnHex(int hexId) {
    Player current = context.loop().getCurrentPlayer(model);
    List<Player> candidates = new ArrayList<>(context.loop().getPlayersOnHex(board, hexId));
    candidates.remove(current);
    if (candidates.isEmpty()) {
      return null;
    }
    return new VictimPickDialog(context.labels(), candidates).showAndPick().orElse(null);
  }

  // ----- board pick arming -----

  private void armNodePick(String statusKey, java.util.function.IntConsumer onNode) {
    armPick(BoardSelectionMode.PICK_NODE, statusKey);
    boardView.setOnNodeSelected(onNode);
  }

  private void armEdgePick(String statusKey, BoardView.EdgeSelectionHandler onEdge) {
    armPick(BoardSelectionMode.PICK_EDGE, statusKey);
    boardView.setOnEdgeSelected(onEdge);
  }

  private void armPick(BoardSelectionMode mode, String statusKey) {
    boardView.setSelectionMode(mode);
    statusLabel.getStyleClass().remove(ERROR_CSS);
    statusLabel.setText(context.labels().getString(statusKey));

    Button cancel = new Button(context.labels().getString("round.cancel"));
    cancel.setOnAction(e -> {
      clearStatus();
      refreshAll();
    });
    controlBar.getChildren().setAll(cancel);
  }

  // ----- dev cards -----

  private void onOpenDevCards() {
    Optional<DevelopmentCard> played =
        new DevCardDialog(context, model, devCardDeck).showAndPlay();
    refreshAll();
    played.ifPresent(this::dispatchDevCard);
  }

  private void dispatchDevCard(DevelopmentCard card) {
    switch (card.getType()) {
      case KNIGHT:
        armPick(BoardSelectionMode.PICK_HEX, "round.status.pickKnightHex");
        boardView.setOnHexSelected(hexId -> {
          Player victim = pickVictimOnHex(hexId);
          runAction(() -> context.devCards().playKnightCard(
              model, card, board.getRobber(), hexId, victim));
        });
        break;
      case MONOPOLY:
        new ResourcePickDialog(context.labels(), 1).showAndPick().ifPresent(picked ->
            runAction(() -> context.devCards().playMonopolyCard(model, card, picked.get(0))));
        break;
      case YEAR_OF_PLENTY:
        new ResourcePickDialog(context.labels(), 2).showAndPick().ifPresent(picked ->
            runAction(() -> context.devCards().playYearOfPlentyCard(
                model, card, picked.get(0), picked.get(1))));
        break;
      case ROAD_BUILDER:
        armEdgePick("round.status.pickRoad1",
            (a, b) -> armSecondRoadPick(card, a, b));
        break;
      default:
        break;
    }
  }

  private void armSecondRoadPick(DevelopmentCard card, int firstA, int firstB) {
    armEdgePick("round.status.pickRoad2", (a, b) ->
        runAction(
            () -> context.devCards().playRoadBuildingCard(model, card, firstA, firstB, a, b)));

    Button skip = new Button(context.labels().getString("devcard.skipSecondRoad"));
    skip.setOnAction(e ->
        runAction(() -> context.devCards()
            .playRoadBuildingCard(model, card, firstA, firstB, null, null)));
    controlBar.getChildren().add(0, skip);
  }

  // ----- trading -----

  private void onOpenTrade() {
    Player current = context.loop().getCurrentPlayer(model);
    Optional<TradeOffer> composed =
        new TradeComposeDialog(context.labels(), current).showAndCompose();
    if (composed.isEmpty()) {
      return;
    }
    TradeOffer offer = composed.get();

    runAction(() -> context.loop().offerTrade(model, offer));
    if (context.loop().getCurrentPhase(model) != GamePhase.OFFERING_TRADE) {
      return;
    }

    Optional<Player> acceptor = new TradeRespondDialog(
        context.labels(), current, offer, context.loop().getOtherPlayers(model)).showAndRespond();
    if (acceptor.isPresent()) {
      try {
        context.loop().acceptTrade(model, offer, acceptor.get());
        clearStatus();
      } catch (RuntimeException ex) {
        showError(messageFor(ex));
      }
    }
    // never leave the phase machine stuck in OFFERING_TRADE
    if (context.loop().getCurrentPhase(model) == GamePhase.OFFERING_TRADE) {
      context.loop().clearOffers(model);
    }
    refreshAll();
  }

  private void onOpenPortTrade() {
    List<Port> ports = availablePorts();
    if (ports.isEmpty()) {
      return;
    }
    new PortTradeDialog(context.labels(), ports).showAndCompose().ifPresent(selection ->
        runAction(() -> context.loop().attemptPortTrade(
            model, selection.getPort(), selection.getGiving(), selection.getReceiving())));
  }

  private List<Port> availablePorts() {
    return context.loop().getAvailablePorts(board, context.loop().getCurrentPlayer(model));
  }

  // ----- action plumbing -----

  private void runAction(Runnable action) {
    try {
      action.run();
      clearStatus();
    } catch (RuntimeException ex) {
      showError(messageFor(ex));
    } finally {
      refreshAll();
    }
  }

  private String messageFor(RuntimeException ex) {
    if (ex instanceof IllegalGamePhaseException) {
      return context.labels().getString("error.wrongPhase");
    }
    if (ex instanceof InsufficientResourcesException) {
      return context.labels().getString("error.insufficientResources");
    }
    if (ex instanceof IllegalSettlementPlacementException
        || ex instanceof IllegalCityPlacementException
        || ex instanceof IllegalRoadPlacementException
        || ex instanceof IllegalEdgeClaim
        || ex instanceof AdjacentNodeAlreadyClaimed) {
      return context.labels().getString("error.invalidPlacement");
    }
    return ex.getMessage();
  }

  private void showError(String message) {
    statusLabel.setText(message);
    if (!statusLabel.getStyleClass().contains(ERROR_CSS)) {
      statusLabel.getStyleClass().add(ERROR_CSS);
    }
  }

  private void showInstruction(String labelKey) {
    // keep an error visible until the next successful action clears it
    if (!statusLabel.getStyleClass().contains(ERROR_CSS)) {
      statusLabel.setText(context.labels().getString(labelKey));
    }
  }

  private void clearStatus() {
    statusLabel.setText("");
    statusLabel.getStyleClass().remove(ERROR_CSS);
  }
}
