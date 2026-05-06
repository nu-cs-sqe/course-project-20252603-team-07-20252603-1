package ui.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class HomeScreenView {

    private final VBox root;

    public HomeScreenView(SetupNavigator navigator) {
        Label title = new Label("Welcome to Catan");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        Label subtitle = new Label("Set up a new game");
        subtitle.setStyle("-fx-font-size: 14px;");

        Button startButton = new Button("Start Game");
        startButton.setOnAction(e -> navigator.goToPlayerCount());

        root = new VBox(20, title, subtitle, startButton);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
    }

    public Parent getRoot() {
        return root;
    }
}
