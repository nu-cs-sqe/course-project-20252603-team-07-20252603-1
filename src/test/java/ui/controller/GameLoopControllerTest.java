package ui.controller;

import domain.model.GameModel;
import domain.model.game_pieces.DiceHandler;
import domain.model.player.Player;
import domain.model.player.PlayerColor;
import domain.model.player.PlayerState;
import domain.model.resources.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;


class GameLoopControllerTest {

    private GameLoopController controller;
    private GameModel mockModel;

    @BeforeEach
    void setUp() {
        controller = new GameLoopController();
        mockModel = createMock(GameModel.class);
    }

    @Test
    void testGetCurrentPlayerDelegatesToModel() {
        Player expected = new Player("Alice", PlayerColor.RED);
        expect(mockModel.getCurrentPlayer()).andReturn(expected);
        replay(mockModel);

        assertSame(expected, controller.getCurrentPlayer(mockModel));

        verify(mockModel);
    }

    @Test
    void testGetCurrentPlayerIndexDelegatesToModel() {
        expect(mockModel.getCurrentPlayerIndex()).andReturn(2);
        replay(mockModel);

        assertEquals(2, controller.getCurrentPlayerIndex(mockModel));

        verify(mockModel);
    }

//    @Test
//    void testGetResourceCountDelegatesThroughPlayerState() {
//        PlayerState mockPlayerState = createMock(PlayerState.class);
//        expect(mockModel.getPlayerState(PlayerColor.RED)).andReturn(mockPlayerState);
//        expect(mockPlayerState.getResourceCount(Resource.GRAIN)).andReturn(3);
//        replay(mockModel, mockPlayerState);
//
//        assertEquals(3, controller.getResourceCount(mockModel, PlayerColor.RED, Resource.GRAIN));
//
//        verify(mockModel, mockPlayerState);
//    }

    @Test
    void testEndTurnDelegatesToModel() {
        mockModel.endTurn();
        expectLastCall();
        replay(mockModel);

        controller.endTurn(mockModel);

        verify(mockModel);
    }

    @Test
    void testRollDiceAndDistributeReturnsRollerValue() {
        DiceHandler mockRoller = createMock(DiceHandler.class);
        expect(mockRoller.rollTwoDice()).andReturn(8);
        mockModel.performTurn(8);
        expectLastCall();
        replay(mockRoller, mockModel);

        assertEquals(8, controller.rollDiceAndDistribute(mockModel, mockRoller));

        verify(mockRoller, mockModel);
    }
}
