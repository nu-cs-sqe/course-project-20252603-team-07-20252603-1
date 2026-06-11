package domain.model.exceptions;

public class InsufficientResourcesException extends RuntimeException {
  public InsufficientResourcesException(String message) {
    super(message);
  }
}
