package domain.model;

import domain.model.player.PlayerColor;
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
        model.addPlayer("Alice", PlayerColor.RED);
        assertFalse(model.isNameAvailable("Alice"));
    }

    @Test
    void testIsNameAvailableIsCaseSensitive() {
        model.addPlayer("Alice", PlayerColor.RED);
        assertTrue(model.isNameAvailable("alice"));
    }

    @Test
    void testIsNameAvailableForEmptyStringReturnsTrue() {
        assertTrue(model.isNameAvailable(""));
    }

    @Test
    void testIsNameAvailableDistinguishesAcrossNames() {
        model.addPlayer("Alice", PlayerColor.RED);
        assertFalse(model.isNameAvailable("Alice"));
        assertTrue(model.isNameAvailable("Bob"));
    }

    @Test
    void testClearPlayersResetsAllSetupPlayerState() {
        model.addPlayer("Alice", PlayerColor.RED);
        model.addPlayer("Bob", PlayerColor.BLUE);
        model.determineTurnOrder();

        model.clearPlayers();

        assertEquals(0, model.getPlayerCount());
        assertTrue(model.isNameAvailable("Alice"));
        assertTrue(model.isColorAvailable(PlayerColor.RED));
        assertTrue(model.getTurnOrder().isEmpty());
    }

    @Test
    void testClearPlayersAllowsReusingNamesAndColors() {
        model.addPlayer("Alice", PlayerColor.RED);
        model.clearPlayers();

        model.addPlayer("Alice", PlayerColor.RED);

        assertEquals(1, model.getPlayerCount());
        assertEquals("Alice", model.getPlayer(0).getName());
        assertEquals(PlayerColor.RED, model.getPlayer(0).getColor());
    }

  @Test
  void isColorAvailable_UnusedColor_ExpectTrue() {
    assertTrue(model.isColorAvailable(PlayerColor.RED));
  }
}
