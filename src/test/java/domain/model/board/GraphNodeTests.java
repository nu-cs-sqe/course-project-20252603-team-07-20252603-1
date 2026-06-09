package domain.model.board;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import domain.model.exceptions.IllegalNodeIDException;
import domain.model.player.PlayerColor;

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

    @Test
    void assertValidNodeID_test01_ID0_ExpectTrue() {
        GraphNode g1 = new GraphNode(0);
        assertNotNull(g1);
    }

    @Test
    void assertValidNodeID_test02_ID53_ExpectTrue() {
        GraphNode g1 = new GraphNode(53);
        assertNotNull(g1);
    }

    @Test
    void assertValidNodeID_test03_IDNegative1_ExpectError() {
        Exception exception = assertThrows(IllegalNodeIDException.class,
                () -> new GraphNode(-1));
        assertEquals("Requested nodeID number illegal", exception.getMessage());
    }

    @Test
    void assertValidNodeID_test04_ID54_ExpectError() {
        Exception exception = assertThrows(IllegalNodeIDException.class,
                () -> new GraphNode(54));
        assertEquals("Requested nodeID number illegal", exception.getMessage());
    }
}
