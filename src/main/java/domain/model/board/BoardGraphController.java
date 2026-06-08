package domain.model.board;

import domain.model.exceptions.AdjacentNodeAlreadyClaimed;
import domain.model.exceptions.IllegalEdgeClaim;
import domain.model.exceptions.IllegalSettlementPlacementException;
import domain.model.player.PlayerColor;

import java.util.Set;

public class BoardGraphController {
    private final BoardGraph boardGraph;

    public BoardGraphController(BoardGraph b) {
        this.boardGraph = b;
    }

    public boolean playerClaimStoredNodeSetupPhase(PlayerColor color, int nodeID) {
        if (boardGraph.checkIfAdjacentNodesNotClaimed(nodeID)) {
            return boardGraph.claimGraphNodeObject(color, nodeID);
        } else {
            throw new AdjacentNodeAlreadyClaimed("Can not claim node adjacent to node already claimed");
        }
    }

    public boolean playerClaimStoredEdgeSetupPhase(PlayerColor color, int nodeID, int startingNodeID, int endingNodeID) {
        checkPlayerOwnsNode(color, nodeID);
        Set<GraphEdge> validEdgesToClaim = boardGraph.getConnectingEdgesByID(nodeID);
        try {
            boardGraph.getMatchingEdgeFromSet(validEdgesToClaim, startingNodeID, endingNodeID);
        } catch (IllegalArgumentException e) {
            throw new IllegalEdgeClaim("Edge must be adjacent to just placed settlement");
        }
        boardGraph.claimGraphEdgeObject(color, startingNodeID, endingNodeID);
        return true;
    }

    private void checkPlayerOwnsNode(PlayerColor color, int nodeID) {
        if (!boardGraph.checkPlayerOwnsGraphNodeObject(color, nodeID)) {
            throw new IllegalEdgeClaim("During setup phase, player must own node next to edge they want to claim");
        }
    }

    void playerClaimStoredNode(PlayerColor color, int nodeID) {
        handleCheckNodeIsUnoccupied(nodeID);
        handleCheckAdjacentNodesNotClaimed(nodeID);
        nodeHandleCheckPlayerOwnsNeighboringEdge(color, nodeID);
        boardGraph.claimGraphNodeObject(color, nodeID);
    }

    private void handleCheckNodeIsUnoccupied(int nodeID) {
        if (boardGraph.checkNodeOccupied(nodeID)) {
            throw new IllegalSettlementPlacementException("Node already claimed");
        }
    }

    private void handleCheckAdjacentNodesNotClaimed(int nodeID) {
        if (!boardGraph.checkIfAdjacentNodesNotClaimed(nodeID)) {
            throw new IllegalSettlementPlacementException("Can not claim node adjacent to node already claimed");
        }
    }

    private void nodeHandleCheckPlayerOwnsNeighboringEdge(PlayerColor color, int nodeID) {
        if (!boardGraph.nodeCheckPlayerOwnsNeighboringEdge(color, nodeID)) {
            throw new IllegalSettlementPlacementException("Must own an adjacent road to claim node");
        }
    }

    void playerClaimStoredEdge(PlayerColor color, int startingNodeID, int endingNodeID) {
        handleCheckEdgeIsUnoccupied(startingNodeID, endingNodeID);
        edgeHandleCheckPlayerOwnsNeighboringEdge(color, startingNodeID, endingNodeID);
        boardGraph.claimGraphEdgeObject(color, startingNodeID, endingNodeID);
    }

    private void handleCheckEdgeIsUnoccupied(int startingNodeID, int endingNodeID) {
        if (boardGraph.checkEdgeOccupied(startingNodeID, endingNodeID)) {
            throw new IllegalEdgeClaim("Edge already claimed");
        }
    }

    private void edgeHandleCheckPlayerOwnsNeighboringEdge(PlayerColor color, int startingNodeID, int endingNodeID) {
        if (!boardGraph.edgeCheckPlayerOwnsNeighboringEdge(color, startingNodeID, endingNodeID)) {
            throw new IllegalEdgeClaim("Edge must be adjacent to an owned structure");
        }
    }
}
