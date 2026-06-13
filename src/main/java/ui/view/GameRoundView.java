package ui.view;

import domain.model.GameModel;
import domain.model.board.BoardHandler;
import domain.model.exceptions.IllegalGamePhaseException;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ui.ViewContext;


@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"},
        justification = "UI classes intentionally share JavaFX nodes, controllers, and models by reference")
public class GameRoundView {

    private static final int SECTION_PADDING_PX = 10;
    private static final String GAME_ROOT_CSS = "game-root";
    private static final String SECTION_CSS = "summary-section";
    private static final String BUTTON_BAR_CSS = "button-bar";
    private static final String DICE_READOUT_CSS = "dice-readout";
    private static final String STATUS_CSS = "status";
    private static final String ERROR_CSS = "error";

    private final GameModel model;
    private final ViewContext context;

    private final CurrentPlayerBanner banner;
    private final Label lastRollLabel;
    private final Label statusLabel;
    private final PlayerResourcesPanel resourcesPanel;
    private final BorderPane root;

    public GameRoundView(RoundNavigator navigator,
                         ViewContext context,
                         GameModel model,
                         BoardHandler board) {
        this.model = model;
        this.context = context;

        this.banner = new CurrentPlayerBanner(context.labels());
        this.lastRollLabel = buildLastRollLabel();
        this.statusLabel = buildStatusLabel();
        this.resourcesPanel = new PlayerResourcesPanel(context.loop(), model, context.labels());
        this.root = buildLayout(board);

        runSafely(this::beginTurn);
    }

    public Parent getRoot() {
        return root;
    }

    private BorderPane buildLayout(BoardHandler board) {
        BorderPane pane = new BorderPane();
        pane.getStyleClass().add(GAME_ROOT_CSS);
        pane.setTop(banner.getRoot());
        pane.setCenter(new BoardPlaceholderView(board, context.labels()).getRoot());
        pane.setRight(buildResourcesSection());
        pane.setBottom(buildBottomSection());
        return pane;
    }

    private VBox buildResourcesSection() {
        VBox section = new VBox(new Label(context.labels().getString("round.resources")), resourcesPanel.getRoot());
        section.getStyleClass().add(SECTION_CSS);
        section.setPadding(new Insets(SECTION_PADDING_PX));
        return section;
    }

    private VBox buildBottomSection() {
        Button endTurnButton = new Button(context.labels().getString("round.endTurn"));
        endTurnButton.setOnAction(e -> onEndTurn());

        HBox controlBar = new HBox(lastRollLabel, endTurnButton);
        controlBar.getStyleClass().add(BUTTON_BAR_CSS);

        VBox section = new VBox(controlBar, statusLabel);
        section.setAlignment(Pos.CENTER);
        return section;
    }

    private static Label buildLastRollLabel() {
        Label label = new Label();
        label.getStyleClass().add(DICE_READOUT_CSS);
        return label;
    }

    private static Label buildStatusLabel() {
        Label label = new Label();
        label.getStyleClass().add(STATUS_CSS);
        return label;
    }

    private void onEndTurn() {
        runSafely(() -> {
            context.loop().endTurn(model);
            beginTurn();
        });
    }

    private void beginTurn() {
        int roll = context.loop().rollDiceAndDistribute(model, context.dice());
        lastRollLabel.setText(MessageFormat.format(context.labels().getString("round.rolled"), roll));
        banner.update(context.loop().getCurrentPlayer(model));
        resourcesPanel.refresh();
    }

    private void runSafely(Runnable action) {
        try {
            action.run();
            clearStatus();
        } catch (RuntimeException ex) {
            showError(messageFor(ex));
        }
    }

    private String messageFor(RuntimeException ex) {
        ResourceBundle labels = context.labels();
        if (ex instanceof IllegalGamePhaseException) {
            return labels.getString("error.wrongPhase");
        }
        return MessageFormat.format(labels.getString("error.unexpected"), ex.getMessage());
    }

    private void showError(String message) {
        statusLabel.getStyleClass().setAll(STATUS_CSS, ERROR_CSS);
        statusLabel.setText(message);
    }

    private void clearStatus() {
        statusLabel.getStyleClass().setAll(STATUS_CSS);
        statusLabel.setText("");
    }
}
