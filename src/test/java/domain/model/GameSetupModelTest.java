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

    @Test
    void testIsNameAvailableIsCaseSensitive() {
        model.addPlayer("Alice", "Red");
        assertTrue(model.isNameAvailable("alice"));
    }

    @Test
    void testIsNameAvailableForEmptyStringReturnsTrue() {
        assertTrue(model.isNameAvailable(""));
    }

    @Test
    void testIsNameAvailableDistinguishesAcrossNames() {
        model.addPlayer("Alice", "Red");
        assertFalse(model.isNameAvailable("Alice"));
        assertTrue(model.isNameAvailable("Bob"));
    }

    @Test
    void testClearPlayersResetsAllSetupPlayerState() {
        model.addPlayer("Alice", "Red");
        model.addPlayer("Bob", "Blue");
        model.determineTurnOrder();

        model.clearPlayers();

        assertEquals(0, model.getPlayerCount());
        assertTrue(model.isNameAvailable("Alice"));
        assertTrue(model.isColorAvailable("Red"));
        assertTrue(model.getTurnOrder().isEmpty());
    }
}
