package domain.model.board;

import domain.model.player.Player;
import domain.model.player.PlayerColor;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Adjacency-list graph of board nodes and edges used to track settlements and roads.
 */
public class BoardGraph {
  private final Map<Integer, Set<GraphEdge>> nodeIdToConnectingEdges = new HashMap<>();
  private final Map<Integer, GraphNode> nodeIdToNodeObject = new HashMap<>();

  boolean addGraphNodeObject(GraphNode graphNode) {
    int nodeId = graphNode.getNodeId();
    if (nodeIdToNodeObject.containsKey(nodeId)) {
      throw new IllegalArgumentException("Node already exists");
    } else {
      this.nodeIdToNodeObject.put(nodeId, graphNode);
      this.nodeIdToConnectingEdges.put(nodeId, new HashSet<>());
      return true;
    }
  }

  boolean addGraphNodeConnection(int nodeId, GraphEdge connectingEdge) {
    getGraphNodeById(nodeId);
    Set<GraphEdge> setOfConnectingEdges = this.nodeIdToConnectingEdges.get(nodeId);
    if (setOfConnectingEdges.contains(connectingEdge)) {
      throw new IllegalArgumentException("Node already has specified edge");
    } else {
      setOfConnectingEdges.add(connectingEdge);
      return true;
    }
  }

  GraphNode getGraphNodeById(int nodeId) {
    if (!nodeIdToNodeObject.containsKey(nodeId)) {
      throw new IllegalArgumentException("Node does not exist");
    } else {
      return this.nodeIdToNodeObject.get(nodeId);
    }
  }

  Set<GraphEdge> getConnectingEdgesById(int nodeId) {
    Set<GraphEdge> result = new HashSet<>();
    if (!nodeIdToConnectingEdges.containsKey(nodeId)) {
      throw new IllegalArgumentException("Node does not exist");
    } else {
      Set<GraphEdge> connectingEdges = nodeIdToConnectingEdges.get(nodeId);
      for (GraphEdge edge : connectingEdges) {
        result.add(edge);
      }
      return result;
    }
  }

  /**
   * Returns whether the given node is occupied by a settlement or city.
   */
  public boolean checkNodeOccupied(int nodeId) {
    GraphNode nodeOfInterest = getGraphNodeById(nodeId);
    return nodeOfInterest.checkOccupied();
  }

  /**
   * Returns whether the edge between the two nodes has a road on it.
   */
  public boolean checkEdgeOccupied(int startingNodeId, int endingNodeId) {
    Set<GraphEdge> setWithRelevantEdge = getConnectingEdgesById(startingNodeId);
    GraphEdge edgeToCheck =
        getMatchingEdgeFromSet(setWithRelevantEdge, startingNodeId, endingNodeId);
    return edgeToCheck.checkRoadExists();
  }

  boolean checkPlayerOwnsGraphNodeObject(PlayerColor color, int nodeId) {
    GraphNode nodeOfInterest = getGraphNodeById(nodeId);
    PlayerColor nodeColor = nodeOfInterest.checkColor();
    return nodeColor == color;
  }

  void claimGraphNodeObject(PlayerColor color, int nodeId) {
    getGraphNodeById(nodeId).playerClaimNode(color);
  }

  boolean claimGraphEdgeObject(PlayerColor color, int startingNodeId, int endingNodeId) {
    Set<GraphEdge> setWithRelevantEdge = getConnectingEdgesById(startingNodeId);
    GraphEdge edgeToClaim =
        getMatchingEdgeFromSet(setWithRelevantEdge, startingNodeId, endingNodeId);
    edgeToClaim.claimGraphEdge(color);
    return true;
  }

  boolean checkIfAdjacentNodesNotClaimed(int nodeId) {
    Set<GraphEdge> connectingEdges = getConnectingEdgesById(nodeId);
    for (GraphEdge edge : connectingEdges) {
      int edgeStartingNodeId = edge.getStartingNodeId();
      int edgeEndingNodeId = edge.getEndingNodeId();
      GraphNode nodeToCheck;
      if (edgeStartingNodeId != nodeId) {
        nodeToCheck = getGraphNodeById(edgeStartingNodeId);
      } else {
        nodeToCheck = getGraphNodeById(edgeEndingNodeId);
      }

      if (nodeToCheck.checkOccupied()) {
        return false;
      }
    }
    return true;
  }

