package domain.model.exceptions;

/** Thrown when a road is placed on an already-occupied edge. */
public class EdgeAlreadyClaimedException extends RuntimeException {

  /**
   * Creates a new EdgeAlreadyClaimedException with the given message.
   *
   * @param message description of the violation
   */
  public EdgeAlreadyClaimedException(String message) {
    super(message);
  }
}
