package domain.model.development_cards;

import org.junit.jupiter.api.Test;

import domain.model.exceptions.EmptyDeckException;

import static org.junit.jupiter.api.Assertions.*;

class DevelopmentCardDeckTest {
  final int DECK_SIZE = 25;

    // TC1, TC13: new DevelopmentCardDeck() -> countRemaining() == 25
    @Test
    void constructDeck_OnNewDeck_ExpectTwentyFiveCards() {
        DevelopmentCardDeck deck = new DevelopmentCardDeck();

        assertEquals(DECK_SIZE, deck.countRemaining());
    }

  // TC2: new DevelopmentCardDeck() -> deck contains exactly 14 KNIGHT cards
  @Test
  void constructDeck_OnNewDeck_ExpectFourteenKnightCards() throws EmptyDeckException {
    DevelopmentCardDeck deck = new DevelopmentCardDeck();
    final int expectedKnightCount = 14;

    int knightCount = 0;
    for (int i = 0; i < DECK_SIZE; i++) {
      if (deck.drawCard().getType() == DevelopmentCardType.KNIGHT) knightCount++;
    }

    assertEquals(expectedKnightCount, knightCount);
  }

  // TC3: new DevelopmentCardDeck() -> deck contains exactly 2 ROAD_BUILDER cards
  @Test
  void constructDeck_OnNewDeck_ExpectTwoRoadBuilderCards() throws EmptyDeckException {
    DevelopmentCardDeck deck = new DevelopmentCardDeck();
    final int expectedRoadBuilderCount = 2;

    int roadBuilderCount = 0;
    for (int i = 0; i < DECK_SIZE; i++) {
      if (deck.drawCard().getType() == DevelopmentCardType.ROAD_BUILDER) roadBuilderCount++;
    }

    assertEquals(expectedRoadBuilderCount, roadBuilderCount);
  }

  // TC4: new DevelopmentCardDeck() -> deck contains exactly 2 YEAR_OF_PLENTY cards
  @Test
  void constructDeck_OnNewDeck_ExpectTwoYearOfPlentyCards() throws EmptyDeckException {
    DevelopmentCardDeck deck = new DevelopmentCardDeck();
    final int expectedYearOfPlentyCount = 2;

    int yearOfPlentyCount = 0;
    for (int i = 0; i < DECK_SIZE; i++) {
      if (deck.drawCard().getType() == DevelopmentCardType.YEAR_OF_PLENTY) yearOfPlentyCount++;
    }

    assertEquals(expectedYearOfPlentyCount, yearOfPlentyCount);
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
