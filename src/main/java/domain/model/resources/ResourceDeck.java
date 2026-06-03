package domain.model.resources;

import domain.model.exceptions.EmptyDeckException;
import domain.model.resources.Resource;

public class ResourceDeck {

    private int count;
    private Resource type;

    /**
     * Default constructor for testing purposes.
     * Creates a placeholder deck. Real implementation should use ResourceDeck(Resource).
     * TODO: Future work - implement proper multi-resource deck management.
     */
    public ResourceDeck() {
        this.type = null; // Placeholder for all resource types
        this.count = 95; // 5 types * 19 cards each
    }

    public ResourceDeck(Resource type) {
        this.type = type;
        this.count = 19; // game standard

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

    // ASSUMPTION -- WE WIL STORE PLAYER DECK IN SOME SORT OF ARRAYLIST AND CAN USE list1.addAll(list2)
    public int drawMultiple(int numCards) {
        // here we'll assume that if there are 2 cards left and you want to draw 3, you get 2 and you deal with it.
        // i.e. it is not an error to return less than numCards if it finishes the deck

        int numCardsReturning = numCards <= this.count ? numCards : this.count;

        // Resource[] cardsToReturn = new Resource[numCardsReturning];

        // for (int i = 0; i < numCardsReturning; i++) {
        //     cardsToReturn[i] = this.type;
        // }

        this.count -= numCardsReturning;

        return numCardsReturning;

    }

    public void replenish() {
        // to be used to put 1 card back into the deck
        this.count++;
    }


    public void replenish(int numToReplenish) { 
        // assuming we wanna keep max at 19
        if (this.count + numToReplenish >= 19) {
            this.count = 19;
        } else {
            this.count += numToReplenish;
        }
    }

    public void replenishAll() {
        // this may be bad coding to have -- almost unnecessary and def
        this.replenish(20); // will always set us back at 19.
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
