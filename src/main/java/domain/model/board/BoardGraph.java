package domain.model.board;

import domain.model.player.Player;
import domain.model.player.PlayerColor;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.List;

public class BoardGraph {
  private final Map<Integer, Set<GraphEdge>> nodeIDToConnectingEdges = new HashMap<>();
  private final Map<Integer, GraphNode> nodeIDToNodeObject = new HashMap<>();

  boolean addGraphNodeObject(GraphNode graphNode) {
    int nodeID = graphNode.getNodeID();
    if (nodeIDToNodeObject.containsKey(nodeID)) {
      throw new IllegalArgumentException("Node already exists");
    } else {
      this.nodeIDToNodeObject.put(nodeID, graphNode);
      this.nodeIDToConnectingEdges.put(nodeID, new HashSet<>());
      return true;
    }
  }

  boolean addGraphNodeConnection(int nodeID, GraphEdge connectingEdge) {
    getGraphNodeByID(nodeID);
    Set<GraphEdge> setOfConnectingEdges = this.nodeIDToConnectingEdges.get(nodeID);
    if (setOfConnectingEdges.contains(connectingEdge)) {
      throw new IllegalArgumentException("Node already has specified edge");
    } else {
      setOfConnectingEdges.add(connectingEdge);
      return true;
    }
  }

  GraphNode getGraphNodeByID(int nodeID) {
    if (!nodeIDToNodeObject.containsKey(nodeID)) {
      throw new IllegalArgumentException("Node does not exist");
    } else {
      return this.nodeIDToNodeObject.get(nodeID);
    }
  }

  Set<GraphEdge> getConnectingEdgesByID(int nodeID) {
    Set<GraphEdge> result = new HashSet<>();
    if (!nodeIDToConnectingEdges.containsKey(nodeID)) {
      throw new IllegalArgumentException("Node does not exist");
    } else {
      Set<GraphEdge> connectingEdges = nodeIDToConnectingEdges.get(nodeID);
      for (GraphEdge edge : connectingEdges) {
        result.add(edge);
      }
      return result;
    }
  }

  public boolean checkNodeOccupied(int nodeID) {
    GraphNode nodeOfInterest = getGraphNodeByID(nodeID);
    return nodeOfInterest.checkOccupied();
  }

  public boolean checkEdgeOccupied(int startingNodeID, int endingNodeID) {
    Set<GraphEdge> setWithRelevantEdge = getConnectingEdgesByID(startingNodeID);
    GraphEdge edgeToCheck = getMatchingEdgeFromSet(setWithRelevantEdge, startingNodeID, endingNodeID);
    return edgeToCheck.checkRoadExists();
  }

  boolean checkPlayerOwnsGraphNodeObject(PlayerColor color, int nodeID) {
    GraphNode nodeOfInterest = getGraphNodeByID(nodeID);
    PlayerColor nodeColor = nodeOfInterest.checkColor();
    return nodeColor == color;
  }

  boolean claimGraphNodeObject(PlayerColor color, int nodeID) {
    return getGraphNodeByID(nodeID).playerClaimNode(color);
  }

  boolean claimGraphEdgeObject(PlayerColor color, int startingNodeID, int endingNodeID) {
    Set<GraphEdge> setWithRelevantEdge = getConnectingEdgesByID(startingNodeID);
    GraphEdge edgeToClaim = getMatchingEdgeFromSet(setWithRelevantEdge, startingNodeID, endingNodeID);
    edgeToClaim.claimGraphEdge(color);
    return true;
  }

  boolean checkIfAdjacentNodesNotClaimed(int nodeID) {
    Set<GraphEdge> connectingEdges = getConnectingEdgesByID(nodeID);
    for (GraphEdge edge : connectingEdges) {
      int edgeStartingNodeID = edge.getStartingNodeID();
      int edgeEndingNodeID = edge.getEndingNodeID();
      GraphNode nodeToCheck;
      // One of these IDs will be the current node trying to be claimed, so we don't need to check it
      if (edgeStartingNodeID != nodeID) {
        nodeToCheck = getGraphNodeByID(edgeStartingNodeID);
      } else {
        nodeToCheck = getGraphNodeByID(edgeEndingNodeID);
      }

      if (nodeToCheck.checkOccupied()) {
        return false;
      }
    }
    return true;
  }

