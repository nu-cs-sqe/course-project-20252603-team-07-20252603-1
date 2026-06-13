package domain.model;

import domain.model.board.BoardHandler;
import domain.model.board.Port;
import domain.model.board.PortTradeRequest;
import domain.model.developmentcards.DevelopmentCard;
import domain.model.developmentcards.DevelopmentCardDeck;
import domain.model.developmentcards.DevelopmentCardType;
import domain.model.exceptions.EmptyDeckException;
import domain.model.exceptions.IllegalCityPlacementException;
import domain.model.exceptions.IllegalGamePhaseException;
import domain.model.exceptions.IllegalSettlementPlacementException;
import domain.model.exceptions.InsufficientResourcesException;
import domain.model.player.Player;
import domain.model.player.PlayerColor;
import domain.model.player.TradeManager;
import domain.model.player.TradeOffer;
import domain.model.resources.Resource;
import domain.model.resources.ResourceDeck;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/** Represents the full state of a Catan game, including board, players, and game phase. */
public class GameModel {

  private static final int ROBBER_ROLL_VALUE = 7;
  private static final int MAX_AMOUNT_SETTLEMENTS = 5;
  private static final int MIN_POINTS_TO_WIN_GAME = 10;
  private static final int POINTS_FOR_SETTLEMENT = 1;
  private static final int POINTS_FOR_CITY = 1;
  private static final int DEV_CARD_COST = 1;
  private static final int POINTS_FOR_LONGEST_ROAD = 2;

  @SuppressFBWarnings(
          value = "EI_EXPOSE_REP2",
          justification = "BoardHandler is intentionally shared"
                  + " between GameSetupModel and GameModel"
                  + " as it represents the single game board state")
  private final BoardHandler board;
  private GamePhase currentGamePhase;
  private int currentPlayerIndex;
  private int currentRound = 0;
  private List<PlayerColor> playerColors;
  private PlayerColor currentPlayerColor;
  private Map<PlayerColor, Player> playerColorToPlayerObject = new HashMap<>();
  private Map<PlayerColor, Integer> playerColorToLastClaimedNodeId = new HashMap<>();
  private PlayerColor currentLongestRoadPlayerColor;
  private final Random random = new Random();

  private final ResourceDeck lumberDeck;
  private final ResourceDeck brickDeck;
  private final ResourceDeck grainDeck;
  private final ResourceDeck oreDeck;
  private final ResourceDeck woolDeck;
  private final Map<Resource, ResourceDeck> decks;
  private final TradeManager tradeManager;

  GameModel(
          ResourceDeck lumberDeck, ResourceDeck brickDeck,
          ResourceDeck grainDeck, ResourceDeck oreDeck,
          ResourceDeck woolDeck,
          Map<PlayerColor, Player> playerColorToPlayerObject,
          BoardHandler board,
          TradeManager tradeManager) {
    this.lumberDeck = lumberDeck;
    this.brickDeck = brickDeck;
    this.grainDeck = grainDeck;
    this.oreDeck = oreDeck;
    this.woolDeck = woolDeck;
    decks = Map.of(
            Resource.LUMBER, lumberDeck,
            Resource.BRICK, brickDeck,
            Resource.GRAIN, grainDeck,
            Resource.WOOL, woolDeck,
            Resource.ORE, oreDeck
    );
    this.playerColorToPlayerObject = playerColorToPlayerObject;
    this.board = board;
    this.currentLongestRoadPlayerColor = PlayerColor.SETUP;
    this.tradeManager = tradeManager;
  }

  /**
   * Creates a GameModel with the given players and board.
   *
   * @param players the list of players in turn order
   * @param board the game board
   */
  public GameModel(List<Player> players, BoardHandler board) {
    this.board = board;
    this.lumberDeck = new ResourceDeck(Resource.LUMBER);
    this.brickDeck = new ResourceDeck(Resource.BRICK);
    this.grainDeck = new ResourceDeck(Resource.GRAIN);
    this.oreDeck = new ResourceDeck(Resource.ORE);
    this.woolDeck = new ResourceDeck(Resource.WOOL);
    decks = Map.of(
            Resource.LUMBER, lumberDeck,
            Resource.BRICK, brickDeck,
            Resource.GRAIN, grainDeck,
            Resource.WOOL, woolDeck,
            Resource.ORE, oreDeck
    );

    playerColors = new ArrayList<>();
    for (Player player : players) {
      PlayerColor currentColor = player.getColor();
      this.playerColorToLastClaimedNodeId.put(currentColor, -1);
      this.playerColorToPlayerObject.put(currentColor, player);
      playerColors.add(currentColor);
    }
    this.currentPlayerIndex = 0;
    this.currentPlayerColor = playerColors.get(0);
    this.currentLongestRoadPlayerColor = PlayerColor.SETUP;
    this.currentGamePhase = GamePhase.BEFORE_ROLL;
    this.tradeManager = new TradeManager();
  }

