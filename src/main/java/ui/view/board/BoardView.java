package ui.view.board;

import domain.model.board.BoardHandler;
import domain.model.board.Port;
import domain.model.player.PlayerColor;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.IntConsumer;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Interactive rendering of the Catan board: hex tiles with roll-number tokens,
 * the robber, and player roads/settlements/cities. A parent view arms a
 * {@link BoardSelectionMode} and registers callbacks to receive the node, edge,
 * or hex the user clicks; legality of the pick is enforced by the model, not here.
 */
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"},
    justification = "UI classes share JavaFX nodes, controllers, and models by reference")
public class BoardView {

  /**
   * Callback for an edge pick; node IDs are always passed smaller-first.
   */
  @FunctionalInterface
  public interface EdgeSelectionHandler {
    /**
     * Called when the user selects an edge; node IDs are always passed smaller-first.
     *
     * @param nodeA the smaller node ID
     * @param nodeB the larger node ID
     */
    void onEdge(int nodeA, int nodeB);
  }

  private static final double HEX_SIZE_PX = 54;
  // padding leaves a sea margin around the board wide enough for port markers
  private static final double PADDING_RATIO = 0.85;
  private static final double EDGE_END_TRIM_RATIO = 0.2;
  private static final int DESERT_ROLL_NUMBER = 7;
  private static final int HOT_ROLL_LOW = 6;
  private static final int HOT_ROLL_HIGH = 8;

  // decoration sizes as fractions of the hex size, so the board renders
  // correctly at any scale
  private static final double ROLL_TOKEN_RADIUS_RATIO = 0.29;
  private static final double ROLL_TOKEN_Y_OFFSET_RATIO = 0.26;
  private static final double HEX_NAME_Y_OFFSET_RATIO = -0.37;
  private static final double ROBBER_RADIUS_RATIO = 0.22;
  private static final double SETTLEMENT_RADIUS_RATIO = 0.18;
  private static final double CITY_SIDE_RATIO = 0.42;
  private static final double NODE_HIT_RADIUS_RATIO = 0.26;
  private static final double EDGE_HIT_WIDTH_RATIO = 0.32;
  private static final double ROAD_WIDTH_RATIO = 0.16;
  private static final double HEX_NAME_FONT_RATIO = 0.26;
  private static final double ROLL_TOKEN_FONT_RATIO = 0.29;
  private static final double PORT_OFFSET_RATIO = 0.5;
  private static final double PORT_RADIUS_RATIO = 0.3;
  private static final double PORT_DOCK_WIDTH_RATIO = 0.07;
  private static final double PORT_FONT_RATIO = 0.24;

  private static final String HEX_SHAPE_CSS = "hex-shape";
  private static final String HEX_FILL_CSS_PREFIX = "hex-fill-";
  private static final String HEX_LABEL_CSS = "hex-label";
  private static final String ROLL_TOKEN_CSS = "roll-token";
  private static final String ROLL_TOKEN_TEXT_CSS = "roll-token-text";
  private static final String ROLL_TOKEN_HOT_CSS = "roll-token-hot";
  private static final String ROAD_CSS = "road";
  private static final String ROAD_CSS_PREFIX = "road-";
  private static final String BUILDING_CSS = "building";
  private static final String BUILDING_CSS_PREFIX = "building-";
  private static final String CITY_CSS = "building-city";
  private static final String ROBBER_CSS = "robber";
  private static final String PICKABLE_CSS = "pickable";
  private static final String NODE_HIT_CSS = "node-hit";
  private static final String EDGE_HIT_CSS = "edge-hit";
  private static final String PORT_MARKER_CSS = "port-marker";
  private static final String PORT_FILL_CSS_PREFIX = "port-";
  private static final String PORT_DOCK_CSS = "port-dock";
  private static final String PORT_LABEL_CSS = "port-label";
  private static final String PORT_ANY_RESOURCE = "ANY";

  private final BoardHandler board;
  private final BoardGeometry geometry;
  private final double hexSize;
  private final Pane root;
  private final List<Polygon> hexPolygons = new ArrayList<>();
  private final Group roadLayer = new Group();
  private final Group buildingLayer = new Group();
  private final Circle robberMarker;
  private final Group edgeHitLayer = new Group();
  private final Group nodeHitLayer = new Group();

  private BoardSelectionMode mode = BoardSelectionMode.INERT;
  private IntConsumer onNodeSelected;
  private EdgeSelectionHandler onEdgeSelected;
  private IntConsumer onHexSelected;

  /**
   * Creates a BoardView at the default hex size.
   *
   * @param board the game board to render
   * @param labels the localized label bundle
   */
  public BoardView(BoardHandler board, ResourceBundle labels) {
    this(board, labels, HEX_SIZE_PX);
  }