  PlayerColor getEdgeOwner(int startingNodeId, int endingNodeId) {
    Set<GraphEdge> setWithRelevantEdge = getConnectingEdgesById(startingNodeId);
    GraphEdge edgeToCheck =
        getMatchingEdgeFromSet(setWithRelevantEdge, startingNodeId, endingNodeId);
    return edgeToCheck.checkOwningColor();
  }

  GraphEdge getMatchingEdgeFromSet(
      Set<GraphEdge> connectingEdges, int startingNodeId, int endingNodeId) {
    for (GraphEdge edge : connectingEdges) {
      if (edge.getStartingNodeId() == startingNodeId
          && edge.getEndingNodeId() == endingNodeId) {
        return edge;
      }
    }
    throw new IllegalArgumentException("Edge does not exist");
  }

  /**
   * Returns whether the player owns an edge neighboring the given edge endpoints.
   */
  protected boolean edgeCheckPlayerOwnsNeighboringEdge(
      PlayerColor color, int startingNodeId, int endingNodeId) {
    Set<GraphEdge> connectingStartingNodeEdges = getConnectingEdgesById(startingNodeId);
    Set<GraphEdge> connectingEndingNodeEdges = getConnectingEdgesById(endingNodeId);

    for (GraphEdge edge : connectingStartingNodeEdges) {
      if (edge.checkOwningColor() == color) {
        return true;
      }
    }

    for (GraphEdge edge : connectingEndingNodeEdges) {
      if (edge.checkOwningColor() == color) {
        return true;
      }
    }
    return false;
  }

  /**
   * Returns whether the player owns either endpoint node of the given edge.
   */
  protected boolean edgeCheckPlayerOwnsNeighboringNode(
      PlayerColor color, int startingNodeId, int endingNodeId) {
    GraphNode startingNode = getGraphNodeById(startingNodeId);
    GraphNode endingNode = getGraphNodeById(endingNodeId);
    return startingNode.checkColor() == color || endingNode.checkColor() == color;
  }

  /**
   * Returns whether the player owns any edge adjacent to the given node.
   */
  public boolean nodeCheckPlayerOwnsNeighboringEdge(PlayerColor color, int nodeId) {
    Set<GraphEdge> relevantEdgeSet = getConnectingEdgesById(nodeId);
    for (GraphEdge edge : relevantEdgeSet) {
      if (edge.checkOwningColor() == color) {
        return true;
      }
    }
    return false;
  }

  PlayerColor calculateLongestRoad(List<Player> activePlayers, PlayerColor previousWinner) {
    int longestRoad = 4;
    PlayerColor longestRoadOwner = previousWinner;

    if (previousWinner != PlayerColor.SETUP) {
      longestRoad = calculatePlayerLongestRoad(previousWinner);
      if (longestRoad < 5) {
        longestRoad = 4;
        longestRoadOwner = PlayerColor.SETUP;
      }
    }

    for (Player player : activePlayers) {
      PlayerColor color = player.getColor();
      if (color == previousWinner) {
        continue;
      }

      int playerLongest = calculatePlayerLongestRoad(color);

      if (playerLongest > longestRoad) {
        longestRoad = playerLongest;
        longestRoadOwner = color;
      }
    }
    return longestRoadOwner;
  }

  private int calculatePlayerLongestRoad(PlayerColor color) {
    Set<GraphEdge> playerEdges = new HashSet<>();
    for (Set<GraphEdge> edges : nodeIdToConnectingEdges.values()) {
      for (GraphEdge edge : edges) {
        if (edge.checkOwningColor() == color) {
          playerEdges.add(edge);
        }
      }
    }

    int longest = 0;
    for (GraphEdge startEdge : playerEdges) {
      int length = dfs(startEdge, -1, new HashSet<>(), color);
      longest = Math.max(longest, length);
    }
    return longest;
  }

