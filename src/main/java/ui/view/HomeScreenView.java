package ui.view;

import java.util.ResourceBundle;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import ui.ViewContext;

public class HomeScreenView {

    private final VBox root;

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
