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

    @Test
    void claimGraphNodeObject_test01_NodeExists_Unclaimed_ExpectTrue(){
        BoardGraph b = new BoardGraph();

        GraphNode nodeMock = EasyMock.createMock(GraphNode.class);
        EasyMock.expect(nodeMock.getNodeID()).andReturn(0);
        EasyMock.expect(nodeMock.playerClaimNode(PlayerColor.RED)).andReturn(true);
        EasyMock.replay(nodeMock);

        b.addGraphNodeObject(nodeMock);

        assertTrue(b.claimGraphNodeObject(PlayerColor.RED, 0));
        EasyMock.verify(nodeMock);
    }

    @Test
    void claimGraphNodeObject_test02_MultipleNodeExists_NodeUnclaimed_ExpectTrue(){
        BoardGraph b = new BoardGraph();

        GraphNode nodeMock = EasyMock.createMock(GraphNode.class);
        GraphNode nodeStub2 = EasyMock.createMock(GraphNode.class);
        GraphNode nodeStub3 = EasyMock.createMock(GraphNode.class);
        EasyMock.expect(nodeMock.getNodeID()).andReturn(0);
        EasyMock.expect(nodeMock.playerClaimNode(PlayerColor.ORANGE)).andReturn(true);
        EasyMock.expect(nodeStub2.getNodeID()).andStubReturn(2);
        EasyMock.expect(nodeStub3.getNodeID()).andStubReturn(3);
        EasyMock.replay(nodeMock, nodeStub2, nodeStub3);

        b.addGraphNodeObject(nodeMock);
        b.addGraphNodeObject(nodeStub2);
        b.addGraphNodeObject(nodeStub3);

        assertTrue(b.claimGraphNodeObject(PlayerColor.ORANGE, 0));
        EasyMock.verify(nodeMock);
    }

    @Test
    void claimGraphNodeObject_test03_NodeDoesNotExist_ExpectError(){
        BoardGraph b = new BoardGraph();

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> b.claimGraphNodeObject(PlayerColor.BLUE, 53));

        assertEquals("Node does not exist", exception.getMessage());
    }

    @Test
    void claimGraphNodeObject_test04_NodeDoesExists_AlreadyClaimed_ExpectError(){
        BoardGraph b = new BoardGraph();

        GraphNode nodeMock = EasyMock.createMock(GraphNode.class);
        EasyMock.expect(nodeMock.getNodeID()).andReturn(53);
        EasyMock.expect(nodeMock.playerClaimNode(PlayerColor.WHITE)).andThrow(new IllegalArgumentException("Node already claimed"));
        EasyMock.replay(nodeMock);

        b.addGraphNodeObject(nodeMock);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> b.claimGraphNodeObject(PlayerColor.WHITE, 53));

        assertEquals("Node already claimed", exception.getMessage());

        EasyMock.verify(nodeMock);
    }
    // TODO playerClaimStoredEdge() tests

    @Test
    void playerClaimEdgeObject_test01_EdgeUnclaimed_SingleItemCollection_ExpectTrue(){
        BoardGraph b = new BoardGraph();
        GraphNode nodeStub = EasyMock.createMock(GraphNode.class);
        GraphEdge edge0to1 = EasyMock.createMock(GraphEdge.class);
        EasyMock.expect(nodeStub.getNodeID()).andReturn(0);
        EasyMock.expect(edge0to1.getStartingNodeID()).andReturn(0);
        EasyMock.expect(edge0to1.getEndingNodeID()).andReturn(1);
        EasyMock.expect(edge0to1.claimGraphEdge(PlayerColor.RED)).andReturn(true);
        EasyMock.replay(nodeStub, edge0to1);
        b.addGraphNodeObject(nodeStub);
        b.addGraphNodeConnection(0, edge0to1);

        assertTrue(b.claimGraphEdgeObject(PlayerColor.RED, 0, 1));
        EasyMock.verify(edge0to1);
    }

    @Test
    void playerClaimEdgeObject_test02_EdgeUnclaimed_MultipleItemCollection_ExpectTrue(){
        BoardGraph b = new BoardGraph();
        GraphNode nodeStub = EasyMock.createNiceMock(GraphNode.class);
        GraphEdge edge0to1 = EasyMock.createMock(GraphEdge.class);
        GraphEdge edge0to2 = EasyMock.createNiceMock(GraphEdge.class);

        EasyMock.expect(nodeStub.getNodeID()).andReturn(0);
        EasyMock.expect(edge0to2.getStartingNodeID()).andReturn(0);
        EasyMock.expect(edge0to2.getEndingNodeID()).andReturn(2);
        EasyMock.expect(edge0to1.getStartingNodeID()).andReturn(0);
        EasyMock.expect(edge0to1.getEndingNodeID()).andReturn(1);
        EasyMock.expect(edge0to1.claimGraphEdge(PlayerColor.RED)).andReturn(true);
        EasyMock.replay(nodeStub, edge0to1);
        b.addGraphNodeObject(nodeStub);
        b.addGraphNodeConnection(0, edge0to1);
        b.addGraphNodeConnection(0, edge0to2);

        assertTrue(b.claimGraphEdgeObject(PlayerColor.RED, 0, 1));
        EasyMock.verify(edge0to1);
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
    void addNewEdge_test02_Duplicate_NodeExistsInMap_ExpectError() {
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
    void edgeCheckPlayerOwnsNeighboringEdges_test01_RedOwnsEdgeConnectingToStartingNode_ExpectTrue(){
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

        assertTrue(b.edgeCheckPlayerOwnsNeighboringEdge(PlayerColor.RED, 0, 1));

    }

    @Test
    void edgeCheckPlayerOwnsNeighboringEdges_test02_WhiteOwnsEdgeConnectingToEndingNode_ExpectTrue(){
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

        assertTrue(b.edgeCheckPlayerOwnsNeighboringEdge(PlayerColor.WHITE, 0, 1));

    }

    @Test
    void edgeCheckPlayerOwnsNeighboringEdges_test03_BlueOwnsNoConnectingEdges_ExpectFalse(){
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

        assertFalse(b.edgeCheckPlayerOwnsNeighboringEdge(PlayerColor.BLUE, 52, 53));

    }
    @Test
    void edgeCheckPlayerOwnsNeighboringEdges_test04_OrangeConnectingEdgesToStartAndEnd_ExpectTrue(){
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

        assertTrue(b.edgeCheckPlayerOwnsNeighboringEdge(PlayerColor.ORANGE, 52, 53));

    }

    // checkPlayerOwnsNeighboringNodes() tests

    @Test
    void edgeCheckPlayerOwnsNeighboringNodes_test01_RedOwnsStartingNode_ExpectTrue() {
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

        assertTrue(b.edgeCheckPlayerOwnsNeighboringNode(PlayerColor.RED, 0, 1));
    }

    @Test
    void edgeCheckPlayerOwnsNeighboringNodes_test02_WhiteOwnsEndingNode_ExpectTrue() {
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

        assertTrue(b.edgeCheckPlayerOwnsNeighboringNode(PlayerColor.WHITE, 0, 1));
    }

    @Test
    void edgeCheckPlayerOwnsNeighboringNodes_test03_BlueOwnsNoNode_ExpectFalse() {
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

        assertFalse(b.edgeCheckPlayerOwnsNeighboringNode(PlayerColor.BLUE, 52, 53));
    }

    @Test
    void edgeCheckPlayerOwnsNeighboringNodes_test04_OrangeOwnsBothNodes_ExpectTrue() {
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

        assertTrue(b.edgeCheckPlayerOwnsNeighboringNode(PlayerColor.ORANGE, 52, 53));
    }

    // checkIfAdjacentNodesNotClaimed() tests

    @Test
    void checkAdjacentClaimedNodes_test01_NoIfAdjacentNodes_ExpectTrue(){
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

        assertTrue(b.checkIfAdjacentNodesNotClaimed(0));
    }

    @Test
    void checkAdjacentClaimedNodes_test02_EndingIfAdjacentNodes_ExpectFalse(){
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


         assertFalse(b.checkIfAdjacentNodesNotClaimed(0));

    }

    @Test
    void checkAdjacentClaimedNodes_test03_StartingIfAdjacentNodes_ExpectFalse(){
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

        assertFalse(b.checkIfAdjacentNodesNotClaimed(53));
    }

    @Test
    void checkIfAdjacentClaimedNodes_test04_BothStartingAndEndingNodes_ExpectFalse(){
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

        EasyMock.replay(nodeStub45, nodeStub49, nodeStub52, nodeStub53, edge45to49, edge49to52, edge49to53);
        b.addGraphNodeObject(nodeStub45);
        b.addGraphNodeObject(nodeStub49);
        b.addGraphNodeObject(nodeStub52);
        b.addGraphNodeObject(nodeStub53);
        b.addGraphNodeConnection(49, edge45to49);
        b.addGraphNodeConnection(49, edge49to52);
        b.addGraphNodeConnection(49, edge49to53);

        assertFalse(b.checkIfAdjacentNodesNotClaimed(49));

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
