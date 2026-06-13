package domain.model.resources;

import domain.model.exceptions.EmptyDeckException;

public class ResourceDeck {

    private static final int TOTAL_NUMBER_OF_RESOURCES = 95;
    private static final int NUMBER_OF_RESOURCES_PER_DECK = 19;

    private int count;
    private Resource type;

    /**
     * Default constructor for testing purposes.
     * Creates a placeholder deck. Real implementation should use ResourceDeck(Resource).
     * TODO: Future work - implement proper multi-resource deck management.
     */
    public ResourceDeck() {
        this.type = null; // Placeholder for all resource types
        this.count = TOTAL_NUMBER_OF_RESOURCES; // 5 types * 19 cards each
    }

    public ResourceDeck(Resource type) {
        this.type = type;
        this.count = NUMBER_OF_RESOURCES_PER_DECK; // game standard

        if (type == Resource.DESERT ) {
            throw new IllegalArgumentException("Resource must be tradeable.");
        }
    }

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

    public void replenish() {
        // to be used to put 1 card back into the deck
        this.count++;
    }


    public void replenish(int numToReplenish) { 
        // assuming we wanna keep max at 19
        if (this.count + numToReplenish >= NUMBER_OF_RESOURCES_PER_DECK) {
            this.count = NUMBER_OF_RESOURCES_PER_DECK;
        } else {
            this.count += numToReplenish;
        }
    }

    public void replenishAll() {
        // this may be bad coding to have -- almost unnecessary and def
        this.replenish(NUMBER_OF_RESOURCES_PER_DECK); 
    }

    /**
     * Gets the total number of cards in the deck.
     * For individual resource decks, returns count.
     * For composite deck (no-arg constructor), returns total across all types.
     *
     * @return the total card count
     */
    public int getTotalCards() {
        return count;
    }

    @Override
    protected final void finalize() {
        // intentionally empty — blocks finalizer attacks
    }


}