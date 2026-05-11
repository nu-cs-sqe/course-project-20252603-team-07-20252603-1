package domain.model;

import domain.model.resources.ResourceCard;
import domain.model.resources.ResourceType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlayerStateTest {

    @Test
    void testPlayerReceivesResource() {
        // Create player and player state
        Player player = new Player("Alice", "RED");
        PlayerState playerState = new PlayerState(player);

        // Add resources
        playerState.addResource(new ResourceCard(ResourceType.WOOD));
        playerState.addResource(new ResourceCard(ResourceType.WOOD));

        // Verify resource count
        assertEquals(2, playerState.getResourceCount(ResourceType.WOOD));
    }
}
