package domain;

import java.util.List;

public class MonopolyCard extends DevelopmentCard {

  public MonopolyCard() {
    super(DevelopmentCardType.MONOPOLY);
  }

  public void play(Resource resource, List<Player> otherPlayers) {
    if (resource == null) {
      throw new IllegalArgumentException("Resource cannot be null.");
    }
    if (resource == Resource.DESERT) {
      throw new IllegalArgumentException("Cannot monopolize DESERT.");
    }
  }
}
