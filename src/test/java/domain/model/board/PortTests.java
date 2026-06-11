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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

}
