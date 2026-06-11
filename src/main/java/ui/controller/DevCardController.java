package ui.controller;

import domain.model.DevelopmentCardHandler;
import domain.model.GameModel;
import domain.model.development_cards.DevelopmentCard;
import domain.model.development_cards.DevelopmentCardDeck;
import domain.model.exceptions.EmptyDeckException;
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

    public void playRoadBuildingCard(GameModel model, DevelopmentCard card, int road1Node1, int road1Node2, Integer road2Node1, Integer road2Node2) {
        Player currentPlayer = model.getCurrentPlayer();
        int currentRound = model.getCurrentRound();

        handler.playRoadBuildingCard(currentPlayer, card, currentRound, model, road1Node1, road1Node2, road2Node1, road2Node2);
    }

    public void playYearOfPlentyCard(GameModel model, DevelopmentCard card, Resource resource1, Resource resource2) {
        Player currentPlayer = model.getCurrentPlayer();
        int currentRound = model.getCurrentRound();
        
        handler.playYearOfPlentyCard(currentPlayer, card, currentRound, resource1, resource2);
    }

    public int getVictoryPointCount(GameModel model) {
        Player currentPlayer = model.getCurrentPlayer();
        
        return handler.countVictoryPointCards(currentPlayer.getDevelopmentCards());
    }

}
