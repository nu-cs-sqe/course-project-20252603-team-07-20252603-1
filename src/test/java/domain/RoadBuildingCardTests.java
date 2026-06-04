package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

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
}
