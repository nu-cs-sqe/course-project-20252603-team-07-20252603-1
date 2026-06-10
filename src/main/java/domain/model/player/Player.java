package domain.model.player;

import domain.model.resources.Resource;
import domain.model.board.Edge;
import domain.model.board.Vertex;
import domain.model.game_pieces.Settlement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import domain.model.exceptions.InsufficientResourcesException;


public class Player {
    private final List<Settlement> settlements;
    private final List<Edge> roads;
    private final Map<Resource, Integer> resources;
    private PlayerColor color;
    private String name;
    private int numSettlement;

    public Player(String name, PlayerColor color) {
        this.settlements = new ArrayList<>();
        this.roads = new ArrayList<>();
        this.resources = new HashMap<>();
        this.color = color;
        this.name = name;
        this.numSettlement = 0;
    }

    public Map<Resource, Integer> getResources() {
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

    public void receiveResources(Map<Resource, Integer> resources) {
        // validate resources before merging
        if (resources == null)
            throw new IllegalArgumentException("Resources cannot be null.");

        // merge resources into player's resources map (adds quantities if keys match, otherwise adds new key-value pair)
        for (Map.Entry<Resource, Integer> entry : resources.entrySet()) {
            // validate data entries
            if (entry.getKey() == Resource.DESERT)
                throw new IllegalArgumentException("Cannot receive DESERT as a resource.");
            if (entry.getValue() < 1)
                throw new IllegalArgumentException("Resource quantity must be at least 1.");

            // merge new resources
            this.resources.merge(entry.getKey(), entry.getValue(), Integer::sum);
        }
    }

    public String getName() {
        return this.name;
    }

    public PlayerColor getColor() { // this might never be necessary
        return this.color;
    }

    // these should maybe be package private
        // if so, then we should put TradeManager at least into player sub-package
    public void updateResources(Resource resource, int delta) {
        if (resource == null)
            throw new IllegalArgumentException("Resource cannot be null.");
        if (resource == Resource.DESERT)
            throw new IllegalArgumentException("Cannot update DESERT resources.");
        int newCount = getResourceCount(resource) + delta;
        if (newCount < 0)
            throw new InsufficientResourcesException("Insufficient " + resource + " resources.");
        resources.put(resource, newCount);
    }

    public int getResourceCount(Resource resource) {
        if (resource == null)
            throw new IllegalArgumentException("Resource cannot be null.");
        if (resource == Resource.DESERT)
            throw new IllegalArgumentException("Cannot get count of DESERT.");
        return resources.getOrDefault(resource, 0);
    }

    public int getTotalResourceCount() {
        return resources.values().stream().mapToInt(Integer::intValue).sum();
    }

    public void increaseSettlementCount() {
        this.numSettlement++;
    }


    public int getSettlementCount() {
        return this.numSettlement;
    }

} 
