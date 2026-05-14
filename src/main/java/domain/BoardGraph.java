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

    boolean claimGraphNodeObject(PlayerColor color, int nodeID){
        return getGraphNodeByID(nodeID).playerClaimNode(color);
    };

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



    boolean checkIfAdjacentNodesNotClaimed(int nodeID) {
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
                return false;
            }
        }
        // we have checked all the neighboring nodes, none of them are occupied
        return true;
    }

    GraphEdge getCorrectEdgeFromSet(Set<GraphEdge> connectingEdges, int startingNodeID, int endingNodeID){
        for (GraphEdge edge : connectingEdges) {
            if (edge.getStartingNodeID() == startingNodeID && edge.getEndingNodeID() == endingNodeID) {
                return edge;
            }
        }
        throw new IllegalArgumentException("Edge does not exist");
    }

    protected boolean checkPlayerOwnsNeighboringEdge(PlayerColor color, int startingNodeID, int endingNodeID) {
        Set<GraphEdge> connectingStartingNodeEdges = getConnectingEdgesByID(startingNodeID);
        Set<GraphEdge> connectingEndingNodeEdges = getConnectingEdgesByID(endingNodeID);
        // check starting Node Edges
        for (GraphEdge edge : connectingStartingNodeEdges) {
            if(edge.checkOwningColor() == color) {
                return true;
            }
        }
        // check ending Node Edges
        for (GraphEdge edge : connectingEndingNodeEdges) {
            if(edge.checkOwningColor() == color) {
                return true;
            }
        }
        return false;
    }

    protected boolean checkPlayerOwnsNeighboringNode(PlayerColor color, int startingNodeID, int endingNodeID) {
        GraphNode startingNode = getGraphNodeByID(startingNodeID);
        GraphNode endingNode = getGraphNodeByID(endingNodeID);
        return startingNode.checkColor() == color || endingNode.checkColor() == color;
    }

    void buildBoard() {
        // add all the graphNodes
        for (int i = 0; i < 54; i++) {
            GraphNode newNode = new GraphNode(i);
            addGraphNodeObject(newNode);
        }

        addGraphEdge(0, 1);
        addGraphEdge(0, 3);
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
        addGraphEdge(24, 29);
        addGraphEdge(25, 30);
        addGraphEdge(25, 31);
        addGraphEdge(22, 28);
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

    void addGraphEdge(int startingNodeID, int endingNodeID) {
        GraphEdge newEdge = new GraphEdge(startingNodeID, endingNodeID);
        addGraphNodeConnection(startingNodeID, newEdge);
        addGraphNodeConnection(endingNodeID, newEdge);
    }

    protected int checkAmountOfNodesForTesting() {
        return this.nodeIDToNodeObject.size();
    }

    protected int checkAmountOfNodesInEdgeMapForTesting() {
        return this.nodeIDToConectingEdges.size();
    }


}