  /**
   * Creates a BoardView at an explicit hex size.
   *
   * @param board the game board to render
   * @param labels the localized label bundle
   * @param hexSize the radius of each hex in pixels
   */
  public BoardView(BoardHandler board, ResourceBundle labels, double hexSize) {
    this.board = board;
    this.hexSize = hexSize;
    this.geometry = new BoardGeometry(hexSize, hexSize * PADDING_RATIO);
    this.robberMarker = buildRobberMarker(hexSize);
    this.root = buildLayers(labels);
    applyMode();
    refresh();
  }

  public Parent getRoot() {
    return root;
  }

  /**
   * Re-reads roads, buildings, and the robber position from the board model.
   */
  public void refresh() {
    rebuildRoads();
    rebuildBuildings();
    positionRobber();
  }

  /**
   * Sets the active pick mode, enabling or disabling the appropriate hit layers.
   *
   * @param newMode the selection mode to activate
   */
  public void setSelectionMode(BoardSelectionMode newMode) {
    this.mode = newMode;
    applyMode();
  }

  public void setOnNodeSelected(IntConsumer handler) {
    this.onNodeSelected = handler;
  }

  public void setOnEdgeSelected(EdgeSelectionHandler handler) {
    this.onEdgeSelected = handler;
  }

  public void setOnHexSelected(IntConsumer handler) {
    this.onHexSelected = handler;
  }

  private Pane buildLayers(ResourceBundle labels) {
    Pane pane = new Pane();
    pane.setPrefSize(geometry.boardWidth(), geometry.boardHeight());
    pane.setMinSize(geometry.boardWidth(), geometry.boardHeight());
    pane.setMaxSize(geometry.boardWidth(), geometry.boardHeight());

    Group portLayer = buildPortLayer(labels);
    Group hexLayer = buildHexLayer(labels);
    buildHitLayers();

    pane.getChildren().addAll(
        portLayer, hexLayer, roadLayer, buildingLayer, robberMarker,
        edgeHitLayer, nodeHitLayer);
    return pane;
  }

  private Group buildPortLayer(ResourceBundle labels) {
    Group layer = new Group();
    BoardGeometry.Point center = geometry.boardCenter();
    for (Port port : board.getAllPorts()) {
      layer.getChildren().addAll(buildPortMarker(port, center, labels));
    }
    return layer;
  }

  private List<javafx.scene.Node> buildPortMarker(
      Port port, BoardGeometry.Point center, ResourceBundle labels) {
    List<Integer> nodeIds = port.getNodeIds();
    int nodeA = nodeIds.get(0);
    int nodeB = nodeIds.get(1);
    BoardGeometry.Point mid = geometry.edgeMidpoint(nodeA, nodeB);

    // push the marker outward from the board center into the sea
    double dx = mid.getX() - center.getX();
    double dy = mid.getY() - center.getY();
    double length = Math.hypot(dx, dy);
    double markerX = mid.getX() + dx / length * scaled(PORT_OFFSET_RATIO);
    double markerY = mid.getY() + dy / length * scaled(PORT_OFFSET_RATIO);

    List<javafx.scene.Node> nodes = new ArrayList<>();
    nodes.add(buildPortDock(markerX, markerY, geometry.nodePositions().get(nodeA)));
    nodes.add(buildPortDock(markerX, markerY, geometry.nodePositions().get(nodeB)));

    Circle marker = new Circle(markerX, markerY, scaled(PORT_RADIUS_RATIO));
    marker.getStyleClass().addAll(PORT_MARKER_CSS,
        PORT_FILL_CSS_PREFIX + portResourceKey(port).toLowerCase());
    marker.setMouseTransparent(true);
    nodes.add(marker);

    Text label = new Text(portRatioText(port, labels));
    label.getStyleClass().add(PORT_LABEL_CSS);
    label.setFont(Font.font(null, FontWeight.BOLD, scaled(PORT_FONT_RATIO)));
    label.setMouseTransparent(true);
    centerText(label, markerX, markerY);
    nodes.add(label);

    return nodes;
  }

  private Line buildPortDock(double markerX, double markerY, BoardGeometry.Point node) {
    Line dock = new Line(markerX, markerY, node.getX(), node.getY());
    dock.getStyleClass().add(PORT_DOCK_CSS);
    dock.setStrokeWidth(scaled(PORT_DOCK_WIDTH_RATIO));
    dock.setMouseTransparent(true);
    return dock;
  }

  private static String portResourceKey(Port port) {
    return port.getResource() == domain.model.resources.Resource.ANY
        ? PORT_ANY_RESOURCE
        : port.getResource().name();
  }

