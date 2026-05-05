package domain;

import java.util.ArrayList;
import java.util.List;

public class Hex {
    public final int hexId;
    public final Resource resource;
    public final int hexRollNum;
    private List<Player> playerSettlements;
    private List<Player> playerCities;
    private int totalBuildingsOnHex;

    public Hex(int hexId, Resource resource, int rollNumber) {
        if (hexId < 0 || hexId > 18){
            throw new IllegalArgumentException("Invalid Hex - hexId must be within [0, 18].");
        }
        this.hexId = hexId;
        this.resource = resource;
        if (rollNumber < 2 || rollNumber > 12){
            throw new IllegalArgumentException("Invalid Hex - rollNumber must be within [2, 12].");
        }
        this.hexRollNum = rollNumber;
        this.playerSettlements = new ArrayList<>();
        this.playerCities = new ArrayList<>();
        this.totalBuildingsOnHex = 0;
    }

    public int getSettlementCount(){return playerSettlements.size();}

    public int getCityCount(){
        return playerCities.size();
    }

    public boolean isPlayerSettlementOnHex(Player player){
        return playerSettlements.contains(player);
    }

    public boolean isPlayerCityOnHex(Player player){
        return playerCities.contains(player);
    }

    public void addPlayerSettlementToHex(Player player) {
        if (this.totalBuildingsOnHex >= 3){
            throw new IllegalStateException("Already three buildings on hex.");
        }
        else if (player == null){
            throw new IllegalArgumentException("Adding invalid player name to Hex.");
        }
        else{
            playerSettlements.add(player);
            this.totalBuildingsOnHex++;
        }
    }

    public void removePlayerSettlementFromHex(Player player) {
        boolean success = playerSettlements.remove(player);
        if (!success){
            throw new IllegalArgumentException("Player does not have a settlement on hex.");
        }
        else{
            this.totalBuildingsOnHex--;
        }
    }

    public void addPlayerCityToHex(Player player){
        if (this.totalBuildingsOnHex >= 3){
            throw new IllegalStateException("Already three buildings on hex.");
        }
        else if (player == null){
            throw new IllegalArgumentException("Adding invalid player name to Hex.");
        }
        else{
            playerCities.add(player);
            this.totalBuildingsOnHex++;
        }
    }

    public void awardSettlementResources(){
        if (resource != Resource.DESERT) {
            playerSettlements.forEach(player -> {
                player.updateResources(resource, 1);
            });
        }
    }

    public void awardCityResources() {
        if (resource != Resource.DESERT) {
            playerCities.forEach(player -> {
                player.updateResources(resource, 2);
            });
        }
    }




}