  /**
   * Returns the players in turn order.
   *
   * @return ordered list of players
   */
  public List<Player> getTurnOrder() {
    return playerColors.stream()
            .map(color -> playerColorToPlayerObject.get(color))
            .collect(Collectors.toList());
  }

  /**
   * Returns the index of the current player in turn order.
   *
   * @return the current player index
   */
  public int getCurrentPlayerIndex() {
    return currentPlayerIndex;
  }

  /**
   * Returns the player whose turn it currently is.
   *
   * @return the current player
   */
  public Player getCurrentPlayer() {
    return playerColorToPlayerObject.get(currentPlayerColor);
  }

  /**
   * Returns the current round number.
   *
   * @return the current round
   */
  public int getCurrentRound() {
    return currentRound;
  }

  /**
   * Returns all players except the current player.
   *
   * @return list of other players
   */
  public List<Player> getOtherPlayers() {
    Player current = getCurrentPlayer();
    return playerColorToPlayerObject.values().stream()
            .filter(p -> p != current)
            .collect(Collectors.toList());
  }

  /**
   * Returns the color of the current player.
   *
   * @return current player color
   */
  public PlayerColor getCurrentPlayerColor() {
    return this.currentPlayerColor;
  }

  /**
   * Sets the current player index.
   *
   * @param newIndex the new player index
   */
  public void setCurrentPlayerIndex(int newIndex) {
    this.currentPlayerIndex = newIndex;
  }

  /**
   * Sets the current player color.
   *
   * @param color the new current player color
   */
  public void setCurrentPlayerColor(PlayerColor color) {
    this.currentPlayerColor = color;
  }

  /**
   * Ends the current player's turn and advances to the next player.
   * Sets the phase to END_GAME if the current player has 10+ victory points.
   */
  public void endTurn() {
    checkCurrentGamePhaseMatches(GamePhase.GENERAL_PLAY);
    checkCurrentPlayerHasTenOrMoreVictoryPoints();
    if (getCurrentPhase() == GamePhase.END_GAME) {
      return;
    } else {
      advanceToNextPlayer();
      if (currentPlayerIndex == 0) {
        currentRound++;
      }
      setCurrentGamePhase(GamePhase.BEFORE_ROLL);
    }
  }

  /**
   * Advances the turn to the next player in order.
   */
  public void advanceToNextPlayer() {
    currentPlayerIndex = (currentPlayerIndex + 1) % playerColors.size();
    currentPlayerColor = playerColors.get(currentPlayerIndex);
  }

  /**
   * Returns the player with the given color.
   *
   * @param color the player color
   * @return the player
   */
  public Player getArbitraryPlayer(PlayerColor color) {
    return playerColorToPlayerObject.get(color);
  }

    public void enterSetupPhase() {
        checkCurrentGamePhaseMatches(GamePhase.BEFORE_ROLL);
        setCurrentGamePhase(GamePhase.SETUP_PHASE);
    }

    public void completeSetupPhase() {
        checkCurrentGamePhaseMatches(GamePhase.SETUP_PHASE);
        this.currentPlayerIndex = 0;
        this.currentPlayerColor = playerColors.get(0);
        setCurrentGamePhase(GamePhase.BEFORE_ROLL);
    }

