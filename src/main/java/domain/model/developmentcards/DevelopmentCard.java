package domain.model.developmentcards;

/**
 * Represents a single development card with a type and the round it was drawn.
 */
public class DevelopmentCard {

  private DevelopmentCardType type;
  private int roundDrawnAt;

  /**
   * Creates a development card of the given type drawn on the given round.
   *
   * @param type               the card type
   * @param currentRoundNumber the round number when this card was created
   */
  public DevelopmentCard(DevelopmentCardType type, int currentRoundNumber) {
    this.type = type;
    this.roundDrawnAt = currentRoundNumber;
  }

  /**
   * Creates a Knight development card.
   *
   * @param currentRoundNumber the current round
   * @return a new Knight card
   */
  public static DevelopmentCard createKnightDevelopmentCard(int currentRoundNumber) {
    return new DevelopmentCard(DevelopmentCardType.KNIGHT, currentRoundNumber);
  }

  /**
   * Creates a Victory Point development card.
   *
   * @param currentRoundNumber the current round
   * @return a new Victory Point card
   */
  public static DevelopmentCard createVictoryPointDevelopmentCard(int currentRoundNumber) {
    return new DevelopmentCard(DevelopmentCardType.VICTORY_POINT, currentRoundNumber);
  }

  /**
   * Creates a Road Builder development card.
   *
   * @param currentRoundNumber the current round
   * @return a new Road Builder card
   */
  public static DevelopmentCard createRoadBuilderDevelopmentCard(int currentRoundNumber) {
    return new DevelopmentCard(DevelopmentCardType.ROAD_BUILDER, currentRoundNumber);
  }

  /**
   * Creates a Year of Plenty development card.
   *
   * @param currentRoundNumber the current round
   * @return a new Year of Plenty card
   */
  public static DevelopmentCard createYearOfPlentyDevelopmentCard(int currentRoundNumber) {
    return new DevelopmentCard(DevelopmentCardType.YEAR_OF_PLENTY, currentRoundNumber);
  }

  /**
   * Creates a Monopoly development card.
   *
   * @param currentRoundNumber the current round
   * @return a new Monopoly card
   */
  public static DevelopmentCard createMonopolyDevelopmentCard(int currentRoundNumber) {
    return new DevelopmentCard(DevelopmentCardType.MONOPOLY, currentRoundNumber);
  }

  /**
   * Returns whether this card can be played on the given round.
   *
   * @param currentRoundNumber the current round
   * @return true if the card is playable
   */
  public boolean isPlayable(int currentRoundNumber) {
    if (this.type == DevelopmentCardType.VICTORY_POINT) {
      return true;
    }
    return currentRoundNumber > this.roundDrawnAt;
  }

  /**
   * Returns the card type.
   *
   * @return the card type
   */
  public DevelopmentCardType getType() {
    return this.type;
  }

  /**
   * Returns the round this card was drawn on.
   *
   * @return the round number
   */
  public int getRoundDrawnAt() {
    return this.roundDrawnAt;
  }

  /**
   * Sets the round this card was drawn on.
   *
   * @param round the round number
   */
  public void setRoundDrawnAt(int round) {
    this.roundDrawnAt = round;
  }
}
