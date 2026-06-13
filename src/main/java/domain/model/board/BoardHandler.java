package domain.model.board;

import domain.model.gamepieces.Robber;
import domain.model.player.Player;
import domain.model.player.PlayerColor;
import domain.model.resources.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Handles all board-related operations for a game of Catan. */
public class BoardHandler {

  private static final int SETTLEMENT_LEVEL = 1;
  private static final int CITY_LEVEL = 2;
  private static final int MIN_HEX_ID = 0;
  private static final int MAX_HEX_ID = 18;
  private static final int MIN_NODE_ID = 0;
  private static final int MAX_NODE_ID = 53;
  private static final int NUM_NODES = 54;

  private BoardGraphController boardGraphController;
  private List<Hex> hexes;
  private Map<Integer, List<Integer>> nodeIdToHexes;
  private int[] nodeBuildingLevels;
  private PlayerColor[] nodeOwners;
  private Robber robber;
  private List<Port> ports;

  /** Creates a new BoardHandler with a fresh board state. */
  public BoardHandler() {
    BoardGraph constructorGraph = new BoardGraph();
    constructorGraph.buildBoard();
    this.boardGraphController = new BoardGraphController(constructorGraph);
    this.hexes = initHexes();
    this.nodeIdToHexes = initNodeHexMap();
    this.nodeBuildingLevels = new int[NUM_NODES];
    this.nodeOwners = new PlayerColor[NUM_NODES];
    Arrays.fill(this.nodeOwners, PlayerColor.SETUP);
    this.robber = new Robber(9);
    this.ports = initPorts();
  }

  private BoardHandler(
      BoardGraphController boardGraphController,
      List<Hex> hexes,
      Map<Integer, List<Integer>> nodeIdToHexes,
      Robber robber,
      List<Port> ports) {
    this.boardGraphController = boardGraphController;
    this.hexes = hexes;
    this.nodeIdToHexes = nodeIdToHexes;
    this.nodeBuildingLevels = new int[NUM_NODES];
    this.nodeOwners = new PlayerColor[NUM_NODES];
    Arrays.fill(this.nodeOwners, PlayerColor.SETUP);
    this.robber = robber;
    this.ports = ports;
  }

  /**
   * Creates a BoardHandler with injected dependencies for testing.
   *
   * @param boardGraphController the board graph controller
   * @param hexes the list of hexes
   * @param nodeIdToHexes mapping of node IDs to adjacent hex IDs
   * @param robber the robber
   * @param ports the list of ports
   * @return a new BoardHandler instance
   */
  public static BoardHandler createForTesting(
      BoardGraphController boardGraphController,
      List<Hex> hexes,
      Map<Integer, List<Integer>> nodeIdToHexes,
      Robber robber,
      List<Port> ports) {
    return new BoardHandler(boardGraphController, hexes, nodeIdToHexes, robber, ports);
  }

  /**
   * Places a settlement for the given player at the specified node.
   *
   * @param player the player building the settlement
   * @param nodeId the ID of the node to build on, must be within [0, 53]
   * @throws IllegalArgumentException if the node ID is out of bounds
   * @throws IllegalStateException if the player has no settlements remaining
   */
  public void buildSettlement(Player player, int nodeId) {
    if (nodeId < MIN_NODE_ID || nodeId > MAX_NODE_ID) {
      throw new IllegalArgumentException("Invalid NodeID - must be within [0, 53].");
    }

    PlayerColor claimingColor = player.getColor();
    boardGraphController.playerClaimStoredNode(claimingColor, nodeId);

    List<Integer> hexIds = nodeIdToHexes.get(nodeId);
    for (int hexId : hexIds) {
      hexes.get(hexId).addPlayerSettlementToHex(player);
    }

    nodeOwners[nodeId] = claimingColor;
    nodeBuildingLevels[nodeId] = SETTLEMENT_LEVEL;
    player.placeSettlement();
  }

  /**
   * Returns whether the given player color owns the specified node.
   *
   * @param playerColor the color to check
   * @param nodeId the node to check
   * @return true if the player owns the node
   */
  public boolean checkPlayerOwnsNode(PlayerColor playerColor, Integer nodeId) {
    return nodeOwners[nodeId] == playerColor;
  }

  /**
   * Returns the building level at the specified node (0=empty, 1=settlement, 2=city).
   *A value of 0 indicates no building, 1 indicates a settlement, and 2 indicates a city.
   *
   * @param nodeId the node to query
   * @return the building level
   */
  public Integer getNodeBuildingLevel(Integer nodeId) {
    return nodeBuildingLevels[nodeId];
  }

