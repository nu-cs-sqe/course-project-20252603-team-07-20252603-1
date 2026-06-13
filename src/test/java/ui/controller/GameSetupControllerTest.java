package ui.controller;

import domain.model.GameSetupModel;
import domain.model.board.BoardHandler;
import domain.model.developmentcards.DevelopmentCardDeck;
import domain.model.player.Player;
import domain.model.player.PlayerColor;
import domain.model.resources.ResourceDeck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.*;

class GameSetupControllerTest {

    private GameSetupController controller;
    private GameSetupModel mockModel;
    private BoardHandler mockBoard;

    @BeforeEach
    void setUp() {
        controller = new GameSetupController();
        mockModel = createMock(GameSetupModel.class);
        mockBoard = createMock(BoardHandler.class);
    }

    // ========== Full Validation Tests (BVA TC1–TC12) ==========

    @Test // TC1
    void testFullValidationNullNameReturnsNameEmpty() {
        replay(mockModel);

        PlayerAddResult result = controller.addPlayerWithFullValidation(mockModel, null, PlayerColor.RED);

        assertEquals(PlayerAddResult.NAME_EMPTY, result);
        verify(mockModel);
    }

    @Test // TC2
    void testFullValidationEmptyStringNameReturnsNameEmpty() {
        replay(mockModel);

        PlayerAddResult result = controller.addPlayerWithFullValidation(mockModel, "", PlayerColor.RED);

        assertEquals(PlayerAddResult.NAME_EMPTY, result);
        verify(mockModel);
    }

    @Test // TC3
    void testFullValidationSingleSpaceNameReturnsNameEmpty() {
        replay(mockModel);

        PlayerAddResult result = controller.addPlayerWithFullValidation(mockModel, " ", PlayerColor.RED);

        assertEquals(PlayerAddResult.NAME_EMPTY, result);
        verify(mockModel);
    }

    @Test // TC4
    void testFullValidationMixedWhitespaceNameReturnsNameEmpty() {
        replay(mockModel);

        PlayerAddResult result = controller.addPlayerWithFullValidation(mockModel, " \t\n ", PlayerColor.RED);

        assertEquals(PlayerAddResult.NAME_EMPTY, result);
        verify(mockModel);
    }

    @Test // TC5
    void testFullValidationSingleCharNameSucceeds() {
        expect(mockModel.isNameAvailable("A")).andReturn(true);
        expect(mockModel.isColorAvailable(PlayerColor.RED)).andReturn(true);
        mockModel.addPlayer("A", PlayerColor.RED);
        expectLastCall();
        replay(mockModel);

        PlayerAddResult result = controller.addPlayerWithFullValidation(mockModel, "A", PlayerColor.RED);

        assertEquals(PlayerAddResult.SUCCESS, result);
        verify(mockModel);
    }

    @Test // TC6
    void testFullValidationTrimsLeadingAndTrailingWhitespace() {
        expect(mockModel.isNameAvailable("Alice")).andReturn(true);
        expect(mockModel.isColorAvailable(PlayerColor.RED)).andReturn(true);
        mockModel.addPlayer("Alice", PlayerColor.RED);
        expectLastCall();
        replay(mockModel);

        PlayerAddResult result = controller.addPlayerWithFullValidation(mockModel, "  Alice  ", PlayerColor.RED);

        assertEquals(PlayerAddResult.SUCCESS, result);
        verify(mockModel);
    }

    @Test // TC7
    void testFullValidationDuplicateNameReturnsNameTaken() {
        expect(mockModel.isNameAvailable("Alice")).andReturn(false);
        replay(mockModel);

        PlayerAddResult result = controller.addPlayerWithFullValidation(mockModel, "Alice", PlayerColor.RED);

        assertEquals(PlayerAddResult.NAME_TAKEN, result);
        verify(mockModel);
    }

    @Test // TC8
    void testFullValidationNullColorReturnsColorEmpty() {
        expect(mockModel.isNameAvailable("Alice")).andReturn(true);
        replay(mockModel);

        PlayerAddResult result = controller.addPlayerWithFullValidation(mockModel, "Alice", null);

        assertEquals(PlayerAddResult.COLOR_EMPTY, result);
        verify(mockModel);
    }

    @Test // TC9
    void testFullValidationDuplicateColorReturnsColorTaken() {
        expect(mockModel.isNameAvailable("Alice")).andReturn(true);
        expect(mockModel.isColorAvailable(PlayerColor.RED)).andReturn(false);
        replay(mockModel);

        PlayerAddResult result = controller.addPlayerWithFullValidation(mockModel, "Alice", PlayerColor.RED);

        assertEquals(PlayerAddResult.COLOR_TAKEN, result);
        verify(mockModel);
    }

