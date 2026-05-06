package domain;

public class GraphEdge {
    // unique Edge_id
    private int nodeID1;
    private int nodeID2;
    // keep track of the
    private boolean roadBuilt;
    private PlayerColor owningPlayerColor;

    GraphEdge(int nodeID1, int nodeID2) {
        this.nodeID1 = nodeID1;
        this.nodeID2 = nodeID2;
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

    int getNodeID1(){
        return this.nodeID1;
    }
    int getNodeID2() {
        return this.nodeID2;
    }

}
