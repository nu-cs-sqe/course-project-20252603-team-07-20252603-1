package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RoadBuildingCardTests {

  @Test // Test Case 1
  public void Play_NullPlayer_ExpectIllegalArgumentException() {
    RoadBuildingCard roadBuildingCard = new RoadBuildingCard();

    Edge edge1 = EasyMock.createMock(Edge.class);
    Edge edge2 = EasyMock.createMock(Edge.class);
    EasyMock.replay(edge1, edge2);

    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> roadBuildingCard.play(null, edge1, edge2)
    );
    assertEquals("Player cannot be null.", exception.getMessage());

    EasyMock.verify(edge1, edge2);
  }

  @Test // Test Case 2
  public void Play_NullEdge1_ExpectIllegalArgumentException() {
    RoadBuildingCard roadBuildingCard = new RoadBuildingCard();

    Player player = EasyMock.createMock(Player.class);
    Edge edge2 = EasyMock.createMock(Edge.class);
    EasyMock.replay(player, edge2);

    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> roadBuildingCard.play(player, null, edge2)
    );
    assertEquals("Edge cannot be null.", exception.getMessage());

    EasyMock.verify(player, edge2);
  }

  @Test // Test Case 3
  public void Play_ValidEdgesZeroRoadsPlaced_ExpectTwoRoadsPlaced() {
    RoadBuildingCard roadBuildingCard = new RoadBuildingCard();

    Player player = EasyMock.createMock(Player.class);
    Edge edge1 = EasyMock.createMock(Edge.class);
    Edge edge2 = EasyMock.createMock(Edge.class);

    EasyMock.expect(player.getRoads()).andReturn(Collections.emptyList());
    EasyMock.expect(edge1.isOccupied()).andReturn(false);
    EasyMock.expect(edge1.isConnectedToPlayerNetwork()).andReturn(true);
    player.placeRoad(edge1);
    EasyMock.expect(edge2.isOccupied()).andReturn(false);
    EasyMock.expect(edge2.isConnectedToPlayerNetwork()).andReturn(true);
    player.placeRoad(edge2);
    EasyMock.replay(player, edge1, edge2);

    roadBuildingCard.play(player, edge1, edge2);

    EasyMock.verify(player, edge1, edge2);
  }

  @Test // Test Case 4
  public void Play_ValidEdgesThirteenRoadsPlaced_ExpectTwoRoadsPlaced() {
    RoadBuildingCard roadBuildingCard = new RoadBuildingCard();

    Player player = EasyMock.createMock(Player.class);
    Edge edge1 = EasyMock.createMock(Edge.class);
    Edge edge2 = EasyMock.createMock(Edge.class);

    List<Edge> existingRoads = Collections.nCopies(13, null);
    EasyMock.expect(player.getRoads()).andReturn(existingRoads);
    EasyMock.expect(edge1.isOccupied()).andReturn(false);
    EasyMock.expect(edge1.isConnectedToPlayerNetwork()).andReturn(true);
    player.placeRoad(edge1);
    EasyMock.expect(edge2.isOccupied()).andReturn(false);
    EasyMock.expect(edge2.isConnectedToPlayerNetwork()).andReturn(true);
    player.placeRoad(edge2);
    EasyMock.replay(player, edge1, edge2);

    roadBuildingCard.play(player, edge1, edge2);

    EasyMock.verify(player, edge1, edge2);
  }

  @Test // Test Case 5
  public void Play_ValidEdge1NullEdge2FourteenRoadsPlaced_ExpectOneRoadPlaced() {
    RoadBuildingCard roadBuildingCard = new RoadBuildingCard();

    Player player = EasyMock.createMock(Player.class);
    Edge edge1 = EasyMock.createMock(Edge.class);

    List<Edge> existingRoads = Collections.nCopies(14, null);
    EasyMock.expect(player.getRoads()).andReturn(existingRoads);
    EasyMock.expect(edge1.isOccupied()).andReturn(false);
    EasyMock.expect(edge1.isConnectedToPlayerNetwork()).andReturn(true);
    player.placeRoad(edge1);
    EasyMock.replay(player, edge1);

    roadBuildingCard.play(player, edge1, null);

    EasyMock.verify(player, edge1);
  }

  @Test // Test Case 6
  public void Play_FifteenRoadsPlaced_ExpectIllegalStateException() {
    RoadBuildingCard roadBuildingCard = new RoadBuildingCard();

    Player player = EasyMock.createMock(Player.class);
    Edge edge1 = EasyMock.createMock(Edge.class);
    Edge edge2 = EasyMock.createMock(Edge.class);

    List<Edge> existingRoads = Collections.nCopies(15, null);
    EasyMock.expect(player.getRoads()).andReturn(existingRoads);
    EasyMock.replay(player, edge1, edge2);

    IllegalStateException exception = assertThrows(
        IllegalStateException.class,
        () -> roadBuildingCard.play(player, edge1, edge2)
    );
    assertEquals("No roads remaining.", exception.getMessage());

    EasyMock.verify(player, edge1, edge2);
  }

  @Test // Test Case 7
  public void Play_Edge1Occupied_ExpectIllegalArgumentException() {
    RoadBuildingCard roadBuildingCard = new RoadBuildingCard();

    Player player = EasyMock.createMock(Player.class);
    Edge edge1 = EasyMock.createMock(Edge.class);
    Edge edge2 = EasyMock.createMock(Edge.class);

    EasyMock.expect(player.getRoads()).andReturn(Collections.emptyList());
    EasyMock.expect(edge1.isOccupied()).andReturn(true);
    EasyMock.replay(player, edge1, edge2);

    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> roadBuildingCard.play(player, edge1, edge2)
    );
    assertEquals("Edge is already occupied.", exception.getMessage());

    EasyMock.verify(player, edge1, edge2);
  }

  @Test // Test Case 8
  public void Play_Edge1NotConnectedToNetwork_ExpectIllegalArgumentException() {
    RoadBuildingCard roadBuildingCard = new RoadBuildingCard();

    Player player = EasyMock.createMock(Player.class);
    Edge edge1 = EasyMock.createMock(Edge.class);
    Edge edge2 = EasyMock.createMock(Edge.class);

    EasyMock.expect(player.getRoads()).andReturn(Collections.emptyList());
    EasyMock.expect(edge1.isOccupied()).andReturn(false);
    EasyMock.expect(edge1.isConnectedToPlayerNetwork()).andReturn(false);
    EasyMock.replay(player, edge1, edge2);

    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> roadBuildingCard.play(player, edge1, edge2)
    );
    assertEquals("Road must connect to player's existing network.", exception.getMessage());

    EasyMock.verify(player, edge1, edge2);
  }

  @Test // Test Case 9
  public void Play_Edge2Occupied_ExpectIllegalArgumentException() {
    RoadBuildingCard roadBuildingCard = new RoadBuildingCard();

    Player player = EasyMock.createMock(Player.class);
    Edge edge1 = EasyMock.createMock(Edge.class);
    Edge edge2 = EasyMock.createMock(Edge.class);

    EasyMock.expect(player.getRoads()).andReturn(Collections.emptyList());
    EasyMock.expect(edge1.isOccupied()).andReturn(false);
    EasyMock.expect(edge1.isConnectedToPlayerNetwork()).andReturn(true);
    player.placeRoad(edge1);
    EasyMock.expect(edge2.isOccupied()).andReturn(true);
    EasyMock.replay(player, edge1, edge2);

    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> roadBuildingCard.play(player, edge1, edge2)
    );
    assertEquals("Edge is already occupied.", exception.getMessage());

    EasyMock.verify(player, edge1, edge2);
  }

  @Test // Test Case 10
  public void Play_Edge2NotConnectedToNetwork_ExpectIllegalArgumentException() {
    RoadBuildingCard roadBuildingCard = new RoadBuildingCard();

    Player player = EasyMock.createMock(Player.class);
    Edge edge1 = EasyMock.createMock(Edge.class);
    Edge edge2 = EasyMock.createMock(Edge.class);

    EasyMock.expect(player.getRoads()).andReturn(Collections.emptyList());
    EasyMock.expect(edge1.isOccupied()).andReturn(false);
    EasyMock.expect(edge1.isConnectedToPlayerNetwork()).andReturn(true);
    player.placeRoad(edge1);
    EasyMock.expect(edge2.isOccupied()).andReturn(false);
    EasyMock.expect(edge2.isConnectedToPlayerNetwork()).andReturn(false);
    EasyMock.replay(player, edge1, edge2);

    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> roadBuildingCard.play(player, edge1, edge2)
    );
    assertEquals("Road must connect to player's existing network.", exception.getMessage());

    EasyMock.verify(player, edge1, edge2);
  }
}
