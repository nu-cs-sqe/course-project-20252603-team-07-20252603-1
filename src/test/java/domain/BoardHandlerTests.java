package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static domain.BoardHandler.initNodeHexMap;

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
    }

}
