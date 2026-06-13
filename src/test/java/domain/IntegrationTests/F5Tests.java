package domain.integrationtests;

// Tests for Feature 5:
// Ability to roll dice and distribute resources to players based on settlements
// and cities adjacent to matching number tokens, excluding robber-blocked hexes

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import domain.model.board.BoardHandler;
import domain.model.gamepieces.DiceHandler;
import domain.model.player.Player;
import domain.model.player.PlayerColor;
import domain.model.resources.Resource;
import org.junit.jupiter.api.Test;

/** Test class. */
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

  // Test Case 5
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

  // Test Case 6
  @Test
  void EightRolled_RobberOnHexEleven_WhiteCityTwenty_EachGetTwoLumber() {
    BoardHandler b = new BoardHandler();
    Player whitePlayer = new Player("Dummy", PlayerColor.WHITE);
    Player bluePlayer = new Player("Dummy", PlayerColor.BLUE);
    Player redPlayer = new Player("Dummy", PlayerColor.RED);
    Player orangePlayer = new Player("Dummy", PlayerColor.ORANGE);

    b.buildSetupSettlement(whitePlayer, 20);
    b.buildCity(whitePlayer, 20);

    b.buildSetupSettlement(bluePlayer, 37);
    b.buildSetupSettlement(bluePlayer, 28);
    b.buildCity(bluePlayer, 28);

    b.buildSetupSettlement(redPlayer, 38);
    b.buildCity(redPlayer, 38);

    b.buildSetupSettlement(orangePlayer, 39);
    b.buildCity(orangePlayer, 39);

    b.moveRobber(11);

    b.awardResources(8);

    assertEquals(0, whitePlayer.getResourceCount(Resource.LUMBER));
    assertEquals(0, whitePlayer.getResourceCount(Resource.ORE));
    assertEquals(0, whitePlayer.getResourceCount(Resource.WOOL));
    assertEquals(0, whitePlayer.getResourceCount(Resource.GRAIN));
    assertEquals(0, whitePlayer.getResourceCount(Resource.BRICK));

    assertEquals(2, bluePlayer.getResourceCount(Resource.LUMBER));
    assertEquals(0, bluePlayer.getResourceCount(Resource.ORE));
    assertEquals(0, bluePlayer.getResourceCount(Resource.WOOL));
    assertEquals(0, bluePlayer.getResourceCount(Resource.GRAIN));
    assertEquals(0, bluePlayer.getResourceCount(Resource.BRICK));

    assertEquals(2, redPlayer.getResourceCount(Resource.LUMBER));
    assertEquals(0, redPlayer.getResourceCount(Resource.ORE));
    assertEquals(0, redPlayer.getResourceCount(Resource.WOOL));
    assertEquals(0, redPlayer.getResourceCount(Resource.GRAIN));
    assertEquals(0, redPlayer.getResourceCount(Resource.BRICK));

    assertEquals(2, orangePlayer.getResourceCount(Resource.LUMBER));
    assertEquals(0, orangePlayer.getResourceCount(Resource.ORE));
    assertEquals(0, orangePlayer.getResourceCount(Resource.WOOL));
    assertEquals(0, orangePlayer.getResourceCount(Resource.GRAIN));
    assertEquals(0, orangePlayer.getResourceCount(Resource.BRICK));
  }

  // Test Case 7
  @Test
  void SixRolled_RobberNotOnHex_WhiteCitiesOnThreeNodes_RedSettlementsOnThreeNodes() {
    BoardHandler b = new BoardHandler();
    Player whitePlayer = new Player("Dummy", PlayerColor.WHITE);
    Player redPlayer = new Player("Dummy", PlayerColor.RED);

    b.buildSetupSettlement(whitePlayer, 8);
    b.buildCity(whitePlayer, 8);
    b.buildSetupSettlement(whitePlayer, 17);
    b.buildCity(whitePlayer, 17);
    b.buildSetupSettlement(whitePlayer, 18);
    b.buildCity(whitePlayer, 18);

    b.buildSetupSettlement(redPlayer, 40);
    b.buildSetupSettlement(redPlayer, 48);
    b.buildSetupSettlement(redPlayer, 49);

    b.awardResources(6);

    assertEquals(6, whitePlayer.getResourceCount(Resource.BRICK));
    assertEquals(0, whitePlayer.getResourceCount(Resource.WOOL));
    assertEquals(0, whitePlayer.getResourceCount(Resource.LUMBER));
    assertEquals(0, whitePlayer.getResourceCount(Resource.ORE));
    assertEquals(0, whitePlayer.getResourceCount(Resource.GRAIN));

    assertEquals(3, redPlayer.getResourceCount(Resource.GRAIN));
    assertEquals(0, redPlayer.getResourceCount(Resource.WOOL));
    assertEquals(0, redPlayer.getResourceCount(Resource.LUMBER));
    assertEquals(0, redPlayer.getResourceCount(Resource.ORE));
    assertEquals(0, redPlayer.getResourceCount(Resource.BRICK));
  }

  // Test Case 8
  @Test
  void SevenRolled_OrangeCityOnEighteen_BlueSettlementOnThirtyFive_NoResourcesAwarded() {
    BoardHandler b = new BoardHandler();
    Player orangePlayer = new Player("Dummy", PlayerColor.ORANGE);
    Player bluePlayer = new Player("Dummy", PlayerColor.BLUE);

    b.buildSetupSettlement(orangePlayer, 18);
    b.buildCity(orangePlayer, 18);

    b.buildSetupSettlement(bluePlayer, 35);

    b.awardResources(7);

    assertEquals(0, orangePlayer.getResourceCount(Resource.WOOL));
    assertEquals(0, orangePlayer.getResourceCount(Resource.LUMBER));
    assertEquals(0, orangePlayer.getResourceCount(Resource.ORE));
    assertEquals(0, orangePlayer.getResourceCount(Resource.GRAIN));
    assertEquals(0, orangePlayer.getResourceCount(Resource.BRICK));

    assertEquals(0, bluePlayer.getResourceCount(Resource.WOOL));
    assertEquals(0, bluePlayer.getResourceCount(Resource.LUMBER));
    assertEquals(0, bluePlayer.getResourceCount(Resource.ORE));
    assertEquals(0, bluePlayer.getResourceCount(Resource.GRAIN));
    assertEquals(0, bluePlayer.getResourceCount(Resource.BRICK));
  }
}
