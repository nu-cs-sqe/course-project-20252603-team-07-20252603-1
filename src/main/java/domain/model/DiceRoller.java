package domain.model;

/**
 * Interface for dice rolling abstraction.
 * Allows for deterministic testing via test implementations.
 */
public interface DiceRoller {

    /**
     * Rolls the dice and returns the result.
     *
     * @return the dice roll result
     */
    int roll();
}
