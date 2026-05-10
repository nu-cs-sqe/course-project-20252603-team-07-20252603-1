package ui.controller;

import domain.model.Board;
import domain.model.GameSetupModel;
import domain.model.Player;
import domain.model.development_cards.DevelopmentCardDeck;
import domain.model.resources.ResourceDeck;
import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for GameSetupController following TDD and SOLID principles.
 * Tests game initialization including player setup, board creation, and game piece initialization.
 */
class GameSetupControllerTest {

    private GameSetupController controller;
    private GameSetupModel mockModel;
    private Board mockBoard;

    @BeforeEach
    void setUp() {
        controller = new GameSetupController();
        mockModel = createMock(GameSetupModel.class);
        mockBoard = createMock(Board.class);
    }

    // ========== Player Count Validation Tests ==========

    @Test
    void testInitializeGameWithThreePlayersSucceeds() {
        // Arrange
        expect(mockModel.getPlayerCount()).andReturn(3);
        replay(mockModel);

        // Act
        boolean result = controller.validatePlayerCount(mockModel);

        // Assert
        assertTrue(result);
        verify(mockModel);
    }

    @Test
    void testInitializeGameWithFourPlayersSucceeds() {
        // Arrange
        expect(mockModel.getPlayerCount()).andReturn(4);
        replay(mockModel);

        // Act
        boolean result = controller.validatePlayerCount(mockModel);

        // Assert
        assertTrue(result);
        verify(mockModel);
    }

    @Test
    void testInitializeGameWithTwoPlayersFailsValidation() {
        // Arrange
        expect(mockModel.getPlayerCount()).andReturn(2);
        replay(mockModel);

        // Act
        boolean result = controller.validatePlayerCount(mockModel);

        // Assert
        assertFalse(result);
        verify(mockModel);
    }

    @Test
    void testInitializeGameWithFivePlayersFailsValidation() {
        // Arrange
        expect(mockModel.getPlayerCount()).andReturn(5);
        replay(mockModel);

        // Act
        boolean result = controller.validatePlayerCount(mockModel);

        // Assert
        assertFalse(result);
        verify(mockModel);
    }

    @Test
    void testInitializeGameWithZeroPlayersFailsValidation() {
        // Arrange
        expect(mockModel.getPlayerCount()).andReturn(0);
        replay(mockModel);

        // Act
        boolean result = controller.validatePlayerCount(mockModel);

        // Assert
        assertFalse(result);
        verify(mockModel);
    }

    // ========== Player Name Assignment Tests ==========

    @Test
    void testAddPlayerWithValidNameSucceeds() {
        // Arrange
        String playerName = "Alice";
        mockModel.addPlayer(playerName, "RED");
        expectLastCall().once();
        replay(mockModel);

        // Act
        controller.addPlayer(mockModel, playerName, "RED");

        // Assert
        verify(mockModel);
    }

    @Test
    void testAddMultiplePlayersWithDifferentNames() {
        // Arrange
        mockModel.addPlayer("Alice", "RED");
        expectLastCall().once();
        mockModel.addPlayer("Bob", "BLUE");
        expectLastCall().once();
        mockModel.addPlayer("Charlie", "WHITE");
        expectLastCall().once();
        replay(mockModel);

        // Act
        controller.addPlayer(mockModel, "Alice", "RED");
        controller.addPlayer(mockModel, "Bob", "BLUE");
        controller.addPlayer(mockModel, "Charlie", "WHITE");

        // Assert
        verify(mockModel);
    }

    @Test
    void testPlayerNamesAreStoredCorrectly() {
        // Arrange
        Player mockPlayer = createMock(Player.class);
        expect(mockPlayer.getName()).andReturn("Alice").once();
        expect(mockModel.getPlayer(0)).andReturn(mockPlayer);
        replay(mockModel, mockPlayer);

        // Act
        String name = controller.getPlayerName(mockModel, 0);

        // Assert
        assertEquals("Alice", name);
        verify(mockModel, mockPlayer);
    }

    // ========== Exclusive Color Selection Tests ==========

    @Test
    void testAddPlayerWithUniqueColorSucceeds() {
        // Arrange
        expect(mockModel.isColorAvailable("RED")).andReturn(true);
        mockModel.addPlayer("Alice", "RED");
        expectLastCall().once();
        replay(mockModel);

        // Act
        boolean result = controller.addPlayerWithColorValidation(mockModel, "Alice", "RED");

        // Assert
        assertTrue(result);
        verify(mockModel);
    }

    @Test
    void testAddPlayerWithDuplicateColorFails() {
        // Arrange
        expect(mockModel.isColorAvailable("RED")).andReturn(false);
        replay(mockModel);

        // Act
        boolean result = controller.addPlayerWithColorValidation(mockModel, "Bob", "RED");

        // Assert
        assertFalse(result);
        verify(mockModel);
    }