  GraphEdge getMatchingEdgeFromSet(Set<GraphEdge> connectingEdges, int startingNodeID, int endingNodeID) {
    for (GraphEdge edge : connectingEdges) {
      if (edge.getStartingNodeID() == startingNodeID && edge.getEndingNodeID() == endingNodeID) {
        return edge;
      }
    }
    throw new IllegalArgumentException("Edge does not exist");
  }

  protected boolean edgeCheckPlayerOwnsNeighboringEdge(PlayerColor color, int startingNodeID, int endingNodeID) {
    Set<GraphEdge> connectingStartingNodeEdges = getConnectingEdgesByID(startingNodeID);
    Set<GraphEdge> connectingEndingNodeEdges = getConnectingEdgesByID(endingNodeID);

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

  protected boolean edgeCheckPlayerOwnsNeighboringNode(PlayerColor color, int startingNodeID, int endingNodeID) {
    GraphNode startingNode = getGraphNodeByID(startingNodeID);
    GraphNode endingNode = getGraphNodeByID(endingNodeID);
    return startingNode.checkColor() == color || endingNode.checkColor() == color;
  }

  public boolean nodeCheckPlayerOwnsNeighboringEdge(PlayerColor color, int nodeID) {
    Set<GraphEdge> relevantEdgeSet = getConnectingEdgesByID(nodeID);
    for (GraphEdge edge : relevantEdgeSet) {
      if (edge.checkOwningColor() == color) {
        return true;
      }
    }
    return false;
  }

  PlayerColor calculateLongestRoad(List<Player> activePlayers, PlayerColor previousWinner) {
    int longestRoad = 0;
    PlayerColor longestRoadOwner = previousWinner;

    if (previousWinner != PlayerColor.SETUP) {
      for (Set<GraphEdge> edges : nodeIDToConnectingEdges.values()) {
        for (GraphEdge edge : edges) {
          if (edge.checkOwningColor() == previousWinner) {
            longestRoad++;
          }
        }
      }
      longestRoad = longestRoad / 2;
    }

    for (Player player : activePlayers) {
      PlayerColor color = player.getPlayerColor();
      if (color == previousWinner) continue;

      int playerLongest = calculatePlayerLongestRoad(color);

      if (playerLongest > longestRoad && playerLongest >= 5) {
        longestRoad = playerLongest;
        longestRoadOwner = color;
      }
    }
    return longestRoadOwner;
  }

  private int calculatePlayerLongestRoad(PlayerColor color) {
    Set<GraphEdge> playerEdges = new HashSet<>();
    for (Set<GraphEdge> edges : nodeIDToConnectingEdges.values()) {
      for (GraphEdge edge : edges) {
        if (edge.checkOwningColor() == color) {
          playerEdges.add(edge);
        }
      }
    }

    int longest = 0;
    for (GraphEdge startEdge : playerEdges) {
      int length = dfs(startEdge, new HashSet<>(), color);
      longest = Math.max(longest, length);
    }
    return longest;
  }

  private int dfs(GraphEdge current, Set<GraphEdge> visited, PlayerColor color) {
    visited.add(current);
    int longest = visited.size();

    int[] nodes = {current.getStartingNodeID(), current.getEndingNodeID()};
    for (int nodeId : nodes) {
      Set<GraphEdge> connecting = getConnectingEdgesByID(nodeId);
      for (GraphEdge neighbor : connecting) {
        if (!visited.contains(neighbor) && neighbor.checkOwningColor() == color) {
          int length = dfs(neighbor, new HashSet<>(visited), color);
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

  private void addGraphEdge(int startingNodeID, int endingNodeID) {
    GraphEdge newEdge = new GraphEdge(startingNodeID, endingNodeID);
    addGraphNodeConnection(startingNodeID, newEdge);
    addGraphNodeConnection(endingNodeID, newEdge);
  }

  protected int checkAmountOfNodesForTesting() {
    return this.nodeIDToNodeObject.size();
  }

  protected int checkAmountOfNodesInEdgeMapForTesting() {
    return this.nodeIDToConnectingEdges.size();
  }
}
