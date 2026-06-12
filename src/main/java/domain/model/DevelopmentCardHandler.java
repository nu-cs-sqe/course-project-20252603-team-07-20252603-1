package domain.model;

import domain.model.developmentcards.DevelopmentCard;
import domain.model.developmentcards.DevelopmentCardDeck;
import domain.model.developmentcards.DevelopmentCardType;
import domain.model.exceptions.EmptyDeckException;
import domain.model.exceptions.InsufficientResourcesException;
import domain.model.gamepieces.Robber;
import domain.model.player.Player;
import domain.model.resources.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/** Handles all development card purchase and play logic. */
public class DevelopmentCardHandler {

  private final Random random;

  DevelopmentCardHandler(Random random) {
    this.random = random;
  }

  /** Creates a DevelopmentCardHandler with a default random source. */
  public DevelopmentCardHandler() {
    this(new Random());
  }

  /**
   * Purchases a development card for the buyer, deducting resources and adding the card to hand.
   *
   * @param buyer the player buying the card
   * @param deck the development card deck to draw from
   * @param currentRound the current round number
   * @return the drawn card
   * @throws EmptyDeckException if the deck is empty
   */
  public DevelopmentCard buyDevelopmentCard(
      Player buyer, DevelopmentCardDeck deck, int currentRound) throws EmptyDeckException {
    if (buyer.getResourceCount(Resource.ORE) < 1
        || buyer.getResourceCount(Resource.WOOL) < 1
        || buyer.getResourceCount(Resource.GRAIN) < 1) {
      throw new InsufficientResourcesException(
          "Not enough resources to buy a development card.");
    }

    DevelopmentCard card = deck.drawCard(currentRound);

    buyer.updateResources(Resource.ORE, -1);
    buyer.updateResources(Resource.WOOL, -1);
    buyer.updateResources(Resource.GRAIN, -1);
    buyer.addDevelopmentCard(card);

    return card;
  }

  /**
   * Plays a Monopoly card, taking all of the chosen resource from all other players.
   *
   * @param player the playing player
   * @param card the Monopoly card to play
   * @param currentRound the current round number
   * @param resource the resource to monopolize
   * @param otherPlayers all other active players
   */
  public void playMonopolyCard(
      Player player, DevelopmentCard card, int currentRound,
      Resource resource, List<Player> otherPlayers) {
    if (card == null) {
      throw new IllegalArgumentException("Development card cannot be null.");
    }
    if (card.getType() != DevelopmentCardType.MONOPOLY) {
      throw new IllegalArgumentException("Card is not a Monopoly card.");
    }
    if (!card.isPlayable(currentRound)) {
      throw new IllegalStateException("Card cannot be played the same turn it was purchased.");
    }
    if (player.hasPlayedDevCardThisTurn()) {
      throw new IllegalStateException("Already played a development card this turn.");
    }
    if (resource == null) {
      throw new IllegalArgumentException("Resource cannot be null.");
    }
    if (resource == Resource.DESERT) {
      throw new IllegalArgumentException("Cannot monopolize DESERT.");
    }
    if (otherPlayers == null) {
      throw new IllegalArgumentException("Other players list cannot be null.");
    }

    for (Player other : otherPlayers) {
      int amount = other.getResourceCount(resource);
      if (amount > 0) {
        other.updateResources(resource, -amount);
        player.updateResources(resource, amount);
      }
    }

    player.removeDevelopmentCard(card);
    player.setHasPlayedDevCardThisTurn(true);
  }

  /**
   * Plays a Road Building card, allowing the player to build up to two free roads.
   *
   * @param player the playing player
   * @param card the Road Builder card to play
   * @param currentRound the current round number
   * @param model the game model used to build the roads
   * @param road1Node1 first endpoint of the first road
   * @param road1Node2 second endpoint of the first road
   * @param road2Node1 first endpoint of the second road (nullable)
   * @param road2Node2 second endpoint of the second road (nullable)
   */
  public void playRoadBuildingCard(
      Player player, DevelopmentCard card, int currentRound, GameModel model,
      int road1Node1, int road1Node2, Integer road2Node1, Integer road2Node2) {
    if (card == null) {
      throw new IllegalArgumentException("Development card cannot be null.");
    }
    if (card.getType() != DevelopmentCardType.ROAD_BUILDER) {
      throw new IllegalArgumentException("Card is not a Road Builder card.");
    }
    if (!card.isPlayable(currentRound)) {
      throw new IllegalStateException("Card cannot be played the same turn it was purchased.");
    }
    if (player.hasPlayedDevCardThisTurn()) {
      throw new IllegalStateException("Already played a development card this turn.");
    }

    model.attemptBuildRoad(road1Node1, road1Node2);
    if (road2Node1 != null && road2Node2 != null) {
      model.attemptBuildRoad(road2Node1, road2Node2);
    }
    player.removeDevelopmentCard(card);
    player.setHasPlayedDevCardThisTurn(true);
  }

