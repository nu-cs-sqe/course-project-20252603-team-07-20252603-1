package domain.model.development_cards;

import domain.model.EmptyDeckException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DevelopmentCardDeckTest {

    @Test
    void testConstructorInitializesWithCorrectCardCount() {
        DevelopmentCardDeck deck = new DevelopmentCardDeck();
        assertEquals(25, deck.countRemaining());
    }

    @Test
    void testDrawCardReducesCount() throws EmptyDeckException {
        DevelopmentCardDeck deck = new DevelopmentCardDeck();
        int initialCount = deck.countRemaining();

        deck.drawCard();

        assertEquals(initialCount - 1, deck.countRemaining());
    }

    @Test
    void testDrawCardReturnsValidCard() throws EmptyDeckException {
        DevelopmentCardDeck deck = new DevelopmentCardDeck();
        DevelopmentCard card = deck.drawCard();

        assertNotNull(card);
        assertNotNull(card.getType());
    }

    @Test
    void testDrawAllCardsFromDeck() throws EmptyDeckException {
        DevelopmentCardDeck deck = new DevelopmentCardDeck();

        // Draw all 25 cards
        for (int i = 0; i < 25; i++) {
            DevelopmentCard card = deck.drawCard();
            assertNotNull(card);
        }

        assertEquals(0, deck.countRemaining());
    }

    @Test
    void testDrawFromEmptyDeckThrowsException() throws EmptyDeckException {
        DevelopmentCardDeck deck = new DevelopmentCardDeck();

        // Draw all 25 cards
        for (int i = 0; i < 25; i++) {
            deck.drawCard();
        }

        // Try to draw one more
        assertThrows(EmptyDeckException.class, () -> {
            deck.drawCard();
        });
    }

    @Test
    void testDeckContainsCorrectCardDistribution() throws EmptyDeckException {
        DevelopmentCardDeck deck = new DevelopmentCardDeck();

        int knightCount = 0;
        int victoryPointCount = 0;
        int roadBuilderCount = 0;
        int yearOfPlentyCount = 0;
        int monopolyCount = 0;

        // Draw all cards and count types
        for (int i = 0; i < 25; i++) {
            DevelopmentCard card = deck.drawCard();
            switch (card.getType()) {
                case KNIGHT:
                    knightCount++;
                    break;
                case VICTORY_POINT:
                    victoryPointCount++;
                    break;
                case ROAD_BUILDER:
                    roadBuilderCount++;
                    break;
                case YEAR_OF_PLENTY:
                    yearOfPlentyCount++;
                    break;
                case MONOPOLY:
                    monopolyCount++;
                    break;
            }
        }

        assertEquals(14, knightCount);
        assertEquals(5, victoryPointCount);
        assertEquals(2, roadBuilderCount);
        assertEquals(2, yearOfPlentyCount);
        assertEquals(2, monopolyCount);
    }

    @Test
    void testCountRemainingAfterMultipleDraws() throws EmptyDeckException {
        DevelopmentCardDeck deck = new DevelopmentCardDeck();

        deck.drawCard();
        assertEquals(24, deck.countRemaining());

        deck.drawCard();
        deck.drawCard();
        assertEquals(22, deck.countRemaining());

        for (int i = 0; i < 10; i++) {
            deck.drawCard();
        }
        assertEquals(12, deck.countRemaining());
    }
}
