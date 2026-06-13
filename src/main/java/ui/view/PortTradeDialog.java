package ui.view;

import domain.model.board.Port;
import domain.model.resources.Resource;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;

/**
 * Modal dialog for trading with the bank through one of the ports the current
 * player has access to. 2:1 ports lock the given resource to the port's own.
 */
public class PortTradeDialog {

  /**
   * The port trade the user composed.
   */
  public static final class Selection {
    private final Port port;
    private final Resource giving;
    private final Resource receiving;

    Selection(Port port, Resource giving, Resource receiving) {
      this.port = port;
      this.giving = giving;
      this.receiving = receiving;
    }

    public Port getPort() {
      return port;
    }

    public Resource getGiving() {
      return giving;
    }

    public Resource getReceiving() {
      return receiving;
    }
  }

  private static final int GRID_GAP_PX = 10;

  private final Dialog<Selection> dialog;

  public PortTradeDialog(ResourceBundle labels, List<Port> availablePorts) {
    ComboBox<Port> portPicker = buildPortPicker(labels, availablePorts);
    ComboBox<Resource> giveResource = DialogSupport.resourceComboBox(labels);
    ComboBox<Resource> getResource = DialogSupport.resourceComboBox(labels);
    getResource.getSelectionModel().select(1);

    // 2:1 ports only accept their own resource
    portPicker.valueProperty().addListener((obs, oldPort, newPort) -> {
      boolean fixedResource = newPort != null && newPort.getResource() != Resource.ANY;
      if (fixedResource) {
        giveResource.getSelectionModel().select(newPort.getResource());
      }
      giveResource.setDisable(fixedResource);
    });
    portPicker.getSelectionModel().selectFirst();

    GridPane grid = new GridPane();
    grid.setHgap(GRID_GAP_PX);
    grid.setVgap(GRID_GAP_PX);
    grid.add(new Label(labels.getString("port.title")), 0, 0);
    grid.add(portPicker, 1, 0);
    grid.add(new Label(labels.getString("trade.give")), 0, 1);
    grid.add(giveResource, 1, 1);
    grid.add(new Label(labels.getString("trade.get")), 0, 2);
    grid.add(getResource, 1, 2);

    dialog = new Dialog<>();
    dialog.setTitle(labels.getString("port.title"));
    dialog.getDialogPane().setContent(grid);

    ButtonType executeButton = new ButtonType(labels.getString("port.execute"),
        javafx.scene.control.ButtonBar.ButtonData.OK_DONE);
    dialog.getDialogPane().getButtonTypes().addAll(executeButton, ButtonType.CANCEL);
    dialog.setResultConverter(button -> button == executeButton
        ? new Selection(portPicker.getValue(), giveResource.getValue(), getResource.getValue())
        : null);
  }

  private static ComboBox<Port> buildPortPicker(ResourceBundle labels, List<Port> ports) {
    ComboBox<Port> picker = new ComboBox<>(FXCollections.observableArrayList(ports));
    picker.setConverter(new StringConverter<>() {
      @Override
      public String toString(Port port) {
        if (port == null) {
          return "";
        }
        String resourceName = port.getResource() == Resource.ANY
            ? labels.getString("port.any")
            : DialogSupport.resourceName(labels, port.getResource());
        return MessageFormat.format(labels.getString("port.ratio"),
            port.getTradeRatio(), resourceName);
      }

      @Override
      public Port fromString(String string) {
        throw new UnsupportedOperationException("Combo box is not editable");
      }
    });
    return picker;
  }

  public Optional<Selection> showAndCompose() {
    return dialog.showAndWait();
  }
}
