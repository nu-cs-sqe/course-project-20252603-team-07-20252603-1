package domain.model;

import domain.model.board.BoardHandler;
import domain.model.board.GraphEdge;
import domain.model.player.PlayerColor;
import domain.model.resources.ResourceDeck;
import domain.model.game_pieces.DiceHandler;
import domain.model.game_pieces.Die;
import domain.model.player.Player;
import domain.model.player.PlayerState;
import domain.model.resources.Resource;
import domain.model.resources.Resources;

import java.util.*;
import java.util.stream.Collectors;

public class GameModel {

    private BoardHandler board;

    //private final List<PlayerState> playerStates; // figure out how to initialize proper
    // private final DiceHandler diceHandler = initializeDiceHandler();

    private int currentPlayerIndex;
    private List<PlayerColor> playerColors;
    private PlayerColor currentPlayerColor;
    private final Map<PlayerColor, PlayerState> playerColorToPlayerState = new HashMap<>();


    private final ResourceDeck lumberDeck = new ResourceDeck(Resource.LUMBER);
    private final ResourceDeck brickDeck = new ResourceDeck(Resource.BRICK);
    private final ResourceDeck grainDeck = new ResourceDeck(Resource.GRAIN);
    private final ResourceDeck oreDeck = new ResourceDeck(Resource.ORE);
    private final ResourceDeck woolDeck = new ResourceDeck(Resource.WOOL);
    private final Map<Resource, ResourceDeck> decks = Map.of(
        Resource.LUMBER, lumberDeck,
        Resource.BRICK, brickDeck,
        Resource.GRAIN, grainDeck,
        Resource.WOOL, woolDeck,
        Resource.ORE, oreDeck
    );



    

    public GameModel(List<Player> players, BoardHandler board) {
        this.board = board;
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

        // interpret the rol result somehow
        Resource rslt = interpretRoll(roll);

        // Give current player one resource (minimal stub - ignoring dice result for now)
        try {

            Resource card = decks.get(rslt).draw();
            playerColorToPlayerState.get(currentPlayerColor).addResource(card);

        } catch (Exception e) {
            // Gracefully handle empty deck - no resource distributed
        }
    }

    public Resource interpretRoll(int roll) {
        // just a fakey function to make performTurn not error
        // really this would be closer to something like Map<Hex, (Player[], Resource)>
        return Resource.WOOL;
    }

    // Functionalities to be added
    public void attemptBuildSettlement(int nodeID){
        for (Resource r : EnumSet.of(Resource.BRICK, Resource.LUMBER, Resource.WOOL, Resource.GRAIN)) {
            checkPlayerOwnsEnoughResources(currentPlayerColor, r, 1);
        }
        board.buildSettlement(currentPlayerColor, nodeID);
        for (Resource r : EnumSet.of(Resource.BRICK, Resource.LUMBER, Resource.WOOL, Resource.GRAIN)) {
            reducePlayerResources(currentPlayerColor, r, 1);
        }
    };

    private void checkPlayerOwnsEnoughResources(PlayerColor currentPlayerColor, Resource type, int amount){
    }

    private void reducePlayerResources(PlayerColor currentPlayerColor, Resource r, int amount) {
        PlayerState releventPlayerState = playerColorToPlayerState.get(currentPlayerColor);
        releventPlayerState.reduceResources(r, amount);
    }

    public void attemptBuildCity(){};

    public void attemptBuildRoad(){};

    public void attemptTrade(){};

    public void playDevCard(){};

    public void buyDevCard(){};

    public void moveRobberAndSteal(){};

}
