package domain.model.exceptions;

public class IllegalGamePhaseException extends RuntimeException {
    public IllegalGamePhaseException(String message) {
        super(message);
    }
}
