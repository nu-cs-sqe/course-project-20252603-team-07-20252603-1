package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class BoardHandlerTests {

    private BoardGraphController mockBoardGraphController;
    private Player mockPlayer;
    private List<Hex> mockHexes;

    @BeforeEach
    void setUp() {
        mockBoardGraphController = EasyMock.createMock(BoardGraphController.class);
        mockPlayer = EasyMock.createMock(Player.class);

        mockHexes = new ArrayList<>();
        for (int i = 0; i < 19; i++) {
            mockHexes.add(EasyMock.createMock(Hex.class));
        }
    }

    // Test Case 1
    @Test
    void RedClaimsNodeZero_AndSucceeds(){
        EasyMock.expect(mockPlayer.getPlayerColor()).andReturn(PlayerColor.RED);

        PlayerColor expectedColor = PlayerColor.RED;

        mockBoardGraphController.playerClaimStoredNode(expectedColor, 0);
        EasyMock.expectLastCall();

        mockHexes.get(0).addPlayerSettlementToHex(mockPlayer);
        EasyMock.expectLastCall();

        EasyMock.replay(mockBoardGraphController, mockPlayer, mockHexes.get(0));

        BoardHandler b = BoardHandler.createForTesting(mockBoardGraphController, mockHexes);

        b.buildSettlement(mockPlayer, 0);

        EasyMock.verify(mockBoardGraphController, mockPlayer, mockHexes.get(0));
    }

}
