package domain.model;

import domain.model.board.BoardHandler;
import domain.model.exceptions.IllegalCityPlacementException;
import domain.model.exceptions.IllegalGamePhaseException;
import domain.model.exceptions.IllegalSettlementPlacementException;
import domain.model.player.PlayerColor;
import domain.model.resources.ResourceDeck;
import domain.model.game_pieces.DiceHandler;
import domain.model.game_pieces.Die;
import domain.model.player.Player;
import domain.model.resources.Resource;

import domain.model.exceptions.InsufficientResourcesException;
import java.util.*;
import java.util.stream.Collectors;

public class GameModel {

    private BoardHandler board;

    //private final List<PlayerState> playerStates; // figure out how to initialize proper
    // private final DiceHandler diceHandler = initializeDiceHandler();

    private GamePhase currentGamePhase;
    private int currentPlayerIndex;
    private List<PlayerColor> playerColors;
    private PlayerColor currentPlayerColor;
    private Map<PlayerColor, Player> playerColorToPlayerObject = new HashMap<>();


    private final ResourceDeck lumberDeck;
    private final ResourceDeck brickDeck;
    private final ResourceDeck grainDeck;
    private final ResourceDeck oreDeck;
    private final ResourceDeck woolDeck;
    private final Map<Resource, ResourceDeck> decks;

    //constructor for injecting mocks/stubs
    GameModel(ResourceDeck lumberDeck, ResourceDeck brickDeck,
              ResourceDeck grainDeck, ResourceDeck oreDeck,
              ResourceDeck woolDeck,
              Map<PlayerColor, Player> playerColorToPlayerObject,
              BoardHandler board) {
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
            //PlayerState newState = new PlayerState(player);
            PlayerColor currentColor = player.getColor();
            this.playerColorToPlayerObject.put(currentColor, player);
            playerColors.add(currentColor);
        }
        this.currentPlayerIndex = 0;
        this.currentPlayerColor = playerColors.get(0);
        this.currentGamePhase = GamePhase.BEFORE_ROLL;
    }


    private DiceHandler initializeDiceHandler() { // shouldn't this be a method of DiceHandler?
        Random r = new Random();
        Die d1 = new Die(r);
        Die d2 = new Die(r);

        return new DiceHandler(d1, d2);

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

    public void setCurrentPlayerColor(PlayerColor color) {
        this.currentPlayerColor = color;
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

        if (roll == 7) {
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

    // SPENCER METHODS

    public void attemptBuildSettlement(int nodeID){
        checkCurrentGamePhaseMatches(GamePhase.GENERAL_PLAY);
        checkIfPlayerAtMaxSettlements(currentPlayerColor);
        for (Resource r : EnumSet.of(Resource.BRICK, Resource.LUMBER, Resource.WOOL, Resource.GRAIN)) {
            checkPlayerOwnsEnoughResources(currentPlayerColor, r, 1);
        }
        board.buildSettlement(getCurrentPlayer(), nodeID);
        for (Resource r : EnumSet.of(Resource.BRICK, Resource.LUMBER, Resource.WOOL, Resource.GRAIN)) {
            reducePlayerResources(currentPlayerColor, r, 1);
            ResourceDeck deckToReplenish = decks.get(r);
            deckToReplenish.replenish();
        }
        incrementNumSettlements(currentPlayerColor);
    }

    public void attemptBuildRoad(int startingNodeID, int endingNodeID) {
        checkCurrentGamePhaseMatches(GamePhase.GENERAL_PLAY, GamePhase.ROAD_BUILDING_DEV_CARD);
        for (Resource r : EnumSet.of(Resource.BRICK, Resource.LUMBER)) {
            checkPlayerOwnsEnoughResources(currentPlayerColor, r, 1);
        }
        board.addRoad(getCurrentPlayer(), startingNodeID, endingNodeID);
        for (Resource r : EnumSet.of(Resource.BRICK, Resource.LUMBER)) {
            reducePlayerResources(currentPlayerColor, r, 1);
            ResourceDeck deckToReplenish = decks.get(r);
            deckToReplenish.replenish();
        }
    };

    void setCurrentGamePhase(GamePhase newGamePhase) {
        this.currentGamePhase = newGamePhase;
    }

    public GamePhase getCurrentPhase() {
        return currentGamePhase;
    }

    public void endTurn() {
        checkCurrentGamePhaseMatches(GamePhase.GENERAL_PLAY);
        advanceToNextPlayer();
        currentGamePhase = GamePhase.BEFORE_ROLL;
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
        if (currentAmountSettlements >= 5) {
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
    };


    public void attemptTrade(){};

    public void playDevCard(){};

    public void buyDevCard(){};

    public void moveRobberAndSteal(){};

}
