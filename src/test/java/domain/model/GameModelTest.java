package domain.model;

import domain.model.board.BoardHandler;
import domain.model.player.Player;
import domain.model.player.PlayerColor;
import domain.model.resources.Resource;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class GameModelTest {

    @Test
    void testGameModelInitializesWithTurnOrder() {
        BoardHandler boardMock = EasyMock.createMock(BoardHandler.class);
        EasyMock.replay(boardMock);
        // Create players
        Player player1 = new Player("Alice", PlayerColor.RED);
        Player player2 = new Player("Bob", PlayerColor.BLUE);
        Player player3 = new Player("Charlie", PlayerColor.WHITE);
        List<Player> players = List.of(player1, player2, player3);

        // Create GameModel with turn order
        GameModel gameModel = new GameModel(players, boardMock);

        // Verify turn order is stored
        assertEquals(players, gameModel.getTurnOrder());
    }

    @Test
    void testGameModelStartsWithFirstPlayer() {
        BoardHandler boardMock = EasyMock.createMock(BoardHandler.class);
        EasyMock.replay(boardMock);
        // Create players
        Player player1 = new Player("Alice", PlayerColor.RED);
        Player player2 = new Player("Bob", PlayerColor.BLUE);
        Player player3 = new Player("Charlie", PlayerColor.WHITE);
        List<Player> players = List.of(player1, player2, player3);

        // Create GameModel
        GameModel gameModel = new GameModel(players, boardMock);

        // Verify starts with first player (index 0)
        assertEquals(0, gameModel.getCurrentPlayerIndex());
    }

    @Test
    void testGetCurrentPlayerReturnsFirstPlayer() {
        BoardHandler boardMock = EasyMock.createMock(BoardHandler.class);
        EasyMock.replay(boardMock);
        // Create players
        Player player1 = new Player("Alice", PlayerColor.RED);
        Player player2 = new Player("Bob", PlayerColor.BLUE);
        Player player3 = new Player("Charlie", PlayerColor.WHITE);
        List<Player> players = List.of(player1, player2, player3);

        // Create GameModel
        GameModel gameModel = new GameModel(players, boardMock);

        // Verify getCurrentPlayer returns first player
        assertEquals(player1, gameModel.getCurrentPlayer());
    }

    @Test
    void testAdvanceToNextPlayerMovesToSecondPlayer() {
        BoardHandler boardMock = EasyMock.createMock(BoardHandler.class);
        EasyMock.replay(boardMock);
        // Create players
        Player player1 = new Player("Alice", PlayerColor.RED);
        Player player2 = new Player("Bob", PlayerColor.BLUE);
        Player player3 = new Player("Charlie", PlayerColor.WHITE);
        List<Player> players = List.of(player1, player2, player3);

        // Create GameModel
        GameModel gameModel = new GameModel(players, boardMock);

        // Advance to next player
        gameModel.advanceToNextPlayer();

        // Verify current player is now second player
        assertEquals(player2, gameModel.getCurrentPlayer());
    }

    @Test
    void testAdvanceToNextPlayerWrapsAroundToFirstPlayer() {
        BoardHandler boardMock = EasyMock.createMock(BoardHandler.class);
        EasyMock.replay(boardMock);
        // Create players
        Player player1 = new Player("Alice", PlayerColor.RED);
        Player player2 = new Player("Bob", PlayerColor.BLUE);
        Player player3 = new Player("Charlie", PlayerColor.WHITE);
        List<Player> players = List.of(player1, player2, player3);

        // Create GameModel
        GameModel gameModel = new GameModel(players, boardMock);

        // Advance through all players (3 times)
        gameModel.advanceToNextPlayer();
        gameModel.advanceToNextPlayer();
        gameModel.advanceToNextPlayer();

        // Verify wrapped back to first player
        assertEquals(player1, gameModel.getCurrentPlayer());
    }

//    @Test
//    void testPerformTurnRollsDiceAndDistributesResource() {
//        BoardHandler boardMock = EasyMock.createMock(BoardHandler.class);
//        EasyMock.replay(boardMock);
//        // Create players
//        Player player1 = new Player("Alice", PlayerColor.RED);
//        Player player2 = new Player("Bob", PlayerColor.BLUE);
//        List<Player> players = List.of(player1, player2);
//
//        // Create GameModel with player states
//        GameModel gameModel = new GameModel(players, boardMock);
//
//        // Perform turn
//        gameModel.performTurn(7);
//
//        // Verify first player received one resource
//        assertEquals(1, gameModel.getArbitraryPlayer(PlayerColor.RED).getResourceCount(Resource.WOOL));
//
//        // Verify second player has no resources
//        assertEquals(0, gameModel.getArbitraryPlayer(PlayerColor.BLUE).getResourceCount(Resource.WOOL));
//    }
}
