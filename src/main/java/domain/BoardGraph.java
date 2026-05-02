package domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BoardGraph {
    // map that connects Nodes to the edges connected

    // map that connects nodes edges; edges themselves contain info on
    private Map<Integer, Set<GraphEdge>> NodeID_to_Connecting_Edges = new HashMap<>();

    // map that connects NodeID to the actual node object
    private Map<Integer, GraphNode> NodeID_to_NodeObject = new HashMap<>();

    // Add a new GraphNode to the Map
    boolean addGraphNodeObject(GraphNode graphNode) {
        int nodeID = graphNode.getNodeID();
        if (NodeID_to_NodeObject.containsKey(nodeID)) {
            throw new IllegalArgumentException("Node already exists");
        }
        else {
            this.NodeID_to_NodeObject.put(nodeID, graphNode);
            return true;
        }
    }

    //
    boolean addGraphNodeConnection(int currentNodeID, GraphEdge connectingEdge){
        return false;
    }

    GraphNode getGraphNodeByID(int nodeID) {
        if (!NodeID_to_NodeObject.containsKey(nodeID)) {
            throw new IllegalArgumentException("Node does not exist");
        }
        else {
            return this.NodeID_to_NodeObject.get(nodeID);
        }
    }


}