    @Test // TC10
    void testFullValidationAllValidReturnsSuccess() {
        expect(mockModel.isNameAvailable("Alice")).andReturn(true);
        expect(mockModel.isColorAvailable(PlayerColor.RED)).andReturn(true);
        mockModel.addPlayer("Alice", PlayerColor.RED);
        expectLastCall();
        replay(mockModel);

        PlayerAddResult result = controller.addPlayerWithFullValidation(mockModel, "Alice", PlayerColor.RED);

        assertEquals(PlayerAddResult.SUCCESS, result);
        verify(mockModel);
    }

    @Test // TC11
    void testFullValidationNameCheckedBeforeColor() {
        replay(mockModel);

        PlayerAddResult result = controller.addPlayerWithFullValidation(mockModel, "", null);

        assertEquals(PlayerAddResult.NAME_EMPTY, result);
        verify(mockModel);
    }

    @Test // TC12
    void testFullValidationDuplicateNameShortCircuitsColorCheck() {
        expect(mockModel.isNameAvailable("Alice")).andReturn(false);
        replay(mockModel);

        PlayerAddResult result = controller.addPlayerWithFullValidation(mockModel, "Alice", PlayerColor.RED);

        assertEquals(PlayerAddResult.NAME_TAKEN, result);
        verify(mockModel);
    }

    // ========== clearPlayers (BVA TC13) ==========

    @Test // TC13
    void testClearPlayersDelegatesToModel() {
        mockModel.clearPlayers();
        expectLastCall();
        replay(mockModel);

        controller.clearPlayers(mockModel);

        verify(mockModel);
    }

    // ========== validatePlayerCount (BVA TC14–TC17) ==========

    @Test // TC14
    void testInitializeGameWithThreePlayersSucceeds() {
        expect(mockModel.getPlayerCount()).andReturn(3);
        replay(mockModel);

        boolean result = controller.validatePlayerCount(mockModel);

        assertTrue(result);
        verify(mockModel);
    }

    @Test // TC15
    void testInitializeGameWithFourPlayersSucceeds() {
        expect(mockModel.getPlayerCount()).andReturn(4);
        replay(mockModel);

        boolean result = controller.validatePlayerCount(mockModel);

        assertTrue(result);
        verify(mockModel);
    }

    @Test // TC16
    void testInitializeGameWithTwoPlayersFailsValidation() {
        expect(mockModel.getPlayerCount()).andReturn(2);
        replay(mockModel);

        boolean result = controller.validatePlayerCount(mockModel);

        assertFalse(result);
        verify(mockModel);
    }

    @Test // TC17
    void testInitializeGameWithFivePlayersFailsValidation() {
        expect(mockModel.getPlayerCount()).andReturn(5);
        replay(mockModel);

        boolean result = controller.validatePlayerCount(mockModel);

        assertFalse(result);
        verify(mockModel);
    }

    // ========== addPlayer (BVA TC18) ==========

    @Test // TC18
    void testAddPlayerWithValidNameSucceeds() {
        String playerName = "Alice";
        mockModel.addPlayer(playerName, PlayerColor.RED);
        expectLastCall().once();
        replay(mockModel);

        controller.addPlayer(mockModel, playerName, PlayerColor.RED);

        verify(mockModel);
    }

    // ========== addPlayerWithColorValidation (BVA TC19–TC20) ==========

    @Test // TC19
    void testAddPlayerWithUniqueColorSucceeds() {
        expect(mockModel.isColorAvailable(PlayerColor.RED)).andReturn(true);
        mockModel.addPlayer("Alice", PlayerColor.RED);
        expectLastCall().once();
        replay(mockModel);

        boolean result = controller.addPlayerWithColorValidation(mockModel, "Alice", PlayerColor.RED);

        assertTrue(result);
        verify(mockModel);
    }

    @Test // TC20
    void testAddPlayerWithDuplicateColorFails() {
        expect(mockModel.isColorAvailable(PlayerColor.RED)).andReturn(false);
        replay(mockModel);

        boolean result = controller.addPlayerWithColorValidation(mockModel, "Bob", PlayerColor.RED);

        assertFalse(result);
        verify(mockModel);
    }

    // ========== getPlayerName (BVA TC21) ==========

