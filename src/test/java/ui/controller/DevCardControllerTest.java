package ui.controller;

import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.model.DevelopmentCardHandler;
import domain.model.GameModel;
import domain.model.developmentcards.DevelopmentCard;
import domain.model.developmentcards.DevelopmentCardDeck;
import domain.model.developmentcards.DevelopmentCardType;
import domain.model.exceptions.EmptyDeckException;
import domain.model.exceptions.InsufficientResourcesException;
import domain.model.gamepieces.Robber;
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

  // TC9: playMonopolyCard(model, card, null); handler throws IllegalArgumentException
  //      -> controller relays IllegalArgumentException: "Resource cannot be null."
  @Test
  void playMonopolyCard_ResourceIsNull_ExpectExceptionRelayed() {
    final int currentRound = 2;

    Player mockPlayer = EasyMock.createMock(Player.class);
    Player mockOther = EasyMock.createMock(Player.class);
    DevelopmentCard mockCard = EasyMock.createMock(DevelopmentCard.class);
    List<Player> otherPlayers = List.of(mockOther);

    EasyMock.expect(mockModel.getCurrentPlayer()).andReturn(mockPlayer);
    EasyMock.expect(mockModel.getCurrentRound()).andReturn(currentRound);
    EasyMock.expect(mockModel.getOtherPlayers()).andReturn(otherPlayers);
    mockHandler.playMonopolyCard(mockPlayer, mockCard, currentRound, null, otherPlayers);
    EasyMock.expectLastCall().andThrow(new IllegalArgumentException("Resource cannot be null."));

    EasyMock.replay(mockModel, mockHandler, mockPlayer, mockOther, mockCard);

    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> controller.playMonopolyCard(mockModel, mockCard, null));

    EasyMock.verify(mockModel, mockHandler, mockPlayer, mockOther, mockCard);
    assertEquals("Resource cannot be null.", exception.getMessage());
  }

  // TC10: playMonopolyCard(model, null, BRICK); handler throws IllegalArgumentException
  //       -> controller relays IllegalArgumentException: "Development card cannot be null."
  @Test
  void playMonopolyCard_CardIsNull_ExpectExceptionRelayed() {
    final int currentRound = 2;

    Player mockPlayer = EasyMock.createMock(Player.class);
    Player mockOther = EasyMock.createMock(Player.class);
    List<Player> otherPlayers = List.of(mockOther);

    EasyMock.expect(mockModel.getCurrentPlayer()).andReturn(mockPlayer);
    EasyMock.expect(mockModel.getCurrentRound()).andReturn(currentRound);
    EasyMock.expect(mockModel.getOtherPlayers()).andReturn(otherPlayers);
    mockHandler.playMonopolyCard(mockPlayer, null, currentRound, Resource.BRICK, otherPlayers);
    EasyMock.expectLastCall().andThrow(new IllegalArgumentException("Development card cannot be null."));

    EasyMock.replay(mockModel, mockHandler, mockPlayer, mockOther);

    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> controller.playMonopolyCard(mockModel, null, Resource.BRICK));

    EasyMock.verify(mockModel, mockHandler, mockPlayer, mockOther);
    assertEquals("Development card cannot be null.", exception.getMessage());
  }

  // TC11: playRoadBuildingCard(model, card, 0, 1, 1, 2); handler succeeds
  //       -> verify handler called with current player, card, currentRound, boardHandler, 0, 1, 1, 2; no exception
  @Test
  void playRoadBuildingCard_HandlerSucceeds_ExpectDelegation() {
    final int currentRound = 2;

    Player mockPlayer = EasyMock.createMock(Player.class);
    DevelopmentCard mockCard = EasyMock.createMock(DevelopmentCard.class);

    EasyMock.expect(mockModel.getCurrentPlayer()).andReturn(mockPlayer);
    EasyMock.expect(mockModel.getCurrentRound()).andReturn(currentRound);
    mockHandler.playRoadBuildingCard(mockPlayer, mockCard, currentRound, mockModel, 0, 1, 1, 2);

    EasyMock.replay(mockModel, mockHandler, mockPlayer, mockCard);

    controller.playRoadBuildingCard(mockModel, mockCard, 0, 1, 1, 2);

    EasyMock.verify(mockModel, mockHandler, mockPlayer, mockCard);
  }

  // TC12: playRoadBuildingCard(model, card, 0, 1, 1, 2); handler throws IllegalArgumentException
  //       -> controller relays IllegalArgumentException: "Edge nodeId out of bounds."
  @Test
  void playRoadBuildingCard_HandlerThrowsIllegalArgument_ExpectExceptionRelayed() {
    final int currentRound = 2;

    Player mockPlayer = EasyMock.createMock(Player.class);
    DevelopmentCard mockCard = EasyMock.createMock(DevelopmentCard.class);

    EasyMock.expect(mockModel.getCurrentPlayer()).andReturn(mockPlayer);
    EasyMock.expect(mockModel.getCurrentRound()).andReturn(currentRound);
    mockHandler.playRoadBuildingCard(mockPlayer, mockCard, currentRound, mockModel, -1, 1, 1, 2);
    EasyMock.expectLastCall().andThrow(new IllegalArgumentException("Edge nodeId out of bounds. Must be within [0, 53]."));

    EasyMock.replay(mockModel, mockHandler, mockPlayer, mockCard);

    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> controller.playRoadBuildingCard(mockModel, mockCard, -1, 1, 1, 2));

    EasyMock.verify(mockModel, mockHandler, mockPlayer, mockCard);
    assertEquals("Edge nodeId out of bounds. Must be within [0, 53].", exception.getMessage());
  }

  // TC13: playRoadBuildingCard(model, card, 0, 1, null, null); handler succeeds (1 road remaining)
  //       -> verify handler called with null road2 node IDs; 1 road placed
  @Test
  void playRoadBuildingCard_Road2Null_ExpectDelegation() {
    final int currentRound = 2;

    Player mockPlayer = EasyMock.createMock(Player.class);
    DevelopmentCard mockCard = EasyMock.createMock(DevelopmentCard.class);

    EasyMock.expect(mockModel.getCurrentPlayer()).andReturn(mockPlayer);
    EasyMock.expect(mockModel.getCurrentRound()).andReturn(currentRound);
    mockHandler.playRoadBuildingCard(mockPlayer, mockCard, currentRound, mockModel, 0, 1, null, null);

    EasyMock.replay(mockModel, mockHandler, mockPlayer, mockCard);

    controller.playRoadBuildingCard(mockModel, mockCard, 0, 1, null, null);

    EasyMock.verify(mockModel, mockHandler, mockPlayer, mockCard);
  }

  // TC14: playRoadBuildingCard(model, null, 0, 1, 1, 2); handler throws IllegalArgumentException
  //       -> controller relays IllegalArgumentException: "Development card cannot be null."
  @Test
  void playRoadBuildingCard_CardIsNull_ExpectExceptionRelayed() {
    final int currentRound = 2;

    Player mockPlayer = EasyMock.createMock(Player.class);

    EasyMock.expect(mockModel.getCurrentPlayer()).andReturn(mockPlayer);
    EasyMock.expect(mockModel.getCurrentRound()).andReturn(currentRound);
    mockHandler.playRoadBuildingCard(mockPlayer, null, currentRound, mockModel, 0, 1, 1, 2);
    EasyMock.expectLastCall().andThrow(new IllegalArgumentException("Development card cannot be null."));

    EasyMock.replay(mockModel, mockHandler, mockPlayer);

    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> controller.playRoadBuildingCard(mockModel, null, 0, 1, 1, 2));

    EasyMock.verify(mockModel, mockHandler, mockPlayer);
    assertEquals("Development card cannot be null.", exception.getMessage());
  }

  // TC15: playYearOfPlentyCard(model, card, ORE, ORE); handler succeeds
  //       -> verify handler called with current player, card, currentRound, ORE, ORE; no exception
  @Test
  void playYearOfPlentyCard_HandlerSucceeds_ExpectDelegation() {
    final int currentRound = 2;

    Player mockPlayer = EasyMock.createMock(Player.class);
    DevelopmentCard mockCard = EasyMock.createMock(DevelopmentCard.class);

    EasyMock.expect(mockModel.getCurrentPlayer()).andReturn(mockPlayer);
    EasyMock.expect(mockModel.getCurrentRound()).andReturn(currentRound);
    mockHandler.playYearOfPlentyCard(mockPlayer, mockCard, currentRound, Resource.ORE, Resource.ORE);

    EasyMock.replay(mockModel, mockHandler, mockPlayer, mockCard);

    controller.playYearOfPlentyCard(mockModel, mockCard, Resource.ORE, Resource.ORE);

    EasyMock.verify(mockModel, mockHandler, mockPlayer, mockCard);
  }

  // TC16: playYearOfPlentyCard(model, card, null, BRICK); handler throws IllegalArgumentException
  //       -> controller relays IllegalArgumentException: "Resource cannot be null."
  @Test
  void playYearOfPlentyCard_Resource1IsNull_ExpectExceptionRelayed() {
    final int currentRound = 2;

    Player mockPlayer = EasyMock.createMock(Player.class);
    DevelopmentCard mockCard = EasyMock.createMock(DevelopmentCard.class);

    EasyMock.expect(mockModel.getCurrentPlayer()).andReturn(mockPlayer);
    EasyMock.expect(mockModel.getCurrentRound()).andReturn(currentRound);
    mockHandler.playYearOfPlentyCard(mockPlayer, mockCard, currentRound, null, Resource.BRICK);
    EasyMock.expectLastCall().andThrow(new IllegalArgumentException("Resource cannot be null."));

    EasyMock.replay(mockModel, mockHandler, mockPlayer, mockCard);

    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> controller.playYearOfPlentyCard(mockModel, mockCard, null, Resource.BRICK));

    EasyMock.verify(mockModel, mockHandler, mockPlayer, mockCard);
    assertEquals("Resource cannot be null.", exception.getMessage());
  }

  // TC17: playYearOfPlentyCard(model, null, ORE, ORE); handler throws IllegalArgumentException
  //       -> controller relays IllegalArgumentException: "Development card cannot be null."
  @Test
  void playYearOfPlentyCard_CardIsNull_ExpectExceptionRelayed() {
    final int currentRound = 2;

    Player mockPlayer = EasyMock.createMock(Player.class);

    EasyMock.expect(mockModel.getCurrentPlayer()).andReturn(mockPlayer);
    EasyMock.expect(mockModel.getCurrentRound()).andReturn(currentRound);
    mockHandler.playYearOfPlentyCard(mockPlayer, null, currentRound, Resource.ORE, Resource.ORE);
    EasyMock.expectLastCall().andThrow(new IllegalArgumentException("Development card cannot be null."));

    EasyMock.replay(mockModel, mockHandler, mockPlayer);

    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> controller.playYearOfPlentyCard(mockModel, null, Resource.ORE, Resource.ORE));

    EasyMock.verify(mockModel, mockHandler, mockPlayer);
    assertEquals("Development card cannot be null.", exception.getMessage());
  }

  // TC18: getVictoryPointCount(model); handler returns 0
  //       -> controller returns 0; verify handler called with current player's hand
  @Test
  void getVictoryPointCount_HandlerReturnsZero_ExpectZero() {
    final int expectedCount = 0;

    Player mockPlayer = EasyMock.createMock(Player.class);
    List<DevelopmentCard> hand = List.of();

    EasyMock.expect(mockModel.getCurrentPlayer()).andReturn(mockPlayer);
    EasyMock.expect(mockPlayer.getDevelopmentCards()).andReturn(hand);
    EasyMock.expect(mockHandler.countVictoryPointCards(hand)).andReturn(expectedCount);

    EasyMock.replay(mockModel, mockHandler, mockPlayer);

    int result = controller.getVictoryPointCount(mockModel);

    EasyMock.verify(mockModel, mockHandler, mockPlayer);
    assertEquals(expectedCount, result);
  }

  // TC19: getVictoryPointCount(model); handler returns 3
  //       -> controller returns 3; verify handler called with current player's hand
  @Test
  void getVictoryPointCount_HandlerReturnsThree_ExpectThree() {
    final int expectedCount = 3;

    Player mockPlayer = EasyMock.createMock(Player.class);
    DevelopmentCard mockVP1 = EasyMock.createMock(DevelopmentCard.class);
    DevelopmentCard mockVP2 = EasyMock.createMock(DevelopmentCard.class);
    DevelopmentCard mockVP3 = EasyMock.createMock(DevelopmentCard.class);
    List<DevelopmentCard> hand = List.of(mockVP1, mockVP2, mockVP3);

    EasyMock.expect(mockModel.getCurrentPlayer()).andReturn(mockPlayer);
    EasyMock.expect(mockPlayer.getDevelopmentCards()).andReturn(hand);
    EasyMock.expect(mockHandler.countVictoryPointCards(hand)).andReturn(expectedCount);

    EasyMock.replay(mockModel, mockHandler, mockPlayer, mockVP1, mockVP2, mockVP3);

    int result = controller.getVictoryPointCount(mockModel);

    EasyMock.verify(mockModel, mockHandler, mockPlayer, mockVP1, mockVP2, mockVP3);
    assertEquals(expectedCount, result);
  }

  // TC20: getVictoryPointCount(model); handler returns 5 (maximum)
  //       -> controller returns 5; verify handler called
  @Test
  void getVictoryPointCount_HandlerReturnsFive_ExpectFive() {
    final int expectedCount = 5;

    Player mockPlayer = EasyMock.createMock(Player.class);
    DevelopmentCard mockVP1 = EasyMock.createMock(DevelopmentCard.class);
    DevelopmentCard mockVP2 = EasyMock.createMock(DevelopmentCard.class);
    DevelopmentCard mockVP3 = EasyMock.createMock(DevelopmentCard.class);
    DevelopmentCard mockVP4 = EasyMock.createMock(DevelopmentCard.class);
    DevelopmentCard mockVP5 = EasyMock.createMock(DevelopmentCard.class);
    List<DevelopmentCard> hand = List.of(mockVP1, mockVP2, mockVP3, mockVP4, mockVP5);

    EasyMock.expect(mockModel.getCurrentPlayer()).andReturn(mockPlayer);
    EasyMock.expect(mockPlayer.getDevelopmentCards()).andReturn(hand);
    EasyMock.expect(mockHandler.countVictoryPointCards(hand)).andReturn(expectedCount);

    EasyMock.replay(mockModel, mockHandler, mockPlayer, mockVP1, mockVP2, mockVP3, mockVP4, mockVP5);

    int result = controller.getVictoryPointCount(mockModel);

    EasyMock.verify(mockModel, mockHandler, mockPlayer, mockVP1, mockVP2, mockVP3, mockVP4, mockVP5);
    assertEquals(expectedCount, result);
  }

}
