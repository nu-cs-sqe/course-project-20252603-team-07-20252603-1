package domain;

import org.junit.jupiter.api.Test;

import org.easymock.EasyMock;
import org.opentest4j.AssertionFailedError;

import static org.junit.jupiter.api.Assertions.*;

public class HexTests {
    @Test // Test Case 1
    public void AddEmptyString_OnEmptyList_ExpectLenOne() {
        Hex h = new Hex(1, Resource.LUMBER, 9);
        h.addPlayerSettlementToHex(PlayerColor.RED);

        int expected = 1;
        int actual = h.getSettlementCount();
        assertEquals(expected, actual);
    }

    @Test // Test Case 2
    public void AddTwoStrings_OneEmpty_ExpectLenTwo() {
        Hex h = new Hex(1, Resource.LUMBER, 9);
        h.addPlayerSettlementToHex(PlayerColor.BLUE);
        h.addPlayerSettlementToHex(PlayerColor.ORANGE);

        int expected = 2;
        int actual = h.getSettlementCount();
        assertEquals(expected, actual);
    }

    @Test // Test Case 3
    public void AddString_ToListWithDuplicates_ExpectLenThree() {
        Hex h = new Hex(1, Resource.LUMBER, 9);
        h.addPlayerSettlementToHex(PlayerColor.ORANGE);
        h.addPlayerSettlementToHex(PlayerColor.ORANGE);
        h.addPlayerSettlementToHex(PlayerColor.WHITE);

        int expected = 3;
        int actual = h.getSettlementCount();
        assertEquals(expected, actual);
    }

    @Test // Test Case 4
    public void AddString_ToListWithThreeElements_ExpectError() {
        Hex h = new Hex(1, Resource.LUMBER, 9);
        h.addPlayerSettlementToHex(PlayerColor.BLUE);
        h.addPlayerSettlementToHex(PlayerColor.BLUE);
        h.addPlayerSettlementToHex(PlayerColor.BLUE);

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            h.addPlayerSettlementToHex(PlayerColor.WHITE);
        });

        String expectedMessage = "Already three settlements on hex.";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test // Test Case 5
    public void AddNullToList_ExpectError() {
        Hex h = new Hex(1, Resource.LUMBER, 9);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            h.addPlayerSettlementToHex(null);
        });

        String expectedMessage = "Adding invalid player name to Hex.";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test // Test Case 6
    public void RemoveFromEmptyList_ExpectError() {
        Hex h = new Hex(1, Resource.LUMBER, 9);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            h.removePlayerSettlementFromHex(PlayerColor.WHITE);
        });

        String expectedMessage = "Player does not have a building on hex.";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test // Test Case 7
    public void RemoveFromList_WithOneElement_ExpectLenZero() {
        Hex h = new Hex(1, Resource.LUMBER, 9);

        h.addPlayerSettlementToHex(PlayerColor.BLUE);

        h.removePlayerSettlementFromHex(PlayerColor.BLUE);

        int expected = 0;
        int actual = h.getSettlementCount();
        assertEquals(expected, actual);
    }

    @Test // Test Case 8
    public void RemoveFromList_WithTwoDuplicates_ExpectLenOne() {
        Hex h = new Hex(1, Resource.LUMBER, 9);

        h.addPlayerSettlementToHex(PlayerColor.RED);
        h.addPlayerSettlementToHex(PlayerColor.RED);

        h.removePlayerSettlementFromHex(PlayerColor.RED);

        int expected = 1;
        int actual = h.getSettlementCount();
        assertEquals(expected, actual);

    }

    @Test // Test Case 9
    public void RemoveFromList_WithThreeElements_ExpectLenTwo() {
        Hex h = new Hex(1, Resource.LUMBER, 9);

        h.addPlayerSettlementToHex(PlayerColor.ORANGE);
        h.addPlayerSettlementToHex(PlayerColor.WHITE);
        h.addPlayerSettlementToHex(PlayerColor.RED);

        h.removePlayerSettlementFromHex(PlayerColor.ORANGE);

        int expected = 2;
        int actual = h.getSettlementCount();
        assertEquals(expected, actual);
    }


    @Test // Test Case 10
    public void RemoveFromList_WithThreeDuplicates_ExpectLenTwo() {
        Hex h = new Hex(1, Resource.LUMBER, 9);

        h.addPlayerSettlementToHex(PlayerColor.WHITE);
        h.addPlayerSettlementToHex(PlayerColor.WHITE);
        h.addPlayerSettlementToHex(PlayerColor.WHITE);

        h.removePlayerSettlementFromHex(PlayerColor.WHITE);

        int expected = 2;
        int actual = h.getSettlementCount();
        assertEquals(expected, actual);
    }
    @Test // Test Case 11
    public void RemoveNull_FromList_ExpectError() {
        Hex h = new Hex(1, Resource.LUMBER, 9);
        h.addPlayerSettlementToHex(PlayerColor.WHITE);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            h.removePlayerSettlementFromHex(null);
        });

        String expectedMessage = "Player does not have a building on hex.";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test // Test Case 12
    public void AddOneCity_ExpectLenOne() {
        Hex h = new Hex(1, Resource.LUMBER, 9);
        h.addPlayerCityToHex(PlayerColor.RED);

        int expected = 1;
        int actual = h.getCityCount();
        assertEquals(expected, actual);
    }

    @Test // Test Case 13
    public void AddTwoCities_ExpectLenTwo() {
        Hex h = new Hex(1, Resource.LUMBER, 9);
        h.addPlayerCityToHex(PlayerColor.BLUE);
        h.addPlayerCityToHex(PlayerColor.ORANGE);

        int expected = 2;
        int actual = h.getCityCount();
        assertEquals(expected, actual);
    }

    @Test // Test Case 14
    public void AddToTwoDuplicateCities_ExpectLenThree() {
        Hex h = new Hex(1, Resource.LUMBER, 9);
        h.addPlayerCityToHex(PlayerColor.ORANGE);
        h.addPlayerCityToHex(PlayerColor.ORANGE);

        h.addPlayerCityToHex(PlayerColor.BLUE);

        int expected = 3;
        int actual = h.getCityCount();
        assertEquals(expected, actual);
    }

    @Test // Test Case 15
    public void AddFourCities_ExpectError() {
        Hex h = new Hex(1, Resource.LUMBER, 9);
        h.addPlayerCityToHex(PlayerColor.BLUE);
        h.addPlayerCityToHex(PlayerColor.BLUE);
        h.addPlayerCityToHex(PlayerColor.BLUE);

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            h.addPlayerCityToHex(PlayerColor.WHITE);
        });

        String expectedMessage = "Already three buildings on hex.";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test // Test Case 16
    public void AddNullCity_ExpectError() {
        Hex h = new Hex(1, Resource.LUMBER, 9);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            h.addPlayerCityToHex(null);
        });

        String expectedMessage = "Adding invalid player name to Hex.";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test // Test Case 17
    public void AwardResourcesToNoSettlements_ExpectNoUpdate() {
        Hex h = new Hex(1, Resource.LUMBER, 9);

        Player mockPlayer = EasyMock.createMock(Player.class);
        EasyMock.replay(mockPlayer);

        // If not expecting to call anything, will throw error
        // We want awardSettlementResources to not have any calls to update
        h.awardSettlementResources();

        EasyMock.verify(mockPlayer);
    }

}

