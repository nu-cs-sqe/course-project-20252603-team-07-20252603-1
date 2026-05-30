package domain;

import java.util.Random;

class Die {
    private static final int DIE_SIDES_NUMBER = 6;

    private final Random randomizer;

    Die(Random randomizer) {
        this.randomizer = randomizer;
    }

    // Need to add 1 to result of nextInt, returns in range of [0, bound)
    int rollOneDie(){
        return randomizer.nextInt(DIE_SIDES_NUMBER) + 1;
    }
}