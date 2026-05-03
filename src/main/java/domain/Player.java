package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player {
    private final List<Settlement> settlements;

    public Player() {
        this.settlements = new ArrayList<>();
    }

    public List<Settlement> getSettlements() {
        return Collections.unmodifiableList(settlements);
    }

    // TODO: refactor this code later for legibility
    public void placeSettlement(Vertex vertex) {
        if (vertex == null) {
            throw new IllegalArgumentException("Vertex cannot be null");
        }
        if (vertex.isOccupied()) {
            throw new IllegalArgumentException("Vertex is already occupied.");
        }
        if (vertex.hasAdjacentSettlementViolatingDistanceRule()) {
            throw new IllegalArgumentException("Settlement violates the distance rule.");
        }
        settlements.add(new Settlement());
    }
}