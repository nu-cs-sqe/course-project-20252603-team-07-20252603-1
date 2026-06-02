package domain.model.board;

import domain.model.exceptions.EdgeAlreadyClaimedException;
import domain.model.exceptions.IllegalNodeOrderingInEdgeException;
import domain.model.player.PlayerColor;

// TODO - ensure startingNodeID < endingNodeID; need to do additional BVA
public class GraphEdge {
    // unique Edge_id
    final private int startingNodeID;
    final private int endingNodeID;
    // keep track of the
    private boolean roadBuilt;
    private PlayerColor owningPlayerColor;

    public GraphEdge(int startingNodeID, int endingNodeID) {
        assertValidNodeIDsOrdering(startingNodeID, endingNodeID);
        this.startingNodeID = startingNodeID;
        this.endingNodeID = endingNodeID;
        this.roadBuilt = false;
        this.owningPlayerColor = PlayerColor.SETUP;
    }

    private boolean assertValidNodeIDsOrdering(int startingNodeID, int endingNodeID) {
        if (!(startingNodeID < endingNodeID)){
            throw new IllegalNodeOrderingInEdgeException("Starting nodeID must be lower than ending nodeID");
        }
        else {
            return true;
        }
    }

    // need to be able to claim an edge
    public boolean claimGraphEdge(PlayerColor color) {
        if (this.roadBuilt) {
            // Edge already occupied
            throw new EdgeAlreadyClaimedException("Edge already claimed");
        }
        else {
            this.roadBuilt = true;
            this.owningPlayerColor = color;
            return true;
        }
    }

    @Override
    protected final void finalize() {
        // intentionally empty — blocks finalizer attacks
    }


    public boolean checkRoadExists() {
        return this.roadBuilt;
    }
    public PlayerColor checkOwningColor() {
        return this.owningPlayerColor;
    }

    public int getStartingNodeID(){
        return this.startingNodeID;
    }
    public int getEndingNodeID() {
        return this.endingNodeID;
    }

}
