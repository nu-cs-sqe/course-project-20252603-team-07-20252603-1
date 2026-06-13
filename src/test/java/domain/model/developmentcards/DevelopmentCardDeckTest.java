package domain.model.developmentcards;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import domain.model.exceptions.EmptyDeckException;
import org.junit.jupiter.api.Test;

class DevelopmentCardDeckTest {
  static final int DECK_SIZE = 25;

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
      if (deck.drawCard(0).getType() == DevelopmentCardType.KNIGHT) {
        knightCount++;
      }
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
      if (deck.drawCard(0).getType() == DevelopmentCardType.ROAD_BUILDER) {
        roadBuilderCount++;
      }
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
      if (deck.drawCard(0).getType() == DevelopmentCardType.YEAR_OF_PLENTY) {
        yearOfPlentyCount++;
      }
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
      if (deck.drawCard(0).getType() == DevelopmentCardType.MONOPOLY) {
        monopolyCount++;
      }
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
      if (deck.drawCard(0).getType() == DevelopmentCardType.VICTORY_POINT) {
        victoryPointCount++;
      }
    }

    assertEquals(expectedVictoryPointCount, victoryPointCount);
  }

  // TC7: drawCard(3) from full deck (size 25) -> card non-null with valid type, roundDrawnAt == 3, countRemaining() == 24
  @Test
  void drawCard_FromFullDeck_ExpectValidCardStampedWithRoundAndCountDecremented()
      throws EmptyDeckException {
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
  void drawCard_FromDeckWithOneCardRemaining_ExpectCardStampedWithRoundAndCountZero()
      throws EmptyDeckException {
    final int currentRound = 7;
    final int expectedRemaining = 0;

    DevelopmentCardDeck deck = new DevelopmentCardDeck();
    for (int i = 0; i < 24; i++) {
      deck.drawCard(0);
    }

    DevelopmentCard card = deck.drawCard(currentRound);

    assertNotNull(card);
    assertEquals(currentRound, card.getRoundDrawnAt());
    assertEquals(expectedRemaining, deck.countRemaining());
  }

  // TC9: drawCard(1) from empty deck (size 0) -> EmptyDeckException: "Cannot draw new DevelopmentCard, no cards remain."
  @Test
  void drawCard_FromEmptyDeck_ExpectEmptyDeckException() throws EmptyDeckException {
    DevelopmentCardDeck deck = new DevelopmentCardDeck();
    for (int i = 0; i < DECK_SIZE; i++) {
      deck.drawCard(0);
    }

    Exception exception = assertThrows(EmptyDeckException.class, () -> deck.drawCard(1));
    assertEquals("Cannot draw new DevelopmentCard, no cards remain.", exception.getMessage());
  }

  // TC10: shuffle() on full deck (25) -> card order is randomized; countRemaining() still 25
  // Note: probabilistic — compares two independently shuffled sequences; failure probability is 1/25! ≈ 10^-25
  @Test
  void shuffle_OnFullDeck_ExpectRandomizedOrderAndCountUnchanged() throws EmptyDeckException {
    DevelopmentCardDeck deck1 = new DevelopmentCardDeck();
    DevelopmentCardDeck deck2 = new DevelopmentCardDeck();
    deck2.shuffle();

    assertEquals(DECK_SIZE, deck2.countRemaining());

    DevelopmentCardType[] order1 = new DevelopmentCardType[DECK_SIZE];
    DevelopmentCardType[] order2 = new DevelopmentCardType[DECK_SIZE];
    for (int i = 0; i < DECK_SIZE; i++) {
      order1[i] = deck1.drawCard(0).getType();
      order2[i] = deck2.drawCard(0).getType();
    }

    boolean isDifferent = false;
    for (int i = 0; i < DECK_SIZE; i++) {
      if (order1[i] != order2[i]) {
        isDifferent = true;
        break;
      }
    }
    assertTrue(isDifferent);
  }

  // TC11: shuffle() on deck with 1 card -> deck unchanged; countRemaining() still 1
  @Test
  void shuffle_OnDeckWithOneCard_ExpectCountUnchanged() throws EmptyDeckException {
    DevelopmentCardDeck deck = new DevelopmentCardDeck();
    for (int i = 0; i < 24; i++) {
      deck.drawCard(0);
    }

    deck.shuffle();

    assertEquals(1, deck.countRemaining());
    assertNotNull(deck.drawCard(0));
  }

  // TC12: shuffle() on empty deck (0) -> deck remains empty; no error
  @Test
  void shuffle_OnEmptyDeck_ExpectNoErrorAndCountZero() throws EmptyDeckException {
    DevelopmentCardDeck deck = new DevelopmentCardDeck();
    for (int i = 0; i < DECK_SIZE; i++) {
      deck.drawCard(0);
    }

    assertDoesNotThrow(deck::shuffle);
    assertEquals(0, deck.countRemaining());
  }

  // TC14: countRemaining() after drawing 1 card -> 24
  @Test
  void countRemaining_AfterDrawingOneCard_ExpectTwentyFour() throws EmptyDeckException {
    DevelopmentCardDeck deck = new DevelopmentCardDeck();
    final int expectedRemaining = 24;

    deck.drawCard(0);

    assertEquals(expectedRemaining, deck.countRemaining());
  }

  // TC15: countRemaining() after drawing all 25 -> 0
  @Test
  void countRemaining_AfterDrawingAllCards_ExpectZero() throws EmptyDeckException {
    DevelopmentCardDeck deck = new DevelopmentCardDeck();
    final int expectedRemaining = 0;

    for (int i = 0; i < DECK_SIZE; i++) {
      deck.drawCard(0);
    }

    assertEquals(expectedRemaining, deck.countRemaining());
  }
}
