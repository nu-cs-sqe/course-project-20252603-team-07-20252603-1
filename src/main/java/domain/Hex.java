package domain;

import java.util.ArrayList;
import java.util.List;

public class Hex {
    private final int hexId;
    private final String resource;
    private final int hexRollNum;
    private List<String> playerSettlements;
    private List<String> playerCities;

    public Hex(int hexId, String resource, int rollNumber) {
        this.hexId = hexId;
        this.resource = resource;
        this.hexRollNum = rollNumber;
        this.playerSettlements = new ArrayList<>();
        this.playerCities = new ArrayList<>();
    }

    public List<String> getSettlements(){
        List<String> settlementsCopy = new ArrayList<>(playerSettlements);
        return settlementsCopy;
    }

    public void addPlayerSettlementToHex(String playerName) {
        if (playerSettlements.size() >= 3){
            throw new IllegalStateException("Already three settlements on hex.");
        }
        else{
            playerSettlements.add(playerName);
        }
    }

    public void removePlayerSettlementFromHex(String playerName){
        throw new IllegalStateException("Player does not have a building on hex.");
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
