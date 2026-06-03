package domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DevelopmentCardDeckTests {
  final int EXPECTED_NUM_CARDS = 25;
  final int EXPECTED_NUM_KNIGHTS = 14;
  final int EXPECTED_NUM_ROAD_BUILDING = 2;
  final int EXPECTED_NUM_YEAR_OF_PLENTY = 2;
  final int EXPECTED_NUM_MONOPOLY = 2;
  final int EXPECTED_NUM_VICTORY_POINT = 5;

  @Test // Test Case 1
  public void Constructor_Default_ExpectSizeTwentyFive() {
    DevelopmentCardDeck deck = new DevelopmentCardDeck();
    int actual = deck.size();

    assertEquals(EXPECTED_NUM_CARDS, actual);
  }

  @Test // Test Case 2
  public void Constructor_Default_ExpectFourteenKnightCards() {
    DevelopmentCardDeck deck = new DevelopmentCardDeck();
    long actual = deck.getCards().stream()
        .filter(card -> card.getType() == DevelopmentCardType.KNIGHT)
        .count();

    assertEquals(EXPECTED_NUM_KNIGHTS, actual);
  }

  @Test // Test Case 3
  public void Constructor_Default_ExpectTwoRoadBuildingCards() {
    DevelopmentCardDeck deck = new DevelopmentCardDeck();
    long actual = deck.getCards().stream()
        .filter(card -> card.getType() == DevelopmentCardType.ROAD_BUILDING)
        .count();

    assertEquals(EXPECTED_NUM_ROAD_BUILDING, actual);
  }

  @Test // Test Case 4
  public void Constructor_Default_ExpectTwoYearOfPlentyCards() {
    DevelopmentCardDeck deck = new DevelopmentCardDeck();
    long actual = deck.getCards().stream()
        .filter(card -> card.getType() == DevelopmentCardType.YEAR_OF_PLENTY)
        .count();

    assertEquals(EXPECTED_NUM_YEAR_OF_PLENTY, actual);
  }

  @Test // Test Case 5
  public void Constructor_Default_ExpectTwoMonopolyCards() {
    DevelopmentCardDeck deck = new DevelopmentCardDeck();
    long actual = deck.getCards().stream()
        .filter(card -> card.getType() == DevelopmentCardType.MONOPOLY)
        .count();

    assertEquals(EXPECTED_NUM_MONOPOLY, actual);
  }

  @Test // Test Case 6
  public void Constructor_Default_ExpectFiveVictoryPointCards() {
    DevelopmentCardDeck deck = new DevelopmentCardDeck();
    long actual = deck.getCards().stream()
        .filter(card -> card.getType() == DevelopmentCardType.VICTORY_POINT)
        .count();

    assertEquals(EXPECTED_NUM_VICTORY_POINT, actual);
  }

  @Test // Test Case 7
  public void DrawCard_FullDeck_ExpectCardReturnedAndSizeTwentyFour() {
    DevelopmentCardDeck deck = new DevelopmentCardDeck();
    DevelopmentCard drawn = deck.drawCard();

    assertNotNull(drawn);
    assertEquals(EXPECTED_NUM_CARDS - 1, deck.size());
  }

  @Test // Test Case 8
  public void DrawCard_OneCardRemaining_ExpectCardReturnedAndSizeZero() {
    DevelopmentCardDeck deck = new DevelopmentCardDeck();
    for (int i = 0; i < EXPECTED_NUM_CARDS - 1; i++) {
      deck.drawCard();
    }
    DevelopmentCard drawn = deck.drawCard();

    assertNotNull(drawn);
    assertEquals(0, deck.size());
  }

  @Test // Test Case 9
  public void DrawCard_EmptyDeck_ExpectIllegalStateException() {
    DevelopmentCardDeck deck = new DevelopmentCardDeck();
    for (int i = 0; i < EXPECTED_NUM_CARDS; i++) {
      deck.drawCard();
    }

    IllegalStateException exception = assertThrows(
        IllegalStateException.class,
        deck::drawCard
    );
    assertEquals("The development card deck is empty.", exception.getMessage());
  }

  @Test // Test Case 10
  public void Shuffle_FullDeck_ExpectOrderRandomizedAndSizeTwentyFive() {
    DevelopmentCardDeck deck = new DevelopmentCardDeck();
    List<DevelopmentCard> before = deck.getCards();

    deck.shuffle();

    assertEquals(EXPECTED_NUM_CARDS, deck.size());
    assertNotEquals(before, deck.getCards());
  }

  @Test // Test Case 11
  public void Shuffle_OneCardDeck_ExpectDeckUnchangedAndSizeOne() {
    DevelopmentCardDeck deck = new DevelopmentCardDeck();
    for (int i = 0; i < EXPECTED_NUM_CARDS - 1; i++) {
      deck.drawCard();
    }
    List<DevelopmentCard> before = deck.getCards();

    deck.shuffle();

    assertEquals(1, deck.size());
    assertEquals(before, deck.getCards());
  }

  @Test // Test Case 12
  public void Shuffle_EmptyDeck_ExpectDeckRemainsEmptyAndNoError() {
    DevelopmentCardDeck deck = new DevelopmentCardDeck();
    for (int i = 0; i < EXPECTED_NUM_CARDS; i++) {
      deck.drawCard();
    }

    deck.shuffle();

    assertEquals(0, deck.size());
  }

  @Test // Test Case 13
  public void IsEmpty_NewDeck_ExpectFalse() {
    DevelopmentCardDeck deck = new DevelopmentCardDeck();
    boolean actual = deck.isEmpty();

    assertEquals(false, actual);
  }

  @Test // Test Case 14
  public void IsEmpty_AllCardsDrawn_ExpectTrue() {
    DevelopmentCardDeck deck = new DevelopmentCardDeck();
    for (int i = 0; i < EXPECTED_NUM_CARDS; i++) {
      deck.drawCard();
    }
    boolean actual = deck.isEmpty();
    boolean expected = true;

    assertEquals(expected, actual);
  }

  @Test // Test Case 15
  public void IsEmpty_OneCardRemaining_ExpectFalse() {
    DevelopmentCardDeck deck = new DevelopmentCardDeck();
    for (int i = 0; i < EXPECTED_NUM_CARDS - 1; i++) {
      deck.drawCard();
    }
    boolean actual = deck.isEmpty();
    boolean expected = false;

    assertEquals(expected, actual);
  }

  @Test // Test Case 16
  public void Size_NewDeck_ExpectTwentyFive() {
    DevelopmentCardDeck deck = new DevelopmentCardDeck();
    int actual = deck.size();

    assertEquals(EXPECTED_NUM_CARDS, actual);
  }
}
