package domain.model.exceptions;

public class IllegalCityPlacementException extends RuntimeException {
  public IllegalCityPlacementException(String message) {
    super(message);
  }
}
