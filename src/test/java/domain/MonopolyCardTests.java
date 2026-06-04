package domain;

import java.util.List;
import java.util.Map;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MonopolyCardTests {

  @Test // Test Case 1
  public void Play_NullResource_ExpectIllegalArgumentException() {
    MonopolyCard monopolyCard = new MonopolyCard();

    Player player = EasyMock.createMock(Player.class);
    Player otherPlayer = EasyMock.createMock(Player.class);
    EasyMock.replay(player, otherPlayer);

    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> monopolyCard.play(player, null, List.of(otherPlayer))
    );
    assertEquals("Resource cannot be null.", exception.getMessage());
  }

  @Test // Test Case 2
  public void Play_DesertResource_ExpectIllegalArgumentException() {
    MonopolyCard monopolyCard = new MonopolyCard();

    Player player = EasyMock.createMock(Player.class);
    Player otherPlayer = EasyMock.createMock(Player.class);
    EasyMock.replay(player, otherPlayer);

    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> monopolyCard.play(player, Resource.DESERT, List.of(otherPlayer))
    );
    assertEquals("Cannot monopolize DESERT.", exception.getMessage());
  }

  @Test // Test Case 3
  public void Play_NullOtherPlayers_ExpectIllegalArgumentException() {
    MonopolyCard monopolyCard = new MonopolyCard();

    Player player = EasyMock.createMock(Player.class);
    EasyMock.replay(player);

    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> monopolyCard.play(player, Resource.BRICK, null)
    );
    assertEquals("Other players list cannot be null.", exception.getMessage());
  }

  @Test // Test Case 4
  public void Play_EmptyOtherPlayersList_ExpectNoResourcesTransferred() {
    MonopolyCard monopolyCard = new MonopolyCard();

    Player player = EasyMock.createMock(Player.class);
    EasyMock.replay(player);

    monopolyCard.play(player, Resource.BRICK, List.of());

    EasyMock.verify(player);
  }

  @Test // Test Case 5
  public void Play_OneOpponentWithFiveOre_ExpectTransferFiveOre() {
    MonopolyCard monopolyCard = new MonopolyCard();

    Player player = EasyMock.createMock(Player.class);
    Player opponent = EasyMock.createMock(Player.class);

    EasyMock.expect(opponent.getResourceCount(Resource.ORE)).andReturn(5);
    opponent.updateResources(Resource.ORE, -5);
    player.receiveResources(Map.of(Resource.ORE, 5));
    EasyMock.replay(player, opponent);

    monopolyCard.play(player, Resource.ORE, List.of(opponent));

    EasyMock.verify(player, opponent);
  }
}
