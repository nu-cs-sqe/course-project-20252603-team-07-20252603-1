package ui.controller;

import domain.model.GameModel;
import domain.model.gamepieces.DiceHandler;
import domain.model.player.Player;
import domain.model.player.PlayerColor;
import domain.model.resources.Resource;

/** Controller for the main game loop, delegating game actions to the model. */
public class GameLoopController {

  /**
   * Returns the player whose turn it currently is.
   *
   * @param model the game model
   * @return the current player
   */
  public Player getCurrentPlayer(GameModel model) {
    return model.getCurrentPlayer();
  }

  /**
   * Returns the index of the current player in turn order.
   *
   * @param model the game model
   * @return the current player index
   */
  public int getCurrentPlayerIndex(GameModel model) {
    return model.getCurrentPlayerIndex();
  }

  /**
   * Returns the resource count for the specified player and resource type.
   *
   * @param model the game model
   * @param color the player color to query
   * @param type the resource type to count
   * @return the resource count
   */
  public int getResourceCount(GameModel model, PlayerColor color, Resource type) {
    Player playerOfInterest = model.getArbitraryPlayer(color);
    return playerOfInterest.getResourceCount(type);
  }

  /**
   * Rolls the dice, distributes resources, and returns the roll result.
   *
   * @param model the game model
   * @param roller the dice handler
   * @return the dice roll total
   */
  public int rollDiceAndDistribute(GameModel model, DiceHandler roller) {
    int roll = roller.rollTwoDice();
    model.performTurn(roll);
    return roll;
  }

  /**
   * Ends the current player's turn.
   *
   * @param model the game model
   */
  public void endTurn(GameModel model) {
    model.endTurn();
  }
}
