package domain.model.exceptions;

/** Thrown when a player lacks the resources required for an action. */
public class InsufficientResourcesException extends RuntimeException {

  /**
   * Creates a new InsufficientResourcesException with the given message.
   *
   * @param message description of the violation
   */
  public InsufficientResourcesException(String message) {
    super(message);
  }
}
