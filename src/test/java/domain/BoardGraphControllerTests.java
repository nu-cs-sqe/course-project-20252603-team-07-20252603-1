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


    /*
    // TODO platerClaimStoredEdge() tests
    @Test
    void playerClaimStoredEdge_test01_PlayerOwnsNeighboringNode_EdgeUnclaimed_ExpectTrue(){
        BoardGraph b = EasyMock.partialMockBuilder(BoardGraph.class)
                .withConstructor()
                .addMockedMethod("edgeCheckPlayerOwnsNeighboringEdge")
                .addMockedMethod("edgeCheckPlayerOwnsNeighboringNode")
                .createMock();

        EasyMock.expect(b.edgeCheckPlayerOwnsNeighboringEdge(PlayerColor.RED, 0, 1))
                .andStubReturn(false);
        EasyMock.expect(b.edgeCheckPlayerOwnsNeighboringNode(PlayerColor.RED, 0, 1))
                .andStubReturn(true);

        GraphNode nodeStub0 = EasyMock.createNiceMock(GraphNode.class);
        GraphNode nodeStub1 = EasyMock.createNiceMock(GraphNode.class);
        GraphEdge edgeMock0to1 = EasyMock.createMock(GraphEdge.class);

        EasyMock.expect(nodeStub0.getNodeID()).andStubReturn(0);
        EasyMock.expect(nodeStub0.checkOccupied()).andStubReturn(true);
        EasyMock.expect(nodeStub0.checkColor()).andStubReturn(PlayerColor.RED);
        EasyMock.expect(nodeStub1.getNodeID()).andStubReturn(1);

        EasyMock.expect(edgeMock0to1.getStartingNodeID()).andReturn(0);
        EasyMock.expect(edgeMock0to1.getEndingNodeID()).andReturn(1);
        EasyMock.expect(edgeMock0to1.claimGraphEdge(PlayerColor.RED)).andReturn(true);

        EasyMock.replay(nodeStub0, nodeStub1, edgeMock0to1, b);

        b.addGraphNodeObject(nodeStub0);
        b.addGraphNodeObject(nodeStub1);
        b.addGraphNodeConnection(0, edgeMock0to1);
        b.addGraphNodeConnection(1, edgeMock0to1);

        assertTrue(b.playerClaimStoredEdge(PlayerColor.RED, 0, 1));
        EasyMock.verify(edgeMock0to1);
    }

    @Test
    void playerClaimStoredEdge_test02_PlayerOwnsNeighboringNode_EdgeUnclaimed_ExpectTrue(){
        BoardGraph b = EasyMock.partialMockBuilder(BoardGraph.class)
                .withConstructor()
                .addMockedMethod("edgeCheckPlayerOwnsNeighboringEdge")
                .addMockedMethod("edgeCheckPlayerOwnsNeighboringNode")
                .createMock();
        GraphNode nodeStub0 = EasyMock.createNiceMock(GraphNode.class);
        GraphEdge edgeMock0to1 = EasyMock.createMock(GraphEdge.class);

        EasyMock.expect(b.edgeCheckPlayerOwnsNeighboringEdge(PlayerColor.BLUE, 0, 1))
                .andStubReturn(false);
        EasyMock.expect(b.edgeCheckPlayerOwnsNeighboringNode(PlayerColor.BLUE, 0, 1))
                .andStubReturn(true);
        EasyMock.expect(nodeStub0.getNodeID()).andStubReturn(0);
        EasyMock.expect(edgeMock0to1.getStartingNodeID()).andReturn(0);
        EasyMock.expect(edgeMock0to1.getEndingNodeID()).andReturn(1);
        EasyMock.expect(edgeMock0to1.claimGraphEdge(PlayerColor.BLUE)).andReturn(true);

        EasyMock.replay(nodeStub0, edgeMock0to1, b);

        b.addGraphNodeObject(nodeStub0);
        b.addGraphNodeConnection(0, edgeMock0to1);

        assertTrue(b.playerClaimStoredEdge(PlayerColor.BLUE, 0, 1));
        EasyMock.verify(edgeMock0to1);
    }

    @Test
    void playerClaimsStoredEdge_test03_EdgeDoesNotExist_ExpectError(){
        BoardGraph b = EasyMock.partialMockBuilder(BoardGraph.class)
                .withConstructor()
                .addMockedMethod("edgeCheckPlayerOwnsNeighboringEdge")
                .addMockedMethod("edgeCheckPlayerOwnsNeighboringNode")
                .createMock();
        GraphNode nodeStub = EasyMock.createNiceMock(GraphNode.class);

        EasyMock.expect(b.edgeCheckPlayerOwnsNeighboringEdge(PlayerColor.ORANGE, 52, 53))
                .andStubReturn(false);
        EasyMock.expect(b.edgeCheckPlayerOwnsNeighboringNode(PlayerColor.ORANGE, 52, 53))
                .andStubReturn(true);
        EasyMock.expect(nodeStub.getNodeID()).andStubReturn(52);

        EasyMock.replay(nodeStub, b);

        b.addGraphNodeObject(nodeStub);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> b.playerClaimStoredEdge(PlayerColor.ORANGE, 52, 53));

        assertEquals("Edge does not exist", exception.getMessage());
    }

    @Test
    void playerClaimsStoredEdge_test04_EdgeAlreadyClaimed_ExpectError(){
        BoardGraph b = EasyMock.partialMockBuilder(BoardGraph.class)
                .withConstructor()
                .addMockedMethod("edgeCheckPlayerOwnsNeighboringEdge")
                .addMockedMethod("edgeCheckPlayerOwnsNeighboringNode")
                .createMock();
        GraphNode nodeStub = EasyMock.createNiceMock(GraphNode.class);
        GraphEdge edgeMock = EasyMock.createMock(GraphEdge.class);

        EasyMock.expect(b.edgeCheckPlayerOwnsNeighboringEdge(PlayerColor.WHITE, 52, 53))
                .andStubReturn(false);
        EasyMock.expect(b.edgeCheckPlayerOwnsNeighboringNode(PlayerColor.WHITE, 52, 53))
                .andStubReturn(true);

        EasyMock.expect(nodeStub.getNodeID()).andStubReturn(52);
        EasyMock.expect(edgeMock.getStartingNodeID()).andReturn(52);
        EasyMock.expect(edgeMock.getEndingNodeID()).andReturn(53);
        EasyMock.expect(edgeMock.claimGraphEdge(PlayerColor.WHITE)).andThrow(new IllegalArgumentException("Edge already claimed"));

        EasyMock.replay(nodeStub, edgeMock, b);

        b.addGraphNodeObject(nodeStub);
        b.addGraphNodeConnection(52, edgeMock);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> b.playerClaimStoredEdge(PlayerColor.WHITE, 52, 53));

        assertEquals("Edge already claimed", exception.getMessage());

        EasyMock.verify(edgeMock);
    }

    @Test
    void playerClaimsStoredEdge_test05_PlayerOwnsNoAdjacency_ExpectError(){
        BoardGraph b = EasyMock.partialMockBuilder(BoardGraph.class)
                .withConstructor()
                .addMockedMethod("edgeCheckPlayerOwnsNeighboringEdge")
                .addMockedMethod("edgeCheckPlayerOwnsNeighboringNode")
                .createMock();

        EasyMock.expect(b.edgeCheckPlayerOwnsNeighboringEdge(PlayerColor.RED, 52, 53))
                .andStubReturn(false);
        EasyMock.expect(b.edgeCheckPlayerOwnsNeighboringNode(PlayerColor.RED, 52, 53))
                .andStubReturn(false);

        EasyMock.replay(b);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> b.playerClaimStoredEdge(PlayerColor.RED, 52, 53));

        assertEquals("To claim an edge, player must own an adjacent node or edge", exception.getMessage());
    }

    @Test
    void playerClaimsStoredEdge_test06_PlayerOwnsAdjacentEdge_ExpectTrue(){
        BoardGraph b = EasyMock.partialMockBuilder(BoardGraph.class)
                .withConstructor()
                .addMockedMethod("edgeCheckPlayerOwnsNeighboringEdge")
                .addMockedMethod("edgeCheckPlayerOwnsNeighboringNode")
                .createMock();

        GraphNode nodeStub0 = EasyMock.createNiceMock(GraphNode.class);
        GraphEdge edgeMock0to1 = EasyMock.createMock(GraphEdge.class);

        EasyMock.expect(b.edgeCheckPlayerOwnsNeighboringEdge(PlayerColor.RED, 0, 1))
                .andStubReturn(true);
        EasyMock.expect(b.edgeCheckPlayerOwnsNeighboringNode(PlayerColor.RED, 0, 1))
                .andStubReturn(false);
        EasyMock.expect(nodeStub0.getNodeID()).andStubReturn(0);
        EasyMock.expect(edgeMock0to1.getStartingNodeID()).andReturn(0);
        EasyMock.expect(edgeMock0to1.getEndingNodeID()).andReturn(1);
        EasyMock.expect(edgeMock0to1.claimGraphEdge(PlayerColor.RED)).andReturn(true);
        EasyMock.replay(nodeStub0, edgeMock0to1, b);

        b.addGraphNodeObject(nodeStub0);
        b.addGraphNodeConnection(0, edgeMock0to1);

        assertTrue(b.playerClaimStoredEdge(PlayerColor.RED, 0, 1));
        EasyMock.verify(edgeMock0to1);

    }
*/
}


