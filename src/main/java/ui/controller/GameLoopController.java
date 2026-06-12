package ui.controller;

import domain.model.GameModel;
import domain.model.board.Port;
import domain.model.game_pieces.DiceHandler;
import domain.model.player.Player;
import domain.model.player.PlayerColor;
import domain.model.player.TradeOffer;
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

    public void offerTrade(GameModel model, TradeOffer offer) {
        model.offerTrade(offer);
    }

    public void acceptTrade(GameModel model, TradeOffer offer, Player acceptingPlayer) {
        model.acceptTrade(offer, acceptingPlayer);
    }

    public void clearOffers(GameModel model) {
        model.clearOffers();
    }

    public void attemptPortTrade(GameModel model, Port port, Resource giving, Resource receiving) {
        model.attemptPortTrade(port, giving, receiving);
    }
}
