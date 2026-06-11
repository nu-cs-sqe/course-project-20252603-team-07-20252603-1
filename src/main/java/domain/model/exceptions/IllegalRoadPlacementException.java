package domain.model.exceptions;

public class IllegalRoadPlacementException extends RuntimeException {
  public IllegalRoadPlacementException(String message) {
    super(message);
  }
}
