package domain.model.board;

import java.util.Set;

import domain.model.exceptions.AdjacentNodeAlreadyClaimed;
import domain.model.exceptions.IllegalEdgeClaim;
import domain.model.player.PlayerColor;

public class BoardGraphController {
    private BoardGraph boardGraph;

    public BoardGraphController(BoardGraph b){
        this.boardGraph = b;
    }

    public boolean playerClaimStoredNodeSetupPhase(PlayerColor color, int nodeID){
        // In setup phase, node does not need to be adjacent to a claimed Edge;
        if (boardGraph.checkIfAdjacentNodesNotClaimed(nodeID)){
            return boardGraph.claimGraphNodeObject(color, nodeID);
        }
        else {
            throw new AdjacentNodeAlreadyClaimed("Can not claim node adjacent to node already claimed");
        }
    }

    public boolean playerClaimStoredEdgeSetupPhase(PlayerColor color, int nodeID, int startingNodeID, int endingNodeID) {
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

/*
// TODO for the non-setup phase

    boolean playerClaimStoredNode(PlayerColor color, int nodeID) {
        //Node must be next to a built road, and not adjacent to any other claimed nodes
        return false;
    }
 */
}


