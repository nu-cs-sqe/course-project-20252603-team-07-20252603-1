package domain;

import java.util.List;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MonopolyCardTests {

  @Test // Test Case 1
  public void Play_NullResource_ExpectIllegalArgumentException() {
    MonopolyCard monopolyCard = new MonopolyCard();

    Player player = EasyMock.createMock(Player.class);
    EasyMock.replay(player);

    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> monopolyCard.play(null, List.of(player))
    );
    assertEquals("Resource cannot be null.", exception.getMessage());
  }
}
