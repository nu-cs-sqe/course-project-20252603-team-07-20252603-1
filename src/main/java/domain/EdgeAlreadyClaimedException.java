package domain;

public class EdgeAlreadyClaimedException extends RuntimeException {
    public EdgeAlreadyClaimedException(String message) {
        super(message);
    }
}
