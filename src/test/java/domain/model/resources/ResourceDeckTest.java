package domain.model.resources;

import org.junit.jupiter.api.Test;

import domain.model.exceptions.EmptyDeckException;

import static org.junit.jupiter.api.Assertions.*;

class ResourceDeckTest {

  @Test
  void testConstructorInitializesWithCorrectTypeAndCount() {
    ResourceDeck woodDeck = new ResourceDeck(Resource.LUMBER);
    assertEquals(Resource.LUMBER, woodDeck.getType());
  }

  @Test
  void testDrawSingleCard() throws EmptyDeckException {
    ResourceDeck deck = new ResourceDeck(Resource.ORE);
    Resource card = deck.draw();

    assertNotNull(card);
    assertEquals(Resource.ORE, card);
  }

  @Test
  void testDrawMultipleCards() {
    ResourceDeck deck = new ResourceDeck(Resource.WOOL);
    int drawn = deck.drawMultiple(5);

    assertEquals(5, drawn);
  }

  @Test
  void testDrawMultipleCardsExceedingAvailable() {
    ResourceDeck deck = new ResourceDeck(Resource.BRICK);

    // Draw 15 cards first, leaving 4
    deck.drawMultiple(15);

    // Try to draw 10, should only get 4
    int drawn = deck.drawMultiple(10);
    assertEquals(4, drawn);
  }

  @Test
  void testDrawFromEmptyDeckThrowsException() {
    ResourceDeck deck = new ResourceDeck(Resource.GRAIN);

    // Draw all 19 cards
    deck.drawMultiple(19);

    // Try to draw one more
    EmptyDeckException exception = assertThrows(EmptyDeckException.class, () -> {
      deck.draw();
    });

    assertTrue(exception.getMessage().contains("GRAIN"));
  }

  @Test
  void testReplenishSingleCard() throws EmptyDeckException {
    ResourceDeck deck = new ResourceDeck(Resource.LUMBER);

    // Draw all cards
    deck.drawMultiple(19);

    // Replenish one
    deck.replenish();

    // Should be able to draw one
    Resource card = deck.draw();
    assertNotNull(card);
  }

  @Test
  void testReplenishMultipleCards() throws EmptyDeckException {
    ResourceDeck deck = new ResourceDeck(Resource.ORE);

    // Draw all cards
    deck.drawMultiple(19);

    // Replenish 5
    deck.replenish(5);

    // Should be able to draw 5
    int drawn = deck.drawMultiple(5);
    assertEquals(5, drawn);
  }

  @Test
  void testReplenishCapsAt19() throws EmptyDeckException {
    ResourceDeck deck = new ResourceDeck(Resource.WOOL);

    // Draw 5 cards (14 left)
    deck.drawMultiple(5);

    // Try to replenish 10 (would be 24 total, should cap at 19)
    deck.replenish(10);

    // Should be able to draw exactly 19
    int drawn = deck.drawMultiple(20);
    assertEquals(19, drawn);
  }

  @Test
  void testReplenishAll() throws EmptyDeckException {
    ResourceDeck deck = new ResourceDeck(Resource.BRICK);

    // Draw some cards
    deck.drawMultiple(10);

    // Replenish all
    deck.replenishAll();

    // Should be able to draw all 19
    int drawn = deck.drawMultiple(19);
    assertEquals(19, drawn);
  }

  @Test
  void testDrawMultipleReturnsEmptyArrayWhenDeckEmpty() {
    ResourceDeck deck = new ResourceDeck(Resource.GRAIN);

    // Draw all cards
    deck.drawMultiple(19);

    // Try to draw more
    int drawn = deck.drawMultiple(5);
    assertEquals(0, drawn);
  }
}
