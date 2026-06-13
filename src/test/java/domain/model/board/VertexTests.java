package domain.model.board;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class VertexTests {
  // TC1 ← REDUCES CXTY
  @Test
  void constructor_NewVertex_ExpectNotNull() {
    assertNotNull(new Vertex());
  }
}
