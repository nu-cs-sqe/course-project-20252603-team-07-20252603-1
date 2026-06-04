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
    player.placeRoad(edge1);
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
    player.placeRoad(edge1);
    player.placeRoad(edge2);
    EasyMock.replay(player, edge1, edge2);

    roadBuildingCard.play(player, edge1, edge2);

    EasyMock.verify(player, edge1, edge2);
  }
}
