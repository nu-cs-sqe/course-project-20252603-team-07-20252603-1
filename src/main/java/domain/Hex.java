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

    public void addPlayerSettlementToHex(PlayerColor player) {
        if (playerSettlements.size() >= 3){
            throw new IllegalStateException("Already three settlements on hex.");
        }
        else{
            playerSettlements.add(player);
        }
    }

    public void removePlayerSettlementFromHex(PlayerColor player) {
        boolean success = playerSettlements.remove(player);
        if (!success){
            throw new IllegalStateException("Player does not have a building on hex.");
        }
    }

    public void addPlayerCityToHex(String playerName){
        return;
    }

    public void awardSettlementResources(){
        return;
    }

    public void awardCityResources(){
        return;
    }




}
