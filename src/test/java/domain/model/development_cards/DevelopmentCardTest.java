package domain.model.development_cards;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DevelopmentCardTest {

    // TC1: new DevelopmentCard(KNIGHT, 0) -> getType() == KNIGHT, roundDrawnAt == 0
    @Test
    void constructKnightCard_AtRoundZero_ExpectKnightTypeAndRoundZero() {
        DevelopmentCard card = new DevelopmentCard(DevelopmentCardType.KNIGHT, 0);

        assertEquals(DevelopmentCardType.KNIGHT, card.getType());
        assertEquals(0, card.getRoundDrawnAt());
    }

    // TC2: new DevelopmentCard(VICTORY_POINT, 5) -> getType() == VICTORY_POINT, roundDrawnAt == 5
    @Test
    void constructVictoryPointCard_AtRoundFive_ExpectVictoryPointTypeAndRoundFive() {
        DevelopmentCard card = new DevelopmentCard(DevelopmentCardType.VICTORY_POINT, 5);

        assertEquals(DevelopmentCardType.VICTORY_POINT, card.getType());
        assertEquals(5, card.getRoundDrawnAt());
    }

    // TC3: new DevelopmentCard(ROAD_BUILDER, 1) -> getType() == ROAD_BUILDER, roundDrawnAt == 1
    @Test
    void constructRoadBuilderCard_AtRoundOne_ExpectRoadBuilderTypeAndRoundOne() {
        DevelopmentCard card = new DevelopmentCard(DevelopmentCardType.ROAD_BUILDER, 1);

        assertEquals(DevelopmentCardType.ROAD_BUILDER, card.getType());
        assertEquals(1, card.getRoundDrawnAt());
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
