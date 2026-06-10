package ui.controller;

import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.model.DevelopmentCardHandler;
import domain.model.GameModel;
import domain.model.development_cards.DevelopmentCard;
import domain.model.development_cards.DevelopmentCardDeck;
import domain.model.development_cards.DevelopmentCardType;
import domain.model.exceptions.EmptyDeckException;
import domain.model.exceptions.InsufficientResourcesException;
import domain.model.game_pieces.Robber;
import domain.model.player.Player;

import static org.junit.jupiter.api.Assertions.*;

class DevCardControllerTest {

  private DevelopmentCardHandler mockHandler;
  private GameModel mockModel;
  private DevCardController controller;

  @BeforeEach
  void setUp() {
    mockHandler = EasyMock.createMock(DevelopmentCardHandler.class);
    mockModel = EasyMock.createMock(GameModel.class);
    controller = new DevCardController(mockHandler);
  }

  // TC1: buyDevelopmentCard(model, deck); handler returns a card
  //      -> controller returns the same DevelopmentCard; verify handler called with current player, deck, and round
  @Test
  void buyDevelopmentCard_HandlerReturnsCard_ExpectCardReturned() throws EmptyDeckException {
    final int currentRound = 2;

    Player mockPlayer = EasyMock.createMock(Player.class);
    DevelopmentCardDeck mockDeck = EasyMock.createMock(DevelopmentCardDeck.class);
    DevelopmentCard mockCard = EasyMock.createMock(DevelopmentCard.class);

    EasyMock.expect(mockModel.getCurrentPlayer()).andReturn(mockPlayer);
    EasyMock.expect(mockModel.getCurrentRound()).andReturn(currentRound);
    EasyMock.expect(mockHandler.buyDevelopmentCard(mockPlayer, mockDeck, currentRound)).andReturn(mockCard);

    EasyMock.replay(mockModel, mockHandler, mockPlayer, mockDeck, mockCard);

    DevelopmentCard result = controller.buyDevelopmentCard(mockModel, mockDeck);

    EasyMock.verify(mockModel, mockHandler, mockPlayer, mockDeck, mockCard);
    assertEquals(mockCard, result);
  }

  // TC2: buyDevelopmentCard(model, deck); handler throws InsufficientResourcesException
  //      -> controller relays InsufficientResourcesException to caller
  @Test
  void buyDevelopmentCard_HandlerThrowsInsufficientResources_ExpectExceptionRelayed() throws EmptyDeckException {
    final int currentRound = 2;

    Player mockPlayer = EasyMock.createMock(Player.class);
    DevelopmentCardDeck mockDeck = EasyMock.createMock(DevelopmentCardDeck.class);

    EasyMock.expect(mockModel.getCurrentPlayer()).andReturn(mockPlayer);
    EasyMock.expect(mockModel.getCurrentRound()).andReturn(currentRound);
    EasyMock.expect(mockHandler.buyDevelopmentCard(mockPlayer, mockDeck, currentRound))
        .andThrow(new InsufficientResourcesException("Not enough resources to buy a development card."));

    EasyMock.replay(mockModel, mockHandler, mockPlayer, mockDeck);

    Exception exception = assertThrows(InsufficientResourcesException.class,
        () -> controller.buyDevelopmentCard(mockModel, mockDeck));

    EasyMock.verify(mockModel, mockHandler, mockPlayer, mockDeck);
    assertEquals("Not enough resources to buy a development card.", exception.getMessage());
  }

  // TC3: buyDevelopmentCard(model, deck); handler throws EmptyDeckException (deck empty)
  //      -> controller relays EmptyDeckException to caller
  @Test
  void buyDevelopmentCard_HandlerThrowsEmptyDeckException_ExpectExceptionRelayed() throws EmptyDeckException {
    final int currentRound = 2;

    Player mockPlayer = EasyMock.createMock(Player.class);
    DevelopmentCardDeck mockDeck = EasyMock.createMock(DevelopmentCardDeck.class);

    EasyMock.expect(mockModel.getCurrentPlayer()).andReturn(mockPlayer);
    EasyMock.expect(mockModel.getCurrentRound()).andReturn(currentRound);
    EasyMock.expect(mockHandler.buyDevelopmentCard(mockPlayer, mockDeck, currentRound))
        .andThrow(new EmptyDeckException("Cannot draw new DevelopmentCard, no cards remain."));

    EasyMock.replay(mockModel, mockHandler, mockPlayer, mockDeck);

    Exception exception = assertThrows(EmptyDeckException.class,
        () -> controller.buyDevelopmentCard(mockModel, mockDeck));

    EasyMock.verify(mockModel, mockHandler, mockPlayer, mockDeck);
    assertEquals("Cannot draw new DevelopmentCard, no cards remain.", exception.getMessage());
  }

  // TC4: playKnightCard(model, card, robber, 5, victim); handler succeeds
  //      -> verify handler called with current player, card, currentRound, robber, 5, victim; no exception
  @Test
  void playKnightCard_HandlerSucceeds_ExpectDelegation() {
    final int currentRound = 2;
    final int targetHexId = 5;

    Player mockPlayer = EasyMock.createMock(Player.class);
    Player mockVictim = EasyMock.createMock(Player.class);
    DevelopmentCard mockCard = EasyMock.createMock(DevelopmentCard.class);
    Robber mockRobber = EasyMock.createMock(Robber.class);

    EasyMock.expect(mockModel.getCurrentPlayer()).andReturn(mockPlayer);
    EasyMock.expect(mockModel.getCurrentRound()).andReturn(currentRound);
    mockHandler.playKnightCard(mockPlayer, mockCard, currentRound, mockRobber, targetHexId, mockVictim);

    EasyMock.replay(mockModel, mockHandler, mockPlayer, mockVictim, mockCard, mockRobber);

    controller.playKnightCard(mockModel, mockCard, mockRobber, targetHexId, mockVictim);

    EasyMock.verify(mockModel, mockHandler, mockPlayer, mockVictim, mockCard, mockRobber);
  }

}
