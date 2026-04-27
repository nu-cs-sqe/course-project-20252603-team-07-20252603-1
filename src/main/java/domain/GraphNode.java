package domain;

public class GraphNode {
    // Graph node represents vertexes of hexagons on board
    // Each node has a unique ID
    // Keep track of:
    // NodeID -> unique ID to identify Node in Graph -> int
    // Occupied -> is this Node occupied?
    private int nodeID;
    private boolean occupied;
    private PlayerColor owningPlayerColor;

    GraphNode(int nodeID) {
        this.nodeID = nodeID;
        this.occupied = false;
        this.owningPlayerColor = PlayerColor.SETUP;
    }

    boolean playerClaimNode(PlayerColor color){
        if (this.occupied) {
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
