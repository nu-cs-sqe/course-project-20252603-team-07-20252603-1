package domain.model.development_cards;

import org.junit.jupiter.api.Test;

import domain.model.exceptions.EmptyDeckException;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class DevelopmentCardDeckTest {

    // TC1, TC13: new DevelopmentCardDeck() -> countRemaining() == 25
    @Test
    void constructDeck_OnNewDeck_ExpectTwentyFiveCards() {
        DevelopmentCardDeck deck = new DevelopmentCardDeck();

        assertEquals(25, deck.countRemaining());
    }

  // TC2: new DevelopmentCardDeck() -> deck contains exactly 14 KNIGHT cards
  @Test
  void constructDeck_OnNewDeck_ExpectFourteenKnightCards() throws EmptyDeckException {
    DevelopmentCardDeck deck = new DevelopmentCardDeck();
    final int expectedKnightCount = 14;

    List<DevelopmentCard> drawnCards = new ArrayList<>();
    while (deck.countRemaining() > 0) drawnCards.add(deck.drawCard());

    int knightCount = 0;
    for (DevelopmentCard card : drawnCards) {
      if (card.getType() == DevelopmentCardType.KNIGHT) knightCount++;
    }

    assertEquals(expectedKnightCount, knightCount);
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
