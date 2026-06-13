package ui.view;

import domain.model.player.Player;
import domain.model.player.TradeOffer;
import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;

/**
 * Modal dialog presenting a trade offer to the other players (hot-seat: all on
 * one screen). Returns the accepting player, or empty when everyone declines.
 */
public class TradeRespondDialog {

  private final Dialog<Player> dialog;

  /**
   * Creates the trade-respond dialog.
   *
   * @param labels the localized label bundle
   * @param offeringPlayer the player who made the offer
   * @param offer the trade offer to display
   * @param otherPlayers the players who may accept
   */
  public TradeRespondDialog(ResourceBundle labels, Player offeringPlayer,
                            TradeOffer offer, List<Player> otherPlayers) {
    dialog = new Dialog<>();
    dialog.setTitle(labels.getString("trade.title"));
    dialog.getDialogPane().setContent(new Label(buildSummary(labels, offeringPlayer, offer)));

    Map<ButtonType, Player> buttonToPlayer = new LinkedHashMap<>();
    for (Player player : otherPlayers) {
      ButtonType button = new ButtonType(
          MessageFormat.format(labels.getString("trade.acceptAs"), player.getName()),
          ButtonBar.ButtonData.OK_DONE);
      buttonToPlayer.put(button, player);
      dialog.getDialogPane().getButtonTypes().add(button);
    }
    ButtonType declineAll = new ButtonType(labels.getString("trade.declineAll"),
        ButtonBar.ButtonData.CANCEL_CLOSE);
    dialog.getDialogPane().getButtonTypes().add(declineAll);

    dialog.setResultConverter(buttonToPlayer::get);
  }

  private static String buildSummary(ResourceBundle labels, Player offeringPlayer,
                                     TradeOffer offer) {
    return MessageFormat.format(labels.getString("trade.summary"),
        offeringPlayer.getName(),
        offer.getGiving().getQuantity(),
        DialogSupport.resourceName(labels, offer.getGiving().getResource()),
        offer.getReceiving().getQuantity(),
        DialogSupport.resourceName(labels, offer.getReceiving().getResource()));
  }

  /** Shows the dialog and returns the accepting player, or empty if all decline. */
  public Optional<Player> showAndRespond() {
    return dialog.showAndWait();
  }
}
