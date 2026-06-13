package domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import domain.model.exceptions.EmptyDeckException;
import org.junit.jupiter.api.Test;

class EmptyDeckExceptionTest {

  @Test
  void testExceptionMessageIsPreserved() {
    String message = "Test exception message";
    EmptyDeckException exception = new EmptyDeckException(message);

    assertEquals(message, exception.getMessage());
  }

  @Test
  void testExceptionCanBeThrown() {
    Exception exception = assertThrows(EmptyDeckException.class, () -> {
      throw new EmptyDeckException("Cannot draw card");
    });
    assertEquals("Cannot draw card", exception.getMessage());
  }
}
