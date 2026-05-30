package domain.model;

import domain.model.resources.ResourceCard;
import domain.model.resources.ResourceType;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents mutable game state for a player.
 * Wraps the immutable Player and tracks resources and other game state.
 */
public class PlayerState {

    private final Player player;
    private final List<ResourceCard> resources;

    public PlayerState(Player player) {
        this.player = player;
        this.resources = new ArrayList<>();
    }

    public Player getPlayer() {
        return player;
    }

    public void addResource(ResourceCard card) {
        resources.add(card);
    }

    public int getResourceCount(ResourceType type) {
        return (int) resources.stream()
                .filter(card -> card.getType() == type)
                .count();
    }
}
