package domain.model;

import domain.model.player.Player;
import domain.model.player.PlayerColor;
import domain.model.player.PlayerState;
import domain.model.resources.Resource;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlayerStateTest {

    @Test
    void testPlayerReceivesResource() {
        // Create player and player state
        Player player = new Player("Alice", PlayerColor.RED);
        PlayerState playerState = new PlayerState(player);

        // Add resources
        playerState.addResource(Resource.LUMBER);
        playerState.addResource(Resource.LUMBER);

        // Verify resource count
        assertEquals(2, playerState.getResourceCount(Resource.LUMBER));
    }
}
