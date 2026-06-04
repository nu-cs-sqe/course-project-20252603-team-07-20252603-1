package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class KnightCardTests {

  @Test // Test Case 1
  public void Play_NullRobber_ExpectIllegalArgumentException() {
    KnightCard knightCard = new KnightCard();
    
    Player victim = EasyMock.createMock(Player.class);
    EasyMock.replay(victim);

    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> knightCard.play(null, 5, victim)
    );
    assertEquals("Robber cannot be null.", exception.getMessage());
  }
}
