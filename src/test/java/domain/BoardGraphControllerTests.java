package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class BoardGraphControllerTests {
    @Test
    void playerClaimStoredNodeSetup_test01_NodeExists_NodeUnclaimed_ExpectTrue(){
        BoardGraph boardMock = EasyMock.createMock(BoardGraph.class);
        BoardGraphController boardControl = new BoardGraphController(boardMock);

        EasyMock.expect(boardMock.checkIfAdjacentNodesNotClaimed(0)).andReturn(true);
        EasyMock.expect(boardMock.claimGraphNodeObject(PlayerColor.RED, 0)).andReturn(true);
        EasyMock.replay(boardMock);

        assertTrue(boardControl.playerClaimStoredNodeSetupPhase(PlayerColor.RED, 0));
        EasyMock.verify(boardMock);
    }

    @Test
    void playerClaimStoredNodeSetup_test02_NodeDoesNotExist_ExpectError() {
        BoardGraph boardMock = EasyMock.createStrictMock(BoardGraph.class);
        BoardGraphController boardControl = new BoardGraphController(boardMock);
        EasyMock.expect(boardMock.checkIfAdjacentNodesNotClaimed(0))
                .andThrow(new IllegalArgumentException("Node does not exist"));
        EasyMock.replay(boardMock);

        Exception exception = assertThrows(IllegalArgumentException.class,
                ()-> boardControl.playerClaimStoredNodeSetupPhase(PlayerColor.BLUE, 0));

        assertEquals("Node does not exist", exception.getMessage());
        EasyMock.verify(boardMock);
    }

    @Test
    void playerClaimStoredNodeSetup_test03_NodeExists_AlreadyClaimed_ExpectError() {
        BoardGraph boardMock = EasyMock.createMock(BoardGraph.class);
        BoardGraphController boardControl = new BoardGraphController(boardMock);

       EasyMock.expect(boardMock.checkIfAdjacentNodesNotClaimed(53)).andReturn(true);
       EasyMock.expect(boardMock.claimGraphNodeObject(PlayerColor.ORANGE, 53))
               .andThrow(new IllegalArgumentException("Node already claimed"));
       EasyMock.replay(boardMock);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> boardControl.playerClaimStoredNodeSetupPhase(PlayerColor.ORANGE, 53));

        assertEquals("Node already claimed", exception.getMessage());
        EasyMock.verify(boardMock);
    }

    @Test
    void playerClaimStoredNodeSetup_test04_NeighboringNodeClaimed_ExpectError(){
        BoardGraph boardMock = EasyMock.createStrictMock(BoardGraph.class);
        BoardGraphController boardControl = new BoardGraphController(boardMock);

        EasyMock.expect(boardMock.checkIfAdjacentNodesNotClaimed(0)).andReturn(false);
        EasyMock.replay(boardMock);

        Exception exception = assertThrows(AdjacentNodeAlreadyClaimed.class,
                () -> boardControl.playerClaimStoredNodeSetupPhase(PlayerColor.WHITE, 0));

        assertEquals("Can not claim node adjacent to node already claimed", exception.getMessage());
    }

    @Test
    void playerClaimStoredEdgeSetup_test01_JustClaimedNeighboringNode_EdgeUnclaimed_ExpectTrue(){
        BoardGraph boardMock = EasyMock.createNiceMock(BoardGraph.class);
        BoardGraphController boardControl = new BoardGraphController(boardMock);
        EasyMock.expect(boardMock.claimGraphEdgeObject(PlayerColor.RED, 0, 3)).andReturn(true);
        EasyMock.replay(boardMock);
        assertTrue(boardControl.playerClaimStoredEdgeSetupPhase(PlayerColor.RED, 0, 0, 3));
        EasyMock.verify(boardMock);
    }

    @Test
    void playerClaimStoredEdgeSetup_test02_JustClaimedNotNeighboringNode_EdgeUnclaimed_ExpectError(){
        BoardGraph boardMock = EasyMock.createMock(BoardGraph.class);
        BoardGraphController boardControl = new BoardGraphController(boardMock);
        EasyMock.expect(boardMock.getConnectingEdgesByID(2)).andReturn(new HashSet<>());
        EasyMock.expect(boardMock.getCorrectEdgeFromSet(new HashSet<>(), 0, 3))
                .andThrow(new IllegalArgumentException("Edge does not exist"));
        EasyMock.replay(boardMock);

        Exception exception = assertThrows(IllegalEdgeClaim.class,
                () -> boardControl.playerClaimStoredEdgeSetupPhase(PlayerColor.BLUE, 2, 0, 3));

        assertEquals("Edge must be adjacent to just placed settlement",
                exception.getMessage());

        EasyMock.verify(boardMock);
    }

    @Test
    void playerClaimStoredEdgeSetup_test03_JustClaimedNeighboringNode_EdgeClaimed_ExpectError(){
        BoardGraph boardMock = EasyMock.createMock(BoardGraph.class);
        BoardGraphController boardControl = new BoardGraphController(boardMock);
        EasyMock.expect(boardMock.getConnectingEdgesByID(50)).andReturn(new HashSet<>());
        EasyMock.expect(boardMock.getCorrectEdgeFromSet(new HashSet<>(), 50, 53)).andReturn(new GraphEdge(50, 53));
        EasyMock.expect(boardMock.claimGraphEdgeObject(PlayerColor.ORANGE, 50, 53))
                        .andThrow(new EdgeAlreadyClaimedException("Edge already claimed"));
        EasyMock.replay(boardMock);

        Exception exception = assertThrows(EdgeAlreadyClaimedException.class,
                () -> boardControl.playerClaimStoredEdgeSetupPhase(PlayerColor.ORANGE, 50, 50, 53));

        assertEquals("Edge already claimed",
                exception.getMessage());

        EasyMock.verify(boardMock);
    }

    @Test
    void playerClaimStoredEdgeSetup_test04_JustClaimedNeighboringNode_EdgeUnclaimed_ExpectError(){
        BoardGraph boardMock = EasyMock.createNiceMock(BoardGraph.class);
        BoardGraphController boardControl = new BoardGraphController(boardMock);
        EasyMock.expect(boardMock.claimGraphEdgeObject(PlayerColor.WHITE, 50, 53)).andReturn(true);
        EasyMock.replay(boardMock);

        assertTrue(boardControl.playerClaimStoredEdgeSetupPhase(PlayerColor.WHITE, 53, 50, 53));

        EasyMock.verify(boardMock);
    }

}