  private int dfs(
      GraphEdge current, int fromNodeId, Set<GraphEdge> visited, PlayerColor color) {
    visited.add(current);
    int longest = visited.size();

    int[] nodes = {current.getStartingNodeId(), current.getEndingNodeId()};
    for (int nodeId : nodes) {
      if (nodeId == fromNodeId) {
        continue;
      }

      GraphNode node = getGraphNodeById(nodeId);
      if (node.checkOccupied() && node.checkColor() != color) {
        continue;
      }

      Set<GraphEdge> connecting = getConnectingEdgesById(nodeId);
      for (GraphEdge neighbor : connecting) {
        if (!visited.contains(neighbor) && neighbor.checkOwningColor() == color) {
          int length = dfs(neighbor, nodeId, new HashSet<>(visited), color);
          longest = Math.max(longest, length);
        }
      }
    }
    return longest;
  }

  void buildBoard() {
    for (int i = 0; i < 54; i++) {
      GraphNode newNode = new GraphNode(i);
      addGraphNodeObject(newNode);
    }

    addGraphEdge(0, 3);
    addGraphEdge(0, 4);
    addGraphEdge(1, 4);
    addGraphEdge(1, 5);
    addGraphEdge(2, 5);
    addGraphEdge(2, 6);
    addGraphEdge(3, 7);
    addGraphEdge(4, 8);
    addGraphEdge(5, 9);
    addGraphEdge(6, 10);
    addGraphEdge(7, 11);
    addGraphEdge(7, 12);
    addGraphEdge(8, 12);
    addGraphEdge(8, 13);
    addGraphEdge(9, 13);
    addGraphEdge(9, 14);
    addGraphEdge(10, 14);
    addGraphEdge(10, 15);
    addGraphEdge(11, 16);
    addGraphEdge(12, 17);
    addGraphEdge(13, 18);
    addGraphEdge(14, 19);
    addGraphEdge(15, 20);
    addGraphEdge(16, 21);
    addGraphEdge(16, 22);
    addGraphEdge(17, 22);
    addGraphEdge(17, 23);
    addGraphEdge(18, 23);
    addGraphEdge(18, 24);
    addGraphEdge(19, 24);
    addGraphEdge(19, 25);
    addGraphEdge(20, 25);
    addGraphEdge(20, 26);
    addGraphEdge(21, 27);
    addGraphEdge(22, 28);
    addGraphEdge(23, 29);
    addGraphEdge(24, 30);
    addGraphEdge(25, 31);
    addGraphEdge(26, 32);
    addGraphEdge(27, 33);
    addGraphEdge(28, 33);
    addGraphEdge(28, 34);
    addGraphEdge(29, 34);
    addGraphEdge(29, 35);
    addGraphEdge(30, 35);
    addGraphEdge(30, 36);
    addGraphEdge(31, 36);
    addGraphEdge(31, 37);
    addGraphEdge(32, 37);
    addGraphEdge(33, 38);
    addGraphEdge(34, 39);
    addGraphEdge(35, 40);
    addGraphEdge(36, 41);
    addGraphEdge(37, 42);
    addGraphEdge(38, 43);
    addGraphEdge(39, 43);
    addGraphEdge(39, 44);
    addGraphEdge(40, 44);
    addGraphEdge(40, 45);
    addGraphEdge(41, 45);
    addGraphEdge(41, 46);
    addGraphEdge(42, 46);
    addGraphEdge(43, 47);
    addGraphEdge(44, 48);
    addGraphEdge(45, 49);
    addGraphEdge(46, 50);
    addGraphEdge(47, 51);
    addGraphEdge(48, 51);
    addGraphEdge(48, 52);
    addGraphEdge(49, 52);
    addGraphEdge(49, 53);
    addGraphEdge(50, 53);
  }

  private void addGraphEdge(int startingNodeId, int endingNodeId) {
    GraphEdge newEdge = new GraphEdge(startingNodeId, endingNodeId);
    addGraphNodeConnection(startingNodeId, newEdge);
    addGraphNodeConnection(endingNodeId, newEdge);
  }

  /**
   * Returns the number of nodes in the graph (for testing).
   */
  protected int checkAmountOfNodesForTesting() {
    return this.nodeIdToNodeObject.size();
  }

  /**
   * Returns the number of entries in the edge map (for testing).
   */
  protected int checkAmountOfNodesInEdgeMapForTesting() {
    return this.nodeIdToConnectingEdges.size();
  }
}
