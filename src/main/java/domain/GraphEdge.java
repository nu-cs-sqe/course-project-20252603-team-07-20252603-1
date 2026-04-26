package domain;

public class GraphEdge {
    // unique Edge_id
    private int edgeID;
    // keep track of the
    private boolean roadBuilt;
    private PlayerColor owningPlayerColor;

    GraphEdge(int edgeID) {
        this.edgeID = edgeID;
        this.roadBuilt = false;
        this.owningPlayerColor = PlayerColor.SETUP;
    }

    // need to be able to claim an edge
    boolean claimGraphEdge(PlayerColor color) {
        this.roadBuilt = true;
        this.owningPlayerColor = color;
        return true;
    }

    boolean checkRoadExists() {
        return this.roadBuilt;
    }
    PlayerColor checkOwningColor() {
        return this.owningPlayerColor;
    }
}
