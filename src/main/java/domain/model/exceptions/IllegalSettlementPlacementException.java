package domain.model.exceptions;

/** Thrown when a settlement is placed in an invalid location. */
public class IllegalSettlementPlacementException extends RuntimeException {

  /**
   * Creates a new IllegalSettlementPlacementException with the given message.
   *
   * @param message description of the violation
   */
  public IllegalSettlementPlacementException(String message) {
    super(message);
  }
}
