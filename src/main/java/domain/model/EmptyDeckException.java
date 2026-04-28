package domain.model;

public class EmptyDeckException extends Exception {
    public EmptyDeckException(String message) {
        super(message);
    }
}