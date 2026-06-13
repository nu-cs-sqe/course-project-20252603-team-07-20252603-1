package domain.model.board;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import domain.model.exceptions.EdgeAlreadyClaimedException;
import domain.model.exceptions.IllegalNodeOrderingInEdgeException;
import domain.model.player.PlayerColor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

/** Test class. */
public class GraphEdgeTests {
  @ParameterizedTest
  @EnumSource(value = PlayerColor.class, names = {"RED", "WHITE", "ORANGE", "BLUE"})
  void claimGraphEdge_NodeUnoccupied_ExpectTrue(PlayerColor color) {
    GraphEdge e1 = new GraphEdge(0, 1);
    assertTrue(e1.claimGraphEdge(color));
    assertTrue(e1.checkRoadExists());
    assertEquals(color, e1.checkOwningColor());

  }

  @Test
  void claimGraphEdge_EdgeUnoccupied_ExpectError() {
    GraphEdge e1 = new GraphEdge(52, 53);
    e1.claimGraphEdge((PlayerColor.BLUE));

    Exception exception = assertThrows(EdgeAlreadyClaimedException.class,
        () -> e1.claimGraphEdge(PlayerColor.RED));

    assertEquals("Edge already claimed", exception.getMessage());
    assertTrue(e1.checkRoadExists());
    assertEquals(PlayerColor.BLUE, e1.checkOwningColor());

  }

  @Test
  void assertValidNodeIdsOrdering_test01_ValidInput_ExpectSuccess() {
    GraphEdge e1 = new GraphEdge(0, 3);
    assertNotNull(e1);
  }

  @Test
  void assertValidNodeIdsOrdering_test02_ValidInput_ExpectSuccess() {
    GraphEdge e1 = new GraphEdge(50, 53);
    assertNotNull(e1);
  }

  @Test
  void assertValidNodeIdsOrdering_test03_EqualNodeIDs_ExpectError() {
    Exception exception = assertThrows(IllegalNodeOrderingInEdgeException.class,
        () -> new GraphEdge(0, 0));

    assertEquals("Starting nodeId must be lower than ending nodeId",
        exception.getMessage());
  }

  @Test
  void assertValidNodeIdsOrdering_test04_startingNodeId_GreaterThan_endingNodeId_ExpectError() {
    Exception exception = assertThrows(IllegalNodeOrderingInEdgeException.class,
        () -> new GraphEdge(53, 52));

    assertEquals("Starting nodeId must be lower than ending nodeId",
        exception.getMessage());
  }

  @Test
  void checkRoadExists_NewEdge_ExpectFalse() {
    GraphEdge e1 = new GraphEdge(0, 1);
    assertFalse(e1.checkRoadExists());
  }
}
