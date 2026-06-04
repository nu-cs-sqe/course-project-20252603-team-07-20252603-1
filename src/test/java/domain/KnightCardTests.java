package domain;

import java.util.Map;
import java.util.Random;

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

    Player player = EasyMock.createMock(Player.class);
    EasyMock.replay(player);

    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> knightCard.play(player, null, 5, victim)
    );
    assertEquals("Robber cannot be null.", exception.getMessage());
  }

  @Test // Test Case 2
  public void Play_ValidTargetWithVictimResources_RobberMovesAndResourceTransferred() {
    KnightCard knightCard = new KnightCard(new Random(0));
    Player player = EasyMock.createMock(Player.class);

    Robber robber = new Robber(0);
    Player victim = EasyMock.createMock(Player.class);

    EasyMock.expect(victim.getResources()).andReturn(Map.of(Resource.LUMBER, 3));
    victim.updateResources(Resource.LUMBER, -1);
    EasyMock.expectLastCall();

    player.receiveResources(Map.of(Resource.LUMBER, 1));
    EasyMock.expectLastCall();

    EasyMock.replay(victim, player);

    knightCard.play(player, robber, 5, victim);

    assertEquals(5, robber.getRobberLocation());
    EasyMock.verify(victim, player);
  }
}
