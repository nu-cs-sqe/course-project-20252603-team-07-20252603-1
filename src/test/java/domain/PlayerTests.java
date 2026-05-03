package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PlayerTests {
    @Test // test case 1
    public void PlaceEmptySettlement_OnNullVertex_ExpectError() {
        Player player = new Player();
        Vertex vertex = null;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            player.placeSettlement(vertex);
        });
        assertEquals("Vertex cannot be null", exception.getMessage());
    }
    @Test
    public void PlaceSettlement_OnUnoccupiedVertexNoAdjacentSettlementsZeroSettlements_ExpectLenOne() {
        final int expectedNumSettlementsAfterPlace = 1;

        // mock vertex
        Vertex vertex = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertex.isOccupied()).andReturn(false);
        EasyMock.expect(vertex.hasAdjacentSettlementViolatingDistanceRule()).andReturn(false);
        EasyMock.replay(vertex);

        // create player
        Player player = new Player();
        player.placeSettlement(vertex);

        assertEquals(
            expectedNumSettlementsAfterPlace,
            player.getSettlements().size(),
            "expected: settlement appended to player's settlements list"
        );

        EasyMock.verify(vertex);
    }
}
