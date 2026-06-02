package ui.controller;

import domain.model.GameModel;
import domain.model.game_pieces.DiceHandler;
import domain.model.player.Player;
import domain.model.player.PlayerState;
import domain.model.resources.Resource;


public class GameLoopController {

    public Player getCurrentPlayer(GameModel model) {
        return model.getCurrentPlayer();
    }

    public int getCurrentPlayerIndex(GameModel model) {
        return model.getCurrentPlayerIndex();
    }

    public int getResourceCount(GameModel model, int playerIndex, Resource type) {
        PlayerState state = model.getPlayerState(playerIndex);
        return state.getResourceCount(type);
    }

    public int rollDiceAndDistribute(GameModel model, DiceHandler roller) {
        int roll = roller.rollTwoDice();
        model.performTurn(roll);
        return roll;
    }

    public void endTurn(GameModel model) {
        model.advanceToNextPlayer();
    }
}
