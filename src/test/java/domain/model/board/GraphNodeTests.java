package domain.model.board;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import domain.model.exceptions.IllegalNodeIdException;
import domain.model.player.PlayerColor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

public class GraphNodeTests {

  @ParameterizedTest
  @EnumSource(value = PlayerColor.class, names = {"RED", "WHITE", "ORANGE", "BLUE"})
  void claimGraphNode_NodeUnoccupied_ExpectTrue(PlayerColor color) {
    GraphNode g1 = new GraphNode(0);
    assertTrue(g1.playerClaimNode(color));
    assertTrue(g1.checkOccupied());
    assertEquals(color, g1.checkColor());
  }

  @Test
  void claimGraphNodeOccupied_ExpectError() {
    GraphNode g1 = new GraphNode(0);
    g1.playerClaimNode(PlayerColor.BLUE);
    assertTrue(g1.checkOccupied());
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> g1.playerClaimNode(PlayerColor.ORANGE));
    assertEquals("Node Already Claimed", exception.getMessage());
    assertTrue(g1.checkOccupied());
    assertEquals(PlayerColor.BLUE, g1.checkColor());
  }

  @Test
  void assertValidNodeId_test01_ID0_ExpectTrue() {
    GraphNode g1 = new GraphNode(0);
    assertNotNull(g1);
  }

  @Test
  void assertValidNodeId_test02_ID53_ExpectTrue() {
    GraphNode g1 = new GraphNode(53);
    assertNotNull(g1);
  }

  @Test
  void assertValidNodeId_test03_IDNegative1_ExpectError() {
    Exception exception = assertThrows(IllegalNodeIdException.class,
        () -> new GraphNode(-1));
    assertEquals("Requested nodeId number illegal", exception.getMessage());
  }

  @Test
  void assertValidNodeId_test04_ID54_ExpectError() {
    Exception exception = assertThrows(IllegalNodeIdException.class,
        () -> new GraphNode(54));
    assertEquals("Requested nodeId number illegal", exception.getMessage());
  }
}
