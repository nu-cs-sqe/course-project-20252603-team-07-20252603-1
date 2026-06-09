package domain.model.development_cards;

import org.easymock.EasyMock;
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
      if (deck.drawCard(0).getType() == DevelopmentCardType.KNIGHT) knightCount++;
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
      if (deck.drawCard(0).getType() == DevelopmentCardType.ROAD_BUILDER) roadBuilderCount++;
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
      if (deck.drawCard(0).getType() == DevelopmentCardType.YEAR_OF_PLENTY) yearOfPlentyCount++;
    }

    assertEquals(expectedYearOfPlentyCount, yearOfPlentyCount);
  }

  // TC5: new DevelopmentCardDeck() -> deck contains exactly 2 MONOPOLY cards
  @Test
  void constructDeck_OnNewDeck_ExpectTwoMonopolyCards() throws EmptyDeckException {
    DevelopmentCardDeck deck = new DevelopmentCardDeck();
    final int expectedMonopolyCount = 2;

    int monopolyCount = 0;
    for (int i = 0; i < DECK_SIZE; i++) {
      if (deck.drawCard(0).getType() == DevelopmentCardType.MONOPOLY) monopolyCount++;
    }

    assertEquals(expectedMonopolyCount, monopolyCount);
  }

  // TC6: new DevelopmentCardDeck() -> deck contains exactly 5 VICTORY_POINT cards
  @Test
  void constructDeck_OnNewDeck_ExpectFiveVictoryPointCards() throws EmptyDeckException {
    DevelopmentCardDeck deck = new DevelopmentCardDeck();
    final int expectedVictoryPointCount = 5;

    int victoryPointCount = 0;
    for (int i = 0; i < DECK_SIZE; i++) {
      if (deck.drawCard(0).getType() == DevelopmentCardType.VICTORY_POINT) victoryPointCount++;
    }

    assertEquals(expectedVictoryPointCount, victoryPointCount);
  }

  // TC7: drawCard(3) from full deck (size 25) -> card non-null with valid type, roundDrawnAt == 3, countRemaining() == 24
  @Test
  void drawCard_FromFullDeck_ExpectValidCardStampedWithRoundAndCountDecremented() throws EmptyDeckException {
    final int currentRound = 3;
    final int expectedRemaining = 24;

    DevelopmentCardDeck deck = new DevelopmentCardDeck();
    DevelopmentCard card = deck.drawCard(currentRound);

    assertNotNull(card);
    assertNotNull(card.getType());
    
    assertEquals(currentRound, card.getRoundDrawnAt());
    assertEquals(expectedRemaining, deck.countRemaining());
  }

  // TC8: drawCard(7) from deck with 1 card remaining -> card returned (roundDrawnAt == 7), countRemaining() == 0
  @Test
  void drawCard_FromDeckWithOneCardRemaining_ExpectCardStampedWithRoundAndCountZero() throws EmptyDeckException {
    final int currentRound = 7;
    final int expectedRemaining = 0;

    DevelopmentCardDeck deck = new DevelopmentCardDeck();
    for (int i = 0; i < 24; i++) deck.drawCard(0);

    DevelopmentCard card = deck.drawCard(currentRound);

    assertNotNull(card);
    assertEquals(currentRound, card.getRoundDrawnAt());
    assertEquals(expectedRemaining, deck.countRemaining());
  }

  // TC9: drawCard(1) from empty deck (size 0) -> EmptyDeckException: "Cannot draw new DevelopmentCard, no cards remain."
  @Test
  void drawCard_FromEmptyDeck_ExpectEmptyDeckException() throws EmptyDeckException {
    DevelopmentCardDeck deck = new DevelopmentCardDeck();
    for (int i = 0; i < DECK_SIZE; i++) deck.drawCard(0);

    Exception exception = assertThrows(EmptyDeckException.class, () -> deck.drawCard(1));
    assertEquals("Cannot draw new DevelopmentCard, no cards remain.", exception.getMessage());
  }

    @Test
    void testDrawCardReducesCount() throws EmptyDeckException {
        DevelopmentCardDeck deck = new DevelopmentCardDeck();
        int initialCount = deck.countRemaining();

        deck.drawCard(0);

        assertEquals(initialCount - 1, deck.countRemaining());
    }

    @Test
    void testDrawCardReturnsValidCard() throws EmptyDeckException {
        DevelopmentCardDeck deck = new DevelopmentCardDeck();
        DevelopmentCard card = deck.drawCard(0);

        assertNotNull(card);
        assertNotNull(card.getType());
    }

    @Test
    void testDrawAllCardsFromDeck() throws EmptyDeckException {
        DevelopmentCardDeck deck = new DevelopmentCardDeck();

        // Draw all 25 cards
        for (int i = 0; i < 25; i++) {
            DevelopmentCard card = deck.drawCard(0);
            assertNotNull(card);
        }

        assertEquals(0, deck.countRemaining());
    }

    @Test
    void testCountRemainingAfterMultipleDraws() throws EmptyDeckException {
        DevelopmentCardDeck deck = new DevelopmentCardDeck();

        deck.drawCard(0);
        assertEquals(24, deck.countRemaining());

        deck.drawCard(0);
        deck.drawCard(0);
        assertEquals(22, deck.countRemaining());

        for (int i = 0; i < 10; i++) {
            deck.drawCard(0);
        }
        assertEquals(12, deck.countRemaining());
    }
}
