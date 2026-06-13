package ui.view;

import domain.model.GameSetupModel;
import domain.model.player.PlayerColor;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import ui.ViewContext;
import ui.controller.GameSetupController;
import ui.controller.PlayerAddResult;

/** Screen for entering each player's name and color before the game starts. */
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"},
    justification = "UI classes share JavaFX nodes, controllers, and models by reference")
public class PlayerConfigView {

  private static final List<PlayerColor> COLOR_PALETTE =
      List.of(PlayerColor.RED, PlayerColor.BLUE, PlayerColor.WHITE, PlayerColor.ORANGE);

  private final VBox root;
  private final List<TextField> nameFields = new ArrayList<>();
  private final List<ComboBox<PlayerColor>> colorBoxes = new ArrayList<>();
  private final Label statusLabel;
  private final ResourceBundle labels;
  private boolean refreshingColors = false;

  /**
   * Creates the player-configuration screen.
   *
   * @param navigator the navigator for screen transitions
   * @param context the shared view context
   * @param model the game-setup model to populate
   * @param playerCount the number of players to configure
   */
  public PlayerConfigView(SetupNavigator navigator,
                          ViewContext context,
                          GameSetupModel model,
                          int playerCount) {
    this.labels = context.labels();

    Label header = new Label(labels.getString("playerConfig.header"));
    header.getStyleClass().add("prompt");

    String playerLabelPattern = labels.getString("playerConfig.playerLabel");

    VBox playerRows = new VBox();
    playerRows.getStyleClass().add("option-list");

    for (int i = 0; i < playerCount; i++) {
      TextField nameField = new TextField();
      nameField.setPromptText(labels.getString("playerConfig.namePrompt"));
      ComboBox<PlayerColor> colorBox = new ComboBox<>();
      colorBox.setItems(FXCollections.observableArrayList(COLOR_PALETTE));
      // Items stay the canonical color names (used by the model and CSS swatch
      // classes); the converter only changes how they are displayed.
      colorBox.setConverter(colorConverter());
      colorBox.setPromptText(labels.getString("playerConfig.colorPrompt"));
      colorBox.valueProperty().addListener((obs, oldV, newV) -> refreshColorChoices());

      nameFields.add(nameField);
      colorBoxes.add(colorBox);

      Label rowLabel = new Label(MessageFormat.format(playerLabelPattern, i + 1));
      HBox row = new HBox(rowLabel, nameField, colorBox);
      row.getStyleClass().add("player-row");
      playerRows.getChildren().add(row);
    }

    statusLabel = new Label();
    statusLabel.getStyleClass().add("status");

    Button back = new Button(labels.getString("common.back"));
    back.setOnAction(e -> navigator.goToPlayerCount());

    Button startButton = new Button(labels.getString("common.startGame"));
    startButton.setOnAction(e -> handleStart(navigator, context.setup(), model));

    HBox buttons = new HBox(back, startButton);
    buttons.getStyleClass().add("button-bar");

    root = new VBox(header, playerRows, statusLabel, buttons);
    root.getStyleClass().add("screen");
  }

  public Parent getRoot() {
    return root;
  }

  private void refreshColorChoices() {
    if (refreshingColors) {
      return;
    }
    refreshingColors = true;
    try {
      for (ComboBox<PlayerColor> box : colorBoxes) {
        PlayerColor current = box.getValue();
        Set<PlayerColor> takenByOthers = colorBoxes.stream()
            .filter(other -> other != box)
            .map(ComboBox::getValue)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
        List<PlayerColor> available = COLOR_PALETTE.stream()
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

  private void handleStart(SetupNavigator navigator, GameSetupController controller,
                           GameSetupModel model) {
    controller.clearPlayers(model);
    int playerCount = nameFields.size();

    for (int i = 0; i < playerCount; i++) {
      String name = nameFields.get(i).getText();
      PlayerColor color = colorBoxes.get(i).getValue();
      PlayerAddResult result = controller.addPlayerWithFullValidation(model, name, color);
      switch (result) {
        case SUCCESS:
          break;
        case NAME_EMPTY:
          showError("playerConfig.error.nameEmpty", i + 1);
          return;
        case NAME_TAKEN:
          showError("playerConfig.error.nameTaken", i + 1);
          return;
        case COLOR_EMPTY:
          showError("playerConfig.error.colorEmpty", i + 1);
          return;
        case COLOR_TAKEN:
          showError("playerConfig.error.colorTaken", i + 1);
          return;
        default:
          break;
      }
    }
    controller.initializeResourceDeck(model);
    controller.initializeDevelopmentCardDeck(model);
    controller.determineTurnOrder(model);

    navigator.goToSetupSummary();
  }

  private void showError(String key, Object... args) {
    statusLabel.getStyleClass().setAll("status", "error");
    statusLabel.setText(MessageFormat.format(labels.getString(key), args));
  }

  /**
   * Converts between canonical color identifiers (stored in the model and used for
   * CSS swatch classes) and their localized display names.
   */
  private StringConverter<PlayerColor> colorConverter() {
    return new StringConverter<PlayerColor>() {
      @Override
      public String toString(PlayerColor color) {
        return color == null ? null : labels.getString("color." + color.name());
      }

      @Override
      public PlayerColor fromString(String displayName) {
        if (displayName == null) {
          return null;
        }
        return COLOR_PALETTE.stream()
            .filter(color -> labels.getString("color." + color.name()).equals(displayName))
            .findFirst()
            .orElse(null);
      }
    };
  }
}
