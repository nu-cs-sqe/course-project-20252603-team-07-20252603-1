package domain.model.board;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class VertexTests {
  // TC1 ← REDUCES CXTY
  @Test
  void constructor_NewVertex_ExpectNotNull() {
    assertNotNull(new Vertex());
  }

  // TC2 ← REDUCES CXTY
  @Test
  void isOccupied_NewVertex_ExpectFalse() {
    assertFalse(new Vertex().isOccupied());
  }

  // TC3 ← REDUCES CXTY
  @Test
  void hasAdjacentSettlementViolatingDistanceRule_NewVertex_ExpectFalse() {
    assertFalse(new Vertex().hasAdjacentSettlementViolatingDistanceRule());
  }
}
