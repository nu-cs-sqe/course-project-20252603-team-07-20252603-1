package ui.view.board;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Pure geometry for the standard 19-hex Catan board (pointy-top hexes in
 * 3-4-5-4-3 rows). Computes hex centers/corners and derives the 54 vertex
 * positions and 72 edges using the same numbering convention as the domain
 * model: node IDs are assigned sorted by y, then x.
 *
 * <p>Contains no JavaFX types so it can be unit tested headlessly.</p>
 */
public final class BoardGeometry {

  /** An immutable 2D point. */
  public static final class Point {
    private final double x;
    private final double y;

    Point(double x, double y) {
      this.x = x;
      this.y = y;
    }

    public double getX() {
      return x;
    }

    public double getY() {
      return y;
    }
  }

  private static final int[] HEX_ROW_SIZES = {3, 4, 5, 4, 3};
  private static final int WIDEST_ROW_SIZE = 5;
  private static final double ROW_VERTICAL_SPACING_FACTOR = 1.5;
  private static final double POSITION_KEY_SCALE = 1000.0;
  private static final double EDGE_LENGTH_TOLERANCE = 0.01;

  private final double hexSize;
  private final double padding;
  private final double hexWidth;
  private final List<Point> hexCenters;
  private final List<Point> nodePositions;
  private final List<int[]> edges;

  public BoardGeometry(double hexSize, double padding) {
    this.hexSize = hexSize;
    this.padding = padding;
    this.hexWidth = Math.sqrt(3) * hexSize;
    this.hexCenters = computeHexCenters();
    this.nodePositions = computeNodePositions();
    this.edges = computeEdges();
  }

  /**
   * Returns the center of each hex, indexed by hex ID 0-18.
   *
   * @return list of 19 hex centers
   */
  public List<Point> hexCenters() {
    return new ArrayList<>(hexCenters);
  }

  /**
   * Returns the six corner points of the given hex, clockwise from the top.
   *
   * @param hexId the hex ID, within [0, 18]
   * @return list of 6 corner points
   */
  public List<Point> hexCorners(int hexId) {
    Point center = hexCenters.get(hexId);
    return cornersAround(center);
  }

  /**
   * Returns the position of each board vertex, indexed by node ID 0-53.
   * Node IDs follow the domain model convention: sorted by y, then x.
   *
   * @return list of 54 vertex positions
   */
  public List<Point> nodePositions() {
    return new ArrayList<>(nodePositions);
  }

  /**
   * Returns all 72 board edges as node-ID pairs, each ordered (smaller, larger).
   *
   * @return list of 72 two-element arrays
   */
  public List<int[]> edges() {
    List<int[]> copy = new ArrayList<>();
    for (int[] edge : edges) {
      copy.add(new int[] {edge[0], edge[1]});
    }
    return copy;
  }

  /**
   * Returns the midpoint of the edge between two nodes.
   *
   * @param nodeA the first node ID
   * @param nodeB the second node ID
   * @return the midpoint of the two vertex positions
   */
  public Point edgeMidpoint(int nodeA, int nodeB) {
    Point a = nodePositions.get(nodeA);
    Point b = nodePositions.get(nodeB);
    return new Point((a.getX() + b.getX()) / 2, (a.getY() + b.getY()) / 2);
  }

  /**
   * Returns the total board width including padding.
   *
   * @return the board width
   */
  public double boardWidth() {
    return WIDEST_ROW_SIZE * hexWidth + 2 * padding;
  }

  /**
   * Returns the total board height including padding.
   *
   * @return the board height
   */
  public double boardHeight() {
    return 8 * hexSize + 2 * padding;
  }

  private List<Point> computeHexCenters() {
    List<Point> centers = new ArrayList<>();
    for (int row = 0; row < HEX_ROW_SIZES.length; row++) {
      int rowSize = HEX_ROW_SIZES[row];
      double rowOffset = (WIDEST_ROW_SIZE - rowSize) * hexWidth / 2;
      double centerY = padding + hexSize + ROW_VERTICAL_SPACING_FACTOR * hexSize * row;
      for (int col = 0; col < rowSize; col++) {
        double centerX = padding + rowOffset + hexWidth / 2 + col * hexWidth;
        centers.add(new Point(centerX, centerY));
      }
    }
    return centers;
  }

  private List<Point> cornersAround(Point center) {
    double halfWidth = hexWidth / 2;
    double halfSize = hexSize / 2;
    List<Point> corners = new ArrayList<>();
    corners.add(new Point(center.getX(), center.getY() - hexSize));
    corners.add(new Point(center.getX() + halfWidth, center.getY() - halfSize));
    corners.add(new Point(center.getX() + halfWidth, center.getY() + halfSize));
    corners.add(new Point(center.getX(), center.getY() + hexSize));
    corners.add(new Point(center.getX() - halfWidth, center.getY() + halfSize));
    corners.add(new Point(center.getX() - halfWidth, center.getY() - halfSize));
    return corners;
  }

  private List<Point> computeNodePositions() {
    Map<Long, Point> uniqueCorners = new LinkedHashMap<>();
    for (Point center : hexCenters) {
      for (Point corner : cornersAround(center)) {
        uniqueCorners.putIfAbsent(positionKey(corner), corner);
      }
    }
    List<Point> nodes = new ArrayList<>(uniqueCorners.values());
    nodes.sort(Comparator
            .comparingLong((Point p) -> Math.round(p.getY() * POSITION_KEY_SCALE))
            .thenComparingLong(p -> Math.round(p.getX() * POSITION_KEY_SCALE)));
    return nodes;
  }

  private long positionKey(Point point) {
    long xKey = Math.round(point.getX() * POSITION_KEY_SCALE);
    long yKey = Math.round(point.getY() * POSITION_KEY_SCALE);
    return xKey * 1_000_000_000L + yKey;
  }

  private List<int[]> computeEdges() {
    List<int[]> result = new ArrayList<>();
    double tolerance = EDGE_LENGTH_TOLERANCE * hexSize;
    for (int a = 0; a < nodePositions.size(); a++) {
      for (int b = a + 1; b < nodePositions.size(); b++) {
        Point pointA = nodePositions.get(a);
        Point pointB = nodePositions.get(b);
        double distance = Math.hypot(
                pointA.getX() - pointB.getX(), pointA.getY() - pointB.getY());
        if (Math.abs(distance - hexSize) <= tolerance) {
          result.add(new int[] {a, b});
        }
      }
    }
    return result;
  }
}