  private static String portRatioText(Port port, ResourceBundle labels) {
    // compact ratio for the marker; the resource is shown by the marker colour
    return MessageFormat.format(labels.getString("port.boardRatio"), port.getTradeRatio());
  }

  private Group buildHexLayer(ResourceBundle labels) {
    Group layer = new Group();
    List<String> hexTypes = board.getHexOrder();
    List<Integer> rollNumbers = board.getHexRollNumbers();

    for (int hexId = 0; hexId < hexTypes.size(); hexId++) {
      Polygon polygon = buildHexPolygon(hexId, hexTypes.get(hexId));
      hexPolygons.add(polygon);
      layer.getChildren().add(polygon);

      BoardGeometry.Point center = geometry.hexCenters().get(hexId);
      layer.getChildren().add(buildHexNameText(hexTypes.get(hexId), center, labels));
      int rollNumber = rollNumbers.get(hexId);
      if (rollNumber != DESERT_ROLL_NUMBER) {
        layer.getChildren().addAll(buildRollToken(rollNumber, center));
      }
    }
    return layer;
  }

  private double scaled(double ratio) {
    return hexSize * ratio;
  }

  private Polygon buildHexPolygon(int hexId, String hexType) {
    Polygon polygon = new Polygon();
    for (BoardGeometry.Point corner : geometry.hexCorners(hexId)) {
      polygon.getPoints().addAll(corner.getX(), corner.getY());
    }
    polygon.getStyleClass().addAll(HEX_SHAPE_CSS,
        HEX_FILL_CSS_PREFIX + hexType.toLowerCase());
    polygon.setOnMouseClicked(e -> {
      if (mode == BoardSelectionMode.PICK_HEX && onHexSelected != null) {
        onHexSelected.accept(hexId);
      }
    });
    return polygon;
  }

  private Text buildHexNameText(String hexType, BoardGeometry.Point center, ResourceBundle labels) {
    Text text = new Text(labels.getString("hex." + hexType));
    text.getStyleClass().add(HEX_LABEL_CSS);
    // set the font directly so centerText measures the final bounds
    text.setFont(Font.font(scaled(HEX_NAME_FONT_RATIO)));
    text.setMouseTransparent(true);
    centerText(text, center.getX(), center.getY() + scaled(HEX_NAME_Y_OFFSET_RATIO));
    return text;
  }

  private List<javafx.scene.Node> buildRollToken(int rollNumber, BoardGeometry.Point center) {
    double tokenY = center.getY() + scaled(ROLL_TOKEN_Y_OFFSET_RATIO);
    Circle token = new Circle(center.getX(), tokenY, scaled(ROLL_TOKEN_RADIUS_RATIO));
    token.getStyleClass().add(ROLL_TOKEN_CSS);
    token.setMouseTransparent(true);

    Text number = new Text(String.valueOf(rollNumber));
    number.getStyleClass().add(ROLL_TOKEN_TEXT_CSS);
    number.setFont(Font.font(null, FontWeight.BOLD, scaled(ROLL_TOKEN_FONT_RATIO)));
    if (rollNumber == HOT_ROLL_LOW || rollNumber == HOT_ROLL_HIGH) {
      number.getStyleClass().add(ROLL_TOKEN_HOT_CSS);
    }
    number.setMouseTransparent(true);
    centerText(number, center.getX(), tokenY);

    return List.of(token, number);
  }

  private static void centerText(Text text, double centerX, double centerY) {
    double width = text.getLayoutBounds().getWidth();
    double height = text.getLayoutBounds().getHeight();
    text.setX(centerX - width / 2);
    text.setY(centerY + height / 4);
  }

  private static Circle buildRobberMarker(double hexSize) {
    Circle marker = new Circle(hexSize * ROBBER_RADIUS_RATIO);
    marker.getStyleClass().add(ROBBER_CSS);
    marker.setMouseTransparent(true);
    return marker;
  }

  private void buildHitLayers() {
    for (int[] edge : geometry.edges()) {
      edgeHitLayer.getChildren().add(buildEdgeHitLine(edge[0], edge[1]));
    }
    List<BoardGeometry.Point> nodes = geometry.nodePositions();
    for (int nodeId = 0; nodeId < nodes.size(); nodeId++) {
      nodeHitLayer.getChildren().add(buildNodeHitCircle(nodeId, nodes.get(nodeId)));
    }
  }

  private Line buildEdgeHitLine(int nodeA, int nodeB) {
    Line line = trimmedEdgeLine(nodeA, nodeB);
    // paints come from the CSS class so :hover affordances can override them
    line.getStyleClass().add(EDGE_HIT_CSS);
    line.setStrokeWidth(scaled(EDGE_HIT_WIDTH_RATIO));
    line.setPickOnBounds(false);
    line.setOnMouseClicked(e -> {
      if (mode == BoardSelectionMode.PICK_EDGE && onEdgeSelected != null) {
        onEdgeSelected.onEdge(nodeA, nodeB);
      }
    });
    return line;
  }

