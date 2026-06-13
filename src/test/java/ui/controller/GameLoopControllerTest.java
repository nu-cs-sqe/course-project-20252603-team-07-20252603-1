package ui.controller;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import domain.model.DevelopmentCardHandler;
import domain.model.GameModel;
import domain.model.GamePhase;
import domain.model.board.BoardHandler;
import domain.model.board.Port;
import domain.model.developmentcards.DevelopmentCard;
import domain.model.developmentcards.DevelopmentCardDeck;
import domain.model.exceptions.EmptyDeckException;
import domain.model.exceptions.IllegalGamePhaseException;
import domain.model.exceptions.InsufficientResourcesException;
import domain.model.gamepieces.DiceHandler;
import domain.model.player.Player;
import domain.model.player.PlayerColor;
import domain.model.player.TradeOffer;
import domain.model.resources.Resource;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


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

  @Test
  void testAttemptPortTradeDelegatesToModel() {
    Port mockPort = createMock(Port.class);
    mockModel.attemptPortTrade(mockPort, Resource.WOOL, Resource.ORE);
    expectLastCall();
    replay(mockModel, mockPort);

    controller.attemptPortTrade(mockModel, mockPort, Resource.WOOL, Resource.ORE);

    verify(mockModel, mockPort);
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

  // TC6: handler throws InsufficientResourcesException (buyer lacks resources)
  //      -> controller relays InsufficientResourcesException to caller
  @Test
  void buyDevCard_HandlerThrowsInsufficientResources_ExpectExceptionRelayed()
      throws EmptyDeckException {
    DevelopmentCardDeck mockDeck = createMock(DevelopmentCardDeck.class);
    DevelopmentCardHandler mockHandler = createMock(DevelopmentCardHandler.class);
    Player mockPlayer = createMock(Player.class);
    final int currentRound = 1;

    expect(mockModel.getCurrentPlayer()).andReturn(mockPlayer);
    expect(mockModel.getCurrentRound()).andReturn(currentRound);
    expect(mockHandler.buyDevelopmentCard(mockPlayer, mockDeck, currentRound))
        .andThrow(new InsufficientResourcesException("Insufficient resources"));

    replay(mockModel, mockDeck, mockHandler, mockPlayer);

    Exception exception = assertThrows(InsufficientResourcesException.class,
        () -> controller.buyDevCard(mockModel, mockDeck, mockHandler));
    assertEquals("Insufficient resources", exception.getMessage());

    verify(mockModel, mockDeck, mockHandler, mockPlayer);
  }

  // TC7: handler throws EmptyDeckException (deck is empty)
  //      -> controller relays EmptyDeckException to caller
  @Test
  void buyDevCard_HandlerThrowsEmptyDeck_ExpectExceptionRelayed() throws EmptyDeckException {
    DevelopmentCardDeck mockDeck = createMock(DevelopmentCardDeck.class);
    DevelopmentCardHandler mockHandler = createMock(DevelopmentCardHandler.class);
    Player mockPlayer = createMock(Player.class);
    final int currentRound = 1;

    expect(mockModel.getCurrentPlayer()).andReturn(mockPlayer);
    expect(mockModel.getCurrentRound()).andReturn(currentRound);
    expect(mockHandler.buyDevelopmentCard(mockPlayer, mockDeck, currentRound))
        .andThrow(new EmptyDeckException("Cannot draw new DevelopmentCard, no cards remain."));

    replay(mockModel, mockDeck, mockHandler, mockPlayer);

    Exception exception = assertThrows(EmptyDeckException.class,
        () -> controller.buyDevCard(mockModel, mockDeck, mockHandler));
    assertEquals("Cannot draw new DevelopmentCard, no cards remain.", exception.getMessage());

    verify(mockModel, mockDeck, mockHandler, mockPlayer);
  }

  @Test
  void testGetCurrentPhaseDelegatesToModel() {
    expect(mockModel.getCurrentPhase()).andReturn(GamePhase.GENERAL_PLAY);
    replay(mockModel);

    assertEquals(GamePhase.GENERAL_PLAY, controller.getCurrentPhase(mockModel));

    verify(mockModel);
  }

  @Test
  void testGetCurrentRoundDelegatesToModel() {
    expect(mockModel.getCurrentRound()).andReturn(3);
    replay(mockModel);

    assertEquals(3, controller.getCurrentRound(mockModel));

    verify(mockModel);
  }

  @Test
  void testGetOtherPlayersDelegatesToModel() {
    List<Player> others = List.of(new Player("Bob", PlayerColor.BLUE));
    expect(mockModel.getOtherPlayers()).andReturn(others);
    replay(mockModel);

    assertEquals(others, controller.getOtherPlayers(mockModel));

    verify(mockModel);
  }

  @Test
  void testAttemptBuildSettlementDelegatesToModel() {
    mockModel.attemptBuildSettlement(12);
    expectLastCall();
    replay(mockModel);

    controller.attemptBuildSettlement(mockModel, 12);

    verify(mockModel);
  }

  @Test
  void testAttemptBuildSettlementPropagatesPhaseException() {
    mockModel.attemptBuildSettlement(12);
    expectLastCall().andThrow(new IllegalGamePhaseException("Not proper phase for that action"));
    replay(mockModel);

    assertThrows(IllegalGamePhaseException.class,
        () -> controller.attemptBuildSettlement(mockModel, 12));

    verify(mockModel);
  }

  @Test
  void testAttemptBuildRoadDelegatesToModel() {
    mockModel.attemptBuildRoad(0, 3);
    expectLastCall();
    replay(mockModel);

    controller.attemptBuildRoad(mockModel, 0, 3);

    verify(mockModel);
  }

  @Test
  void testAttemptBuildCityDelegatesToModel() {
    mockModel.attemptBuildCity(7);
    expectLastCall();
    replay(mockModel);

    controller.attemptBuildCity(mockModel, 7);

    verify(mockModel);
  }

  @Test
  void testGetPlayersOnHexDelegatesToBoard() {
    BoardHandler mockBoard = createMock(BoardHandler.class);
    Set<Player> players = Set.of(new Player("Bob", PlayerColor.BLUE));
    expect(mockBoard.getPlayersOnHex(5)).andReturn(players);
    replay(mockBoard);

    assertEquals(players, controller.getPlayersOnHex(mockBoard, 5));

    verify(mockBoard);
  }

  @Test
  void testEnterSetupPhaseDelegatesToModel() {
    mockModel.enterSetupPhase();
    expectLastCall();
    replay(mockModel);

    controller.enterSetupPhase(mockModel);

    verify(mockModel);
  }

  @Test
  void testCompleteSetupPhaseDelegatesToModel() {
    mockModel.completeSetupPhase();
    expectLastCall();
    replay(mockModel);

    controller.completeSetupPhase(mockModel);

    verify(mockModel);
  }

  @Test
  void testSetCurrentPlayerSetsIndexAndColor() {
    Player bob = new Player("Bob", PlayerColor.BLUE);
    mockModel.setCurrentPlayerIndex(1);
    expectLastCall();
    expect(mockModel.getTurnOrder()).andReturn(
        List.of(new Player("Alice", PlayerColor.RED), bob));
    mockModel.setCurrentPlayerColor(PlayerColor.BLUE);
    expectLastCall();
    replay(mockModel);

    controller.setCurrentPlayer(mockModel, 1);

    verify(mockModel);
  }

  @Test
  void testGetAvailablePortsDelegatesToBoard() {
    BoardHandler mockBoard = createMock(BoardHandler.class);
    Player player = new Player("Bob", PlayerColor.BLUE);
    List<Port> ports = List.of();
    expect(mockBoard.getAvailablePorts(player)).andReturn(ports);
    replay(mockBoard);

    assertEquals(ports, controller.getAvailablePorts(mockBoard, player));

    verify(mockBoard);
  }

  // TC8: playDevCard(model, card); model completes normally
  //      -> model.playDevCard(card) called once; no exception
  @Test
  void playDevCard_ModelCompletesNormally_ExpectDelegationToModel() {
    DevelopmentCard mockCard = createMock(DevelopmentCard.class);
    mockModel.playDevCard(mockCard);
    expectLastCall();
    replay(mockModel, mockCard);

    controller.playDevCard(mockModel, mockCard);

    verify(mockModel, mockCard);
  }

  // TC9: playDevCard(model, card); model throws IllegalGamePhaseException (wrong phase)
  //      -> IllegalGamePhaseException relayed to caller
  @Test
  void playDevCard_ModelThrowsIllegalGamePhaseException_ExpectExceptionRelayed() {
    DevelopmentCard mockCard = createMock(DevelopmentCard.class);
    mockModel.playDevCard(mockCard);
    expectLastCall().andThrow(new IllegalGamePhaseException("Not proper phase for that action"));
    replay(mockModel, mockCard);

    Exception exception = assertThrows(IllegalGamePhaseException.class,
        () -> controller.playDevCard(mockModel, mockCard));
    assertEquals("Not proper phase for that action", exception.getMessage());

    verify(mockModel, mockCard);
  }

  // TC11 ← REDUCES CXTY
  // TC11: getResourceCount(model, RED, ORE); model.getArbitraryPlayer(RED) returns mockPlayer;
  //       mockPlayer.getResourceCount(ORE) returns 5 -> controller returns 5
  @Test
  void getResourceCount_ArbitraryPlayerReturnsCount_ExpectCountRelayed() {
    Player mockPlayer = createMock(Player.class);
    expect(mockModel.getArbitraryPlayer(PlayerColor.RED)).andReturn(mockPlayer);
    expect(mockPlayer.getResourceCount(Resource.ORE)).andReturn(5);
    replay(mockModel, mockPlayer);

    assertEquals(5, controller.getResourceCount(mockModel, PlayerColor.RED, Resource.ORE));

    verify(mockModel, mockPlayer);
  }

  // TC10: playDevCard(model, card); model throws IllegalArgumentException (null card)
  //       -> IllegalArgumentException relayed to caller
  @Test
  void playDevCard_ModelThrowsIllegalArgumentException_ExpectExceptionRelayed() {
    DevelopmentCard mockCard = createMock(DevelopmentCard.class);
    mockModel.playDevCard(mockCard);
    expectLastCall().andThrow(new IllegalArgumentException("Development card cannot be null."));
    replay(mockModel, mockCard);

    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> controller.playDevCard(mockModel, mockCard));
    assertEquals("Development card cannot be null.", exception.getMessage());

    verify(mockModel, mockCard);
  }

  //
  @Test
  void testMoveRobberAndStealDelegatesToModel() {
    mockModel.moveRobberAndSteal(5, PlayerColor.RED);
    expectLastCall();
    replay(mockModel);

    controller.moveRobberAndSteal(mockModel, 5, PlayerColor.RED);

    verify(mockModel);
  }
}
