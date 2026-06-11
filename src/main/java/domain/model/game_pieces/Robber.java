package domain.model.game_pieces;

public class Robber {
  private static final int MIN_HEX_ID = 0;
  private static final int MAX_HEX_ID = 18;

  private int currentHexId;

  public Robber(int initialId) {
    currentHexId = initialId;
  }

  public int getRobberLocation() {
    return currentHexId;
  }

  public void moveRobber(int hexId) {
    if (hexId < MIN_HEX_ID || hexId > MAX_HEX_ID) {
      throw new IllegalArgumentException("Cannot move Robber to invalid HexId");
    }
    currentHexId = hexId;
  }

}
