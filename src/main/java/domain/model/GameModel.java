package domain.model;

import domain.model.board.BoardHandler;
import domain.model.board.Port;
import domain.model.board.PortTradeRequest;
import domain.model.exceptions.*;
import domain.model.player.PlayerColor;
import domain.model.player.TradeManager;
import domain.model.player.TradeOffer;
import domain.model.resources.ResourceDeck;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import domain.model.player.Player;
import domain.model.resources.Resource;

import java.util.*;
import java.util.stream.Collectors;

public class GameModel {

    private static final int ROBBER_ROLL_VALUE = 7;
    private static final int MAX_AMOUNT_SETTLEMENTS = 5;
    private static final int MIN_POINTS_TO_WIN_GAME = 10;
    private static final int POINTS_FOR_SETTLEMENT = 1;
    private static final int POINTS_FOR_CITY = 1;
    private static final int POINTS_FOR_LONGEST_ROAD = 2;

    @SuppressFBWarnings(value = "EI_EXPOSE_REP2",
            justification = "BoardHandler is intentionally shared between GameSetupModel and GameModel as it represents the single game board state")
    private final BoardHandler board;
    private GamePhase currentGamePhase;
    private int currentPlayerIndex;
    private int currentRound = 0;
    private List<PlayerColor> playerColors;
    private PlayerColor currentPlayerColor;
    private Map<PlayerColor, Player> playerColorToPlayerObject = new HashMap<>();
    private Map<PlayerColor, Integer> playerColorToLastClaimedNodeID = new HashMap<>();
    private PlayerColor currentLongestRoadPlayerColor;


    private final ResourceDeck lumberDeck;
    private final ResourceDeck brickDeck;
    private final ResourceDeck grainDeck;
    private final ResourceDeck oreDeck;
    private final ResourceDeck woolDeck;
    private final Map<Resource, ResourceDeck> decks;
    private final TradeManager tradeManager;

