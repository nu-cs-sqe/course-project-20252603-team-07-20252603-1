package domain.IntegrationTests;

// Tests for Feature 5:
// Ability to roll dice and distribute resources to players based on settlements and cities adjacent to matching number tokens, excluding robber-blocked hexes

import domain.model.board.BoardHandler;
import domain.model.game_pieces.DiceHandler;
import domain.model.player.Player;
import domain.model.player.PlayerColor;
import domain.model.resources.Resource;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class F5Tests {
  // Test Case 1 - rollTwoDice
  @Test
  void DiceHandlerRolled_InRangeTwoToTwelve() {
    DiceHandler diceHandler = new DiceHandler();
    int result = diceHandler.rollTwoDice();
    assertTrue(result >= 2 && result <= 12);
  }

  // Test Case 2
  @Test
  void TwoRolled_RobberNotOnHex_NoSettlementsOrCities_NoResourcesAwarded() {
    BoardHandler b = new BoardHandler();

    Player orangePlayer = new Player("Dummy", PlayerColor.ORANGE);

    b.awardResources(2);

    assertEquals(0, orangePlayer.getResourceCount(Resource.WOOL));
    assertEquals(0, orangePlayer.getResourceCount(Resource.LUMBER));
    assertEquals(0, orangePlayer.getResourceCount(Resource.ORE));
    assertEquals(0, orangePlayer.getResourceCount(Resource.GRAIN));
    assertEquals(0, orangePlayer.getResourceCount(Resource.BRICK));
  }

  // Test Case 3
  @Test
  void TwelveRolled_RobberNotOnHex_OrangeSettlementOnNodeSeven_OrangeGetsOneGrain() {
    BoardHandler b = new BoardHandler();
    Player orangePlayer = new Player("Dummy", PlayerColor.ORANGE);

    b.buildSetupSettlement(orangePlayer, 7);

    b.awardResources(12);

    assertEquals(1, orangePlayer.getResourceCount(Resource.GRAIN));
    assertEquals(0, orangePlayer.getResourceCount(Resource.WOOL));
    assertEquals(0, orangePlayer.getResourceCount(Resource.LUMBER));
    assertEquals(0, orangePlayer.getResourceCount(Resource.ORE));
    assertEquals(0, orangePlayer.getResourceCount(Resource.BRICK));
  }

  // Test Case 4
  @Test
  void EightRolled_RobberNotOnHex_BlueOnNodeThirtyOne_RedOnNodeTwentyEight_EachGetOne() {
    BoardHandler b = new BoardHandler();
    Player bluePlayer = new Player("Dummy", PlayerColor.BLUE);
    Player redPlayer = new Player("Dummy", PlayerColor.RED);

    b.buildSetupSettlement(bluePlayer, 31);
    b.buildSetupSettlement(redPlayer, 28);

    b.awardResources(8);

    assertEquals(1, bluePlayer.getResourceCount(Resource.ORE));
    assertEquals(0, bluePlayer.getResourceCount(Resource.WOOL));
    assertEquals(0, bluePlayer.getResourceCount(Resource.LUMBER));
    assertEquals(0, bluePlayer.getResourceCount(Resource.GRAIN));
    assertEquals(0, bluePlayer.getResourceCount(Resource.BRICK));

    assertEquals(1, redPlayer.getResourceCount(Resource.LUMBER));
    assertEquals(0, redPlayer.getResourceCount(Resource.WOOL));
    assertEquals(0, redPlayer.getResourceCount(Resource.ORE));
    assertEquals(0, redPlayer.getResourceCount(Resource.GRAIN));
    assertEquals(0, redPlayer.getResourceCount(Resource.BRICK));
  }

  // Test Case 8
  @Test
  void TwoRolled_RobberOnHexOne_WhiteSettlementsOnNodesFourFiveThirteen_NoResourcesAwarded() {
    BoardHandler b = new BoardHandler();
    Player whitePlayer = new Player("Dummy", PlayerColor.WHITE);

    b.buildSetupSettlement(whitePlayer, 4);
    b.buildSetupSettlement(whitePlayer, 5);
    b.buildSetupSettlement(whitePlayer, 13);

    b.moveRobber(1);

    b.awardResources(2);

    assertEquals(0, whitePlayer.getResourceCount(Resource.WOOL));
    assertEquals(0, whitePlayer.getResourceCount(Resource.LUMBER));
    assertEquals(0, whitePlayer.getResourceCount(Resource.ORE));
    assertEquals(0, whitePlayer.getResourceCount(Resource.GRAIN));
    assertEquals(0, whitePlayer.getResourceCount(Resource.BRICK));
  }


}


