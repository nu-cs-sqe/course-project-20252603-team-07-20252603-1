package ui.view;

import domain.model.resources.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.layout.VBox;

/**
 * Modal dialog asking the user to pick one or more resources, used by the
 * Monopoly (1 resource) and Year of Plenty (2 resources) dev card flows.
 */
public class ResourcePickDialog {

  private static final int SPACING_PX = 10;

  private final Dialog<List<Resource>> dialog;

  /** Constructs a resource pick dialog for the given number of resources to pick. */
  public ResourcePickDialog(ResourceBundle labels, int resourceCount) {
    List<ComboBox<Resource>> pickers = new ArrayList<>();
    VBox content = new VBox();
    content.setSpacing(SPACING_PX);
    for (int i = 0; i < resourceCount; i++) {
      ComboBox<Resource> picker = DialogSupport.resourceComboBox(labels);
      pickers.add(picker);
      content.getChildren().add(picker);
    }

    dialog = new Dialog<>();
    dialog.setTitle(labels.getString(resourceCount > 1
        ? "devcard.pickTwoResources" : "devcard.pickResource"));
    dialog.getDialogPane().setContent(content);
    dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
    dialog.setResultConverter(button -> {
      if (button != ButtonType.OK) {
        return null;
      }
      List<Resource> picked = new ArrayList<>();
      for (ComboBox<Resource> picker : pickers) {
        picked.add(picker.getValue());
      }
      return picked;
    });
  }

  /** Shows the dialog and returns the picked resources, or empty if cancelled. */
  public Optional<List<Resource>> showAndPick() {
    return dialog.showAndWait();
  }
}
