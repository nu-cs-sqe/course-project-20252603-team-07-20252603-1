package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
