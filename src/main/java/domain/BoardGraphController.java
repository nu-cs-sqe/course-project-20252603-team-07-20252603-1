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
        Set<GraphEdge> validEdgesToClaim = boardGraph.getConnectingEdgesByID(nodeID);
        try {
            // will check to make sure edge neighbors nodeID
            boardGraph.getCorrectEdgeFromSet(validEdgesToClaim, startingNodeID, endingNodeID);
        } catch (IllegalArgumentException e) {
            throw new IllegalEdgeClaim("Edge must be adjacent to just placed settlement");
        }
        boardGraph.claimGraphEdgeObject(color, startingNodeID, endingNodeID);
        return true;
    }

/*
// TODO refactored from original BoardGraph class, need to change for new Controller class

    boolean playerClaimStoredEdge(PlayerColor color, int startingNodeID, int endingNodeID){
        boolean playerOwnsNeighboringNode = boardGraph.edgeCheckPlayerOwnsNeighboringNode(color, startingNodeID, endingNodeID);
        boolean playerOwnsNeighboringEdge = boardGraph.edgeCheckPlayerOwnsNeighboringEdge(color, startingNodeID, endingNodeID);
        if(playerOwnsNeighboringNode || playerOwnsNeighboringEdge) {
            Set<GraphEdge> connectingEdges = boardGraph.getConnectingEdgesByID(startingNodeID);
            // Our Edge of interest is guaranteed to be in this set IF it exists
            GraphEdge edgeToClaim = boardGraph.getCorrectEdgeFromSet(connectingEdges, startingNodeID, endingNodeID);
            edgeToClaim.claimGraphEdge(color);
            return true;
        }
        else {
            throw new IllegalArgumentException("To claim an edge, player must own an adjacent node or edge");
        }
    }

// TODO for the non-setup phase

    boolean playerClaimStoredNode(PlayerColor color, int nodeID) {
        //Node must be next to a built road, and not adjacent to any other claimed nodes
        return false;
    }
 */
}


