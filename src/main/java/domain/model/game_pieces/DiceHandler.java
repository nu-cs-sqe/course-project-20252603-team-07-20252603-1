package domain.model.game_pieces;

import java.util.Random;

public class DiceHandler {
    private final Die dieOne;
    private final Die dieTwo;

    // private constructor for tests, with injection
    DiceHandler(Die firstDie, Die secondDie) {
        this.dieOne = firstDie;
        this.dieTwo = secondDie;
    }

    // public constructor for usage
    public DiceHandler(){
        this.dieOne = new Die(new Random());
        this.dieTwo = new Die(new Random());
    }

    // Add two separate Die rolls together
    public int rollTwoDice() {
        int rollOne = dieOne.rollOneDie();
        int rollTwo = dieTwo.rollOneDie();

        return rollOne + rollTwo;
    }
}