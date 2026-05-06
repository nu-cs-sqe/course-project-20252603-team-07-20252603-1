package domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameSetupModelTest {

    private GameSetupModel model;

    @BeforeEach
    void setUp() {
        model = new GameSetupModel();
    }

    @Test
    void testIsNameAvailableForUnusedNameReturnsTrue() {
        assertTrue(model.isNameAvailable("Alice"));
    }

    @Test
    void testIsNameAvailableAfterAddReturnsFalse() {
        model.addPlayer("Alice", "Red");
        assertFalse(model.isNameAvailable("Alice"));
    }
}
