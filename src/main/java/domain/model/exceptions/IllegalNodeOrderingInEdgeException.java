package domain.model.exceptions;

/** Thrown when the starting node ID is not less than the ending node ID. */
public class IllegalNodeOrderingInEdgeException extends RuntimeException {

  /**
   * Creates a new IllegalNodeOrderingInEdgeException with the given message.
   *
   * @param message description of the violation
   */
  public IllegalNodeOrderingInEdgeException(String message) {
    super(message);
  }
}
