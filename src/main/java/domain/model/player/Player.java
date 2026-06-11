package domain.model.player;

import domain.model.resources.Resource;
import domain.model.game_pieces.Road;
import domain.model.game_pieces.Settlement;
import domain.model.exceptions.InsufficientResourcesException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;   


public class Player {
    private final List<Settlement> settlements;
    private final List<Road> roads;
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

    public void placeSettlement() {
        if (settlements.size() >= 5)
            throw new IllegalStateException("No settlements remaining.");
        settlements.add(new Settlement());
    }

    public List<Road> getRoads() {
        return Collections.unmodifiableList(roads);
    }

    public void placeRoad() {
        if (roads.size() >= 15)
            throw new IllegalStateException("No roads remaining.");
        roads.add(new Road());
    }

    public void receiveResources(Map<Resource, Integer> resources) {
        if (resources == null)
            throw new IllegalArgumentException("Resources cannot be null.");

        for (Map.Entry<Resource, Integer> entry : resources.entrySet()) {
            if (entry.getKey() == Resource.DESERT)
                throw new IllegalArgumentException("Cannot receive DESERT as a resource.");
            if (entry.getValue() < 1)
                throw new IllegalArgumentException("Resource quantity must be at least 1.");

            this.resources.merge(entry.getKey(), entry.getValue(), Integer::sum);
        }
    }

    public String getName() {
        return this.name;
    }

    public PlayerColor getColor() {
        return this.color;
    }

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