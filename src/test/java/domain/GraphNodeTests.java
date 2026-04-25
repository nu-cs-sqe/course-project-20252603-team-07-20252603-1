package domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GraphNodeTests {
    @ParameterizedTest
    @EnumSource(value = PlayerColor.class, names = {"RED", "WHITE", "ORANGE", "BLUE"})
    void claimGraphNode_NodeUnoccupied_ExpectTrue(PlayerColor color) {
        GraphNode g1 = new GraphNode(0);
        assertTrue(g1.playerClaimNode(color));
        assertTrue(g1.checkOccupied());
        assertEquals(color, g1.checkColor());
    }
}
