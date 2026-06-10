package ui.controller;

import domain.model.DevelopmentCardHandler;
import domain.model.GameModel;
import domain.model.development_cards.DevelopmentCard;
import domain.model.development_cards.DevelopmentCardDeck;
import domain.model.exceptions.EmptyDeckException;
import domain.model.board.Edge;
import domain.model.game_pieces.Robber;
import domain.model.player.Player;
import domain.model.resources.Resource;

import java.util.List;

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

    public void playMonopolyCard(GameModel model, DevelopmentCard card, Resource resource) {
        Player currentPlayer = model.getCurrentPlayer();
        int currentRound = model.getCurrentRound();
        
        List<Player> otherPlayers = model.getOtherPlayers();
        
        handler.playMonopolyCard(currentPlayer, card, currentRound, resource, otherPlayers);
    }

    public void playRoadBuildingCard(GameModel model, DevelopmentCard card, Edge edge1, Edge edge2) {
        Player currentPlayer = model.getCurrentPlayer();
        int currentRound = model.getCurrentRound();
        
        handler.playRoadBuildingCard(currentPlayer, card, currentRound, edge1, edge2);
    }

    public void playYearOfPlentyCard(GameModel model, DevelopmentCard card, Resource resource1, Resource resource2) {
        Player currentPlayer = model.getCurrentPlayer();
        int currentRound = model.getCurrentRound();
        
        handler.playYearOfPlentyCard(currentPlayer, card, currentRound, resource1, resource2);
    }

}
