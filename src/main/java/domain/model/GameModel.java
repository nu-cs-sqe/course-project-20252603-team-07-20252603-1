package domain.model;

import domain.model.resources.ResourceDeck;
import domain.model.game_pieces.DiceHandler;
import domain.model.game_pieces.Die;
import domain.model.player.Player;
import domain.model.player.PlayerState;
import domain.model.resources.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.Map;

public class GameModel {

    private final List<PlayerState> playerStates; // figure out how to initialize proper
    // private final DiceHandler diceHandler = initializeDiceHandler();

    private int currentPlayerIndex;

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



    

    public GameModel(List<Player> players) {
        this.playerStates = new ArrayList<>();
        for (Player player : players) {
            this.playerStates.add(new PlayerState(player));
        }
        this.currentPlayerIndex = 0;
    }


    private DiceHandler initializeDiceHandler() { // shouldn't this be a method of DiceHandler?
        Random r = new Random();
        Die d1 = new Die(r);
        Die d2 = new Die(r);

        return new DiceHandler(d1, d2);

    }

    public List<Player> getTurnOrder() {
        return playerStates.stream()
                .map(PlayerState::getPlayer)
                .collect(Collectors.toList());
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public Player getCurrentPlayer() {
        return playerStates.get(currentPlayerIndex).getPlayer();
    }

    public void advanceToNextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % playerStates.size();
    }

    public PlayerState getPlayerState(int index) {
        return playerStates.get(index);
    }

    public void performTurn(int roll) { // takes in the dice roll, doesn't perform it.
        // Roll dice
        // int roll = diceHandler.rollTwoDice();

        // interpret the rol result somehow
        Resource rslt = interpretRoll(roll);

        // Give current player one resource (minimal stub - ignoring dice result for now)
        try {

            Resource card = decks.get(rslt).draw();
            playerStates.get(currentPlayerIndex).addResource(card);

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
    public void attemptBuildSettlement(){};

    public void attemptBuildCity(){};

    public void attemptBuildRoad(){};

    public void attemptTrade(){};

    public void playDevCard(){};

    public void buyDevCard(){};

    public void moveRobberAndSteal(){};

}
