package domain.model.resources;

import domain.model.exceptions.EmptyDeckException;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/** Represents a deck of one type of resource card. */
public class ResourceDeck {

  private static final int TOTAL_NUMBER_OF_RESOURCES = 95;
  private static final int NUMBER_OF_RESOURCES_PER_DECK = 19;

  private int count;
  private Resource type;

  /** Creates a placeholder deck covering all resource types. */
  public ResourceDeck() {
    this.type = null;
    this.count = TOTAL_NUMBER_OF_RESOURCES;
  }

  /**
   * Creates a deck for the specified resource type.
   *
   * @param type the resource type (must not be DESERT)
   */
  @SuppressFBWarnings(
      value = "CT_CONSTRUCTOR_THROW",
      justification = "Validation in constructor is intentional; no finalizer risk")
  public ResourceDeck(Resource type) {
    if (type == Resource.DESERT) {
      throw new IllegalArgumentException("Resource must be tradeable.");
    }
    this.type = type;
    this.count = NUMBER_OF_RESOURCES_PER_DECK;
  }

  /**
   * Returns the resource type of this deck.
   *
   * @return the resource type
   */
  public Resource getType() {
    return this.type;
  }

    public Resource draw() throws EmptyDeckException {
        // just instantiate a brand new one, decrease count
        if (count > 0) {
            this.count--;
            return this.type; // caller will index into store and ++
        } else {
            throw new EmptyDeckException(String.format("Cannot draw new %s card, no cards remain.", this.type.name()));
        }
    }


    public int drawMultiple(int numCards) throws EmptyDeckException {
        int numCardsReturning = numCards <= this.count ? numCards : this.count;
        this.count -= numCardsReturning;
        return numCardsReturning;
    }

  /** Returns one card to this deck. */
  public void replenish() {
    this.count++;
  }

  /**
   * Returns up to the specified number of cards, capped at the deck maximum.
   *
   * @param numToReplenish number of cards to return
   */
  public void replenish(int numToReplenish) {
    if (this.count + numToReplenish >= NUMBER_OF_RESOURCES_PER_DECK) {
      this.count = NUMBER_OF_RESOURCES_PER_DECK;
    } else {
      this.count += numToReplenish;
    }
  }

  /** Replenishes this deck to its maximum capacity. */
  public void replenishAll() {
    this.replenish(NUMBER_OF_RESOURCES_PER_DECK);
  }

  /**
   * Returns the total number of cards remaining in this deck.
   *
   * @return the card count
   */
  public int getTotalCards() {
    return count;
  }
}
