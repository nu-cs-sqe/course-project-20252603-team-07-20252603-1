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


}


