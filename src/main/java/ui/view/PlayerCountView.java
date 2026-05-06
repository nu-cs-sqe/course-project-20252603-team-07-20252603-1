package ui.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PlayerCountView {

    private final VBox root;
    private final ToggleGroup countGroup;

    public PlayerCountView(SetupNavigator navigator) {
        Label prompt = new Label("How many players?");
        prompt.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        countGroup = new ToggleGroup();
        RadioButton three = new RadioButton("3 Players");
        three.setUserData(3);
        three.setToggleGroup(countGroup);
        three.setSelected(true);
        RadioButton four = new RadioButton("4 Players");
        four.setUserData(4);
        four.setToggleGroup(countGroup);

        VBox options = new VBox(10, three, four);
        options.setAlignment(Pos.CENTER);

        Button back = new Button("Back");
        back.setOnAction(e -> navigator.goToHome());

        Button next = new Button("Next");
        next.setOnAction(e -> {});

        HBox buttons = new HBox(10, back, next);
        buttons.setAlignment(Pos.CENTER);

        root = new VBox(20, prompt, options, buttons);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
    }

    public Parent getRoot() {
        return root;
    }

    private int getSelectedCount() {
        Toggle selected = countGroup.getSelectedToggle();
        return (Integer) selected.getUserData();
    }
}
