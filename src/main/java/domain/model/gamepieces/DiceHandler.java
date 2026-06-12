package domain.model.gamepieces;

import java.util.Random;

/** Handles rolling two dice for a turn. */
public class DiceHandler {
  private final Die dieOne;
  private final Die dieTwo;

  DiceHandler(Die firstDie, Die secondDie) {
    this.dieOne = firstDie;
    this.dieTwo = secondDie;
  }

  /** Constructs a DiceHandler with default random dice. */
  public DiceHandler() {
    this.dieOne = new Die(new Random());
    this.dieTwo = new Die(new Random());
  }

  /** Rolls both dice and returns their sum. */
  public int rollTwoDice() {
    int rollOne = dieOne.rollOneDie();
    int rollTwo = dieTwo.rollOneDie();
    return rollOne + rollTwo;
  }
}
