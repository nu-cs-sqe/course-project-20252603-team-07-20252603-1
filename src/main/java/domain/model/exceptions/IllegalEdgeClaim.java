package domain.model.exceptions;

/** Thrown when a road claim violates placement rules. */
public class IllegalEdgeClaim extends RuntimeException {

  /**
   * Creates a new IllegalEdgeClaim with the given message.
   *
   * @param message description of the violation
   */
  public IllegalEdgeClaim(String message) {
    super(message);
  }
}
