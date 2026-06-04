package domain;

public class RoadBuildingCard extends DevelopmentCard {

  private static final int MAX_ROADS = 15;

  public RoadBuildingCard() {
    super(DevelopmentCardType.ROAD_BUILDING);
  }

  public void play(Player player, Edge edge1, Edge edge2) {
    if (player == null) {
      throw new IllegalArgumentException("Player cannot be null.");
    }
    if (edge1 == null) {
      throw new IllegalArgumentException("Edge cannot be null.");
    }

    int remainingRoads = MAX_ROADS - player.getRoads().size();
    if (remainingRoads == 0) {
      throw new IllegalStateException("No roads remaining.");
    }

    player.placeRoad(edge1);
    if (remainingRoads >= 2) {
      player.placeRoad(edge2);
    }
  }
}
