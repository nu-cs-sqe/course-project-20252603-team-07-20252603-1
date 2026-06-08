package domain;

import org.easymock.EasyMock;
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

  @Test // Test Case 2
  public void Play_NullResource1_ExpectIllegalArgumentException() {
    YearOfPlentyCard yearOfPlentyCard = new YearOfPlentyCard();

    Player player = EasyMock.createMock(Player.class);
    EasyMock.replay(player);

    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> yearOfPlentyCard.play(player, null, Resource.BRICK)
    );
    assertEquals("Resource cannot be null.", exception.getMessage());

    EasyMock.verify(player);
  }

  @Test // Test Case 3
  public void Play_NullResource2_ExpectIllegalArgumentException() {
    YearOfPlentyCard yearOfPlentyCard = new YearOfPlentyCard();

    Player player = EasyMock.createMock(Player.class);
    EasyMock.replay(player);

    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> yearOfPlentyCard.play(player, Resource.BRICK, null)
    );
    assertEquals("Resource cannot be null.", exception.getMessage());

    EasyMock.verify(player);
  }

  @Test // Test Case 4
  public void Play_DesertResource1_ExpectIllegalArgumentException() {
    YearOfPlentyCard yearOfPlentyCard = new YearOfPlentyCard();

    Player player = EasyMock.createMock(Player.class);
    EasyMock.replay(player);

    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> yearOfPlentyCard.play(player, Resource.DESERT, Resource.ORE)
    );
    assertEquals("Cannot take DESERT as a resource.", exception.getMessage());

    EasyMock.verify(player);
  }

  @Test // Test Case 5
  public void Play_DesertResource2_ExpectIllegalArgumentException() {
    YearOfPlentyCard yearOfPlentyCard = new YearOfPlentyCard();

    Player player = EasyMock.createMock(Player.class);
    EasyMock.replay(player);

    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> yearOfPlentyCard.play(player, Resource.LUMBER, Resource.DESERT)
    );
    assertEquals("Cannot take DESERT as a resource.", exception.getMessage());

    EasyMock.verify(player);
  }
}
