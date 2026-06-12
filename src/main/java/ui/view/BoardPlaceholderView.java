package ui.view;

import domain.model.board.BoardHandler;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.ResourceBundle;

@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"},
        justification = "UI classes intentionally share JavaFX nodes, controllers, and models by reference")
public class BoardPlaceholderView {

    private static final int[] HEX_ROW_SIZES = {3, 4, 5, 4, 3};

    private final VBox root;

    public BoardPlaceholderView(BoardHandler board, ResourceBundle labels) {
        root = buildGrid(board.getHexOrder(), labels);
    }

    public Parent getRoot() {
        return root;
    }

    //TODO: make with actual hexes instead of rectangles
    private static VBox buildGrid(List<String> hexes, ResourceBundle labels) {
        HBox[] rows = new HBox[HEX_ROW_SIZES.length];
        int idx = 0;
        for (int i = 0; i < rows.length; i++) {
            rows[i] = buildRow(hexes, idx, HEX_ROW_SIZES[i], labels);
            idx += HEX_ROW_SIZES[i];
        }
        VBox grid = new VBox(rows);
        grid.getStyleClass().add("hex-grid");
        return grid;
    }

    private static HBox buildRow(List<String> hexes, int startIdx, int rowSize, ResourceBundle labels) {
        Label[] cells = new Label[rowSize];
        for (int i = 0; i < rowSize; i++) {
            cells[i] = buildHex(hexes.get(startIdx + i), labels);
        }
        HBox row = new HBox(cells);
        row.getStyleClass().add("hex-row");
        return row;
    }

    private static Label buildHex(String type, ResourceBundle labels) {
        // Display name is localized; the CSS class keeps the canonical hex type.
        Label hex = new Label(labels.getString("hex." + type));
        hex.getStyleClass().addAll("hex", String.format("hex-%s", type.toLowerCase()));
        return hex;
    }
}
