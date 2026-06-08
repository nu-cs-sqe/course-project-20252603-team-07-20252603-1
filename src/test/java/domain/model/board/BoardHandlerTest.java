package domain.model.board;

import domain.model.exceptions.IllegalRoadPlacementException;
import domain.model.exceptions.IllegalSettlementPlacementException;
import domain.model.player.PlayerColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoardHandlerTest {

    private BoardHandler boardHandler;

    @BeforeEach
    void setUp() {
        boardHandler = new BoardHandler();
    }

    // settlement ownership

    @Test
    void buildSettlement_unclaimedNode_claimsNodeForPlayer() {
        boardHandler.buildSettlement(PlayerColor.RED, 7);
        assertEquals(PlayerColor.RED, boardHandler.getNodeOwner(7));
    }

    @Test
    void buildSettlement_alreadyClaimedNode_throwsIllegalSettlementPlacement() {
        boardHandler.buildSettlement(PlayerColor.RED, 7);
        assertThrows(IllegalSettlementPlacementException.class,
                () -> boardHandler.buildSettlement(PlayerColor.BLUE, 7));
    }

    // BVA: settlement node id out of range

    @Test
    void buildSettlement_nodeIdNegativeOne_throws() {
        assertThrows(IllegalSettlementPlacementException.class,
                () -> boardHandler.buildSettlement(PlayerColor.RED, -1));
    }

    @Test
    void buildSettlement_nodeIdFiftyFour_throws() {
        assertThrows(IllegalSettlementPlacementException.class,
                () -> boardHandler.buildSettlement(PlayerColor.RED, 54));
    }

    // BVA: settlement node id at valid boundaries

    @Test
    void buildSettlement_nodeIdZero_claimsNode() {
        boardHandler.buildSettlement(PlayerColor.ORANGE, 0);
        assertEquals(PlayerColor.ORANGE, boardHandler.getNodeOwner(0));
    }

    @Test
    void buildSettlement_nodeIdFiftyThree_claimsNode() {
        boardHandler.buildSettlement(PlayerColor.BLUE, 53);
        assertEquals(PlayerColor.BLUE, boardHandler.getNodeOwner(53));
    }

    // road ownership

    @Test
    void addRoad_unclaimedEdge_claimsEdgeForPlayer() {
        boardHandler.addRoad(PlayerColor.RED, 0, 3);
        assertTrue(boardHandler.isRoadOwnedBy(PlayerColor.RED, 0, 3));
    }

    @Test
    void addRoad_alreadyClaimedEdge_throwsIllegalRoadPlacement() {
        boardHandler.addRoad(PlayerColor.RED, 0, 3);
        assertThrows(IllegalRoadPlacementException.class,
                () -> boardHandler.addRoad(PlayerColor.BLUE, 0, 3));
    }

    // BVA: edge with identical endpoints

    @Test
    void addRoad_sameStartAndEnd_throwsIllegalRoadPlacement() {
        assertThrows(IllegalRoadPlacementException.class,
                () -> boardHandler.addRoad(PlayerColor.RED, 5, 5));
    }

    // BVA: edge with reversed ordering (pins startNodeId < endNodeId convention)

    @Test
    void addRoad_reversedEdgeOrder_throwsIllegalRoadPlacement() {
        assertThrows(IllegalRoadPlacementException.class,
                () -> boardHandler.addRoad(PlayerColor.RED, 3, 0));
    }

    // BVA: road node id out of range

    @Test
    void addRoad_startNodeIdNegativeOne_throwsIllegalRoadPlacement() {
        assertThrows(IllegalRoadPlacementException.class,
                () -> boardHandler.addRoad(PlayerColor.RED, -1, 0));
    }

    @Test
    void addRoad_endNodeIdFiftyFour_throwsIllegalRoadPlacement() {
        assertThrows(IllegalRoadPlacementException.class,
                () -> boardHandler.addRoad(PlayerColor.RED, 0, 54));
    }
}
