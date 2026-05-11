package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class BoardGraphTests {

    // addGraphNodeObj() Tests
    @Test
    void addNodeToGraph_test01_EmptyGraph_ExpectTrue(){
        BoardGraph b = new BoardGraph();
        GraphNode nodeMock = EasyMock.createMock(GraphNode.class);
        EasyMock.expect(nodeMock.getNodeID()).andReturn(0);
        EasyMock.replay(nodeMock);

        assertTrue(b.addGraphNodeObject(nodeMock));
        assertNotNull(b.getGraphNodeByID(0));
        assertNotNull(b.getConnectingEdgesByID(0));
        EasyMock.verify(nodeMock);

    }

    @Test
    void addNodeToGraph_test02_OneElementGraph_ExpectTrue(){
        BoardGraph b = new BoardGraph();

        GraphNode nodeMock1 = EasyMock.createMock(GraphNode.class);
        GraphNode nodeMock2 = EasyMock.createMock(GraphNode.class);

        EasyMock.expect(nodeMock1.getNodeID()).andReturn(0);
        EasyMock.expect(nodeMock2.getNodeID()).andReturn(53);
        EasyMock.replay(nodeMock1, nodeMock2);

        assertTrue(b.addGraphNodeObject(nodeMock1));
        assertTrue(b.addGraphNodeObject(nodeMock2));
        assertNotNull(b.getGraphNodeByID(53));
        assertNotNull(b.getConnectingEdgesByID(53));
        EasyMock.verify(nodeMock1, nodeMock2);
    }

    @Test
    void addNodeToGraph_test03_MultipleElementGraph_ExpectTrue(){
        BoardGraph b = new BoardGraph();

        GraphNode nodeMock1 = EasyMock.createMock(GraphNode.class);
        GraphNode nodeMock2 = EasyMock.createMock(GraphNode.class);
        GraphNode nodeMock3 = EasyMock.createMock(GraphNode.class);

        EasyMock.expect(nodeMock1.getNodeID()).andReturn(0);
        EasyMock.expect(nodeMock2.getNodeID()).andReturn(1);
        EasyMock.expect(nodeMock3.getNodeID()).andReturn(53);
        EasyMock.replay(nodeMock1, nodeMock2, nodeMock3);

        b.addGraphNodeObject(nodeMock1);
        b.addGraphNodeObject(nodeMock2);

        assertTrue(b.addGraphNodeObject(nodeMock3));

        assertNotNull(b.getGraphNodeByID(53));
        assertNotNull(b.getConnectingEdgesByID(53));

        EasyMock.verify(nodeMock1, nodeMock2, nodeMock3);

    }

    @Test
    void addDuplicateNodeToGraph_test04_ExpectError() {
        BoardGraph b = new BoardGraph();

        GraphNode nodeMock1 = EasyMock.createMock(GraphNode.class);
        GraphNode nodeMock2 = EasyMock.createMock(GraphNode.class);
        GraphNode nodeMock3 = EasyMock.createMock(GraphNode.class);

        EasyMock.expect(nodeMock1.getNodeID()).andReturn(0);
        EasyMock.expect(nodeMock2.getNodeID()).andReturn(1);
        EasyMock.expect(nodeMock3.getNodeID()).andReturn(0);

        EasyMock.replay(nodeMock1, nodeMock2, nodeMock3);

        b.addGraphNodeObject(nodeMock1);
        b.addGraphNodeObject(nodeMock2);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> b.addGraphNodeObject(nodeMock3));

        assertEquals("Node already exists", exception.getMessage());

        assertNotNull(b.getGraphNodeByID(0));
        assertNotNull(b.getConnectingEdgesByID(0));

        EasyMock.verify(nodeMock1, nodeMock2);

    }

    // getGraphNodeByID() Tests
    @Test
    void getNodeID0_test01_EmptyMap_ExpectError(){
        BoardGraph b = new BoardGraph();

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> b.getGraphNodeByID(0));

        assertEquals("Node does not exist", exception.getMessage());

    }

    @Test
    void getNodeID0_test02_OneElementMap_ID0Exists_ExpectGraphNode(){
        BoardGraph b = new BoardGraph();

        GraphNode nodeStub = EasyMock.createMock(GraphNode.class);
        EasyMock.expect(nodeStub.getNodeID()).andStubReturn(0);

        b.addGraphNodeObject(nodeStub);

        GraphNode result = b.getGraphNodeByID(0);

        assertNotNull(result);
        assertEquals(nodeStub, result);

    }

    @Test
    void getNodeID53_test03_MultipleElementMap_ID53DoesNotExists_ExpectError(){
        BoardGraph b = new BoardGraph();

        GraphNode nodeStub0 = EasyMock.createMock(GraphNode.class);
        GraphNode nodeStub1 = EasyMock.createMock(GraphNode.class);

        EasyMock.expect(nodeStub0.getNodeID()).andStubReturn(0);
        EasyMock.expect(nodeStub1.getNodeID()).andStubReturn(1);
        EasyMock.replay(nodeStub0, nodeStub1);

        b.addGraphNodeObject(nodeStub0);
        b.addGraphNodeObject(nodeStub1);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> b.getGraphNodeByID(53));


        assertEquals("Node does not exist", exception.getMessage());
    }

    // addGraphNodeConnection() Tests
    @Test
    void addNewEdge_test01_NotDuplicate_NodeExistsInMap_ExpectTrue() {
        BoardGraph b = new BoardGraph();

        GraphNode nodeStub = EasyMock.createMock(GraphNode.class);
        GraphEdge edgeStub = EasyMock.createMock(GraphEdge.class);

        EasyMock.expect(nodeStub.getNodeID()).andStubReturn(0);
        EasyMock.replay(nodeStub, edgeStub);

        b.addGraphNodeObject(nodeStub);

        assertTrue(b.addGraphNodeConnection(0, edgeStub));

        assertTrue(b.getConnectingEdgesByID(0).contains(edgeStub));
    }

    @Test
    void ddNewEdge_test02_Duplicate_NodeExistsInMap_ExpectError() {
        BoardGraph b = new BoardGraph();

        GraphNode nodeStub = EasyMock.createMock(GraphNode.class);
        GraphEdge edgeStub = EasyMock.createMock(GraphEdge.class);

        EasyMock.expect(nodeStub.getNodeID()).andStubReturn(0);
        EasyMock.replay(nodeStub, edgeStub);

        b.addGraphNodeObject(nodeStub);

        b.addGraphNodeConnection(0, edgeStub);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> b.addGraphNodeConnection(0, edgeStub));


        assertEquals("Node already has specified edge", exception.getMessage());

    }

    @Test
    void addNewEdge_test03_Duplicate_SeparateExistingNode_ExpectTrue() {
        BoardGraph b = new BoardGraph();

        GraphNode nodeStub0 = EasyMock.createMock(GraphNode.class);
        GraphNode nodeStub1 = EasyMock.createMock(GraphNode.class);
        GraphEdge edgeStub = EasyMock.createMock(GraphEdge.class);

        EasyMock.expect(nodeStub0.getNodeID()).andStubReturn(0);
        EasyMock.expect(nodeStub1.getNodeID()).andStubReturn(1);

        EasyMock.replay(nodeStub0, nodeStub1, edgeStub);

        b.addGraphNodeObject(nodeStub0);
        b.addGraphNodeObject(nodeStub1);
        b.addGraphNodeConnection(0, edgeStub);

        assertTrue(b.addGraphNodeConnection(1, edgeStub));
        assertTrue(b.getConnectingEdgesByID(0).contains(edgeStub));
        assertTrue(b.getConnectingEdgesByID(1).contains(edgeStub));

    }

    @Test
    void addNewEdge_test04_NodeDoesNotExist_ExpectError() {
        BoardGraph b = new BoardGraph();
        GraphEdge edgeStub = EasyMock.createMock(GraphEdge.class);

        EasyMock.replay(edgeStub);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> b.addGraphNodeConnection(0, edgeStub));

        assertEquals("Node does not exist", exception.getMessage());

    }

    // getConnectingEdgesByID Tests
    @Test
    void getEdgeSet_test01_NodeDoesNotExist_ExpectError() {
        BoardGraph b = new BoardGraph();

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> b.getConnectingEdgesByID(0));

        assertEquals("Node does not exist", exception.getMessage());
    }

    @Test
    void getEdgeSet_test02_OneNodeExists_ExpectEmptySet() {
        BoardGraph b = new BoardGraph();
        GraphNode nodeStub = EasyMock.createMock(GraphNode.class);

        EasyMock.expect(nodeStub.getNodeID()).andStubReturn(0);

        EasyMock.replay(nodeStub);

        b.addGraphNodeObject(nodeStub);

        assertNotNull(b.getConnectingEdgesByID(0));
        assertEquals(0, b.getConnectingEdgesByID(0).size());
    }

    @Test
    void getEdgeSet_test03_MultipleNodesExist_ExpectOneEdgeSet() {
        BoardGraph b = new BoardGraph();
        GraphNode nodeStub0 = EasyMock.createMock(GraphNode.class);
        GraphNode nodeStub53 = EasyMock.createMock(GraphNode.class);
        GraphEdge edgeStub = EasyMock.createMock(GraphEdge.class);

        EasyMock.expect(nodeStub0.getNodeID()).andStubReturn(0);
        EasyMock.expect(nodeStub53.getNodeID()).andStubReturn(53);

        EasyMock.replay(nodeStub0, nodeStub53, edgeStub);

        b.addGraphNodeObject(nodeStub0);
        b.addGraphNodeObject(nodeStub53);
        b.addGraphNodeConnection(53, edgeStub);

        assertNotNull(b.getConnectingEdgesByID(53));
        assertEquals(1, b.getConnectingEdgesByID(53).size());
        assertTrue(b.getConnectingEdgesByID(53).contains(edgeStub));
    }

    @Test
    void getEdgeSet_test04_MultipleNodesExist_ExpectMultipleEdgeSet() {
        BoardGraph b = new BoardGraph();
        GraphNode nodeStub0 = EasyMock.createMock(GraphNode.class);
        GraphNode nodeStub53 = EasyMock.createMock(GraphNode.class);
        GraphEdge edgeStub0 = EasyMock.createMock(GraphEdge.class);
        GraphEdge edgeStub1 = EasyMock.createMock(GraphEdge.class);

        EasyMock.expect(nodeStub0.getNodeID()).andStubReturn(0);
        EasyMock.expect(nodeStub53.getNodeID()).andStubReturn(53);

        EasyMock.replay(nodeStub0, nodeStub53, edgeStub0, edgeStub1);

        b.addGraphNodeObject(nodeStub0);
        b.addGraphNodeObject(nodeStub53);
        b.addGraphNodeConnection(53, edgeStub0);
        b.addGraphNodeConnection(53, edgeStub1);

        assertNotNull(b.getConnectingEdgesByID(53));
        assertEquals(2, b.getConnectingEdgesByID(53).size());
        assertTrue(b.getConnectingEdgesByID(53).contains(edgeStub0));
        assertTrue(b.getConnectingEdgesByID(53).contains(edgeStub1));
    }

    @Test
    void playerClaimStoredNode_test01_NodeExists_NodeUnclaimed_ExpectTrue(){
        BoardGraph b = new BoardGraph();
        GraphNode nodeMock = EasyMock.createMock(GraphNode.class);

        EasyMock.expect(nodeMock.getNodeID()).andReturn(0);
        EasyMock.expect(nodeMock.playerClaimNode(PlayerColor.RED)).andReturn(true);
        EasyMock.replay(nodeMock);

        b.addGraphNodeObject(nodeMock);
        assertTrue(b.playerClaimStoredNode(PlayerColor.RED, 0));
        EasyMock.verify(nodeMock);
    }

    @Test
    void playerClaimStoredNode_test02_NodeDoesNotExist_ExpectError() {
        BoardGraph b = new BoardGraph();

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> b.playerClaimStoredNode(PlayerColor.BLUE, 0));

        assertEquals("Node does not exist", exception.getMessage());
    }

    @Test
    void playerClaimStoredNode_test03_NodeExists_AlreadyClaimed_ExpectError() {
        BoardGraph b = new BoardGraph();
        GraphNode nodeMock = EasyMock.createMock(GraphNode.class);

        EasyMock.expect(nodeMock.getNodeID()).andReturn(0);
        EasyMock.expect(nodeMock.playerClaimNode(PlayerColor.BLUE)).andReturn(true);
        EasyMock.expect(nodeMock.playerClaimNode(PlayerColor.ORANGE)).andThrow(new IllegalArgumentException("Node already claimed"));
        EasyMock.replay(nodeMock);

        b.addGraphNodeObject(nodeMock);

        b.playerClaimStoredNode(PlayerColor.BLUE, 0);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> b.playerClaimStoredNode(PlayerColor.ORANGE, 0));

        assertEquals("Node already claimed", exception.getMessage());
        EasyMock.verify(nodeMock);
    }

    @Test
    void playerClaimStoredNode_test04_NodeExists_NodeUnclaimed_ExpectTrue(){
        BoardGraph b = new BoardGraph();
        GraphNode nodeMock = EasyMock.createMock(GraphNode.class);
        GraphNode nodeMock2 = EasyMock.createMock(GraphNode.class);

        EasyMock.expect(nodeMock.getNodeID()).andReturn(0);
        EasyMock.expect(nodeMock2.getNodeID()).andReturn(53);
        EasyMock.expect(nodeMock2.playerClaimNode(PlayerColor.WHITE)).andReturn(true);
        EasyMock.replay(nodeMock, nodeMock2);

        b.addGraphNodeObject(nodeMock);
        b.addGraphNodeObject(nodeMock2);

        assertTrue(b.playerClaimStoredNode(PlayerColor.WHITE, 53));
        EasyMock.verify(nodeMock, nodeMock2);
    }

    @Test
    void playerClaimStoredNode_test05_NeighboringNodeClaimed_ExpectError(){
        BoardGraph b = new BoardGraph();
        GraphNode nodeMock0 = EasyMock.createMock(GraphNode.class);
        GraphNode nodeMock1 = EasyMock.createMock(GraphNode.class);
        GraphNode nodeMock2 = EasyMock.createMock(GraphNode.class);
        GraphEdge edgeMock0to1 = EasyMock.createMock(GraphEdge.class);
        GraphEdge edgeMock0to2 = EasyMock.createMock(GraphEdge.class);

        EasyMock.expect(nodeMock0.getNodeID()).andStubReturn(0);
        EasyMock.expect(nodeMock1.getNodeID()).andStubReturn(1);
        EasyMock.expect(nodeMock2.getNodeID()).andStubReturn(2);

        EasyMock.expect(edgeMock0to1.getStartingNodeID()).andStubReturn(0);
        EasyMock.expect(edgeMock0to1.getEndingNodeID()).andStubReturn(1);
        EasyMock.expect(edgeMock0to2.getStartingNodeID()).andStubReturn(0);
        EasyMock.expect(edgeMock0to2.getEndingNodeID()).andStubReturn(2);

        EasyMock.expect(nodeMock1.checkOccupied()).andStubReturn(true);
        EasyMock.expect(nodeMock2.checkOccupied()).andStubReturn(false);

        EasyMock.replay(nodeMock0, nodeMock1, nodeMock2, edgeMock0to1, edgeMock0to2);

        b.addGraphNodeObject(nodeMock0);
        b.addGraphNodeObject(nodeMock1);
        b.addGraphNodeObject(nodeMock2);
        b.addGraphNodeConnection(0, edgeMock0to1);
        b.addGraphNodeConnection(0, edgeMock0to2);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> b.playerClaimStoredNode(PlayerColor.BLUE, 0));

        assertEquals("Can not claim node adjacent to node already claimed", exception.getMessage());
    }

    @Test
    void playerClaimStoredNode_test06_NeighboringNodeNotClaimed_ExpectTrue(){
        BoardGraph b = new BoardGraph();
        GraphNode nodeMock0 = EasyMock.createMock(GraphNode.class);
        GraphNode nodeMock1 = EasyMock.createMock(GraphNode.class);
        GraphNode nodeMock2 = EasyMock.createMock(GraphNode.class);
        GraphEdge edgeMock0to1 = EasyMock.createMock(GraphEdge.class);
        GraphEdge edgeMock0to2 = EasyMock.createMock(GraphEdge.class);

        EasyMock.expect(nodeMock0.getNodeID()).andStubReturn(0);
        EasyMock.expect(nodeMock1.getNodeID()).andStubReturn(1);
        EasyMock.expect(nodeMock2.getNodeID()).andStubReturn(2);

        EasyMock.expect(edgeMock0to1.getStartingNodeID()).andStubReturn(0);
        EasyMock.expect(edgeMock0to1.getEndingNodeID()).andStubReturn(1);
        EasyMock.expect(edgeMock0to2.getStartingNodeID()).andStubReturn(0);
        EasyMock.expect(edgeMock0to2.getEndingNodeID()).andStubReturn(2);

        EasyMock.expect(nodeMock1.checkOccupied()).andStubReturn(false);
        EasyMock.expect(nodeMock2.checkOccupied()).andStubReturn(false);

        EasyMock.expect(nodeMock0.playerClaimNode(PlayerColor.RED)).andStubReturn(true);

        EasyMock.replay(nodeMock0, nodeMock1, nodeMock2, edgeMock0to1, edgeMock0to2);

        b.addGraphNodeObject(nodeMock0);
        b.addGraphNodeObject(nodeMock1);
        b.addGraphNodeObject(nodeMock2);
        b.addGraphNodeConnection(0, edgeMock0to1);
        b.addGraphNodeConnection(0, edgeMock0to2);

        assertTrue(b.playerClaimStoredNode(PlayerColor.RED, 0));
    }

    // platerClaimStoredEdge() tests
    @Test
    void playerClaimStoredEdge_test01_PlayerOwnsNeighboringNode_EdgeUnclaimed_ExpectTrue(){
        BoardGraph b = EasyMock.partialMockBuilder(BoardGraph.class)
                .withConstructor()
                .addMockedMethod("checkPlayerOwnsNeighboringEdge")
                .addMockedMethod("checkPlayerOwnsNeighboringNode")
                .createMock();

        EasyMock.expect(b.checkPlayerOwnsNeighboringEdge(PlayerColor.RED, 0, 1))
                .andStubReturn(false);
        EasyMock.expect(b.checkPlayerOwnsNeighboringNode(PlayerColor.RED, 0, 1))
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
                .addMockedMethod("checkPlayerOwnsNeighboringEdge")
                .addMockedMethod("checkPlayerOwnsNeighboringNode")
                .createMock();
        GraphNode nodeStub0 = EasyMock.createNiceMock(GraphNode.class);
        GraphEdge edgeMock0to1 = EasyMock.createMock(GraphEdge.class);

        EasyMock.expect(b.checkPlayerOwnsNeighboringEdge(PlayerColor.BLUE, 0, 1))
                .andStubReturn(false);
        EasyMock.expect(b.checkPlayerOwnsNeighboringNode(PlayerColor.BLUE, 0, 1))
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
                .addMockedMethod("checkPlayerOwnsNeighboringEdge")
                .addMockedMethod("checkPlayerOwnsNeighboringNode")
                .createMock();
        GraphNode nodeStub = EasyMock.createNiceMock(GraphNode.class);

        EasyMock.expect(b.checkPlayerOwnsNeighboringEdge(PlayerColor.ORANGE, 52, 53))
                .andStubReturn(false);
        EasyMock.expect(b.checkPlayerOwnsNeighboringNode(PlayerColor.ORANGE, 52, 53))
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
                .addMockedMethod("checkPlayerOwnsNeighboringEdge")
                .addMockedMethod("checkPlayerOwnsNeighboringNode")
                .createMock();
        GraphNode nodeStub = EasyMock.createNiceMock(GraphNode.class);
        GraphEdge edgeMock = EasyMock.createMock(GraphEdge.class);

        EasyMock.expect(b.checkPlayerOwnsNeighboringEdge(PlayerColor.WHITE, 52, 53))
                .andStubReturn(false);
        EasyMock.expect(b.checkPlayerOwnsNeighboringNode(PlayerColor.WHITE, 52, 53))
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
                .addMockedMethod("checkPlayerOwnsNeighboringEdge")
                .addMockedMethod("checkPlayerOwnsNeighboringNode")
                .createMock();

        EasyMock.expect(b.checkPlayerOwnsNeighboringEdge(PlayerColor.RED, 52, 53))
                .andStubReturn(false);
        EasyMock.expect(b.checkPlayerOwnsNeighboringNode(PlayerColor.RED, 52, 53))
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
                .addMockedMethod("checkPlayerOwnsNeighboringEdge")
                .addMockedMethod("checkPlayerOwnsNeighboringNode")
                .createMock();

        GraphNode nodeStub0 = EasyMock.createNiceMock(GraphNode.class);
        GraphEdge edgeMock0to1 = EasyMock.createMock(GraphEdge.class);

        EasyMock.expect(b.checkPlayerOwnsNeighboringEdge(PlayerColor.RED, 0, 1))
                .andStubReturn(true);
        EasyMock.expect(b.checkPlayerOwnsNeighboringNode(PlayerColor.RED, 0, 1))
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

    // getCorrectEdgeFromSet() tests
    @Test
    void getCorrectEdgeFromSet_test01_EmptySet_ExpectError(){
        BoardGraph b = new BoardGraph();
        GraphNode nodeStub = EasyMock.createNiceMock((GraphNode.class));
        EasyMock.expect(nodeStub.getNodeID()).andStubReturn(0);
        EasyMock.replay(nodeStub);

        b.addGraphNodeObject(nodeStub);
        Set<GraphEdge> node0EdgeSet = b.getConnectingEdgesByID(0);
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> b.getCorrectEdgeFromSet(node0EdgeSet, 0, 1));

        assertEquals("Edge does not exist", exception.getMessage());
    }

    @Test
    void getCorrectEdgeFromSet_test02_OneElementSet_ExpectEdge(){
        BoardGraph b = new BoardGraph();
        GraphNode nodeStub = EasyMock.createNiceMock(GraphNode.class);
        GraphEdge edgeStub = EasyMock.createNiceMock(GraphEdge.class);
        EasyMock.expect(nodeStub.getNodeID()).andStubReturn(0);
        EasyMock.expect(edgeStub.getStartingNodeID()).andStubReturn(0);
        EasyMock.expect(edgeStub.getEndingNodeID()).andStubReturn(1);
        EasyMock.replay(nodeStub, edgeStub);

        b.addGraphNodeObject(nodeStub);
        b.addGraphNodeConnection(0, edgeStub);
        Set<GraphEdge> node0EdgeSet = b.getConnectingEdgesByID(0);

        assertEquals(edgeStub, b.getCorrectEdgeFromSet(node0EdgeSet, 0, 1));
    }

    @Test
    void getCorrectEdgeFromSet_test03_MultipleElementSet_ExpectEdge(){
        BoardGraph b = new BoardGraph();
        GraphNode nodeStub = EasyMock.createNiceMock(GraphNode.class);
        GraphEdge edgeStub0 = EasyMock.createNiceMock(GraphEdge.class);
        GraphEdge edgeStub1 = EasyMock.createNiceMock(GraphEdge.class);
        EasyMock.expect(nodeStub.getNodeID()).andStubReturn(53);
        EasyMock.expect(edgeStub0.getStartingNodeID()).andStubReturn(52);
        EasyMock.expect(edgeStub0.getEndingNodeID()).andStubReturn(53);
        EasyMock.expect(edgeStub1.getStartingNodeID()).andStubReturn(51);
        EasyMock.expect(edgeStub1.getEndingNodeID()).andStubReturn(53);
        EasyMock.replay(nodeStub, edgeStub0, edgeStub1);

        b.addGraphNodeObject(nodeStub);
        b.addGraphNodeConnection(53, edgeStub0);
        b.addGraphNodeConnection(53, edgeStub1);
        Set<GraphEdge> node0EdgeSet = b.getConnectingEdgesByID(53);

        assertEquals(edgeStub1, b.getCorrectEdgeFromSet(node0EdgeSet, 51, 53));
    }

    @Test
    void getCorrectEdgeFromSet_test04_MultipleElementSet_EdgeDoesNotExist_ExpectError(){
        BoardGraph b = new BoardGraph();
        GraphNode nodeStub = EasyMock.createNiceMock(GraphNode.class);
        GraphEdge edgeStub0 = EasyMock.createNiceMock(GraphEdge.class);
        GraphEdge edgeStub1 = EasyMock.createNiceMock(GraphEdge.class);
        GraphEdge edgeStub2 = EasyMock.createNiceMock(GraphEdge.class);
        EasyMock.expect(nodeStub.getNodeID()).andStubReturn(53);

        EasyMock.expect(edgeStub0.getStartingNodeID()).andStubReturn(52);
        EasyMock.expect(edgeStub0.getEndingNodeID()).andStubReturn(53);

        EasyMock.expect(edgeStub1.getStartingNodeID()).andStubReturn(51);
        EasyMock.expect(edgeStub1.getEndingNodeID()).andStubReturn(53);

        EasyMock.expect(edgeStub2.getStartingNodeID()).andStubReturn(50);
        EasyMock.expect(edgeStub2.getEndingNodeID()).andStubReturn(53);

        EasyMock.replay(nodeStub, edgeStub0, edgeStub1, edgeStub2);

        b.addGraphNodeObject(nodeStub);
        b.addGraphNodeConnection(53, edgeStub0);
        b.addGraphNodeConnection(53, edgeStub1);
        b.addGraphNodeConnection(53, edgeStub2);
        Set<GraphEdge> node0EdgeSet = b.getConnectingEdgesByID(53);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> b.getCorrectEdgeFromSet(node0EdgeSet, 49, 53));

        assertEquals("Edge does not exist", exception.getMessage());

    }

    // checkPlayerOwnsNeighboringEdges() tests
    @Test
    void checkPlayerOwnsNeighboringEdges_test01_RedOwnsEdgeConnectingToStartingNode_ExpectTrue(){
        BoardGraph b = new BoardGraph();

        GraphNode nodeStub0 = EasyMock.createNiceMock(GraphNode.class);
        GraphNode nodeStub1 = EasyMock.createNiceMock(GraphNode.class);
        GraphEdge edge0to1 = EasyMock.createNiceMock(GraphEdge.class);
        // edge which red owns
        GraphEdge edge0to2 = EasyMock.createNiceMock(GraphEdge.class);

        EasyMock.expect(nodeStub0.getNodeID()).andStubReturn(0);
        EasyMock.expect(nodeStub1.getNodeID()).andStubReturn(1);

        EasyMock.expect(edge0to1.getStartingNodeID()).andStubReturn(0);
        EasyMock.expect(edge0to1.getEndingNodeID()).andStubReturn(1);
        EasyMock.expect(edge0to1.checkOwningColor()).andStubReturn(PlayerColor.SETUP);

        EasyMock.expect(edge0to2.getStartingNodeID()).andStubReturn(0);
        EasyMock.expect(edge0to2.getEndingNodeID()).andStubReturn(2);
        EasyMock.expect(edge0to2.checkOwningColor()).andStubReturn(PlayerColor.RED);
        EasyMock.replay(nodeStub0, nodeStub1, edge0to1, edge0to2);

        b.addGraphNodeObject(nodeStub0);
        b.addGraphNodeObject(nodeStub1);
        b.addGraphNodeConnection(0, edge0to1);
        b.addGraphNodeConnection(0, edge0to2);
        b.addGraphNodeConnection(1, edge0to1);

        assertTrue(b.checkPlayerOwnsNeighboringEdge(PlayerColor.RED, 0, 1));

    }

    @Test
    void checkPlayerOwnsNeighboringEdges_test02_WhiteOwnsEdgeConnectingToEndingNode_ExpectTrue(){
        BoardGraph b = new BoardGraph();

        GraphNode nodeStub0 = EasyMock.createNiceMock(GraphNode.class);
        GraphNode nodeStub1 = EasyMock.createNiceMock(GraphNode.class);
        GraphEdge edge0to1 = EasyMock.createNiceMock(GraphEdge.class);
        // edge which red owns
        GraphEdge edge1to2 = EasyMock.createNiceMock(GraphEdge.class);

        EasyMock.expect(nodeStub0.getNodeID()).andStubReturn(0);
        EasyMock.expect(nodeStub1.getNodeID()).andStubReturn(1);

        EasyMock.expect(edge0to1.getStartingNodeID()).andStubReturn(0);
        EasyMock.expect(edge0to1.getEndingNodeID()).andStubReturn(1);
        EasyMock.expect(edge0to1.checkOwningColor()).andStubReturn(PlayerColor.SETUP);

        EasyMock.expect(edge1to2.getStartingNodeID()).andStubReturn(1);
        EasyMock.expect(edge1to2.getEndingNodeID()).andStubReturn(2);
        EasyMock.expect(edge1to2.checkOwningColor()).andStubReturn(PlayerColor.WHITE);
        EasyMock.replay(nodeStub0, nodeStub1, edge0to1, edge1to2);

        b.addGraphNodeObject(nodeStub0);
        b.addGraphNodeObject(nodeStub1);
        b.addGraphNodeConnection(0, edge0to1);
        b.addGraphNodeConnection(0, edge1to2);
        b.addGraphNodeConnection(1, edge0to1);

        assertTrue(b.checkPlayerOwnsNeighboringEdge(PlayerColor.WHITE, 0, 1));

    }

    @Test
    void checkPlayerOwnsNeighboringEdges_test03_BlueOwnsNoConnectingEdges_ExpectFalse(){
        BoardGraph b = new BoardGraph();

        GraphNode nodeStub52 = EasyMock.createNiceMock(GraphNode.class);
        GraphNode nodeStub53 = EasyMock.createNiceMock(GraphNode.class);
        GraphEdge edge52to53 = EasyMock.createNiceMock(GraphEdge.class);

        GraphEdge edge51to53 = EasyMock.createNiceMock(GraphEdge.class);
        GraphEdge edge51to52 = EasyMock.createNiceMock(GraphEdge.class);

        EasyMock.expect(nodeStub52.getNodeID()).andStubReturn(52);
        EasyMock.expect(nodeStub53.getNodeID()).andStubReturn(53);

        EasyMock.expect(edge52to53.getStartingNodeID()).andStubReturn(52);
        EasyMock.expect(edge52to53.getEndingNodeID()).andStubReturn(53);
        EasyMock.expect(edge52to53.checkOwningColor()).andStubReturn(PlayerColor.SETUP);

        EasyMock.expect(edge51to53.getStartingNodeID()).andStubReturn(51);
        EasyMock.expect(edge51to53.getEndingNodeID()).andStubReturn(53);
        EasyMock.expect(edge51to53.checkOwningColor()).andStubReturn(PlayerColor.WHITE);

        EasyMock.expect(edge51to52.getStartingNodeID()).andStubReturn(51);
        EasyMock.expect(edge51to52.getEndingNodeID()).andStubReturn(52);
        EasyMock.expect(edge51to52.checkOwningColor()).andStubReturn(PlayerColor.WHITE);

        EasyMock.replay(nodeStub52, nodeStub53, edge51to52, edge51to53, edge52to53);

        b.addGraphNodeObject(nodeStub52);
        b.addGraphNodeObject(nodeStub53);
        b.addGraphNodeConnection(52, edge52to53);
        b.addGraphNodeConnection(52, edge51to52);
        b.addGraphNodeConnection(53, edge52to53);
        b.addGraphNodeConnection(53, edge51to53);

        assertFalse(b.checkPlayerOwnsNeighboringEdge(PlayerColor.BLUE, 52, 53));

    }
    @Test
    void checkPlayerOwnsNeighboringEdges_test04_OrangeConnectingEdgesToStartAndEnd_ExpectTrue(){
        BoardGraph b = new BoardGraph();

        GraphNode nodeStub52 = EasyMock.createNiceMock(GraphNode.class);
        GraphNode nodeStub53 = EasyMock.createNiceMock(GraphNode.class);
        GraphEdge edge52to53 = EasyMock.createNiceMock(GraphEdge.class);

        GraphEdge edge51to53 = EasyMock.createNiceMock(GraphEdge.class);
        GraphEdge edge51to52 = EasyMock.createNiceMock(GraphEdge.class);

        EasyMock.expect(nodeStub52.getNodeID()).andStubReturn(52);
        EasyMock.expect(nodeStub53.getNodeID()).andStubReturn(53);

        EasyMock.expect(edge52to53.getStartingNodeID()).andStubReturn(52);
        EasyMock.expect(edge52to53.getEndingNodeID()).andStubReturn(53);
        EasyMock.expect(edge52to53.checkOwningColor()).andStubReturn(PlayerColor.SETUP);

        EasyMock.expect(edge51to53.getStartingNodeID()).andStubReturn(51);
        EasyMock.expect(edge51to53.getEndingNodeID()).andStubReturn(53);
        EasyMock.expect(edge51to53.checkOwningColor()).andStubReturn(PlayerColor.ORANGE);

        EasyMock.expect(edge51to52.getStartingNodeID()).andStubReturn(51);
        EasyMock.expect(edge51to52.getEndingNodeID()).andStubReturn(52);
        EasyMock.expect(edge51to52.checkOwningColor()).andStubReturn(PlayerColor.ORANGE);

        EasyMock.replay(nodeStub52, nodeStub53, edge51to52, edge51to53, edge52to53);

        b.addGraphNodeObject(nodeStub52);
        b.addGraphNodeObject(nodeStub53);
        b.addGraphNodeConnection(52, edge52to53);
        b.addGraphNodeConnection(52, edge51to52);
        b.addGraphNodeConnection(53, edge52to53);
        b.addGraphNodeConnection(53, edge51to53);

        assertTrue(b.checkPlayerOwnsNeighboringEdge(PlayerColor.ORANGE, 52, 53));

    }

    // checkPlayerOwnsNeighboringNodes() tests

    @Test
    void checkPlayerOwnsNeighboringNodes_test01_RedOwnsStartingNode_ExpectTrue() {
        BoardGraph b = new BoardGraph();

        GraphNode nodeStub0 = EasyMock.createNiceMock(GraphNode.class);
        GraphNode nodeStub1 = EasyMock.createNiceMock(GraphNode.class);

        EasyMock.expect(nodeStub0.getNodeID()).andStubReturn(0);
        EasyMock.expect(nodeStub0.checkColor()).andStubReturn(PlayerColor.RED);
        EasyMock.expect(nodeStub1.getNodeID()).andStubReturn(1);
        EasyMock.expect(nodeStub1.checkColor()).andStubReturn(PlayerColor.ORANGE);

        EasyMock.replay(nodeStub0, nodeStub1);

        b.addGraphNodeObject(nodeStub0);
        b.addGraphNodeObject(nodeStub1);

        assertTrue(b.checkPlayerOwnsNeighboringNode(PlayerColor.RED, 0, 1));
    }

    @Test
    void checkPlayerOwnsNeighboringNodes_test02_WhiteOwnsEndingNode_ExpectTrue() {
        BoardGraph b = new BoardGraph();

        GraphNode nodeStub0 = EasyMock.createNiceMock(GraphNode.class);
        GraphNode nodeStub1 = EasyMock.createNiceMock(GraphNode.class);

        EasyMock.expect(nodeStub0.getNodeID()).andStubReturn(0);
        EasyMock.expect(nodeStub0.checkColor()).andStubReturn(PlayerColor.RED);
        EasyMock.expect(nodeStub1.getNodeID()).andStubReturn(1);
        EasyMock.expect(nodeStub1.checkColor()).andStubReturn(PlayerColor.WHITE);

        EasyMock.replay(nodeStub0, nodeStub1);

        b.addGraphNodeObject(nodeStub0);
        b.addGraphNodeObject(nodeStub1);

        assertTrue(b.checkPlayerOwnsNeighboringNode(PlayerColor.WHITE, 0, 1));
    }

    @Test
    void checkPlayerOwnsNeighboringNodes_test03_BlueOwnsNoNode_ExpectFalse() {
        BoardGraph b = new BoardGraph();

        GraphNode nodeStub52 = EasyMock.createNiceMock(GraphNode.class);
        GraphNode nodeStub53 = EasyMock.createNiceMock(GraphNode.class);

        EasyMock.expect(nodeStub52.getNodeID()).andStubReturn(52);
        EasyMock.expect(nodeStub52.checkColor()).andStubReturn(PlayerColor.RED);
        EasyMock.expect(nodeStub53.getNodeID()).andStubReturn(53);
        EasyMock.expect(nodeStub53.checkColor()).andStubReturn(PlayerColor.WHITE);

        EasyMock.replay(nodeStub52, nodeStub53);

        b.addGraphNodeObject(nodeStub52);
        b.addGraphNodeObject(nodeStub53);

        assertFalse(b.checkPlayerOwnsNeighboringNode(PlayerColor.BLUE, 52, 53));
    }

    @Test
    void checkPlayerOwnsNeighboringNodes_test04_OrangeOwnsBothNodes_ExpectTrue() {
        BoardGraph b = new BoardGraph();

        GraphNode nodeStub52 = EasyMock.createNiceMock(GraphNode.class);
        GraphNode nodeStub53 = EasyMock.createNiceMock(GraphNode.class);

        EasyMock.expect(nodeStub52.getNodeID()).andStubReturn(52);
        EasyMock.expect(nodeStub52.checkColor()).andStubReturn(PlayerColor.ORANGE);
        EasyMock.expect(nodeStub53.getNodeID()).andStubReturn(53);
        EasyMock.expect(nodeStub53.checkColor()).andStubReturn(PlayerColor.ORANGE);

        EasyMock.replay(nodeStub52, nodeStub53);

        b.addGraphNodeObject(nodeStub52);
        b.addGraphNodeObject(nodeStub53);

        assertTrue(b.checkPlayerOwnsNeighboringNode(PlayerColor.ORANGE, 52, 53));
    }

    // checkAdjacentClaimedNodes() tests

    @Test
    void checkAdjacentClaimedNodes_test01_NoAdjacentNodesClaimed_ExpectTrue(){
        BoardGraph b = new BoardGraph();
        GraphNode nodeStub0 = EasyMock.createNiceMock(GraphNode.class);
        GraphNode nodeStub3 = EasyMock.createNiceMock(GraphNode.class);
        GraphNode nodeStub4 = EasyMock.createNiceMock(GraphNode.class);

        GraphEdge edge0to3 = EasyMock.createNiceMock(GraphEdge.class);
        GraphEdge edge0to4 = EasyMock.createNiceMock(GraphEdge.class);

        EasyMock.expect(nodeStub0.getNodeID()).andStubReturn(0);
        EasyMock.expect(nodeStub3.getNodeID()).andStubReturn(3);
        EasyMock.expect(nodeStub4.getNodeID()).andStubReturn(4);

        EasyMock.expect(edge0to3.getStartingNodeID()).andStubReturn(0);
        EasyMock.expect(edge0to3.getEndingNodeID()).andStubReturn(3);
        EasyMock.expect(edge0to3.checkOwningColor()).andStubReturn(PlayerColor.SETUP);
        EasyMock.expect(edge0to4.getStartingNodeID()).andStubReturn(0);
        EasyMock.expect(edge0to4.getEndingNodeID()).andStubReturn(4);
        EasyMock.expect(edge0to4.checkOwningColor()).andStubReturn(PlayerColor.SETUP);

        EasyMock.replay(nodeStub0, nodeStub3, nodeStub4, edge0to3, edge0to4);
        b.addGraphNodeObject(nodeStub0);
        b.addGraphNodeObject(nodeStub3);
        b.addGraphNodeObject(nodeStub4);
        b.addGraphNodeConnection(0, edge0to3);
        b.addGraphNodeConnection(0, edge0to4);

        assertTrue(b.checkAdjacentClaimedNodes(0));
    }

    @Test
    void checkAdjacentClaimedNodes_test02_EndingAdjacentNodesClaimed_ExpectError(){
        BoardGraph b = new BoardGraph();
        GraphNode nodeStub0 = EasyMock.createNiceMock(GraphNode.class);
        GraphNode nodeStub3 = EasyMock.createNiceMock(GraphNode.class);
        GraphNode nodeStub4 = EasyMock.createNiceMock(GraphNode.class);

        GraphEdge edge0to3 = EasyMock.createNiceMock(GraphEdge.class);
        GraphEdge edge0to4 = EasyMock.createNiceMock(GraphEdge.class);

        EasyMock.expect(nodeStub0.getNodeID()).andStubReturn(0);
        EasyMock.expect(nodeStub3.getNodeID()).andStubReturn(3);
        EasyMock.expect(nodeStub3.checkOccupied()).andStubReturn(true);
        EasyMock.expect(nodeStub4.getNodeID()).andStubReturn(4);

        EasyMock.expect(edge0to3.getStartingNodeID()).andStubReturn(0);
        EasyMock.expect(edge0to3.getEndingNodeID()).andStubReturn(3);
        EasyMock.expect(edge0to3.checkOwningColor()).andStubReturn(PlayerColor.SETUP);
        EasyMock.expect(edge0to4.getStartingNodeID()).andStubReturn(0);
        EasyMock.expect(edge0to4.getEndingNodeID()).andStubReturn(4);
        EasyMock.expect(edge0to4.checkOwningColor()).andStubReturn(PlayerColor.SETUP);

        EasyMock.replay(nodeStub0, nodeStub3, nodeStub4, edge0to3, edge0to4);
        b.addGraphNodeObject(nodeStub0);
        b.addGraphNodeObject(nodeStub3);
        b.addGraphNodeObject(nodeStub4);
        b.addGraphNodeConnection(0, edge0to3);
        b.addGraphNodeConnection(0, edge0to4);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> b.checkAdjacentClaimedNodes(0));

        assertEquals("Can not claim node adjacent to node already claimed", exception.getMessage());
    }

    @Test
    void checkAdjacentClaimedNodes_test03_StartingAdjacentNodesClaimed_ExpectError(){
        BoardGraph b = new BoardGraph();
        GraphNode nodeStub49 = EasyMock.createNiceMock(GraphNode.class);
        GraphNode nodeStub50 = EasyMock.createNiceMock(GraphNode.class);
        GraphNode nodeStub53 = EasyMock.createNiceMock(GraphNode.class);

        GraphEdge edge49to53 = EasyMock.createNiceMock(GraphEdge.class);
        GraphEdge edge50to53 = EasyMock.createNiceMock(GraphEdge.class);

        EasyMock.expect(nodeStub49.getNodeID()).andStubReturn(49);
        EasyMock.expect(nodeStub50.getNodeID()).andStubReturn(50);
        EasyMock.expect(nodeStub50.checkOccupied()).andStubReturn(true);
        EasyMock.expect(nodeStub53.getNodeID()).andStubReturn(53);

        EasyMock.expect(edge49to53.getStartingNodeID()).andStubReturn(49);
        EasyMock.expect(edge49to53.getEndingNodeID()).andStubReturn(53);
        EasyMock.expect(edge49to53.checkOwningColor()).andStubReturn(PlayerColor.SETUP);
        EasyMock.expect(edge50to53.getStartingNodeID()).andStubReturn(50);
        EasyMock.expect(edge50to53.getEndingNodeID()).andStubReturn(53);
        EasyMock.expect(edge50to53.checkOwningColor()).andStubReturn(PlayerColor.SETUP);

        EasyMock.replay(nodeStub49, nodeStub50, nodeStub53, edge49to53, edge50to53);
        b.addGraphNodeObject(nodeStub49);
        b.addGraphNodeObject(nodeStub50);
        b.addGraphNodeObject(nodeStub53);
        b.addGraphNodeConnection(53, edge49to53);
        b.addGraphNodeConnection(53, edge50to53);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> b.checkAdjacentClaimedNodes(53));

        assertEquals("Can not claim node adjacent to node already claimed", exception.getMessage());
    }

    @Test
    void checkAdjacentClaimedNodes_test04_BothStartingAndEndingNodesClaimed_ExpectError(){
        BoardGraph b = new BoardGraph();
        GraphNode nodeStub45 = EasyMock.createNiceMock(GraphNode.class);
        GraphNode nodeStub49 = EasyMock.createNiceMock(GraphNode.class);
        GraphNode nodeStub52 = EasyMock.createNiceMock(GraphNode.class);
        GraphNode nodeStub53 = EasyMock.createNiceMock(GraphNode.class);

        GraphEdge edge45to49 = EasyMock.createNiceMock(GraphEdge.class);
        GraphEdge edge49to52 = EasyMock.createNiceMock(GraphEdge.class);
        GraphEdge edge49to53 = EasyMock.createNiceMock(GraphEdge.class);

        EasyMock.expect(nodeStub45.getNodeID()).andStubReturn(45);
        EasyMock.expect(nodeStub49.checkOccupied()).andStubReturn(true);
        EasyMock.expect(nodeStub49.getNodeID()).andStubReturn(49);
        EasyMock.expect(nodeStub52.getNodeID()).andStubReturn(52);
        EasyMock.expect(nodeStub52.checkOccupied()).andStubReturn(true);
        EasyMock.expect(nodeStub53.getNodeID()).andStubReturn(53);

        EasyMock.expect(edge45to49.getStartingNodeID()).andStubReturn(45);
        EasyMock.expect(edge45to49.getEndingNodeID()).andStubReturn(49);
        EasyMock.expect(edge45to49.checkOwningColor()).andStubReturn(PlayerColor.SETUP);
        EasyMock.expect(edge49to52.getStartingNodeID()).andStubReturn(49);
        EasyMock.expect(edge49to52.getEndingNodeID()).andStubReturn(52);
        EasyMock.expect(edge49to52.checkOwningColor()).andStubReturn(PlayerColor.SETUP);
        EasyMock.expect(edge49to53.getStartingNodeID()).andStubReturn(49);
        EasyMock.expect(edge49to53.getEndingNodeID()).andStubReturn(53);
        EasyMock.expect(edge49to53.checkOwningColor()).andStubReturn(PlayerColor.SETUP);

        EasyMock.replay(nodeStub45, nodeStub49, nodeStub52, edge45to49, edge49to52, edge49to53);
        b.addGraphNodeObject(nodeStub45);
        b.addGraphNodeObject(nodeStub49);
        b.addGraphNodeObject(nodeStub52);
        b.addGraphNodeObject(nodeStub53);
        b.addGraphNodeConnection(49, edge45to49);
        b.addGraphNodeConnection(49, edge49to52);
        b.addGraphNodeConnection(49, edge49to53);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> b.checkAdjacentClaimedNodes(49));

        assertEquals("Can not claim node adjacent to node already claimed", exception.getMessage());
    }


    // buildBoard() test
    @Test
    void buildBoard_test01_ExpectCompletedBoard() {
        BoardGraph b = new BoardGraph();
        b.buildBoard();
        assertEquals(54, b.checkAmountOfNodesForTesting());
        assertEquals(54, b.checkAmountOfNodesInEdgeMapForTesting());
    }

}
