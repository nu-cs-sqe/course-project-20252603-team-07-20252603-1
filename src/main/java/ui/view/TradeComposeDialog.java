package ui.view;

import domain.model.player.Player;
import domain.model.player.TradeOffer;
import domain.model.resources.Resource;
import domain.model.resources.ResourceQuantity;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.GridPane;

/**
 * Modal dialog where the current player composes a trade offer: a resource
 * quantity to give and one to receive. Offers are untargeted; every other
 * player will be shown the offer in {@link TradeRespondDialog}.
 */
public class TradeComposeDialog {

  private static final int GRID_GAP_PX = 10;
  private static final int MIN_QUANTITY = 1;
  private static final int MAX_QUANTITY = 19;
  private static final String STATUS_CSS = "dialog-status";
  private static final String ERROR_CSS = "error";

  private final Dialog<TradeOffer> dialog;

  public TradeComposeDialog(ResourceBundle labels, Player offeringPlayer) {
    ComboBox<Resource> giveResource = DialogSupport.resourceComboBox(labels);
    Spinner<Integer> giveQuantity = new Spinner<>(MIN_QUANTITY, MAX_QUANTITY, MIN_QUANTITY);
    ComboBox<Resource> getResource = DialogSupport.resourceComboBox(labels);
    getResource.getSelectionModel().select(1);
    Spinner<Integer> getQuantity = new Spinner<>(MIN_QUANTITY, MAX_QUANTITY, MIN_QUANTITY);

    Label statusLabel = new Label();
    statusLabel.getStyleClass().addAll(STATUS_CSS, ERROR_CSS);

    GridPane grid = new GridPane();
    grid.setHgap(GRID_GAP_PX);
    grid.setVgap(GRID_GAP_PX);
    grid.add(new Label(labels.getString("trade.give")), 0, 0);
    grid.add(giveResource, 1, 0);
    grid.add(giveQuantity, 2, 0);
    grid.add(new Label(labels.getString("trade.get")), 0, 1);
    grid.add(getResource, 1, 1);
    grid.add(getQuantity, 2, 1);
    grid.add(statusLabel, 0, 2, 3, 1);

    dialog = new Dialog<>();
    dialog.setTitle(labels.getString("trade.title"));
    dialog.getDialogPane().setContent(grid);

    ButtonType offerButton = new ButtonType(labels.getString("trade.offer"),
        javafx.scene.control.ButtonBar.ButtonData.OK_DONE);
    dialog.getDialogPane().getButtonTypes().addAll(offerButton, ButtonType.CANCEL);

    // keep the dialog open when the composed offer is invalid
    Button offerControl = (Button) dialog.getDialogPane().lookupButton(offerButton);
    offerControl.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
      try {
        TradeOffer offer = TradeOffer.create(offeringPlayer,
            ResourceQuantity.create(giveResource.getValue(), giveQuantity.getValue()),
            ResourceQuantity.create(getResource.getValue(), getQuantity.getValue()));
        dialog.setResult(offer);
      } catch (IllegalArgumentException e) {
        statusLabel.setText(e.getMessage());
        event.consume();
      }
    });
    dialog.setResultConverter(button -> button == offerButton ? dialog.getResult() : null);
  }

  public Optional<TradeOffer> showAndCompose() {
    return dialog.showAndWait();
  }
}
