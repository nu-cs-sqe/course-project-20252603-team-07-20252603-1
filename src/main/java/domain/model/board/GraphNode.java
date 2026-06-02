package domain.model.board;

import domain.model.exceptions.IllegalNodeIDException;
import domain.model.player.PlayerColor;

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

    public GraphNode(int nodeID) {
        assertValidNodeID(nodeID);
        this.nodeID = nodeID;
        this.occupied = false;
        this.owningPlayerColor = PlayerColor.SETUP;
    }

    private boolean assertValidNodeID(int nodeID) {
        if (nodeID < MIN_NODE_ID || nodeID > MAX_NODE_ID) {
            throw new IllegalNodeIDException("Requested nodeID number illegal");
        }
        else {
            return true;
        }
    }

    public boolean playerClaimNode(PlayerColor color){
        if (checkOccupied()) {
            throw new IllegalArgumentException("Node Already Claimed");
        }
        else {
            this.occupied = true;
            this.owningPlayerColor = color;
            return true;
        }
    }

    public boolean checkOccupied(){
        return this.occupied;
    }

    public PlayerColor checkColor(){
        return this.owningPlayerColor;
    }

    public int getNodeID(){
        return this.nodeID;
    }

    @Override
    protected final void finalize() {
        // intentionally empty — blocks finalizer attacks
    }
}
