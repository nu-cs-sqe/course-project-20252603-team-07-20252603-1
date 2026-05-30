package ui.view;

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
        prompt.getStyleClass().add("prompt");

        countGroup = new ToggleGroup();
        RadioButton three = new RadioButton("3 Players");
        three.setUserData(3);
        three.setToggleGroup(countGroup);

        three.setSelected(true);

        RadioButton four = new RadioButton("4 Players");
        four.setUserData(4);
        four.setToggleGroup(countGroup);

        VBox options = new VBox(three, four);
        options.getStyleClass().add("option-list");

        Button back = new Button("Back");
        back.setOnAction(e -> navigator.goToHome());

        Button next = new Button("Next");
        next.setOnAction(e -> navigator.goToPlayerConfig(getSelectedCount()));

        HBox buttons = new HBox(back, next);
        buttons.getStyleClass().add("button-bar");

        root = new VBox(prompt, options, buttons);
        root.getStyleClass().add("screen");
    }

    public Parent getRoot() {
        return root;
    }

    private int getSelectedCount() {
        Toggle selected = countGroup.getSelectedToggle();
        return (Integer) selected.getUserData();
    }
}