    @Test
    void testEachPlayerHasExclusiveColor() {
        // Arrange
        expect(mockModel.isColorAvailable("RED")).andReturn(true);
        mockModel.addPlayer("Alice", "RED");
        expectLastCall().once();

        expect(mockModel.isColorAvailable("BLUE")).andReturn(true);
        mockModel.addPlayer("Bob", "BLUE");
        expectLastCall().once();

        expect(mockModel.isColorAvailable("RED")).andReturn(false);
        replay(mockModel);

        // Act
        boolean result1 = controller.addPlayerWithColorValidation(mockModel, "Alice", "RED");
        boolean result2 = controller.addPlayerWithColorValidation(mockModel, "Bob", "BLUE");
        boolean result3 = controller.addPlayerWithColorValidation(mockModel, "Charlie", "RED");

        // Assert
        assertTrue(result1);
        assertTrue(result2);
        assertFalse(result3);
        verify(mockModel);
    }

    @Test
    void testAllFourColorsCanBeAssignedExclusively() {
        // Arrange
        expect(mockModel.isColorAvailable("RED")).andReturn(true);
        mockModel.addPlayer("Alice", "RED");
        expectLastCall().once();

        expect(mockModel.isColorAvailable("BLUE")).andReturn(true);
        mockModel.addPlayer("Bob", "BLUE");
        expectLastCall().once();

        expect(mockModel.isColorAvailable("WHITE")).andReturn(true);
        mockModel.addPlayer("Charlie", "WHITE");
        expectLastCall().once();

        expect(mockModel.isColorAvailable("ORANGE")).andReturn(true);
        mockModel.addPlayer("Diana", "ORANGE");
        expectLastCall().once();

        replay(mockModel);

        // Act & Assert
        assertTrue(controller.addPlayerWithColorValidation(mockModel, "Alice", "RED"));
        assertTrue(controller.addPlayerWithColorValidation(mockModel, "Bob", "BLUE"));
        assertTrue(controller.addPlayerWithColorValidation(mockModel, "Charlie", "WHITE"));
        assertTrue(controller.addPlayerWithColorValidation(mockModel, "Diana", "ORANGE"));
        verify(mockModel);
    }

    // ========== Board Initialization Tests ==========

    @Test
    void testBoardIsInitializedWithNineteenHexes() {
        // Arrange
        expect(mockBoard.getHexCount()).andReturn(19);
        replay(mockBoard);

        // Act
        int hexCount = controller.getBoardHexCount(mockBoard);

        // Assert
        assertEquals(19, hexCount);
        verify(mockBoard);
    }

    @Test
    void testInitializeBoardCreatesBoard() {
        // Arrange
        expect(mockModel.getBoard()).andReturn(mockBoard).times(2);
        expect(mockBoard.getHexCount()).andReturn(19);
        replay(mockModel, mockBoard);

        // Act
        controller.initializeBoard(mockModel);
        int hexCount = controller.getBoardHexCount(mockModel.getBoard());

        // Assert
        assertEquals(19, hexCount);
        verify(mockModel, mockBoard);
    }

    @Test
    void testBoardIsNotNull() {
        // Arrange
        expect(mockModel.getBoard()).andReturn(mockBoard);
        replay(mockModel);

        // Act
        Board board = controller.getBoard(mockModel);

        // Assert
        assertNotNull(board);
        verify(mockModel);
    }

    // ========== Hex Order Determination Tests ==========

    @Test
    void testHexOrderIsDetermined() {
        // Arrange
        List<String> mockHexOrder = List.of("WHEAT", "SHEEP", "WOOD", "BRICK", "ORE");
        expect(mockBoard.getHexOrder()).andReturn(mockHexOrder);
        replay(mockBoard);

        // Act
        List<String> hexOrder = controller.getHexOrder(mockBoard);

        // Assert
        assertNotNull(hexOrder);
        assertFalse(hexOrder.isEmpty());
        verify(mockBoard);
    }

    @Test
    void testHexOrderContainsNineteenElements() {
        // Arrange
        List<String> mockHexOrder = List.of(
            "WHEAT", "SHEEP", "WOOD", "BRICK", "ORE",
            "WHEAT", "SHEEP", "WOOD", "BRICK", "ORE",
            "WHEAT", "SHEEP", "WOOD", "BRICK", "ORE",
            "WHEAT", "SHEEP", "WOOD", "DESERT"
        );
        expect(mockBoard.getHexOrder()).andReturn(mockHexOrder);
        replay(mockBoard);

        // Act
        List<String> hexOrder = controller.getHexOrder(mockBoard);

        // Assert
        assertEquals(19, hexOrder.size());
        verify(mockBoard);
    }

