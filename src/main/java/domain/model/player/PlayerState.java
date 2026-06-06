package domain.model.player;

import domain.model.resources.Resource;

import java.util.*;

/**
 * Represents mutable game state for a player.
 * Wraps the immutable Player and tracks resources and other game state.
 */
public class PlayerState {

    private final Player player;
    private final PlayerColor color;
    //private final List<Resource> resources;
    private final Map<Resource, Integer> resourceCount = new HashMap<>();
    private int numSettlement;

    public PlayerState(Player player) {
        this.player = player;
        this.color = player.getColor();
        //this.resources = new ArrayList<>();
        // Connor and Ben have been defining methods for interacting
        // With the player class using the Player (not PlayerState) class
        // As such, the way that keeps track of resources is by using a Map
        // From resource Type to int (i.e. wood -> 3)
        // I've edited the methods below to use this map
        // At some point in the future, we will mutate their use of the player class
        // to use the PlayerState class, this is just in anticipation for that
        for (Resource r : Resource.values()){
            resourceCount.put(r, 0);
        }
        numSettlement = 0;
    }

    //

    public Player getPlayer() {
        return player;
    }

    public PlayerColor getColor(){
        return this.color;
    }

    // Resources with List
//    public void addResource(Resource card) {
//        resources.add(card);
//    }

//    public int getResourceCount(Resource type) {
//        return (int) resources.stream()
//                .filter(card -> card == type)
//                .count();
//    }

    // Resources with Map

    public void addResource(Resource type) {
        int oldCount = getResourceCount(type);
        int newCount = oldCount + 1;
        resourceCount.put(type, newCount);
    }
    public int getResourceCount(Resource type) {
        return resourceCount.get(type);
    }

    // Defined while working on GameModel
    //TODO
    public boolean reduceResources(Resource r, int amount) {
        return true;
    }

    // Defined while working on GameModel
    //TODO
    public int getSettlementCount() {
        return this.numSettlement;
    }

    // Defined while working on GameModel
    //TODO
    public void increaseSettlementCount() {
        this.numSettlement++;
    }
}
