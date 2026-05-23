package domain;

import java.util.ArrayList;
import java.util.List;

public final class Hex {
    private static final int MIN_HEX_ID = 0;
    private static final int MAX_HEX_ID = 18;

    private static final int MIN_ROLL_NUMBER = 2;
    private static final int MAX_ROLL_NUMBER = 12;
    private static final int DESERT_ROLL_NUMBER = 7;

    private static final int MAX_BUILDINGS_ON_HEX = 3;
    private static final int SETTLEMENT_RESOURCE_AMOUNT = 1;
    private static final int CITY_RESOURCE_AMOUNT = 2;

    private final int hexId;
    private final Resource resource;
    private final int hexRollNum;
    private List<Player> playerSettlements;
    private List<Player> playerCities;
    private int totalBuildingsOnHex;

    public Hex(int hexId, Resource resource, int rollNumber) {
        validateHexId(hexId);
        this.hexId = hexId;
        validateRollNum(rollNumber);
        this.hexRollNum = rollNumber;
        validateResourceAndHexNumber(resource, rollNumber);
        this.resource = resource;
        this.playerSettlements = new ArrayList<>();
        this.playerCities = new ArrayList<>();
        this.totalBuildingsOnHex = 0;
    }

    private void validateHexId(int hexId){
        if (hexId < MIN_HEX_ID || hexId > MAX_HEX_ID){
            throw new IllegalArgumentException("Invalid Hex - hexId must be within [0, 18].");
        }
    }

    private void validateRollNum(int rollNumber){
        if (rollNumber < MIN_ROLL_NUMBER || rollNumber > MAX_ROLL_NUMBER){
            throw new IllegalArgumentException("Invalid Hex - rollNumber must be within [2, 12].");
        }
    }

    private void validateResourceAndHexNumber(Resource resource, int rollNum){
        if (resource != Resource.DESERT && rollNum == DESERT_ROLL_NUMBER){
            throw new IllegalArgumentException("Invalid Hex - Only Desert Hex can have rollNumber 7");
        }
        else if (resource == Resource.DESERT && rollNum != DESERT_ROLL_NUMBER){
            throw new IllegalArgumentException("Invalid Hex - Desert Hex must have rollNumber 7.");
        }
    }

    public void addPlayerSettlementToHex(Player player) {
        if (this.totalBuildingsOnHex >= MAX_BUILDINGS_ON_HEX){
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
        if (this.totalBuildingsOnHex >= MAX_BUILDINGS_ON_HEX){
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
                player.updateResources(resource, SETTLEMENT_RESOURCE_AMOUNT);
            });
        }
    }

    public void awardCityResources() {
        if (resource != Resource.DESERT) {
            playerCities.forEach(player -> {
                player.updateResources(resource, CITY_RESOURCE_AMOUNT);
            });
        }
    }

    public int getSettlementCount(){return playerSettlements.size();}

    public int getCityCount(){
        return playerCities.size();
    }

    int getHexId(){
        return this.hexId;
    }

    Resource getHexResource(){
        return this.resource;
    }

    int getHexRollNum(){
        return this.hexRollNum;
    }

    public boolean isPlayerSettlementOnHex(Player player){
        return playerSettlements.contains(player);
    }

    public boolean isPlayerCityOnHex(Player player){
        return playerCities.contains(player);
    }
}