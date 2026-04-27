package domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

public class GraphEdgeTests {
    @ParameterizedTest
    @EnumSource(value = PlayerColor.class, names = {"RED", "WHITE", "ORANGE", "BLUE"})
    void claimGraphEdge_NodeUnoccupied_ExpectTrue(PlayerColor color) {
        GraphEdge e1 = new GraphEdge(0, 1);
        assertTrue(e1.claimGraphEdge(color));
        assertTrue(e1.checkRoadExists());
        assertEquals(color, e1.checkOwningColor());

    }

    @Test
    void claimGraphEdge_EdgeUnoccupied_ExpectError() {
        GraphEdge e1 = new GraphEdge(52, 53);
        e1.claimGraphEdge((PlayerColor.BLUE));

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> e1.claimGraphEdge(PlayerColor.RED));

        assertEquals("Edge already claimed", exception.getMessage());
        assertTrue(e1.checkRoadExists());
        assertEquals(PlayerColor.BLUE, e1.checkOwningColor());

    }
}
