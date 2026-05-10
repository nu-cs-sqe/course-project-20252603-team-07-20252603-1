package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class BoardGraphTests {

    // addGraphNodeObj() Tests
    @Test
    void addNodeToGraph_EmptyGraph_ExpectTrue(){
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
    void addNodeToGraph_OneElementGraph_ExpectTrue(){
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
    void addNodeToGraph_MultipleElementGraph_ExpectTrue(){
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
    void addDuplicateNodeToGraph_ExpectError() {
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
    void getNodeID0_EmptyMap_ExpectError(){
        BoardGraph b = new BoardGraph();

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> b.getGraphNodeByID(0));

        assertEquals("Node does not exist", exception.getMessage());

    }

    @Test
    void getNodeID0_OneElementMap_ID0Exists_ExpectGraphNode(){
        BoardGraph b = new BoardGraph();

        GraphNode nodeStub = EasyMock.createMock(GraphNode.class);
        EasyMock.expect(nodeStub.getNodeID()).andStubReturn(0);

        b.addGraphNodeObject(nodeStub);

        GraphNode result = b.getGraphNodeByID(0);

        assertNotNull(result);
        assertEquals(nodeStub, result);

    }

    @Test
    void getNodeID53_MultipleElementMap_ID53DoesNotExists_ExpectError(){
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
    void test01_addNewEdge_NotDuplicate_NodeExistsInMap_ExpectTrue() {
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
    void test02_addNewEdge_Duplicate_NodeExistsInMap_ExpectError() {
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
    void test03_addNewEdge_Duplicate_SeparateExistingNode_ExpectTrue() {
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
    void test04_addNewEdge_NodeDoesNotExist_ExpectError() {
        BoardGraph b = new BoardGraph();
        GraphEdge edgeStub = EasyMock.createMock(GraphEdge.class);

        EasyMock.replay(edgeStub);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> b.addGraphNodeConnection(0, edgeStub));

        assertEquals("Node does not exist", exception.getMessage());

    }

    // getConnectingEdgesByID Tests
    @Test
    void test01_getEdgeSet_NodeDoesNotExist_ExpectError() {
        BoardGraph b = new BoardGraph();

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> b.getConnectingEdgesByID(0));

        assertEquals("Node does not exist", exception.getMessage());
    }

    @Test
    void test02_getEdgeSet_OneNodeExists_ExpectEmptySet() {
        BoardGraph b = new BoardGraph();
        GraphNode nodeStub = EasyMock.createMock(GraphNode.class);

        EasyMock.expect(nodeStub.getNodeID()).andStubReturn(0);

        EasyMock.replay(nodeStub);

        b.addGraphNodeObject(nodeStub);

        assertNotNull(b.getConnectingEdgesByID(0));
        assertEquals(0, b.getConnectingEdgesByID(0).size());
    }

    @Test
    void test03_getEdgeSet_MultipleNodesExist_ExpectOneEdgeSet() {
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
    void test04_getEdgeSet_MultipleNodesExist_ExpectMultipleEdgeSet() {
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

    @Test
    void playerClaimStoredEdge_test01_PlayerOwnsNeighboringNode_EdgeUnclaimed_ExpectTrue(){
        BoardGraph b = new BoardGraph();

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

        EasyMock.replay(nodeStub0, nodeStub1, edgeMock0to1);

        b.addGraphNodeObject(nodeStub0);
        b.addGraphNodeObject(nodeStub1);
        b.addGraphNodeConnection(0, edgeMock0to1);
        b.addGraphNodeConnection(1, edgeMock0to1);

        assertTrue(b.playerClaimStoredEdge(PlayerColor.RED, 0, 1));
        EasyMock.verify(edgeMock0to1);
    }

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
}
