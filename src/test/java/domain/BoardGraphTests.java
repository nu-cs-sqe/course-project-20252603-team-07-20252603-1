package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoardGraphTests {
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

    @Test
    void addNewEdge_NotDuplicate_NodeExistsInMap_ExpectTrue() {
        BoardGraph b = new BoardGraph();

        GraphNode nodeStub = EasyMock.createMock(GraphNode.class);
        GraphEdge edgeStub = EasyMock.createMock(GraphEdge.class);

        EasyMock.expect(nodeStub.getNodeID()).andStubReturn(0);
        EasyMock.replay(nodeStub, edgeStub);

        b.addGraphNodeObject(nodeStub);

        assertTrue(b.addGraphNodeConnection(0, edgeStub));

        assertTrue(b.getConnectingEdgesByID(0).contains(edgeStub));
    }

}
