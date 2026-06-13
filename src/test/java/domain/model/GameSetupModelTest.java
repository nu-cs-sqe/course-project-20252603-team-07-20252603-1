package domain.model;

import domain.model.developmentcards.DevelopmentCardDeck;
import domain.model.player.PlayerColor;
import domain.model.resources.Resource;
import domain.model.resources.ResourceDeck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.model.developmentcards.DevelopmentCardDeck;
import domain.model.resources.ResourceDeck;
import org.easymock.EasyMock;

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
    void testIsColorAvailableAfterAddReturnsFalse() {
        model.addPlayer("Alice", PlayerColor.BLUE);
        assertFalse(model.isColorAvailable(PlayerColor.BLUE));
    }

    @Test
    void testGetTurnOrderAfterDetermineIsNonEmpty() {
        model.addPlayer("Alice", PlayerColor.RED);
        model.addPlayer("Bob", PlayerColor.BLUE);
        model.determineTurnOrder();
        assertFalse(model.getTurnOrder().isEmpty());
    }

    @Test
    void testGetResourceDeckReturnsTheSetDeck() {
        ResourceDeck deck = new ResourceDeck(Resource.LUMBER);
        model.setResourceDeck(deck);
        assertSame(deck, model.getResourceDeck());
    }

    @Test
    void testGetDevelopmentCardDeckReturnsTheSetDeck() {
        DevelopmentCardDeck deck = new DevelopmentCardDeck();
        model.setDevelopmentCardDeck(deck);
        assertSame(deck, model.getDevelopmentCardDeck());
    }

    @Test
    void testCreateGameModelWithPlayersReturnsNonNullModel() {
        model.addPlayer("Alice", PlayerColor.RED);
        model.addPlayer("Bob", PlayerColor.BLUE);
        assertNotNull(model.createGameModel());
    }
  // TC8 ← REDUCES CXTY
  @Test
  void isColorAvailable_UnusedColor_ExpectTrue() {
    assertTrue(model.isColorAvailable(PlayerColor.RED));
  }

  // TC9 ← REDUCES CXTY
  @Test
  void isColorAvailable_UsedColor_ExpectFalse() {
    model.addPlayer("Alice", PlayerColor.RED);
    assertFalse(model.isColorAvailable(PlayerColor.RED));
  }

  // TC10 ← REDUCES CXTY
  @Test
  void setResourceDeck_AndGetResourceDeck_ExpectSameInstance() {
    ResourceDeck mockDeck = EasyMock.createMock(ResourceDeck.class);
    EasyMock.replay(mockDeck);
    model.setResourceDeck(mockDeck);
    assertSame(mockDeck, model.getResourceDeck());
    EasyMock.verify(mockDeck);
  }

  // TC11 ← REDUCES CXTY
  @Test
  void getResourceDeck_BeforeSet_ExpectNull() {
    assertNull(model.getResourceDeck());
  }

  // TC12 ← REDUCES CXTY
  @Test
  void setDevelopmentCardDeck_AndGetDevelopmentCardDeck_ExpectSameInstance() {
    DevelopmentCardDeck mockDeck = EasyMock.createMock(DevelopmentCardDeck.class);
    EasyMock.replay(mockDeck);
    model.setDevelopmentCardDeck(mockDeck);
    assertSame(mockDeck, model.getDevelopmentCardDeck());
    EasyMock.verify(mockDeck);
  }

  // TC13 ← REDUCES CXTY
  @Test
  void getDevelopmentCardDeck_BeforeSet_ExpectNull() {
    assertNull(model.getDevelopmentCardDeck());
  }

  // TC14 ← REDUCES CXTY
  @Test
  void createGameModel_WithTwoPlayers_ExpectNonNullGameModel() {
    model.addPlayer("Alice", PlayerColor.RED);
    model.addPlayer("Bob", PlayerColor.BLUE);
    GameModel gameModel = model.createGameModel();
    assertNotNull(gameModel);
  }

  @Test
  void getBoard_FreshSetupModel_ReturnsNonNullBoard() {
    assertNotNull(model.getBoard());
  }

  // TC15 ← REDUCES CXTY
  @Test
  void getBoard_OnNewModel_ExpectNonNull() {
    assertNotNull(model.getBoard());
  }

}
