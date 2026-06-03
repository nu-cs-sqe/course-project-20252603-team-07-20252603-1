package domain.model;

import domain.model.board.BoardHandler;
import domain.model.exceptions.IllegalGamePhaseException;
import domain.model.exceptions.IllegalSettlementPlacementException;
import domain.model.player.PlayerColor;
import domain.model.resources.ResourceDeck;
import domain.model.game_pieces.DiceHandler;
import domain.model.game_pieces.Die;
import domain.model.player.Player;
import domain.model.player.PlayerState;
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
    private Map<PlayerColor, PlayerState> playerColorToPlayerState = new HashMap<>();


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
              Map<PlayerColor, PlayerState> playerColorToPlayerState,
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
        this.playerColorToPlayerState = playerColorToPlayerState;
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
            PlayerState newState = new PlayerState(player);
            PlayerColor currentColor = player.getColor();
            this.playerColorToPlayerState.put(currentColor, newState);
            playerColors.add(currentColor);
        }
        this.currentPlayerIndex = 0;
        this.currentPlayerColor = playerColors.get(0);
    }


    private DiceHandler initializeDiceHandler() { // shouldn't this be a method of DiceHandler?
        Random r = new Random();
        Die d1 = new Die(r);
        Die d2 = new Die(r);

        return new DiceHandler(d1, d2);

    }

    public List<Player> getTurnOrder() {
        return playerColors.stream()
                .map(color -> playerColorToPlayerState.get(color))
                .map(PlayerState::getPlayer)
                .collect(Collectors.toList());
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public Player getCurrentPlayer() {
        return playerColorToPlayerState.get(currentPlayerColor).getPlayer();
    }

    public void setCurrentPlayerColor(PlayerColor color) {
        this.currentPlayerColor = color;
    }

    public void advanceToNextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % playerColors.size();
        currentPlayerColor = playerColors.get(currentPlayerIndex);
    }

    public PlayerState getPlayerState(PlayerColor color) {
        return playerColorToPlayerState.get(color);
    }

    public void performTurn(int roll) { // takes in the dice roll, doesn't perform it.
        // Roll dice
        // int roll = diceHandler.rollTwoDice();
        currentGamePhase = GamePhase.RESOURCE_PRODUCTION;

        // interpret the rol result somehow
        Resource rslt = interpretRoll(roll);

        // Give current player one resource (minimal stub - ignoring dice result for now)
        try {

            Resource card = decks.get(rslt).draw();
            playerColorToPlayerState.get(currentPlayerColor).addResource(card);

        } catch (Exception e) {
            // Gracefully handle empty deck - no resource distributed
            throw new IllegalArgumentException(e.getMessage()); // placeholder to placate spotbugs
        }
    }

    public Resource interpretRoll(int roll) {
        // just a fakey function to make performTurn not error
        // really this would be closer to something like Map<Hex, (Player[], Resource)>
        // Rewarding resources on is the responsibility of the tile, just cause lowkey
        return Resource.WOOL;
    }

    // SPENCER METHODS

    public void attemptBuildSettlement(int nodeID){
        checkCurrentGamePhaseMatches(GamePhase.GENERAL_PLAY);
        checkIfPlayerAtMaxSettlements();
        for (Resource r : EnumSet.of(Resource.BRICK, Resource.LUMBER, Resource.WOOL, Resource.GRAIN)) {
            checkPlayerOwnsEnoughResources(currentPlayerColor, r, 1);
        }
        board.buildSettlement(currentPlayerColor, nodeID);
        for (Resource r : EnumSet.of(Resource.BRICK, Resource.LUMBER, Resource.WOOL, Resource.GRAIN)) {
            reducePlayerResources(currentPlayerColor, r, 1);
            ResourceDeck deckToReplenish = decks.get(r);
            deckToReplenish.replenish();
        }
        incrementNumSettlements(currentPlayerColor);
    }

    public void attemptBuildRoad(int startingNodeID, int endingNodeID){
        checkCurrentGamePhaseMatches(GamePhase.GENERAL_PLAY);
        for (Resource r : EnumSet.of(Resource.BRICK, Resource.LUMBER)) {
            checkPlayerOwnsEnoughResources(currentPlayerColor, r, 1);
        }
        board.addRoad(currentPlayerColor, startingNodeID, endingNodeID);
        for (Resource r : EnumSet.of(Resource.BRICK, Resource.LUMBER)) {
            reducePlayerResources(currentPlayerColor, r, 1);
            ResourceDeck deckToReplenish = decks.get(r);
            deckToReplenish.replenish();
        }
    };

    void setCurrentGamePhase(GamePhase newGamePhase) {
        this.currentGamePhase = newGamePhase;
    }

    private void checkCurrentGamePhaseMatches(GamePhase expectedGamePhase) {
        if(currentGamePhase != expectedGamePhase) {
            throw new IllegalGamePhaseException("Not proper phase for that action");
        }
    }

    private void incrementNumSettlements(PlayerColor currentPlayerColor) {
        PlayerState relevantPlayerState = getPlayerState(currentPlayerColor);
        relevantPlayerState.increaseSettlementCount();
    }

    private void checkIfPlayerAtMaxSettlements() {
        PlayerState relevantPlayerState = getPlayerState(currentPlayerColor);
        int currentAmountSettlements = relevantPlayerState.getSettlementCount();
        if (currentAmountSettlements >= 5) {
            throw new IllegalSettlementPlacementException("Can not have more than 5 settlements");
        }
    }

    private void checkPlayerOwnsEnoughResources(PlayerColor currentPlayerColor, Resource type, int amountNeeded){
        PlayerState relevantPlayerState = getPlayerState(currentPlayerColor);
        int amountPlayerOwnsResource = relevantPlayerState.getResourceCount(type);
        if (amountPlayerOwnsResource < amountNeeded) {
            throw new InsufficientResourcesException("Insufficient resources");
        }
    }

    private void reducePlayerResources(PlayerColor currentPlayerColor, Resource r, int amount) {
        PlayerState releventPlayerState = playerColorToPlayerState.get(currentPlayerColor);
        releventPlayerState.reduceResources(r, amount);
    }

    public void attemptBuildCity(){};


    public void attemptTrade(){};

    public void playDevCard(){};

    public void buyDevCard(){};

    public void moveRobberAndSteal(){};

}
