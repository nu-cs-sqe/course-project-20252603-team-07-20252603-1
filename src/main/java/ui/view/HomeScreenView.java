package ui.view;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.ResourceBundle;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import ui.ViewContext;

/** Home screen showing the title and a button to start game setup. */
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"},
    justification = "UI classes share JavaFX nodes, controllers, and models by reference")
public class HomeScreenView {

  private final VBox root;

  /**
   * Creates the home screen.
   *
   * @param navigator the navigator for screen transitions
   * @param context the shared view context
   */
  public HomeScreenView(SetupNavigator navigator, ViewContext context) {
    ResourceBundle labels = context.labels();

    Label title = new Label(labels.getString("home.title"));
    title.getStyleClass().add("title");

    Label subtitle = new Label(labels.getString("home.subtitle"));
    subtitle.getStyleClass().add("subtitle");

    Button startButton = new Button(labels.getString("common.startGame"));
    startButton.setOnAction(e -> navigator.goToPlayerCount());

    root = new VBox(title, subtitle, startButton);
    root.getStyleClass().add("screen");
  }

  public Parent getRoot() {
    return root;
  }
}
