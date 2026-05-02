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


}
