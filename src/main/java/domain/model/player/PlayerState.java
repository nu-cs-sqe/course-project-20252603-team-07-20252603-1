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
    private final List<Resource> resources;

    public PlayerState(Player player) {
        this.player = player;
        this.resources = new ArrayList<>();
    }

    public Player getPlayer() {
        return player;
    }

    public void addResource(Resource card) {
        resources.add(card);
    }

    public int getResourceCount(Resource type) {
        return (int) resources.stream()
                .filter(card -> card == type)
                .count();
    }
}
