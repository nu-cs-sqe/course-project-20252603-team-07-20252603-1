package ui.controller;

import domain.model.DevelopmentCardHandler;
import domain.model.GameModel;
import domain.model.board.Port;
import domain.model.developmentcards.DevelopmentCard;
import domain.model.developmentcards.DevelopmentCardDeck;
import domain.model.exceptions.EmptyDeckException;
import domain.model.gamepieces.DiceHandler;
import domain.model.player.Player;
import domain.model.player.PlayerColor;
import domain.model.player.TradeOffer;
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
   * Plays a development card for the current player.
   *
   * @param model the game model
   * @param card the development card to play
   */
  public void playDevCard(GameModel model, DevelopmentCard card) {
    model.playDevCard(card);
  }
}