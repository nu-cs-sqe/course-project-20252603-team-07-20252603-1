package domain;

import java.util.ArrayList;
import java.util.List;

public class BoardHandler {
    private BoardGraphController boardGraphController;
    private List<Hex> hexes;

    // private constructor for testing
    private BoardHandler(BoardGraphController boardGraphController, List<Hex> hexes) {
        this.boardGraphController = boardGraphController;
        this.hexes = hexes;
    }

    // static factory method that calls the private constructor
    public static BoardHandler createForTesting(BoardGraphController boardGraphController, List<Hex> hexes) {
        return new BoardHandler(boardGraphController, hexes);
    }

    // public constructor
    public BoardHandler() {
        BoardGraph constructorGraph = new BoardGraph();
        this.boardGraphController = new BoardGraphController(constructorGraph);
        this.hexes = initHexes();
    }

    private List<Hex> initHexes(){
        List<Hex> hexes = new ArrayList<>(List.of(
                new Hex(0,  Resource.ORE,    10),
                new Hex(1,  Resource.WOOL,    2),
                new Hex(2,  Resource.LUMBER,  9),
                new Hex(3,  Resource.GRAIN,  12),
                new Hex(4,  Resource.BRICK,   6),
                new Hex(5,  Resource.WOOL,    4),
                new Hex(6,  Resource.BRICK,  10),
                new Hex(7,  Resource.GRAIN,   9),
                new Hex(8,  Resource.LUMBER, 11),
                new Hex(9,  Resource.DESERT,  7),
                new Hex(10, Resource.LUMBER,  3),
                new Hex(11, Resource.ORE,     8),
                new Hex(12, Resource.LUMBER,    8),
                new Hex(13, Resource.ORE,     3),
                new Hex(14, Resource.GRAIN,   4),
                new Hex(15, Resource.WOOL,   5),
                new Hex(16, Resource.BRICK,   5),
                new Hex(17, Resource.GRAIN,   6),
                new Hex(18, Resource.LUMBER,  11)
        ));
        return hexes;
    }

    void buildSettlement(Player player, int nodeId){
        PlayerColor claimingColor = player.getPlayerColor();
        boardGraphController.playerClaimStoredNode(claimingColor, nodeId);
        hexes.get(nodeId).addPlayerSettlementToHex(player);
    }

    void buildCity(Player player, int nodeId){

    }

    void addRoad(int edgeId){

    }

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

    void buildHexes(){

    }
}
