package domain.model.exceptions;

/** Thrown when a city is placed on an invalid or unowned node. */
public class IllegalCityPlacementException extends RuntimeException {

  /**
   * Creates a new IllegalCityPlacementException with the given message.
   *
   * @param message description of the violation
   */
  public IllegalCityPlacementException(String message) {
    super(message);
  }
}
