package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
}
