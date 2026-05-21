package domain;

public class GraphNode {
    // Graph node represents vertexes of hexagons on board
    // Each node has a unique ID
    // Keep track of:
    // NodeID -> unique ID to identify Node in Graph -> int
    // Occupied -> is this Node occupied?
    final private int nodeID;
    private boolean occupied;
    private PlayerColor owningPlayerColor;

    private static final int MAX_NODE_ID = 53;
    private static final int MIN_NODE_ID = 0;

    GraphNode(int nodeID) {
        assertValidNodeID();
        this.nodeID = nodeID;
        this.occupied = false;
        this.owningPlayerColor = PlayerColor.SETUP;
    }

    private boolean assertValidNodeID() {
        return true;
    }

    boolean playerClaimNode(PlayerColor color){
        if (checkOccupied()) {
            throw new IllegalArgumentException("Node Already Claimed");
        }
        else {
            this.occupied = true;
            this.owningPlayerColor = color;
            return true;
        }
    }

    boolean checkOccupied(){
        return this.occupied;
    }

    PlayerColor checkColor(){
        return this.owningPlayerColor;
    }

    int getNodeID(){
        return this.nodeID;
    }
}
