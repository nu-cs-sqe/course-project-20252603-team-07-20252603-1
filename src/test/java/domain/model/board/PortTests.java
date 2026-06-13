package domain.model.board;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import domain.model.exceptions.EmptyDeckException;
import domain.model.player.Player;
import domain.model.player.PlayerColor;
import domain.model.resources.Resource;
import domain.model.resources.ResourceDeck;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Test class. */
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

  private PortTradeRequest buildRequest(Resource giving, Resource receiving) {
    Map<Resource, ResourceDeck> decks = new HashMap<>();
    decks.put(giving, mockGivingDeck);
    decks.put(receiving, mockReceivingDeck);
    return new PortTradeRequest(giving, receiving, decks);
  }

  private PortTradeRequest buildRequestSameResource(Resource resource) {
    Map<Resource, ResourceDeck> decks = new HashMap<>();
    decks.put(resource, mockGivingDeck);
    return new PortTradeRequest(resource, resource, decks);
  }

  // Test Case 1
  @Test
  void PlayerOwnsNeitherPortNode_ReturnsFalse() {
    final Port port = new Port(3, Resource.ANY, List.of(0, 3));

    EasyMock.expect(mockPlayer.getColor()).andReturn(PlayerColor.RED).times(2);
    EasyMock.expect(mockBoard.checkPlayerOwnsNode(PlayerColor.RED, 0)).andReturn(false);
    EasyMock.expect(mockBoard.checkPlayerOwnsNode(PlayerColor.RED, 3)).andReturn(false);

    EasyMock.replay(mockBoard, mockPlayer);

    assertFalse(port.playerCanUsePort(mockBoard, mockPlayer));

    EasyMock.verify(mockBoard, mockPlayer);
  }

  // Test Case 2
  @Test
  void PlayerOwnsFirstPortNode_ReturnsTrue() {
    final Port port = new Port(3, Resource.ANY, List.of(0, 3));

    EasyMock.expect(mockPlayer.getColor()).andReturn(PlayerColor.RED).anyTimes();
    EasyMock.expect(mockBoard.checkPlayerOwnsNode(PlayerColor.RED, 0)).andReturn(true);

    EasyMock.replay(mockBoard, mockPlayer);

    assertTrue(port.playerCanUsePort(mockBoard, mockPlayer));

    EasyMock.verify(mockBoard, mockPlayer);
  }

  // Test Case 3
  @Test
  void PlayerOwnsSecondPortNode_ReturnsTrue() {
    final Port port = new Port(3, Resource.ANY, List.of(0, 3));

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
    final Port port = new Port(3, Resource.ANY, List.of(0, 3));

    EasyMock.expect(mockPlayer.getColor()).andReturn(PlayerColor.RED).anyTimes();
    EasyMock.expect(mockBoard.checkPlayerOwnsNode(PlayerColor.RED, 0)).andReturn(true);
    EasyMock.expect(mockPlayer.getResourceCount(Resource.WOOL)).andReturn(3);
    EasyMock.expect(mockReceivingDeck.getTotalCards()).andReturn(19);
    mockPlayer.updateResources(Resource.WOOL, -3);
    EasyMock.expectLastCall();
    mockGivingDeck.replenish(3);
    EasyMock.expectLastCall();
    EasyMock.expect(mockReceivingDeck.draw()).andReturn(Resource.ORE);
    mockPlayer.updateResources(Resource.ORE, 1);
    EasyMock.expectLastCall();

    EasyMock.replay(mockBoard, mockPlayer, mockGivingDeck, mockReceivingDeck);

    port.executePortTrade(mockPlayer, mockBoard,
        buildRequest(Resource.WOOL, Resource.ORE));

    EasyMock.verify(mockBoard, mockPlayer, mockGivingDeck, mockReceivingDeck);
  }

  // Test Case 5
  @Test
  void RedAtAnyPort_GivesThreeLumber_ReceivesOneGrain() throws EmptyDeckException {
    final Port port = new Port(3, Resource.ANY, List.of(0, 3));

    EasyMock.expect(mockPlayer.getColor()).andReturn(PlayerColor.RED).anyTimes();
    EasyMock.expect(mockBoard.checkPlayerOwnsNode(PlayerColor.RED, 0)).andReturn(true);
    EasyMock.expect(mockPlayer.getResourceCount(Resource.LUMBER)).andReturn(3);
    EasyMock.expect(mockReceivingDeck.getTotalCards()).andReturn(19);
    mockPlayer.updateResources(Resource.LUMBER, -3);
    EasyMock.expectLastCall();
    mockGivingDeck.replenish(3);
    EasyMock.expectLastCall();
    EasyMock.expect(mockReceivingDeck.draw()).andReturn(Resource.GRAIN);
    mockPlayer.updateResources(Resource.GRAIN, 1);
    EasyMock.expectLastCall();

    EasyMock.replay(mockBoard, mockPlayer, mockGivingDeck, mockReceivingDeck);

    port.executePortTrade(mockPlayer, mockBoard,
        buildRequest(Resource.LUMBER, Resource.GRAIN));

    EasyMock.verify(mockBoard, mockPlayer, mockGivingDeck, mockReceivingDeck);
  }

  // Test Case 6
  @Test
  void RedAtWoolPort_GivesTwoWool_ReceivesOneOre() throws EmptyDeckException {
    final Port port = new Port(2, Resource.WOOL, List.of(0, 3));

    EasyMock.expect(mockPlayer.getColor()).andReturn(PlayerColor.RED).anyTimes();
    EasyMock.expect(mockBoard.checkPlayerOwnsNode(PlayerColor.RED, 0)).andReturn(true);
    EasyMock.expect(mockPlayer.getResourceCount(Resource.WOOL)).andReturn(2);
    EasyMock.expect(mockReceivingDeck.getTotalCards()).andReturn(19);
    mockPlayer.updateResources(Resource.WOOL, -2);
    EasyMock.expectLastCall();
    mockGivingDeck.replenish(2);
    EasyMock.expectLastCall();
    EasyMock.expect(mockReceivingDeck.draw()).andReturn(Resource.ORE);
    mockPlayer.updateResources(Resource.ORE, 1);
    EasyMock.expectLastCall();

    EasyMock.replay(mockBoard, mockPlayer, mockGivingDeck, mockReceivingDeck);

    port.executePortTrade(mockPlayer, mockBoard,
        buildRequest(Resource.WOOL, Resource.ORE));

    EasyMock.verify(mockBoard, mockPlayer, mockGivingDeck, mockReceivingDeck);
  }

  // Test Case 7
  @Test
  void RedAtOrePort_GivesTwoOre_ReceivesOneBrick() throws EmptyDeckException {
    final Port port = new Port(2, Resource.ORE, List.of(0, 3));

    EasyMock.expect(mockPlayer.getColor()).andReturn(PlayerColor.RED).anyTimes();
    EasyMock.expect(mockBoard.checkPlayerOwnsNode(PlayerColor.RED, 0)).andReturn(true);
    EasyMock.expect(mockPlayer.getResourceCount(Resource.ORE)).andReturn(2);
    EasyMock.expect(mockReceivingDeck.getTotalCards()).andReturn(19);
    mockPlayer.updateResources(Resource.ORE, -2);
    EasyMock.expectLastCall();
    mockGivingDeck.replenish(2);
    EasyMock.expectLastCall();
    EasyMock.expect(mockReceivingDeck.draw()).andReturn(Resource.BRICK);
    mockPlayer.updateResources(Resource.BRICK, 1);
    EasyMock.expectLastCall();

    EasyMock.replay(mockBoard, mockPlayer, mockGivingDeck, mockReceivingDeck);

    port.executePortTrade(mockPlayer, mockBoard,
        buildRequest(Resource.ORE, Resource.BRICK));

    EasyMock.verify(mockBoard, mockPlayer, mockGivingDeck, mockReceivingDeck);
  }

  // Test Case 8
  @Test
  void RedAtLumberPort_GivesTwoLumber_ReceivesOneGrain() throws EmptyDeckException {
    final Port port = new Port(2, Resource.LUMBER, List.of(0, 3));

    EasyMock.expect(mockPlayer.getColor()).andReturn(PlayerColor.RED).anyTimes();
    EasyMock.expect(mockBoard.checkPlayerOwnsNode(PlayerColor.RED, 0)).andReturn(true);
    EasyMock.expect(mockPlayer.getResourceCount(Resource.LUMBER)).andReturn(2);
    EasyMock.expect(mockReceivingDeck.getTotalCards()).andReturn(19);
    mockPlayer.updateResources(Resource.LUMBER, -2);
    EasyMock.expectLastCall();
    mockGivingDeck.replenish(2);
    EasyMock.expectLastCall();
    EasyMock.expect(mockReceivingDeck.draw()).andReturn(Resource.GRAIN);
    mockPlayer.updateResources(Resource.GRAIN, 1);
    EasyMock.expectLastCall();

    EasyMock.replay(mockBoard, mockPlayer, mockGivingDeck, mockReceivingDeck);

    port.executePortTrade(mockPlayer, mockBoard,
        buildRequest(Resource.LUMBER, Resource.GRAIN));

    EasyMock.verify(mockBoard, mockPlayer, mockGivingDeck, mockReceivingDeck);
  }

  // Test Case 9
  @Test
  void RedAtGrainPort_GivesTwoGrain_ReceivesOneWool() throws EmptyDeckException {
    final Port port = new Port(2, Resource.GRAIN, List.of(0, 3));

    EasyMock.expect(mockPlayer.getColor()).andReturn(PlayerColor.RED).anyTimes();
    EasyMock.expect(mockBoard.checkPlayerOwnsNode(PlayerColor.RED, 0)).andReturn(true);
    EasyMock.expect(mockPlayer.getResourceCount(Resource.GRAIN)).andReturn(2);
    EasyMock.expect(mockReceivingDeck.getTotalCards()).andReturn(19);
    mockPlayer.updateResources(Resource.GRAIN, -2);
    EasyMock.expectLastCall();
    mockGivingDeck.replenish(2);
    EasyMock.expectLastCall();
    EasyMock.expect(mockReceivingDeck.draw()).andReturn(Resource.WOOL);
    mockPlayer.updateResources(Resource.WOOL, 1);
    EasyMock.expectLastCall();

    EasyMock.replay(mockBoard, mockPlayer, mockGivingDeck, mockReceivingDeck);

    port.executePortTrade(mockPlayer, mockBoard,
        buildRequest(Resource.GRAIN, Resource.WOOL));

    EasyMock.verify(mockBoard, mockPlayer, mockGivingDeck, mockReceivingDeck);
  }

  // Test Case 10
  @Test
  void RedAtBrickPort_GivesTwoBrick_ReceivesOneLumber() throws EmptyDeckException {
    final Port port = new Port(2, Resource.BRICK, List.of(0, 3));

    EasyMock.expect(mockPlayer.getColor()).andReturn(PlayerColor.RED).anyTimes();
    EasyMock.expect(mockBoard.checkPlayerOwnsNode(PlayerColor.RED, 0)).andReturn(true);
    EasyMock.expect(mockPlayer.getResourceCount(Resource.BRICK)).andReturn(2);
    EasyMock.expect(mockReceivingDeck.getTotalCards()).andReturn(19);
    mockPlayer.updateResources(Resource.BRICK, -2);
    EasyMock.expectLastCall();
    mockGivingDeck.replenish(2);
    EasyMock.expectLastCall();
    EasyMock.expect(mockReceivingDeck.draw()).andReturn(Resource.LUMBER);
    mockPlayer.updateResources(Resource.LUMBER, 1);
    EasyMock.expectLastCall();

    EasyMock.replay(mockBoard, mockPlayer, mockGivingDeck, mockReceivingDeck);

    port.executePortTrade(mockPlayer, mockBoard,
        buildRequest(Resource.BRICK, Resource.LUMBER));

    EasyMock.verify(mockBoard, mockPlayer, mockGivingDeck, mockReceivingDeck);
  }

  // Test Case 11
  @Test
  void RedAtWoolPort_TriesToGiveOre_ThrowsException() {
    final Port port = new Port(2, Resource.WOOL, List.of(0, 3));

    EasyMock.expect(mockPlayer.getColor()).andReturn(PlayerColor.RED).anyTimes();
    EasyMock.expect(mockBoard.checkPlayerOwnsNode(PlayerColor.RED, 0)).andReturn(true);

    EasyMock.replay(mockBoard, mockPlayer, mockGivingDeck, mockReceivingDeck);

    Exception exception = assertThrows(IllegalArgumentException.class, () ->
        port.executePortTrade(mockPlayer, mockBoard,
            buildRequest(Resource.ORE, Resource.WOOL)));

    assertEquals("This port only accepts WOOL for 2:1 trades.", exception.getMessage());

    EasyMock.verify(mockBoard, mockPlayer, mockGivingDeck, mockReceivingDeck);
  }

  // Test Case 12
  @Test
  void RedAtAnyPort_TradesWoolForWool_ThrowsException() {
    final Port port = new Port(3, Resource.ANY, List.of(0, 3));

    EasyMock.expect(mockPlayer.getColor()).andReturn(PlayerColor.RED).anyTimes();
    EasyMock.expect(mockBoard.checkPlayerOwnsNode(PlayerColor.RED, 0)).andReturn(true);

    EasyMock.replay(mockBoard, mockPlayer, mockGivingDeck, mockReceivingDeck);

    Exception exception = assertThrows(IllegalArgumentException.class, () ->
        port.executePortTrade(mockPlayer, mockBoard,
            buildRequestSameResource(Resource.WOOL)));

    assertEquals("Cannot trade a resource for itself.", exception.getMessage());

    EasyMock.verify(mockBoard, mockPlayer, mockGivingDeck, mockReceivingDeck);
  }

  // Test Case 13
  @Test
  void RedAtWoolPort_HasOneWool_TriesToGiveTwo_ThrowsException() {
    final Port port = new Port(2, Resource.WOOL, List.of(0, 3));

    EasyMock.expect(mockPlayer.getColor()).andReturn(PlayerColor.RED).anyTimes();
    EasyMock.expect(mockBoard.checkPlayerOwnsNode(PlayerColor.RED, 0)).andReturn(true);
    EasyMock.expect(mockPlayer.getResourceCount(Resource.WOOL)).andReturn(1);

    EasyMock.replay(mockBoard, mockPlayer, mockGivingDeck, mockReceivingDeck);

    Exception exception = assertThrows(IllegalStateException.class, () ->
        port.executePortTrade(mockPlayer, mockBoard,
            buildRequest(Resource.WOOL, Resource.ORE)));

    assertEquals("Player has insufficient resources for this trade.", exception.getMessage());

    EasyMock.verify(mockBoard, mockPlayer, mockGivingDeck, mockReceivingDeck);
  }

  // Test Case 14
  @Test
  void RedDoesNotOwnAdjacentNode_ThrowsException() {
    final Port port = new Port(3, Resource.ANY, List.of(0, 3));

    EasyMock.expect(mockPlayer.getColor()).andReturn(PlayerColor.RED).anyTimes();
    EasyMock.expect(mockBoard.checkPlayerOwnsNode(PlayerColor.RED, 0)).andReturn(false);
    EasyMock.expect(mockBoard.checkPlayerOwnsNode(PlayerColor.RED, 3)).andReturn(false);

    EasyMock.replay(mockBoard, mockPlayer, mockGivingDeck, mockReceivingDeck);

    Exception exception = assertThrows(IllegalStateException.class, () ->
        port.executePortTrade(mockPlayer, mockBoard,
            buildRequest(Resource.WOOL, Resource.ORE)));

    assertEquals("Player does not have access to this port.", exception.getMessage());

    EasyMock.verify(mockBoard, mockPlayer, mockGivingDeck, mockReceivingDeck);
  }

  // Test Case 15
  @Test
  void RedAtAnyPort_BankHasZeroOre_ThrowsEmptyDeckException() throws EmptyDeckException {
    final Port port = new Port(3, Resource.ANY, List.of(0, 3));

    EasyMock.expect(mockPlayer.getColor()).andReturn(PlayerColor.RED).anyTimes();
    EasyMock.expect(mockBoard.checkPlayerOwnsNode(PlayerColor.RED, 0)).andReturn(true);
    EasyMock.expect(mockPlayer.getResourceCount(Resource.WOOL)).andReturn(3);
    EasyMock.expect(mockReceivingDeck.getTotalCards()).andReturn(0);

    EasyMock.replay(mockBoard, mockPlayer, mockGivingDeck, mockReceivingDeck);

    assertThrows(EmptyDeckException.class, () ->
        port.executePortTrade(mockPlayer, mockBoard,
            buildRequest(Resource.WOOL, Resource.ORE)));

    EasyMock.verify(mockBoard, mockPlayer, mockGivingDeck, mockReceivingDeck);
  }

  // Test Case 16
  @Test
  void RedAtAnyPort_BankHasOneOre_TradeSucceeds() throws EmptyDeckException {
    final Port port = new Port(3, Resource.ANY, List.of(0, 3));

    EasyMock.expect(mockPlayer.getColor()).andReturn(PlayerColor.RED).anyTimes();
    EasyMock.expect(mockBoard.checkPlayerOwnsNode(PlayerColor.RED, 0)).andReturn(true);
    EasyMock.expect(mockPlayer.getResourceCount(Resource.WOOL)).andReturn(3);
    EasyMock.expect(mockReceivingDeck.getTotalCards()).andReturn(1);
    mockPlayer.updateResources(Resource.WOOL, -3);
    EasyMock.expectLastCall();
    mockGivingDeck.replenish(3);
    EasyMock.expectLastCall();
    EasyMock.expect(mockReceivingDeck.draw()).andReturn(Resource.ORE);
    mockPlayer.updateResources(Resource.ORE, 1);
    EasyMock.expectLastCall();

    EasyMock.replay(mockBoard, mockPlayer, mockGivingDeck, mockReceivingDeck);

    port.executePortTrade(mockPlayer, mockBoard,
        buildRequest(Resource.WOOL, Resource.ORE));

    EasyMock.verify(mockBoard, mockPlayer, mockGivingDeck, mockReceivingDeck);
  }

  // Test Case 17
  @Test
  void RedAtAnyPort_PlayerHasNineteenWool_TradeSucceeds() throws EmptyDeckException {
    final Port port = new Port(3, Resource.ANY, List.of(0, 3));

    EasyMock.expect(mockPlayer.getColor()).andReturn(PlayerColor.RED).anyTimes();
    EasyMock.expect(mockBoard.checkPlayerOwnsNode(PlayerColor.RED, 0)).andReturn(true);
    EasyMock.expect(mockPlayer.getResourceCount(Resource.WOOL)).andReturn(19);
    EasyMock.expect(mockReceivingDeck.getTotalCards()).andReturn(19);
    mockPlayer.updateResources(Resource.WOOL, -3);
    EasyMock.expectLastCall();
    mockGivingDeck.replenish(3);
    EasyMock.expectLastCall();
    EasyMock.expect(mockReceivingDeck.draw()).andReturn(Resource.ORE);
    mockPlayer.updateResources(Resource.ORE, 1);
    EasyMock.expectLastCall();

    EasyMock.replay(mockBoard, mockPlayer, mockGivingDeck, mockReceivingDeck);

    port.executePortTrade(mockPlayer, mockBoard,
        buildRequest(Resource.WOOL, Resource.ORE));

    EasyMock.verify(mockBoard, mockPlayer, mockGivingDeck, mockReceivingDeck);
  }

  // Test Case 18
  @Test
  void RedAtWoolPort_GivesTwoWool_ReceivesOneWool_ThrowsException() {
    final Port port = new Port(2, Resource.WOOL, List.of(0, 3));

    EasyMock.expect(mockPlayer.getColor()).andReturn(PlayerColor.RED).anyTimes();
    EasyMock.expect(mockBoard.checkPlayerOwnsNode(PlayerColor.RED, 0)).andReturn(true);

    EasyMock.replay(mockBoard, mockPlayer, mockGivingDeck, mockReceivingDeck);

    Exception exception = assertThrows(IllegalArgumentException.class, () ->
        port.executePortTrade(mockPlayer, mockBoard,
            buildRequestSameResource(Resource.WOOL)));

    assertEquals("Cannot trade a resource for itself.", exception.getMessage());

    EasyMock.verify(mockBoard, mockPlayer, mockGivingDeck, mockReceivingDeck);
  }
}