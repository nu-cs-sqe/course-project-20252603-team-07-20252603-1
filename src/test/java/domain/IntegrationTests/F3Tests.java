package domain.IntegrationTests;

// Tests for Feature 3:
// Ability to place initial settlements and roads during the setup phase according to setup rules

import domain.model.board.BoardHandler;
import domain.model.player.Player;
import domain.model.player.PlayerColor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class F3Tests {
  // Test Case 1
  @Test
  void RedClaimsNodeZero_NodeOwnedByRed_HexZeroUpdated() {
    BoardHandler b = new BoardHandler();
    Player redPlayer = new Player("Dummy", PlayerColor.RED);

    b.buildSetupSettlement(redPlayer, 0);

    assertTrue(b.checkPlayerOwnsNode(PlayerColor.RED, 0));
    assertEquals(1, b.getNodeBuildingLevel(0));
    assertTrue(b.getPlayersOnHex(0).contains(redPlayer));
  }

  // Test Case 2
  @Test
  void BlueClaimsNodeFiftyThree_NodeOwnedByBlue_HexEighteenUpdated() {
    BoardHandler b = new BoardHandler();
    Player bluePlayer = new Player("Dummy", PlayerColor.BLUE);

    b.buildSetupSettlement(bluePlayer, 53);

    assertTrue(b.checkPlayerOwnsNode(PlayerColor.BLUE, 53));
    assertEquals(1, b.getNodeBuildingLevel(53));
    assertTrue(b.getPlayersOnHex(18).contains(bluePlayer));
  }
}
