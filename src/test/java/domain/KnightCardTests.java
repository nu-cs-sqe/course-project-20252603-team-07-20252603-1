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
    Robber robber = EasyMock.createMock(Robber.class);
    Player victim = EasyMock.createMock(Player.class);

    EasyMock.expect(robber.getRobberLocation()).andReturn(0);
    robber.moveRobber(5);
    EasyMock.expectLastCall();

    EasyMock.expect(victim.getResources()).andReturn(Map.of(Resource.LUMBER, 3));
    victim.updateResources(Resource.LUMBER, -1);
    EasyMock.expectLastCall();

    player.receiveResources(Map.of(Resource.LUMBER, 1));
    EasyMock.expectLastCall();

    EasyMock.replay(robber, victim, player);

    knightCard.play(player, robber, 5, victim);

    EasyMock.verify(robber, victim, player);
  }

  @Test // Test Case 3
  public void Play_LowBoundaryTarget_RobberMovesAndResourceTransferred() {
    KnightCard knightCard = new KnightCard(new Random(0));
    
    Player player = EasyMock.createMock(Player.class);
    Robber robber = EasyMock.createMock(Robber.class);
    Player victim = EasyMock.createMock(Player.class);

    EasyMock.expect(robber.getRobberLocation()).andReturn(5);
    robber.moveRobber(0);
    EasyMock.expectLastCall();

    EasyMock.expect(victim.getResources()).andReturn(Map.of(Resource.LUMBER, 2));
    victim.updateResources(Resource.LUMBER, -1);
    EasyMock.expectLastCall();

    player.receiveResources(Map.of(Resource.LUMBER, 1));
    EasyMock.expectLastCall();

    EasyMock.replay(robber, victim, player);

    knightCard.play(player, robber, 0, victim);

    EasyMock.verify(robber, victim, player);
  }

  @Test // Test Case 4
  public void Play_HighBoundaryTarget_RobberMovesAndResourceTransferred() {
    KnightCard knightCard = new KnightCard(new Random(0));

    Player player = EasyMock.createMock(Player.class);
    Robber robber = EasyMock.createMock(Robber.class);
    Player victim = EasyMock.createMock(Player.class);

    EasyMock.expect(robber.getRobberLocation()).andReturn(5);
    robber.moveRobber(18);
    EasyMock.expectLastCall();

    EasyMock.expect(victim.getResources()).andReturn(Map.of(Resource.LUMBER, 2));
    victim.updateResources(Resource.LUMBER, -1);
    EasyMock.expectLastCall();

    player.receiveResources(Map.of(Resource.LUMBER, 1));
    EasyMock.expectLastCall();

    EasyMock.replay(robber, victim, player);

    knightCard.play(player, robber, 18, victim);

    EasyMock.verify(robber, victim, player);
  }

  @Test // Test Case 5
  public void Play_BelowLowBoundaryTarget_ExpectIllegalArgumentException() {
    KnightCard knightCard = new KnightCard();

    Player player = EasyMock.createMock(Player.class);
    Robber robber = EasyMock.createMock(Robber.class);
    Player victim = EasyMock.createMock(Player.class);

    EasyMock.expect(robber.getRobberLocation()).andReturn(5);
    robber.moveRobber(-1);
    EasyMock.expectLastCall().andThrow(
        new IllegalArgumentException("Cannot move Robber to invalid HexId"));

    EasyMock.replay(player, robber, victim);

    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> knightCard.play(player, robber, -1, victim)
    );
    assertEquals("Cannot move Robber to invalid HexId", exception.getMessage());
    EasyMock.verify(player, robber, victim);
  }

  @Test // Test Case 6
  public void Play_AboveHighBoundaryTarget_ExpectIllegalArgumentException() {
    KnightCard knightCard = new KnightCard();

    Player player = EasyMock.createMock(Player.class);
    Robber robber = EasyMock.createMock(Robber.class);
    Player victim = EasyMock.createMock(Player.class);

    EasyMock.expect(robber.getRobberLocation()).andReturn(5);
    robber.moveRobber(19);
    EasyMock.expectLastCall().andThrow(
        new IllegalArgumentException("Cannot move Robber to invalid HexId"));

    EasyMock.replay(player, robber, victim);

    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> knightCard.play(player, robber, 19, victim)
    );
    assertEquals("Cannot move Robber to invalid HexId", exception.getMessage());
    EasyMock.verify(player, robber, victim);
  }

  @Test // Test Case 7
  public void Play_SameHexTarget_ExpectIllegalArgumentException() {
    KnightCard knightCard = new KnightCard();

    Player player = EasyMock.createMock(Player.class);
    Robber robber = EasyMock.createMock(Robber.class);
    Player victim = EasyMock.createMock(Player.class);

    EasyMock.expect(robber.getRobberLocation()).andReturn(5);

    EasyMock.replay(player, robber, victim);

    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> knightCard.play(player, robber, 5, victim)
    );
    assertEquals("Must move robber to a different hex.", exception.getMessage());
    EasyMock.verify(player, robber, victim);
  }

  @Test // Test Case 8
  public void Play_ValidTargetWithNullVictim_RobberMovesNoResourceStolen() {
    KnightCard knightCard = new KnightCard();

    Player player = EasyMock.createMock(Player.class);
    Robber robber = EasyMock.createMock(Robber.class);

    EasyMock.expect(robber.getRobberLocation()).andReturn(0);
    robber.moveRobber(5);
    EasyMock.expectLastCall();

    EasyMock.replay(player, robber);

    knightCard.play(player, robber, 5, null);

    EasyMock.verify(robber, player);
  }


  @Test // Test Case 9
  public void Play_ValidTargetWithVictimZeroResources_RobberMovesNoResourceStolen() {
    KnightCard knightCard = new KnightCard();

    Player player = EasyMock.createMock(Player.class);
    Robber robber = EasyMock.createMock(Robber.class);
    Player victim = EasyMock.createMock(Player.class);

    EasyMock.expect(robber.getRobberLocation()).andReturn(0);
    robber.moveRobber(5);
    EasyMock.expectLastCall();

    EasyMock.expect(victim.getResources()).andReturn(Map.of());

    EasyMock.replay(player, robber, victim);

    knightCard.play(player, robber, 5, victim);

    EasyMock.verify(robber, player, victim);
  }
}
