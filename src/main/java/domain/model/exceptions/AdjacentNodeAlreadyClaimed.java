package domain.model.exceptions;

public class AdjacentNodeAlreadyClaimed extends RuntimeException {
  public AdjacentNodeAlreadyClaimed(String message) {
    super(message);
  }
}
