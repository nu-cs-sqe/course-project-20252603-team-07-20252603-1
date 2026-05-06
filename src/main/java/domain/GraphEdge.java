package domain;

// TODO - ensure startingNodeID < endingNodeID; need to do additional BVA
public class GraphEdge {
    // unique Edge_id
    private int startingNodeID;
    private int endingNodeID;
    // keep track of the
    private boolean roadBuilt;
    private PlayerColor owningPlayerColor;

    GraphEdge(int startingNodeID, int endingNodeID) {
        this.startingNodeID = startingNodeID;
        this.endingNodeID = endingNodeID;
        this.roadBuilt = false;
        this.owningPlayerColor = PlayerColor.SETUP;
    }

    // need to be able to claim an edge
    boolean claimGraphEdge(PlayerColor color) {
        if (this.roadBuilt) {
            // Edge already occupied
            throw new IllegalArgumentException("Edge already claimed");
        }
        else {
            this.roadBuilt = true;
            this.owningPlayerColor = color;
            return true;
        }
    }

    boolean checkRoadExists() {
        return this.roadBuilt;
    }
    PlayerColor checkOwningColor() {
        return this.owningPlayerColor;
    }

    int getStartingNodeID(){
        return this.startingNodeID;
    }
    int getEndingNodeID() {
        return this.endingNodeID;
    }

}
