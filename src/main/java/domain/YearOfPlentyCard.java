package domain;

import java.util.HashMap;
import java.util.Map;

public class YearOfPlentyCard extends DevelopmentCard {

  public YearOfPlentyCard() {
    super(DevelopmentCardType.YEAR_OF_PLENTY);
  }

  public void play(Player player, Resource resource1, Resource resource2) {
    if (player == null) {
      throw new IllegalArgumentException("Player cannot be null.");
    }
    if (resource1 == null || resource2 == null) {
      throw new IllegalArgumentException("Resource cannot be null.");
    }
    if (resource1 == Resource.DESERT || resource2 == Resource.DESERT) {
      throw new IllegalArgumentException("Cannot take DESERT as a resource.");
    }

    player.receiveResources(buildResourceMap(resource1, resource2));
  }

  private Map<Resource, Integer> buildResourceMap(Resource resource1, Resource resource2) {
    Map<Resource, Integer> resources = new HashMap<>();

    resources.merge(resource1, 1, Integer::sum);
    resources.merge(resource2, 1, Integer::sum);
    
    return resources;
  }
}
