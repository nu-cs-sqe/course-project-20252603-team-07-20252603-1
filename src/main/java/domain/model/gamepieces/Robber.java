package domain.model.gamepieces;

/** Represents the robber piece that blocks resource production on a hex. */
public class Robber {

  private static final int MIN_HEX_ID = 0;
  private static final int MAX_HEX_ID = 18;

  private int currentHexId;

  /**
   * Creates a new Robber at the specified hex.
   *
   * @param initialId the starting hex ID for the robber
   */
  public Robber(int initialId) {
    currentHexId = initialId;
  }

  /** Returns the hex ID where the robber is currently located. */
  public int getRobberLocation() {
    return currentHexId;
  }

  /**
   * Moves the robber to the specified hex.
   *
   * @param hexId the destination hex ID
   */
  public void moveRobber(int hexId) {
    if (hexId < MIN_HEX_ID || hexId > MAX_HEX_ID) {
      throw new IllegalArgumentException("Cannot move Robber to invalid HexId");
    }
    currentHexId = hexId;
  }
}
