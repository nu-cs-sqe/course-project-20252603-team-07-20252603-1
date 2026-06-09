package domain.model.development_cards;

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

    @Test
    void testCreateKnightDevelopmentCard() {
        DevelopmentCard knight = DevelopmentCard.createKnightDevelopmentCard(3);
        assertEquals(DevelopmentCardType.KNIGHT, knight.getType());
    }

    @Test
    void testCreateVictoryPointDevelopmentCard() {
        DevelopmentCard vp = DevelopmentCard.createVictoryPointDevelopmentCard(2);
        assertEquals(DevelopmentCardType.VICTORY_POINT, vp.getType());
    }

    @Test
    void testCreateRoadBuilderDevelopmentCard() {
        DevelopmentCard rb = DevelopmentCard.createRoadBuilderDevelopmentCard(1);
        assertEquals(DevelopmentCardType.ROAD_BUILDER, rb.getType());
    }

    @Test
    void testCreateYearOfPlentyDevelopmentCard() {
        DevelopmentCard yop = DevelopmentCard.createYearOfPlentyDevelopmentCard(4);
        assertEquals(DevelopmentCardType.YEAR_OF_PLENTY, yop.getType());
    }

    @Test
    void testCreateMonopolyDevelopmentCard() {
        DevelopmentCard monopoly = DevelopmentCard.createMonopolyDevelopmentCard(6);
        assertEquals(DevelopmentCardType.MONOPOLY, monopoly.getType());
    }

    @Test
    void testIsPlayableOnSameRound() {
        DevelopmentCard card = new DevelopmentCard(DevelopmentCardType.KNIGHT, 5);
        assertTrue(card.isPlayable(5));
    }

    @Test
    void testIsPlayableOnLaterRound() {
        DevelopmentCard card = new DevelopmentCard(DevelopmentCardType.KNIGHT, 5);
        assertTrue(card.isPlayable(10));
    }

    @Test
    void testIsNotPlayableOnEarlierRound() {
        DevelopmentCard card = new DevelopmentCard(DevelopmentCardType.KNIGHT, 5);
        assertFalse(card.isPlayable(4));
    }

    @Test
    void testIsPlayableImmediatelyAfterDrawn() {
        DevelopmentCard card = new DevelopmentCard(DevelopmentCardType.ROAD_BUILDER, 3);
        assertFalse(card.isPlayable(2));
        assertTrue(card.isPlayable(3));
        assertTrue(card.isPlayable(4));
    }
}
