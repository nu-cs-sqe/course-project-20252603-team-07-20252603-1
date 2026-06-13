package ui.controller;

import domain.model.DevelopmentCardHandler;
import domain.model.GameModel;
import domain.model.GamePhase;
import domain.model.board.BoardHandler;
import domain.model.board.Port;
import domain.model.development_cards.DevelopmentCard;
import domain.model.development_cards.DevelopmentCardDeck;
import domain.model.exceptions.EmptyDeckException;
import domain.model.game_pieces.DiceHandler;
import domain.model.player.Player;
import domain.model.player.PlayerColor;
import domain.model.player.TradeOffer;
import domain.model.resources.Resource;

import java.util.List;
import java.util.Set;


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

    public DevelopmentCard buyDevCard(GameModel model, DevelopmentCardDeck deck, DevelopmentCardHandler handler) throws EmptyDeckException {
        Player player = model.getCurrentPlayer();
        int round = model.getCurrentRound();

        return handler.buyDevelopmentCard(player, deck, round);
    }

    public GamePhase getCurrentPhase(GameModel model) {
        return model.getCurrentPhase();
    }

    public void enterSetupPhase(GameModel model) {
        model.enterSetupPhase();
    }

    public void completeSetupPhase(GameModel model) {
        model.completeSetupPhase();
    }

    public void setCurrentPlayer(GameModel model, int playerIndex) {
        model.setCurrentPlayerIndex(playerIndex);
        model.setCurrentPlayerColor(model.getTurnOrder().get(playerIndex).getColor());
    }

    public int getCurrentRound(GameModel model) {
        return model.getCurrentRound();
    }

    public List<Player> getOtherPlayers(GameModel model) {
        return model.getOtherPlayers();
    }

    public void attemptBuildSettlement(GameModel model, int nodeId) {
        model.attemptBuildSettlement(nodeId);
    }

    public void attemptBuildRoad(GameModel model, int nodeId1, int nodeId2) {
        model.attemptBuildRoad(nodeId1, nodeId2);
    }

    public void attemptBuildCity(GameModel model, int nodeId) {
        model.attemptBuildCity(nodeId);
    }

    public void moveRobberAndSteal(GameModel model, int targetHexId, Player victim) {
        model.moveRobberAndSteal(targetHexId, victim);
    }

    public Set<Player> getPlayersOnHex(BoardHandler board, int hexId) {
        return board.getPlayersOnHex(hexId);
    }

    public List<Port> getAvailablePorts(BoardHandler board, Player player) {
        return board.getAvailablePorts(player);
    }
}
