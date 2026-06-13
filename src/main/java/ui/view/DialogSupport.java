package ui.view;

import domain.model.resources.Resource;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

/**
 * Shared helpers for the game's modal dialogs.
 */
final class DialogSupport {

  static final Resource[] TRADEABLE_RESOURCES = {
      Resource.LUMBER, Resource.BRICK, Resource.WOOL, Resource.GRAIN, Resource.ORE
  };

  private DialogSupport() {
  }

  static String resourceName(ResourceBundle labels, Resource resource) {
    return labels.getString("resource." + resource.name());
  }

  static ComboBox<Resource> resourceComboBox(ResourceBundle labels) {
    ComboBox<Resource> comboBox = new ComboBox<>(
        FXCollections.observableArrayList(TRADEABLE_RESOURCES));
    comboBox.setConverter(new StringConverter<>() {
      @Override
      public String toString(Resource resource) {
        return resource == null ? "" : resourceName(labels, resource);
      }

      @Override
      public Resource fromString(String string) {
        throw new UnsupportedOperationException("Combo box is not editable");
      }
    });
    comboBox.getSelectionModel().selectFirst();
    return comboBox;
  }
}
