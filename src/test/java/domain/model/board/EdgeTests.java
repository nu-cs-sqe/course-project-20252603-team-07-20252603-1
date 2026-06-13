package domain.model.board;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EdgeTests {
  // TC1 ← REDUCES CXTY
  @Test
  void constructor_NewEdge_ExpectNotNull() {
    assertNotNull(new Edge());
  }
}
