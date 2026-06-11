package domain.model.board;

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
}
