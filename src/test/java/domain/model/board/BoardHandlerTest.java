package domain.model.board;

import domain.model.game_pieces.Robber;
import domain.model.player.Player;
import domain.model.player.PlayerColor;
import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static domain.model.board.BoardHandler.initNodeHexMap;
import static org.junit.jupiter.api.Assertions.*;

public class BoardHandlerTest {

    private BoardGraphController mockBoardGraphController;
    private Player mockRedPlayer;
    private Player mockBluePlayer;
    private Player mockWhitePlayer;
    private Player mockOrangePlayer;
    private List<Hex> mockHexes;
    private Map<Integer, List<Integer>> nodeIdToHexes;
    private Robber mockRobber;

    @BeforeEach
    void setUp() {
        mockBoardGraphController = EasyMock.createMock(BoardGraphController.class);

        mockRedPlayer = EasyMock.createMock(Player.class);
        mockBluePlayer = EasyMock.createMock(Player.class);
        mockWhitePlayer = EasyMock.createMock(Player.class);
        mockOrangePlayer = EasyMock.createMock(Player.class);

        mockHexes = new ArrayList<>();
        for (int i = 0; i < 19; i++) {
            mockHexes.add(EasyMock.createMock(Hex.class));
        }

        nodeIdToHexes = initNodeHexMap();

        mockRobber = EasyMock.createMock(Robber.class);
    }

    // Test Case 1
    @Test
    void RedClaimsNodeZero_AndSucceeds(){
        EasyMock.expect(mockRedPlayer.getColor()).andReturn(PlayerColor.RED);

        PlayerColor expectedColor = PlayerColor.RED;

        mockBoardGraphController.playerClaimStoredNode(expectedColor, 0);
        EasyMock.expectLastCall();

        mockHexes.get(0).addPlayerSettlementToHex(mockRedPlayer);
        EasyMock.expectLastCall();

        EasyMock.replay(mockBoardGraphController, mockRedPlayer, mockHexes.get(0));

        BoardHandler b = BoardHandler.createForTesting(mockBoardGraphController, mockHexes, nodeIdToHexes, mockRobber);

        b.buildSettlement(mockRedPlayer, 0);

        EasyMock.verify(mockBoardGraphController, mockRedPlayer, mockHexes.get(0));

        assertTrue(b.checkPlayerOwnsNode(expectedColor, 0));

        int expected = 1;
        int actual = b.getNodeBuildingLevel(0);
        assertEquals(expected, actual);
    }

    // Test Case 2
    @Test
    void BlueClaimsNodeFiftyThree_AndSucceeds(){
        EasyMock.expect(mockBluePlayer.getColor()).andReturn(PlayerColor.BLUE);

        PlayerColor expectedColor = PlayerColor.BLUE;

        mockBoardGraphController.playerClaimStoredNode(expectedColor, 53);
        EasyMock.expectLastCall();

        mockHexes.get(18).addPlayerSettlementToHex(mockBluePlayer);
        EasyMock.expectLastCall();

        EasyMock.replay(mockBoardGraphController, mockBluePlayer, mockHexes.get(18));

        BoardHandler b = BoardHandler.createForTesting(mockBoardGraphController, mockHexes, nodeIdToHexes, mockRobber);

        b.buildSettlement(mockBluePlayer, 53);

        EasyMock.verify(mockBoardGraphController, mockBluePlayer, mockHexes.get(18));

        assertTrue(b.checkPlayerOwnsNode(expectedColor, 53));

        int expected = 1;
        int actual = b.getNodeBuildingLevel(53);
        assertEquals(expected, actual);
    }

