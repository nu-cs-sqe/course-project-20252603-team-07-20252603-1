package domain.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EmptyDeckExceptionTest {

    @Test
    void testExceptionMessageIsPreserved() {
        String message = "Test exception message";
        EmptyDeckException exception = new EmptyDeckException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    void testExceptionCanBeThrown() {
        assertThrows(EmptyDeckException.class, () -> {
            throw new EmptyDeckException("Cannot draw card");
        });
    }
}
