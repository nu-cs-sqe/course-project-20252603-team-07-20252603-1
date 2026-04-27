package domain;

public class GraphEdge {
    // unique Edge_id
    private int nodeID_1;
    private int nodeID_2;
    // keep track of the
    private boolean roadBuilt;
    private PlayerColor owningPlayerColor;

    GraphEdge(int nodeID_1, int nodeID_2) {
        this.nodeID_1 = nodeID_1;
        this.nodeID_2 = nodeID_2;
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
}
