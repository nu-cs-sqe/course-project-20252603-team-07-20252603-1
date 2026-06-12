package domain.model.exceptions;

/** Thrown when a road is placed in an invalid location. */
public class IllegalRoadPlacementException extends RuntimeException {

  /**
   * Creates a new IllegalRoadPlacementException with the given message.
   *
   * @param message description of the violation
   */
  public IllegalRoadPlacementException(String message) {
    super(message);
  }
}
