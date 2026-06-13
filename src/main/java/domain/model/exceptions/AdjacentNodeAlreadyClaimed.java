package domain.model.exceptions;

/**
 * Thrown when a node adjacent to an already-claimed node is claimed.
 */
public class AdjacentNodeAlreadyClaimed extends RuntimeException {

  /**
   * Creates a new AdjacentNodeAlreadyClaimed with the given message.
   *
   * @param message description of the violation
   */
  public AdjacentNodeAlreadyClaimed(String message) {
    super(message);
  }
}
