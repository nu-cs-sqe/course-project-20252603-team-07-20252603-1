package domain.model.board;

import domain.model.exceptions.EmptyDeckException;
import domain.model.player.Player;
import domain.model.player.PlayerColor;
import domain.model.resources.Resource;
import domain.model.resources.ResourceDeck;
import org.junit.jupiter.api.BeforeEach;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PortTests {

  private BoardHandler mockBoard;
  private Player mockPlayer;
  private ResourceDeck mockGivingDeck;
  private ResourceDeck mockReceivingDeck;

  @BeforeEach
  void setUp() {
    mockBoard = EasyMock.createMock(BoardHandler.class);
    mockPlayer = EasyMock.createMock(Player.class);
    mockGivingDeck = EasyMock.createMock(ResourceDeck.class);
    mockReceivingDeck = EasyMock.createMock(ResourceDeck.class);
  }

  // Test Case 1
  @Test
  void PlayerOwnsNeitherPortNode_ReturnsFalse() {
    Port port = new Port(3, Resource.ANY, List.of(0, 3));

    EasyMock.expect(mockPlayer.getColor()).andReturn(PlayerColor.RED).times(2);
    EasyMock.expect(mockBoard.checkPlayerOwnsNode(PlayerColor.RED, 0)).andReturn(false);
    EasyMock.expect(mockBoard.checkPlayerOwnsNode(PlayerColor.RED, 4)).andReturn(false);

    EasyMock.replay(mockBoard, mockPlayer);

    assertFalse(port.playerCanUsePort(mockBoard, mockPlayer));

    EasyMock.verify(mockBoard, mockPlayer);
  }

  // Test Case 2
  @Test
  void PlayerOwnsFirstPortNode_ReturnsTrue() {
    Port port = new Port(3, Resource.ANY, List.of(0, 3));

    EasyMock.expect(mockPlayer.getColor()).andReturn(PlayerColor.RED).anyTimes();
    EasyMock.expect(mockBoard.checkPlayerOwnsNode(PlayerColor.RED, 0)).andReturn(true);

    EasyMock.replay(mockBoard, mockPlayer);

    assertTrue(port.playerCanUsePort(mockBoard, mockPlayer));

    EasyMock.verify(mockBoard, mockPlayer);
  }

  // Test Case 3
  @Test
  void PlayerOwnsSecondPortNode_ReturnsTrue() {
    Port port = new Port(3, Resource.ANY, List.of(0, 3));

    EasyMock.expect(mockPlayer.getColor()).andReturn(PlayerColor.RED).anyTimes();
    EasyMock.expect(mockBoard.checkPlayerOwnsNode(PlayerColor.RED, 0)).andReturn(false);
    EasyMock.expect(mockBoard.checkPlayerOwnsNode(PlayerColor.RED, 3)).andReturn(true);

    EasyMock.replay(mockBoard, mockPlayer);

    assertTrue(port.playerCanUsePort(mockBoard, mockPlayer));

    EasyMock.verify(mockBoard, mockPlayer);
  }

  // Test Case 4
  @Test
  void RedAtAnyPort_GivesThreeWool_ReceivesOneOre_BankHasNineteen() throws EmptyDeckException {
    Port port = new Port(3, Resource.ANY, List.of(0, 3));

    EasyMock.expect(mockPlayer.getColor()).andReturn(PlayerColor.RED).anyTimes();
    EasyMock.expect(mockBoard.checkPlayerOwnsNode(PlayerColor.RED, 0)).andReturn(true);
    EasyMock.expect(mockPlayer.getResourceCount(Resource.WOOL)).andReturn(3);
    mockPlayer.updateResources(Resource.WOOL, -3);
    EasyMock.expectLastCall();
    mockGivingDeck.replenish(3);
    EasyMock.expectLastCall();
    EasyMock.expect(mockReceivingDeck.draw()).andReturn(Resource.ORE);
    mockPlayer.updateResources(Resource.ORE, 1);
    EasyMock.expectLastCall();

    EasyMock.replay(mockBoard, mockPlayer, mockGivingDeck, mockReceivingDeck);

    port.executePortTrade(mockPlayer, mockBoard, Resource.WOOL, Resource.ORE,
            mockGivingDeck, mockReceivingDeck);

    EasyMock.verify(mockBoard, mockPlayer, mockGivingDeck, mockReceivingDeck);
  }

  // Test Case 5
  @Test
  void RedAtAnyPort_GivesThreeLumber_ReceivesOneGrain() throws EmptyDeckException {
    Port port = new Port(3, Resource.ANY, List.of(0, 3));

    EasyMock.expect(mockPlayer.getColor()).andReturn(PlayerColor.RED).anyTimes();
    EasyMock.expect(mockBoard.checkPlayerOwnsNode(PlayerColor.RED, 0)).andReturn(true);
    EasyMock.expect(mockPlayer.getResourceCount(Resource.LUMBER)).andReturn(3);
    mockPlayer.updateResources(Resource.LUMBER, -3);
    EasyMock.expectLastCall();
    mockGivingDeck.replenish(3);
    EasyMock.expectLastCall();
    EasyMock.expect(mockReceivingDeck.draw()).andReturn(Resource.GRAIN);
    mockPlayer.updateResources(Resource.GRAIN, 1);
    EasyMock.expectLastCall();

    EasyMock.replay(mockBoard, mockPlayer, mockGivingDeck, mockReceivingDeck);

    port.executePortTrade(mockPlayer, mockBoard, Resource.LUMBER, Resource.GRAIN,
            mockGivingDeck, mockReceivingDeck);

    EasyMock.verify(mockBoard, mockPlayer, mockGivingDeck, mockReceivingDeck);
  }

  // Test Case 6
  @Test
  void RedAtWoolPort_GivesTwoWool_ReceivesOneOre() throws EmptyDeckException {
    Port port = new Port(2, Resource.WOOL, List.of(0, 3));

    EasyMock.expect(mockPlayer.getColor()).andReturn(PlayerColor.RED).anyTimes();
    EasyMock.expect(mockBoard.checkPlayerOwnsNode(PlayerColor.RED, 0)).andReturn(true);
    EasyMock.expect(mockPlayer.getResourceCount(Resource.WOOL)).andReturn(2);
    mockPlayer.updateResources(Resource.WOOL, -2);
    EasyMock.expectLastCall();
    mockGivingDeck.replenish(2);
    EasyMock.expectLastCall();
    EasyMock.expect(mockReceivingDeck.draw()).andReturn(Resource.ORE);
    mockPlayer.updateResources(Resource.ORE, 1);
    EasyMock.expectLastCall();

    EasyMock.replay(mockBoard, mockPlayer, mockGivingDeck, mockReceivingDeck);

    port.executePortTrade(mockPlayer, mockBoard, Resource.WOOL, Resource.ORE,
            mockGivingDeck, mockReceivingDeck);

    EasyMock.verify(mockBoard, mockPlayer, mockGivingDeck, mockReceivingDeck);
  }

  // Test Case 7
  @Test
  void RedAtOrePort_GivesTwoOre_ReceivesOneBrick() throws EmptyDeckException {
    Port port = new Port(2, Resource.ORE, List.of(0, 3));

    EasyMock.expect(mockPlayer.getColor()).andReturn(PlayerColor.RED).anyTimes();
    EasyMock.expect(mockBoard.checkPlayerOwnsNode(PlayerColor.RED, 0)).andReturn(true);
    EasyMock.expect(mockPlayer.getResourceCount(Resource.ORE)).andReturn(2);
    mockPlayer.updateResources(Resource.ORE, -2);
    EasyMock.expectLastCall();
    mockGivingDeck.replenish(2);
    EasyMock.expectLastCall();
    EasyMock.expect(mockReceivingDeck.draw()).andReturn(Resource.BRICK);
    mockPlayer.updateResources(Resource.BRICK, 1);
    EasyMock.expectLastCall();

    EasyMock.replay(mockBoard, mockPlayer, mockGivingDeck, mockReceivingDeck);

    port.executePortTrade(mockPlayer, mockBoard, Resource.ORE, Resource.BRICK,
            mockGivingDeck, mockReceivingDeck);

    EasyMock.verify(mockBoard, mockPlayer, mockGivingDeck, mockReceivingDeck);
  }

  // Test Case 8
  @Test
  void RedAtLumberPort_GivesTwoLumber_ReceivesOneGrain() throws EmptyDeckException {
    Port port = new Port(2, Resource.LUMBER, List.of(0, 3));

    EasyMock.expect(mockPlayer.getColor()).andReturn(PlayerColor.RED).anyTimes();
    EasyMock.expect(mockBoard.checkPlayerOwnsNode(PlayerColor.RED, 0)).andReturn(true);
    EasyMock.expect(mockPlayer.getResourceCount(Resource.LUMBER)).andReturn(2);
    mockPlayer.updateResources(Resource.LUMBER, -2);
    EasyMock.expectLastCall();
    mockGivingDeck.replenish(2);
    EasyMock.expectLastCall();
    EasyMock.expect(mockReceivingDeck.draw()).andReturn(Resource.GRAIN);
    mockPlayer.updateResources(Resource.GRAIN, 1);
    EasyMock.expectLastCall();

    EasyMock.replay(mockBoard, mockPlayer, mockGivingDeck, mockReceivingDeck);

    port.executePortTrade(mockPlayer, mockBoard, Resource.LUMBER, Resource.GRAIN,
            mockGivingDeck, mockReceivingDeck);

    EasyMock.verify(mockBoard, mockPlayer, mockGivingDeck, mockReceivingDeck);
  }

  // Test Case 9
  @Test
  void RedAtGrainPort_GivesTwoGrain_ReceivesOneWool() throws EmptyDeckException {
    Port port = new Port(2, Resource.GRAIN, List.of(0, 3));

    EasyMock.expect(mockPlayer.getColor()).andReturn(PlayerColor.RED).anyTimes();
    EasyMock.expect(mockBoard.checkPlayerOwnsNode(PlayerColor.RED, 0)).andReturn(true);
    EasyMock.expect(mockPlayer.getResourceCount(Resource.GRAIN)).andReturn(2);
    mockPlayer.updateResources(Resource.GRAIN, -2);
    EasyMock.expectLastCall();
    mockGivingDeck.replenish(2);
    EasyMock.expectLastCall();
    EasyMock.expect(mockReceivingDeck.draw()).andReturn(Resource.WOOL);
    mockPlayer.updateResources(Resource.WOOL, 1);
    EasyMock.expectLastCall();

    EasyMock.replay(mockBoard, mockPlayer, mockGivingDeck, mockReceivingDeck);

    port.executePortTrade(mockPlayer, mockBoard, Resource.GRAIN, Resource.WOOL,
            mockGivingDeck, mockReceivingDeck);

    EasyMock.verify(mockBoard, mockPlayer, mockGivingDeck, mockReceivingDeck);
  }

  // Test Case 10
  @Test
  void RedAtBrickPort_GivesTwoBrick_ReceivesOneLumber() throws EmptyDeckException {
    Port port = new Port(2, Resource.BRICK, List.of(0, 3));

    EasyMock.expect(mockPlayer.getColor()).andReturn(PlayerColor.RED).anyTimes();
    EasyMock.expect(mockBoard.checkPlayerOwnsNode(PlayerColor.RED, 0)).andReturn(true);
    EasyMock.expect(mockPlayer.getResourceCount(Resource.BRICK)).andReturn(2);
    mockPlayer.updateResources(Resource.BRICK, -2);
    EasyMock.expectLastCall();
    mockGivingDeck.replenish(2);
    EasyMock.expectLastCall();
    EasyMock.expect(mockReceivingDeck.draw()).andReturn(Resource.LUMBER);
    mockPlayer.updateResources(Resource.LUMBER, 1);
    EasyMock.expectLastCall();

    EasyMock.replay(mockBoard, mockPlayer, mockGivingDeck, mockReceivingDeck);

    port.executePortTrade(mockPlayer, mockBoard, Resource.BRICK, Resource.LUMBER,
            mockGivingDeck, mockReceivingDeck);

    EasyMock.verify(mockBoard, mockPlayer, mockGivingDeck, mockReceivingDeck);
  }

  // Test Case 11
  @Test
  void RedAtWoolPort_TriesToGiveOre_ThrowsException() {
    Port port = new Port(2, Resource.WOOL, List.of(0, 3));

    EasyMock.expect(mockPlayer.getColor()).andReturn(PlayerColor.RED).anyTimes();
    EasyMock.expect(mockBoard.checkPlayerOwnsNode(PlayerColor.RED, 0)).andReturn(true);

    EasyMock.replay(mockBoard, mockPlayer, mockGivingDeck, mockReceivingDeck);

    Exception exception = assertThrows(IllegalArgumentException.class, () ->
            port.executePortTrade(mockPlayer, mockBoard, Resource.ORE, Resource.WOOL,
                    mockGivingDeck, mockReceivingDeck));

    assertEquals("This port only accepts WOOL for 2:1 trades.", exception.getMessage());

    EasyMock.verify(mockBoard, mockPlayer, mockGivingDeck, mockReceivingDeck);
  }

  // Test Case 12
  @Test
  void RedAtAnyPort_TradesWoolForWool_ThrowsException() {
    Port port = new Port(3, Resource.ANY, List.of(0, 3));

    EasyMock.expect(mockPlayer.getColor()).andReturn(PlayerColor.RED).anyTimes();
    EasyMock.expect(mockBoard.checkPlayerOwnsNode(PlayerColor.RED, 0)).andReturn(true);

    EasyMock.replay(mockBoard, mockPlayer, mockGivingDeck, mockReceivingDeck);

    Exception exception = assertThrows(IllegalArgumentException.class, () ->
            port.executePortTrade(mockPlayer, mockBoard, Resource.WOOL, Resource.WOOL,
                    mockGivingDeck, mockReceivingDeck));

    assertEquals("Cannot trade a resource for itself.", exception.getMessage());

    EasyMock.verify(mockBoard, mockPlayer, mockGivingDeck, mockReceivingDeck);
  }

  // Test Case 13
  @Test
  void RedAtWoolPort_HasOneWool_TriesToGiveTwo_ThrowsException() {
    Port port = new Port(2, Resource.WOOL, List.of(0, 4));

    EasyMock.expect(mockPlayer.getColor()).andReturn(PlayerColor.RED).anyTimes();
    EasyMock.expect(mockBoard.checkPlayerOwnsNode(PlayerColor.RED, 0)).andReturn(true);
    EasyMock.expect(mockPlayer.getResourceCount(Resource.WOOL)).andReturn(1);

    EasyMock.replay(mockBoard, mockPlayer, mockGivingDeck, mockReceivingDeck);

    Exception exception = assertThrows(IllegalStateException.class, () ->
            port.executePortTrade(mockPlayer, mockBoard, Resource.WOOL, Resource.ORE,
                    mockGivingDeck, mockReceivingDeck));

    assertEquals("Player has insufficient resources for this trade.", exception.getMessage());

    EasyMock.verify(mockBoard, mockPlayer, mockGivingDeck, mockReceivingDeck);
  }

  // Test Case 14
  @Test
  void RedDoesNotOwnAdjacentNode_ThrowsException() {
    Port port = new Port(3, Resource.ANY, List.of(0, 4));

    EasyMock.expect(mockPlayer.getColor()).andReturn(PlayerColor.RED).anyTimes();
    EasyMock.expect(mockBoard.checkPlayerOwnsNode(PlayerColor.RED, 0)).andReturn(false);
    EasyMock.expect(mockBoard.checkPlayerOwnsNode(PlayerColor.RED, 4)).andReturn(false);

    EasyMock.replay(mockBoard, mockPlayer, mockGivingDeck, mockReceivingDeck);

    Exception exception = assertThrows(IllegalStateException.class, () ->
            port.executePortTrade(mockPlayer, mockBoard, Resource.WOOL, Resource.ORE,
                    mockGivingDeck, mockReceivingDeck));

    assertEquals("Player does not have access to this port.", exception.getMessage());

    EasyMock.verify(mockBoard, mockPlayer, mockGivingDeck, mockReceivingDeck);
  }

  // Test Case 15
  @Test
  void RedAtAnyPort_BankHasZeroOre_ThrowsEmptyDeckException() throws EmptyDeckException {
    Port port = new Port(3, Resource.ANY, List.of(0, 4));

    EasyMock.expect(mockPlayer.getColor()).andReturn(PlayerColor.RED).anyTimes();
    EasyMock.expect(mockBoard.checkPlayerOwnsNode(PlayerColor.RED, 0)).andReturn(true);
    EasyMock.expect(mockPlayer.getResourceCount(Resource.WOOL)).andReturn(3);
    mockPlayer.updateResources(Resource.WOOL, -3);
    EasyMock.expectLastCall();
    mockGivingDeck.replenish(3);
    EasyMock.expectLastCall();
    EasyMock.expect(mockReceivingDeck.draw()).andThrow(
            new EmptyDeckException("Cannot draw new ORE card, no cards remain."));
    mockPlayer.updateResources(Resource.WOOL, 3);
    EasyMock.expectLastCall();
    EasyMock.expect(mockGivingDeck.drawMultiple(3)).andReturn(3);

    EasyMock.replay(mockBoard, mockPlayer, mockGivingDeck, mockReceivingDeck);

    Exception exception = assertThrows(EmptyDeckException.class, () ->
            port.executePortTrade(mockPlayer, mockBoard, Resource.WOOL, Resource.ORE,
                    mockGivingDeck, mockReceivingDeck));

    assertEquals("Cannot draw new ORE card, no cards remain.", exception.getMessage());

    EasyMock.verify(mockBoard, mockPlayer, mockGivingDeck, mockReceivingDeck);
  }

  // Test Case 16
  @Test
  void RedAtAnyPort_BankHasOneOre_TradeSucceeds() throws EmptyDeckException {
    Port port = new Port(3, Resource.ANY, List.of(0, 4));

    EasyMock.expect(mockPlayer.getColor()).andReturn(PlayerColor.RED).anyTimes();
    EasyMock.expect(mockBoard.checkPlayerOwnsNode(PlayerColor.RED, 0)).andReturn(true);
    EasyMock.expect(mockPlayer.getResourceCount(Resource.WOOL)).andReturn(3);
    mockPlayer.updateResources(Resource.WOOL, -3);
    EasyMock.expectLastCall();
    mockGivingDeck.replenish(3);
    EasyMock.expectLastCall();
    EasyMock.expect(mockReceivingDeck.draw()).andReturn(Resource.ORE);
    mockPlayer.updateResources(Resource.ORE, 1);
    EasyMock.expectLastCall();

    EasyMock.replay(mockBoard, mockPlayer, mockGivingDeck, mockReceivingDeck);

    port.executePortTrade(mockPlayer, mockBoard, Resource.WOOL, Resource.ORE,
            mockGivingDeck, mockReceivingDeck);

    EasyMock.verify(mockBoard, mockPlayer, mockGivingDeck, mockReceivingDeck);
  }

  // Test Case 17
  @Test
  void RedAtAnyPort_PlayerHasNineteenWool_TradeSucceeds() throws EmptyDeckException {
    Port port = new Port(3, Resource.ANY, List.of(0, 4));

    EasyMock.expect(mockPlayer.getColor()).andReturn(PlayerColor.RED).anyTimes();
    EasyMock.expect(mockBoard.checkPlayerOwnsNode(PlayerColor.RED, 0)).andReturn(true);
    EasyMock.expect(mockPlayer.getResourceCount(Resource.WOOL)).andReturn(19);
    mockPlayer.updateResources(Resource.WOOL, -3);
    EasyMock.expectLastCall();
    mockGivingDeck.replenish(3);
    EasyMock.expectLastCall();
    EasyMock.expect(mockReceivingDeck.draw()).andReturn(Resource.ORE);
    mockPlayer.updateResources(Resource.ORE, 1);
    EasyMock.expectLastCall();

    EasyMock.replay(mockBoard, mockPlayer, mockGivingDeck, mockReceivingDeck);

    port.executePortTrade(mockPlayer, mockBoard, Resource.WOOL, Resource.ORE,
            mockGivingDeck, mockReceivingDeck);

    EasyMock.verify(mockBoard, mockPlayer, mockGivingDeck, mockReceivingDeck);
  }


}
