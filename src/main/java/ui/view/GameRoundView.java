package ui.view;

import domain.model.Board;
import domain.model.GameModel;
import domain.model.resources.ResourceDeck;
import java.text.MessageFormat;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ui.ViewContext;


public class GameRoundView {

    private static final int SECTION_PADDING_PX = 10;
    private static final String GAME_ROOT_CSS = "game-root";
    private static final String SECTION_CSS = "summary-section";
    private static final String BUTTON_BAR_CSS = "button-bar";
    private static final String DICE_READOUT_CSS = "dice-readout";

    private final GameModel model;
    private final ViewContext context;
    private final ResourceDeck deck;

    private final CurrentPlayerBanner banner;
    private final Label lastRollLabel;
    private final PlayerResourcesPanel resourcesPanel;
    private final BorderPane root;

    public GameRoundView(RoundNavigator navigator,
                         ViewContext context,
                         GameModel model,
                         ResourceDeck deck,
                         Board board) {
        this.model = model;
        this.context = context;
        this.deck = deck;

        this.banner = new CurrentPlayerBanner(context.labels());
        this.lastRollLabel = buildLastRollLabel();
        this.resourcesPanel = new PlayerResourcesPanel(context.loop(), model, context.labels());
        this.root = buildLayout(board);

        beginTurn();
    }

    public Parent getRoot() {
        return root;
    }

    private BorderPane buildLayout(Board board) {
        BorderPane pane = new BorderPane();
        pane.getStyleClass().add(GAME_ROOT_CSS);
        pane.setTop(banner.getRoot());
        pane.setCenter(new BoardPlaceholderView(board, context.labels()).getRoot());
        pane.setRight(buildResourcesSection());
        pane.setBottom(buildControlsBar());
        return pane;
    }

    private VBox buildResourcesSection() {
        VBox section = new VBox(new Label(context.labels().getString("round.resources")), resourcesPanel.getRoot());
        section.getStyleClass().add(SECTION_CSS);
        section.setPadding(new Insets(SECTION_PADDING_PX));
        return section;
    }

    private HBox buildControlsBar() {
        Button endTurnButton = new Button(context.labels().getString("round.endTurn"));
        endTurnButton.setOnAction(e -> onEndTurn());
        HBox bar = new HBox(lastRollLabel, endTurnButton);
        bar.getStyleClass().add(BUTTON_BAR_CSS);
        return bar;
    }

    private static Label buildLastRollLabel() {
        Label label = new Label();
        label.getStyleClass().add(DICE_READOUT_CSS);
        return label;
    }

    private void onEndTurn() {
        context.loop().endTurn(model);
        beginTurn();
    }

    private void beginTurn() {
        int roll = context.loop().rollDiceAndDistribute(model, context.dice(), deck);
        lastRollLabel.setText(MessageFormat.format(context.labels().getString("round.rolled"), roll));
        banner.update(context.loop().getCurrentPlayer(model));
        resourcesPanel.refresh();
    }
}