  /**
   * Upgrades the settlement at the specified node to a city for the given player.
   *
   * @param player the player building the city
   * @param nodeId the ID of the node to upgrade, must be within [0, 53]
   * @throws IllegalArgumentException if the node ID is out of bounds
   * @throws IllegalStateException if the node is not owned by the player or
   *     does not have a settlement to upgrade
   */
  public void buildCity(Player player, int nodeId) {
    if (nodeId < MIN_NODE_ID || nodeId > MAX_NODE_ID) {
      throw new IllegalArgumentException("Invalid NodeID - must be within [0, 53].");
    }

    PlayerColor claimingColor = player.getColor();
    if (!checkPlayerOwnsNode(claimingColor, nodeId) && getNodeBuildingLevel(nodeId) != 0) {
      throw new IllegalStateException("Node owned by other player, cannot build here.");
    }

    if (getNodeBuildingLevel(nodeId) != SETTLEMENT_LEVEL) {
      throw new IllegalStateException("Must upgrade a settlement to a city.");
    }

    List<Integer> hexIds = nodeIdToHexes.get(nodeId);
    for (int hexId : hexIds) {
      hexes.get(hexId).removePlayerSettlementFromHex(player);
      hexes.get(hexId).addPlayerCityToHex(player);
    }
    nodeBuildingLevels[nodeId] = CITY_LEVEL;
  }

  /**
   * Places a road for the given player along the edge between two nodes.
   *
   * @param player the player building the road
   * @param nodeId1 the ID of the first node, must be within [0, 53]
   * @param nodeId2 the ID of the second node, must be within [0, 53]
   * @throws IllegalArgumentException if either node ID is out of bounds
   * @throws IllegalStateException if the player has no roads remaining
   */
  public void addRoad(Player player, int nodeId1, int nodeId2) {
    if (nodeId1 < MIN_NODE_ID || nodeId2 < MIN_NODE_ID
        || nodeId1 > MAX_NODE_ID || nodeId2 > MAX_NODE_ID) {
      throw new IllegalArgumentException("Edge nodeId out of bounds. Must be within [0, 53].");
    }
    PlayerColor claimingColor = player.getColor();
    boardGraphController.playerClaimStoredEdge(claimingColor, nodeId1, nodeId2);
    player.placeRoad();
  }

  /**
   * Awards resources to all players with settlements or cities adjacent to hexes
   * matching the given roll number, excluding any hex currently occupied by the robber.
   *
   * @param rollNum the dice roll number to match against hex roll numbers
   */
  public void awardResources(int rollNum) {
    int robberLocation = robber.getRobberLocation();

    for (Hex hex : hexes) {
      int curHexId = hex.getHexId();
      if (hex.getHexRollNum() == rollNum && robberLocation != curHexId) {
        hexes.get(curHexId).awardSettlementResources();
        hexes.get(curHexId).awardCityResources();
      }
    }
  }

  /**
   * Moves the robber to the specified hex.
   *
   * @param hexId the ID of the hex to move the robber to, must be within [0, 18]
   * @throws IllegalArgumentException if the hex ID is out of bounds or the robber
   *     is already at the specified hex
   */
  public void moveRobber(int hexId) {
    if (hexId < MIN_HEX_ID || hexId > MAX_HEX_ID) {
      throw new IllegalArgumentException("Cannot move Robber to invalid Hex ID");
    }

    int previousRobberLocation = robber.getRobberLocation();
    if (previousRobberLocation == hexId) {
      throw new IllegalArgumentException("Must move robber to new location");
    }

    robber.moveRobber(hexId);
  }

  /**
   * Returns the set of players with settlements or cities on the specified hex.
   *
   * @param hexId the ID of the hex to query, must be within [0, 18]
   * @return a set of players on the hex; empty if no players are present
   * @throws IllegalArgumentException if the hex ID is out of bounds
   */
  public Set<Player> getPlayersOnHex(int hexId) {
    if (hexId < MIN_HEX_ID || hexId > MAX_HEX_ID) {
      throw new IllegalArgumentException("Invalid Hex ID, must be within [0,18]");
    }
    Hex curHex = hexes.get(hexId);

    Set<Player> playersOnHex = new HashSet<>();
    playersOnHex.addAll(curHex.getHexSettlementPlayers());
    playersOnHex.addAll(curHex.getHexCityPlayers());

    return playersOnHex;
  }

  /**
   * Places a settlement during the setup phase for the given player at the specified node.
   * Unlike {@link #buildSettlement}, this bypasses adjacency restrictions enforced
   * during normal play.
   *
   * @param player the player building the settlement
   * @param nodeId the ID of the node to build on, must be within [0, 53]
   * @throws IllegalArgumentException if the node ID is out of bounds
   * @throws IllegalStateException if the player has no settlements remaining
   */
  public void buildSetupSettlement(Player player, int nodeId) {
    if (nodeId < MIN_NODE_ID || nodeId > MAX_NODE_ID) {
      throw new IllegalArgumentException("Invalid NodeID - must be within [0, 53].");
    }

    PlayerColor claimingColor = player.getColor();
    boardGraphController.playerClaimStoredNodeSetupPhase(claimingColor, nodeId);

    List<Integer> hexIds = nodeIdToHexes.get(nodeId);
    for (int hexId : hexIds) {
      hexes.get(hexId).addPlayerSettlementToHex(player);
    }
    nodeOwners[nodeId] = claimingColor;
    nodeBuildingLevels[nodeId] = SETTLEMENT_LEVEL;
    player.placeSettlement();
  }