  /**
   * Plays a Knight card, moving the robber and optionally stealing from a victim.
   *
   * @param player the playing player
   * @param card the Knight card to play
   * @param currentRound the current round number
   * @param robber the robber piece
   * @param targetHexId the hex to move the robber to
   * @param victim the player to steal from (nullable)
   */
  public void playKnightCard(
      Player player, DevelopmentCard card, int currentRound,
      Robber robber, int targetHexId, Player victim) {
    if (card == null) {
      throw new IllegalArgumentException("Development card cannot be null.");
    }
    if (card.getType() != DevelopmentCardType.KNIGHT) {
      throw new IllegalArgumentException("Card is not a Knight card.");
    }
    if (!card.isPlayable(currentRound)) {
      throw new IllegalStateException("Card cannot be played the same turn it was purchased.");
    }
    if (player.hasPlayedDevCardThisTurn()) {
      throw new IllegalStateException("Already played a development card this turn.");
    }
    if (robber == null) {
      throw new IllegalArgumentException("Robber cannot be null.");
    }
    if (targetHexId == robber.getRobberLocation()) {
      throw new IllegalArgumentException("Must move robber to a different hex.");
    }
    if (victim != null && !victim.isAdjacentToHex(targetHexId)) {
      throw new IllegalArgumentException("Victim must be adjacent to the robber's new hex.");
    }

    robber.moveRobber(targetHexId);

    if (victim != null && victim.getTotalResourceCount() > 0) {
      List<Resource> available = new ArrayList<>();
      for (Map.Entry<Resource, Integer> entry : victim.getResources().entrySet()) {
        if (entry.getValue() > 0) {
          available.add(entry.getKey());
        }
      }
      Resource stolen = available.get(random.nextInt(available.size()));
      victim.updateResources(stolen, -1);
      player.updateResources(stolen, 1);
    }

    player.incrementKnightCount();
    player.removeDevelopmentCard(card);
    player.setHasPlayedDevCardThisTurn(true);
  }

  /**
   * Plays a Year of Plenty card, granting the player two free resources.
   *
   * @param player the playing player
   * @param card the Year of Plenty card to play
   * @param currentRound the current round number
   * @param resource1 the first resource to receive
   * @param resource2 the second resource to receive
   */
  public void playYearOfPlentyCard(
      Player player, DevelopmentCard card, int currentRound,
      Resource resource1, Resource resource2) {
    if (card == null) {
      throw new IllegalArgumentException("Development card cannot be null.");
    }
    if (card.getType() != DevelopmentCardType.YEAR_OF_PLENTY) {
      throw new IllegalArgumentException("Card is not a Year of Plenty card.");
    }
    if (!card.isPlayable(currentRound)) {
      throw new IllegalStateException("Card cannot be played the same turn it was purchased.");
    }
    if (player.hasPlayedDevCardThisTurn()) {
      throw new IllegalStateException("Already played a development card this turn.");
    }
    if (resource1 == null) {
      throw new IllegalArgumentException("Resource cannot be null.");
    }
    if (resource2 == null) {
      throw new IllegalArgumentException("Resource cannot be null.");
    }
    if (resource1 == Resource.DESERT) {
      throw new IllegalArgumentException("Cannot take DESERT as a resource.");
    }
    if (resource2 == Resource.DESERT) {
      throw new IllegalArgumentException("Cannot take DESERT as a resource.");
    }

    player.updateResources(resource1, 1);
    player.updateResources(resource2, 1);
    player.removeDevelopmentCard(card);
    player.setHasPlayedDevCardThisTurn(true);
  }

  /**
   * Counts the number of Victory Point cards in the given hand.
   *
   * @param hand the player's development card hand
   * @return the count of Victory Point cards
   */
  public int countVictoryPointCards(List<DevelopmentCard> hand) {
    int count = 0;
    for (DevelopmentCard c : hand) {
      if (c.getType() == DevelopmentCardType.VICTORY_POINT) {
        count++;
      }
    }
    return count;
  }
}
