package domain.model.player;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import domain.model.exceptions.InsufficientResourcesException;
import domain.model.resources.Resource;
import java.util.Map;
import org.junit.jupiter.api.Test;

// no easymock here — Player is the class under test; mocking it would make these tests meaningless

/** Test class. */
public class PlayerResourceTest {

  // behavior 1: new player has 0 of each normal resource
  @Test
  public void GetResourceCount_NewPlayer_AllFiveResourcesAreZero() {
    Player player = new Player("Dummy", PlayerColor.BLUE);
    assertEquals(0, player.getResourceCount(Resource.BRICK));
    assertEquals(0, player.getResourceCount(Resource.LUMBER));
    assertEquals(0, player.getResourceCount(Resource.WOOL));
    assertEquals(0, player.getResourceCount(Resource.GRAIN));
    assertEquals(0, player.getResourceCount(Resource.ORE));
  }

  // behavior 2: BVA min positive delta — adding 1
  @Test
  public void UpdateResources_AddOneBrick_GetResourceCountReturnsOne() {
    Player player = new Player("Dummy", PlayerColor.BLUE);
    player.updateResources(Resource.BRICK, 1);
    assertEquals(1, player.getResourceCount(Resource.BRICK));
  }

  // behavior 3: BVA bank max — adding 19
  @Test
  public void UpdateResources_AddNineteenWool_GetResourceCountReturnsNineteen() {
    Player player = new Player("Dummy", PlayerColor.BLUE);
    player.updateResources(Resource.WOOL, 19);
    assertEquals(19, player.getResourceCount(Resource.WOOL));
  }

  // behavior 4: two different resources are tracked independently
  @Test
  public void UpdateResources_AddBrickAndLumber_TrackedIndependently() {
    Player player = new Player("Dummy", PlayerColor.BLUE);
    player.updateResources(Resource.BRICK, 3);
    player.updateResources(Resource.LUMBER, 5);
    assertEquals(3, player.getResourceCount(Resource.BRICK));
    assertEquals(5, player.getResourceCount(Resource.LUMBER));
    assertEquals(0, player.getResourceCount(Resource.WOOL));
  }

  // behavior 5: adding to an existing count accumulates
  @Test
  public void UpdateResources_AddTwiceToSameResource_CountAccumulates() {
    Player player = new Player("Dummy", PlayerColor.BLUE);
    player.updateResources(Resource.ORE, 2);
    player.updateResources(Resource.ORE, 3);
    assertEquals(5, player.getResourceCount(Resource.ORE));
  }

  // behavior 6: BVA subtract to exactly zero
  @Test
  public void UpdateResources_SubtractOneFromOne_CountBecomesZero() {
    Player player = new Player("Dummy", PlayerColor.BLUE);
    player.updateResources(Resource.BRICK, 1);
    player.updateResources(Resource.BRICK, -1);
    assertEquals(0, player.getResourceCount(Resource.BRICK));
  }

  // behavior 7: BVA subtract below zero from empty hand
  @Test
  public void UpdateResources_SubtractOneFromZero_ThrowsInsufficientResources() {
    Player player = new Player("Dummy", PlayerColor.BLUE);
    Exception exception = assertThrows(InsufficientResourcesException.class, () ->
        player.updateResources(Resource.BRICK, -1)
    );
    assertEquals("Insufficient BRICK resources.", exception.getMessage());
  }

  // behavior 8: BVA delta exceeds current count — must throw and leave count unchanged
  @Test
  public void UpdateResources_SubtractTwoFromOne_ThrowsAndCountStaysOne() {
    Player player = new Player("Dummy", PlayerColor.BLUE);
    player.updateResources(Resource.BRICK, 1);
    Exception exception = assertThrows(InsufficientResourcesException.class, () ->
        player.updateResources(Resource.BRICK, -2)
    );
    assertEquals("Insufficient BRICK resources.", exception.getMessage());
    assertEquals(1, player.getResourceCount(Resource.BRICK));
  }

  // behavior 9: null resource → IllegalArgumentException
  @Test
  public void UpdateResources_NullResource_ThrowsIllegalArgument() {
    Player player = new Player("Dummy", PlayerColor.BLUE);
    Exception exception = assertThrows(IllegalArgumentException.class, () ->
        player.updateResources(null, 1)
    );
    assertEquals("Resource cannot be null.", exception.getMessage());
  }

  // behavior 10: DESERT resource → IllegalArgumentException
  @Test
  public void UpdateResources_DesertResource_ThrowsIllegalArgument() {
    Player player = new Player("Dummy", PlayerColor.BLUE);
    Exception exception = assertThrows(IllegalArgumentException.class, () ->
        player.updateResources(Resource.DESERT, 1)
    );
    assertEquals("Cannot update DESERT resources.", exception.getMessage());
  }

  // behavior 11: getResourceCount with null → IllegalArgumentException
  @Test
  public void GetResourceCount_NullResource_ThrowsIllegalArgument() {
    Player player = new Player("Dummy", PlayerColor.BLUE);
    Exception exception = assertThrows(IllegalArgumentException.class, () ->
        player.getResourceCount(null)
    );
    assertEquals("Resource cannot be null.", exception.getMessage());
  }

  // behavior 12: getResourceCount with DESERT → IllegalArgumentException
  @Test
  public void GetResourceCount_DesertResource_ThrowsIllegalArgument() {
    Player player = new Player("Dummy", PlayerColor.BLUE);
    Exception exception = assertThrows(IllegalArgumentException.class, () ->
        player.getResourceCount(Resource.DESERT)
    );
    assertEquals("Cannot get count of DESERT.", exception.getMessage());
  }

  // behavior 13: getTotalResourceCount on new player → 0
  @Test
  public void GetTotalResourceCount_NewPlayer_ReturnsZero() {
    Player player = new Player("Dummy", PlayerColor.BLUE);
    assertEquals(0, player.getTotalResourceCount());
  }

  // behavior 14: getTotalResourceCount sums all normal resources
  @Test
  public void GetTotalResourceCount_MultipleResources_SumsCorrectly() {
    Player player = new Player("Dummy", PlayerColor.BLUE);
    player.updateResources(Resource.BRICK, 2);
    player.updateResources(Resource.LUMBER, 3);
    player.updateResources(Resource.WOOL, 1);
    player.updateResources(Resource.GRAIN, 4);
    player.updateResources(Resource.ORE, 2);
    assertEquals(12, player.getTotalResourceCount());
  }

  // behavior 15: receiveResources and getResourceCount use the same underlying map
  @Test
  public void ReceiveResources_UpdatesSameMapAsGetResourceCount() {
    Player player = new Player("Dummy", PlayerColor.BLUE);
    player.receiveResources(Map.of(Resource.BRICK, 1, Resource.WOOL, 2));
    assertEquals(1, player.getResourceCount(Resource.BRICK));
    assertEquals(2, player.getResourceCount(Resource.WOOL));
  }

  // behavior 18: failed update leaves all resources unchanged (no partial mutation)
  @Test
  public void UpdateResources_FailedSubtract_LeavesAllResourcesUnchanged() {
    Player player = new Player("Dummy", PlayerColor.BLUE);
    player.updateResources(Resource.BRICK, 2);
    player.updateResources(Resource.WOOL, 3);
    Exception exception = assertThrows(InsufficientResourcesException.class, () ->
        player.updateResources(Resource.BRICK, -5)
    );
    assertEquals("Insufficient BRICK resources.", exception.getMessage());
    assertEquals(2, player.getResourceCount(Resource.BRICK));
    assertEquals(3, player.getResourceCount(Resource.WOOL));
  }
}
