package domain.IntegrationTests;

// Tests for Feature 5:
// Ability to roll dice and distribute resources to players based on settlements and cities adjacent to matching number tokens, excluding robber-blocked hexes

import domain.model.game_pieces.DiceHandler;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class F5Tests {
  // Test Case 1 - rollTwoDice
  @Test
  void DiceHandlerRolled_InRangeTwoToTwelve() {
    DiceHandler diceHandler = new DiceHandler();
    int result = diceHandler.rollTwoDice();
    assertTrue(result >= 2 && result <= 12);
  }


}


