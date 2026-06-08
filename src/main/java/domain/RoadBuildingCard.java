package domain;

public class RoadBuildingCard extends DevelopmentCard {

  private static final int MAX_ROADS = 15;

  public RoadBuildingCard() {
    super(DevelopmentCardType.ROAD_BUILDING);
  }

  public void play(Player player, Edge edge1, Edge edge2) {
    int remainingRoads = validatePlacement(player, edge1);

    player.placeRoad(edge1);
    if (remainingRoads >= 2) {
      if (edge2.isOccupied()) {
        throw new IllegalArgumentException("Edge is already occupied.");
      }
      if (!edge2.isConnectedToPlayerNetwork()) {
        throw new IllegalArgumentException("Road must connect to player's existing network.");
      }
      player.placeRoad(edge2);
    }
  }

  private int validatePlacement(Player player, Edge edge1) {
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
    if (edge1.isOccupied()) {
      throw new IllegalArgumentException("Edge is already occupied.");
    }
    if (!edge1.isConnectedToPlayerNetwork()) {
      throw new IllegalArgumentException("Road must connect to player's existing network.");
    }
    return remainingRoads;
  }
}
