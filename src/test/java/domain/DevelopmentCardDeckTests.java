package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DevelopmentCardDeckTests {
    final int EXPECTED_NUM_CARDS = 25;

    @Test // Test Case 1
    public void Constructor_Default_ExpectSizeTwentyFive() {
        DevelopmentCardDeck deck = new DevelopmentCardDeck();
        int actual = deck.size();

        assertEquals(EXPECTED_NUM_CARDS, actual);
    }
}
