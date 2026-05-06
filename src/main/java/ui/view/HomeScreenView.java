package ui.view;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class HomeScreenView {

    private final VBox root;

    public HomeScreenView(SetupNavigator navigator) {
        Label title = new Label("Welcome to Catan");
        title.getStyleClass().add("title");

        Label subtitle = new Label("Set up a new game");
        subtitle.getStyleClass().add("subtitle");

        Button startButton = new Button("Start Game");
        startButton.setOnAction(e -> navigator.goToPlayerCount());

        root = new VBox(title, subtitle, startButton);
        root.getStyleClass().add("screen");
    }

    public Parent getRoot() {
        return root;
    }
}
