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
import domain.model.resources.Resource;

import java.util.List;

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

  // TC5: playKnightCard(model, card, null, 5, victim); handler throws IllegalArgumentException
  //      -> controller relays IllegalArgumentException: "Robber cannot be null."
  @Test
  void playKnightCard_RobberIsNull_ExpectExceptionRelayed() {
    final int currentRound = 2;
    final int targetHexId = 5;

    Player mockPlayer = EasyMock.createMock(Player.class);
    Player mockVictim = EasyMock.createMock(Player.class);
    DevelopmentCard mockCard = EasyMock.createMock(DevelopmentCard.class);

    EasyMock.expect(mockModel.getCurrentPlayer()).andReturn(mockPlayer);
    EasyMock.expect(mockModel.getCurrentRound()).andReturn(currentRound);
    mockHandler.playKnightCard(mockPlayer, mockCard, currentRound, null, targetHexId, mockVictim);
    EasyMock.expectLastCall().andThrow(new IllegalArgumentException("Robber cannot be null."));

    EasyMock.replay(mockModel, mockHandler, mockPlayer, mockVictim, mockCard);

    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> controller.playKnightCard(mockModel, mockCard, null, targetHexId, mockVictim));

    EasyMock.verify(mockModel, mockHandler, mockPlayer, mockVictim, mockCard);
    assertEquals("Robber cannot be null.", exception.getMessage());
  }

  // TC6: playKnightCard(model, card, robber, 5, null); handler succeeds (no victim)
  //      -> verify handler called; no exception
  @Test
  void playKnightCard_VictimIsNull_ExpectDelegation() {
    final int currentRound = 2;
    final int targetHexId = 5;

    Player mockPlayer = EasyMock.createMock(Player.class);
    DevelopmentCard mockCard = EasyMock.createMock(DevelopmentCard.class);
    Robber mockRobber = EasyMock.createMock(Robber.class);

    EasyMock.expect(mockModel.getCurrentPlayer()).andReturn(mockPlayer);
    EasyMock.expect(mockModel.getCurrentRound()).andReturn(currentRound);
    mockHandler.playKnightCard(mockPlayer, mockCard, currentRound, mockRobber, targetHexId, null);

    EasyMock.replay(mockModel, mockHandler, mockPlayer, mockCard, mockRobber);

    controller.playKnightCard(mockModel, mockCard, mockRobber, targetHexId, null);

    EasyMock.verify(mockModel, mockHandler, mockPlayer, mockCard, mockRobber);
  }

  // TC7: playKnightCard(model, null, robber, 5, victim); handler throws IllegalArgumentException
  //      -> controller relays IllegalArgumentException: "Development card cannot be null."
  @Test
  void playKnightCard_CardIsNull_ExpectExceptionRelayed() {
    final int currentRound = 2;
    final int targetHexId = 5;

    Player mockPlayer = EasyMock.createMock(Player.class);
    Player mockVictim = EasyMock.createMock(Player.class);
    Robber mockRobber = EasyMock.createMock(Robber.class);

    EasyMock.expect(mockModel.getCurrentPlayer()).andReturn(mockPlayer);
    EasyMock.expect(mockModel.getCurrentRound()).andReturn(currentRound);
    mockHandler.playKnightCard(mockPlayer, null, currentRound, mockRobber, targetHexId, mockVictim);
    EasyMock.expectLastCall().andThrow(new IllegalArgumentException("Development card cannot be null."));

    EasyMock.replay(mockModel, mockHandler, mockPlayer, mockVictim, mockRobber);

    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> controller.playKnightCard(mockModel, null, mockRobber, targetHexId, mockVictim));

    EasyMock.verify(mockModel, mockHandler, mockPlayer, mockVictim, mockRobber);
    assertEquals("Development card cannot be null.", exception.getMessage());
  }

  // TC8: playMonopolyCard(model, card, BRICK); handler succeeds
  //      -> verify handler called with current player, card, currentRound, BRICK, and other players; no exception
  @Test
  void playMonopolyCard_HandlerSucceeds_ExpectDelegation() {
    final int currentRound = 2;

    Player mockPlayer = EasyMock.createMock(Player.class);
    Player mockOther = EasyMock.createMock(Player.class);
    DevelopmentCard mockCard = EasyMock.createMock(DevelopmentCard.class);
    List<Player> otherPlayers = List.of(mockOther);

    EasyMock.expect(mockModel.getCurrentPlayer()).andReturn(mockPlayer);
    EasyMock.expect(mockModel.getCurrentRound()).andReturn(currentRound);
    EasyMock.expect(mockModel.getOtherPlayers()).andReturn(otherPlayers);
    mockHandler.playMonopolyCard(mockPlayer, mockCard, currentRound, Resource.BRICK, otherPlayers);

    EasyMock.replay(mockModel, mockHandler, mockPlayer, mockOther, mockCard);

    controller.playMonopolyCard(mockModel, mockCard, Resource.BRICK);

    EasyMock.verify(mockModel, mockHandler, mockPlayer, mockOther, mockCard);
  }

}
