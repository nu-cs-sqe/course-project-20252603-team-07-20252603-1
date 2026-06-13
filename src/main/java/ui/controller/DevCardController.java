package ui.controller;

import domain.model.DevelopmentCardHandler;
import domain.model.GameModel;
import domain.model.developmentcards.DevelopmentCard;
import domain.model.developmentcards.DevelopmentCardDeck;
import domain.model.exceptions.EmptyDeckException;
import domain.model.gamepieces.Robber;
import domain.model.player.Player;
import domain.model.resources.Resource;
import java.util.List;

/** Controller for development card actions, delegating to DevelopmentCardHandler. */
public class DevCardController {

  private final DevelopmentCardHandler handler;

  /**
   * Creates a DevCardController backed by the given handler.
   *
   * @param handler the development card handler
   */
  public DevCardController(DevelopmentCardHandler handler) {
    this.handler = handler;
  }

  /**
   * Purchases a development card for the current player.
   *
   * @param model the game model
   * @param deck the development card deck
   * @return the drawn card
   * @throws EmptyDeckException if the deck is empty
   */
  public DevelopmentCard buyDevelopmentCard(GameModel model, DevelopmentCardDeck deck)
      throws EmptyDeckException {
    Player currentPlayer = model.getCurrentPlayer();
    int currentRound = model.getCurrentRound();
    return handler.buyDevelopmentCard(currentPlayer, deck, currentRound);
  }

  /**
   * Plays a Knight card for the current player, moving the robber and optionally stealing.
   *
   * @param model the game model
   * @param card the Knight card to play
   * @param robber the robber piece
   * @param targetHexId the hex to move the robber to
   * @param victim the player to steal from (nullable)
   */
  public void playKnightCard(
      GameModel model, DevelopmentCard card, Robber robber,
      int targetHexId, Player victim) {
    Player currentPlayer = model.getCurrentPlayer();
    int currentRound = model.getCurrentRound();
    handler.playKnightCard(currentPlayer, card, currentRound, robber, targetHexId, victim);
  }

  /**
   * Plays a Monopoly card for the current player.
   *
   * @param model the game model
   * @param card the Monopoly card to play
   * @param resource the resource to monopolize
   */
  public void playMonopolyCard(GameModel model, DevelopmentCard card, Resource resource) {
    Player currentPlayer = model.getCurrentPlayer();
    int currentRound = model.getCurrentRound();
    List<Player> otherPlayers = model.getOtherPlayers();
    handler.playMonopolyCard(currentPlayer, card, currentRound, resource, otherPlayers);
  }

  /**
   * Plays a Road Building card for the current player.
   *
   * @param model the game model
   * @param card the Road Builder card to play
   * @param road1Node1 first endpoint of the first road
   * @param road1Node2 second endpoint of the first road
   * @param road2Node1 first endpoint of the second road (nullable)
   * @param road2Node2 second endpoint of the second road (nullable)
   */
  public void playRoadBuildingCard(
      GameModel model, DevelopmentCard card,
      int road1Node1, int road1Node2, Integer road2Node1, Integer road2Node2) {
    Player currentPlayer = model.getCurrentPlayer();
    int currentRound = model.getCurrentRound();
    handler.playRoadBuildingCard(
        currentPlayer, card, currentRound, model,
        road1Node1, road1Node2, road2Node1, road2Node2);
  }

  /**
   * Plays a Year of Plenty card for the current player.
   *
   * @param model the game model
   * @param card the Year of Plenty card to play
   * @param resource1 the first resource to receive
   * @param resource2 the second resource to receive
   */
  public void playYearOfPlentyCard(
      GameModel model, DevelopmentCard card, Resource resource1, Resource resource2) {
    Player currentPlayer = model.getCurrentPlayer();
    int currentRound = model.getCurrentRound();
    handler.playYearOfPlentyCard(currentPlayer, card, currentRound, resource1, resource2);
  }

  /**
   * Returns the number of Victory Point cards in the current player's hand.
   *
   * @param model the game model
   * @return the Victory Point card count
   */
  public int getVictoryPointCount(GameModel model) {
    Player currentPlayer = model.getCurrentPlayer();
    return handler.countVictoryPointCards(currentPlayer.getDevelopmentCards());
  }
}
