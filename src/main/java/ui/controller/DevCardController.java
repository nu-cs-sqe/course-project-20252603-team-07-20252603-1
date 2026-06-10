package ui.controller;

import domain.model.DevelopmentCardHandler;
import domain.model.GameModel;
import domain.model.development_cards.DevelopmentCard;
import domain.model.development_cards.DevelopmentCardDeck;
import domain.model.exceptions.EmptyDeckException;
import domain.model.game_pieces.Robber;
import domain.model.player.Player;

public class DevCardController {

    private final DevelopmentCardHandler handler;

    public DevCardController(DevelopmentCardHandler handler) {
        this.handler = handler;
    }

    public DevelopmentCard buyDevelopmentCard(GameModel model, DevelopmentCardDeck deck) throws EmptyDeckException {
        Player currentPlayer = model.getCurrentPlayer();
        int currentRound = model.getCurrentRound();
        return handler.buyDevelopmentCard(currentPlayer, deck, currentRound);
    }

    public void playKnightCard(GameModel model, DevelopmentCard card, Robber robber, int targetHexId, Player victim) {
        Player currentPlayer = model.getCurrentPlayer();
        int currentRound = model.getCurrentRound();
        handler.playKnightCard(currentPlayer, card, currentRound, robber, targetHexId, victim);
    }

}
