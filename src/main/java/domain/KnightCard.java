package domain;

public class KnightCard extends DevelopmentCard {

  public KnightCard() {
    super(DevelopmentCardType.KNIGHT);
  }

  public void play(Robber robber, int targetHexId, Player victim) {
    if (robber == null) {
      throw new IllegalArgumentException("Robber cannot be null.");
    }
  }
}
