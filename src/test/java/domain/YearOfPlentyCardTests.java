package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class YearOfPlentyCardTests {

  @Test // Test Case 1
  public void Play_NullPlayer_ExpectIllegalArgumentException() {
    YearOfPlentyCard yearOfPlentyCard = new YearOfPlentyCard();

    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> yearOfPlentyCard.play(null, Resource.BRICK, Resource.LUMBER)
    );
    assertEquals("Player cannot be null.", exception.getMessage());
  }
}
