package domain.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DiceRollerTest {

    @Test
    void testFixedDiceRollerReturnsConfiguredValue() {
        // Create fixed dice roller with value 7
        DiceRoller diceRoller = new FixedDiceRoller(7);

        // Roll dice
        int result = diceRoller.roll();

        // Verify returns configured value
        assertEquals(7, result);
    }
}
