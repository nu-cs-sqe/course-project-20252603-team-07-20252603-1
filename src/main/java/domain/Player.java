package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player {
    private final List<Settlement> settlements;
    private final List<Edge> roads;
    private final Map<ResourceType, Integer> resources;

    public Player() {
        this.settlements = new ArrayList<>();
        this.roads = new ArrayList<>();
        this.resources = new HashMap<>();
    }

    public Map<ResourceType, Integer> getResources() {
        return Collections.unmodifiableMap(resources);
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

    // TODO: consider refactoring this code later for legibility
    public void placeRoad(Edge edge) {
        // validate road conditions
        if (edge == null)
            throw new IllegalArgumentException("Edge cannot be null.");
        if (roads.size() >= 15)
            throw new IllegalStateException("No roads remaining.");
        if (edge.isOccupied())
            throw new IllegalArgumentException("Edge is already occupied.");
        if (!edge.isConnectedToPlayerNetwork())
            throw new IllegalArgumentException("Road must connect to player's existing network.");

        // add edge to list of roads
        roads.add(edge);
    }

    public void receiveResources(Map<ResourceType, Integer> resources) {
        // validate resources before merging
        if (resources == null)
            throw new IllegalArgumentException("Resources cannot be null.");

        // merge resources into player's resources map (adds quantities if keys match, otherwise adds new key-value pair)
        for (Map.Entry<ResourceType, Integer> entry : resources.entrySet()) {
            // validate data entry
            if (entry.getValue() < 1)
                throw new IllegalArgumentException("Resource quantity must be at least 1.");

            // merge new resources
            this.resources.merge(entry.getKey(), entry.getValue(), Integer::sum);
        }
    }
}