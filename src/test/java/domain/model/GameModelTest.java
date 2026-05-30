package domain.model;

import domain.model.resources.ResourceDeck;
import domain.model.resources.ResourceType;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class GameModelTest {

    @Test
    void testGameModelInitializesWithTurnOrder() {
        // Create players
        Player player1 = new Player("Alice", "RED");
        Player player2 = new Player("Bob", "BLUE");
        Player player3 = new Player("Charlie", "WHITE");
        List<Player> players = List.of(player1, player2, player3);

        // Create GameModel with turn order
        GameModel gameModel = new GameModel(players);

        // Verify turn order is stored
        assertEquals(players, gameModel.getTurnOrder());
    }

    @Test
    void testGameModelStartsWithFirstPlayer() {
        // Create players
        Player player1 = new Player("Alice", "RED");
        Player player2 = new Player("Bob", "BLUE");
        Player player3 = new Player("Charlie", "WHITE");
        List<Player> players = List.of(player1, player2, player3);

        // Create GameModel
        GameModel gameModel = new GameModel(players);

        // Verify starts with first player (index 0)
        assertEquals(0, gameModel.getCurrentPlayerIndex());
    }

    @Test
    void testGetCurrentPlayerReturnsFirstPlayer() {
        // Create players
        Player player1 = new Player("Alice", "RED");
        Player player2 = new Player("Bob", "BLUE");
        Player player3 = new Player("Charlie", "WHITE");
        List<Player> players = List.of(player1, player2, player3);

        // Create GameModel
        GameModel gameModel = new GameModel(players);

        // Verify getCurrentPlayer returns first player
        assertEquals(player1, gameModel.getCurrentPlayer());
    }

    @Test
    void testAdvanceToNextPlayerMovesToSecondPlayer() {
        // Create players
        Player player1 = new Player("Alice", "RED");
        Player player2 = new Player("Bob", "BLUE");
        Player player3 = new Player("Charlie", "WHITE");
        List<Player> players = List.of(player1, player2, player3);

        // Create GameModel
        GameModel gameModel = new GameModel(players);

        // Advance to next player
        gameModel.advanceToNextPlayer();

        // Verify current player is now second player
        assertEquals(player2, gameModel.getCurrentPlayer());
    }

    @Test
    void testAdvanceToNextPlayerWrapsAroundToFirstPlayer() {
        // Create players
        Player player1 = new Player("Alice", "RED");
        Player player2 = new Player("Bob", "BLUE");
        Player player3 = new Player("Charlie", "WHITE");
        List<Player> players = List.of(player1, player2, player3);

        // Create GameModel
        GameModel gameModel = new GameModel(players);

        // Advance through all players (3 times)
        gameModel.advanceToNextPlayer();
        gameModel.advanceToNextPlayer();
        gameModel.advanceToNextPlayer();

        // Verify wrapped back to first player
        assertEquals(player1, gameModel.getCurrentPlayer());
    }

    @Test
    void testPerformTurnRollsDiceAndDistributesResource() {
        // Create players
        Player player1 = new Player("Alice", "RED");
        Player player2 = new Player("Bob", "BLUE");
        List<Player> players = List.of(player1, player2);

        // Create GameModel with player states
        GameModel gameModel = new GameModel(players);

        // Create fixed dice roller and resource deck
        DiceRoller diceRoller = new FixedDiceRoller(7);
        ResourceDeck resourceDeck = new ResourceDeck(ResourceType.WOOD);

        // Perform turn
        gameModel.performTurn(diceRoller, resourceDeck);

        // Verify first player received one WOOD resource
        assertEquals(1, gameModel.getPlayerState(0).getResourceCount(ResourceType.WOOD));

        // Verify second player has no resources
        assertEquals(0, gameModel.getPlayerState(1).getResourceCount(ResourceType.WOOD));
    }
}
