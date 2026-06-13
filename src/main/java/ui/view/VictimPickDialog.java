package ui.view;

import domain.model.player.Player;
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
 * Modal dialog asking which player to steal from after moving the robber.
 * Returns the chosen victim, or empty when the user opts not to steal.
 * The dialog offers no cancel button: once the robber is moved, a choice
 * (possibly "no victim") is mandatory.
 */
public class VictimPickDialog {

  private final Dialog<Player> dialog;

  /** Constructs the victim pick dialog with the list of candidate players. */
  public VictimPickDialog(ResourceBundle labels, List<Player> candidates) {
    dialog = new Dialog<>();
    dialog.setTitle(labels.getString("robber.title"));
    dialog.getDialogPane().setContent(new Label(labels.getString("robber.chooseVictim")));

    Map<ButtonType, Player> buttonToVictim = new LinkedHashMap<>();
    for (Player candidate : candidates) {
      ButtonType button = new ButtonType(candidate.getName(), ButtonBar.ButtonData.OK_DONE);
      buttonToVictim.put(button, candidate);
      dialog.getDialogPane().getButtonTypes().add(button);
    }
    ButtonType noVictim = new ButtonType(
        labels.getString("robber.noVictim"), ButtonBar.ButtonData.CANCEL_CLOSE);
    dialog.getDialogPane().getButtonTypes().add(noVictim);

    dialog.setResultConverter(buttonToVictim::get);
  }

  /** Shows the dialog and returns the chosen victim, or empty if none selected. */
  public Optional<Player> showAndPick() {
    return dialog.showAndWait();
  }
}
