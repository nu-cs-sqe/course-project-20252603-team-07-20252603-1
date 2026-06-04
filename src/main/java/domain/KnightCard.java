package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class KnightCard extends DevelopmentCard {

  private final Random random;

  public KnightCard() {
    this(new Random());
  }

  public KnightCard(Random random) {
    super(DevelopmentCardType.KNIGHT);
    this.random = random;
  }

  public void play(Player player, Robber robber, int targetHexId, Player victim) {
    if (robber == null) {
      throw new IllegalArgumentException("Robber cannot be null.");
    }
    robber.moveRobber(targetHexId);

    if (victim != null) {
      Map<Resource, Integer> resources = victim.getResources();

      if (!resources.isEmpty()) {
        List<Resource> available = new ArrayList<>(resources.keySet());
        Resource stolen = available.get(random.nextInt(available.size()));

        victim.updateResources(stolen, -1);
        player.receiveResources(Map.of(stolen, 1));
      }
    }
  }
}
