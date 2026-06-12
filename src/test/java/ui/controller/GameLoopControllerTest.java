package ui.controller;

import domain.model.DevelopmentCardHandler;
import domain.model.GameModel;
import domain.model.development_cards.DevelopmentCard;
import domain.model.development_cards.DevelopmentCardDeck;
import domain.model.exceptions.EmptyDeckException;
import domain.model.game_pieces.DiceHandler;
import domain.model.player.Player;
import domain.model.player.PlayerColor;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;


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

    // TC5: handler returns a DevelopmentCard
    //      -> controller returns the same card; model.getCurrentPlayer() and model.getCurrentRound() called; handler called with player, deck, round
    @Test
    void buyDevCard_HandlerReturnsCard_ExpectCardRelayedToCaller() throws EmptyDeckException {
        DevelopmentCardDeck mockDeck = createMock(DevelopmentCardDeck.class);
        DevelopmentCardHandler mockHandler = createMock(DevelopmentCardHandler.class);
        DevelopmentCard mockCard = createMock(DevelopmentCard.class);
        Player mockPlayer = createMock(Player.class);
        final int currentRound = 1;

        expect(mockModel.getCurrentPlayer()).andReturn(mockPlayer);
        expect(mockModel.getCurrentRound()).andReturn(currentRound);
        expect(mockHandler.buyDevelopmentCard(mockPlayer, mockDeck, currentRound)).andReturn(mockCard);

        replay(mockModel, mockDeck, mockHandler, mockCard, mockPlayer);

        DevelopmentCard result = controller.buyDevCard(mockModel, mockDeck, mockHandler);
        assertSame(mockCard, result);

        verify(mockModel, mockDeck, mockHandler, mockCard, mockPlayer);
    }
}
