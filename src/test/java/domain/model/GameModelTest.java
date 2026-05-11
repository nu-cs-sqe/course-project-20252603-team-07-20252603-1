package domain.model;

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
}
