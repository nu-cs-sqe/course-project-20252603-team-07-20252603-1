package domain.model.exceptions;

/** Thrown when a node ID is outside the valid range [0, 53]. */
public class IllegalNodeIdException extends RuntimeException {

  /**
   * Creates a new IllegalNodeIdException with the given message.
   *
   * @param message description of the violation
   */
  public IllegalNodeIdException(String message) {
    super(message);
  }
}
