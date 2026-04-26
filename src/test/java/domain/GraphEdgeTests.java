package domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GraphEdgeTests {
    @ParameterizedTest
    @EnumSource(value = PlayerColor.class, names = {"RED", "WHITE", "ORANGE", "BLUE"})
    void claimGraphEdge_NodeUnoccupied_ExpectTrue(PlayerColor color) {
        GraphEdge e1 = new GraphEdge(0);
        assertTrue(e1.claimGraphEdge(color));
        assertTrue(e1.checkRoadExists());
        assertEquals(color, e1.checkOwningColor());

    }
}
