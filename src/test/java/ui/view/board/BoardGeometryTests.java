package ui.view.board;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import org.junit.jupiter.api.Test;

public class BoardGeometryTests {

  private static final double HEX_SIZE = 40.0;
  private static final double PADDING = 10.0;
  private static final double EPSILON = 0.001;

  private final BoardGeometry geometry = new BoardGeometry(HEX_SIZE, PADDING);

  @Test
  public void testHexCentersHasNineteenEntries() {
    assertEquals(19, geometry.hexCenters().size());
  }

  @Test
  public void testNodePositionsHasFiftyFourDistinctEntries() {
    List<BoardGeometry.Point> nodes = geometry.nodePositions();
    assertEquals(54, nodes.size());

    Set<String> distinct = new HashSet<>();
    for (BoardGeometry.Point node : nodes) {
      distinct.add(Math.round(node.getX() * 100) + ":" + Math.round(node.getY() * 100));
    }
    assertEquals(54, distinct.size());
  }

  @Test
  public void testNodeRowsMatchModelConvention() {
    // 12 vertex rows of sizes [3,4,4,5,5,6,6,5,5,4,4,3], matching the
    // node-ID numbering in BoardGraph.buildBoard()
    int[] expectedRowSizes = {3, 4, 4, 5, 5, 6, 6, 5, 5, 4, 4, 3};

    TreeMap<Long, Integer> rowCounts = new TreeMap<>();
    for (BoardGeometry.Point node : geometry.nodePositions()) {
      long yKey = Math.round(node.getY() * 100);
      rowCounts.merge(yKey, 1, Integer::sum);
    }

    assertEquals(expectedRowSizes.length, rowCounts.size());
    int row = 0;
    for (int count : rowCounts.values()) {
      assertEquals(expectedRowSizes[row], count, "row " + row);
      row++;
    }
  }

  @Test
  public void testNodePositionsSortedByYThenX() {
    List<BoardGeometry.Point> nodes = geometry.nodePositions();
    for (int i = 1; i < nodes.size(); i++) {
      BoardGeometry.Point prev = nodes.get(i - 1);
      BoardGeometry.Point cur = nodes.get(i);
      boolean lowerRow = prev.getY() < cur.getY() - EPSILON;
      boolean sameRowLeftToRight = Math.abs(prev.getY() - cur.getY()) <= EPSILON
          && prev.getX() < cur.getX() - EPSILON;
      assertTrue(lowerRow || sameRowLeftToRight, "ordering violated at node " + i);
    }
  }

  @Test
  public void testEdgesHasSeventyTwoEntriesOrderedMinFirst() {
    List<int[]> edges = geometry.edges();
    assertEquals(72, edges.size());
    for (int[] edge : edges) {
      assertTrue(edge[0] < edge[1]);
    }
  }

  @Test
  public void testEdgesMatchModelEdgeList() {
    Set<String> edgeKeys = new HashSet<>();
    for (int[] edge : geometry.edges()) {
      edgeKeys.add(edge[0] + "-" + edge[1]);
    }
    // spot-checks against the literal edge list in BoardGraph.buildBoard()
    assertTrue(edgeKeys.contains("0-3"));
    assertTrue(edgeKeys.contains("0-4"));
    assertTrue(edgeKeys.contains("8-13"));
    assertTrue(edgeKeys.contains("28-34"));
    assertTrue(edgeKeys.contains("49-53"));
    assertFalse(edgeKeys.contains("0-1"));
    assertFalse(edgeKeys.contains("7-8"));
  }

  @Test
  public void testNodeToHexAdjacencyMatchesModelMap() {
    // samples from BoardHandler.initNodeHexMap(): node -> adjacent hex IDs
    assertEquals(List.of(0), hexesAdjacentToNode(0));
    assertEquals(List.of(0, 1, 4), hexesAdjacentToNode(8));
    assertEquals(List.of(0, 3, 4), hexesAdjacentToNode(12));
    assertEquals(List.of(9, 10, 14), hexesAdjacentToNode(30));
    assertEquals(List.of(18), hexesAdjacentToNode(53));
  }

