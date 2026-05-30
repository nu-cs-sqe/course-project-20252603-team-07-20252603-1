package domain.model;

class DiceHandler {
    private final Die dieOne;
    private final Die dieTwo;

    DiceHandler(Die firstDie, Die secondDie) {
        this.dieOne = firstDie;
        this.dieTwo = secondDie;
    }

    // Add two separate Die rolls together
    int rollTwoDice() {
        int rollOne = dieOne.rollOneDie();
        int rollTwo = dieTwo.rollOneDie();

        return rollOne + rollTwo;
    }
}