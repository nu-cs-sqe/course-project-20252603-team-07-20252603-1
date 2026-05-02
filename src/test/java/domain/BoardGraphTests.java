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

        EasyMock.verify(nodeMock1, nodeMock2);

    }

    @Test
    void getNodeID0_EmptyMap_ExpectError(){
        BoardGraph b = new BoardGraph();

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> b.getGraphNodeByID(0));

        assertEquals("Node does not exist", exception.getMessage());

    }

}
