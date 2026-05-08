package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

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

    @Test // test case 7
    public void PlaceRoad_OnNullEdge_ExpectError() {
        Player player = new Player();
        Edge edge = null;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            player.placeRoad(edge);
        });
        assertEquals("Edge cannot be null.", exception.getMessage());
    }

    @Test // test case 8
    public void PlaceRoad_OnUnoccupiedEdgeZeroRoads_ExpectLenOne() {
        final int expectedNumRoadsAfterPlace = 1;

        // mock edge
        Edge edge = EasyMock.createMock(Edge.class);
        EasyMock.expect(edge.isOccupied()).andReturn(false);
        EasyMock.expect(edge.isConnectedToPlayerNetwork()).andReturn(true);
        EasyMock.replay(edge);

        // create player
        Player player = new Player();
        player.placeRoad(edge);

        assertEquals(expectedNumRoadsAfterPlace, player.getRoads().size());
        EasyMock.verify(edge);
    }

    @Test // test case 9
    public void PlaceRoad_OnUnoccupiedEdgeFourteenExistingRoads_ExpectLenFifteen() {
        final int expectedNumRoadsAfterPlace = 15;

        // mock 14 setup edges to reach state of 14 existing roads
        Edge setupEdge = EasyMock.createMock(Edge.class);
        EasyMock.expect(setupEdge.isOccupied()).andReturn(false).times(14);
        EasyMock.expect(setupEdge.isConnectedToPlayerNetwork()).andReturn(true).times(14);
        EasyMock.replay(setupEdge);

        // mock the edge under test
        Edge edge = EasyMock.createMock(Edge.class);
        EasyMock.expect(edge.isOccupied()).andReturn(false);
        EasyMock.expect(edge.isConnectedToPlayerNetwork()).andReturn(true);
        EasyMock.replay(edge);

        // initialize player and add edges
        Player player = new Player();
        for (int i = 0; i < 14; i++) {
            player.placeRoad(setupEdge);
        }
        EasyMock.verify(setupEdge);

        // place road
        player.placeRoad(edge);

        assertEquals(
            expectedNumRoadsAfterPlace,
            player.getRoads().size(),
            "expected: road appended to player's roads list"
        );
        EasyMock.verify(edge);
    }

    @Test // test case 10
    public void PlaceRoad_OnUnoccupiedEdgeFifteenExistingRoads_ExpectError() {
        // mock 15 setup edges to reach state of 15 existing roads
        Edge setupEdge = EasyMock.createMock(Edge.class);
        EasyMock.expect(setupEdge.isOccupied()).andReturn(false).times(15);
        EasyMock.expect(setupEdge.isConnectedToPlayerNetwork()).andReturn(true).times(15);
        EasyMock.replay(setupEdge);

        // mock the edge under test
        Edge edge = EasyMock.createMock(Edge.class);
        EasyMock.replay(edge);

        // create player, append first 15 roads to mocked edges
        Player player = new Player();
        for (int i = 0; i < 15; i++) {
            player.placeRoad(setupEdge);
        }
        EasyMock.verify(setupEdge);

        // expect error for the 16th road
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            player.placeRoad(edge);
        });
        assertEquals("No roads remaining.", exception.getMessage());
        EasyMock.verify(edge);
    }

    @Test // test case 11
    public void PlaceRoad_OnOccupiedEdgeZeroRoads_ExpectError() {
        // mock edge that is already occupied
        Edge edge = EasyMock.createMock(Edge.class);
        EasyMock.expect(edge.isOccupied()).andReturn(true);
        EasyMock.replay(edge);

        // create player
        Player player = new Player();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            player.placeRoad(edge)
        );
        assertEquals("Edge is already occupied.", exception.getMessage());
        EasyMock.verify(edge);
    }

    @Test // test case 12
    public void PlaceRoad_OnUnoccupiedEdgeNotConnectedToNetworkZeroRoads_ExpectError() {
        // mock edge that is unoccupied but not connected to the player's network
        Edge edge = EasyMock.createMock(Edge.class);
        EasyMock.expect(edge.isOccupied()).andReturn(false);
        EasyMock.expect(edge.isConnectedToPlayerNetwork()).andReturn(false);
        EasyMock.replay(edge);

        // create player
        Player player = new Player();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                player.placeRoad(edge)
        );
        assertEquals("Road must connect to player's existing network.", exception.getMessage());
        EasyMock.verify(edge);
    }

    @Test // test case 13
    public void ReceiveResources_NullResources_ExpectError() {
        Player player = new Player();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                player.receiveResources(null)
        );
        assertEquals("Resources cannot be null.", exception.getMessage());
    }

    @Test // test case 14
    public void ReceiveResources_EmptyMap_ExpectResourcesUnchanged() {
        // create data
        Map<ResourceType, Integer> emptyResources = new HashMap<>();

        // create player and receive resources
        Player player = new Player();
        player.receiveResources(emptyResources);

        // assert data
        assertEquals(0, player.getResources().size(), "expected: player's resources map unchanged");
    }

    @Test // test case 15
    public void ReceiveResources_WoodOneAtLowerBoundary_ExpectWoodCountIncreasedByOne() {
        final int expectedWoodCount = 1;

        // create test resource
        Map<ResourceType, Integer> resources = new HashMap<>();
        resources.put(ResourceType.WOOD, 1);

        // instantiate player to test receiving resource
        Player player = new Player();
        player.receiveResources(resources);

        // assert correct value received
        assertEquals(expectedWoodCount, player.getResources().get(ResourceType.WOOD),
                "expected: player's WOOD count increases by 1");
    }

}
