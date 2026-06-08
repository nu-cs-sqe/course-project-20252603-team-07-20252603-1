package domain;

public class YearOfPlentyCard extends DevelopmentCard {

  public YearOfPlentyCard() {
    super(DevelopmentCardType.YEAR_OF_PLENTY);
  }

  public void play(Player player, Resource resource1, Resource resource2) {
    if (player == null) {
      throw new IllegalArgumentException("Player cannot be null.");
    }
    if (resource1 == null) {
      throw new IllegalArgumentException("Resource cannot be null.");
    }
  }
}
