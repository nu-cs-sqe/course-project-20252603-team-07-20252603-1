package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VictoryPointCardTests {

  @Test // Test Case 1
  public void GetVictoryPoints_OnVictoryPointCard_ExpectOne() {
    VictoryPointCard victoryPointCard = new VictoryPointCard();

    int actual = victoryPointCard.getVictoryPoints();

    assertEquals(1, actual);
  }
}
