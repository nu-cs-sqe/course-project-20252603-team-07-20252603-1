package domain;

import java.util.ArrayList;
import java.util.List;

public class Hex {
    public final int hexId;
    public final Resource resource;
    public final int hexRollNum;
    private List<Player> playerSettlements;
    private List<Player> playerCities;

    public Hex(int hexId, Resource resource, int rollNumber) {
        this.hexId = hexId;
        this.resource = resource;
        this.hexRollNum = rollNumber;
        this.playerSettlements = new ArrayList<>();
        this.playerCities = new ArrayList<>();
    }

    public int getSettlementCount(){
        return playerSettlements.size();
    }

    public int getCityCount(){
        return playerCities.size();
    }

    public void addPlayerSettlementToHex(Player player) {
        if (playerSettlements.size() >= 3){
            throw new IllegalStateException("Already three settlements on hex.");
        }
        else if (player == null){
            throw new IllegalArgumentException("Adding invalid player name to Hex.");
        }
        else{
            playerSettlements.add(player);
        }
    }

    public void removePlayerSettlementFromHex(Player player) {
        boolean success = playerSettlements.remove(player);
        if (!success){
            throw new IllegalArgumentException("Player does not have a building on hex.");
        }
    }

    public void addPlayerCityToHex(Player player){
        if (playerCities.size() >= 3){
            throw new IllegalStateException("Already three buildings on hex.");
        }
        else if (player == null){
            throw new IllegalArgumentException("Adding invalid player name to Hex.");
        }
        else{
            playerCities.add(player);
        }
    }

    public void awardSettlementResources(){
        if (resource != Resource.DESERT) {
            playerSettlements.forEach(player -> {
                player.updateResources(resource, 1);
            });
        }
    }

    public void awardCityResources(){
        return;
    }




}
