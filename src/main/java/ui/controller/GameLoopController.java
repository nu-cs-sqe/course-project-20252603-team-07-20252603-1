package ui.controller;

import domain.model.DevelopmentCardHandler;
import domain.model.GameModel;
import domain.model.GamePhase;
import domain.model.board.BoardHandler;
import domain.model.board.Port;
import domain.model.developmentcards.DevelopmentCard;
import domain.model.developmentcards.DevelopmentCardDeck;
import domain.model.exceptions.EmptyDeckException;
import domain.model.gamepieces.DiceHandler;
import domain.model.player.Player;
import domain.model.player.PlayerColor;
import domain.model.player.TradeOffer;
import domain.model.resources.Resource;
import java.util.List;
import java.util.Set;

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

  /**
   * Offers a trade on behalf of the current player.
   *
   * @param model the game model
   * @param offer the trade offer to make
   */
  public void offerTrade(GameModel model, TradeOffer offer) {
    model.offerTrade(offer);
  }

  /**
   * Accepts a trade offer on behalf of the accepting player.
   *
   * @param model the game model
   * @param offer the trade offer being accepted
   * @param acceptingPlayer the player accepting the trade
   */
  public void acceptTrade(GameModel model, TradeOffer offer, Player acceptingPlayer) {
    model.acceptTrade(offer, acceptingPlayer);
  }

  /**
   * Clears all active trade offers.
   *
   * @param model the game model
   */
  public void clearOffers(GameModel model) {
    model.clearOffers();
  }

  /**
   * Attempts a port trade for the current player.
   *
   * @param model the game model
   * @param port the port to trade at
   * @param giving the resource being given
   * @param receiving the resource being received
   */
  public void attemptPortTrade(
          GameModel model, Port port, Resource giving, Resource receiving) {
    model.attemptPortTrade(port, giving, receiving);
  }

  /**
   * Purchases a development card for the current player.
   *
   * @param model the game model
   * @param deck the development card deck to draw from
   * @param handler the development card handler
   * @return the drawn development card
   * @throws EmptyDeckException if the deck has no cards remaining
   */
  public DevelopmentCard buyDevCard(
          GameModel model, DevelopmentCardDeck deck, DevelopmentCardHandler handler)
          throws EmptyDeckException {
    Player player = model.getCurrentPlayer();
    int round = model.getCurrentRound();
    return handler.buyDevelopmentCard(player, deck, round);
  }

  /**
   * Moves the robber to the target hex and steals from the victim of the given color.
   *
   * @param model the game model
   * @param targetHexId the hex to move the robber to
   * @param victimColor the color of the player to steal from, or null
   */
  public void moveRobberAndSteal(GameModel model, int targetHexId, PlayerColor victimColor) {
    model.moveRobberAndSteal(targetHexId, victimColor);
  }

  /**
   * Returns the current game phase.
   *
   * @param model the game model
   * @return the current game phase
   */
  public GamePhase getCurrentPhase(GameModel model) {
    return model.getCurrentPhase();
  }

  /**
   * Enters the setup phase.
   *
   * @param model the game model
   */
  public void enterSetupPhase(GameModel model) {
    model.enterSetupPhase();
  }

  /**
   * Completes the setup phase.
   *
   * @param model the game model
   */
  public void completeSetupPhase(GameModel model) {
    model.completeSetupPhase();
  }

  /**
   * Sets the current player by turn-order index.
   *
   * @param model the game model
   * @param playerIndex the index in turn order
   */
  public void setCurrentPlayer(GameModel model, int playerIndex) {
    model.setCurrentPlayerIndex(playerIndex);
    model.setCurrentPlayerColor(model.getTurnOrder().get(playerIndex).getColor());
  }

  /**
   * Returns the current round number.
   *
   * @param model the game model
   * @return the current round
   */
  public int getCurrentRound(GameModel model) {
    return model.getCurrentRound();
  }

  /**
   * Returns all players other than the current player.
   *
   * @param model the game model
   * @return the list of other players
   */
  public List<Player> getOtherPlayers(GameModel model) {
    return model.getOtherPlayers();
  }

  /**
   * Attempts to build a settlement at the given node.
   *
   * @param model the game model
   * @param nodeId the node to build at
   */
  public void attemptBuildSettlement(GameModel model, int nodeId) {
    model.attemptBuildSettlement(nodeId);
  }

  /**
   * Attempts to build a road between the two given nodes.
   *
   * @param model the game model
   * @param nodeId1 the first endpoint node
   * @param nodeId2 the second endpoint node
   */
  public void attemptBuildRoad(GameModel model, int nodeId1, int nodeId2) {
    model.attemptBuildRoad(nodeId1, nodeId2);
  }

  /**
   * Attempts to build a city at the given node.
   *
   * @param model the game model
   * @param nodeId the node to build at
   */
  public void attemptBuildCity(GameModel model, int nodeId) {
    model.attemptBuildCity(nodeId);
  }


  /**
   * Returns the players with structures on the given hex.
   *
   * @param board the game board
   * @param hexId the hex to query
   * @return the set of players on the hex
   */
  public Set<Player> getPlayersOnHex(BoardHandler board, int hexId) {
    return board.getPlayersOnHex(hexId);
  }

  /**
   * Returns the ports available to the given player.
   *
   * @param board the game board
   * @param player the player to query
   * @return the list of available ports
   */
  public List<Port> getAvailablePorts(BoardHandler board, Player player) {
    return board.getAvailablePorts(player);
  }

  /**
   * Plays a development card for the current player.
   *
   * @param model the game model
   * @param card the development card to play
   */
  public void playDevCard(GameModel model, DevelopmentCard card) {
    model.playDevCard(card);
  }
}
