package domain;

import java.util.List;
import java.util.Map;

public class MonopolyCard extends DevelopmentCard {

  public MonopolyCard() {
    super(DevelopmentCardType.MONOPOLY);
  }

  public void play(Player player, Resource resource, List<Player> otherPlayers) {
    if (resource == null) {
      throw new IllegalArgumentException("Resource cannot be null.");
    }
    if (resource == Resource.DESERT) {
      throw new IllegalArgumentException("Cannot monopolize DESERT.");
    }
    if (otherPlayers == null) {
      throw new IllegalArgumentException("Other players list cannot be null.");
    }

    for (Player opponent : otherPlayers) {
      int count = opponent.getResourceCount(resource);
      if (count > 0) {
        opponent.updateResources(resource, -count);
        player.receiveResources(Map.of(resource, count));
      }
    }
  }
}
