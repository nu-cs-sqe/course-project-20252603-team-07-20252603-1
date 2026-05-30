package ui.controller;

import domain.model.DiceRoller;
import domain.model.GameModel;
import domain.model.player.Player;
import domain.model.player.PlayerState;
import domain.model.resources.ResourceDeck;
import domain.model.resources.ResourceType;

public class GameLoopController {

    public Player getCurrentPlayer(GameModel model) {
        return model.getCurrentPlayer();
    }

    public int getCurrentPlayerIndex(GameModel model) {
        return model.getCurrentPlayerIndex();
    }

    public int getResourceCount(GameModel model, int playerIndex, ResourceType type) {
        PlayerState state = model.getPlayerState(playerIndex);
        return state.getResourceCount(type);
    }

    public int rollDiceAndDistribute(GameModel model, DiceRoller roller, ResourceDeck deck) {
        int roll = roller.roll();
        model.performTurn(() -> roll, deck);
        return roll;
    }

    public void endTurn(GameModel model) {
        model.advanceToNextPlayer();
    }
}
