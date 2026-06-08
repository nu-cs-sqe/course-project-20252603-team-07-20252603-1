package domain;

import java.util.*;

public class BoardHandler {
    // static fields to represent the type of building on a node
    private static final int SETTLEMENT_LEVEL = 1;
    private static final int CITY_LEVEL = 2;
    private static final int MIN_HEX_ID = 0;
    private static final int MAX_HEX_ID = 18;

    private BoardGraphController boardGraphController;
    private List<Hex> hexes;
    private Map<Integer, List<Integer>> nodeIdToHexes;
    private int[] nodeBuildingLevels;
    private PlayerColor[] nodeOwners;
    private Robber robber;

    // public constructor
    public BoardHandler() {
        BoardGraph constructorGraph = new BoardGraph();
        this.boardGraphController = new BoardGraphController(constructorGraph);
        this.hexes = initHexes();
        this.nodeIdToHexes = initNodeHexMap();
        this.nodeBuildingLevels = new int[54];
        this.nodeOwners = new PlayerColor[54];
        Arrays.fill(this.nodeOwners, PlayerColor.SETUP);
        this.robber = new Robber(9);
    }

    // private constructor for testing
    private BoardHandler(BoardGraphController boardGraphController, List<Hex> hexes, Map<Integer, List<Integer>> nodeIdToHexes, Robber robber) {
        this.boardGraphController = boardGraphController;
        this.hexes = hexes;
        this.nodeIdToHexes = nodeIdToHexes;
        this.nodeBuildingLevels = new int[54];
        this.nodeOwners = new PlayerColor[54];
        Arrays.fill(this.nodeOwners, PlayerColor.SETUP);
        this.robber = robber;
    }

    // static factory method that calls the private constructor
    public static BoardHandler createForTesting(BoardGraphController boardGraphController, List<Hex> hexes, Map<Integer, List<Integer>> nodeIdToHexes, Robber robber) {
        return new BoardHandler(boardGraphController, hexes, nodeIdToHexes, robber);
    }

    void buildSettlement(Player player, int nodeId){
        if (nodeId < 0 || nodeId > 53){
            throw new IllegalArgumentException("Invalid NodeID - must be within [0, 53].");
        }
        PlayerColor claimingColor = player.getPlayerColor();
        boardGraphController.playerClaimStoredNode(claimingColor, nodeId);
        List<Integer> hexIds = nodeIdToHexes.get(nodeId);
        for (int hexId : hexIds){
            hexes.get(hexId).addPlayerSettlementToHex(player);
        }
        nodeOwners[nodeId] = claimingColor;
        nodeBuildingLevels[nodeId] = SETTLEMENT_LEVEL;
    }

    boolean checkPlayerOwnsNode(PlayerColor playerColor, Integer nodeId){
        return nodeOwners[nodeId] == playerColor;
    }

    Integer getNodeBuildingLevel(Integer nodeId){
        return nodeBuildingLevels[nodeId];
    }

    void buildCity(Player player, int nodeId){
        if (nodeId < 0 || nodeId > 53){
            throw new IllegalArgumentException("Invalid NodeID - must be within [0, 53].");
        }

        PlayerColor claimingColor = player.getPlayerColor();
        if (!checkPlayerOwnsNode(claimingColor, nodeId) && getNodeBuildingLevel(nodeId) != 0){
            throw new IllegalStateException("Node owned by other player, cannot build here.");
        }

        if (getNodeBuildingLevel(nodeId) != SETTLEMENT_LEVEL){
            throw new IllegalStateException("Must upgrade a settlement to a city.");
        }

        List<Integer> hexIds = nodeIdToHexes.get(nodeId);
        for (int hexId : hexIds){
            hexes.get(hexId).removePlayerSettlementFromHex(player);
            hexes.get(hexId).addPlayerCityToHex(player);
        }
        nodeBuildingLevels[nodeId] = CITY_LEVEL;
    }

    void addRoad(Player player, int nodeId1, int nodeId2){
        if (nodeId1 < 0 || nodeId2 < 0 || nodeId1 > 53 || nodeId2 > 53){
            throw new IllegalArgumentException("Edge nodeId out of bounds. Must be within [0, 53].");
        }
        PlayerColor claimingColor = player.getPlayerColor();
        boardGraphController.playerClaimStoredEdge(claimingColor, nodeId1, nodeId2);
    }

