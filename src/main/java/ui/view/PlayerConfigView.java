package ui.view;

import domain.model.GameSetupModel;
import javafx.collections.FXCollections;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ui.controller.GameSetupController;
import ui.controller.PlayerAddResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class PlayerConfigView {

    private static final List<String> COLOR_PALETTE = List.of("Red", "Blue", "White", "Orange");

    private final VBox root;
    private final List<TextField> nameFields = new ArrayList<>();
    private final List<ComboBox<String>> colorBoxes = new ArrayList<>();
    private final Label statusLabel;
    private final Button startButton;
    private boolean refreshingColors = false;

    public PlayerConfigView(SetupNavigator navigator,
                            GameSetupController controller,
                            GameSetupModel model,
                            int playerCount) {
        Label header = new Label("Configure players");
        header.getStyleClass().add("prompt");

        VBox playerRows = new VBox();
        playerRows.getStyleClass().add("option-list");

        for (int i = 0; i < playerCount; i++) {
            Label rowLabel = new Label("Player " + (i + 1));
            TextField nameField = new TextField();
            nameField.setPromptText("Name");
            ComboBox<String> colorBox = new ComboBox<>();
            colorBox.setItems(FXCollections.observableArrayList(COLOR_PALETTE));
            colorBox.setPromptText("Color");
            colorBox.valueProperty().addListener((obs, oldV, newV) -> refreshColorChoices());

            nameFields.add(nameField);
            colorBoxes.add(colorBox);

            HBox row = new HBox(rowLabel, nameField, colorBox);
            row.getStyleClass().add("player-row");
            playerRows.getChildren().add(row);
        }

        statusLabel = new Label();
        statusLabel.getStyleClass().add("status");

        Button back = new Button("Back");
        back.setOnAction(e -> navigator.goToPlayerCount());

        startButton = new Button("Start Game");
        startButton.setOnAction(e -> handleStart(controller, model));

        HBox buttons = new HBox(back, startButton);
        buttons.getStyleClass().add("button-bar");

        root = new VBox(header, playerRows, statusLabel, buttons);
        root.getStyleClass().add("screen");
    }

    public Parent getRoot() {
        return root;
    }

    private void refreshColorChoices() {
        if (refreshingColors) return;
        refreshingColors = true;
        try {
            for (ComboBox<String> box : colorBoxes) {
                String current = box.getValue();
                Set<String> takenByOthers = colorBoxes.stream()
                        .filter(other -> other != box)
                        .map(ComboBox::getValue)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toSet());
                List<String> available = COLOR_PALETTE.stream()
                        .filter(color -> !takenByOthers.contains(color))
                        .collect(Collectors.toList());
                box.getItems().setAll(available);
                if (current != null && available.contains(current)) {
                    box.setValue(current);
                }
            }
        } finally {
            refreshingColors = false;
        }
    }

    private void handleStart(GameSetupController controller, GameSetupModel model) {
        controller.clearPlayers(model);
        int playerCount = nameFields.size();

        for (int i = 0; i < playerCount; i++) {
            String name = nameFields.get(i).getText();
            String color = colorBoxes.get(i).getValue();
            PlayerAddResult result = controller.addPlayerWithFullValidation(model, name, color);
            switch (result) {
                case SUCCESS:
                    break;
                case NAME_EMPTY:
                    showError("Player " + (i + 1) + " needs a name.");
                    return;
                case NAME_TAKEN:
                    showError("Player " + (i + 1) + "'s name is already used.");
                    return;
                case COLOR_EMPTY:
                    showError("Player " + (i + 1) + " needs a color.");
                    return;
                case COLOR_TAKEN:
                    showError("Player " + (i + 1) + "'s color is already taken.");
                    return;
            }
        }
        controller.initializeBoard(model);
        controller.initializeResourceDeck(model);
        controller.initializeDevelopmentCardDeck(model);
        controller.determineTurnOrder(model);

        showSuccess("Game ready. " + playerCount + " players configured.");
        startButton.setDisable(true);
    }

    private void showError(String message) {
        statusLabel.getStyleClass().setAll("status", "error");
        statusLabel.setText(message);
    }

    private void showSuccess(String message) {
        statusLabel.getStyleClass().setAll("status", "success");
        statusLabel.setText(message);
    }
}
