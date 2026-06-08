package domain.model.board;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import domain.model.exceptions.IllegalEdgeClaim;
import domain.model.exceptions.IllegalSettlementPlacementException;
import domain.model.game_pieces.Robber;
import domain.model.player.Player;
import domain.model.player.PlayerColor;
import domain.model.resources.Resource;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoardHandlerTest {

    private BoardHandler boardForSettlement(BoardGraph mockGraph, int nodeId) {
        List<Hex> hexes = new ArrayList<>(List.of(new Hex(0, Resource.ORE, 10)));
        return BoardHandler.createForTesting(
            new BoardGraphController(mockGraph),
            hexes,
            Map.of(nodeId, List.of(0)),
            new Robber(9));
    }

    private BoardHandler boardForRoad(BoardGraph mockGraph) {
        return BoardHandler.createForTesting(
            new BoardGraphController(mockGraph),
            new ArrayList<>(),
            Map.of(),
            new Robber(9));
    }

    private void expectSuccessfulSettlement(BoardGraph mockGraph, PlayerColor color, int nodeId) {
        EasyMock.expect(mockGraph.checkNodeOccupied(nodeId)).andReturn(false);
        EasyMock.expect(mockGraph.checkIfAdjacentNodesNotClaimed(nodeId)).andReturn(true);
        EasyMock.expect(mockGraph.nodeCheckPlayerOwnsNeighboringEdge(color, nodeId)).andReturn(true);
        EasyMock.expect(mockGraph.claimGraphNodeObject(color, nodeId)).andReturn(true);
    }

    private void expectSuccessfulRoad(BoardGraph mockGraph, PlayerColor color, int n1, int n2) {
        EasyMock.expect(mockGraph.checkEdgeOccupied(n1, n2)).andReturn(false);
        EasyMock.expect(mockGraph.edgeCheckPlayerOwnsNeighboringEdge(color, n1, n2)).andReturn(true);
        EasyMock.expect(mockGraph.claimGraphEdgeObject(color, n1, n2)).andReturn(true);
    }

    @Test
    void buildSettlement_nodeIdNegativeOne_throwsIllegalArgument() {
        Player player = EasyMock.createMock(Player.class);
        EasyMock.replay(player);

        BoardHandler board = new BoardHandler();
        assertThrows(IllegalArgumentException.class, () -> board.buildSettlement(player, -1));

        EasyMock.verify(player);
    }

    @Test
    void buildSettlement_nodeIdFiftyFour_throwsIllegalArgument() {
        Player player = EasyMock.createMock(Player.class);
        EasyMock.replay(player);

        BoardHandler board = new BoardHandler();
        assertThrows(IllegalArgumentException.class, () -> board.buildSettlement(player, 54));

        EasyMock.verify(player);
    }

    @Test
    void buildSettlement_nodeIdZero_passesRangeCheck() {
        BoardGraph mockGraph = EasyMock.createMock(BoardGraph.class);
        Player player = EasyMock.createMock(Player.class);
        EasyMock.expect(player.getColor()).andReturn(PlayerColor.RED);
        expectSuccessfulSettlement(mockGraph, PlayerColor.RED, 0);
        EasyMock.replay(mockGraph, player);

        boardForSettlement(mockGraph, 0).buildSettlement(player, 0);

        EasyMock.verify(mockGraph, player);
    }

    @Test
    void buildSettlement_nodeIdFiftyThree_passesRangeCheck() {
        BoardGraph mockGraph = EasyMock.createMock(BoardGraph.class);
        Player player = EasyMock.createMock(Player.class);
        EasyMock.expect(player.getColor()).andReturn(PlayerColor.BLUE);
        expectSuccessfulSettlement(mockGraph, PlayerColor.BLUE, 53);
        EasyMock.replay(mockGraph, player);

        boardForSettlement(mockGraph, 53).buildSettlement(player, 53);

        EasyMock.verify(mockGraph, player);
    }

    @Test
    void buildSettlement_nodeIdOne_passesRangeCheck() {
        BoardGraph mockGraph = EasyMock.createMock(BoardGraph.class);
        Player player = EasyMock.createMock(Player.class);
        EasyMock.expect(player.getColor()).andReturn(PlayerColor.RED);
        expectSuccessfulSettlement(mockGraph, PlayerColor.RED, 1);
        EasyMock.replay(mockGraph, player);

        boardForSettlement(mockGraph, 1).buildSettlement(player, 1);

        EasyMock.verify(mockGraph, player);
    }

    @Test
    void buildSettlement_nodeIdFiftyTwo_passesRangeCheck() {
        BoardGraph mockGraph = EasyMock.createMock(BoardGraph.class);
        Player player = EasyMock.createMock(Player.class);
        EasyMock.expect(player.getColor()).andReturn(PlayerColor.ORANGE);
        expectSuccessfulSettlement(mockGraph, PlayerColor.ORANGE, 52);
        EasyMock.replay(mockGraph, player);

        boardForSettlement(mockGraph, 52).buildSettlement(player, 52);

        EasyMock.verify(mockGraph, player);
    }

    @Test
    void buildSettlement_unclaimedNode_callsController() {
        BoardGraph mockGraph = EasyMock.createMock(BoardGraph.class);
        Player player = EasyMock.createMock(Player.class);
        EasyMock.expect(player.getColor()).andReturn(PlayerColor.RED);
        expectSuccessfulSettlement(mockGraph, PlayerColor.RED, 7);
        EasyMock.replay(mockGraph, player);

        boardForSettlement(mockGraph, 7).buildSettlement(player, 7);

        EasyMock.verify(mockGraph, player);
    }

    @Test
    void buildSettlement_alreadyClaimedNode_throwsIllegalSettlementPlacement() {
        BoardGraph mockGraph = EasyMock.createMock(BoardGraph.class);
        Player player = EasyMock.createMock(Player.class);
        EasyMock.expect(player.getColor()).andReturn(PlayerColor.RED);
        EasyMock.expect(mockGraph.checkNodeOccupied(7)).andReturn(true);
        EasyMock.replay(mockGraph, player);

        BoardHandler board = boardForSettlement(mockGraph, 7);
        assertThrows(IllegalSettlementPlacementException.class, () -> board.buildSettlement(player, 7));

        EasyMock.verify(mockGraph, player);
    }

    @Test
    void addRoad_startNodeIdNegativeOne_throwsIllegalArgument() {
        Player player = EasyMock.createMock(Player.class);
        EasyMock.replay(player);

        BoardHandler board = new BoardHandler();
        assertThrows(IllegalArgumentException.class, () -> board.addRoad(player, -1, 0));

        EasyMock.verify(player);
    }

    @Test
    void addRoad_endNodeIdFiftyFour_throwsIllegalArgument() {
        Player player = EasyMock.createMock(Player.class);
        EasyMock.replay(player);

        BoardHandler board = new BoardHandler();
        assertThrows(IllegalArgumentException.class, () -> board.addRoad(player, 0, 54));

        EasyMock.verify(player);
    }

    @Test
    void addRoad_startNodeIdFiftyFour_throwsIllegalArgument() {
        Player player = EasyMock.createMock(Player.class);
        EasyMock.replay(player);

        BoardHandler board = new BoardHandler();
        assertThrows(IllegalArgumentException.class, () -> board.addRoad(player, 54, 0));

        EasyMock.verify(player);
    }

    @Test
    void addRoad_endNodeIdNegativeOne_throwsIllegalArgument() {
        Player player = EasyMock.createMock(Player.class);
        EasyMock.replay(player);

        BoardHandler board = new BoardHandler();
        assertThrows(IllegalArgumentException.class, () -> board.addRoad(player, 0, -1));

        EasyMock.verify(player);
    }

    @Test
    void addRoad_validEdge_callsController() {
        BoardGraph mockGraph = EasyMock.createMock(BoardGraph.class);
        Player player = EasyMock.createMock(Player.class);
        EasyMock.expect(player.getColor()).andReturn(PlayerColor.RED);
        expectSuccessfulRoad(mockGraph, PlayerColor.RED, 0, 3);
        EasyMock.replay(mockGraph, player);

        boardForRoad(mockGraph).addRoad(player, 0, 3);

        EasyMock.verify(mockGraph, player);
    }

    @Test
    void addRoad_alreadyClaimedEdge_throwsIllegalEdgeClaim() {
        BoardGraph mockGraph = EasyMock.createMock(BoardGraph.class);
        Player player = EasyMock.createMock(Player.class);
        EasyMock.expect(player.getColor()).andReturn(PlayerColor.RED);
        EasyMock.expect(mockGraph.checkEdgeOccupied(0, 3)).andReturn(true);
        EasyMock.replay(mockGraph, player);

        BoardHandler board = boardForRoad(mockGraph);
        assertThrows(IllegalEdgeClaim.class, () -> board.addRoad(player, 0, 3));

        EasyMock.verify(mockGraph, player);
    }
}
