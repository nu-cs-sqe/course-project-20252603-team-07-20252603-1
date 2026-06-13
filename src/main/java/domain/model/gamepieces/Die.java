package domain.model.gamepieces;

import java.util.Random;

class Die {
  private static final int DIE_SIDES_NUMBER = 6;

  private final Random randomizer;

  Die(Random randomizer) {
    this.randomizer = randomizer;
  }

  int rollOneDie() {
    return randomizer.nextInt(DIE_SIDES_NUMBER) + 1;
  }
}
