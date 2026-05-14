package domain;

import java.util.Random;

class Die {
    private final Random randomizer;

    Die(Random randomizer) {
        this.randomizer = randomizer;
    }

    int rollOneDie(){
        return randomizer.nextInt(6) + 1;
    }
}