package domain.model.player;

import domain.model.developmentcards.DevelopmentCard;
import domain.model.exceptions.InsufficientResourcesException;
import domain.model.gamepieces.Road;
import domain.model.gamepieces.Settlement;
import domain.model.resources.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;   


/** Represents a player in the Catan game, tracking resources, buildings, and victory points. */
public class Player {

  private final List<Settlement> settlements;
  private final List<Road> roads;
  private final Map<Resource, Integer> resources;
  private final List<DevelopmentCard> developmentCards;
  private PlayerColor color;
  private String name;
  private int numSettlement;
  private boolean hasPlayedDevCardThisTurn = false;
  private int knightCount = 0;
  private int numVictoryPoints;

  /**
   * Creates a new player with the given name and color.
   *
   * @param name the player's display name
   * @param color the player's color token
   */
  public Player(String name, PlayerColor color) {
    this.settlements = new ArrayList<>();
    this.roads = new ArrayList<>();
    this.resources = new HashMap<>();
    this.developmentCards = new ArrayList<>();
    this.color = color;
    this.name = name;
    this.numSettlement = 0;
    this.numVictoryPoints = 0;
  }

  /**
   * Returns an unmodifiable view of the player's resource counts.
   *
   * @return the resource map
   */
  public Map<Resource, Integer> getResources() {
    return Collections.unmodifiableMap(resources);
  }

  /**
   * Returns an unmodifiable view of the player's settlements.
   *
   * @return the settlements list
   */
  public List<Settlement> getSettlements() {
    return Collections.unmodifiableList(settlements);
  }

  /** Places a settlement piece, limited to a max of 5. */
  public void placeSettlement() {
    if (settlements.size() >= 5) {
      throw new IllegalStateException("No settlements remaining.");
    }
    settlements.add(new Settlement());
  }

  /**
   * Returns an unmodifiable view of the player's roads.
   *
   * @return the roads list
   */
  public List<Road> getRoads() {
    return Collections.unmodifiableList(roads);
  }

  /** Places a road piece, limited to a max of 15. */
  public void placeRoad() {
    if (roads.size() >= 15) {
      throw new IllegalStateException("No roads remaining.");
    }
    roads.add(new Road());
  }

  /**
   * Adds the given resources to the player's hand.
   *
   * @param resources map of resource types to quantities (must not contain DESERT)
   */
  public void receiveResources(Map<Resource, Integer> resources) {
    if (resources == null) {
      throw new IllegalArgumentException("Resources cannot be null.");
    }
    for (Map.Entry<Resource, Integer> entry : resources.entrySet()) {
      if (entry.getKey() == Resource.DESERT) {
        throw new IllegalArgumentException("Cannot receive DESERT as a resource.");
      }
      if (entry.getValue() < 1) {
        throw new IllegalArgumentException("Resource quantity must be at least 1.");
      }
      this.resources.merge(entry.getKey(), entry.getValue(), Integer::sum);
    }
  }

  /**
   * Returns the player's display name.
   *
   * @return the name
   */
  public String getName() {
    return this.name;
  }

  /**
   * Returns the player's color token.
   *
   * @return the color
   */
  public PlayerColor getColor() {
    return this.color;
  }

  /**
   * Adjusts the player's count of the given resource by delta (positive=gain, negative=spend).
   *
   * @param resource the resource to update
   * @param delta the change amount
   */
  public void updateResources(Resource resource, int delta) {
    if (resource == null) {
      throw new IllegalArgumentException("Resource cannot be null.");
    }
    if (resource == Resource.DESERT) {
      throw new IllegalArgumentException("Cannot update DESERT resources.");
    }
    int newCount = getResourceCount(resource) + delta;
    if (newCount < 0) {
      throw new InsufficientResourcesException("Insufficient " + resource + " resources.");
    }
    resources.put(resource, newCount);
  }

  /**
   * Returns the player's count of the given resource.
   *
   * @param resource the resource to check
   * @return the count
   */
  public int getResourceCount(Resource resource) {
    if (resource == null) {
      throw new IllegalArgumentException("Resource cannot be null.");
    }
    if (resource == Resource.DESERT) {
      throw new IllegalArgumentException("Cannot get count of DESERT.");
    }
    return resources.getOrDefault(resource, 0);
  }

  /**
   * Returns the total number of resource cards in hand.
   *
   * @return total resource count
   */
  public int getTotalResourceCount() {
    return resources.values().stream().mapToInt(Integer::intValue).sum();
  }

  /** Increments the player's settlement count (separate from piece tracking). */
  public void increaseSettlementCount() {
    this.numSettlement++;
  }

  /**
   * Returns the player's settlement count.
   *
   * @return settlement count
   */
  public int getSettlementCount() {
    return this.numSettlement;
  }

  /**
   * Adds a development card to the player's hand.
   *
   * @param card the card to add
   */
  public void addDevelopmentCard(DevelopmentCard card) {
    this.developmentCards.add(card);
  }

  /**
   * Returns an unmodifiable view of the player's development cards.
   *
   * @return the development cards list
   */
  public List<DevelopmentCard> getDevelopmentCards() {
    return Collections.unmodifiableList(developmentCards);
  }

  /**
   * Returns whether the player has already played a development card this turn.
   *
   * @return true if a dev card was played this turn
   */
  public boolean hasPlayedDevCardThisTurn() {
    return hasPlayedDevCardThisTurn;
  }

  /**
   * Sets whether the player has played a development card this turn.
   *
   * @param played true if a card was played
   */
  public void setHasPlayedDevCardThisTurn(boolean played) {
    hasPlayedDevCardThisTurn = played;
  }

  /**
   * Returns whether this player has a settlement or city adjacent to the given hex.
   *
   * @param hexId the hex to check
   * @return true if adjacent
   */
  public boolean isAdjacentToHex(int hexId) {
    return false;
  }

  /**
   * Removes a development card from the player's hand.
   *
   * @param card the card to remove
   */
  public void removeDevelopmentCard(DevelopmentCard card) {
    developmentCards.remove(card);
  }

  /** Increments the player's knight count. */
  public void incrementKnightCount() {
    knightCount++;
  }

  /**
   * Returns the number of knight cards the player has played.
   *
   * @return knight count
   */
  public int getKnightCount() {
    return knightCount;
  }

  /**
   * Adjusts the player's victory point total by the given amount.
   *
   * @param amount the change to apply (positive or negative)
   */
  public void updateVictoryPoints(int amount) {
    this.numVictoryPoints += amount;
  }

  /**
   * Returns the player's current victory point total.
   *
   * @return victory points
   */
  public int getVictoryPoints() {
    return this.numVictoryPoints;
  }
}
