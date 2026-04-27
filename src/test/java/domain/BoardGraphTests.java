package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BoardGraphTests {
    @Test
    void addNodeToGraph_EmptyGraph_ExpectTrue(){
        BoardGraph b = new BoardGraph();
        GraphNode nodeMock = EasyMock.createMock(GraphNode.class);
        EasyMock.expect(nodeMock.getNodeID()).andReturn(0);
        EasyMock.replay(nodeMock);

        assertTrue(b.addGraphNodeObject(nodeMock));
        assertNotNull(b.getGraphNodeByID(0));
        EasyMock.verify();
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
        EasyMock.verify();
    }
}
