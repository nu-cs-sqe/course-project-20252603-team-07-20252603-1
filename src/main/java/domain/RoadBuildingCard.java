package domain;

public class RoadBuildingCard extends DevelopmentCard {

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

    int roadsPlaced = player.getRoads().size();

    player.placeRoad(edge1);
    player.placeRoad(edge2);
  }
}
