package domain.model;

/**
 * Test implementation of DiceRoller that returns a fixed value.
 * Used for deterministic testing.
 */
public class FixedDiceRoller {

    private final int fixedValue;

    public FixedDiceRoller(int fixedValue) {
        this.fixedValue = fixedValue;
    }

    public int roll() {
        return fixedValue;
    }
}
