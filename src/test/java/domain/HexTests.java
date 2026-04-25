package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HexTests {
    @Test // Test Case 1
    public void AddEmptyString_OnEmptyList_ExpectLenOne(){
        Hex h = new Hex(1, "Lumber", 9);
        h.addPlayerSettlementToHex("");

        int expected = 1;
        int actual = h.getSettlements().size();
        assertEquals(expected, actual);
    }

    @Test // Test Case 2
    public void AddTwoStrings_OneEmpty_ExpectLenTwo(){
        Hex h = new Hex(1, "Lumber", 9);
        h.addPlayerSettlementToHex("Blue");
        h.addPlayerSettlementToHex("");

        int expected = 2;
        int actual = h.getSettlements().size();
        assertEquals(expected, actual);
    }

    @Test // Test Case 3
    public void AddString_ToListWithDuplicates_ExpectLenThree(){
        Hex h = new Hex(1, "Lumber", 9);
        h.addPlayerSettlementToHex("Blue");
        h.addPlayerSettlementToHex("Blue");
        h.addPlayerSettlementToHex("Orange");

        int expected = 3;
        int actual = h.getSettlements().size();
        assertEquals(expected, actual);
    }

    @Test // Test Case 4
    public void AddString_ToListWithThreeElements_ExpectError(){
        Hex h = new Hex(1, "Lumber", 9);
        h.addPlayerSettlementToHex("Blue");
        h.addPlayerSettlementToHex("Blue");
        h.addPlayerSettlementToHex("Blue");

        Exception exception = assertThrows(IllegalStateException.class, ()-> {
            h.addPlayerSettlementToHex("White");
        });

        String expectedMessage = "Already three settlements on hex.";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test // Test Case 5
    public void RemoveFromEmptyList_ExpectError(){
        Hex h = new Hex(1, "Lumber", 9);

        Exception exception = assertThrows(IllegalStateException.class, ()-> {
            h.removePlayerSettlementFromHex("White");
        });

        String expectedMessage = "Player does not have a building on hex.";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test // Test Case 6
    public void RemoveFromList_WithOneElement_ExpectLenZero(){
        Hex h = new Hex(1, "Lumber", 9);

        h.addPlayerSettlementToHex("Blue");

        h.removePlayerSettlementFromHex("Blue");

        int expected = 0;
        int actual = h.getSettlements().size();
        assertEquals(expected, actual);
    }

    @Test // Test Case 6
    public void RemoveFromList_WithTwoDuplicates_ExpectLenOne(){
        Hex h = new Hex(1, "Lumber", 9);

        h.addPlayerSettlementToHex("Blue");
        h.addPlayerSettlementToHex("Blue");

        h.removePlayerSettlementFromHex("Blue");

        int expected = 1;
        int actual = h.getSettlements().size();
        assertEquals(expected, actual);

    }
}
