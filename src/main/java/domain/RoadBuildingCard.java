package domain;

public class RoadBuildingCard extends DevelopmentCard {

  public RoadBuildingCard() {
    super(DevelopmentCardType.ROAD_BUILDING);
  }

  public void play(Player player, Edge edge1, Edge edge2) {
    if (player == null) {
      throw new IllegalArgumentException("Player cannot be null.");
    }
  }
}
