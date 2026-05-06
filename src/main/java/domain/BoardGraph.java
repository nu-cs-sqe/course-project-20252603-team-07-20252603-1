package domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BoardGraph {
    // map that connects Nodes to the edges connected

    // map that connects nodes edges; edges themselves contain info on
    private Map<Integer, Set<GraphEdge>> nodeIDToConectingEdges = new HashMap<>();

    // map that connects NodeID to the actual node object
    private Map<Integer, GraphNode> nodeIDToNodeObject = new HashMap<>();

    // Add a new GraphNode to the Map
    boolean addGraphNodeObject(GraphNode graphNode) {
        int nodeID = graphNode.getNodeID();
        if (nodeIDToNodeObject.containsKey(nodeID)) {
            throw new IllegalArgumentException("Node already exists");
        }
        else {
            this.nodeIDToNodeObject.put(nodeID, graphNode);
            this.nodeIDToConectingEdges.put(nodeID, new HashSet<>());
            return true;
        }
    }

    // Add a connecting edge to the set of edges within the map <GraphID, set of Edges>
    boolean addGraphNodeConnection(int nodeID, GraphEdge connectingEdge){
        getGraphNodeByID(nodeID); // This call works as a check that the node Exists, throwing the proper error if it does not
        if (this.nodeIDToConectingEdges.get(nodeID).contains(connectingEdge)) {
            throw new IllegalArgumentException("Node already has specified edge");
        }
        else {
            this.nodeIDToConectingEdges.get(nodeID).add(connectingEdge);
            return true;
        }
    }

    // Getter function to get the graphNode Object from the map
    GraphNode getGraphNodeByID(int nodeID) {
        if (!nodeIDToNodeObject.containsKey(nodeID)) {
            throw new IllegalArgumentException("Node does not exist");
        }
        else {
            return this.nodeIDToNodeObject.get(nodeID);
        }
    }

    // Getter function to get the set of edges from the map
    Set<GraphEdge> getConnectingEdgesByID(int nodeID) {
        if (!nodeIDToConectingEdges.containsKey(nodeID)) {
            throw new IllegalArgumentException("Node does not exist");
        }
        else {
            return this.nodeIDToConectingEdges.get(nodeID);
        }
    }

    boolean playerClaimStoredNode(PlayerColor color, int nodeID){
        GraphNode nodeToClaim = getGraphNodeByID(nodeID);
        checkAdjacentClaimedNodes(nodeID);
        nodeToClaim.playerClaimNode(color);
        return true;
    }

    boolean checkAdjacentClaimedNodes(int nodeID) {
        Set<GraphEdge> connectingEdges = getConnectingEdgesByID(nodeID);
        for (GraphEdge edge: connectingEdges) {
            int edgeStartingNodeID = edge.getStartingNodeID();
            int edgeEndingNodeID = edge.getEndingNodeID();
            GraphNode nodeToCheck;
            // One of these IDs will be the current node trying to be claimed, so we don't need to check it
            if (edgeStartingNodeID != nodeID) {
                nodeToCheck = getGraphNodeByID(edgeStartingNodeID);
            }
            else {
                nodeToCheck = getGraphNodeByID(edgeEndingNodeID);
            }

            if (nodeToCheck.checkOccupied()) {
                throw new IllegalArgumentException("Can not claim node adjacent to node already claimed");
            }
        }
        // we have checked all the neighboring nodes, none of them are occupied
        return true;
    }

    boolean playerClaimStoredEdge(PlayerColor color, int startingNodeID, int endingNodeID){
        boolean playerOwnsNeighboringNode = checkPlayerOwnsNeighboringNode(color, startingNodeID, endingNodeID);
        boolean playerOwnsNeighboringEdge = checkPlayerOwnsNeighboringEdge(color, startingNodeID, endingNodeID);
        if(playerOwnsNeighboringNode || playerOwnsNeighboringEdge) {
            Set<GraphEdge> connectingEdges = getConnectingEdgesByID(startingNodeID);
            // Our Edge of interest is guaranteed to be in this set IF it exists
            GraphEdge edgeToClaim = getCorrectEdgeFromSet(connectingEdges, startingNodeID, endingNodeID);
            edgeToClaim.claimGraphEdge(color);
            return true;
        }
        return false;
    }

    GraphEdge getCorrectEdgeFromSet(Set<GraphEdge> connectingEdges, int startingNodeID, int endingNodeID){
        for (GraphEdge edge : connectingEdges) {
            if (edge.getStartingNodeID() == startingNodeID && edge.getEndingNodeID() == endingNodeID) {
                return edge;
            }
        }
        throw new IllegalArgumentException("Edge does not exist");
    }

    boolean checkPlayerOwnsNeighboringEdge(PlayerColor color, int startingNodeID, int endingNodeID) {
        return false;
    }

    boolean checkPlayerOwnsNeighboringNode(PlayerColor color, int startingNodeID, int endingNodeID) {
        return true;
    }



}
