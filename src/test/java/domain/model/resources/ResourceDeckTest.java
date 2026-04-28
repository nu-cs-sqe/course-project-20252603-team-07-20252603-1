package domain.model.resources;

import domain.model.EmptyDeckException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ResourceDeckTest {

    @Test
    void testConstructorInitializesWithCorrectTypeAndCount() {
        ResourceDeck woodDeck = new ResourceDeck(ResourceType.WOOD);
        assertEquals(ResourceType.WOOD, woodDeck.getType());
    }

    @Test
    void testDrawSingleCard() throws EmptyDeckException {
        ResourceDeck deck = new ResourceDeck(ResourceType.ROCK);
        ResourceCard card = deck.draw();

        assertNotNull(card);
        assertEquals(ResourceType.ROCK, card.getType());
    }

    @Test
    void testDrawMultipleCards() {
        ResourceDeck deck = new ResourceDeck(ResourceType.SHEEP);
        ResourceCard[] cards = deck.drawMultiple(5);

        assertEquals(5, cards.length);
        for (ResourceCard card : cards) {
            assertEquals(ResourceType.SHEEP, card.getType());
        }
    }

    @Test
    void testDrawMultipleCardsExceedingAvailable() {
        ResourceDeck deck = new ResourceDeck(ResourceType.CLAY);

        // Draw 15 cards first, leaving 4
        deck.drawMultiple(15);

        // Try to draw 10, should only get 4
        ResourceCard[] cards = deck.drawMultiple(10);
        assertEquals(4, cards.length);
    }

    @Test
    void testDrawFromEmptyDeckThrowsException() {
        ResourceDeck deck = new ResourceDeck(ResourceType.WHEAT);

        // Draw all 19 cards
        deck.drawMultiple(19);

        // Try to draw one more
        EmptyDeckException exception = assertThrows(EmptyDeckException.class, () -> {
            deck.draw();
        });

        assertTrue(exception.getMessage().contains("WHEAT"));
    }

    @Test
    void testReplenishSingleCard() throws EmptyDeckException {
        ResourceDeck deck = new ResourceDeck(ResourceType.WOOD);

        // Draw all cards
        deck.drawMultiple(19);

        // Replenish one
        deck.replenish();

        // Should be able to draw one
        ResourceCard card = deck.draw();
        assertNotNull(card);
    }

    @Test
    void testReplenishMultipleCards() throws EmptyDeckException {
        ResourceDeck deck = new ResourceDeck(ResourceType.ROCK);

        // Draw all cards
        deck.drawMultiple(19);

        // Replenish 5
        deck.replenish(5);

        // Should be able to draw 5
        ResourceCard[] cards = deck.drawMultiple(5);
        assertEquals(5, cards.length);
    }

    @Test
    void testReplenishCapsAt19() throws EmptyDeckException {
        ResourceDeck deck = new ResourceDeck(ResourceType.SHEEP);

        // Draw 5 cards (14 left)
        deck.drawMultiple(5);

        // Try to replenish 10 (would be 24 total, should cap at 19)
        deck.replenish(10);

        // Should be able to draw exactly 19
        ResourceCard[] cards = deck.drawMultiple(20);
        assertEquals(19, cards.length);
    }

    @Test
    void testReplenishAll() throws EmptyDeckException {
        ResourceDeck deck = new ResourceDeck(ResourceType.CLAY);

        // Draw some cards
        deck.drawMultiple(10);

        // Replenish all
        deck.replenishAll();

        // Should be able to draw all 19
        ResourceCard[] cards = deck.drawMultiple(19);
        assertEquals(19, cards.length);
    }

    @Test
    void testDrawMultipleReturnsEmptyArrayWhenDeckEmpty() {
        ResourceDeck deck = new ResourceDeck(ResourceType.WHEAT);

        // Draw all cards
        deck.drawMultiple(19);

        // Try to draw more
        ResourceCard[] cards = deck.drawMultiple(5);
        assertEquals(0, cards.length);
    }
}
