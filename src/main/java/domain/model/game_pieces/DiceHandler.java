package domain.model.game_pieces;

public class DiceHandler {
    private final Die dieOne;
    private final Die dieTwo;

    public DiceHandler(Die firstDie, Die secondDie) {
        this.dieOne = firstDie;
        this.dieTwo = secondDie;
    }

    // Add two separate Die rolls together
    public int rollTwoDice() {
        int rollOne = dieOne.rollOneDie();
        int rollTwo = dieTwo.rollOneDie();

        return rollOne + rollTwo;
    }
}