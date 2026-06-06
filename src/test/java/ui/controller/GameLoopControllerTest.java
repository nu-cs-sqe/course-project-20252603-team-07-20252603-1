package ui.controller;

import domain.model.DiceRoller;
import domain.model.GameModel;
import domain.model.Player;
import domain.model.PlayerState;
import domain.model.resources.ResourceDeck;
import domain.model.resources.ResourceType;
import org.easymock.Capture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.newCapture;
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
        Player expected = new Player("Alice", "RED");
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

    @Test
    void testGetResourceCountDelegatesThroughPlayerState() {
        PlayerState mockPlayerState = createMock(PlayerState.class);
        expect(mockModel.getPlayerState(1)).andReturn(mockPlayerState);
        expect(mockPlayerState.getResourceCount(ResourceType.WHEAT)).andReturn(3);
        replay(mockModel, mockPlayerState);

        assertEquals(3, controller.getResourceCount(mockModel, 1, ResourceType.WHEAT));

        verify(mockModel, mockPlayerState);
    }

    @Test
    void testEndTurnDelegatesToModel() {
        mockModel.advanceToNextPlayer();
        expectLastCall();
        replay(mockModel);

        controller.endTurn(mockModel);

        verify(mockModel);
    }

    @Test
    void testRollDiceAndDistributeReturnsRollerValue() {
        DiceRoller mockRoller = createMock(DiceRoller.class);
        ResourceDeck mockDeck = createMock(ResourceDeck.class);
        expect(mockRoller.roll()).andReturn(8);
        mockModel.performTurn(anyObject(DiceRoller.class), eq(mockDeck));
        expectLastCall();
        replay(mockRoller, mockDeck, mockModel);

        assertEquals(8, controller.rollDiceAndDistribute(mockModel, mockRoller, mockDeck));

        verify(mockRoller, mockDeck, mockModel);
    }
}
