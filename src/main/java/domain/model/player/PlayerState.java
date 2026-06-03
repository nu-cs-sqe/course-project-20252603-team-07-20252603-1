package domain.model.player;

import domain.model.resources.Resource;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents mutable game state for a player.
 * Wraps the immutable Player and tracks resources and other game state.
 */
public class PlayerState {

    private final Player player;
    private final PlayerColor color;
    private final List<Resource> resources;
    private int numSettlement;

    public PlayerState(Player player) {
        this.player = player;
        this.color = player.getColor();
        this.resources = new ArrayList<>();
        numSettlement = 0;
    }

    public Player getPlayer() {
        return player;
    }

    public PlayerColor getColor(){
        return this.color;
    }
    public void addResource(Resource card) {
        resources.add(card);
    }

    public int getResourceCount(Resource type) {
        return (int) resources.stream()
                .filter(card -> card == type)
                .count();
    }

    // Defined while working on GameModel
    public boolean reduceResources(Resource r, int amount) {
        return true;
    }

    // Defined while working on GameModel
    public int getSettlementCount() {
        return this.numSettlement;
    }

    // Defined while working on GameModel
    public void increaseSettlementCount() {
        this.numSettlement++;
    }
}
