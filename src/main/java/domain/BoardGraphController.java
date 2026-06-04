package domain;

import java.util.Set;

public class BoardGraphController {
    private BoardGraph boardGraph;

    BoardGraphController(BoardGraph b){
        this.boardGraph = b;
    }

    boolean playerClaimStoredNodeSetupPhase(PlayerColor color, int nodeID){
        // In setup phase, node does not need to be adjacent to a claimed Edge;
        if (boardGraph.checkIfAdjacentNodesNotClaimed(nodeID)){
            return boardGraph.claimGraphNodeObject(color, nodeID);
        }
        else {
            throw new AdjacentNodeAlreadyClaimed("Can not claim node adjacent to node already claimed");
        }
    }

    boolean playerClaimStoredEdgeSetupPhase(PlayerColor color, int nodeID, int startingNodeID, int endingNodeID) {
        checkPlayerOwnsNode(color, nodeID);
        Set<GraphEdge> validEdgesToClaim = boardGraph.getConnectingEdgesByID(nodeID);
        try {
            // will check to make sure edge neighbors nodeID
            boardGraph.getMatchingEdgeFromSet(validEdgesToClaim, startingNodeID, endingNodeID);
        } catch (IllegalArgumentException e) {
            throw new IllegalEdgeClaim("Edge must be adjacent to just placed settlement");
        }
        boardGraph.claimGraphEdgeObject(color, startingNodeID, endingNodeID);
        return true;
    }

    private void checkPlayerOwnsNode(PlayerColor color, int nodeID) {
        if(!boardGraph.checkPlayerOwnsGraphNodeObject(color, nodeID)) {
            throw new IllegalEdgeClaim("During setup phase, player must own node next to edge they want to claim");
        }
    }


    void playerClaimStoredNode(PlayerColor color, int nodeID) {
        handleCheckNodeIsUnoccupied(nodeID);
        handleCheckAdjacentNodesNotClaimed(nodeID);
        handleCheckPlayerOwnsNeighboringEdge(color, nodeID);
        boardGraph.claimGraphNodeObject(color, nodeID);
    }

    private void handleCheckNodeIsUnoccupied(int nodeID) {
    }

    private void handleCheckPlayerOwnsNeighboringEdge(PlayerColor color, int nodeID) {
    }

    private void handleCheckAdjacentNodesNotClaimed(int nodeID) {
        if (!boardGraph.checkIfAdjacentNodesNotClaimed(nodeID)) {
            throw new IllegalSettlementPlacementException("Can not claim node adjacent to node already claimed");
        }
    }

}


