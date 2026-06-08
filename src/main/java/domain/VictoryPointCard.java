package domain;

public class VictoryPointCard extends DevelopmentCard {

  private static final int VICTORY_POINTS = 1;

  public VictoryPointCard() {
    super(DevelopmentCardType.VICTORY_POINT);
  }

  public int getVictoryPoints() {
    return VICTORY_POINTS;
  }
}
