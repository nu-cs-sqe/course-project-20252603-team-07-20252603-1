package ui.controller;

import domain.model.GameModel;
import domain.model.game_pieces.DiceHandler;
import domain.model.player.Player;
import domain.model.player.PlayerColor;


import domain.model.player.TradeOffer;
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

    @Test
    void testOfferTradeDelegatesToModel() {
        TradeOffer mockOffer = createMock(TradeOffer.class);
        mockModel.offerTrade(mockOffer);
        expectLastCall();
        replay(mockModel, mockOffer);

        controller.offerTrade(mockModel, mockOffer);

        verify(mockModel, mockOffer);
    }

    @Test
    void testAcceptTradeDelegatesToModel() {
        TradeOffer mockOffer = createMock(TradeOffer.class);
        Player mockPlayer = createMock(Player.class);
        mockModel.acceptTrade(mockOffer, mockPlayer);
        expectLastCall();
        replay(mockModel, mockOffer, mockPlayer);

        controller.acceptTrade(mockModel, mockOffer, mockPlayer);

        verify(mockModel, mockOffer, mockPlayer);
    }

    @Test
    void testClearOffersDelegatesToModel() {
        mockModel.clearOffers();
        expectLastCall();
        replay(mockModel);

        controller.clearOffers(mockModel);

        verify(mockModel);
    }


}
