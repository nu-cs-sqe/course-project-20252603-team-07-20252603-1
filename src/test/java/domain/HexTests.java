package domain;

import org.junit.jupiter.api.Test;

import org.easymock.EasyMock;

import static org.junit.jupiter.api.Assertions.*;

public class HexTests {
    @Test // Test Case 1
    public void AddEmptyString_OnEmptyList_ExpectLenOne() {
        Hex h = new Hex(1, Resource.LUMBER, 9);

        Player mockRedPlayer = EasyMock.createMock(Player.class);

        h.addPlayerSettlementToHex(mockRedPlayer);

        int expected = 1;
        int actual = h.getSettlementCount();
        assertEquals(expected, actual);
    }

    @Test // Test Case 2
    public void AddTwoStrings_OneEmpty_ExpectLenTwo() {
        Hex h = new Hex(1, Resource.LUMBER, 9);

        Player mockBluePlayer = EasyMock.createMock(Player.class);
        Player mockOrangePlayer = EasyMock.createMock(Player.class);
        
        h.addPlayerSettlementToHex(mockBluePlayer);
        h.addPlayerSettlementToHex(mockOrangePlayer);

        int expected = 2;
        int actual = h.getSettlementCount();
        assertEquals(expected, actual);
    }

    @Test // Test Case 3
    public void AddString_ToListWithDuplicates_ExpectLenThree() {
        Hex h = new Hex(1, Resource.LUMBER, 9);
        
        Player mockOrangePlayer = EasyMock.createMock(Player.class);
        Player mockWhitePlayer = EasyMock.createMock(Player.class);

        h.addPlayerSettlementToHex(mockOrangePlayer);
        h.addPlayerSettlementToHex(mockOrangePlayer);
        h.addPlayerSettlementToHex(mockWhitePlayer);

        int expected = 3;
        int actual = h.getSettlementCount();
        assertEquals(expected, actual);
    }