  @Test
  public void testHexCornersLieOnNodePositions() {
    Set<String> nodeKeys = new HashSet<>();
    for (BoardGeometry.Point node : geometry.nodePositions()) {
      nodeKeys.add(Math.round(node.getX() * 100) + ":" + Math.round(node.getY() * 100));
    }
    for (int hexId = 0; hexId < 19; hexId++) {
      List<BoardGeometry.Point> corners = geometry.hexCorners(hexId);
      assertEquals(6, corners.size());
      for (BoardGeometry.Point corner : corners) {
        String key = Math.round(corner.getX() * 100) + ":" + Math.round(corner.getY() * 100);
        assertTrue(nodeKeys.contains(key), "hex " + hexId + " corner not a node");
      }
    }
  }

  @Test
  public void testEdgeMidpointIsHalfwayBetweenNodes() {
    BoardGeometry.Point midpoint = geometry.edgeMidpoint(0, 3);
    List<BoardGeometry.Point> nodes = geometry.nodePositions();
    assertEquals((nodes.get(0).getX() + nodes.get(3).getX()) / 2, midpoint.getX(), EPSILON);
    assertEquals((nodes.get(0).getY() + nodes.get(3).getY()) / 2, midpoint.getY(), EPSILON);
  }

  @Test
  public void testBoardCenterIsTheMiddleHexCenter() {
    // by symmetry, the centroid of all vertices is the center hex (id 9)
    BoardGeometry.Point center = geometry.boardCenter();
    BoardGeometry.Point middleHex = geometry.hexCenters().get(9);
    assertEquals(middleHex.getX(), center.getX(), EPSILON);
    assertEquals(middleHex.getY(), center.getY(), EPSILON);
  }

  @Test
  public void testBoardDimensions() {
    assertEquals(5 * Math.sqrt(3) * HEX_SIZE + 2 * PADDING, geometry.boardWidth(), EPSILON);
    assertEquals(8 * HEX_SIZE + 2 * PADDING, geometry.boardHeight(), EPSILON);
  }

  @Test
  public void testPositionsScaleLinearlyWithHexSize() {
    BoardGeometry doubled = new BoardGeometry(2 * HEX_SIZE, 0);
    BoardGeometry base = new BoardGeometry(HEX_SIZE, 0);
    List<BoardGeometry.Point> baseNodes = base.nodePositions();
    List<BoardGeometry.Point> doubledNodes = doubled.nodePositions();
    for (int i = 0; i < baseNodes.size(); i++) {
      assertEquals(2 * baseNodes.get(i).getX(), doubledNodes.get(i).getX(), EPSILON);
      assertEquals(2 * baseNodes.get(i).getY(), doubledNodes.get(i).getY(), EPSILON);
    }
  }

  @Test
  public void testAllDerivedEdgesAndNodesExistInRealBoard() {
    // BoardView.refresh() queries every derived edge and node against the
    // model; any mismatch with the graph would crash the view at runtime
    domain.model.board.BoardHandler board = new domain.model.board.BoardHandler();
    for (int[] edge : geometry.edges()) {
      assertEquals(domain.model.player.PlayerColor.SETUP,
          board.getEdgeOwner(edge[0], edge[1]),
          "edge " + edge[0] + "-" + edge[1]);
    }
    for (int nodeId = 0; nodeId < geometry.nodePositions().size(); nodeId++) {
      assertEquals(domain.model.player.PlayerColor.SETUP, board.getNodeOwner(nodeId));
      assertEquals(0, board.getNodeBuildingLevel(nodeId));
    }
    assertEquals(19, board.getHexRollNumbers().size());
    assertTrue(board.getRobberLocation() >= 0 && board.getRobberLocation() <= 18);
  }

  private List<Integer> hexesAdjacentToNode(int nodeId) {
    BoardGeometry.Point node = geometry.nodePositions().get(nodeId);
    List<BoardGeometry.Point> centers = geometry.hexCenters();
    List<Integer> adjacent = new ArrayList<>();
    for (int hexId = 0; hexId < centers.size(); hexId++) {
      double distance = Math.hypot(
          centers.get(hexId).getX() - node.getX(),
          centers.get(hexId).getY() - node.getY());
      if (distance <= HEX_SIZE + EPSILON) {
        adjacent.add(hexId);
      }
    }
    return adjacent;
  }
}
