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

    @Test // test case 2
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

    @Test // test case 3
    public void PlaceSettlement_OnUnoccupiedVertexNoAdjacentSettlementsFourExisting_ExpectLenFive() {
        final int expectedNumSettlementsAfterPlace = 5;

        // mock 4 setup vertices to reach state of 4 existing settlements
        Vertex setupVertex = EasyMock.createMock(Vertex.class);
        EasyMock.expect(setupVertex.isOccupied()).andReturn(false).times(4);
        EasyMock.expect(setupVertex.hasAdjacentSettlementViolatingDistanceRule()).andReturn(false).times(4);
        EasyMock.replay(setupVertex);

        // create player, append settlements to mocked vertices
        Player player = new Player();
        for (int i = 0; i < 4; i++) {
            player.placeSettlement(setupVertex);
        }
        EasyMock.verify(setupVertex);

        // mock the vertex under test
        Vertex vertex = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertex.isOccupied()).andReturn(false);
        EasyMock.expect(vertex.hasAdjacentSettlementViolatingDistanceRule()).andReturn(false);
        EasyMock.replay(vertex);

        player.placeSettlement(vertex);

        assertEquals(
            expectedNumSettlementsAfterPlace,
            player.getSettlements().size(),
            "expected: settlement appended to player's settlements list"
        );
        EasyMock.verify(vertex);
    }

    @Test // test case 4
    public void PlaceSettlement_OnUnoccupiedVertexNoAdjacentSettlementsFiveExisting_ExpectError() {
        // mock 5 setup vertices to reach state of 5 existing settlements
        Vertex setupVertex = EasyMock.createMock(Vertex.class);
        EasyMock.expect(setupVertex.isOccupied()).andReturn(false).times(5);
        EasyMock.expect(setupVertex.hasAdjacentSettlementViolatingDistanceRule()).andReturn(false).times(5);
        EasyMock.replay(setupVertex);

        // create player, append settlements to mocked vertices
        Player player = new Player();
        for (int i = 0; i < 5; i++) {
            player.placeSettlement(setupVertex);
        }
        EasyMock.verify(setupVertex);

        // mock the vertex under test
        Vertex vertex = EasyMock.createMock(Vertex.class);
        EasyMock.replay(vertex);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            player.placeSettlement(vertex);
        });
        assertEquals("No settlements remaining.", exception.getMessage());
        EasyMock.verify(vertex);
    }

    @Test // test case 5
    public void PlaceSettlement_OnOccupiedVertexZeroSettlements_ExpectError() {
        // mock vertex that is already occupied
        Vertex vertex = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertex.isOccupied()).andReturn(true);
        EasyMock.replay(vertex);

        // create new player
        Player player = new Player();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            player.placeSettlement(vertex);
        });
        assertEquals("Vertex is already occupied.", exception.getMessage());
        EasyMock.verify(vertex);
    }

    @Test // test case 6
    public void PlaceSettlement_OnVertexAdjacentToExistingAndZeroSettlements_ExpectError() {
        // mock vertex that is already occupied
        Vertex vertex = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertex.isOccupied()).andReturn(false);
        EasyMock.expect(vertex.hasAdjacentSettlementViolatingDistanceRule()).andReturn(true);
        EasyMock.replay(vertex);

        // create new player
        Player player = new Player();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            player.placeSettlement(vertex);
        });
        assertEquals("Settlement violates the distance rule.", exception.getMessage());
        EasyMock.verify(vertex);
    }
}