    // Test Case 3
    @Test
    void OrangeClaimsNodeNegativeOne_ReturnsError(){
        BoardHandler b = BoardHandler.createForTesting(mockBoardGraphController, mockHexes, nodeIdToHexes, mockRobber);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            b.buildSettlement(mockOrangePlayer, -1);
        });

        String expectedMessage = "Invalid NodeID - must be within [0, 53].";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    // Test Case 4
    @Test
    void WhiteClaimsNodeFiftyFour_ReturnsError(){
        BoardHandler b = BoardHandler.createForTesting(mockBoardGraphController, mockHexes, nodeIdToHexes, mockRobber);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            b.buildSettlement(mockWhitePlayer, 54);
        });

        String expectedMessage = "Invalid NodeID - must be within [0, 53].";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    // Test Case 5
    @Test
    void OrangeClaimsNodeEight_HexesZeroOneFourUpdated(){
        EasyMock.expect(mockOrangePlayer.getColor()).andReturn(PlayerColor.ORANGE);

        PlayerColor expectedColor = PlayerColor.ORANGE;

        mockBoardGraphController.playerClaimStoredNode(expectedColor, 8);
        EasyMock.expectLastCall();

        mockHexes.get(0).addPlayerSettlementToHex(mockOrangePlayer);
        EasyMock.expectLastCall();
        mockHexes.get(1).addPlayerSettlementToHex(mockOrangePlayer);
        EasyMock.expectLastCall();
        mockHexes.get(4).addPlayerSettlementToHex(mockOrangePlayer);
        EasyMock.expectLastCall();

        EasyMock.replay(mockBoardGraphController, mockOrangePlayer, mockHexes.get(0), mockHexes.get(1), mockHexes.get(4));

        BoardHandler b = BoardHandler.createForTesting(mockBoardGraphController, mockHexes, nodeIdToHexes, mockRobber);

        b.buildSettlement(mockOrangePlayer, 8);

        EasyMock.verify(mockBoardGraphController, mockOrangePlayer, mockHexes.get(0), mockHexes.get(1), mockHexes.get(4));

        assertTrue(b.checkPlayerOwnsNode(expectedColor, 8));

        int expected = 1;
        int actual = b.getNodeBuildingLevel(8);
        assertEquals(expected, actual);
    }

    // Test Case 6
    @Test
    void BlueClaimsNodeFour_HexesZeroOneUpdated(){
        EasyMock.expect(mockBluePlayer.getColor()).andReturn(PlayerColor.BLUE);

        PlayerColor expectedColor = PlayerColor.BLUE;

        mockBoardGraphController.playerClaimStoredNode(expectedColor, 4);
        EasyMock.expectLastCall();

        mockHexes.get(0).addPlayerSettlementToHex(mockBluePlayer);
        EasyMock.expectLastCall();
        mockHexes.get(1).addPlayerSettlementToHex(mockBluePlayer);
        EasyMock.expectLastCall();

        EasyMock.replay(mockBoardGraphController, mockBluePlayer, mockHexes.get(0), mockHexes.get(1));

        BoardHandler b = BoardHandler.createForTesting(mockBoardGraphController, mockHexes, nodeIdToHexes, mockRobber);

        b.buildSettlement(mockBluePlayer, 4);

        EasyMock.verify(mockBoardGraphController, mockBluePlayer, mockHexes.get(0), mockHexes.get(1));

        assertTrue(b.checkPlayerOwnsNode(expectedColor, 4));

        int expected = 1;
        int actual = b.getNodeBuildingLevel(4);
        assertEquals(expected, actual);

    }

    // Test Case 7
    @Test
    void RedBuildsCityOnOwnedNodeZero_HexRemovesSettlement_AddsCity(){
        EasyMock.expect(mockRedPlayer.getColor()).andReturn(PlayerColor.RED).times(2);

        PlayerColor expectedColor = PlayerColor.RED;

        mockBoardGraphController.playerClaimStoredNode(expectedColor, 0);
        EasyMock.expectLastCall();

        mockHexes.get(0).addPlayerSettlementToHex(mockRedPlayer);
        EasyMock.expectLastCall();

        mockHexes.get(0).removePlayerSettlementFromHex(mockRedPlayer);
        EasyMock.expectLastCall();
        mockHexes.get(0).addPlayerCityToHex(mockRedPlayer);
        EasyMock.expectLastCall();

        EasyMock.replay(mockBoardGraphController, mockRedPlayer, mockHexes.get(0));

        BoardHandler b = BoardHandler.createForTesting(mockBoardGraphController, mockHexes, nodeIdToHexes, mockRobber);

        b.buildSettlement(mockRedPlayer, 0);

        b.buildCity(mockRedPlayer, 0);

        EasyMock.verify(mockBoardGraphController, mockRedPlayer, mockHexes.get(0));

        assertTrue(b.checkPlayerOwnsNode(expectedColor, 0));

        int expected = 2;
        int actual = b.getNodeBuildingLevel(0);
        assertEquals(expected, actual);
    }

    // Test Case 8
    @Test
    void BlueBuildsCityOnOwnedNodeFiftyThree_HexRemovesSettlement_AddsCity(){
        EasyMock.expect(mockBluePlayer.getColor()).andReturn(PlayerColor.BLUE).times(2);

        PlayerColor expectedColor = PlayerColor.BLUE;

        mockBoardGraphController.playerClaimStoredNode(expectedColor, 53);
        EasyMock.expectLastCall();

        mockHexes.get(18).addPlayerSettlementToHex(mockBluePlayer);
        EasyMock.expectLastCall();

        mockHexes.get(18).removePlayerSettlementFromHex(mockBluePlayer);
        EasyMock.expectLastCall();
        mockHexes.get(18).addPlayerCityToHex(mockBluePlayer);
        EasyMock.expectLastCall();

        EasyMock.replay(mockBoardGraphController, mockBluePlayer, mockHexes.get(18));

        BoardHandler b = BoardHandler.createForTesting(mockBoardGraphController, mockHexes, nodeIdToHexes, mockRobber);

        b.buildSettlement(mockBluePlayer, 53);

        b.buildCity(mockBluePlayer, 53);

        EasyMock.verify(mockBoardGraphController, mockBluePlayer, mockHexes.get(18));

        assertTrue(b.checkPlayerOwnsNode(expectedColor, 53));

        int expected = 2;
        int actual = b.getNodeBuildingLevel(53);
        assertEquals(expected, actual);
    }

    // Test Case 9
    @Test
    void OrangeBuildsCityOnNodeNegativeOne_ThrowsError(){
        BoardHandler b = BoardHandler.createForTesting(mockBoardGraphController, mockHexes, nodeIdToHexes, mockRobber);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            b.buildCity(mockOrangePlayer, -1);
        });

        String expectedMessage = "Invalid NodeID - must be within [0, 53].";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    // Test Case 10
    @Test
    void WhiteBuildsCityOnNodeFiftyFour_ThrowsError(){
        BoardHandler b = BoardHandler.createForTesting(mockBoardGraphController, mockHexes, nodeIdToHexes, mockRobber);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            b.buildCity(mockOrangePlayer, 54);
        });

        String expectedMessage = "Invalid NodeID - must be within [0, 53].";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    // Test Case 11
    @Test
    void RedBuildsCityOnNodeSix_BlueOwnsNodeSix_ThrowsError(){
        EasyMock.expect(mockBluePlayer.getColor()).andReturn(PlayerColor.BLUE);
        EasyMock.expect(mockRedPlayer.getColor()).andReturn(PlayerColor.RED);

        PlayerColor expectedColor = PlayerColor.BLUE;

        mockBoardGraphController.playerClaimStoredNode(expectedColor, 6);
        EasyMock.expectLastCall();

        mockHexes.get(2).addPlayerSettlementToHex(mockBluePlayer);
        EasyMock.expectLastCall();

        EasyMock.replay(mockBoardGraphController, mockBluePlayer, mockHexes.get(2));

        BoardHandler b = BoardHandler.createForTesting(mockBoardGraphController, mockHexes, nodeIdToHexes, mockRobber);

        b.buildSettlement(mockBluePlayer, 6);

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            b.buildCity(mockRedPlayer, 6);
        });

        EasyMock.verify(mockBoardGraphController, mockBluePlayer, mockHexes.get(2));

        String expectedMessage = "Node owned by other player, cannot build here.";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);

        assertTrue(b.checkPlayerOwnsNode(expectedColor, 6));
    }

    // Test Case 12
    @Test
    void BlueBuildsCity_OnUnclaimedNode_ThrowsError() {
        EasyMock.expect(mockBluePlayer.getColor()).andReturn(PlayerColor.BLUE);

        EasyMock.replay(mockBluePlayer);

        BoardHandler b = BoardHandler.createForTesting(mockBoardGraphController, mockHexes, nodeIdToHexes, mockRobber);

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            b.buildCity(mockBluePlayer, 36);
        });

        String expectedMessage = "Must upgrade a settlement to a city.";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);

        EasyMock.verify(mockBluePlayer);

        int expected = 0;
        int actual = b.getNodeBuildingLevel(36);
        assertEquals(expected, actual);
    }

    // Test Case 13
    @Test
    void OrangeBuildsCityOnOwnedNodeTwenty_HexRemovesSettlement_AddsCity_Twice(){
        EasyMock.expect(mockOrangePlayer.getColor()).andReturn(PlayerColor.ORANGE).times(2);

        PlayerColor expectedColor = PlayerColor.ORANGE;

        mockBoardGraphController.playerClaimStoredNode(expectedColor, 20);
        EasyMock.expectLastCall();

        mockHexes.get(6).addPlayerSettlementToHex(mockOrangePlayer);
        EasyMock.expectLastCall();

        mockHexes.get(6).removePlayerSettlementFromHex(mockOrangePlayer);
        EasyMock.expectLastCall();
        mockHexes.get(6).addPlayerCityToHex(mockOrangePlayer);
        EasyMock.expectLastCall();

        mockHexes.get(11).addPlayerSettlementToHex(mockOrangePlayer);
        EasyMock.expectLastCall();

        mockHexes.get(11).removePlayerSettlementFromHex(mockOrangePlayer);
        EasyMock.expectLastCall();
        mockHexes.get(11).addPlayerCityToHex(mockOrangePlayer);
        EasyMock.expectLastCall();

        EasyMock.replay(mockBoardGraphController, mockOrangePlayer, mockHexes.get(6), mockHexes.get(11));

        BoardHandler b = BoardHandler.createForTesting(mockBoardGraphController, mockHexes, nodeIdToHexes, mockRobber);

        b.buildSettlement(mockOrangePlayer, 20);

        b.buildCity(mockOrangePlayer, 20);

        EasyMock.verify(mockBoardGraphController, mockOrangePlayer, mockHexes.get(6), mockHexes.get(11));

        assertTrue(b.checkPlayerOwnsNode(expectedColor, 20));

        int expected = 2;
        int actual = b.getNodeBuildingLevel(20);
        assertEquals(expected, actual);
    }

    // Test Case 14
    @Test
    void WhiteBuildsCityOnOwnedNodeTwentyFour_HexRemovesSettlement_AddsCity_ThreeTimes(){
        EasyMock.expect(mockWhitePlayer.getColor()).andReturn(PlayerColor.WHITE).times(2);

        PlayerColor expectedColor = PlayerColor.WHITE;

        mockBoardGraphController.playerClaimStoredNode(expectedColor, 24);
        EasyMock.expectLastCall();

        mockHexes.get(5).addPlayerSettlementToHex(mockWhitePlayer);
        EasyMock.expectLastCall();

        mockHexes.get(5).removePlayerSettlementFromHex(mockWhitePlayer);
        EasyMock.expectLastCall();
        mockHexes.get(5).addPlayerCityToHex(mockWhitePlayer);
        EasyMock.expectLastCall();

        mockHexes.get(9).addPlayerSettlementToHex(mockWhitePlayer);
        EasyMock.expectLastCall();

        mockHexes.get(9).removePlayerSettlementFromHex(mockWhitePlayer);
        EasyMock.expectLastCall();
        mockHexes.get(9).addPlayerCityToHex(mockWhitePlayer);
        EasyMock.expectLastCall();

        mockHexes.get(10).addPlayerSettlementToHex(mockWhitePlayer);
        EasyMock.expectLastCall();

        mockHexes.get(10).removePlayerSettlementFromHex(mockWhitePlayer);
        EasyMock.expectLastCall();
        mockHexes.get(10).addPlayerCityToHex(mockWhitePlayer);
        EasyMock.expectLastCall();

        EasyMock.replay(mockBoardGraphController, mockWhitePlayer, mockHexes.get(5), mockHexes.get(9), mockHexes.get(10));

        BoardHandler b = BoardHandler.createForTesting(mockBoardGraphController, mockHexes, nodeIdToHexes, mockRobber);

        b.buildSettlement(mockWhitePlayer, 24);

        b.buildCity(mockWhitePlayer, 24);

        EasyMock.verify(mockBoardGraphController, mockWhitePlayer, mockHexes.get(5), mockHexes.get(9), mockHexes.get(10));

        assertTrue(b.checkPlayerOwnsNode(expectedColor, 24));

        int expected = 2;
        int actual = b.getNodeBuildingLevel(24);
        assertEquals(expected, actual);
    }

    // Test Case 15
    @Test
    void RedClaimsEdge_ZeroOne_CallPlayerClaimStoredEdge(){
        EasyMock.expect(mockRedPlayer.getColor()).andReturn(PlayerColor.RED);

        PlayerColor expectedColor = PlayerColor.RED;

        mockBoardGraphController.playerClaimStoredEdge(expectedColor, 0, 1);
        EasyMock.expectLastCall();

        EasyMock.replay(mockBoardGraphController, mockRedPlayer);

        BoardHandler b = BoardHandler.createForTesting(mockBoardGraphController, mockHexes, nodeIdToHexes, mockRobber);

        b.addRoad(mockRedPlayer, 0, 1);

        EasyMock.verify(mockBoardGraphController, mockRedPlayer);
    }

    // Test Case 16
    @Test
    void OrangeClaimsEdge_FiftyTwo_FiftyThree_CallPlayerClaimStoredEdge(){
        EasyMock.expect(mockOrangePlayer.getColor()).andReturn(PlayerColor.ORANGE);

        PlayerColor expectedColor = PlayerColor.ORANGE;

        mockBoardGraphController.playerClaimStoredEdge(expectedColor, 52, 53);
        EasyMock.expectLastCall();

        EasyMock.replay(mockBoardGraphController, mockOrangePlayer);

        BoardHandler b = BoardHandler.createForTesting(mockBoardGraphController, mockHexes, nodeIdToHexes, mockRobber);

        b.addRoad(mockOrangePlayer, 52, 53);

        EasyMock.verify(mockBoardGraphController, mockOrangePlayer);
    }

    // Test Case 17
    @Test
    void WhiteClaimsEdge_NegativeOne_Zero_ThrowError(){
        BoardHandler b = BoardHandler.createForTesting(mockBoardGraphController, mockHexes, nodeIdToHexes, mockRobber);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            b.addRoad(mockWhitePlayer, -1, 0);
        });

        String expectedMessage = "Edge nodeId out of bounds. Must be within [0, 53].";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    // Test Case 18
    @Test
    void WhiteClaimsEdge_Zero_NegativeOne_ThrowError(){
        BoardHandler b = BoardHandler.createForTesting(mockBoardGraphController, mockHexes, nodeIdToHexes, mockRobber);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            b.addRoad(mockWhitePlayer, 0, -1);
        });

        String expectedMessage = "Edge nodeId out of bounds. Must be within [0, 53].";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    // Test Case 19
    @Test
    void BlueClaimsEdge_FiftyThree_FiftyFour_ThrowError(){
        BoardHandler b = BoardHandler.createForTesting(mockBoardGraphController, mockHexes, nodeIdToHexes, mockRobber);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            b.addRoad(mockBluePlayer, 53, 54);
        });

        String expectedMessage = "Edge nodeId out of bounds. Must be within [0, 53].";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    // Test Case 20
    @Test
    void BlueClaimsEdge_FiftyFour_FiftyThree_ThrowError(){
        BoardHandler b = BoardHandler.createForTesting(mockBoardGraphController, mockHexes, nodeIdToHexes, mockRobber);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            b.addRoad(mockBluePlayer, 54, 53);
        });

        String expectedMessage = "Edge nodeId out of bounds. Must be within [0, 53].";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    // Test Case 21
    @Test
    void TwoRolled_RobberNotPresent_CallsAwardResources(){
        for (int i = 0; i < 19; i++) {
            if (i == 1) {
                EasyMock.expect(mockHexes.get(i).getHexId()).andReturn(1);
                EasyMock.expect(mockHexes.get(i).getHexRollNum()).andReturn(2);
            } else {
                EasyMock.expect(mockHexes.get(i).getHexId()).andReturn(0);
                EasyMock.expect(mockHexes.get(i).getHexRollNum()).andReturn(0);
            }
        }

        mockHexes.get(1).awardSettlementResources();
        EasyMock.expectLastCall();
        mockHexes.get(1).awardCityResources();
        EasyMock.expectLastCall();

        EasyMock.expect(mockRobber.getRobberLocation()).andReturn(9);

        EasyMock.replay(mockHexes.toArray());
        EasyMock.replay(mockRobber);

        BoardHandler b = BoardHandler.createForTesting(mockBoardGraphController, mockHexes, nodeIdToHexes, mockRobber);

        b.awardResources(2);

        EasyMock.verify(mockHexes.toArray());
        EasyMock.verify(mockRobber);
    }

    // Test Case 22
    @Test
    void TwelveRolled_RobberNotPresent_CallsAwardResources(){
        for (int i = 0; i < 19; i++) {
            if (i == 3) {
                EasyMock.expect(mockHexes.get(i).getHexId()).andReturn(3);
                EasyMock.expect(mockHexes.get(i).getHexRollNum()).andReturn(12);
            }
            else {
                EasyMock.expect(mockHexes.get(i).getHexId()).andReturn(0);
                EasyMock.expect(mockHexes.get(i).getHexRollNum()).andReturn(0);
            }
        }

        mockHexes.get(3).awardSettlementResources();
        EasyMock.expectLastCall();

        mockHexes.get(3).awardCityResources();
        EasyMock.expectLastCall();

        EasyMock.expect(mockRobber.getRobberLocation()).andReturn(9);

        EasyMock.replay(mockHexes.toArray());
        EasyMock.replay(mockRobber);

        BoardHandler b = BoardHandler.createForTesting(mockBoardGraphController, mockHexes, nodeIdToHexes, mockRobber);

        b.awardResources(12);

        EasyMock.verify(mockHexes.toArray());
        EasyMock.verify(mockRobber);
    }

    // Test Case 23
    @Test
    void EightRolled_RobberNotPresent_CallsAwardResourcesTwice(){
        for (int i = 0; i < 19; i++) {
            if (i == 11) {
                EasyMock.expect(mockHexes.get(i).getHexId()).andReturn(11);
                EasyMock.expect(mockHexes.get(i).getHexRollNum()).andReturn(8);
            }
            else if (i == 12) {
                EasyMock.expect(mockHexes.get(i).getHexId()).andReturn(12);
                EasyMock.expect(mockHexes.get(i).getHexRollNum()).andReturn(8);
            }
            else {
                EasyMock.expect(mockHexes.get(i).getHexId()).andReturn(0);
                EasyMock.expect(mockHexes.get(i).getHexRollNum()).andReturn(0);
            }
        }

        mockHexes.get(11).awardSettlementResources();
        EasyMock.expectLastCall();
        mockHexes.get(11).awardCityResources();
        EasyMock.expectLastCall();

        mockHexes.get(12).awardSettlementResources();
        EasyMock.expectLastCall();
        mockHexes.get(12).awardCityResources();
        EasyMock.expectLastCall();

        EasyMock.expect(mockRobber.getRobberLocation()).andReturn(9);

        EasyMock.replay(mockHexes.toArray());
        EasyMock.replay(mockRobber);

        BoardHandler b = BoardHandler.createForTesting(mockBoardGraphController, mockHexes, nodeIdToHexes, mockRobber);

        b.awardResources(8);

        EasyMock.verify(mockHexes.toArray());
        EasyMock.verify(mockRobber);
    }

    // Test Case 24
    @Test
    void TwoRolled_RobberIsPresent_NoCallsToAward(){
        for (int i = 0; i < 19; i++) {
            if (i == 1) {
                EasyMock.expect(mockHexes.get(i).getHexId()).andReturn(1);
                EasyMock.expect(mockHexes.get(i).getHexRollNum()).andReturn(2);
            }
            else {
                EasyMock.expect(mockHexes.get(i).getHexId()).andReturn(0);
                EasyMock.expect(mockHexes.get(i).getHexRollNum()).andReturn(0);
            }
        }

        EasyMock.expect(mockRobber.getRobberLocation()).andReturn(1);

        EasyMock.replay(mockHexes.toArray());
        EasyMock.replay(mockRobber);

        BoardHandler b = BoardHandler.createForTesting(mockBoardGraphController, mockHexes, nodeIdToHexes, mockRobber);

        b.awardResources(2);

        EasyMock.verify(mockHexes.toArray());
        EasyMock.verify(mockRobber);
    }

    // Test Case 25
    @Test
    void EightRolled_RobberIsPresent_CallsAwardResourcesOnce(){
        for (int i = 0; i < 19; i++) {
            if (i == 11) {
                EasyMock.expect(mockHexes.get(i).getHexId()).andReturn(11);
                EasyMock.expect(mockHexes.get(i).getHexRollNum()).andReturn(8);
            }
            else if (i == 12) {
                EasyMock.expect(mockHexes.get(i).getHexId()).andReturn(12);
                EasyMock.expect(mockHexes.get(i).getHexRollNum()).andReturn(8);
            }
            else {
                EasyMock.expect(mockHexes.get(i).getHexId()).andReturn(0);
                EasyMock.expect(mockHexes.get(i).getHexRollNum()).andReturn(0);
            }
        }

        mockHexes.get(11).awardSettlementResources();
        EasyMock.expectLastCall();
        mockHexes.get(11).awardCityResources();
        EasyMock.expectLastCall();

        EasyMock.expect(mockRobber.getRobberLocation()).andReturn(12);

        EasyMock.replay(mockHexes.toArray());
        EasyMock.replay(mockRobber);

        BoardHandler b = BoardHandler.createForTesting(mockBoardGraphController, mockHexes, nodeIdToHexes, mockRobber);

        b.awardResources(8);

        EasyMock.verify(mockHexes.toArray());
        EasyMock.verify(mockRobber);
    }

    // Test Case 26
    @Test
    void MoveRobberLocation_FromHexIdZero_Eighteen(){
        EasyMock.expect(mockRobber.getRobberLocation()).andReturn(0);

        mockRobber.moveRobber(18);
        EasyMock.expectLastCall();

        EasyMock.replay(mockRobber);

        BoardHandler b = BoardHandler.createForTesting(mockBoardGraphController, mockHexes, nodeIdToHexes, mockRobber);

        b.moveRobber(18);

        EasyMock.verify(mockRobber);
    }

    // Test Case 27
    @Test
    void MoveRobberLocation_FromHexIdEighteen_ToZero(){
        EasyMock.expect(mockRobber.getRobberLocation()).andReturn(18);

        mockRobber.moveRobber(0);
        EasyMock.expectLastCall();

        EasyMock.replay(mockRobber);

        BoardHandler b = BoardHandler.createForTesting(mockBoardGraphController, mockHexes, nodeIdToHexes, mockRobber);

        b.moveRobber(0);

        EasyMock.verify(mockRobber);
    }

    // Test Case 28
    @Test
    void MoveRobberLocation_ToNegativeOne_ThrowError(){
        BoardHandler b = BoardHandler.createForTesting(mockBoardGraphController, mockHexes, nodeIdToHexes, mockRobber);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            b.moveRobber(-1);
        });

        String expectedMessage = "Cannot move Robber to invalid Hex ID";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    // Test Case 29
    @Test
    void MoveRobberLocation_ToNineteen_ThrowError(){
        BoardHandler b = BoardHandler.createForTesting(mockBoardGraphController, mockHexes, nodeIdToHexes, mockRobber);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            b.moveRobber(19);
        });

        String expectedMessage = "Cannot move Robber to invalid Hex ID";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    // Test Case 30
    @Test
    void MoveRobberLocation_FromNine_ToNine_ThrowError(){
        EasyMock.expect(mockRobber.getRobberLocation()).andReturn(9);

        EasyMock.replay(mockRobber);

        BoardHandler b = BoardHandler.createForTesting(mockBoardGraphController, mockHexes, nodeIdToHexes, mockRobber);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            b.moveRobber(9);
        });

        String expectedMessage = "Must move robber to new location";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);

        EasyMock.verify(mockRobber);
    }

    // Test Case 31
    @Test
    void GetPlayersOnHexZero_JustBlueSettlement_ReturnBlue(){
        List<Player> settlementPlayers = List.of(mockBluePlayer);
        List<Player> cityPlayers = List.of();

        EasyMock.expect(mockHexes.get(0).getHexSettlementPlayers()).andReturn(settlementPlayers);
        EasyMock.expect(mockHexes.get(0).getHexCityPlayers()).andReturn(cityPlayers);

        EasyMock.replay(mockHexes.get(0));

        BoardHandler b = BoardHandler.createForTesting(mockBoardGraphController, mockHexes, nodeIdToHexes, mockRobber);

        Set<Player> result = b.getPlayersOnHex(0);

        EasyMock.verify(mockHexes.get(0));

        assertEquals(Set.of(mockBluePlayer), result);
    }

    // Test Case 32
    @Test
    void GetPlayersOnHexEighteen_JustRedCity_ReturnRed(){
        List<Player> settlementPlayers = List.of();
        List<Player> cityPlayers = List.of(mockRedPlayer);

        EasyMock.expect(mockHexes.get(18).getHexSettlementPlayers()).andReturn(settlementPlayers);
        EasyMock.expect(mockHexes.get(18).getHexCityPlayers()).andReturn(cityPlayers);

        EasyMock.replay(mockHexes.get(18));

        BoardHandler b = BoardHandler.createForTesting(mockBoardGraphController, mockHexes, nodeIdToHexes, mockRobber);

        Set<Player> result = b.getPlayersOnHex(18);

        EasyMock.verify(mockHexes.get(18));

        assertEquals(Set.of(mockRedPlayer), result);
    }

    // Test Case 33
    @Test
    void GetPlayersOnHexEighteen_WhiteOrangeSettlements_RedCity_ReturnWhiteOrangeRed(){
        List<Player> settlementPlayers = List.of(mockWhitePlayer, mockOrangePlayer);
        List<Player> cityPlayers = List.of(mockRedPlayer);

        EasyMock.expect(mockHexes.get(18).getHexSettlementPlayers()).andReturn(settlementPlayers);
        EasyMock.expect(mockHexes.get(18).getHexCityPlayers()).andReturn(cityPlayers);

        EasyMock.replay(mockHexes.get(18));

        BoardHandler b = BoardHandler.createForTesting(mockBoardGraphController, mockHexes, nodeIdToHexes, mockRobber);

        Set<Player> result = b.getPlayersOnHex(18);

        EasyMock.verify(mockHexes.get(18));

        assertEquals(Set.of(mockRedPlayer, mockWhitePlayer, mockOrangePlayer), result);
    }

    // Test Case 34
    @Test
    void GetPlayersOnHexEighteen_TwoWhiteSettlements_RedCity_ReturnWhiteRed_NoDuplicate(){
        List<Player> settlementPlayers = List.of(mockWhitePlayer, mockWhitePlayer);
        List<Player> cityPlayers = List.of(mockRedPlayer);

        EasyMock.expect(mockHexes.get(18).getHexSettlementPlayers()).andReturn(settlementPlayers);
        EasyMock.expect(mockHexes.get(18).getHexCityPlayers()).andReturn(cityPlayers);

        EasyMock.replay(mockHexes.get(18));

        BoardHandler b = BoardHandler.createForTesting(mockBoardGraphController, mockHexes, nodeIdToHexes, mockRobber);

        Set<Player> result = b.getPlayersOnHex(18);

        EasyMock.verify(mockHexes.get(18));

        assertEquals(Set.of(mockRedPlayer, mockWhitePlayer), result);
    }

    // Test Case 35
    @Test
    void GetPlayersOnHexEighteen_TwoBlueCities_ReturnBlue_NoDuplicate(){
        List<Player> settlementPlayers = List.of();
        List<Player> cityPlayers = List.of(mockBluePlayer, mockBluePlayer);

        EasyMock.expect(mockHexes.get(18).getHexSettlementPlayers()).andReturn(settlementPlayers);
        EasyMock.expect(mockHexes.get(18).getHexCityPlayers()).andReturn(cityPlayers);

        EasyMock.replay(mockHexes.get(18));

        BoardHandler b = BoardHandler.createForTesting(mockBoardGraphController, mockHexes, nodeIdToHexes, mockRobber);

        Set<Player> result = b.getPlayersOnHex(18);

        EasyMock.verify(mockHexes.get(18));

        assertEquals(Set.of(mockBluePlayer), result);
    }

    // Test Case 36
    @Test
    void GetPlayersOnHexEighteen_ThreeOrangeSettlements_ReturnOrange_NoDuplicate(){
        List<Player> settlementPlayers = List.of(mockOrangePlayer, mockOrangePlayer, mockOrangePlayer);
        List<Player> cityPlayers = List.of();

        EasyMock.expect(mockHexes.get(18).getHexSettlementPlayers()).andReturn(settlementPlayers);
        EasyMock.expect(mockHexes.get(18).getHexCityPlayers()).andReturn(cityPlayers);

        EasyMock.replay(mockHexes.get(18));

        BoardHandler b = BoardHandler.createForTesting(mockBoardGraphController, mockHexes, nodeIdToHexes, mockRobber);

        Set<Player> result = b.getPlayersOnHex(18);

        EasyMock.verify(mockHexes.get(18));

        assertEquals(Set.of(mockOrangePlayer), result);
    }

    // Test Case 37
    @Test
    void GetPlayersOnHexEighteen_ThreeRedCities_ReturnRed_NoDuplicate(){
        List<Player> settlementPlayers = List.of();
        List<Player> cityPlayers = List.of(mockRedPlayer, mockRedPlayer, mockRedPlayer);

        EasyMock.expect(mockHexes.get(18).getHexSettlementPlayers()).andReturn(settlementPlayers);
        EasyMock.expect(mockHexes.get(18).getHexCityPlayers()).andReturn(cityPlayers);

        EasyMock.replay(mockHexes.get(18));

        BoardHandler b = BoardHandler.createForTesting(mockBoardGraphController, mockHexes, nodeIdToHexes, mockRobber);

        Set<Player> result = b.getPlayersOnHex(18);

        EasyMock.verify(mockHexes.get(18));

        assertEquals(Set.of(mockRedPlayer), result);
    }

    // Test Case 38
    @Test
    void GetPlayersOnHexEighteen_NoBuildings_ReturnSetup(){
        List<Player> settlementPlayers = List.of();
        List<Player> cityPlayers = List.of();

        EasyMock.expect(mockHexes.get(18).getHexSettlementPlayers()).andReturn(settlementPlayers);
        EasyMock.expect(mockHexes.get(18).getHexCityPlayers()).andReturn(cityPlayers);

        EasyMock.replay(mockHexes.get(18));

        BoardHandler b = BoardHandler.createForTesting(mockBoardGraphController, mockHexes, nodeIdToHexes, mockRobber);

        Set<Player> result = b.getPlayersOnHex(18);

        EasyMock.verify(mockHexes.get(18));

        assertEquals(Set.of(), result);
    }

    // Test Case 39
    @Test
    void GetPlayersOnHexNegativeOne_ThrowError(){
        BoardHandler b = BoardHandler.createForTesting(mockBoardGraphController, mockHexes, nodeIdToHexes, mockRobber);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            b.getPlayersOnHex(-1);
        });

        String expectedMessage = "Invalid Hex ID, must be within [0,18]";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    // Test Case 40
    @Test
    void GetPlayersOnHexNineteen_ThrowError(){
        BoardHandler b = BoardHandler.createForTesting(mockBoardGraphController, mockHexes, nodeIdToHexes, mockRobber);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            b.getPlayersOnHex(19);
        });

        String expectedMessage = "Invalid Hex ID, must be within [0,18]";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}