    @Test // Test Case 4
    public void AddString_ToListWithThreeElements_ExpectError() {
        Hex h = new Hex(1, Resource.LUMBER, 9);

        Player mockBluePlayer = EasyMock.createMock(Player.class);
        Player mockWhitePlayer = EasyMock.createMock(Player.class);

        h.addPlayerSettlementToHex(mockBluePlayer);
        h.addPlayerSettlementToHex(mockBluePlayer);
        h.addPlayerSettlementToHex(mockBluePlayer);

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            h.addPlayerSettlementToHex(mockWhitePlayer);
        });

        String expectedMessage = "Already three buildings on hex.";
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
    public void AddThreeCities_AddSettlement_ExpectError() {
        Hex h = new Hex(1, Resource.LUMBER, 0);

        Player mockWhitePlayer = EasyMock.createMock(Player.class);

        h.addPlayerSettlementToHex(mockWhitePlayer);
        h.addPlayerSettlementToHex(mockWhitePlayer);
        h.addPlayerSettlementToHex(mockWhitePlayer);

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            h.addPlayerSettlementToHex(mockWhitePlayer);
        });

        String expectedMessage = "Already three buildings on hex.";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test // Test Case 6
    public void RemoveFromEmptyList_ExpectError() {
        Hex h = new Hex(1, Resource.LUMBER, 9);

        Player mockWhitePlayer = EasyMock.createMock(Player.class);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            h.removePlayerSettlementFromHex(mockWhitePlayer);
        });

        String expectedMessage = "Player does not have a settlement on hex.";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test // Test Case 7
    public void RemoveFromList_WithOneElement_ExpectLenZero() {
        Hex h = new Hex(1, Resource.LUMBER, 9);

        Player mockBluePlayer = EasyMock.createMock(Player.class);

        h.addPlayerSettlementToHex(mockBluePlayer);

        h.removePlayerSettlementFromHex(mockBluePlayer);

        int expected = 0;
        int actual = h.getSettlementCount();
        assertEquals(expected, actual);
    }

    @Test // Test Case 8
    public void RemoveFromList_WithTwoDuplicates_ExpectLenOne() {
        Hex h = new Hex(1, Resource.LUMBER, 9);

        Player mockRedPlayer = EasyMock.createMock(Player.class);

        h.addPlayerSettlementToHex(mockRedPlayer);
        h.addPlayerSettlementToHex(mockRedPlayer);

        h.removePlayerSettlementFromHex(mockRedPlayer);

        int expected = 1;
        int actual = h.getSettlementCount();
        assertEquals(expected, actual);

    }

    @Test // Test Case 9
    public void RemoveFromList_WithThreeElements_ExpectLenTwo() {
        Hex h = new Hex(1, Resource.LUMBER, 9);

        Player mockOrangePlayer = EasyMock.createMock(Player.class);
        Player mockWhitePlayer = EasyMock.createMock(Player.class);
        Player mockRedPlayer = EasyMock.createMock(Player.class);

        h.addPlayerSettlementToHex(mockOrangePlayer);
        h.addPlayerSettlementToHex(mockWhitePlayer);
        h.addPlayerSettlementToHex(mockRedPlayer);

        h.removePlayerSettlementFromHex(mockOrangePlayer);

        int expected = 2;
        int actual = h.getSettlementCount();
        assertEquals(expected, actual);
    }


    @Test // Test Case 10
    public void RemoveFromList_WithThreeDuplicates_ExpectLenTwo() {
        Hex h = new Hex(1, Resource.LUMBER, 9);

        Player mockWhitePlayer = EasyMock.createMock(Player.class);

        h.addPlayerSettlementToHex(mockWhitePlayer);
        h.addPlayerSettlementToHex(mockWhitePlayer);
        h.addPlayerSettlementToHex(mockWhitePlayer);

        h.removePlayerSettlementFromHex(mockWhitePlayer);

        int expected = 2;
        int actual = h.getSettlementCount();
        assertEquals(expected, actual);
    }
    @Test // Test Case 11
    public void RemoveNull_FromList_ExpectError() {
        Hex h = new Hex(1, Resource.LUMBER, 9);

        Player mockWhitePlayer = EasyMock.createMock(Player.class);

        h.addPlayerSettlementToHex(mockWhitePlayer);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            h.removePlayerSettlementFromHex(null);
        });

        String expectedMessage = "Player does not have a settlement on hex.";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test // Test Case 13
    public void RemoveSettlement_FromHexWithThreeCities_ExpectError() {
        Hex h = new Hex(1, Resource.LUMBER, 12);

        Player mockWhitePlayer = EasyMock.createMock(Player.class);
        Player mockBluePlayer = EasyMock.createMock(Player.class);

        h.addPlayerCityToHex(mockWhitePlayer);
        h.addPlayerCityToHex(mockBluePlayer);
        h.addPlayerCityToHex(mockWhitePlayer);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            h.removePlayerSettlementFromHex(mockWhitePlayer);
        });

        String expectedMessage = "Player does not have a settlement on hex.";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test // Test Case 12
    public void AddOneCity_ExpectLenOne() {
        Hex h = new Hex(1, Resource.LUMBER, 9);

        Player mockRedPlayer = EasyMock.createMock(Player.class);

        h.addPlayerCityToHex(mockRedPlayer);

        int expected = 1;
        int actual = h.getCityCount();
        assertEquals(expected, actual);
    }

    @Test // Test Case 13
    public void AddTwoCities_ExpectLenTwo() {
        Hex h = new Hex(1, Resource.LUMBER, 9);

        Player mockBluePlayer = EasyMock.createMock(Player.class);
        Player mockOrangePlayer = EasyMock.createMock(Player.class);

        h.addPlayerCityToHex(mockBluePlayer);
        h.addPlayerCityToHex(mockOrangePlayer);

        int expected = 2;
        int actual = h.getCityCount();
        assertEquals(expected, actual);
    }

    @Test // Test Case 14
    public void AddToTwoDuplicateCities_ExpectLenThree() {
        Hex h = new Hex(1, Resource.LUMBER, 9);

        Player mockOrangePlayer = EasyMock.createMock(Player.class);
        Player mockBluePlayer = EasyMock.createMock(Player.class);

        h.addPlayerCityToHex(mockOrangePlayer);
        h.addPlayerCityToHex(mockOrangePlayer);

        h.addPlayerCityToHex(mockBluePlayer);

        int expected = 3;
        int actual = h.getCityCount();
        assertEquals(expected, actual);
    }

    @Test // Test Case 15
    public void AddFourCities_ExpectError() {
        Hex h = new Hex(1, Resource.LUMBER, 9);

        Player mockBluePlayer = EasyMock.createMock(Player.class);
        Player mockWhitePlayer = EasyMock.createMock(Player.class);

        h.addPlayerCityToHex(mockBluePlayer);
        h.addPlayerCityToHex(mockBluePlayer);
        h.addPlayerCityToHex(mockBluePlayer);

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            h.addPlayerCityToHex(mockWhitePlayer);
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

    @Test // Test Case 18
    public void AwardResourcesToRedSettlement_ExpectOneUpdateCall() {
        Hex h = new Hex(1, Resource.BRICK, 9);

        Player mockRedPlayer = EasyMock.createMock(Player.class);

        h.addPlayerSettlementToHex(mockRedPlayer);

        mockRedPlayer.updateResources(h.resource, 1);
        EasyMock.expectLastCall();

        EasyMock.replay(mockRedPlayer);

        h.awardSettlementResources();

        EasyMock.verify(mockRedPlayer);
    }

    @Test // Test Case 19
    public void AwardResourcesToTwoDifferentSettlements_ExpectTwoCalls() {
        Hex h = new Hex(1, Resource.GRAIN, 1);

        Player mockOrangePlayer = EasyMock.createMock(Player.class);
        Player mockWhitePlayer = EasyMock.createMock(Player.class);

        h.addPlayerSettlementToHex(mockOrangePlayer);
        h.addPlayerSettlementToHex(mockWhitePlayer);

        mockOrangePlayer.updateResources(h.resource, 1);
        EasyMock.expectLastCall();

        mockWhitePlayer.updateResources(h.resource, 1);
        EasyMock.expectLastCall();

        EasyMock.replay(mockOrangePlayer, mockWhitePlayer);

        h.awardSettlementResources();

        EasyMock.verify(mockOrangePlayer, mockWhitePlayer);
    }

    @Test // Test Case 20
    public void AwardResourcesToTwoSameSettlements_OneDifferent_ExpectThreeCalls() {
        Hex h = new Hex(1, Resource.LUMBER, 1);

        Player mockBluePlayer = EasyMock.createMock(Player.class);
        Player mockWhitePlayer = EasyMock.createMock(Player.class);

        h.addPlayerSettlementToHex(mockBluePlayer);
        h.addPlayerSettlementToHex(mockBluePlayer);
        h.addPlayerSettlementToHex(mockWhitePlayer);

        mockBluePlayer.updateResources(h.resource, 1);
        EasyMock.expectLastCall();

        mockBluePlayer.updateResources(h.resource, 1);
        EasyMock.expectLastCall();

        mockWhitePlayer.updateResources(h.resource, 1);
        EasyMock.expectLastCall();

        EasyMock.replay(mockBluePlayer, mockWhitePlayer);

        h.awardSettlementResources();

        EasyMock.verify(mockBluePlayer, mockWhitePlayer);
    }

    @Test // Test Case 21
    public void AwardResourcesToThreeRedSettlements_ExpectThreeCalls() {
        Hex h = new Hex(1, Resource.ORE, 1);

        Player mockRedPlayer = EasyMock.createMock(Player.class);

        h.addPlayerSettlementToHex(mockRedPlayer);
        h.addPlayerSettlementToHex(mockRedPlayer);
        h.addPlayerSettlementToHex(mockRedPlayer);

        mockRedPlayer.updateResources(h.resource, 1);
        EasyMock.expectLastCall();

        mockRedPlayer.updateResources(h.resource, 1);
        EasyMock.expectLastCall();

        mockRedPlayer.updateResources(h.resource, 1);
        EasyMock.expectLastCall();

        EasyMock.replay(mockRedPlayer);

        h.awardSettlementResources();

        EasyMock.verify(mockRedPlayer);
    }

    @Test // Test Case 22
    public void AwardWool_ThreeDifferentSettlements_ExpectThreeCalls() {
        Hex h = new Hex(1, Resource.WOOL, 1);

        Player mockRedPlayer = EasyMock.createMock(Player.class);
        Player mockWhitePlayer = EasyMock.createMock(Player.class);
        Player mockBluePlayer = EasyMock.createMock(Player.class);

        h.addPlayerSettlementToHex(mockRedPlayer);
        h.addPlayerSettlementToHex(mockWhitePlayer);
        h.addPlayerSettlementToHex(mockBluePlayer);

        mockRedPlayer.updateResources(h.resource, 1);
        EasyMock.expectLastCall();

        mockWhitePlayer.updateResources(h.resource, 1);
        EasyMock.expectLastCall();

        mockBluePlayer.updateResources(h.resource, 1);
        EasyMock.expectLastCall();

        EasyMock.replay(mockRedPlayer, mockWhitePlayer, mockBluePlayer);

        h.awardSettlementResources();

        EasyMock.verify(mockRedPlayer, mockWhitePlayer, mockBluePlayer);
    }

    @Test // Test Case 23
    public void AwardSettlementResources_WithOnePlayer_OnDesert_ExpectNoUpdate() {
        Hex h = new Hex(1, Resource.DESERT, 9);

        Player mockOrangePlayer = EasyMock.createMock(Player.class);

        h.addPlayerSettlementToHex(mockOrangePlayer);

        EasyMock.replay(mockOrangePlayer);

        // Want no update to happen, as we are on a desert
        h.awardSettlementResources();

        EasyMock.verify(mockOrangePlayer);
    }

    @Test // Test Case 24
    public void AwardResourcesToNoCities_ExpectNoUpdate() {
        Hex h = new Hex(1, Resource.LUMBER, 9);

        Player mockPlayer = EasyMock.createMock(Player.class);
        EasyMock.replay(mockPlayer);

        // If not expecting to call anything, will throw error if something is called
        // We want awardCityResources to not have any calls to update
        h.awardCityResources();

        EasyMock.verify(mockPlayer);
    }

    @Test // Test Case 25
    public void AwardResourcesToRedCity_ExpectOneUpdateCall() {
        Hex h = new Hex(29, Resource.BRICK, 3);

        Player mockRedPlayer = EasyMock.createMock(Player.class);

        h.addPlayerCityToHex(mockRedPlayer);

        mockRedPlayer.updateResources(h.resource, 2);
        EasyMock.expectLastCall();

        EasyMock.replay(mockRedPlayer);

        h.awardCityResources();

        EasyMock.verify(mockRedPlayer);
    }

    @Test // Test Case 26
    public void AwardResourcesToTwoDifferentCities_ExpectTwoCalls() {
        Hex h = new Hex(1, Resource.GRAIN, 1);

        Player mockOrangePlayer = EasyMock.createMock(Player.class);
        Player mockWhitePlayer = EasyMock.createMock(Player.class);

        h.addPlayerCityToHex(mockOrangePlayer);
        h.addPlayerCityToHex(mockWhitePlayer);

        mockOrangePlayer.updateResources(h.resource, 2);
        EasyMock.expectLastCall();

        mockWhitePlayer.updateResources(h.resource, 2);
        EasyMock.expectLastCall();

        EasyMock.replay(mockOrangePlayer, mockWhitePlayer);

        h.awardCityResources();

        EasyMock.verify(mockOrangePlayer, mockWhitePlayer);
    }

    @Test // Test Case 27
    public void AwardResourcesToTwoSameCities_OneDifferent_ExpectThreeCalls() {
        Hex h = new Hex(1, Resource.LUMBER, 1);

        Player mockBluePlayer = EasyMock.createMock(Player.class);
        Player mockWhitePlayer = EasyMock.createMock(Player.class);

        h.addPlayerCityToHex(mockBluePlayer);
        h.addPlayerCityToHex(mockBluePlayer);
        h.addPlayerCityToHex(mockWhitePlayer);

        mockBluePlayer.updateResources(h.resource, 2);
        EasyMock.expectLastCall();

        mockBluePlayer.updateResources(h.resource, 2);
        EasyMock.expectLastCall();

        mockWhitePlayer.updateResources(h.resource, 2);
        EasyMock.expectLastCall();

        EasyMock.replay(mockBluePlayer, mockWhitePlayer);

        h.awardCityResources();

        EasyMock.verify(mockBluePlayer, mockWhitePlayer);
    }

    @Test // Test Case 28
    public void AwardResourcesToThreeRedCities_ExpectThreeCalls() {
        Hex h = new Hex(1, Resource.ORE, 1);

        Player mockRedPlayer = EasyMock.createMock(Player.class);

        h.addPlayerCityToHex(mockRedPlayer);
        h.addPlayerCityToHex(mockRedPlayer);
        h.addPlayerCityToHex(mockRedPlayer);

        mockRedPlayer.updateResources(h.resource, 2);
        EasyMock.expectLastCall();

        mockRedPlayer.updateResources(h.resource, 2);
        EasyMock.expectLastCall();

        mockRedPlayer.updateResources(h.resource, 2);
        EasyMock.expectLastCall();

        EasyMock.replay(mockRedPlayer);

        h.awardCityResources();

        EasyMock.verify(mockRedPlayer);
    }

    @Test // Test Case 29
    public void AwardWool_ThreeDifferentCities_ExpectThreeCalls() {
        Hex h = new Hex(1, Resource.WOOL, 1);

        Player mockRedPlayer = EasyMock.createMock(Player.class);
        Player mockWhitePlayer = EasyMock.createMock(Player.class);
        Player mockBluePlayer = EasyMock.createMock(Player.class);

        h.addPlayerCityToHex(mockRedPlayer);
        h.addPlayerCityToHex(mockWhitePlayer);
        h.addPlayerCityToHex(mockBluePlayer);

        mockRedPlayer.updateResources(h.resource, 2);
        EasyMock.expectLastCall();

        mockWhitePlayer.updateResources(h.resource, 2);
        EasyMock.expectLastCall();

        mockBluePlayer.updateResources(h.resource, 2);
        EasyMock.expectLastCall();

        EasyMock.replay(mockRedPlayer, mockWhitePlayer, mockBluePlayer);

        h.awardCityResources();

        EasyMock.verify(mockRedPlayer, mockWhitePlayer, mockBluePlayer);
    }

    @Test // Test Case 30
    public void AwardCityResources_WithTwoPlayers_OnDesert_ExpectNoUpdate() {
        Hex h = new Hex(1, Resource.DESERT, 9);

        Player mockOrangePlayer = EasyMock.createMock(Player.class);
        Player mockBluePlayer = EasyMock.createMock(Player.class);

        h.addPlayerCityToHex(mockOrangePlayer);
        h.addPlayerCityToHex(mockBluePlayer);

        EasyMock.replay(mockOrangePlayer, mockBluePlayer);

        // Want no update to happen, as we are on a desert
        h.awardCityResources();

        EasyMock.verify(mockOrangePlayer, mockBluePlayer);
    }



}

