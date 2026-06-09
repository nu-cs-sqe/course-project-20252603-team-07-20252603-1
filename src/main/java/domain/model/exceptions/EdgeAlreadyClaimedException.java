package domain.model.exceptions;

public class EdgeAlreadyClaimedException extends RuntimeException {
    public EdgeAlreadyClaimedException(String message) {
        super(message);
    }
}
