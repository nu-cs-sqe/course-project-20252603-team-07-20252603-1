package ui.view;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.util.Locale;
import java.util.ResourceBundle;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import ui.ViewContext;

/** The initial home screen view shown when the application starts. */
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"},
    justification = "UI classes intentionally share JavaFX nodes, controllers, and "
        + "models by reference")
public class HomeScreenView {

  private final VBox root;

  /** Constructs the home screen view. */
  public HomeScreenView(SetupNavigator navigator, ViewContext context) {
    ResourceBundle labels = context.labels();

    Label title = new Label(labels.getString("home.title"));
    title.getStyleClass().add("title");

    Label subtitle = new Label(labels.getString("home.subtitle"));
    subtitle.getStyleClass().add("subtitle");

        ComboBox<Locale> langBox = new ComboBox<>();
        langBox.getItems().addAll(Locale.ENGLISH, Locale.forLanguageTag("es"));
        langBox.setValue(Locale.getDefault());
        langBox.setConverter(new StringConverter<Locale>() {
            public String toString(Locale l) {
                return l == null ? "" : l.getDisplayLanguage(l);
            }
            public Locale fromString(String s) { return null; }
        });
        langBox.setOnAction(e -> navigator.changeLocale(langBox.getValue()));

        Button startButton = new Button(labels.getString("common.startGame"));
        startButton.setOnAction(e -> navigator.goToPlayerCount());

        root = new VBox(title, subtitle, langBox, startButton);
        root.getStyleClass().add("screen");
    }

  public Parent getRoot() {
    return root;
  }
}