  /**
   * Places a road during the setup phase for the given player, anchored to a
   * previously claimed setup settlement node.
   *
   * @param player the player building the road
   * @param claimedNodeId the node ID of the player's most recently placed setup settlement
   * @param nodeId1 the ID of the first node of the edge, must be within [0, 53]
   * @param nodeId2 the ID of the second node of the edge, must be within [0, 53]
   * @throws IllegalArgumentException if either edge node ID is out of bounds
   * @throws IllegalStateException if the player has no roads remaining
   */
  public void buildSetupRoad(Player player, int claimedNodeId, int nodeId1, int nodeId2) {
    if (nodeId1 < MIN_NODE_ID || nodeId2 < MIN_NODE_ID
        || nodeId1 > MAX_NODE_ID || nodeId2 > MAX_NODE_ID) {
      throw new IllegalArgumentException("Edge nodeId out of bounds. Must be within [0, 53].");
    }
    PlayerColor claimingColor = player.getColor();
    boardGraphController.playerClaimStoredEdgeSetupPhase(
        claimingColor, claimedNodeId, nodeId1, nodeId2);
    player.placeRoad();
  }

  /**
   * Calculates and returns the color of the player with the longest road.
   * Returns {@link PlayerColor#SETUP} if no player has achieved the longest road yet.
   *
   * @param players the list of all players in the game
   * @param previousWinner the color of the previous longest road holder,
   *     or {@link PlayerColor#SETUP} if none
   * @return the color of the player with the longest road, or
   *     {@link PlayerColor#SETUP} if no player qualifies
   */
  // Note: Returns SETUP PlayerColor if nobody has achieved longest road yet
  public PlayerColor calculateLongestRoad(List<Player> players, PlayerColor previousWinner) {
    return boardGraphController.calculateLongestRoad(players, previousWinner);
  }

  private List<Hex> initHexes() {
    List<Hex> hexList = new ArrayList<>(List.of(
        new Hex(0, Resource.ORE, 10),
        new Hex(1, Resource.WOOL, 2),
        new Hex(2, Resource.LUMBER, 9),
        new Hex(3, Resource.GRAIN, 12),
        new Hex(4, Resource.BRICK, 6),
        new Hex(5, Resource.WOOL, 4),
        new Hex(6, Resource.BRICK, 10),
        new Hex(7, Resource.GRAIN, 9),
        new Hex(8, Resource.LUMBER, 11),
        new Hex(9, Resource.DESERT, 7),
        new Hex(10, Resource.LUMBER, 3),
        new Hex(11, Resource.ORE, 8),
        new Hex(12, Resource.LUMBER, 8),
        new Hex(13, Resource.ORE, 3),
        new Hex(14, Resource.GRAIN, 4),
        new Hex(15, Resource.WOOL, 5),
        new Hex(16, Resource.BRICK, 5),
        new Hex(17, Resource.GRAIN, 6),
        new Hex(18, Resource.LUMBER, 11)
    ));
    return hexList;
  }

  /**
   * Returns the list of hex resources in board order, as strings of resource names.
   *
   * @return a list of resource name strings corresponding to each hex
   */
  public List<String> getHexOrder() {
    List<String> order = new ArrayList<>();
    for (Hex hex : hexes) {
      order.add(hex.getHexResource().name());
    }
    return order;
  }

  /**
   * Returns the total number of hexes on the board.
   *
   * @return the hex count
   */
  public int getHexCount() {
    return this.hexes.size();
  }

  static Map<Integer, List<Integer>> initNodeHexMap() {
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

  List<Port> initPorts() {
    return List.of(
            new Port(3, Resource.ANY, List.of(0, 3)),
            new Port(2, Resource.GRAIN, List.of(1, 5)),
            new Port(2, Resource.ORE, List.of(10, 15)),
            new Port(2, Resource.LUMBER, List.of(11, 16)),
            new Port(3, Resource.ANY, List.of(26, 32)),
            new Port(2, Resource.BRICK, List.of(33, 38)),
            new Port(2, Resource.WOOL, List.of(42, 46)),
            new Port(3, Resource.ANY, List.of(47, 51)),
            new Port(3, Resource.ANY, List.of(49, 52))
    );
  }

  /**
   * Returns a list of ports that the given player can use
   * based on their settlements and cities on the board.
   *
   * @param player the player to check port access for
   * @return a list of ports the player has access to, or an empty list if none
   */
  public List<Port> getAvailablePorts(Player player) {
    List<Port> availablePorts = new ArrayList<>();
    for (Port port : ports) {
      if (port.playerCanUsePort(this, player)) {
        availablePorts.add(port);
      }
    }
    return availablePorts;
  }

  /**
   * Checks whether the edge between two nodes is already occupied by a road.
   *
   * @param nodeId1 the ID of the first node
   * @param nodeId2 the ID of the second node
   * @return {@code true} if the edge is occupied, {@code false} otherwise
   */
  public boolean checkEdgeOccupied(int nodeId1, int nodeId2) {
    return boardGraphController.checkEdgeOccupied(nodeId1, nodeId2);
  }
}