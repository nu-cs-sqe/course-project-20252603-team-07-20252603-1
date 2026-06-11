package domain.model.player;

import domain.model.player.Player;
import domain.model.player.PlayerColor;
import domain.model.resources.Resource;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PlayerTests {

    // --- placeSettlement: BVA settlement count boundary ---

    @Test // test case 1
    public void PlaceSettlement_ZeroExisting_ExpectLenOne() {
        final int expectedNumSettlements = 1;

        Player player = new Player("Dummy", PlayerColor.BLUE);
        player.placeSettlement();

        assertEquals(
            expectedNumSettlements,
            player.getSettlements().size(),
            "expected: settlement appended to player's settlements list"
        );
    }

    @Test // test case 2
    public void PlaceSettlement_FourExisting_ExpectLenFive() {
        final int expectedNumSettlements = 5;

        Player player = new Player("Dummy", PlayerColor.BLUE);
        for (int i = 0; i < 4; i++) {
            player.placeSettlement();
        }

        player.placeSettlement();

        assertEquals(
            expectedNumSettlements,
            player.getSettlements().size(),
            "expected: fifth settlement appended to player's settlements list"
        );
    }

    @Test // test case 3
    public void PlaceSettlement_FiveExisting_ExpectError() {
        Player player = new Player("Dummy", PlayerColor.BLUE);
        for (int i = 0; i < 5; i++) {
            player.placeSettlement();
        }

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            player.placeSettlement();
        });
        assertEquals("No settlements remaining.", exception.getMessage());
    }

    // --- placeRoad: BVA road count boundary ---

    @Test // test case 4
    public void PlaceRoad_ZeroExisting_ExpectLenOne() {
        final int expectedNumRoads = 1;

        Player player = new Player("Dummy", PlayerColor.BLUE);
        player.placeRoad();

        assertEquals(
            expectedNumRoads,
            player.getRoads().size(),
            "expected: road appended to player's roads list"
        );
    }

    @Test // test case 5
    public void PlaceRoad_FourteenExisting_ExpectLenFifteen() {
        final int expectedNumRoads = 15;

        Player player = new Player("Dummy", PlayerColor.BLUE);
        for (int i = 0; i < 14; i++) {
            player.placeRoad();
        }

        player.placeRoad();

        assertEquals(
            expectedNumRoads,
            player.getRoads().size(),
            "expected: fifteenth road appended to player's roads list"
        );
    }

    @Test // test case 6
    public void PlaceRoad_FifteenExisting_ExpectError() {
        Player player = new Player("Dummy", PlayerColor.BLUE);
        for (int i = 0; i < 15; i++) {
            player.placeRoad();
        }

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            player.placeRoad();
        });
        assertEquals("No roads remaining.", exception.getMessage());
    }

    // --- receiveResources ---

    @Test // test case 7
    public void ReceiveResources_NullResources_ExpectError() {
        Player player = new Player("Dummy", PlayerColor.BLUE);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                player.receiveResources(null)
        );
        assertEquals("Resources cannot be null.", exception.getMessage());
    }

    @Test // test case 8
    public void ReceiveResources_EmptyMap_ExpectResourcesUnchanged() {
        Map<Resource, Integer> emptyResources = new HashMap<>();

        Player player = new Player("Dummy", PlayerColor.BLUE);
        player.receiveResources(emptyResources);

        assertEquals(0, player.getResources().size(), "expected: player's resources map unchanged");
    }

    @Test // test case 9
    public void ReceiveResources_WoodOneAtLowerBoundary_ExpectWoodCountIncreasedByOne() {
        final int expectedWoodCount = 1;

        Map<Resource, Integer> resources = new HashMap<>();
        resources.put(Resource.LUMBER, 1);

        Player player = new Player("Dummy", PlayerColor.BLUE);
        player.receiveResources(resources);

        assertEquals(expectedWoodCount, player.getResources().get(Resource.LUMBER),
                "expected: player's LUMBER count increases by 1");
    }

    @Test // test case 10
    public void ReceiveResources_BrickNineteenAtUpperBoundary_ExpectBrickCountIncreasedByNineteen() {
        final int expectedBrickCount = 19;

        Map<Resource, Integer> resources = new HashMap<>();
        resources.put(Resource.BRICK, 19);

        Player player = new Player("Dummy", PlayerColor.BLUE);
        player.receiveResources(resources);

        assertEquals(expectedBrickCount, player.getResources().get(Resource.BRICK),
                "expected: player's BRICK count increases by 19");
    }

    @Test // test case 11
    public void ReceiveResources_SheepZeroBelowLowerBoundary_ExpectError() {
        Map<Resource, Integer> resources = new HashMap<>();
        resources.put(Resource.WOOL, 0);

        Player player = new Player("Dummy", PlayerColor.BLUE);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                player.receiveResources(resources)
        );
        assertEquals("Resource quantity must be at least 1.", exception.getMessage());
    }

    @Test // test case 12
    public void ReceiveResources_WoodFiveAndBrickThreeMoreThanOneEntry_ExpectBothCountsIncreased() {
        final int expectedWoodCount = 5;
        final int expectedBrickCount = 3;

        Map<Resource, Integer> resources = new HashMap<>();
        resources.put(Resource.LUMBER, expectedWoodCount);
        resources.put(Resource.BRICK, expectedBrickCount);

        Player player = new Player("Dummy", PlayerColor.BLUE);
        player.receiveResources(resources);

        assertEquals(expectedWoodCount, player.getResources().get(Resource.LUMBER),
                "expected: player's LUMBER count increases by 5");
        assertEquals(expectedBrickCount, player.getResources().get(Resource.BRICK),
                "expected: player's BRICK count increases by 3");
    }

    @Test // test case 13
    public void ReceiveResources_DesertOneInvalidResourceType_ExpectError() {
        Map<Resource, Integer> resources = new HashMap<>();
        resources.put(Resource.DESERT, 1);

        Player player = new Player("Dummy", PlayerColor.BLUE);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            player.receiveResources(resources)
        );
        assertEquals("Cannot receive DESERT as a resource.", exception.getMessage());
    }

    // updateVictoryPoints() Tests
    @Test
    void updateVictoryPoints_PlayerHas0_Receives1_ExpectPlayerHas1() {
        Player player = new Player("Spencer", PlayerColor.RED);
        assertEquals(0, player.getVictoryPoints());
        player.updateVictoryPoints(1);
        assertEquals(1, player.getVictoryPoints());
    }

    @Test
    void updateVictoryPoints_PlayerHas0_Receives2_ExpectPlayerHas2() {
        Player player = new Player("Spencer", PlayerColor.RED);
        assertEquals(0, player.getVictoryPoints());
        player.updateVictoryPoints(2);
        assertEquals(2, player.getVictoryPoints());
    }

    @Test
    void updateVictoryPoints_PlayerHas8_Receives2_ExpectPlayerHas10() {
        Player player = new Player("Spencer", PlayerColor.RED);
        player.updateVictoryPoints(8);
        assertEquals(8, player.getVictoryPoints());
        player.updateVictoryPoints(2);
        assertEquals(10, player.getVictoryPoints());
    }

    @Test
    void updateVictoryPoints_PlayerHas10_Loses2_ExpectPlayerHas8() {
        Player player = new Player("Spencer", PlayerColor.RED);
        player.updateVictoryPoints(10);
        assertEquals(10, player.getVictoryPoints());
        player.updateVictoryPoints(-2);
        assertEquals(8, player.getVictoryPoints());
    }

    @Test
    void updateVictoryPoints_PlayerHas11_Loses2_ExpectPlayerHas9() {
        Player player = new Player("Spencer", PlayerColor.RED);
        player.updateVictoryPoints(11);
        assertEquals(11, player.getVictoryPoints());
        player.updateVictoryPoints(-2);
        assertEquals(9, player.getVictoryPoints());
    }

    @Test
    void updateVictoryPoints_PlayerHas9_Receives2_ExpectPlayerHas11() {
        Player player = new Player("Spencer", PlayerColor.RED);
        player.updateVictoryPoints(9);
        assertEquals(9, player.getVictoryPoints());
        player.updateVictoryPoints(2);
        assertEquals(11, player.getVictoryPoints());
    }

    @Test
    void updateVictoryPoints_PlayerHas2_Loses2_ExpectPlayerHas0() {
        Player player = new Player("Spencer", PlayerColor.RED);
        player.updateVictoryPoints(2);
        assertEquals(2, player.getVictoryPoints());
        player.updateVictoryPoints(-2);
        assertEquals(0, player.getVictoryPoints());
    }


}
