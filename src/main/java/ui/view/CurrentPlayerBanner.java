package ui.view;

import domain.model.player.Player;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;


public class CurrentPlayerBanner {

    private static final int SPACING_PX = 10;
    private static final String BANNER_CSS = "current-player-banner";
    private static final String SWATCH_CSS = "color-swatch";
    private static final String SWATCH_CLASS_PREFIX = "swatch-";

    private final HBox root;
    private final Region swatch;
    private final Label nameLabel;

    public CurrentPlayerBanner() {
        this.swatch = new Region();
        this.swatch.getStyleClass().add(SWATCH_CSS);
        this.nameLabel = new Label();
        this.root = new HBox(swatch, nameLabel);
        this.root.getStyleClass().add(BANNER_CSS);
        this.root.setAlignment(Pos.CENTER);
        this.root.setSpacing(SPACING_PX);
    }

    public Parent getRoot() {
        return root;
    }

    public void update(Player player) {
        nameLabel.setText(String.format("%s's turn", player.getName()));
        swatch.getStyleClass().removeIf(c -> c.startsWith(SWATCH_CLASS_PREFIX));
        swatch.getStyleClass().add(String.format("%s%s", SWATCH_CLASS_PREFIX, player.getColor().toString().toLowerCase()));
    }
}
