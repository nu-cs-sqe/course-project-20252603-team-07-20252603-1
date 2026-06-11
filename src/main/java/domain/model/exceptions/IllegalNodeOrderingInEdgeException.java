package domain.model.exceptions;

public class IllegalNodeOrderingInEdgeException extends RuntimeException {
  public IllegalNodeOrderingInEdgeException(String message) {
    super(message);
  }
}
