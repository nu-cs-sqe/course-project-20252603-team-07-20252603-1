package domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class GraphNodeTests {

    @ParameterizedTest
    @EnumSource(value = PlayerColor.class, names = {"RED", "WHITE", "ORANGE", "BLUE"})
    void claimGraphNode_NodeUnoccupied_ExpectTrue(PlayerColor color) {
        GraphNode g1 = new GraphNode(0);
        assertTrue(g1.playerClaimNode(color));
        assertTrue(g1.checkOccupied());
        assertEquals(color, g1.checkColor());
    }

    @Test
    void claimGraphNodeOccupied_ExpectError() {
        GraphNode g1 = new GraphNode(0);
        g1.playerClaimNode(PlayerColor.BLUE);
        assertTrue(g1.checkOccupied());
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> g1.playerClaimNode(PlayerColor.ORANGE));
        assertEquals("Node Already Claimed", exception.getMessage());
        assertTrue(g1.checkOccupied());
        assertEquals(PlayerColor.BLUE, g1.checkColor());
    }
}
