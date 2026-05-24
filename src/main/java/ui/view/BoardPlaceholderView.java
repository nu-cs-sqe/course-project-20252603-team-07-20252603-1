package ui.view;

import domain.model.Board;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * Static board placeholder: renders the 19 hex tiles from a Board in the
 * standard 3-4-5-4-3 row layout. No interactivity. Settlements, roads, and
 * the robber will land here once graph-wip merges.
 */
public class BoardPlaceholderView {

    private static final int[] HEX_ROW_SIZES = {3, 4, 5, 4, 3};

    private final VBox root;

    public BoardPlaceholderView(Board board) {
        root = buildGrid(board.getHexOrder());
    }

    public Parent getRoot() {
        return root;
    }

    private static VBox buildGrid(List<String> hexes) {
        HBox[] rows = new HBox[HEX_ROW_SIZES.length];
        int idx = 0;
        for (int i = 0; i < rows.length; i++) {
            rows[i] = buildRow(hexes, idx, HEX_ROW_SIZES[i]);
            idx += HEX_ROW_SIZES[i];
        }
        VBox grid = new VBox(rows);
        grid.getStyleClass().add("hex-grid");
        return grid;
    }

    private static HBox buildRow(List<String> hexes, int startIdx, int rowSize) {
        Label[] cells = new Label[rowSize];
        for (int i = 0; i < rowSize; i++) {
            cells[i] = buildHex(hexes.get(startIdx + i));
        }
        HBox row = new HBox(cells);
        row.getStyleClass().add("hex-row");
        return row;
    }

    private static Label buildHex(String type) {
        Label hex = new Label(type);
        hex.getStyleClass().addAll("hex", String.format("hex-%s", type.toLowerCase()));
        return hex;
    }
}