    //constructor for injecting mocks/stubs
    GameModel(ResourceDeck lumberDeck, ResourceDeck brickDeck,
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
            this.playerColorToLastClaimedNodeID.put(currentColor, -1);
            this.playerColorToPlayerObject.put(currentColor, player);
            playerColors.add(currentColor);
        }
        this.currentPlayerIndex = 0;
        this.currentPlayerColor = playerColors.get(0);
        this.currentLongestRoadPlayerColor = PlayerColor.SETUP;
        this.currentGamePhase = GamePhase.BEFORE_ROLL;
        this.tradeManager = new TradeManager();
    }

    public List<Player> getTurnOrder() {
        return playerColors.stream()
                .map(color -> playerColorToPlayerObject.get(color))
                .collect(Collectors.toList());
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public Player getCurrentPlayer() {
        return playerColorToPlayerObject.get(currentPlayerColor);
    }

    public int getCurrentRound() {
        return currentRound;
    }

public List<Player> getOtherPlayers() {
        Player current = getCurrentPlayer();
        return playerColorToPlayerObject.values().stream()
                .filter(p -> p != current)
                .collect(Collectors.toList());
    }

    public PlayerColor getCurrentPlayerColor() { return this.currentPlayerColor; }

    public void setCurrentPlayerIndex(int newIndex) { this.currentPlayerIndex = newIndex; }

    public void setCurrentPlayerColor(PlayerColor color) {
        this.currentPlayerColor = color;
    }

    public void endTurn() {
        checkCurrentGamePhaseMatches(GamePhase.GENERAL_PLAY);
        checkCurrentPlayerHasTenOrMoreVictoryPoints();
        if (getCurrentPhase() == GamePhase.END_GAME) {
            return;
        }
        else {
            advanceToNextPlayer();
            setCurrentGamePhase(GamePhase.BEFORE_ROLL);
        }

    }
    public void advanceToNextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % playerColors.size();
        currentPlayerColor = playerColors.get(currentPlayerIndex);
    }

    public Player getArbitraryPlayer(PlayerColor color) {
        return playerColorToPlayerObject.get(color);
    }

    public void performTurn(int roll) {
        checkCurrentGamePhaseMatches(GamePhase.BEFORE_ROLL);

        if (roll == ROBBER_ROLL_VALUE) {
            currentGamePhase = GamePhase.MOVE_ROBBER;
            return;
        }

        // stub production — real distribution comes in a later part
        Resource rslt = interpretRoll(roll);
        try {
            Resource card = decks.get(rslt).draw();
            playerColorToPlayerObject.get(currentPlayerColor).updateResources(card, 1);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        currentGamePhase = GamePhase.GENERAL_PLAY;
    }

    public Resource interpretRoll(int roll) {
        // just a fakey function to make performTurn not error
        // really this would be closer to something like Map<Hex, (Player[], Resource)>
        // Rewarding resources on is the responsibility of the tile, just cause lowkey
        // Ben has rewarding resources in his hex class
        return Resource.WOOL;
    }

    public void attemptBuildSettlement(int nodeID){
        checkCurrentGamePhaseMatches(GamePhase.GENERAL_PLAY, GamePhase.SETUP_PHASE);
        if (this.currentGamePhase == GamePhase.SETUP_PHASE) {
            board.buildSetupSettlement(getCurrentPlayer(), nodeID);
            this.playerColorToLastClaimedNodeID.put(currentPlayerColor, nodeID);
            return;
        }
        checkIfPlayerAtMaxSettlements(currentPlayerColor);
        for (Resource r : EnumSet.of(Resource.BRICK, Resource.LUMBER, Resource.WOOL, Resource.GRAIN)) {
            checkPlayerOwnsEnoughResources(currentPlayerColor, r, 1); // 1s are not magic numbers?
        }
        board.buildSettlement(getCurrentPlayer(), nodeID);
        for (Resource r : EnumSet.of(Resource.BRICK, Resource.LUMBER, Resource.WOOL, Resource.GRAIN)) {
            reducePlayerResources(currentPlayerColor, r, 1);
            ResourceDeck deckToReplenish = decks.get(r);
            deckToReplenish.replenish();
        }
        incrementNumSettlements(currentPlayerColor);
        Player currentPlayer = getCurrentPlayer();
        currentPlayer.updateVictoryPoints(POINTS_FOR_SETTLEMENT);
        // Building a settlement can change who has the longest road
        handleLongestRoad();
    }

    int getPlayerLastClaimedNode(PlayerColor color) {
        return this.playerColorToLastClaimedNodeID.get(color);
    }
    public void attemptBuildRoad(int startingNodeID, int endingNodeID) {
        checkCurrentGamePhaseMatches(GamePhase.GENERAL_PLAY, GamePhase.ROAD_BUILDING_DEV_CARD, GamePhase.SETUP_PHASE);
        if (currentGamePhase == GamePhase.SETUP_PHASE) {
            board.buildSetupRoad(getCurrentPlayer(), getPlayerLastClaimedNode(currentPlayerColor), startingNodeID, endingNodeID);
            return;
        }
        else if (currentGamePhase == GamePhase.ROAD_BUILDING_DEV_CARD) {
            board.addRoad(getCurrentPlayer(), startingNodeID, endingNodeID);
            handleLongestRoad();
            return;
        }
        for (Resource r : EnumSet.of(Resource.BRICK, Resource.LUMBER)) {
            checkPlayerOwnsEnoughResources(currentPlayerColor, r, 1);
        }
        board.addRoad(getCurrentPlayer(), startingNodeID, endingNodeID);
        for (Resource r : EnumSet.of(Resource.BRICK, Resource.LUMBER)) {
            reducePlayerResources(currentPlayerColor, r, 1);
            ResourceDeck deckToReplenish = decks.get(r);
            deckToReplenish.replenish();
        }
        // building a road can edit longest road
        handleLongestRoad();
    };

    void setCurrentGamePhase(GamePhase newGamePhase) {
        this.currentGamePhase = newGamePhase;
    }

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

    private void checkPlayerOwnsEnoughResources(PlayerColor playerColorOfInterest, Resource type, int amountNeeded){
        Player relevantPlayer = getArbitraryPlayer(playerColorOfInterest);
        int amountPlayerOwnsResource = relevantPlayer.getResourceCount(type);
        if (amountPlayerOwnsResource < amountNeeded) {
            throw new InsufficientResourcesException("Insufficient resources");
        }
    }

    private void reducePlayerResources(PlayerColor playerColorOfInterest, Resource type, int amount) {
        Player relevantPlayer = playerColorToPlayerObject.get(playerColorOfInterest);
        relevantPlayer.updateResources(type, -amount);
    }


    public void attemptBuildCity(int nodeID){
        checkCurrentGamePhaseMatches(GamePhase.GENERAL_PLAY);
        checkPlayerOwnsEnoughResources(currentPlayerColor, Resource.ORE, 3);
        checkPlayerOwnsEnoughResources(currentPlayerColor, Resource.GRAIN, 2);
        try {
            board.buildCity(getCurrentPlayer(), nodeID);
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

    public PlayerColor getCurrentLongestRoadPlayerColor() {
        return this.currentLongestRoadPlayerColor;
    }

    void setCurrentLongestRoadPlayerColor(PlayerColor newLongestRoadColor) {
        this.currentLongestRoadPlayerColor = newLongestRoadColor;
    }
    public void handleLongestRoad() {
        List<Player> playerList = new ArrayList<>(playerColorToPlayerObject.values());
        PlayerColor newLongestRoadColor = board.calculateLongestRoad(playerList, currentLongestRoadPlayerColor);
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

    public void updateVictoryPoints(PlayerColor color, int amount) {
        Player relevantPlayer = getArbitraryPlayer(color);
        relevantPlayer.updateVictoryPoints(amount);
    }

    public void checkCurrentPlayerHasTenOrMoreVictoryPoints() {
        Player currentPlayer = getCurrentPlayer();
        int currentPlayerVictoryPoints = currentPlayer.getVictoryPoints();
        if (currentPlayerVictoryPoints >= MIN_POINTS_TO_WIN_GAME) {
            setCurrentGamePhase(GamePhase.END_GAME);
        }
    }

    public void offerTrade(TradeOffer offer) {
        checkCurrentGamePhaseMatches(GamePhase.GENERAL_PLAY);
        tradeManager.offerTrade(offer);
        currentGamePhase = GamePhase.OFFERING_TRADE;
    }

    public void acceptTrade(TradeOffer offer, Player acceptingPlayer){};

    public void clearOffers() {};

    public void playDevCard(){};

    public void buyDevCard(){};

    public void moveRobberAndSteal(){};

    // TODO: UI can call board to get list of available ports, and then upon the user clicking a port, it will be passed into this function
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
