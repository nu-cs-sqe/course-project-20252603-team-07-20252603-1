package domain.model.developmentcards;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DevelopmentCardTest {

    // TC1, TC6: new DevelopmentCard(KNIGHT, 0) -> getType() == KNIGHT, roundDrawnAt == 0
    @Test
    void constructKnightCard_AtRoundZero_ExpectKnightTypeAndRoundZero() {
      final int expectedRound = 0;
      DevelopmentCard card = new DevelopmentCard(DevelopmentCardType.KNIGHT, 0);

      assertEquals(DevelopmentCardType.KNIGHT, card.getType());
      assertEquals(expectedRound, card.getRoundDrawnAt());
    }

    // TC2, TC7: new DevelopmentCard(VICTORY_POINT, 5) -> getType() == VICTORY_POINT, roundDrawnAt == 5
    @Test
    void constructVictoryPointCard_AtRoundFive_ExpectVictoryPointTypeAndRoundFive() {
      final int expectedRound = 5;
      DevelopmentCard card = new DevelopmentCard(DevelopmentCardType.VICTORY_POINT, 5);

      assertEquals(DevelopmentCardType.VICTORY_POINT, card.getType());
      assertEquals(expectedRound, card.getRoundDrawnAt());
    }

    // TC3, TC8: new DevelopmentCard(ROAD_BUILDER, 1) -> getType() == ROAD_BUILDER, roundDrawnAt == 1
    @Test
    void constructRoadBuilderCard_AtRoundOne_ExpectRoadBuilderTypeAndRoundOne() {
      final int expectedRound = 1;
      DevelopmentCard card = new DevelopmentCard(DevelopmentCardType.ROAD_BUILDER, 1);

      assertEquals(DevelopmentCardType.ROAD_BUILDER, card.getType());
      assertEquals(expectedRound, card.getRoundDrawnAt());
    }

    // TC4, TC9: new DevelopmentCard(YEAR_OF_PLENTY, 10) -> getType() == YEAR_OF_PLENTY, roundDrawnAt == 10
    @Test
    void constructYearOfPlentyCard_AtRoundTen_ExpectYearOfPlentyTypeAndRoundTen() {
      final int expectedRound = 10;
      DevelopmentCard card = new DevelopmentCard(DevelopmentCardType.YEAR_OF_PLENTY, 10);

      assertEquals(DevelopmentCardType.YEAR_OF_PLENTY, card.getType());
      assertEquals(expectedRound, card.getRoundDrawnAt());
    }

    // TC5, TC10: new DevelopmentCard(MONOPOLY, 3) -> getType() == MONOPOLY, roundDrawnAt == 3
    @Test
    void constructMonopolyCard_AtRoundThree_ExpectMonopolyTypeAndRoundThree() {
      final int expectedRound = 3;
      DevelopmentCard card = new DevelopmentCard(DevelopmentCardType.MONOPOLY, 3);

      assertEquals(DevelopmentCardType.MONOPOLY, card.getType());
      assertEquals(expectedRound, card.getRoundDrawnAt());
    }

    // TC11: isPlayable(4), KNIGHT drawn at round 5 (currentRound < roundDrawnAt) -> false
    @Test
    void isPlayable_KnightCardCurrentRoundLessThanDrawnRound_ExpectFalse() {
        final int drawnAtRound = 5;
        final int currentRound = 4;

        DevelopmentCard card = new DevelopmentCard(DevelopmentCardType.KNIGHT, drawnAtRound);
        assertFalse(card.isPlayable(currentRound));
    }

    // TC12: isPlayable(5), KNIGHT drawn at round 5 (currentRound == roundDrawnAt) -> false
    @Test
    void isPlayable_KnightCardCurrentRoundEqualsDrawnRound_ExpectFalse() {
        final int drawnAtRound = 5;
        final int currentRound = 5;

        DevelopmentCard card = new DevelopmentCard(DevelopmentCardType.KNIGHT, drawnAtRound);
        assertFalse(card.isPlayable(currentRound));
    }

    // TC13: isPlayable(6), KNIGHT drawn at round 5 (currentRound > roundDrawnAt) -> true
    @Test
    void isPlayable_KnightCardCurrentRoundGreaterThanDrawnRound_ExpectTrue() {
        final int drawnAtRound = 5;
        final int currentRound = 6;

        DevelopmentCard card = new DevelopmentCard(DevelopmentCardType.KNIGHT, drawnAtRound);
        assertTrue(card.isPlayable(currentRound));
    }

    // TC14: isPlayable(0), MONOPOLY drawn at round 0 (both at LOW boundary) -> false
    @Test
    void isPlayable_MonopolyCardBothRoundsAtLowBoundary_ExpectFalse() {
        final int drawnAtRound = 0;
        final int currentRound = 0;

        DevelopmentCard card = new DevelopmentCard(DevelopmentCardType.MONOPOLY, drawnAtRound);
        assertFalse(card.isPlayable(currentRound));
    }

    // TC15: isPlayable(1), ROAD_BUILDER drawn at round 0 (LOW + ε vs LOW) -> true
    @Test
    void isPlayable_RoadBuilderCardCurrentRoundOneAboveLowBoundary_ExpectTrue() {
        final int drawnAtRound = 0;
        final int currentRound = 1;

        DevelopmentCard card = new DevelopmentCard(DevelopmentCardType.ROAD_BUILDER, drawnAtRound);
        assertTrue(card.isPlayable(currentRound));
    }

    // TC16: isPlayable(5), VICTORY_POINT drawn at round 5 (same turn — VP exception) -> true
    @Test
    void isPlayable_VictoryPointCardCurrentRoundEqualsDrawnRound_ExpectTrue() {
        final int drawnAtRound = 5;
        final int currentRound = 5;

        DevelopmentCard card = new DevelopmentCard(DevelopmentCardType.VICTORY_POINT, drawnAtRound);
        assertTrue(card.isPlayable(currentRound));
    }

    // TC17: isPlayable(0), VICTORY_POINT drawn at round 0 (LOW boundary — VP exception) -> true
    @Test
    void isPlayable_VictoryPointCardBothRoundsAtLowBoundary_ExpectTrue() {
        final int drawnAtRound = 0;
        final int currentRound = 0;

        DevelopmentCard card = new DevelopmentCard(DevelopmentCardType.VICTORY_POINT, drawnAtRound);
        assertTrue(card.isPlayable(currentRound));
    }
}
