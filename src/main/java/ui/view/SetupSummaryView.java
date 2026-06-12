package ui.view;

import domain.model.GameSetupModel;
import domain.model.board.BoardHandler;
import domain.model.player.Player;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import ui.ViewContext;
import ui.controller.GameSetupController;

import java.text.MessageFormat;
import java.util.List;
import java.util.ResourceBundle;

@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"},
        justification = "UI classes intentionally share JavaFX nodes, controllers, and models by reference")
public class SetupSummaryView {

    private static final int[] HEX_ROW_SIZES = {3, 4, 5, 4, 3};

    private final ScrollPane root;
    private final ResourceBundle labels;

    public SetupSummaryView(SetupNavigator navigator, ViewContext context, GameSetupModel model) {
        this.labels = context.labels();
        GameSetupController controller = context.setup();

        Label header = new Label(labels.getString("summary.title"));
        header.getStyleClass().add("title");

        VBox turnOrder = buildTurnOrderSection(controller, model);
        VBox board = buildBoardSection(controller, model);
        VBox decks = buildDecksSection(controller, model);

        Button homeButton = new Button(labels.getString("common.backToHome"));
        homeButton.setOnAction(e -> navigator.goToHome());

        Button startGameButton = new Button(labels.getString("common.startGame"));
        startGameButton.setDisable(controller.getTurnOrder(model).isEmpty());
        startGameButton.setOnAction(e -> navigator.startGame());

        HBox buttons = new HBox(homeButton, startGameButton);
        buttons.getStyleClass().add("button-bar");

        VBox content = new VBox(header, turnOrder, board, decks, buttons);
        content.getStyleClass().add("screen");

        root = new ScrollPane(content);
        root.setFitToWidth(true);
    }

    public Parent getRoot() {
        return root;
    }

    private VBox buildTurnOrderSection(GameSetupController controller, GameSetupModel model) {
        Label heading = new Label(labels.getString("summary.turnOrder"));
        heading.getStyleClass().add("section-heading");

        VBox playerLines = new VBox();
        playerLines.getStyleClass().add("turn-order-list");

        List<Player> order = controller.getTurnOrder(model);
        for (int i = 0; i < order.size(); i++) {
            Player p = order.get(i);
            Label position = new Label((i + 1) + ".");
            Region swatch = new Region();
            swatch.getStyleClass().addAll("color-swatch", "swatch-" + p.getColor().name().toLowerCase());
            Label name = new Label(p.getName());

            HBox line = new HBox(position, swatch, name);
            line.getStyleClass().add("player-line");
            playerLines.getChildren().add(line);
        }

        VBox section = new VBox(heading, playerLines);
        section.getStyleClass().add("summary-section");
        return section;
    }

    private VBox buildBoardSection(GameSetupController controller, GameSetupModel model) {
        Label heading = new Label(labels.getString("summary.boardLayout"));
        heading.getStyleClass().add("section-heading");

        BoardHandler board = controller.getBoard(model);
        List<String> hexes = controller.getHexOrder(board);

        VBox grid = new VBox();
        grid.getStyleClass().add("hex-grid");

        int idx = 0;
        for (int rowSize : HEX_ROW_SIZES) {
            HBox row = new HBox();
            row.getStyleClass().add("hex-row");
            for (int j = 0; j < rowSize; j++) {
                String type = hexes.get(idx++);
                // Display name is localized; the CSS class keeps the canonical hex type.
                Label hex = new Label(labels.getString("hex." + type));
                hex.getStyleClass().addAll("hex", "hex-" + type.toLowerCase());
                row.getChildren().add(hex);
            }
            grid.getChildren().add(row);
        }

        VBox section = new VBox(heading, grid);
        section.getStyleClass().add("summary-section");
        return section;
    }

    private VBox buildDecksSection(GameSetupController controller, GameSetupModel model) {
        Label heading = new Label(labels.getString("summary.decks"));
        heading.getStyleClass().add("section-heading");

        int resourceCount = controller.getResourceDeck(model).getTotalCards();
        int devCount = controller.getDevelopmentCardDeck(model).countRemaining();

        Label resources = new Label(
                MessageFormat.format(labels.getString("summary.resourceCards"), resourceCount));
        Label dev = new Label(
                MessageFormat.format(labels.getString("summary.developmentCards"), devCount));

        VBox section = new VBox(heading, resources, dev);
        section.getStyleClass().add("summary-section");
        return section;
    }
}
