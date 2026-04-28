package domain.model.development_cards;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DevelopmentCardTest {

    @Test
    void testConstructorAndGetter() {
        DevelopmentCard card = new DevelopmentCard(DevelopmentCardType.KNIGHT, 5);
        assertEquals(DevelopmentCardType.KNIGHT, card.getType());
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