    @Test
    void testHexOrderIsNotEmpty() {
        // Arrange
        List<String> mockHexOrder = List.of("WHEAT", "SHEEP", "WOOD");
        expect(mockBoard.getHexOrder()).andReturn(mockHexOrder);
        replay(mockBoard);

        // Act
        List<String> hexOrder = controller.getHexOrder(mockBoard);

        // Assert
        assertNotNull(hexOrder);
        assertFalse(hexOrder.isEmpty());
        verify(mockBoard);
    }

    // ========== Resource Deck Initialization Tests ==========

    @Test
    void testResourceDeckIsInitialized() {
        // Arrange
        ResourceDeck mockResourceDeck = createMock(ResourceDeck.class);
        expect(mockModel.getResourceDeck()).andReturn(mockResourceDeck);
        replay(mockModel);

        // Act
        ResourceDeck deck = controller.getResourceDeck(mockModel);

        // Assert
        assertNotNull(deck);
        verify(mockModel);
    }

    @Test
    void testInitializeResourceDeckCreatesValidDeck() {
        // Arrange
        ResourceDeck mockResourceDeck = createMock(ResourceDeck.class);
        expect(mockResourceDeck.getTotalCards()).andReturn(95);
        mockModel.setResourceDeck(anyObject(ResourceDeck.class));
        expectLastCall().once();
        replay(mockModel, mockResourceDeck);

        // Act
        controller.initializeResourceDeck(mockModel);

        // Assert
        verify(mockModel);
    }

    @Test
    void testResourceDeckIsNotNullAfterInitialization() {
        // Arrange
        ResourceDeck mockResourceDeck = createMock(ResourceDeck.class);
        expect(mockModel.getResourceDeck()).andReturn(mockResourceDeck);
        replay(mockModel);

        // Act
        ResourceDeck deck = controller.getResourceDeck(mockModel);

        // Assert
        assertNotNull(deck);
        verify(mockModel);
    }

    // ========== Development Card Deck Initialization Tests ==========

    @Test
    void testDevelopmentCardDeckIsInitialized() {
        // Arrange
        DevelopmentCardDeck mockDevDeck = createMock(DevelopmentCardDeck.class);
        expect(mockModel.getDevelopmentCardDeck()).andReturn(mockDevDeck);
        replay(mockModel);

        // Act
        DevelopmentCardDeck deck = controller.getDevelopmentCardDeck(mockModel);

        // Assert
        assertNotNull(deck);
        verify(mockModel);
    }

    @Test
    void testInitializeDevelopmentCardDeckCreatesValidDeck() {
        // Arrange
        DevelopmentCardDeck mockDevDeck = createMock(DevelopmentCardDeck.class);
        expect(mockDevDeck.countRemaining()).andReturn(25);
        mockModel.setDevelopmentCardDeck(anyObject(DevelopmentCardDeck.class));
        expectLastCall().once();
        replay(mockModel, mockDevDeck);

        // Act
        controller.initializeDevelopmentCardDeck(mockModel);

        // Assert
        verify(mockModel);
    }

    @Test
    void testDevelopmentCardDeckIsNotNullAfterInitialization() {
        // Arrange
        DevelopmentCardDeck mockDevDeck = createMock(DevelopmentCardDeck.class);
        expect(mockModel.getDevelopmentCardDeck()).andReturn(mockDevDeck);
        replay(mockModel);

        // Act
        DevelopmentCardDeck deck = controller.getDevelopmentCardDeck(mockModel);

        // Assert
        assertNotNull(deck);
        verify(mockModel);
    }

    // ========== Turn Order Determination Tests ==========

    @Test
    void testTurnOrderIsDetermined() {
        // Arrange
        List<Player> mockPlayers = List.of(
            createMock(Player.class),
            createMock(Player.class),
            createMock(Player.class)
        );
        expect(mockModel.getTurnOrder()).andReturn(mockPlayers);
        replay(mockModel);

        // Act
        List<Player> turnOrder = controller.getTurnOrder(mockModel);

        // Assert
        assertNotNull(turnOrder);
        assertFalse(turnOrder.isEmpty());
        verify(mockModel);
    }

    @Test
    void testTurnOrderContainsAllPlayers() {
        // Arrange
        Player player1 = createMock(Player.class);
        Player player2 = createMock(Player.class);
        Player player3 = createMock(Player.class);
        List<Player> mockPlayers = List.of(player1, player2, player3);

        expect(mockModel.getPlayerCount()).andReturn(3);
        expect(mockModel.getTurnOrder()).andReturn(mockPlayers);
        replay(mockModel);

        // Act
        List<Player> turnOrder = controller.getTurnOrder(mockModel);
        int playerCount = controller.getPlayerCount(mockModel);

        // Assert
        assertEquals(playerCount, turnOrder.size());
        verify(mockModel);
    }

