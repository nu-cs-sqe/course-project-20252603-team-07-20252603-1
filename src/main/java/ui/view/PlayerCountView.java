package ui.view;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ui.ViewContext;

@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"},
    justification = "UI classes intentionally share JavaFX nodes, controllers, and models by reference")
public class PlayerCountView {

  private final VBox root;
  private final ToggleGroup countGroup;

  public PlayerCountView(SetupNavigator navigator, ViewContext context) {
    ResourceBundle labels = context.labels();

    Label prompt = new Label(labels.getString("playerCount.prompt"));
    prompt.getStyleClass().add("prompt");

    String optionPattern = labels.getString("playerCount.option");

    countGroup = new ToggleGroup();
    RadioButton three = new RadioButton(MessageFormat.format(optionPattern, 3));
    three.setUserData(3);
    three.setToggleGroup(countGroup);

    three.setSelected(true);

    RadioButton four = new RadioButton(MessageFormat.format(optionPattern, 4));
    four.setUserData(4);
    four.setToggleGroup(countGroup);

    VBox options = new VBox(three, four);
    options.getStyleClass().add("option-list");

    Button back = new Button(labels.getString("common.back"));
    back.setOnAction(e -> navigator.goToHome());

    Button next = new Button(labels.getString("common.next"));
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
