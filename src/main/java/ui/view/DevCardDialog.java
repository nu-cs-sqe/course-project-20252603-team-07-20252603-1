package ui.view;

import domain.model.GameModel;
import domain.model.development_cards.DevelopmentCard;
import domain.model.development_cards.DevelopmentCardDeck;
import domain.model.development_cards.DevelopmentCardType;
import domain.model.exceptions.EmptyDeckException;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.text.MessageFormat;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import ui.ViewContext;

/**
 * Modal dialog showing the current player's development card hand. The player
 * can buy a card (handled inside the dialog) or choose a card to play; playing
 * closes the dialog and returns the chosen card so the parent view can run
 * that card's pick flow on the board.
 */
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"},
        justification = "UI classes intentionally share JavaFX nodes, controllers, and models by reference")
public class DevCardDialog {

    private static final int SPACING_PX = 8;
    private static final int ROW_SPACING_PX = 12;
    private static final String ROW_CSS = "dev-card-row";
    private static final String UNPLAYABLE_CSS = "dev-card-unplayable";
    private static final String STATUS_CSS = "dialog-status";
    private static final String ERROR_CSS = "error";

    private final ViewContext context;
    private final GameModel model;
    private final DevelopmentCardDeck deck;
    private final ResourceBundle labels;
    private final Dialog<DevelopmentCard> dialog;
    private final VBox handBox;
    private final Label vpLabel;
    private final Label statusLabel;

    public DevCardDialog(ViewContext context, GameModel model, DevelopmentCardDeck deck) {
        this.context = context;
        this.model = model;
        this.deck = deck;
        this.labels = context.labels();

        this.handBox = new VBox();
        this.handBox.setSpacing(SPACING_PX);
        this.vpLabel = new Label();
        this.statusLabel = new Label();
        this.statusLabel.getStyleClass().add(STATUS_CSS);

        this.dialog = buildDialog();
        renderHand();
    }

    public Optional<DevelopmentCard> showAndPlay() {
        return dialog.showAndWait();
    }

    private Dialog<DevelopmentCard> buildDialog() {
        Dialog<DevelopmentCard> built = new Dialog<>();
        built.setTitle(labels.getString("devcard.title"));

        Button buyButton = new Button(labels.getString("devcard.buy"));
        buyButton.setOnAction(e -> onBuy());

        VBox content = new VBox(handBox, buyButton, vpLabel, statusLabel);
        content.setSpacing(ROW_SPACING_PX);
        built.getDialogPane().setContent(content);
        built.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        built.setResultConverter(button -> null);
        return built;
    }

    private void onBuy() {
        try {
            context.devCards().buyDevelopmentCard(model, deck);
            clearStatus();
        } catch (EmptyDeckException e) {
            showError(labels.getString("error.emptyDeck"));
        } catch (RuntimeException e) {
            showError(e.getMessage());
        }
        renderHand();
    }

    private void renderHand() {
        handBox.getChildren().clear();
        int currentRound = context.loop().getCurrentRound(model);
        for (DevelopmentCard card : context.loop().getCurrentPlayer(model).getDevelopmentCards()) {
            handBox.getChildren().add(buildCardRow(card, currentRound));
        }
        vpLabel.setText(MessageFormat.format(labels.getString("devcard.vpCount"),
                context.devCards().getVictoryPointCount(model)));
    }

    private HBox buildCardRow(DevelopmentCard card, int currentRound) {
        Label nameLabel = new Label(labels.getString("devcard." + card.getType().name()));
        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        HBox row = new HBox(nameLabel, spacer);
        row.setSpacing(ROW_SPACING_PX);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add(ROW_CSS);

        boolean playable = card.getType() != DevelopmentCardType.VICTORY_POINT
                && card.isPlayable(currentRound);
        if (playable) {
            Button playButton = new Button(labels.getString("devcard.play"));
            playButton.setOnAction(e -> {
                dialog.setResult(card);
                dialog.close();
            });
            row.getChildren().add(playButton);
        } else {
            Label unplayable = new Label(labels.getString("devcard.notPlayable"));
            unplayable.getStyleClass().add(UNPLAYABLE_CSS);
            row.getChildren().add(unplayable);
        }
        return row;
    }

    private void showError(String message) {
        statusLabel.setText(message);
        if (!statusLabel.getStyleClass().contains(ERROR_CSS)) {
            statusLabel.getStyleClass().add(ERROR_CSS);
        }
    }

    private void clearStatus() {
        statusLabel.setText("");
        statusLabel.getStyleClass().remove(ERROR_CSS);
    }
}
