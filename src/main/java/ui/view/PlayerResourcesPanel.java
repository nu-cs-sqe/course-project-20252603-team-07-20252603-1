package ui.view;

import domain.model.GameModel;
import domain.model.player.Player;
import domain.model.resources.Resource;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import ui.controller.GameLoopController;

import java.util.List;

public class PlayerResourcesPanel {

    private static final Resource[] RESOURCE_COLUMNS = Resource.values();


    private static final int HEADER_ROW = 0;
    private static final int PLAYER_NAME_COLUMN = 0;
    private static final int FIRST_RESOURCE_COLUMN = 1;
    private static final int FIRST_PLAYER_ROW = 1;
    private static final int NAME_CELL_SPACING_PX = 6;

    private static final String PANEL_CSS = "resources-panel";
    private static final String CELL_CSS = "resources-cell";
    private static final String HEADER_CSS = "resources-header";
    private static final String CURRENT_PLAYER_CSS = "current-player-row";
    private static final String SWATCH_CSS = "color-swatch";

    private final GridPane root;
    private final GameModel model;
    private final GameLoopController controller;

    public PlayerResourcesPanel(GameLoopController controller, GameModel model) {
        this.controller = controller;
        this.model = model;
        this.root = new GridPane();
        this.root.getStyleClass().add(PANEL_CSS);
        refresh();
    }

    public Parent getRoot() {
        return root;
    }

    public void refresh() {
        root.getChildren().clear();
        addHeaderRow();
        addPlayerRows();
    }

    private void addHeaderRow() {
        root.add(buildHeaderCell("Player"), PLAYER_NAME_COLUMN, HEADER_ROW);
        for (int resourceIdx = 0; resourceIdx < RESOURCE_COLUMNS.length; resourceIdx++) {
            root.add(buildHeaderCell(RESOURCE_COLUMNS[resourceIdx].name()),
                    FIRST_RESOURCE_COLUMN + resourceIdx, HEADER_ROW);
        }
    }

    private void addPlayerRows() {
        List<Player> players = model.getTurnOrder();
        int currentPlayerIdx = controller.getCurrentPlayerIndex(model);
        for (int playerIdx = 0; playerIdx < players.size(); playerIdx++) {
            addPlayerRow(playerIdx, players.get(playerIdx), playerIdx == currentPlayerIdx);
        }
    }

    private void addPlayerRow(int playerIdx, Player player, boolean isCurrentPlayer) {
        int gridRow = FIRST_PLAYER_ROW + playerIdx;
        root.add(buildNameCell(player, isCurrentPlayer), PLAYER_NAME_COLUMN, gridRow);

        for (int resourceIdx = 0; resourceIdx < RESOURCE_COLUMNS.length; resourceIdx++) {
            int count = controller.getResourceCount(model, playerIdx, RESOURCE_COLUMNS[resourceIdx]);
            root.add(buildCountCell(count, isCurrentPlayer),
                    FIRST_RESOURCE_COLUMN + resourceIdx, gridRow);
        }
    }

    private static HBox buildNameCell(Player player, boolean isCurrentPlayer) {
        Region swatch = new Region();
        swatch.getStyleClass().addAll(SWATCH_CSS,
                String.format("swatch-%s", player.getColor().toString().toLowerCase()));

        HBox cell = new HBox(swatch, new Label(player.getName()));
        cell.setAlignment(Pos.CENTER_LEFT);
        cell.setSpacing(NAME_CELL_SPACING_PX);
        cell.getStyleClass().add(CELL_CSS);
        highlightIfCurrent(cell, isCurrentPlayer);
        return cell;
    }

    private static Label buildCountCell(int count, boolean isCurrentPlayer) {
        Label cell = new Label(String.valueOf(count));
        cell.getStyleClass().add(CELL_CSS);
        highlightIfCurrent(cell, isCurrentPlayer);
        return cell;
    }

    private static Label buildHeaderCell(String text) {
        Label label = new Label(text);
        label.getStyleClass().addAll(CELL_CSS, HEADER_CSS);
        return label;
    }

    private static void highlightIfCurrent(Node cell, boolean isCurrentPlayer) {
        if (isCurrentPlayer) {
            cell.getStyleClass().add(CURRENT_PLAYER_CSS);
        }
    }
}
