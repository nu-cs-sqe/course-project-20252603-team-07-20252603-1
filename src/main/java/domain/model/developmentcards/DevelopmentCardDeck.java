package domain.model.developmentcards;

import domain.model.exceptions.EmptyDeckException;
import java.util.Collections;
import java.util.Stack;

/** Represents the deck of development cards used during the game. */
public class DevelopmentCardDeck {

  private Stack<DevelopmentCard> deck;
  private int cardsLeft;

  private int knightCount;
  private int victoryPointCount;
  private int roadBuilderCount;
  private int yearOfPlentyCount;
  private int monopolyCount;

  /** Creates and shuffles a full development card deck with standard card counts. */
  public DevelopmentCardDeck() {
    this.deck = new Stack<DevelopmentCard>();
    this.knightCount = 14;
    this.victoryPointCount = 5;
    this.roadBuilderCount = 2;
    this.yearOfPlentyCount = 2;
    this.monopolyCount = 2;

    for (int i = 0; i < knightCount; i++) {
      DevelopmentCard knight = DevelopmentCard.createKnightDevelopmentCard(0);
      this.deck.push(knight);
    }

    for (int i = 0; i < victoryPointCount; i++) {
      DevelopmentCard victoryPoint = DevelopmentCard.createVictoryPointDevelopmentCard(0);
      this.deck.push(victoryPoint);
    }

    for (int i = 0; i < roadBuilderCount; i++) {
      DevelopmentCard roadBuilder = DevelopmentCard.createRoadBuilderDevelopmentCard(0);
      this.deck.push(roadBuilder);
    }

    for (int i = 0; i < yearOfPlentyCount; i++) {
      DevelopmentCard yearOfPlenty = DevelopmentCard.createYearOfPlentyDevelopmentCard(0);
      this.deck.push(yearOfPlenty);
    }

    for (int i = 0; i < monopolyCount; i++) {
      DevelopmentCard monopoly = DevelopmentCard.createMonopolyDevelopmentCard(0);
      this.deck.push(monopoly);
    }

    this.cardsLeft = this.deck.size();
    this.shuffle();
  }

  /** Shuffles the deck. */
  public void shuffle() {
    Collections.shuffle(this.deck);
  }

  /**
   * Draws the top card from the deck, updating its round-drawn timestamp.
   *
   * @param currentRound the round number when the card is drawn
   * @return the drawn development card
   * @throws EmptyDeckException if no cards remain
   */
  public DevelopmentCard drawCard(int currentRound) throws EmptyDeckException {
    if (this.cardsLeft <= 0) {
      throw new EmptyDeckException("Cannot draw new DevelopmentCard, no cards remain.");
    }

    DevelopmentCard drawn = this.deck.pop();
    drawn.setRoundDrawnAt(currentRound);
    this.cardsLeft--;
    return drawn;
  }

  /**
   * Returns the number of cards remaining in the deck.
   *
   * @return cards remaining
   */
  public int countRemaining() {
    return cardsLeft;
  }
}