    @Test
    void testTurnOrderIsNotEmpty() {
        // Arrange
        List<Player> mockPlayers = List.of(
            createMock(Player.class),
            createMock(Player.class),
            createMock(Player.class),
            createMock(Player.class)
        );
        expect(mockModel.getTurnOrder()).andReturn(mockPlayers);
        replay(mockModel);

        // Act
        List<Player> turnOrder = controller.getTurnOrder(mockModel);

        // Assert
        assertNotNull(turnOrder);
        assertFalse(turnOrder.isEmpty());
        verify(mockModel);
    }

    @Test
    void testDetermineTurnOrderWithThreePlayers() {
        // Arrange
        Player player1 = createMock(Player.class);
        Player player2 = createMock(Player.class);
        Player player3 = createMock(Player.class);
        List<Player> expectedOrder = List.of(player1, player2, player3);

        mockModel.determineTurnOrder();
        expectLastCall().once();
        expect(mockModel.getTurnOrder()).andReturn(expectedOrder);
        replay(mockModel);

        // Act
        controller.determineTurnOrder(mockModel);
        List<Player> turnOrder = controller.getTurnOrder(mockModel);

        // Assert
        assertEquals(3, turnOrder.size());
        verify(mockModel);
    }

    @Test
    void testDetermineTurnOrderWithFourPlayers() {
        // Arrange
        Player player1 = createMock(Player.class);
        Player player2 = createMock(Player.class);
        Player player3 = createMock(Player.class);
        Player player4 = createMock(Player.class);
        List<Player> expectedOrder = List.of(player1, player2, player3, player4);

        mockModel.determineTurnOrder();
        expectLastCall().once();
        expect(mockModel.getTurnOrder()).andReturn(expectedOrder);
        replay(mockModel);

        // Act
        controller.determineTurnOrder(mockModel);
        List<Player> turnOrder = controller.getTurnOrder(mockModel);

        // Assert
        assertEquals(4, turnOrder.size());
        verify(mockModel);
    }

    // ========== Complete Game Initialization Integration Tests ==========

    @Test
    void testCompleteGameInitializationSequence() {
        // Arrange
        ResourceDeck mockResourceDeck = createMock(ResourceDeck.class);
        DevelopmentCardDeck mockDevDeck = createMock(DevelopmentCardDeck.class);
        List<Player> mockPlayers = List.of(
            createMock(Player.class),
            createMock(Player.class),
            createMock(Player.class)
        );

        expect(mockModel.getPlayerCount()).andReturn(3);
        expect(mockModel.getBoard()).andReturn(mockBoard);
        expect(mockModel.getResourceDeck()).andReturn(mockResourceDeck);
        expect(mockModel.getDevelopmentCardDeck()).andReturn(mockDevDeck);
        expect(mockModel.getTurnOrder()).andReturn(mockPlayers);
        expect(mockBoard.getHexCount()).andReturn(19);

        replay(mockModel, mockBoard);

        // Act
        boolean validPlayerCount = controller.validatePlayerCount(mockModel);
        Board board = controller.getBoard(mockModel);
        ResourceDeck resourceDeck = controller.getResourceDeck(mockModel);
        DevelopmentCardDeck devDeck = controller.getDevelopmentCardDeck(mockModel);
        List<Player> turnOrder = controller.getTurnOrder(mockModel);
        int hexCount = controller.getBoardHexCount(board);

        // Assert
        assertTrue(validPlayerCount);
        assertNotNull(board);
        assertNotNull(resourceDeck);
        assertNotNull(devDeck);
        assertNotNull(turnOrder);
        assertEquals(19, hexCount);
        assertEquals(3, turnOrder.size());
        verify(mockModel, mockBoard);
    }

    @Test
    void testGameInitializationFailsWithInvalidPlayerCount() {
        // Arrange
        expect(mockModel.getPlayerCount()).andReturn(2);
        replay(mockModel);

        // Act
        boolean result = controller.validatePlayerCount(mockModel);

        // Assert
        assertFalse(result);
        verify(mockModel);
    }

    // ========== Helper Method Tests ==========

    @Test
    void testGetPlayerCountReturnsCorrectValue() {
        // Arrange
        expect(mockModel.getPlayerCount()).andReturn(4);
        replay(mockModel);

        // Act
        int count = controller.getPlayerCount(mockModel);

        // Assert
        assertEquals(4, count);
        verify(mockModel);
    }
}
