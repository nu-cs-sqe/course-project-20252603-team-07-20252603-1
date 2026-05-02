package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

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

}
