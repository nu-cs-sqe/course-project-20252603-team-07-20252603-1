package domain;

public class BoardHandler {
    void buildSettlement(Player player, int nodeId){

    }

    void buildCity(Player player, int nodeId){

    }

    void addRoad(int edgeId){

    }

    Boolean validateSettlementResources(Player player){
        return false;
    }

    Boolean validateCityResources(Player player){
        return false;
    }

    Boolean validateRoadResources(Player player){
        return false;
    }

    void spendSettlementResources(Player player){
    }

    void spendCityResources(Player player){}

    void spendRoadResources(Player player){}

    void awardResources(int rollNum){

    }

    void moveRobber(int hexId){

    }

    void getPlayersOnHex(int hexId){

    }

    int buildSetupSettlement(Player player, int nodeId){
        return 0;
    }

    void buildSetupRoad(int edgeId){

    }

    Player calculateLongestRoad(){
        // TODO - after gamesetup phase
        return new Player();
    }
}
