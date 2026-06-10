package domain.IntegrationTests;

// Tests for Feature 3:
// Ability to place initial settlements and roads during the setup phase according to setup rules

import domain.model.board.BoardHandler;
import domain.model.exceptions.AdjacentNodeAlreadyClaimed;
import domain.model.player.Player;
import domain.model.player.PlayerColor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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

  // Test Case 3
  @Test
  void OrangeClaimsNodeNegativeOne_ThrowsInvalidNodeID() {
    BoardHandler b = new BoardHandler();
    Player orangePlayer = new Player("Ben", PlayerColor.ORANGE);

    Exception exception = assertThrows(IllegalArgumentException.class, () ->
            b.buildSetupSettlement(orangePlayer, -1));

    assertEquals("Invalid NodeID - must be within [0, 53].", exception.getMessage());
  }

  // Test Case 4
  @Test
  void WhiteClaimsNodeFiftyFour_ThrowsInvalidNodeID() {
    BoardHandler b = new BoardHandler();
    Player whitePlayer = new Player("Spencer", PlayerColor.WHITE);

    Exception exception = assertThrows(IllegalArgumentException.class, () ->
            b.buildSetupSettlement(whitePlayer, 54));

    assertEquals("Invalid NodeID - must be within [0, 53].", exception.getMessage());
  }

  // Test Case 5
  @Test
  void OrangeClaimsNodeEight_NodeOwnedByOrange_ThreeHexesUpdated() {
    BoardHandler b = new BoardHandler();
    Player orangePlayer = new Player("theo", PlayerColor.ORANGE);

    b.buildSetupSettlement(orangePlayer, 8);

    assertTrue(b.checkPlayerOwnsNode(PlayerColor.ORANGE, 8));
    assertEquals(1, b.getNodeBuildingLevel(8));
    assertTrue(b.getPlayersOnHex(0).contains(orangePlayer));
    assertTrue(b.getPlayersOnHex(1).contains(orangePlayer));
    assertTrue(b.getPlayersOnHex(4).contains(orangePlayer));
  }

  // Test Case 6
  @Test
  void BlueClaimsNodeFour_NodeOwnedByBlue_TwoHexesUpdated() {
    BoardHandler b = new BoardHandler();
    Player bluePlayer = new Player("kevin", PlayerColor.BLUE);

    b.buildSetupSettlement(bluePlayer, 4);

    assertTrue(b.checkPlayerOwnsNode(PlayerColor.BLUE, 4));
    assertEquals(1, b.getNodeBuildingLevel(4));
    assertTrue(b.getPlayersOnHex(0).contains(bluePlayer));
    assertTrue(b.getPlayersOnHex(1).contains(bluePlayer));
  }

  // Test Case 7
  @Test
  void RedClaimsNodeZero_BlueClaimsNodeOne_ThrowsAdjacentNodeAlreadyClaimed() {
    BoardHandler b = new BoardHandler();
    Player redPlayer = new Player("connor", PlayerColor.RED);
    Player bluePlayer = new Player("ben", PlayerColor.BLUE);

    b.buildSetupSettlement(redPlayer, 0);

    Exception exception = assertThrows(AdjacentNodeAlreadyClaimed.class, () ->
            b.buildSetupSettlement(bluePlayer, 1));

    assertEquals("Can not claim node adjacent to node already claimed", exception.getMessage());
  }

  // Test Case 8
  @Test
  void RedClaimsNodeZero_ThenClaimsEdgeZeroToFour_EdgeIsClaimed() {
    BoardHandler b = new BoardHandler();
    Player redPlayer = new Player("Dummy", PlayerColor.RED);

    b.buildSetupSettlement(redPlayer, 0);
    b.buildSetupRoad(redPlayer, 0, 0, 4);

    assertTrue(b.checkEdgeOccupied(0, 4));
  }

  // Test Case 9
  @Test
  void OrangeClaimsNodeFiftyThree_ThenClaimsEdgeFourtyNineToFiftyThree_EdgeIsClaimed() {
    BoardHandler b = new BoardHandler();
    Player orangePlayer = new Player("Dummy", PlayerColor.ORANGE);

    b.buildSetupSettlement(orangePlayer, 53);
    b.buildSetupRoad(orangePlayer, 53, 49, 53);

    assertTrue(b.checkEdgeOccupied(49, 53));
  }

  // Test Case 10
  @Test
  void WhiteTriesToClaimEdgeNegativeOneToZero_ThrowsOutOfBounds() {
    BoardHandler b = new BoardHandler();
    Player whitePlayer = new Player("Dummy", PlayerColor.WHITE);

    Exception exception = assertThrows(IllegalArgumentException.class, () ->
            b.buildSetupRoad(whitePlayer, 0, -1, 0));

    assertEquals("Edge nodeId out of bounds. Must be within [0, 53].", exception.getMessage());
  }

  // Test Case 11
  @Test
  void WhiteTriesToClaimEdgeZeroToNegativeOne_ThrowsOutOfBounds() {
    BoardHandler b = new BoardHandler();
    Player whitePlayer = new Player("Dummy", PlayerColor.WHITE);

    Exception exception = assertThrows(IllegalArgumentException.class, () ->
            b.buildSetupRoad(whitePlayer, 0, 0, -1));

    assertEquals("Edge nodeId out of bounds. Must be within [0, 53].", exception.getMessage());
  }

  // Test Case 12
  @Test
  void BlueTriesToClaimEdgeFiftyThreeToFiftyFour_ThrowsOutOfBounds() {
    BoardHandler b = new BoardHandler();
    Player bluePlayer = new Player("Dummy", PlayerColor.BLUE);

    Exception exception = assertThrows(IllegalArgumentException.class, () ->
            b.buildSetupRoad(bluePlayer, 53, 53, 54));

    assertEquals("Edge nodeId out of bounds. Must be within [0, 53].", exception.getMessage());
  }
}


