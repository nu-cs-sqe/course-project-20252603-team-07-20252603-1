package ui.controller;

import domain.model.DevelopmentCardHandler;
import domain.model.GameModel;
import domain.model.development_cards.DevelopmentCard;
import domain.model.development_cards.DevelopmentCardDeck;
import domain.model.exceptions.EmptyDeckException;
import domain.model.game_pieces.DiceHandler;
import domain.model.player.Player;
import domain.model.player.PlayerColor;
import domain.model.resources.Resource;


public class GameLoopController {

    public Player getCurrentPlayer(GameModel model) {
        return model.getCurrentPlayer();
    }

    public int getCurrentPlayerIndex(GameModel model) {
        return model.getCurrentPlayerIndex();
    }

    public int getResourceCount(GameModel model, PlayerColor color, Resource type) {
        Player playerOfInterest = model.getArbitraryPlayer(color);
        return playerOfInterest.getResourceCount(type);
    }

    public int rollDiceAndDistribute(GameModel model, DiceHandler roller) {
        int roll = roller.rollTwoDice();
        model.performTurn(roll);
        return roll;
    }

    public void endTurn(GameModel model) {
        model.endTurn();
    }

    public DevelopmentCard buyDevCard(GameModel model, DevelopmentCardDeck deck, DevelopmentCardHandler handler) throws EmptyDeckException {
        Player player = model.getCurrentPlayer();
        int round = model.getCurrentRound();
        
        return handler.buyDevelopmentCard(player, deck, round);
    }
}
