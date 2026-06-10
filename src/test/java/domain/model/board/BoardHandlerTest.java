package domain.model.board;

import domain.model.exceptions.AdjacentNodeAlreadyClaimed;
import domain.model.exceptions.IllegalEdgeClaim;
import domain.model.player.Player;
import domain.model.player.PlayerColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoardHandlerTest {

    private BoardHandler boardHandler;
    private Player redPlayer;
    private Player bluePlayer;
    private Player orangePlayer;

    @BeforeEach
    void setUp() {
        boardHandler = new BoardHandler();
        redPlayer = new Player("Red", PlayerColor.RED);
        bluePlayer = new Player("Blue", PlayerColor.BLUE);
        orangePlayer = new Player("Orange", PlayerColor.ORANGE);
    }

    // --- buildSettlement: BVA node id boundary ---

    @Test
    void buildSettlement_nodeIdNegativeOne_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> boardHandler.buildSettlement(redPlayer, -1));
    }

    @Test
    void buildSettlement_nodeIdFiftyFour_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> boardHandler.buildSettlement(redPlayer, 54));
    }

    // --- buildSetupSettlement: node ownership ---

    @Test
    void buildSetupSettlement_nodeZero_claimsNodeForOrange() {
        boardHandler.buildSetupSettlement(orangePlayer, 0);
        assertTrue(boardHandler.checkPlayerOwnsNode(PlayerColor.ORANGE, 0));
    }

    @Test
    void buildSetupSettlement_nodeFiftyThree_claimsNodeForBlue() {
        boardHandler.buildSetupSettlement(bluePlayer, 53);
        assertTrue(boardHandler.checkPlayerOwnsNode(PlayerColor.BLUE, 53));
    }

    @Test
    void buildSetupSettlement_adjacentNodeAlreadyClaimed_throwsAdjacentNodeAlreadyClaimed() {
        // nodes 7 and 12 are adjacent via edge (7,12)
        boardHandler.buildSetupSettlement(redPlayer, 7);
        assertThrows(AdjacentNodeAlreadyClaimed.class,
                () -> boardHandler.buildSetupSettlement(bluePlayer, 12));
    }

    // --- addRoad: BVA node id boundary ---

    @Test
    void addRoad_startNodeIdNegativeOne_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> boardHandler.addRoad(redPlayer, -1, 0));
    }

    @Test
    void addRoad_endNodeIdFiftyFour_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> boardHandler.addRoad(redPlayer, 0, 54));
    }

    // --- addRoad: non-existent edge ---

    @Test
    void addRoad_sameStartAndEnd_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> boardHandler.addRoad(redPlayer, 5, 5));
    }

    @Test
    void addRoad_reversedEdgeOrder_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> boardHandler.addRoad(redPlayer, 3, 0));
    }

    // --- buildSetupRoad: road placement ---

    @Test
    void buildSetupRoad_adjacentToOwnedNode_doesNotThrow() {
        boardHandler.buildSetupSettlement(redPlayer, 0);
        assertDoesNotThrow(() -> boardHandler.buildSetupRoad(redPlayer, 0, 0, 1));
    }

    @Test
    void addRoad_alreadyClaimedEdge_throwsIllegalEdgeClaim() {
        boardHandler.buildSetupSettlement(redPlayer, 0);
        boardHandler.buildSetupRoad(redPlayer, 0, 0, 1);
        assertThrows(IllegalEdgeClaim.class,
                () -> boardHandler.addRoad(bluePlayer, 0, 1));
    }
}
