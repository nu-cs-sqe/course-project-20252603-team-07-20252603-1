package domain.model.exceptions;

/** Thrown when an action is attempted in the wrong game phase. */
public class IllegalGamePhaseException extends RuntimeException {

  /**
   * Creates a new IllegalGamePhaseException with the given message.
   *
   * @param message description of the violation
   */
  public IllegalGamePhaseException(String message) {
    super(message);
  }
}