    public void performTurn(int roll) {
        checkCurrentGamePhaseMatches(GamePhase.BEFORE_ROLL);

        if (roll == ROBBER_ROLL_VALUE) {
            currentGamePhase = GamePhase.MOVE_ROBBER;
            return;
        }
        try {
            distributeResources(roll);
        } catch (EmptyDeckException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        currentGamePhase = GamePhase.GENERAL_PLAY;
    }

    private void distributeResources(int roll) throws EmptyDeckException {
        Map<Resource, Map<Player, Integer>> demand = board.computeResourceDemand(roll);
        for (Map.Entry<Resource, Map<Player, Integer>> resourceEntry : demand.entrySet()) {
            distributeResourceToPlayers(resourceEntry.getKey(), resourceEntry.getValue());
        }
    }

    // if not enough to satisfy all players, no one gets anything; if only one player is requesting, they can get a partial amount
    private void distributeResourceToPlayers(Resource resource, Map<Player, Integer> playerAmounts) throws EmptyDeckException {
        ResourceDeck deck = decks.get(resource);
        if (playerAmounts.size() > 1) {
            int total = playerAmounts.values().stream().mapToInt(Integer::intValue).sum();
            if (deck.getTotalCards() < total) return;
        }
        for (Map.Entry<Player, Integer> playerAmountEntry : playerAmounts.entrySet()) {
            int drawn = deck.drawMultiple(playerAmountEntry.getValue());
            if (drawn > 0) {
                playerAmountEntry.getKey().updateResources(resource, drawn);
            }
        }
    }

  /**
   * Attempts to build a settlement at the specified node for the current player.
   *
   * @param nodeId the node to build on
   */
  public void attemptBuildSettlement(int nodeId) {
    checkCurrentGamePhaseMatches(GamePhase.GENERAL_PLAY, GamePhase.SETUP_PHASE);
    if (this.currentGamePhase == GamePhase.SETUP_PHASE) {
      board.buildSetupSettlement(getCurrentPlayer(), nodeId);
      this.playerColorToLastClaimedNodeId.put(currentPlayerColor, nodeId);
      return;
    }
    checkIfPlayerAtMaxSettlements(currentPlayerColor);
    for (Resource r : EnumSet.of(Resource.BRICK, Resource.LUMBER, Resource.WOOL, Resource.GRAIN)) {
      checkPlayerOwnsEnoughResources(currentPlayerColor, r, 1);
    }
    board.buildSettlement(getCurrentPlayer(), nodeId);
    for (Resource r : EnumSet.of(Resource.BRICK, Resource.LUMBER, Resource.WOOL, Resource.GRAIN)) {
      reducePlayerResources(currentPlayerColor, r, 1);
      ResourceDeck deckToReplenish = decks.get(r);
      deckToReplenish.replenish();
    }
    incrementNumSettlements(currentPlayerColor);
    Player currentPlayer = getCurrentPlayer();
    currentPlayer.updateVictoryPoints(POINTS_FOR_SETTLEMENT);
    handleLongestRoad();
  }

  int getPlayerLastClaimedNode(PlayerColor color) {
    return this.playerColorToLastClaimedNodeId.get(color);
  }

  /**
   * Attempts to build a road between the two specified nodes for the current player.
   *
   * @param startingNodeId one road endpoint
   * @param endingNodeId the other road endpoint
   */
  public void attemptBuildRoad(int startingNodeId, int endingNodeId) {
    checkCurrentGamePhaseMatches(
            GamePhase.GENERAL_PLAY, GamePhase.ROAD_BUILDING_DEV_CARD, GamePhase.SETUP_PHASE);
    if (currentGamePhase == GamePhase.SETUP_PHASE) {
      board.buildSetupRoad(
              getCurrentPlayer(), getPlayerLastClaimedNode(currentPlayerColor),
              startingNodeId, endingNodeId);
      return;
    } else if (currentGamePhase == GamePhase.ROAD_BUILDING_DEV_CARD) {
      board.addRoad(getCurrentPlayer(), startingNodeId, endingNodeId);
      handleLongestRoad();
      return;
    }
    for (Resource r : EnumSet.of(Resource.BRICK, Resource.LUMBER)) {
      checkPlayerOwnsEnoughResources(currentPlayerColor, r, 1);
    }
    board.addRoad(getCurrentPlayer(), startingNodeId, endingNodeId);
    for (Resource r : EnumSet.of(Resource.BRICK, Resource.LUMBER)) {
      reducePlayerResources(currentPlayerColor, r, 1);
      ResourceDeck deckToReplenish = decks.get(r);
      deckToReplenish.replenish();
    }
    handleLongestRoad();
  }

  void setCurrentGamePhase(GamePhase newGamePhase) {
    this.currentGamePhase = newGamePhase;
  }

  /**
   * Returns the current game phase.
   *
   * @return the current phase
   */
  public GamePhase getCurrentPhase() {
    return currentGamePhase;
  }

  private void checkCurrentGamePhaseMatches(GamePhase... expectedGamePhaseOptions) {
    for (GamePhase allowedPhase : expectedGamePhaseOptions) {
      if (currentGamePhase == allowedPhase) {
        return;
      }
    }
    throw new IllegalGamePhaseException("Not proper phase for that action");
  }

  private void incrementNumSettlements(PlayerColor playerColorOfInterest) {
    Player relevantPlayer = getArbitraryPlayer(playerColorOfInterest);
    relevantPlayer.increaseSettlementCount();
  }

  private void checkIfPlayerAtMaxSettlements(PlayerColor playerColorOfInterest) {
    Player relevantPlayer = getArbitraryPlayer(playerColorOfInterest);
    int currentAmountSettlements = relevantPlayer.getSettlementCount();
    if (currentAmountSettlements >= MAX_AMOUNT_SETTLEMENTS) {
      throw new IllegalSettlementPlacementException("Can not have more than 5 settlements");
    }
  }

  private void checkPlayerOwnsEnoughResources(
          PlayerColor playerColorOfInterest, Resource type, int amountNeeded) {
    Player relevantPlayer = getArbitraryPlayer(playerColorOfInterest);
    int amountPlayerOwnsResource = relevantPlayer.getResourceCount(type);
    if (amountPlayerOwnsResource < amountNeeded) {
      throw new InsufficientResourcesException("Insufficient resources");
    }
  }

  private void reducePlayerResources(
          PlayerColor playerColorOfInterest, Resource type, int amount) {
    Player relevantPlayer = playerColorToPlayerObject.get(playerColorOfInterest);
    relevantPlayer.updateResources(type, -amount);
  }

  /**
   * Attempts to build a city at the specified node for the current player.
   *
   * @param nodeId the node to upgrade to a city
   */
  public void attemptBuildCity(int nodeId) {
    checkCurrentGamePhaseMatches(GamePhase.GENERAL_PLAY);
    checkPlayerOwnsEnoughResources(currentPlayerColor, Resource.ORE, 3);
    checkPlayerOwnsEnoughResources(currentPlayerColor, Resource.GRAIN, 2);
    try {
      board.buildCity(getCurrentPlayer(), nodeId);
    } catch (Exception e) {
      throw new IllegalCityPlacementException("Can not place city at specified node");
    }
    reducePlayerResources(currentPlayerColor, Resource.ORE, 3);
    oreDeck.replenish(3);
    reducePlayerResources(currentPlayerColor, Resource.GRAIN, 2);
    grainDeck.replenish(2);
    Player currentPlayer = getCurrentPlayer();
    currentPlayer.updateVictoryPoints(POINTS_FOR_CITY);
  }

  /**
   * Returns the color of the player who currently holds the Longest Road card.
   *
   * @return the longest road holder's color (SETUP if unclaimed)
   */
  public PlayerColor getCurrentLongestRoadPlayerColor() {
    return this.currentLongestRoadPlayerColor;
  }

  void setCurrentLongestRoadPlayerColor(PlayerColor newLongestRoadColor) {
    this.currentLongestRoadPlayerColor = newLongestRoadColor;
  }

  /**
   * Recalculates the longest road and transfers the award if the holder has changed.
   */
  public void handleLongestRoad() {
    List<Player> playerList = new ArrayList<>(playerColorToPlayerObject.values());
    PlayerColor newLongestRoadColor =
            board.calculateLongestRoad(playerList, currentLongestRoadPlayerColor);
    if (newLongestRoadColor != this.currentLongestRoadPlayerColor) {
      Player playerToAwardPoints = getArbitraryPlayer(newLongestRoadColor);
      playerToAwardPoints.updateVictoryPoints(POINTS_FOR_LONGEST_ROAD);
      if (this.currentLongestRoadPlayerColor != PlayerColor.SETUP) {
        Player playerToLosePoints = getArbitraryPlayer(this.currentLongestRoadPlayerColor);
        playerToLosePoints.updateVictoryPoints(-POINTS_FOR_LONGEST_ROAD);
      }
      this.currentLongestRoadPlayerColor = newLongestRoadColor;
    }
  }

  /**
   * Updates the victory points for the player with the given color.
   *
   * @param color the player color
   * @param amount the amount to add (positive or negative)
   */
  public void updateVictoryPoints(PlayerColor color, int amount) {
    Player relevantPlayer = getArbitraryPlayer(color);
    relevantPlayer.updateVictoryPoints(amount);
  }

  /**
   * Checks if the current player has reached 10 or more victory points and sets END_GAME phase.
   */
  public void checkCurrentPlayerHasTenOrMoreVictoryPoints() {
    Player currentPlayer = getCurrentPlayer();
    int currentPlayerVictoryPoints = currentPlayer.getVictoryPoints();
    if (currentPlayerVictoryPoints >= MIN_POINTS_TO_WIN_GAME) {
      setCurrentGamePhase(GamePhase.END_GAME);
    }
  }

  /**
   * Offers a trade during general play, transitioning to the OFFERING_TRADE phase.
   *
   * @param offer the trade offer to make
   */
  public void offerTrade(TradeOffer offer) {
    checkCurrentGamePhaseMatches(GamePhase.GENERAL_PLAY);
    tradeManager.offerTrade(offer);
    currentGamePhase = GamePhase.OFFERING_TRADE;
  }

  /**
   * Accepts a trade offer on behalf of the accepting player.
   *
   * @param offer the trade offer being accepted
   * @param acceptingPlayer the player accepting the trade
   */
  public void acceptTrade(TradeOffer offer, Player acceptingPlayer) {
    checkCurrentGamePhaseMatches(GamePhase.OFFERING_TRADE);
    tradeManager.acceptTrade(offer, acceptingPlayer);
    currentGamePhase = GamePhase.GENERAL_PLAY;
  }

  /**
   * Clears all active trade offers and returns to general play.
   */
  public void clearOffers() {
    checkCurrentGamePhaseMatches(GamePhase.OFFERING_TRADE);
    tradeManager.clearOffers();
    currentGamePhase = GamePhase.GENERAL_PLAY;
  }

  /**
   * Plays a development card, transitioning the game phase based on card type.
   *
   * @param card the development card to play
   */
  public void playDevCard(DevelopmentCard card) {
    if (card == null) {
      throw new IllegalArgumentException("Development card cannot be null.");
    }
    checkCurrentGamePhaseMatches(GamePhase.BEFORE_ROLL, GamePhase.GENERAL_PLAY);
    DevelopmentCardType type = card.getType();
    if (type == DevelopmentCardType.KNIGHT) {
      currentGamePhase = GamePhase.MOVE_ROBBER;
    } else if (type == DevelopmentCardType.ROAD_BUILDER) {
      currentGamePhase = GamePhase.ROAD_BUILDING_DEV_CARD;
    } else if (type == DevelopmentCardType.MONOPOLY) {
      currentGamePhase = GamePhase.MONOPOLY_DEV_CARD;
    }
  }

  /**
   * Purchases a development card from the deck, deducting resources from the current player.
   *
   * @param deck the development card deck to draw from
   * @return the drawn development card
   * @throws EmptyDeckException if the deck has no cards remaining
   */
  public DevelopmentCard buyDevCard(DevelopmentCardDeck deck) throws EmptyDeckException {
    checkCurrentGamePhaseMatches(GamePhase.GENERAL_PLAY);
    checkPlayerOwnsEnoughResources(currentPlayerColor, Resource.ORE, DEV_CARD_COST);
    checkPlayerOwnsEnoughResources(currentPlayerColor, Resource.WOOL, DEV_CARD_COST);
    checkPlayerOwnsEnoughResources(currentPlayerColor, Resource.GRAIN, DEV_CARD_COST);
    final DevelopmentCard card = deck.drawCard(currentRound);
    Player player = getCurrentPlayer();
    player.updateResources(Resource.ORE, -DEV_CARD_COST);
    player.updateResources(Resource.WOOL, -DEV_CARD_COST);
    player.updateResources(Resource.GRAIN, -DEV_CARD_COST);
    oreDeck.replenish();
    woolDeck.replenish();
    grainDeck.replenish();
    player.addDevelopmentCard(card);
    return card;
  }

  /**
   * Moves the robber to the target hex and steals a random resource from the victim, if any.
   *
   * @param targetHexId the hex to move the robber to
   * @param victim the player to steal from, or null if there is no victim
   */
  public void moveRobberAndSteal(int targetHexId, Player victim) {
    checkCurrentGamePhaseMatches(GamePhase.MOVE_ROBBER);
    board.moveRobber(targetHexId);
    if (victim != null && victim.getTotalResourceCount() > 0) {
      List<Resource> available = new ArrayList<>();
      for (Map.Entry<Resource, Integer> entry : victim.getResources().entrySet()) {
        if (entry.getValue() > 0) {
          available.add(entry.getKey());
        }
      }
      Resource stolen = available.get(random.nextInt(available.size()));
      victim.updateResources(stolen, -1);
      getCurrentPlayer().updateResources(stolen, 1);
    }
    setCurrentGamePhase(GamePhase.GENERAL_PLAY);
  }

  /**
   * Attempts a port trade for the current player, exchanging the given resource for another.
   *
   * @param port the port to trade at
   * @param giving the resource being given
   * @param receiving the resource being received
   */
  public void attemptPortTrade(Port port, Resource giving, Resource receiving) {
    checkCurrentGamePhaseMatches(GamePhase.GENERAL_PLAY);
    PortTradeRequest request = new PortTradeRequest(giving, receiving, decks);
    try {
      port.executePortTrade(getCurrentPlayer(), board, request);
    } catch (EmptyDeckException e) {
      throw new IllegalStateException("Bank has insufficient resources for this trade.");
    }
  }
}