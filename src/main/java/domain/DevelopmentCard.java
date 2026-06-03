package domain;

public abstract class DevelopmentCard {

  private final DevelopmentCardType type;

  protected DevelopmentCard(DevelopmentCardType type) {
    this.type = type;
  }

  public DevelopmentCardType getType() {
    return type;
  }
}