    @Test // TC21
    void testPlayerNamesAreStoredCorrectly() {
        Player mockPlayer = createMock(Player.class);
        expect(mockPlayer.getName()).andReturn("Alice").once();
        expect(mockModel.getPlayer(0)).andReturn(mockPlayer);
        replay(mockModel, mockPlayer);

        String name = controller.getPlayerName(mockModel, 0);

        assertEquals("Alice", name);
        verify(mockModel, mockPlayer);
    }

    // ========== getPlayerCount (BVA TC22) ==========

    @Test // TC22
    void testGetPlayerCountReturnsCorrectValue() {
        expect(mockModel.getPlayerCount()).andReturn(4);
        replay(mockModel);

        int count = controller.getPlayerCount(mockModel);

        assertEquals(4, count);
        verify(mockModel);
    }

    // ========== getBoardHexCount (BVA TC23) ==========

    @Test // TC23
    void testBoardIsInitializedWithNineteenHexes() {
        expect(mockBoard.getHexCount()).andReturn(19);
        replay(mockBoard);

        int hexCount = controller.getBoardHexCount(mockBoard);

        assertEquals(19, hexCount);
        verify(mockBoard);
    }

    // ========== getHexOrder (BVA TC24) ==========

    @Test // TC24
    void testHexOrderIsDetermined() {
        List<String> mockHexOrder = List.of("WHEAT", "SHEEP", "WOOD", "BRICK", "ORE");
        expect(mockBoard.getHexOrder()).andReturn(mockHexOrder);
        replay(mockBoard);

        List<String> hexOrder = controller.getHexOrder(mockBoard);

        assertNotNull(hexOrder);
        assertFalse(hexOrder.isEmpty());
        verify(mockBoard);
    }

    // ========== getResourceDeck (BVA TC25) ==========

    @Test // TC25
    void testResourceDeckIsInitialized() {
        ResourceDeck mockResourceDeck = createMock(ResourceDeck.class);
        expect(mockModel.getResourceDeck()).andReturn(mockResourceDeck);
        replay(mockModel);

        ResourceDeck deck = controller.getResourceDeck(mockModel);

        assertNotNull(deck);
        verify(mockModel);
    }

    // ========== initializeResourceDeck (BVA TC26) ==========

    @Test // TC26
    void testInitializeResourceDeckCreatesValidDeck() {
        mockModel.setResourceDeck(anyObject(ResourceDeck.class));
        expectLastCall().once();
        replay(mockModel);

        controller.initializeResourceDeck(mockModel);

        verify(mockModel);
    }

    // ========== getDevelopmentCardDeck (BVA TC27) ==========

    @Test // TC27
    void testDevelopmentCardDeckIsInitialized() {
        DevelopmentCardDeck mockDevDeck = createMock(DevelopmentCardDeck.class);
        expect(mockModel.getDevelopmentCardDeck()).andReturn(mockDevDeck);
        replay(mockModel);

        DevelopmentCardDeck deck = controller.getDevelopmentCardDeck(mockModel);

        assertNotNull(deck);
        verify(mockModel);
    }

    // ========== initializeDevelopmentCardDeck (BVA TC28) ==========

    @Test // TC28
    void testInitializeDevelopmentCardDeckCreatesValidDeck() {
        mockModel.setDevelopmentCardDeck(anyObject(DevelopmentCardDeck.class));
        expectLastCall().once();
        replay(mockModel);

        controller.initializeDevelopmentCardDeck(mockModel);

        verify(mockModel);
    }

    // ========== determineTurnOrder (BVA TC29) ==========

    @Test // TC29
    void testDetermineTurnOrderWithThreePlayers() {
        Player player1 = createMock(Player.class);
        Player player2 = createMock(Player.class);
        Player player3 = createMock(Player.class);
        List<Player> expectedOrder = List.of(player1, player2, player3);

        mockModel.determineTurnOrder();
        expectLastCall().once();
        expect(mockModel.getTurnOrder()).andReturn(expectedOrder);
        replay(mockModel);

        controller.determineTurnOrder(mockModel);
        List<Player> turnOrder = controller.getTurnOrder(mockModel);

        assertEquals(3, turnOrder.size());
        verify(mockModel);
    }

    // ========== getTurnOrder (BVA TC30) ==========

    @Test // TC30
    void testTurnOrderIsDetermined() {
        List<Player> mockPlayers = List.of(
                createMock(Player.class),
                createMock(Player.class),
                createMock(Player.class)
        );
        expect(mockModel.getTurnOrder()).andReturn(mockPlayers);
        replay(mockModel);

        List<Player> turnOrder = controller.getTurnOrder(mockModel);

        assertNotNull(turnOrder);
        assertFalse(turnOrder.isEmpty());
        verify(mockModel);
    }
}
