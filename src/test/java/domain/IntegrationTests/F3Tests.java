package domain.IntegrationTests;

// Tests for Feature 3:
// Ability to place initial settlements and roads during the setup phase according to setup rules

import domain.model.board.BoardHandler;
import domain.model.exceptions.AdjacentNodeAlreadyClaimed;
import domain.model.exceptions.EdgeAlreadyClaimedException;
import domain.model.exceptions.IllegalEdgeClaim;
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

  // Test Case 13
  @Test
  void BlueTriesToClaimEdgeFiftyFourToFiftyThree_ThrowsOutOfBounds() {
    BoardHandler b = new BoardHandler();
    Player bluePlayer = new Player("Dummy", PlayerColor.BLUE);

    Exception exception = assertThrows(IllegalArgumentException.class, () ->
            b.buildSetupRoad(bluePlayer, 53, 54, 53));

    assertEquals("Edge nodeId out of bounds. Must be within [0, 53].", exception.getMessage());
  }

  // Test Case 14
  @Test
  void RedTriesToClaimEdgeZeroToOne_ThrowsEdgeDoesNotExist() {
    BoardHandler b = new BoardHandler();
    Player redPlayer = new Player("Dummy", PlayerColor.RED);

    b.buildSetupSettlement(redPlayer, 0);

    Exception exception = assertThrows(IllegalEdgeClaim.class, () ->
            b.buildSetupRoad(redPlayer, 0, 0, 1));

    assertEquals("Edge must be adjacent to just placed settlement", exception.getMessage());
  }

  // Test Case 15
  @Test
  void RedClaimsEdgeZeroToFour_WhiteTriesToClaimSameEdge_ThrowsEdgeAlreadyClaimed() {
    BoardHandler b = new BoardHandler();
    Player redPlayer = new Player("Dummy", PlayerColor.RED);
    Player whitePlayer = new Player("Dummy", PlayerColor.WHITE);

    b.buildSetupSettlement(redPlayer, 0);
    b.buildSetupRoad(redPlayer, 0, 0, 4);

    b.buildSetupSettlement(whitePlayer, 20);

    Exception exception = assertThrows(IllegalEdgeClaim.class, () ->
            b.buildSetupRoad(whitePlayer, 20, 0, 4));

    assertEquals("Edge must be adjacent to just placed settlement", exception.getMessage());
  }

  // Test Case 16
  @Test
  void BlueClaimsNodeEight_TriesToClaimEdgeZeroToFour_ThrowsNotAdjacent() {
    BoardHandler b = new BoardHandler();
    Player bluePlayer = new Player("Dummy", PlayerColor.BLUE);

    b.buildSetupSettlement(bluePlayer, 8);

    Exception exception = assertThrows(IllegalEdgeClaim.class, () ->
            b.buildSetupRoad(bluePlayer, 8, 0, 4));

    assertEquals("Edge must be adjacent to just placed settlement", exception.getMessage());
  }

  // Test Case 17
  @Test
  void RedClaimsNodeZeroAndRoad_ThenNodeFortyAndRoad_BothSucceed() {
    BoardHandler b = new BoardHandler();
    Player redPlayer = new Player("Dummy", PlayerColor.RED);

    b.buildSetupSettlement(redPlayer, 0);
    b.buildSetupRoad(redPlayer, 0, 0, 4);
    b.buildSetupSettlement(redPlayer, 40);
    b.buildSetupRoad(redPlayer, 40, 40, 45);

    assertTrue(b.checkPlayerOwnsNode(PlayerColor.RED, 0));
    assertTrue(b.checkPlayerOwnsNode(PlayerColor.RED, 40));
    assertTrue(b.checkEdgeOccupied(0, 4));
    assertTrue(b.checkEdgeOccupied(40, 45));
    assertEquals(1, b.getNodeBuildingLevel(0));
    assertEquals(1, b.getNodeBuildingLevel(40));
    assertTrue(b.getPlayersOnHex(0).contains(redPlayer));
    assertTrue(b.getPlayersOnHex(13).contains(redPlayer));
  }

  // Test Case 18
  @Test
  void FullRoundOne_AllFourPlayers_AllSettlementsAndRoadsPlaced() {
    BoardHandler b = new BoardHandler();
    Player redPlayer = new Player("Dummy", PlayerColor.RED);
    Player bluePlayer = new Player("Dummy", PlayerColor.BLUE);
    Player orangePlayer = new Player("Dummy", PlayerColor.ORANGE);
    Player whitePlayer = new Player("Dummy", PlayerColor.WHITE);

    b.buildSetupSettlement(redPlayer, 0);
    b.buildSetupRoad(redPlayer, 0, 0, 4);

    b.buildSetupSettlement(bluePlayer, 20);
    b.buildSetupRoad(bluePlayer, 20, 20, 25);

    b.buildSetupSettlement(orangePlayer, 40);
    b.buildSetupRoad(orangePlayer, 40, 40, 45);

    b.buildSetupSettlement(whitePlayer, 53);
    b.buildSetupRoad(whitePlayer, 53, 49, 53);

    assertTrue(b.checkPlayerOwnsNode(PlayerColor.RED, 0));
    assertTrue(b.checkPlayerOwnsNode(PlayerColor.BLUE, 20));
    assertTrue(b.checkPlayerOwnsNode(PlayerColor.ORANGE, 40));
    assertTrue(b.checkPlayerOwnsNode(PlayerColor.WHITE, 53));

    assertTrue(b.checkEdgeOccupied(0, 4));
    assertTrue(b.checkEdgeOccupied(20, 25));
    assertTrue(b.checkEdgeOccupied(40, 45));
    assertTrue(b.checkEdgeOccupied(49, 53));

    assertEquals(1, b.getNodeBuildingLevel(0));
    assertEquals(1, b.getNodeBuildingLevel(20));
    assertEquals(1, b.getNodeBuildingLevel(40));
    assertEquals(1, b.getNodeBuildingLevel(53));

    assertTrue(b.getPlayersOnHex(0).contains(redPlayer));
    assertTrue(b.getPlayersOnHex(6).contains(bluePlayer));
    assertTrue(b.getPlayersOnHex(13).contains(orangePlayer));
    assertTrue(b.getPlayersOnHex(18).contains(whitePlayer));
  }

  // Test Case 19
  @Test
  void FullRoundOneAndTwo_AllFourPlayers_AllSettlementsAndRoadsPlaced() {
    BoardHandler b = new BoardHandler();
    Player redPlayer = new Player("Dummy", PlayerColor.RED);
    Player bluePlayer = new Player("Dummy", PlayerColor.BLUE);
    Player orangePlayer = new Player("Dummy", PlayerColor.ORANGE);
    Player whitePlayer = new Player("Dummy", PlayerColor.WHITE);

    // Round 1
    b.buildSetupSettlement(redPlayer, 0);
    b.buildSetupRoad(redPlayer, 0, 0, 4);

    b.buildSetupSettlement(bluePlayer, 20);
    b.buildSetupRoad(bluePlayer, 20, 20, 25);

    b.buildSetupSettlement(orangePlayer, 40);
    b.buildSetupRoad(orangePlayer, 40, 40, 45);

    b.buildSetupSettlement(whitePlayer, 53);
    b.buildSetupRoad(whitePlayer, 53, 49, 53);

    // Round 2 - reverse order
    b.buildSetupSettlement(whitePlayer, 10);
    b.buildSetupRoad(whitePlayer, 10, 10, 14);

    b.buildSetupSettlement(orangePlayer, 30);
    b.buildSetupRoad(orangePlayer, 30, 30, 35);

    b.buildSetupSettlement(bluePlayer, 17);
    b.buildSetupRoad(bluePlayer, 17, 17, 22);

    b.buildSetupSettlement(redPlayer, 47);
    b.buildSetupRoad(redPlayer, 47, 47, 51);

    // verify all nodes
    assertTrue(b.checkPlayerOwnsNode(PlayerColor.RED, 0));
    assertTrue(b.checkPlayerOwnsNode(PlayerColor.RED, 47));
    assertTrue(b.checkPlayerOwnsNode(PlayerColor.BLUE, 20));
    assertTrue(b.checkPlayerOwnsNode(PlayerColor.BLUE, 17));
    assertTrue(b.checkPlayerOwnsNode(PlayerColor.ORANGE, 40));
    assertTrue(b.checkPlayerOwnsNode(PlayerColor.ORANGE, 30));
    assertTrue(b.checkPlayerOwnsNode(PlayerColor.WHITE, 53));
    assertTrue(b.checkPlayerOwnsNode(PlayerColor.WHITE, 10));

    // verify all edges
    assertTrue(b.checkEdgeOccupied(0, 4));
    assertTrue(b.checkEdgeOccupied(20, 25));
    assertTrue(b.checkEdgeOccupied(40, 45));
    assertTrue(b.checkEdgeOccupied(49, 53));
    assertTrue(b.checkEdgeOccupied(10, 14));
    assertTrue(b.checkEdgeOccupied(30, 35));
    assertTrue(b.checkEdgeOccupied(17, 22));
    assertTrue(b.checkEdgeOccupied(47, 51));

    // verify all building levels
    assertEquals(1, b.getNodeBuildingLevel(0));
    assertEquals(1, b.getNodeBuildingLevel(47));
    assertEquals(1, b.getNodeBuildingLevel(20));
    assertEquals(1, b.getNodeBuildingLevel(17));
    assertEquals(1, b.getNodeBuildingLevel(40));
    assertEquals(1, b.getNodeBuildingLevel(30));
    assertEquals(1, b.getNodeBuildingLevel(53));
    assertEquals(1, b.getNodeBuildingLevel(10));

    // verify hex settlement lists
    assertTrue(b.getPlayersOnHex(0).contains(redPlayer));
    assertTrue(b.getPlayersOnHex(16).contains(redPlayer));
    assertTrue(b.getPlayersOnHex(6).contains(bluePlayer));
    assertTrue(b.getPlayersOnHex(3).contains(bluePlayer));
    assertTrue(b.getPlayersOnHex(13).contains(orangePlayer));
    assertTrue(b.getPlayersOnHex(9).contains(orangePlayer));
    assertTrue(b.getPlayersOnHex(18).contains(whitePlayer));
    assertTrue(b.getPlayersOnHex(2).contains(whitePlayer));
  }
}


