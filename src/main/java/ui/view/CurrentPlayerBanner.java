package ui.view;

import domain.model.player.Player;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;


/** Banner showing the current player's name and colour swatch. */
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"},
    justification = "UI classes intentionally share JavaFX nodes, controllers, and "
        + "models by reference")
public class CurrentPlayerBanner {

  private static final int SPACING_PX = 10;
  private static final String BANNER_CSS = "current-player-banner";
  private static final String SWATCH_CSS = "color-swatch";
  private static final String SWATCH_CLASS_PREFIX = "swatch-";

  private final HBox root;
  private final Region swatch;
  private final Label nameLabel;
  private final ResourceBundle labels;

  /** Constructs the current player banner. */
  public CurrentPlayerBanner(ResourceBundle labels) {
    this.labels = labels;
    this.swatch = new Region();
    this.swatch.getStyleClass().add(SWATCH_CSS);
    this.nameLabel = new Label();
    this.root = new HBox(swatch, nameLabel);
    this.root.getStyleClass().add(BANNER_CSS);
    this.root.setAlignment(Pos.CENTER);
    this.root.setSpacing(SPACING_PX);
  }

  /** Returns the root JavaFX node. */
  public Parent getRoot() {
    return root;
  }

  /** Updates the banner to show the given player. */
  public void update(Player player) {
    nameLabel.setText(MessageFormat.format(labels.getString("banner.turn"), player.getName()));
    swatch.getStyleClass().removeIf(c -> c.startsWith(SWATCH_CLASS_PREFIX));
    swatch.getStyleClass()
        .add(String.format("%s%s", SWATCH_CLASS_PREFIX, player.getColor().name().toLowerCase()));
  }
}
