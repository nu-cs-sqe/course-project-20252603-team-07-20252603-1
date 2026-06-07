package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static domain.BoardHandler.initNodeHexMap;
import static org.junit.jupiter.api.Assertions.*;

public class BoardHandlerTests {

    private BoardGraphController mockBoardGraphController;
    private Player mockRedPlayer;
    private Player mockBluePlayer;
    private Player mockWhitePlayer;
    private Player mockOrangePlayer;
    private List<Hex> mockHexes;
    private Map<Integer, List<Integer>> nodeIdToHexes;

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
    }

    // Test Case 1
    @Test
    void RedClaimsNodeZero_AndSucceeds(){
        EasyMock.expect(mockRedPlayer.getPlayerColor()).andReturn(PlayerColor.RED);

        PlayerColor expectedColor = PlayerColor.RED;

        mockBoardGraphController.playerClaimStoredNode(expectedColor, 0);
        EasyMock.expectLastCall();

        mockHexes.get(0).addPlayerSettlementToHex(mockRedPlayer);
        EasyMock.expectLastCall();

        EasyMock.replay(mockBoardGraphController, mockRedPlayer, mockHexes.get(0));

        BoardHandler b = BoardHandler.createForTesting(mockBoardGraphController, mockHexes, nodeIdToHexes);

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
        EasyMock.expect(mockBluePlayer.getPlayerColor()).andReturn(PlayerColor.BLUE);

        PlayerColor expectedColor = PlayerColor.BLUE;

        mockBoardGraphController.playerClaimStoredNode(expectedColor, 53);
        EasyMock.expectLastCall();

        mockHexes.get(18).addPlayerSettlementToHex(mockBluePlayer);
        EasyMock.expectLastCall();

        EasyMock.replay(mockBoardGraphController, mockBluePlayer, mockHexes.get(18));

        BoardHandler b = BoardHandler.createForTesting(mockBoardGraphController, mockHexes, nodeIdToHexes);

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
        BoardHandler b = BoardHandler.createForTesting(mockBoardGraphController, mockHexes, nodeIdToHexes);

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
        BoardHandler b = BoardHandler.createForTesting(mockBoardGraphController, mockHexes, nodeIdToHexes);

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
        EasyMock.expect(mockOrangePlayer.getPlayerColor()).andReturn(PlayerColor.ORANGE);

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

        BoardHandler b = BoardHandler.createForTesting(mockBoardGraphController, mockHexes, nodeIdToHexes);

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
        EasyMock.expect(mockBluePlayer.getPlayerColor()).andReturn(PlayerColor.BLUE);

        PlayerColor expectedColor = PlayerColor.BLUE;

        mockBoardGraphController.playerClaimStoredNode(expectedColor, 4);
        EasyMock.expectLastCall();

        mockHexes.get(0).addPlayerSettlementToHex(mockBluePlayer);
        EasyMock.expectLastCall();
        mockHexes.get(1).addPlayerSettlementToHex(mockBluePlayer);
        EasyMock.expectLastCall();

        EasyMock.replay(mockBoardGraphController, mockBluePlayer, mockHexes.get(0), mockHexes.get(1));

        BoardHandler b = BoardHandler.createForTesting(mockBoardGraphController, mockHexes, nodeIdToHexes);

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
        EasyMock.expect(mockRedPlayer.getPlayerColor()).andReturn(PlayerColor.RED);

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

        BoardHandler b = BoardHandler.createForTesting(mockBoardGraphController, mockHexes, nodeIdToHexes);

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
        EasyMock.expect(mockBluePlayer.getPlayerColor()).andReturn(PlayerColor.BLUE);

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

        BoardHandler b = BoardHandler.createForTesting(mockBoardGraphController, mockHexes, nodeIdToHexes);

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
        BoardHandler b = BoardHandler.createForTesting(mockBoardGraphController, mockHexes, nodeIdToHexes);

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
        BoardHandler b = BoardHandler.createForTesting(mockBoardGraphController, mockHexes, nodeIdToHexes);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            b.buildCity(mockOrangePlayer, 54);
        });

        String expectedMessage = "Invalid NodeID - must be within [0, 53].";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }


}
