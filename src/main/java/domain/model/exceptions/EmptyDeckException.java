package domain.model.exceptions;

/**
 * Thrown when drawing from an empty development card deck.
 */
public class EmptyDeckException extends Exception {

  /**
   * Creates a new EmptyDeckException with the given message.
   *
   * @param message description of the violation
   */
  public EmptyDeckException(String message) {
    super(message);
  }
}
