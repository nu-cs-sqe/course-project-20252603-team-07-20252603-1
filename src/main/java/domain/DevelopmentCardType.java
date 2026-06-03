package domain;

import java.util.function.Supplier;

public enum DevelopmentCardType {
  KNIGHT(KnightCard::new),
  ROAD_BUILDING(RoadBuildingCard::new),
  YEAR_OF_PLENTY(YearOfPlentyCard::new),
  MONOPOLY(MonopolyCard::new),
  VICTORY_POINT(VictoryPointCard::new);

  private final Supplier<DevelopmentCard> factory;

  DevelopmentCardType(Supplier<DevelopmentCard> factory) {
    this.factory = factory;
  }

  public DevelopmentCard createCard() {
    return factory.get();
  }
}