    void awardResources(int rollNum){
        int robberLocation = robber.getRobberLocation();

        for (Hex hex : hexes) {
            int curHexId = hex.getHexId();
            if (hex.getHexRollNum() == rollNum && robberLocation != curHexId){
                hexes.get(curHexId).awardSettlementResources();
                hexes.get(curHexId).awardCityResources();
            }
        }
    }

    void moveRobber(int hexId){
        if (hexId < MIN_HEX_ID || hexId > MAX_HEX_ID){
            throw new IllegalArgumentException("Cannot move Robber to invalid Hex ID");
        }

        int previousRobberLocation = robber.getRobberLocation();
        if (previousRobberLocation == hexId){
            throw new IllegalArgumentException("Must move robber to new location");
        }

        robber.moveRobber(hexId);
    }

    Set<Player> getPlayersOnHex(int hexId){
        Hex curHex = hexes.get(hexId);

        Set<Player> playersOnHex = new HashSet<>();
        playersOnHex.addAll(curHex.getHexSettlementPlayers());
        playersOnHex.addAll(curHex.getHexCityPlayers());

        return playersOnHex;
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

    static Map<Integer, List<Integer>> initNodeHexMap(){
        Map<Integer, List<Integer>> nodeHexMap = new HashMap<>();
        nodeHexMap.put(0, List.of(0));
        nodeHexMap.put(1, List.of(1));
        nodeHexMap.put(2, List.of(2));
        nodeHexMap.put(3, List.of(0));
        nodeHexMap.put(4, List.of(0, 1));
        nodeHexMap.put(5, List.of(1, 2));
        nodeHexMap.put(6, List.of(2));
        nodeHexMap.put(7, List.of(0, 3));
        nodeHexMap.put(8, List.of(0, 1, 4));
        nodeHexMap.put(9, List.of(1, 2, 5));
        nodeHexMap.put(10, List.of(2, 6));
        nodeHexMap.put(11, List.of(3));
        nodeHexMap.put(12, List.of(0, 3, 4));
        nodeHexMap.put(13, List.of(1, 4, 5));
        nodeHexMap.put(14, List.of(2, 5, 6));
        nodeHexMap.put(15, List.of(6));
        nodeHexMap.put(16, List.of(3, 7));
        nodeHexMap.put(17, List.of(3, 4, 8));
        nodeHexMap.put(18, List.of(4, 5, 9));
        nodeHexMap.put(19, List.of(5, 6, 10));
        nodeHexMap.put(20, List.of(6, 11));
        nodeHexMap.put(21, List.of(7));
        nodeHexMap.put(22, List.of(3, 7, 8));
        nodeHexMap.put(23, List.of(4, 8, 9));
        nodeHexMap.put(24, List.of(5, 9, 10));
        nodeHexMap.put(25, List.of(6, 10, 11));
        nodeHexMap.put(26, List.of(11));
        nodeHexMap.put(27, List.of(7));
        nodeHexMap.put(28, List.of(7, 8, 12));
        nodeHexMap.put(29, List.of(8, 9, 13));
        nodeHexMap.put(30, List.of(9, 10, 14));
        nodeHexMap.put(31, List.of(10, 11, 15));
        nodeHexMap.put(32, List.of(11));
        nodeHexMap.put(33, List.of(7, 12));
        nodeHexMap.put(34, List.of(8, 12, 13));
        nodeHexMap.put(35, List.of(9, 13, 14));
        nodeHexMap.put(36, List.of(10, 14, 15));
        nodeHexMap.put(37, List.of(11, 15));
        nodeHexMap.put(38, List.of(12));
        nodeHexMap.put(39, List.of(12, 13, 16));
        nodeHexMap.put(40, List.of(13, 14, 17));
        nodeHexMap.put(41, List.of(14, 15, 18));
        nodeHexMap.put(42, List.of(15));
        nodeHexMap.put(43, List.of(12, 16));
        nodeHexMap.put(44, List.of(13, 16, 17));
        nodeHexMap.put(45, List.of(14, 17, 18));
        nodeHexMap.put(46, List.of(15, 18));
        nodeHexMap.put(47, List.of(16));
        nodeHexMap.put(48, List.of(16, 17));
        nodeHexMap.put(49, List.of(17, 18));
        nodeHexMap.put(50, List.of(18));
        nodeHexMap.put(51, List.of(16));
        nodeHexMap.put(52, List.of(17));
        nodeHexMap.put(53, List.of(18));
        return nodeHexMap;
    }


}