  private Circle buildNodeHitCircle(int nodeId, BoardGeometry.Point position) {
    Circle circle = new Circle(position.getX(), position.getY(), scaled(NODE_HIT_RADIUS_RATIO));
    circle.getStyleClass().add(NODE_HIT_CSS);
    circle.setPickOnBounds(false);
    circle.setOnMouseClicked(e -> {
      if (mode == BoardSelectionMode.PICK_NODE && onNodeSelected != null) {
        onNodeSelected.accept(nodeId);
      }
    });
    return circle;
  }

  private Line trimmedEdgeLine(int nodeA, int nodeB) {
    List<BoardGeometry.Point> nodes = geometry.nodePositions();
    BoardGeometry.Point a = nodes.get(nodeA);
    BoardGeometry.Point b = nodes.get(nodeB);
    double dx = b.getX() - a.getX();
    double dy = b.getY() - a.getY();
    return new Line(
        a.getX() + dx * EDGE_END_TRIM_RATIO,
        a.getY() + dy * EDGE_END_TRIM_RATIO,
        b.getX() - dx * EDGE_END_TRIM_RATIO,
        b.getY() - dy * EDGE_END_TRIM_RATIO);
  }

  private void rebuildRoads() {
    roadLayer.getChildren().clear();
    for (int[] edge : geometry.edges()) {
      PlayerColor owner = board.getEdgeOwner(edge[0], edge[1]);
      if (owner != PlayerColor.SETUP) {
        Line roadLine = trimmedEdgeLine(edge[0], edge[1]);
        roadLine.setStrokeWidth(scaled(ROAD_WIDTH_RATIO));
        roadLine.setMouseTransparent(true);
        roadLine.getStyleClass().addAll(ROAD_CSS,
            ROAD_CSS_PREFIX + owner.name().toLowerCase());
        roadLayer.getChildren().add(roadLine);
      }
    }
  }

  private void rebuildBuildings() {
    buildingLayer.getChildren().clear();
    List<BoardGeometry.Point> nodes = geometry.nodePositions();
    for (int nodeId = 0; nodeId < nodes.size(); nodeId++) {
      int level = board.getNodeBuildingLevel(nodeId);
      if (level > 0) {
        buildingLayer.getChildren().add(
            buildBuildingShape(nodes.get(nodeId), board.getNodeOwner(nodeId), level));
      }
    }
  }

  private javafx.scene.shape.Shape buildBuildingShape(
      BoardGeometry.Point position, PlayerColor owner, int level) {
    javafx.scene.shape.Shape shape;
    if (level == 1) {
      shape = new Circle(position.getX(), position.getY(), scaled(SETTLEMENT_RADIUS_RATIO));
    } else {
      double citySide = scaled(CITY_SIDE_RATIO);
      shape = new Rectangle(position.getX() - citySide / 2,
          position.getY() - citySide / 2, citySide, citySide);
      shape.getStyleClass().add(CITY_CSS);
    }
    shape.getStyleClass().addAll(BUILDING_CSS,
        BUILDING_CSS_PREFIX + owner.name().toLowerCase());
    shape.setMouseTransparent(true);
    return shape;
  }

  private void positionRobber() {
    // the robber sits on the hex's roll-number token, as in the real game
    BoardGeometry.Point center = geometry.hexCenters().get(board.getRobberLocation());
    robberMarker.setCenterX(center.getX());
    robberMarker.setCenterY(center.getY() + scaled(ROLL_TOKEN_Y_OFFSET_RATIO));
  }

  private void applyMode() {
    nodeHitLayer.setMouseTransparent(mode != BoardSelectionMode.PICK_NODE);
    edgeHitLayer.setMouseTransparent(mode != BoardSelectionMode.PICK_EDGE);

    boolean hexPicking = mode == BoardSelectionMode.PICK_HEX;
    for (Polygon polygon : hexPolygons) {
      togglePickable(polygon, hexPicking);
    }
    for (javafx.scene.Node node : nodeHitLayer.getChildren()) {
      togglePickable(node, mode == BoardSelectionMode.PICK_NODE);
    }
    for (javafx.scene.Node edge : edgeHitLayer.getChildren()) {
      togglePickable(edge, mode == BoardSelectionMode.PICK_EDGE);
    }
  }

  private static void togglePickable(javafx.scene.Node node, boolean pickable) {
    node.getStyleClass().remove(PICKABLE_CSS);
    if (pickable) {
      node.getStyleClass().add(PICKABLE_CSS);
    }
  }
}
