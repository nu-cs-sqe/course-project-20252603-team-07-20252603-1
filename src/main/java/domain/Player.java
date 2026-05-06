package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player {
    private final List<Settlement> settlements;
    private final List<Edge> roads;

    public Player() {
        this.settlements = new ArrayList<>();
        this.roads = new ArrayList<>();
    }

    public List<Settlement> getSettlements() {
        return Collections.unmodifiableList(settlements);
    }

    // TODO: consider refactoring this code later for legibility
    public void placeSettlement(Vertex vertex) {
        // validate vertex before adding settlement
        if (vertex == null)
            throw new IllegalArgumentException("Vertex cannot be null");
        if (settlements.size() >= 5)
            throw new IllegalStateException("No settlements remaining.");
        if (vertex.isOccupied())
            throw new IllegalArgumentException("Vertex is already occupied.");
        if (vertex.hasAdjacentSettlementViolatingDistanceRule())
            throw new IllegalArgumentException("Settlement violates the distance rule.");

        // add settlement to player's settlements list
        settlements.add(new Settlement());
    }

    public List<Edge> getRoads() {
        return Collections.unmodifiableList(roads);
    }

    public void placeRoad(Edge edge) {
        if (edge == null)
            throw new IllegalArgumentException("Edge cannot be null.");
        if (edge.isOccupied())
            throw new IllegalArgumentException("Edge is already occupied.");
        edge.isConnectedToPlayerNetwork();

        roads.add(edge);
    }
}