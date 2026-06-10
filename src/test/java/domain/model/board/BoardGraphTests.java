package domain.model.board;

import domain.model.exceptions.EdgeAlreadyClaimedException;
import domain.model.board.BoardGraph;
import domain.model.board.GraphEdge;
import domain.model.board.GraphNode;
import domain.model.player.Player;
import domain.model.player.PlayerColor;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class BoardGraphTests {

  // addGraphNodeObj() Tests
  @Test
  void addNodeToGraph_test01_EmptyGraph_ExpectTrue() {
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
  void addNodeToGraph_test02_OneElementGraph_ExpectTrue() {
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
  void addNodeToGraph_test03_MultipleElementGraph_ExpectTrue() {
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
  void getNodeID0_test01_EmptyMap_ExpectError() {
    BoardGraph b = new BoardGraph();

    Exception exception = assertThrows(IllegalArgumentException.class,
            () -> b.getGraphNodeByID(0));

    assertEquals("Node does not exist", exception.getMessage());

  }

  @Test
  void getNodeID0_test02_OneElementMap_ID0Exists_ExpectGraphNode() {
    BoardGraph b = new BoardGraph();

    GraphNode nodeStub = EasyMock.createMock(GraphNode.class);
    EasyMock.expect(nodeStub.getNodeID()).andStubReturn(0);

    b.addGraphNodeObject(nodeStub);

    GraphNode result = b.getGraphNodeByID(0);

    assertNotNull(result);
    assertEquals(nodeStub, result);

  }

  @Test
  void getNodeID53_test03_MultipleElementMap_ID53DoesNotExists_ExpectError() {
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
  void checkPlayerOwnsGraphNodeObject_test01_NodeExists_PlayerOwnsIt_ExpectTrue() {
    BoardGraph b = new BoardGraph();
    GraphNode nodeMock = EasyMock.createMock(GraphNode.class);
    EasyMock.expect(nodeMock.getNodeID()).andReturn(0);
    EasyMock.expect(nodeMock.checkColor()).andReturn(PlayerColor.RED);
    EasyMock.replay(nodeMock);

    b.addGraphNodeObject(nodeMock);

    assertTrue(b.checkPlayerOwnsGraphNodeObject(PlayerColor.RED, 0));
  }

  @Test
  void checkPlayerOwnsGraphNodeObject_test02_NodeExists_PlayerDoesNotOwnsIt_ExpectFalse() {
    BoardGraph b = new BoardGraph();
    GraphNode nodeMock = EasyMock.createMock(GraphNode.class);
    EasyMock.expect(nodeMock.getNodeID()).andReturn(0);
    EasyMock.expect(nodeMock.checkColor()).andReturn(PlayerColor.WHITE);
    EasyMock.replay(nodeMock);

    b.addGraphNodeObject(nodeMock);

    assertFalse(b.checkPlayerOwnsGraphNodeObject(PlayerColor.ORANGE, 0));
  }

  @Test
  void checkPlayerOwnsGraphNodeObject_test03_NodeDoesNotExist_ExpectError() {
    BoardGraph b = new BoardGraph();
    Exception exception = assertThrows(IllegalArgumentException.class,
            () -> b.checkPlayerOwnsGraphNodeObject(PlayerColor.BLUE, 0));
    assertEquals("Node does not exist", exception.getMessage());
  }

  @Test
  void checkPlayerOwnsGraphNodeObject_test04_NodeExists_DifferentColor_ExpectFalse() {
    BoardGraph b = new BoardGraph();
    GraphNode nodeMock = EasyMock.createMock(GraphNode.class);
    EasyMock.expect(nodeMock.getNodeID()).andReturn(0);
    EasyMock.expect(nodeMock.checkColor()).andReturn(PlayerColor.BLUE);
    EasyMock.replay(nodeMock);

    b.addGraphNodeObject(nodeMock);

    assertFalse(b.checkPlayerOwnsGraphNodeObject(PlayerColor.WHITE, 0));
  }

  @Test
  void claimGraphNodeObject_test01_NodeExists_Unclaimed_ExpectTrue() {
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
  void claimGraphNodeObject_test02_MultipleNodeExists_NodeUnclaimed_ExpectTrue() {
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
  void claimGraphNodeObject_test03_NodeDoesNotExist_ExpectError() {
    BoardGraph b = new BoardGraph();

    Exception exception = assertThrows(IllegalArgumentException.class,
            () -> b.claimGraphNodeObject(PlayerColor.BLUE, 53));

    assertEquals("Node does not exist", exception.getMessage());
  }

  @Test
  void claimGraphNodeObject_test04_NodeDoesExists_AlreadyClaimed_ExpectError() {
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
  void playerClaimEdgeObject_test01_EdgeUnclaimed_SingleItemCollection_ExpectTrue() {
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
  void playerClaimEdgeObject_test02_EdgeUnclaimed_MultipleItemCollection_ExpectTrue() {
    BoardGraph b = new BoardGraph();
    GraphNode nodeStub = EasyMock.createNiceMock(GraphNode.class);
    GraphEdge edge0to1 = EasyMock.createMock(GraphEdge.class);
    GraphEdge edge0to2 = EasyMock.createNiceMock(GraphEdge.class);

    EasyMock.expect(nodeStub.getNodeID()).andReturn(0);
    EasyMock.expect(edge0to2.getStartingNodeID()).andReturn(0);
    EasyMock.expect(edge0to2.getEndingNodeID()).andReturn(2);
    EasyMock.expect(edge0to1.getStartingNodeID()).andReturn(0);
    EasyMock.expect(edge0to1.getEndingNodeID()).andReturn(1);
    EasyMock.expect(edge0to1.claimGraphEdge(PlayerColor.BLUE)).andReturn(true);
    EasyMock.replay(nodeStub, edge0to1, edge0to2);
    b.addGraphNodeObject(nodeStub);
    b.addGraphNodeConnection(0, edge0to1);
    b.addGraphNodeConnection(0, edge0to2);

    assertTrue(b.claimGraphEdgeObject(PlayerColor.BLUE, 0, 1));
    EasyMock.verify(edge0to1);
  }

  @Test
  void playerClaimEdgeObject_test03_EdgeDoesNotExist_EmptyCollection_ExpectError() {
    BoardGraph b = new BoardGraph();
    GraphNode nodeStub = EasyMock.createNiceMock(GraphNode.class);

    EasyMock.expect(nodeStub.getNodeID()).andReturn(52);
    EasyMock.replay(nodeStub);

    b.addGraphNodeObject(nodeStub);

    Exception exception = assertThrows(IllegalArgumentException.class,
            () -> b.claimGraphEdgeObject(PlayerColor.ORANGE, 52, 53));

    assertEquals("Edge does not exist", exception.getMessage());
  }

  @Test
  void playerClaimEdgeObject_test04_EdgeAlreadyClaimed_MultipleItemCollection_ExpectError() {
    BoardGraph b = new BoardGraph();
    GraphNode nodeStub = EasyMock.createMock(GraphNode.class);
    GraphEdge edge50to53 = EasyMock.createMock(GraphEdge.class);
    GraphEdge edge50to52 = EasyMock.createNiceMock(GraphEdge.class);
    GraphEdge edge50to51 = EasyMock.createNiceMock(GraphEdge.class);

    EasyMock.expect(nodeStub.getNodeID()).andReturn(50);

    EasyMock.expect(edge50to53.getStartingNodeID()).andReturn(50);
    EasyMock.expect(edge50to53.getEndingNodeID()).andReturn(53);

    EasyMock.expect(edge50to52.getStartingNodeID()).andReturn(50);
    EasyMock.expect(edge50to52.getEndingNodeID()).andReturn(52);

    EasyMock.expect(edge50to51.getStartingNodeID()).andReturn(50);
    EasyMock.expect(edge50to51.getEndingNodeID()).andReturn(51);

    EasyMock.expect(edge50to53.claimGraphEdge(PlayerColor.WHITE))
            .andThrow(new EdgeAlreadyClaimedException("Edge already claimed"));
    EasyMock.replay(nodeStub, edge50to53, edge50to51, edge50to52);
    b.addGraphNodeObject(nodeStub);
    b.addGraphNodeConnection(50, edge50to53);
    b.addGraphNodeConnection(50, edge50to52);
    b.addGraphNodeConnection(50, edge50to51);

    Exception exception = assertThrows(EdgeAlreadyClaimedException.class,
            () -> b.claimGraphEdgeObject(PlayerColor.WHITE, 50, 53));

    assertEquals("Edge already claimed", exception.getMessage());

    EasyMock.verify(edge50to53);
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

  // getMatchingEdgeFromSet() tests
  @Test
  void getMatchingEdgeFromSet_test01_EmptySet_ExpectError() {
    BoardGraph b = new BoardGraph();
    GraphNode nodeStub = EasyMock.createNiceMock((GraphNode.class));
    EasyMock.expect(nodeStub.getNodeID()).andStubReturn(0);
    EasyMock.replay(nodeStub);

    b.addGraphNodeObject(nodeStub);
    Set<GraphEdge> node0EdgeSet = b.getConnectingEdgesByID(0);
    Exception exception = assertThrows(IllegalArgumentException.class,
            () -> b.getMatchingEdgeFromSet(node0EdgeSet, 0, 1));

    assertEquals("Edge does not exist", exception.getMessage());
  }

  @Test
  void getMatchingEdgeFromSet_test02_OneElementSet_ExpectEdge() {
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

    assertEquals(edgeStub, b.getMatchingEdgeFromSet(node0EdgeSet, 0, 1));
  }

  @Test
  void getMatchingEdgeFromSet_test03_MultipleElementSet_ExpectEdge() {
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

    assertEquals(edgeStub1, b.getMatchingEdgeFromSet(node0EdgeSet, 51, 53));
  }

  @Test
  void getMatchingEdgeFromSet_test04_MultipleElementSet_EdgeDoesNotExist_ExpectError() {
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
            () -> b.getMatchingEdgeFromSet(node0EdgeSet, 49, 53));

    assertEquals("Edge does not exist", exception.getMessage());

  }

  // checkPlayerOwnsNeighboringEdges() tests
  @Test
  void edgeCheckPlayerOwnsNeighboringEdges_test01_RedOwnsEdgeConnectingToStartingNode_ExpectTrue() {
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
  void edgeCheckPlayerOwnsNeighboringEdges_test02_WhiteOwnsEdgeConnectingToEndingNode_ExpectTrue() {
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
  void edgeCheckPlayerOwnsNeighboringEdges_test03_BlueOwnsNoConnectingEdges_ExpectFalse() {
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
  void edgeCheckPlayerOwnsNeighboringEdges_test04_OrangeConnectingEdgesToStartAndEnd_ExpectTrue() {
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

  // nodeCheckPlayerOwnsNeighboringEdge() tests

  @Test
  void nodeCheckPlayerOwnsNeighboringEdge_test01_playerOwnsNeighboringEdge_ExpectTrue() {
    BoardGraph b = new BoardGraph();
    GraphNode nodeStub0 = EasyMock.createNiceMock(GraphNode.class);
    GraphNode nodeStub1 = EasyMock.createNiceMock(GraphNode.class);
    GraphNode nodeStub3 = EasyMock.createNiceMock(GraphNode.class);

    GraphEdge edge0to1 = EasyMock.createNiceMock(GraphEdge.class);
    GraphEdge edge0to3 = EasyMock.createNiceMock(GraphEdge.class);

    EasyMock.expect(nodeStub0.getNodeID()).andStubReturn(0);
    EasyMock.expect(nodeStub1.getNodeID()).andStubReturn(1);
    EasyMock.expect(nodeStub3.getNodeID()).andStubReturn(3);

    EasyMock.expect(edge0to3.getStartingNodeID()).andStubReturn(0);
    EasyMock.expect(edge0to3.getEndingNodeID()).andStubReturn(3);
    EasyMock.expect(edge0to3.checkOwningColor()).andStubReturn(PlayerColor.SETUP);
    EasyMock.expect(edge0to1.getStartingNodeID()).andStubReturn(0);
    EasyMock.expect(edge0to1.getEndingNodeID()).andStubReturn(1);
    // Adjacent Edge which Red Owns
    EasyMock.expect(edge0to1.checkOwningColor()).andStubReturn(PlayerColor.RED);

    EasyMock.replay(nodeStub0, nodeStub3, nodeStub1, edge0to3, edge0to1);
    b.addGraphNodeObject(nodeStub0);
    b.addGraphNodeObject(nodeStub3);
    b.addGraphNodeObject(nodeStub1);
    b.addGraphNodeConnection(0, edge0to3);
    b.addGraphNodeConnection(0, edge0to1);

    assertTrue(b.nodeCheckPlayerOwnsNeighboringEdge(PlayerColor.RED, 0));

  }

  @Test
  void nodeCheckPlayerOwnsNeighboringEdge_test02_playerOwnsNeighboringEdge_ExpectTrue() {
    BoardGraph b = new BoardGraph();
    GraphNode nodeStub0 = EasyMock.createNiceMock(GraphNode.class);
    GraphNode nodeStub1 = EasyMock.createNiceMock(GraphNode.class);
    GraphNode nodeStub3 = EasyMock.createNiceMock(GraphNode.class);

    GraphEdge edge0to1 = EasyMock.createNiceMock(GraphEdge.class);
    GraphEdge edge0to3 = EasyMock.createNiceMock(GraphEdge.class);

    EasyMock.expect(nodeStub0.getNodeID()).andStubReturn(0);
    EasyMock.expect(nodeStub1.getNodeID()).andStubReturn(1);
    EasyMock.expect(nodeStub3.getNodeID()).andStubReturn(3);

    EasyMock.expect(edge0to3.getStartingNodeID()).andStubReturn(0);
    EasyMock.expect(edge0to3.getEndingNodeID()).andStubReturn(3);
    EasyMock.expect(edge0to3.checkOwningColor()).andStubReturn(PlayerColor.WHITE);
    EasyMock.expect(edge0to1.getStartingNodeID()).andStubReturn(0);
    EasyMock.expect(edge0to1.getEndingNodeID()).andStubReturn(1);
    EasyMock.expect(edge0to1.checkOwningColor()).andStubReturn(PlayerColor.RED);

    EasyMock.replay(nodeStub0, nodeStub3, nodeStub1, edge0to3, edge0to1);
    b.addGraphNodeObject(nodeStub0);
    b.addGraphNodeObject(nodeStub3);
    b.addGraphNodeObject(nodeStub1);
    b.addGraphNodeConnection(0, edge0to3);
    b.addGraphNodeConnection(0, edge0to1);

    assertTrue(b.nodeCheckPlayerOwnsNeighboringEdge(PlayerColor.WHITE, 0));

  }

  @Test
  void nodeCheckPlayerOwnsNeighboringEdge_test03_playerOwnsNoNeighboringEdge_ExpectFalse() {
    BoardGraph b = new BoardGraph();
    GraphNode nodeStub49 = EasyMock.createNiceMock(GraphNode.class);
    GraphNode nodeStub50 = EasyMock.createNiceMock(GraphNode.class);
    GraphNode nodeStub53 = EasyMock.createNiceMock(GraphNode.class);

    GraphEdge edge49to53 = EasyMock.createNiceMock(GraphEdge.class);
    GraphEdge edge50to53 = EasyMock.createNiceMock(GraphEdge.class);

    EasyMock.expect(nodeStub49.getNodeID()).andStubReturn(49);
    EasyMock.expect(nodeStub50.getNodeID()).andStubReturn(50);
    EasyMock.expect(nodeStub53.getNodeID()).andStubReturn(53);

    EasyMock.expect(edge50to53.getStartingNodeID()).andStubReturn(50);
    EasyMock.expect(edge50to53.getEndingNodeID()).andStubReturn(53);
    EasyMock.expect(edge50to53.checkOwningColor()).andStubReturn(PlayerColor.WHITE);
    EasyMock.expect(edge49to53.getStartingNodeID()).andStubReturn(49);
    EasyMock.expect(edge49to53.getEndingNodeID()).andStubReturn(53);
    EasyMock.expect(edge49to53.checkOwningColor()).andStubReturn(PlayerColor.RED);

    EasyMock.replay(nodeStub49, nodeStub53, nodeStub50, edge50to53, edge49to53);
    b.addGraphNodeObject(nodeStub50);
    b.addGraphNodeObject(nodeStub53);
    b.addGraphNodeObject(nodeStub49);
    b.addGraphNodeConnection(53, edge50to53);
    b.addGraphNodeConnection(53, edge49to53);

    assertFalse(b.nodeCheckPlayerOwnsNeighboringEdge(PlayerColor.BLUE, 53));

  }

  @Test
  void nodeCheckPlayerOwnsNeighboringEdge_test04_playerOwnsNoNeighboringEdge_ExpectFalse() {
    BoardGraph b = new BoardGraph();
    GraphNode nodeStub49 = EasyMock.createNiceMock(GraphNode.class);
    GraphNode nodeStub50 = EasyMock.createNiceMock(GraphNode.class);
    GraphNode nodeStub53 = EasyMock.createNiceMock(GraphNode.class);

    GraphEdge edge49to53 = EasyMock.createNiceMock(GraphEdge.class);
    GraphEdge edge50to53 = EasyMock.createNiceMock(GraphEdge.class);

    EasyMock.expect(nodeStub49.getNodeID()).andStubReturn(49);
    EasyMock.expect(nodeStub50.getNodeID()).andStubReturn(50);
    EasyMock.expect(nodeStub53.getNodeID()).andStubReturn(53);

    EasyMock.expect(edge50to53.getStartingNodeID()).andStubReturn(50);
    EasyMock.expect(edge50to53.getEndingNodeID()).andStubReturn(53);
    EasyMock.expect(edge50to53.checkOwningColor()).andStubReturn(PlayerColor.SETUP);
    EasyMock.expect(edge49to53.getStartingNodeID()).andStubReturn(49);
    EasyMock.expect(edge49to53.getEndingNodeID()).andStubReturn(53);
    EasyMock.expect(edge49to53.checkOwningColor()).andStubReturn(PlayerColor.SETUP);

    EasyMock.replay(nodeStub49, nodeStub53, nodeStub50, edge50to53, edge49to53);
    b.addGraphNodeObject(nodeStub50);
    b.addGraphNodeObject(nodeStub53);
    b.addGraphNodeObject(nodeStub49);
    b.addGraphNodeConnection(53, edge50to53);
    b.addGraphNodeConnection(53, edge49to53);

    assertFalse(b.nodeCheckPlayerOwnsNeighboringEdge(PlayerColor.ORANGE, 53));

  }


  // checkIfAdjacentNodesNotClaimed() tests

  @Test
  void checkAdjacentClaimedNodes_test01_NoIfAdjacentNodes_ExpectTrue() {
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
  void checkAdjacentClaimedNodes_test02_EndingIfAdjacentNodes_ExpectFalse() {
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
  void checkAdjacentClaimedNodes_test03_StartingIfAdjacentNodes_ExpectFalse() {
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
  void checkIfAdjacentClaimedNodes_test04_BothStartingAndEndingNodes_ExpectFalse() {
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

  // calculateLongestRoad() tests
  @Test
  void noPlayerRoads_NoPreviousWinner_ReturnSetup() {
    BoardGraph b = new BoardGraph();

    Player mockRedPlayer = EasyMock.createMock(Player.class);
    Player mockWhitePlayer = EasyMock.createMock(Player.class);
    Player mockBluePlayer = EasyMock.createMock(Player.class);
    Player mockOrangePlayer = EasyMock.createMock(Player.class);

    List<Player> players = List.of(mockRedPlayer, mockBluePlayer, mockOrangePlayer, mockWhitePlayer);

    assertEquals(PlayerColor.SETUP, b.calculateLongestRoad(players, PlayerColor.SETUP));
  }

  @Test
  void RedHasExactlyFourRoads_NoPreviousWinner_ReturnsSetup() {
    BoardGraph b = new BoardGraph();

    Player mockRedPlayer = EasyMock.createMock(Player.class);
    Player mockWhitePlayer = EasyMock.createMock(Player.class);
    Player mockBluePlayer = EasyMock.createMock(Player.class);
    Player mockOrangePlayer = EasyMock.createMock(Player.class);

    List<Player> players = List.of(mockRedPlayer, mockBluePlayer, mockOrangePlayer, mockWhitePlayer);

    GraphNode mockNode0 = EasyMock.createMock(GraphNode.class);
    GraphNode mockNode4 = EasyMock.createMock(GraphNode.class);
    GraphNode mockNode8 = EasyMock.createMock(GraphNode.class);
    GraphNode mockNode12 = EasyMock.createMock(GraphNode.class);
    GraphNode mockNode17 = EasyMock.createMock(GraphNode.class);

    GraphEdge mockEdge0To4 = EasyMock.createMock(GraphEdge.class);
    GraphEdge mockEdge4To8 = EasyMock.createMock(GraphEdge.class);
    GraphEdge mockEdge8To12 = EasyMock.createMock(GraphEdge.class);
    GraphEdge mockEdge12To17 = EasyMock.createMock(GraphEdge.class);

    EasyMock.expect(mockNode0.getNodeID()).andReturn(0);
    EasyMock.expect(mockNode4.getNodeID()).andReturn(4);
    EasyMock.expect(mockNode8.getNodeID()).andReturn(8);
    EasyMock.expect(mockNode12.getNodeID()).andReturn(12);
    EasyMock.expect(mockNode17.getNodeID()).andReturn(17);

    EasyMock.expect(mockEdge0To4.getStartingNodeID()).andReturn(0).anyTimes();
    EasyMock.expect(mockEdge0To4.getEndingNodeID()).andReturn(4).anyTimes();
    EasyMock.expect(mockEdge4To8.getStartingNodeID()).andReturn(4).anyTimes();
    EasyMock.expect(mockEdge4To8.getEndingNodeID()).andReturn(8).anyTimes();
    EasyMock.expect(mockEdge8To12.getStartingNodeID()).andReturn(8).anyTimes();
    EasyMock.expect(mockEdge8To12.getEndingNodeID()).andReturn(12).anyTimes();
    EasyMock.expect(mockEdge12To17.getStartingNodeID()).andReturn(12).anyTimes();
    EasyMock.expect(mockEdge12To17.getEndingNodeID()).andReturn(17).anyTimes();

    EasyMock.expect(mockEdge0To4.checkOwningColor()).andReturn(PlayerColor.RED).anyTimes();
    EasyMock.expect(mockEdge4To8.checkOwningColor()).andReturn(PlayerColor.RED).anyTimes();
    EasyMock.expect(mockEdge8To12.checkOwningColor()).andReturn(PlayerColor.RED).anyTimes();
    EasyMock.expect(mockEdge12To17.checkOwningColor()).andReturn(PlayerColor.RED).anyTimes();

    EasyMock.expect(mockRedPlayer.getPlayerColor()).andReturn(PlayerColor.RED).anyTimes();
    EasyMock.expect(mockBluePlayer.getPlayerColor()).andReturn(PlayerColor.BLUE).anyTimes();
    EasyMock.expect(mockOrangePlayer.getPlayerColor()).andReturn(PlayerColor.ORANGE).anyTimes();
    EasyMock.expect(mockWhitePlayer.getPlayerColor()).andReturn(PlayerColor.WHITE).anyTimes();

    EasyMock.replay(mockNode0, mockNode4, mockNode8, mockNode12, mockNode17,
            mockEdge0To4, mockEdge4To8, mockEdge8To12, mockEdge12To17,
            mockRedPlayer, mockBluePlayer, mockOrangePlayer, mockWhitePlayer);

    b.addGraphNodeObject(mockNode0);
    b.addGraphNodeObject(mockNode4);
    b.addGraphNodeObject(mockNode8);
    b.addGraphNodeObject(mockNode12);
    b.addGraphNodeObject(mockNode17);

    b.addGraphNodeConnection(0, mockEdge0To4);
    b.addGraphNodeConnection(4, mockEdge0To4);
    b.addGraphNodeConnection(4, mockEdge4To8);
    b.addGraphNodeConnection(8, mockEdge4To8);
    b.addGraphNodeConnection(8, mockEdge8To12);
    b.addGraphNodeConnection(12, mockEdge8To12);
    b.addGraphNodeConnection(12, mockEdge12To17);
    b.addGraphNodeConnection(17, mockEdge12To17);

    assertEquals(PlayerColor.SETUP, b.calculateLongestRoad(players, PlayerColor.SETUP));

    EasyMock.verify(mockNode0, mockNode4, mockNode8, mockNode12, mockNode17,
            mockEdge0To4, mockEdge4To8, mockEdge8To12, mockEdge12To17,
            mockRedPlayer, mockBluePlayer, mockOrangePlayer, mockWhitePlayer);
  }

  // Test Case 3
  @Test
  void OrangeHasExactlyFiveRoads_NoPreviousWinner_ReturnsOrange() {
    BoardGraph b = new BoardGraph();

    Player mockRedPlayer = EasyMock.createMock(Player.class);
    Player mockWhitePlayer = EasyMock.createMock(Player.class);
    Player mockBluePlayer = EasyMock.createMock(Player.class);
    Player mockOrangePlayer = EasyMock.createMock(Player.class);

    List<Player> players = List.of(mockRedPlayer, mockBluePlayer, mockOrangePlayer, mockWhitePlayer);

    GraphNode mockNode0 = EasyMock.createMock(GraphNode.class);
    GraphNode mockNode4 = EasyMock.createMock(GraphNode.class);
    GraphNode mockNode8 = EasyMock.createMock(GraphNode.class);
    GraphNode mockNode12 = EasyMock.createMock(GraphNode.class);
    GraphNode mockNode17 = EasyMock.createMock(GraphNode.class);
    GraphNode mockNode22 = EasyMock.createMock(GraphNode.class);

    GraphEdge mockEdge0To4 = EasyMock.createMock(GraphEdge.class);
    GraphEdge mockEdge4To8 = EasyMock.createMock(GraphEdge.class);
    GraphEdge mockEdge8To12 = EasyMock.createMock(GraphEdge.class);
    GraphEdge mockEdge12To17 = EasyMock.createMock(GraphEdge.class);
    GraphEdge mockEdge17To22 = EasyMock.createMock(GraphEdge.class);

    EasyMock.expect(mockNode0.getNodeID()).andReturn(0);
    EasyMock.expect(mockNode4.getNodeID()).andReturn(4);
    EasyMock.expect(mockNode8.getNodeID()).andReturn(8);
    EasyMock.expect(mockNode12.getNodeID()).andReturn(12);
    EasyMock.expect(mockNode17.getNodeID()).andReturn(17);
    EasyMock.expect(mockNode22.getNodeID()).andReturn(22);

    EasyMock.expect(mockEdge0To4.getStartingNodeID()).andReturn(0).anyTimes();
    EasyMock.expect(mockEdge0To4.getEndingNodeID()).andReturn(4).anyTimes();
    EasyMock.expect(mockEdge4To8.getStartingNodeID()).andReturn(4).anyTimes();
    EasyMock.expect(mockEdge4To8.getEndingNodeID()).andReturn(8).anyTimes();
    EasyMock.expect(mockEdge8To12.getStartingNodeID()).andReturn(8).anyTimes();
    EasyMock.expect(mockEdge8To12.getEndingNodeID()).andReturn(12).anyTimes();
    EasyMock.expect(mockEdge12To17.getStartingNodeID()).andReturn(12).anyTimes();
    EasyMock.expect(mockEdge12To17.getEndingNodeID()).andReturn(17).anyTimes();
    EasyMock.expect(mockEdge17To22.getStartingNodeID()).andReturn(17).anyTimes();
    EasyMock.expect(mockEdge17To22.getEndingNodeID()).andReturn(22).anyTimes();

    EasyMock.expect(mockEdge0To4.checkOwningColor()).andReturn(PlayerColor.ORANGE).anyTimes();
    EasyMock.expect(mockEdge4To8.checkOwningColor()).andReturn(PlayerColor.ORANGE).anyTimes();
    EasyMock.expect(mockEdge8To12.checkOwningColor()).andReturn(PlayerColor.ORANGE).anyTimes();
    EasyMock.expect(mockEdge12To17.checkOwningColor()).andReturn(PlayerColor.ORANGE).anyTimes();
    EasyMock.expect(mockEdge17To22.checkOwningColor()).andReturn(PlayerColor.ORANGE).anyTimes();

    EasyMock.expect(mockRedPlayer.getPlayerColor()).andReturn(PlayerColor.RED).anyTimes();
    EasyMock.expect(mockBluePlayer.getPlayerColor()).andReturn(PlayerColor.BLUE).anyTimes();
    EasyMock.expect(mockOrangePlayer.getPlayerColor()).andReturn(PlayerColor.ORANGE).anyTimes();
    EasyMock.expect(mockWhitePlayer.getPlayerColor()).andReturn(PlayerColor.WHITE).anyTimes();

    EasyMock.replay(mockNode0, mockNode4, mockNode8, mockNode12, mockNode17, mockNode22,
            mockEdge0To4, mockEdge4To8, mockEdge8To12, mockEdge12To17, mockEdge17To22,
            mockRedPlayer, mockBluePlayer, mockOrangePlayer, mockWhitePlayer);

    b.addGraphNodeObject(mockNode0);
    b.addGraphNodeObject(mockNode4);
    b.addGraphNodeObject(mockNode8);
    b.addGraphNodeObject(mockNode12);
    b.addGraphNodeObject(mockNode17);
    b.addGraphNodeObject(mockNode22);

    b.addGraphNodeConnection(0, mockEdge0To4);
    b.addGraphNodeConnection(4, mockEdge0To4);
    b.addGraphNodeConnection(4, mockEdge4To8);
    b.addGraphNodeConnection(8, mockEdge4To8);
    b.addGraphNodeConnection(8, mockEdge8To12);
    b.addGraphNodeConnection(12, mockEdge8To12);
    b.addGraphNodeConnection(12, mockEdge12To17);
    b.addGraphNodeConnection(17, mockEdge12To17);
    b.addGraphNodeConnection(17, mockEdge17To22);
    b.addGraphNodeConnection(22, mockEdge17To22);

    assertEquals(PlayerColor.ORANGE, b.calculateLongestRoad(players, PlayerColor.SETUP));

    EasyMock.verify(mockNode0, mockNode4, mockNode8, mockNode12, mockNode17, mockNode22,
            mockEdge0To4, mockEdge4To8, mockEdge8To12, mockEdge12To17, mockEdge17To22,
            mockRedPlayer, mockBluePlayer, mockOrangePlayer, mockWhitePlayer);
  }

  // Test Case 4
  @Test
  void WhiteAndBlueHaveFiveRoads_WhiteIsPreviousWinner_ReturnsWhite() {
    BoardGraph b = new BoardGraph();

    Player mockRedPlayer = EasyMock.createMock(Player.class);
    Player mockWhitePlayer = EasyMock.createMock(Player.class);
    Player mockBluePlayer = EasyMock.createMock(Player.class);
    Player mockOrangePlayer = EasyMock.createMock(Player.class);

    List<Player> players = List.of(mockRedPlayer, mockBluePlayer, mockOrangePlayer, mockWhitePlayer);

    GraphNode mockNode0 = EasyMock.createMock(GraphNode.class);
    GraphNode mockNode4 = EasyMock.createMock(GraphNode.class);
    GraphNode mockNode8 = EasyMock.createMock(GraphNode.class);
    GraphNode mockNode12 = EasyMock.createMock(GraphNode.class);
    GraphNode mockNode17 = EasyMock.createMock(GraphNode.class);
    GraphNode mockNode22 = EasyMock.createMock(GraphNode.class);
    GraphNode mockNode2 = EasyMock.createMock(GraphNode.class);
    GraphNode mockNode5 = EasyMock.createMock(GraphNode.class);
    GraphNode mockNode9 = EasyMock.createMock(GraphNode.class);
    GraphNode mockNode13 = EasyMock.createMock(GraphNode.class);
    GraphNode mockNode18 = EasyMock.createMock(GraphNode.class);
    GraphNode mockNode23 = EasyMock.createMock(GraphNode.class);

    GraphEdge mockEdge0To4 = EasyMock.createMock(GraphEdge.class);
    GraphEdge mockEdge4To8 = EasyMock.createMock(GraphEdge.class);
    GraphEdge mockEdge8To12 = EasyMock.createMock(GraphEdge.class);
    GraphEdge mockEdge12To17 = EasyMock.createMock(GraphEdge.class);
    GraphEdge mockEdge17To22 = EasyMock.createMock(GraphEdge.class);
    GraphEdge mockEdge2To5 = EasyMock.createMock(GraphEdge.class);
    GraphEdge mockEdge5To9 = EasyMock.createMock(GraphEdge.class);
    GraphEdge mockEdge9To13 = EasyMock.createMock(GraphEdge.class);
    GraphEdge mockEdge13To18 = EasyMock.createMock(GraphEdge.class);
    GraphEdge mockEdge18To23 = EasyMock.createMock(GraphEdge.class);

    EasyMock.expect(mockNode0.getNodeID()).andReturn(0);
    EasyMock.expect(mockNode4.getNodeID()).andReturn(4);
    EasyMock.expect(mockNode8.getNodeID()).andReturn(8);
    EasyMock.expect(mockNode12.getNodeID()).andReturn(12);
    EasyMock.expect(mockNode17.getNodeID()).andReturn(17);
    EasyMock.expect(mockNode22.getNodeID()).andReturn(22);
    EasyMock.expect(mockNode2.getNodeID()).andReturn(2);
    EasyMock.expect(mockNode5.getNodeID()).andReturn(5);
    EasyMock.expect(mockNode9.getNodeID()).andReturn(9);
    EasyMock.expect(mockNode13.getNodeID()).andReturn(13);
    EasyMock.expect(mockNode18.getNodeID()).andReturn(18);
    EasyMock.expect(mockNode23.getNodeID()).andReturn(23);

    EasyMock.expect(mockEdge0To4.getStartingNodeID()).andReturn(0).anyTimes();
    EasyMock.expect(mockEdge0To4.getEndingNodeID()).andReturn(4).anyTimes();
    EasyMock.expect(mockEdge4To8.getStartingNodeID()).andReturn(4).anyTimes();
    EasyMock.expect(mockEdge4To8.getEndingNodeID()).andReturn(8).anyTimes();
    EasyMock.expect(mockEdge8To12.getStartingNodeID()).andReturn(8).anyTimes();
    EasyMock.expect(mockEdge8To12.getEndingNodeID()).andReturn(12).anyTimes();
    EasyMock.expect(mockEdge12To17.getStartingNodeID()).andReturn(12).anyTimes();
    EasyMock.expect(mockEdge12To17.getEndingNodeID()).andReturn(17).anyTimes();
    EasyMock.expect(mockEdge17To22.getStartingNodeID()).andReturn(17).anyTimes();
    EasyMock.expect(mockEdge17To22.getEndingNodeID()).andReturn(22).anyTimes();
    EasyMock.expect(mockEdge2To5.getStartingNodeID()).andReturn(2).anyTimes();
    EasyMock.expect(mockEdge2To5.getEndingNodeID()).andReturn(5).anyTimes();
    EasyMock.expect(mockEdge5To9.getStartingNodeID()).andReturn(5).anyTimes();
    EasyMock.expect(mockEdge5To9.getEndingNodeID()).andReturn(9).anyTimes();
    EasyMock.expect(mockEdge9To13.getStartingNodeID()).andReturn(9).anyTimes();
    EasyMock.expect(mockEdge9To13.getEndingNodeID()).andReturn(13).anyTimes();
    EasyMock.expect(mockEdge13To18.getStartingNodeID()).andReturn(13).anyTimes();
    EasyMock.expect(mockEdge13To18.getEndingNodeID()).andReturn(18).anyTimes();
    EasyMock.expect(mockEdge18To23.getStartingNodeID()).andReturn(18).anyTimes();
    EasyMock.expect(mockEdge18To23.getEndingNodeID()).andReturn(23).anyTimes();

    EasyMock.expect(mockEdge0To4.checkOwningColor()).andReturn(PlayerColor.WHITE).anyTimes();
    EasyMock.expect(mockEdge4To8.checkOwningColor()).andReturn(PlayerColor.WHITE).anyTimes();
    EasyMock.expect(mockEdge8To12.checkOwningColor()).andReturn(PlayerColor.WHITE).anyTimes();
    EasyMock.expect(mockEdge12To17.checkOwningColor()).andReturn(PlayerColor.WHITE).anyTimes();
    EasyMock.expect(mockEdge17To22.checkOwningColor()).andReturn(PlayerColor.WHITE).anyTimes();
    EasyMock.expect(mockEdge2To5.checkOwningColor()).andReturn(PlayerColor.BLUE).anyTimes();
    EasyMock.expect(mockEdge5To9.checkOwningColor()).andReturn(PlayerColor.BLUE).anyTimes();
    EasyMock.expect(mockEdge9To13.checkOwningColor()).andReturn(PlayerColor.BLUE).anyTimes();
    EasyMock.expect(mockEdge13To18.checkOwningColor()).andReturn(PlayerColor.BLUE).anyTimes();
    EasyMock.expect(mockEdge18To23.checkOwningColor()).andReturn(PlayerColor.BLUE).anyTimes();

    EasyMock.expect(mockRedPlayer.getPlayerColor()).andReturn(PlayerColor.RED).anyTimes();
    EasyMock.expect(mockBluePlayer.getPlayerColor()).andReturn(PlayerColor.BLUE).anyTimes();
    EasyMock.expect(mockOrangePlayer.getPlayerColor()).andReturn(PlayerColor.ORANGE).anyTimes();
    EasyMock.expect(mockWhitePlayer.getPlayerColor()).andReturn(PlayerColor.WHITE).anyTimes();

    EasyMock.replay(mockNode0, mockNode4, mockNode8, mockNode12, mockNode17, mockNode22,
            mockNode2, mockNode5, mockNode9, mockNode13, mockNode18, mockNode23,
            mockEdge0To4, mockEdge4To8, mockEdge8To12, mockEdge12To17, mockEdge17To22,
            mockEdge2To5, mockEdge5To9, mockEdge9To13, mockEdge13To18, mockEdge18To23,
            mockRedPlayer, mockBluePlayer, mockOrangePlayer, mockWhitePlayer);

    b.addGraphNodeObject(mockNode0);
    b.addGraphNodeObject(mockNode4);
    b.addGraphNodeObject(mockNode8);
    b.addGraphNodeObject(mockNode12);
    b.addGraphNodeObject(mockNode17);
    b.addGraphNodeObject(mockNode22);
    b.addGraphNodeObject(mockNode2);
    b.addGraphNodeObject(mockNode5);
    b.addGraphNodeObject(mockNode9);
    b.addGraphNodeObject(mockNode13);
    b.addGraphNodeObject(mockNode18);
    b.addGraphNodeObject(mockNode23);

    b.addGraphNodeConnection(0, mockEdge0To4);
    b.addGraphNodeConnection(4, mockEdge0To4);
    b.addGraphNodeConnection(4, mockEdge4To8);
    b.addGraphNodeConnection(8, mockEdge4To8);
    b.addGraphNodeConnection(8, mockEdge8To12);
    b.addGraphNodeConnection(12, mockEdge8To12);
    b.addGraphNodeConnection(12, mockEdge12To17);
    b.addGraphNodeConnection(17, mockEdge12To17);
    b.addGraphNodeConnection(17, mockEdge17To22);
    b.addGraphNodeConnection(22, mockEdge17To22);
    b.addGraphNodeConnection(2, mockEdge2To5);
    b.addGraphNodeConnection(5, mockEdge2To5);
    b.addGraphNodeConnection(5, mockEdge5To9);
    b.addGraphNodeConnection(9, mockEdge5To9);
    b.addGraphNodeConnection(9, mockEdge9To13);
    b.addGraphNodeConnection(13, mockEdge9To13);
    b.addGraphNodeConnection(13, mockEdge13To18);
    b.addGraphNodeConnection(18, mockEdge13To18);
    b.addGraphNodeConnection(18, mockEdge18To23);
    b.addGraphNodeConnection(23, mockEdge18To23);

    assertEquals(PlayerColor.WHITE, b.calculateLongestRoad(players, PlayerColor.WHITE));

    EasyMock.verify(mockNode0, mockNode4, mockNode8, mockNode12, mockNode17, mockNode22,
            mockNode2, mockNode5, mockNode9, mockNode13, mockNode18, mockNode23,
            mockEdge0To4, mockEdge4To8, mockEdge8To12, mockEdge12To17, mockEdge17To22,
            mockEdge2To5, mockEdge5To9, mockEdge9To13, mockEdge13To18, mockEdge18To23,
            mockRedPlayer, mockBluePlayer, mockOrangePlayer, mockWhitePlayer);
  }

  // Test Case 5
  @Test
  void RedAndBlueHaveFiveRoads_BlueIsPreviousWinner_ReturnsBlue() {
    BoardGraph b = new BoardGraph();

    Player mockRedPlayer = EasyMock.createMock(Player.class);
    Player mockWhitePlayer = EasyMock.createMock(Player.class);
    Player mockBluePlayer = EasyMock.createMock(Player.class);
    Player mockOrangePlayer = EasyMock.createMock(Player.class);

    List<Player> players = List.of(mockRedPlayer, mockBluePlayer, mockOrangePlayer, mockWhitePlayer);

    GraphNode mockNode0 = EasyMock.createMock(GraphNode.class);
    GraphNode mockNode4 = EasyMock.createMock(GraphNode.class);
    GraphNode mockNode8 = EasyMock.createMock(GraphNode.class);
    GraphNode mockNode12 = EasyMock.createMock(GraphNode.class);
    GraphNode mockNode17 = EasyMock.createMock(GraphNode.class);
    GraphNode mockNode22 = EasyMock.createMock(GraphNode.class);
    GraphNode mockNode2 = EasyMock.createMock(GraphNode.class);
    GraphNode mockNode5 = EasyMock.createMock(GraphNode.class);
    GraphNode mockNode9 = EasyMock.createMock(GraphNode.class);
    GraphNode mockNode13 = EasyMock.createMock(GraphNode.class);
    GraphNode mockNode18 = EasyMock.createMock(GraphNode.class);
    GraphNode mockNode23 = EasyMock.createMock(GraphNode.class);

    GraphEdge mockEdge0To4 = EasyMock.createMock(GraphEdge.class);
    GraphEdge mockEdge4To8 = EasyMock.createMock(GraphEdge.class);
    GraphEdge mockEdge8To12 = EasyMock.createMock(GraphEdge.class);
    GraphEdge mockEdge12To17 = EasyMock.createMock(GraphEdge.class);
    GraphEdge mockEdge17To22 = EasyMock.createMock(GraphEdge.class);
    GraphEdge mockEdge2To5 = EasyMock.createMock(GraphEdge.class);
    GraphEdge mockEdge5To9 = EasyMock.createMock(GraphEdge.class);
    GraphEdge mockEdge9To13 = EasyMock.createMock(GraphEdge.class);
    GraphEdge mockEdge13To18 = EasyMock.createMock(GraphEdge.class);
    GraphEdge mockEdge18To23 = EasyMock.createMock(GraphEdge.class);

    EasyMock.expect(mockNode0.getNodeID()).andReturn(0);
    EasyMock.expect(mockNode4.getNodeID()).andReturn(4);
    EasyMock.expect(mockNode8.getNodeID()).andReturn(8);
    EasyMock.expect(mockNode12.getNodeID()).andReturn(12);
    EasyMock.expect(mockNode17.getNodeID()).andReturn(17);
    EasyMock.expect(mockNode22.getNodeID()).andReturn(22);
    EasyMock.expect(mockNode2.getNodeID()).andReturn(2);
    EasyMock.expect(mockNode5.getNodeID()).andReturn(5);
    EasyMock.expect(mockNode9.getNodeID()).andReturn(9);
    EasyMock.expect(mockNode13.getNodeID()).andReturn(13);
    EasyMock.expect(mockNode18.getNodeID()).andReturn(18);
    EasyMock.expect(mockNode23.getNodeID()).andReturn(23);

    EasyMock.expect(mockEdge0To4.getStartingNodeID()).andReturn(0).anyTimes();
    EasyMock.expect(mockEdge0To4.getEndingNodeID()).andReturn(4).anyTimes();
    EasyMock.expect(mockEdge4To8.getStartingNodeID()).andReturn(4).anyTimes();
    EasyMock.expect(mockEdge4To8.getEndingNodeID()).andReturn(8).anyTimes();
    EasyMock.expect(mockEdge8To12.getStartingNodeID()).andReturn(8).anyTimes();
    EasyMock.expect(mockEdge8To12.getEndingNodeID()).andReturn(12).anyTimes();
    EasyMock.expect(mockEdge12To17.getStartingNodeID()).andReturn(12).anyTimes();
    EasyMock.expect(mockEdge12To17.getEndingNodeID()).andReturn(17).anyTimes();
    EasyMock.expect(mockEdge17To22.getStartingNodeID()).andReturn(17).anyTimes();
    EasyMock.expect(mockEdge17To22.getEndingNodeID()).andReturn(22).anyTimes();
    EasyMock.expect(mockEdge2To5.getStartingNodeID()).andReturn(2).anyTimes();
    EasyMock.expect(mockEdge2To5.getEndingNodeID()).andReturn(5).anyTimes();
    EasyMock.expect(mockEdge5To9.getStartingNodeID()).andReturn(5).anyTimes();
    EasyMock.expect(mockEdge5To9.getEndingNodeID()).andReturn(9).anyTimes();
    EasyMock.expect(mockEdge9To13.getStartingNodeID()).andReturn(9).anyTimes();
    EasyMock.expect(mockEdge9To13.getEndingNodeID()).andReturn(13).anyTimes();
    EasyMock.expect(mockEdge13To18.getStartingNodeID()).andReturn(13).anyTimes();
    EasyMock.expect(mockEdge13To18.getEndingNodeID()).andReturn(18).anyTimes();
    EasyMock.expect(mockEdge18To23.getStartingNodeID()).andReturn(18).anyTimes();
    EasyMock.expect(mockEdge18To23.getEndingNodeID()).andReturn(23).anyTimes();

    EasyMock.expect(mockEdge0To4.checkOwningColor()).andReturn(PlayerColor.RED).anyTimes();
    EasyMock.expect(mockEdge4To8.checkOwningColor()).andReturn(PlayerColor.RED).anyTimes();
    EasyMock.expect(mockEdge8To12.checkOwningColor()).andReturn(PlayerColor.RED).anyTimes();
    EasyMock.expect(mockEdge12To17.checkOwningColor()).andReturn(PlayerColor.RED).anyTimes();
    EasyMock.expect(mockEdge17To22.checkOwningColor()).andReturn(PlayerColor.RED).anyTimes();
    EasyMock.expect(mockEdge2To5.checkOwningColor()).andReturn(PlayerColor.BLUE).anyTimes();
    EasyMock.expect(mockEdge5To9.checkOwningColor()).andReturn(PlayerColor.BLUE).anyTimes();
    EasyMock.expect(mockEdge9To13.checkOwningColor()).andReturn(PlayerColor.BLUE).anyTimes();
    EasyMock.expect(mockEdge13To18.checkOwningColor()).andReturn(PlayerColor.BLUE).anyTimes();
    EasyMock.expect(mockEdge18To23.checkOwningColor()).andReturn(PlayerColor.BLUE).anyTimes();

    EasyMock.expect(mockRedPlayer.getPlayerColor()).andReturn(PlayerColor.RED).anyTimes();
    EasyMock.expect(mockBluePlayer.getPlayerColor()).andReturn(PlayerColor.BLUE).anyTimes();
    EasyMock.expect(mockOrangePlayer.getPlayerColor()).andReturn(PlayerColor.ORANGE).anyTimes();
    EasyMock.expect(mockWhitePlayer.getPlayerColor()).andReturn(PlayerColor.WHITE).anyTimes();

    EasyMock.replay(mockNode0, mockNode4, mockNode8, mockNode12, mockNode17, mockNode22,
            mockNode2, mockNode5, mockNode9, mockNode13, mockNode18, mockNode23,
            mockEdge0To4, mockEdge4To8, mockEdge8To12, mockEdge12To17, mockEdge17To22,
            mockEdge2To5, mockEdge5To9, mockEdge9To13, mockEdge13To18, mockEdge18To23,
            mockRedPlayer, mockBluePlayer, mockOrangePlayer, mockWhitePlayer);

    b.addGraphNodeObject(mockNode0);
    b.addGraphNodeObject(mockNode4);
    b.addGraphNodeObject(mockNode8);
    b.addGraphNodeObject(mockNode12);
    b.addGraphNodeObject(mockNode17);
    b.addGraphNodeObject(mockNode22);
    b.addGraphNodeObject(mockNode2);
    b.addGraphNodeObject(mockNode5);
    b.addGraphNodeObject(mockNode9);
    b.addGraphNodeObject(mockNode13);
    b.addGraphNodeObject(mockNode18);
    b.addGraphNodeObject(mockNode23);

    b.addGraphNodeConnection(0, mockEdge0To4);
    b.addGraphNodeConnection(4, mockEdge0To4);
    b.addGraphNodeConnection(4, mockEdge4To8);
    b.addGraphNodeConnection(8, mockEdge4To8);
    b.addGraphNodeConnection(8, mockEdge8To12);
    b.addGraphNodeConnection(12, mockEdge8To12);
    b.addGraphNodeConnection(12, mockEdge12To17);
    b.addGraphNodeConnection(17, mockEdge12To17);
    b.addGraphNodeConnection(17, mockEdge17To22);
    b.addGraphNodeConnection(22, mockEdge17To22);
    b.addGraphNodeConnection(2, mockEdge2To5);
    b.addGraphNodeConnection(5, mockEdge2To5);
    b.addGraphNodeConnection(5, mockEdge5To9);
    b.addGraphNodeConnection(9, mockEdge5To9);
    b.addGraphNodeConnection(9, mockEdge9To13);
    b.addGraphNodeConnection(13, mockEdge9To13);
    b.addGraphNodeConnection(13, mockEdge13To18);
    b.addGraphNodeConnection(18, mockEdge13To18);
    b.addGraphNodeConnection(18, mockEdge18To23);
    b.addGraphNodeConnection(23, mockEdge18To23);

    assertEquals(PlayerColor.BLUE, b.calculateLongestRoad(players, PlayerColor.BLUE));

    EasyMock.verify(mockNode0, mockNode4, mockNode8, mockNode12, mockNode17, mockNode22,
            mockNode2, mockNode5, mockNode9, mockNode13, mockNode18, mockNode23,
            mockEdge0To4, mockEdge4To8, mockEdge8To12, mockEdge12To17, mockEdge17To22,
            mockEdge2To5, mockEdge5To9, mockEdge9To13, mockEdge13To18, mockEdge18To23,
            mockRedPlayer, mockBluePlayer, mockOrangePlayer, mockWhitePlayer);
  }

  // Test Case 6
  @Test
  void RedHasFiveRoads_BlueBuildsToSix_ReturnsBlue() {
    BoardGraph b = new BoardGraph();

    Player mockRedPlayer = EasyMock.createMock(Player.class);
    Player mockWhitePlayer = EasyMock.createMock(Player.class);
    Player mockBluePlayer = EasyMock.createMock(Player.class);
    Player mockOrangePlayer = EasyMock.createMock(Player.class);

    List<Player> players = List.of(mockRedPlayer, mockBluePlayer, mockOrangePlayer, mockWhitePlayer);

    GraphNode mockNode0 = EasyMock.createMock(GraphNode.class);
    GraphNode mockNode4 = EasyMock.createMock(GraphNode.class);
    GraphNode mockNode8 = EasyMock.createMock(GraphNode.class);
    GraphNode mockNode12 = EasyMock.createMock(GraphNode.class);
    GraphNode mockNode17 = EasyMock.createMock(GraphNode.class);
    GraphNode mockNode22 = EasyMock.createMock(GraphNode.class);
    GraphNode mockNode2 = EasyMock.createMock(GraphNode.class);
    GraphNode mockNode5 = EasyMock.createMock(GraphNode.class);
    GraphNode mockNode9 = EasyMock.createMock(GraphNode.class);
    GraphNode mockNode13 = EasyMock.createMock(GraphNode.class);
    GraphNode mockNode18 = EasyMock.createMock(GraphNode.class);
    GraphNode mockNode23 = EasyMock.createMock(GraphNode.class);
    GraphNode mockNode29 = EasyMock.createMock(GraphNode.class);

    GraphEdge mockEdge0To4 = EasyMock.createMock(GraphEdge.class);
    GraphEdge mockEdge4To8 = EasyMock.createMock(GraphEdge.class);
    GraphEdge mockEdge8To12 = EasyMock.createMock(GraphEdge.class);
    GraphEdge mockEdge12To17 = EasyMock.createMock(GraphEdge.class);
    GraphEdge mockEdge17To22 = EasyMock.createMock(GraphEdge.class);
    GraphEdge mockEdge2To5 = EasyMock.createMock(GraphEdge.class);
    GraphEdge mockEdge5To9 = EasyMock.createMock(GraphEdge.class);
    GraphEdge mockEdge9To13 = EasyMock.createMock(GraphEdge.class);
    GraphEdge mockEdge13To18 = EasyMock.createMock(GraphEdge.class);
    GraphEdge mockEdge18To23 = EasyMock.createMock(GraphEdge.class);
    GraphEdge mockEdge23To29 = EasyMock.createMock(GraphEdge.class);

    EasyMock.expect(mockNode0.getNodeID()).andReturn(0);
    EasyMock.expect(mockNode4.getNodeID()).andReturn(4);
    EasyMock.expect(mockNode8.getNodeID()).andReturn(8);
    EasyMock.expect(mockNode12.getNodeID()).andReturn(12);
    EasyMock.expect(mockNode17.getNodeID()).andReturn(17);
    EasyMock.expect(mockNode22.getNodeID()).andReturn(22);
    EasyMock.expect(mockNode2.getNodeID()).andReturn(2);
    EasyMock.expect(mockNode5.getNodeID()).andReturn(5);
    EasyMock.expect(mockNode9.getNodeID()).andReturn(9);
    EasyMock.expect(mockNode13.getNodeID()).andReturn(13);
    EasyMock.expect(mockNode18.getNodeID()).andReturn(18);
    EasyMock.expect(mockNode23.getNodeID()).andReturn(23);
    EasyMock.expect(mockNode29.getNodeID()).andReturn(29);

    EasyMock.expect(mockEdge0To4.getStartingNodeID()).andReturn(0).anyTimes();
    EasyMock.expect(mockEdge0To4.getEndingNodeID()).andReturn(4).anyTimes();
    EasyMock.expect(mockEdge4To8.getStartingNodeID()).andReturn(4).anyTimes();
    EasyMock.expect(mockEdge4To8.getEndingNodeID()).andReturn(8).anyTimes();
    EasyMock.expect(mockEdge8To12.getStartingNodeID()).andReturn(8).anyTimes();
    EasyMock.expect(mockEdge8To12.getEndingNodeID()).andReturn(12).anyTimes();
    EasyMock.expect(mockEdge12To17.getStartingNodeID()).andReturn(12).anyTimes();
    EasyMock.expect(mockEdge12To17.getEndingNodeID()).andReturn(17).anyTimes();
    EasyMock.expect(mockEdge17To22.getStartingNodeID()).andReturn(17).anyTimes();
    EasyMock.expect(mockEdge17To22.getEndingNodeID()).andReturn(22).anyTimes();
    EasyMock.expect(mockEdge2To5.getStartingNodeID()).andReturn(2).anyTimes();
    EasyMock.expect(mockEdge2To5.getEndingNodeID()).andReturn(5).anyTimes();
    EasyMock.expect(mockEdge5To9.getStartingNodeID()).andReturn(5).anyTimes();
    EasyMock.expect(mockEdge5To9.getEndingNodeID()).andReturn(9).anyTimes();
    EasyMock.expect(mockEdge9To13.getStartingNodeID()).andReturn(9).anyTimes();
    EasyMock.expect(mockEdge9To13.getEndingNodeID()).andReturn(13).anyTimes();
    EasyMock.expect(mockEdge13To18.getStartingNodeID()).andReturn(13).anyTimes();
    EasyMock.expect(mockEdge13To18.getEndingNodeID()).andReturn(18).anyTimes();
    EasyMock.expect(mockEdge18To23.getStartingNodeID()).andReturn(18).anyTimes();
    EasyMock.expect(mockEdge18To23.getEndingNodeID()).andReturn(23).anyTimes();
    EasyMock.expect(mockEdge23To29.getStartingNodeID()).andReturn(23).anyTimes();
    EasyMock.expect(mockEdge23To29.getEndingNodeID()).andReturn(29).anyTimes();

    EasyMock.expect(mockEdge0To4.checkOwningColor()).andReturn(PlayerColor.RED).anyTimes();
    EasyMock.expect(mockEdge4To8.checkOwningColor()).andReturn(PlayerColor.RED).anyTimes();
    EasyMock.expect(mockEdge8To12.checkOwningColor()).andReturn(PlayerColor.RED).anyTimes();
    EasyMock.expect(mockEdge12To17.checkOwningColor()).andReturn(PlayerColor.RED).anyTimes();
    EasyMock.expect(mockEdge17To22.checkOwningColor()).andReturn(PlayerColor.RED).anyTimes();
    EasyMock.expect(mockEdge2To5.checkOwningColor()).andReturn(PlayerColor.BLUE).anyTimes();
    EasyMock.expect(mockEdge5To9.checkOwningColor()).andReturn(PlayerColor.BLUE).anyTimes();
    EasyMock.expect(mockEdge9To13.checkOwningColor()).andReturn(PlayerColor.BLUE).anyTimes();
    EasyMock.expect(mockEdge13To18.checkOwningColor()).andReturn(PlayerColor.BLUE).anyTimes();
    EasyMock.expect(mockEdge18To23.checkOwningColor()).andReturn(PlayerColor.BLUE).anyTimes();
    EasyMock.expect(mockEdge23To29.checkOwningColor()).andReturn(PlayerColor.BLUE).anyTimes();

    EasyMock.expect(mockRedPlayer.getPlayerColor()).andReturn(PlayerColor.RED).anyTimes();
    EasyMock.expect(mockBluePlayer.getPlayerColor()).andReturn(PlayerColor.BLUE).anyTimes();
    EasyMock.expect(mockOrangePlayer.getPlayerColor()).andReturn(PlayerColor.ORANGE).anyTimes();
    EasyMock.expect(mockWhitePlayer.getPlayerColor()).andReturn(PlayerColor.WHITE).anyTimes();

    EasyMock.replay(mockNode0, mockNode4, mockNode8, mockNode12, mockNode17, mockNode22,
            mockNode2, mockNode5, mockNode9, mockNode13, mockNode18, mockNode23, mockNode29,
            mockEdge0To4, mockEdge4To8, mockEdge8To12, mockEdge12To17, mockEdge17To22,
            mockEdge2To5, mockEdge5To9, mockEdge9To13, mockEdge13To18, mockEdge18To23, mockEdge23To29,
            mockRedPlayer, mockBluePlayer, mockOrangePlayer, mockWhitePlayer);

    b.addGraphNodeObject(mockNode0);
    b.addGraphNodeObject(mockNode4);
    b.addGraphNodeObject(mockNode8);
    b.addGraphNodeObject(mockNode12);
    b.addGraphNodeObject(mockNode17);
    b.addGraphNodeObject(mockNode22);
    b.addGraphNodeObject(mockNode2);
    b.addGraphNodeObject(mockNode5);
    b.addGraphNodeObject(mockNode9);
    b.addGraphNodeObject(mockNode13);
    b.addGraphNodeObject(mockNode18);
    b.addGraphNodeObject(mockNode23);
    b.addGraphNodeObject(mockNode29);

    b.addGraphNodeConnection(0, mockEdge0To4);
    b.addGraphNodeConnection(4, mockEdge0To4);
    b.addGraphNodeConnection(4, mockEdge4To8);
    b.addGraphNodeConnection(8, mockEdge4To8);
    b.addGraphNodeConnection(8, mockEdge8To12);
    b.addGraphNodeConnection(12, mockEdge8To12);
    b.addGraphNodeConnection(12, mockEdge12To17);
    b.addGraphNodeConnection(17, mockEdge12To17);
    b.addGraphNodeConnection(17, mockEdge17To22);
    b.addGraphNodeConnection(22, mockEdge17To22);
    b.addGraphNodeConnection(2, mockEdge2To5);
    b.addGraphNodeConnection(5, mockEdge2To5);
    b.addGraphNodeConnection(5, mockEdge5To9);
    b.addGraphNodeConnection(9, mockEdge5To9);
    b.addGraphNodeConnection(9, mockEdge9To13);
    b.addGraphNodeConnection(13, mockEdge9To13);
    b.addGraphNodeConnection(13, mockEdge13To18);
    b.addGraphNodeConnection(18, mockEdge13To18);
    b.addGraphNodeConnection(18, mockEdge18To23);
    b.addGraphNodeConnection(23, mockEdge18To23);
    b.addGraphNodeConnection(23, mockEdge23To29);
    b.addGraphNodeConnection(29, mockEdge23To29);

    assertEquals(PlayerColor.BLUE, b.calculateLongestRoad(players, PlayerColor.RED));

    EasyMock.verify(mockNode0, mockNode4, mockNode8, mockNode12, mockNode17, mockNode22,
            mockNode2, mockNode5, mockNode9, mockNode13, mockNode18, mockNode23, mockNode29,
            mockEdge0To4, mockEdge4To8, mockEdge8To12, mockEdge12To17, mockEdge17To22,
            mockEdge2To5, mockEdge5To9, mockEdge9To13, mockEdge13To18, mockEdge18To23, mockEdge23To29,
            mockRedPlayer, mockBluePlayer, mockOrangePlayer, mockWhitePlayer);
  }

  // Test Case 7
  @Test
  void BlueHasSixRoadsBranching_LongestPathIsFour_RedIsPreviousWinner_ReturnsRed() {
    BoardGraph b = new BoardGraph();

    Player mockRedPlayer = EasyMock.createMock(Player.class);
    Player mockWhitePlayer = EasyMock.createMock(Player.class);
    Player mockBluePlayer = EasyMock.createMock(Player.class);
    Player mockOrangePlayer = EasyMock.createMock(Player.class);

    List<Player> players = List.of(mockRedPlayer, mockBluePlayer, mockOrangePlayer, mockWhitePlayer);

    // BLUE nodes - main line 0-4-8-12-17 plus disconnected segment 49-53-50
    GraphNode mockNode0 = EasyMock.createMock(GraphNode.class);
    GraphNode mockNode4 = EasyMock.createMock(GraphNode.class);
    GraphNode mockNode7 = EasyMock.createMock(GraphNode.class);
    GraphNode mockNode8 = EasyMock.createMock(GraphNode.class);
    GraphNode mockNode12 = EasyMock.createMock(GraphNode.class);
    GraphNode mockNode49 = EasyMock.createMock(GraphNode.class);
    GraphNode mockNode50 = EasyMock.createMock(GraphNode.class);
    GraphNode mockNode53 = EasyMock.createMock(GraphNode.class);

    // RED nodes - straight line of 5
    GraphNode mockNode2 = EasyMock.createMock(GraphNode.class);
    GraphNode mockNode6 = EasyMock.createMock(GraphNode.class);
    GraphNode mockNode10 = EasyMock.createMock(GraphNode.class);
    GraphNode mockNode14 = EasyMock.createMock(GraphNode.class);
    GraphNode mockNode19 = EasyMock.createMock(GraphNode.class);
    GraphNode mockNode25 = EasyMock.createMock(GraphNode.class);

    // BLUE edges - 4 in main line, 2 disconnected
    GraphEdge mockEdge0To4 = EasyMock.createMock(GraphEdge.class);
    GraphEdge mockEdge4To8 = EasyMock.createMock(GraphEdge.class);
    GraphEdge mockEdge7To12 = EasyMock.createMock(GraphEdge.class);
    GraphEdge mockEdge8To12 = EasyMock.createMock(GraphEdge.class);
    GraphEdge mockEdge49To53 = EasyMock.createMock(GraphEdge.class);
    GraphEdge mockEdge50To53 = EasyMock.createMock(GraphEdge.class);

    // RED edges - straight line of 5
    GraphEdge mockEdge2To6 = EasyMock.createMock(GraphEdge.class);
    GraphEdge mockEdge6To10 = EasyMock.createMock(GraphEdge.class);
    GraphEdge mockEdge10To14 = EasyMock.createMock(GraphEdge.class);
    GraphEdge mockEdge14To19 = EasyMock.createMock(GraphEdge.class);
    GraphEdge mockEdge19To25 = EasyMock.createMock(GraphEdge.class);

    EasyMock.expect(mockNode0.getNodeID()).andReturn(0);
    EasyMock.expect(mockNode4.getNodeID()).andReturn(4);
    EasyMock.expect(mockNode7.getNodeID()).andReturn(7);
    EasyMock.expect(mockNode8.getNodeID()).andReturn(8);
    EasyMock.expect(mockNode12.getNodeID()).andReturn(12);
    EasyMock.expect(mockNode49.getNodeID()).andReturn(49);
    EasyMock.expect(mockNode50.getNodeID()).andReturn(50);
    EasyMock.expect(mockNode53.getNodeID()).andReturn(53);
    EasyMock.expect(mockNode2.getNodeID()).andReturn(2);
    EasyMock.expect(mockNode6.getNodeID()).andReturn(6);
    EasyMock.expect(mockNode10.getNodeID()).andReturn(10);
    EasyMock.expect(mockNode14.getNodeID()).andReturn(14);
    EasyMock.expect(mockNode19.getNodeID()).andReturn(19);
    EasyMock.expect(mockNode25.getNodeID()).andReturn(25);

    EasyMock.expect(mockEdge0To4.getStartingNodeID()).andReturn(0).anyTimes();
    EasyMock.expect(mockEdge0To4.getEndingNodeID()).andReturn(4).anyTimes();
    EasyMock.expect(mockEdge4To8.getStartingNodeID()).andReturn(4).anyTimes();
    EasyMock.expect(mockEdge4To8.getEndingNodeID()).andReturn(8).anyTimes();
    EasyMock.expect(mockEdge7To12.getStartingNodeID()).andReturn(7).anyTimes();
    EasyMock.expect(mockEdge7To12.getEndingNodeID()).andReturn(12).anyTimes();
    EasyMock.expect(mockEdge8To12.getStartingNodeID()).andReturn(8).anyTimes();
    EasyMock.expect(mockEdge8To12.getEndingNodeID()).andReturn(12).anyTimes();
    EasyMock.expect(mockEdge49To53.getStartingNodeID()).andReturn(49).anyTimes();
    EasyMock.expect(mockEdge49To53.getEndingNodeID()).andReturn(53).anyTimes();
    EasyMock.expect(mockEdge50To53.getStartingNodeID()).andReturn(50).anyTimes();
    EasyMock.expect(mockEdge50To53.getEndingNodeID()).andReturn(53).anyTimes();
    EasyMock.expect(mockEdge2To6.getStartingNodeID()).andReturn(2).anyTimes();
    EasyMock.expect(mockEdge2To6.getEndingNodeID()).andReturn(6).anyTimes();
    EasyMock.expect(mockEdge6To10.getStartingNodeID()).andReturn(6).anyTimes();
    EasyMock.expect(mockEdge6To10.getEndingNodeID()).andReturn(10).anyTimes();
    EasyMock.expect(mockEdge10To14.getStartingNodeID()).andReturn(10).anyTimes();
    EasyMock.expect(mockEdge10To14.getEndingNodeID()).andReturn(14).anyTimes();
    EasyMock.expect(mockEdge14To19.getStartingNodeID()).andReturn(14).anyTimes();
    EasyMock.expect(mockEdge14To19.getEndingNodeID()).andReturn(19).anyTimes();
    EasyMock.expect(mockEdge19To25.getStartingNodeID()).andReturn(19).anyTimes();
    EasyMock.expect(mockEdge19To25.getEndingNodeID()).andReturn(25).anyTimes();

    EasyMock.expect(mockEdge0To4.checkOwningColor()).andReturn(PlayerColor.BLUE).anyTimes();
    EasyMock.expect(mockEdge4To8.checkOwningColor()).andReturn(PlayerColor.BLUE).anyTimes();
    EasyMock.expect(mockEdge7To12.checkOwningColor()).andReturn(PlayerColor.BLUE).anyTimes();
    EasyMock.expect(mockEdge8To12.checkOwningColor()).andReturn(PlayerColor.BLUE).anyTimes();
    EasyMock.expect(mockEdge49To53.checkOwningColor()).andReturn(PlayerColor.BLUE).anyTimes();
    EasyMock.expect(mockEdge50To53.checkOwningColor()).andReturn(PlayerColor.BLUE).anyTimes();
    EasyMock.expect(mockEdge2To6.checkOwningColor()).andReturn(PlayerColor.RED).anyTimes();
    EasyMock.expect(mockEdge6To10.checkOwningColor()).andReturn(PlayerColor.RED).anyTimes();
    EasyMock.expect(mockEdge10To14.checkOwningColor()).andReturn(PlayerColor.RED).anyTimes();
    EasyMock.expect(mockEdge14To19.checkOwningColor()).andReturn(PlayerColor.RED).anyTimes();
    EasyMock.expect(mockEdge19To25.checkOwningColor()).andReturn(PlayerColor.RED).anyTimes();

    EasyMock.expect(mockRedPlayer.getPlayerColor()).andReturn(PlayerColor.RED).anyTimes();
    EasyMock.expect(mockBluePlayer.getPlayerColor()).andReturn(PlayerColor.BLUE).anyTimes();
    EasyMock.expect(mockOrangePlayer.getPlayerColor()).andReturn(PlayerColor.ORANGE).anyTimes();
    EasyMock.expect(mockWhitePlayer.getPlayerColor()).andReturn(PlayerColor.WHITE).anyTimes();

    EasyMock.replay(mockNode0, mockNode4, mockNode7, mockNode8, mockNode12,
            mockNode49, mockNode50, mockNode53,
            mockNode2, mockNode6, mockNode10, mockNode14, mockNode19, mockNode25,
            mockEdge0To4, mockEdge4To8, mockEdge7To12, mockEdge8To12,
            mockEdge49To53, mockEdge50To53,
            mockEdge2To6, mockEdge6To10, mockEdge10To14, mockEdge14To19, mockEdge19To25,
            mockRedPlayer, mockBluePlayer, mockOrangePlayer, mockWhitePlayer);

    b.addGraphNodeObject(mockNode0);
    b.addGraphNodeObject(mockNode4);
    b.addGraphNodeObject(mockNode7);
    b.addGraphNodeObject(mockNode8);
    b.addGraphNodeObject(mockNode12);
    b.addGraphNodeObject(mockNode49);
    b.addGraphNodeObject(mockNode50);
    b.addGraphNodeObject(mockNode53);
    b.addGraphNodeObject(mockNode2);
    b.addGraphNodeObject(mockNode6);
    b.addGraphNodeObject(mockNode10);
    b.addGraphNodeObject(mockNode14);
    b.addGraphNodeObject(mockNode19);
    b.addGraphNodeObject(mockNode25);

    b.addGraphNodeConnection(0, mockEdge0To4);
    b.addGraphNodeConnection(4, mockEdge0To4);
    b.addGraphNodeConnection(4, mockEdge4To8);
    b.addGraphNodeConnection(8, mockEdge4To8);
    b.addGraphNodeConnection(7, mockEdge7To12);
    b.addGraphNodeConnection(12, mockEdge7To12);
    b.addGraphNodeConnection(8, mockEdge8To12);
    b.addGraphNodeConnection(12, mockEdge8To12);
    b.addGraphNodeConnection(49, mockEdge49To53);
    b.addGraphNodeConnection(53, mockEdge49To53);
    b.addGraphNodeConnection(50, mockEdge50To53);
    b.addGraphNodeConnection(53, mockEdge50To53);
    b.addGraphNodeConnection(2, mockEdge2To6);
    b.addGraphNodeConnection(6, mockEdge2To6);
    b.addGraphNodeConnection(6, mockEdge6To10);
    b.addGraphNodeConnection(10, mockEdge6To10);
    b.addGraphNodeConnection(10, mockEdge10To14);
    b.addGraphNodeConnection(14, mockEdge10To14);
    b.addGraphNodeConnection(14, mockEdge14To19);
    b.addGraphNodeConnection(19, mockEdge14To19);
    b.addGraphNodeConnection(19, mockEdge19To25);
    b.addGraphNodeConnection(25, mockEdge19To25);

    assertEquals(PlayerColor.RED, b.calculateLongestRoad(players, PlayerColor.RED));

    EasyMock.verify(mockNode0, mockNode4, mockNode7, mockNode8, mockNode12,
            mockNode49, mockNode50, mockNode53,
            mockNode2, mockNode6, mockNode10, mockNode14, mockNode19, mockNode25,
            mockEdge0To4, mockEdge4To8, mockEdge7To12, mockEdge8To12,
            mockEdge49To53, mockEdge50To53,
            mockEdge2To6, mockEdge6To10, mockEdge10To14, mockEdge14To19, mockEdge19To25,
            mockRedPlayer, mockBluePlayer, mockOrangePlayer, mockWhitePlayer);
  }

}
