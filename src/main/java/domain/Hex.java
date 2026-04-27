package domain;

import java.util.ArrayList;
import java.util.List;

public class Hex {
    public final int hexId;
    public final String resource;
    public final int hexRollNum;
    private List<PlayerColor> playerSettlements;
    private List<PlayerColor> playerCities;

    public Hex(int hexId, String resource, int rollNumber) {
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

    public void addPlayerSettlementToHex(PlayerColor player) {
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

    public void removePlayerSettlementFromHex(PlayerColor player) {
        boolean success = playerSettlements.remove(player);
        if (!success){
            throw new IllegalArgumentException("Player does not have a building on hex.");
        }
    }

    public void addPlayerCityToHex(PlayerColor player){
        if (playerCities.size() >= 3){
            throw new IllegalStateException("Already three buildings on hex.");
        }
        else{
            playerCities.add(player);
        }
    }

    public void awardSettlementResources(){
        return;
    }

    public void awardCityResources(){
        return;
    }




}
