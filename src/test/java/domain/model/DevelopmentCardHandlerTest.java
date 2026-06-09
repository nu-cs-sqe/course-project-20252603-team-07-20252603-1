package domain.model;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import domain.model.development_cards.DevelopmentCard;
import domain.model.development_cards.DevelopmentCardDeck;
import domain.model.exceptions.EmptyDeckException;
import domain.model.player.Player;
import domain.model.resources.Resource;

import java.util.List;
import java.util.Map;

import domain.model.development_cards.DevelopmentCardType;
import domain.model.exceptions.InsufficientResourcesException;
import domain.model.game_pieces.Robber;

import static org.junit.jupiter.api.Assertions.*;

class DevelopmentCardHandlerTest {

  // TC1: buyer has 1 ORE, 1 WOOL, 1 GRAIN (exact cost); deck full (25)
  //      -> card returned; buyer loses 1 ORE/WOOL/GRAIN; card added to hand; deck decremented
  @Test
  void buyDevelopmentCard_BuyerHasExactResources_ExpectCardDrawnAndResourcesDecremented() throws EmptyDeckException {
    final int currentRound = 1;

    Player mockBuyer = EasyMock.createMock(Player.class);
    DevelopmentCardDeck mockDeck = EasyMock.createMock(DevelopmentCardDeck.class);
    DevelopmentCard mockCard = EasyMock.createMock(DevelopmentCard.class);

    EasyMock.expect(mockBuyer.getResourceCount(Resource.ORE)).andReturn(1);
    EasyMock.expect(mockBuyer.getResourceCount(Resource.WOOL)).andReturn(1);
    EasyMock.expect(mockBuyer.getResourceCount(Resource.GRAIN)).andReturn(1);
    EasyMock.expect(mockDeck.drawCard(currentRound)).andReturn(mockCard);
    
    mockBuyer.updateResources(Resource.ORE, -1);
    EasyMock.expectLastCall();
    mockBuyer.updateResources(Resource.WOOL, -1);
    EasyMock.expectLastCall();
    mockBuyer.updateResources(Resource.GRAIN, -1);
    EasyMock.expectLastCall();
    mockBuyer.addDevelopmentCard(mockCard);
    EasyMock.expectLastCall();

    EasyMock.replay(mockBuyer, mockDeck, mockCard);

    DevelopmentCardHandler handler = new DevelopmentCardHandler();
    DevelopmentCard result = handler.buyDevelopmentCard(mockBuyer, mockDeck, currentRound);

    EasyMock.verify(mockBuyer, mockDeck, mockCard);
    assertEquals(mockCard, result);
  }

  // TC2: buyer has 3 ORE, 2 WOOL, 4 GRAIN (surplus); deck full (25)
  //      -> card returned; buyer's resources each decremented by 1; card added to hand
  @Test
  void buyDevelopmentCard_BuyerHasSurplusResources_ExpectCardDrawnAndResourcesDecremented() throws EmptyDeckException {
    final int currentRound = 1;

    Player mockBuyer = EasyMock.createMock(Player.class);
    DevelopmentCardDeck mockDeck = EasyMock.createMock(DevelopmentCardDeck.class);
    DevelopmentCard mockCard = EasyMock.createMock(DevelopmentCard.class);

    EasyMock.expect(mockBuyer.getResourceCount(Resource.ORE)).andReturn(3);
    EasyMock.expect(mockBuyer.getResourceCount(Resource.WOOL)).andReturn(2);
    EasyMock.expect(mockBuyer.getResourceCount(Resource.GRAIN)).andReturn(4);
    EasyMock.expect(mockDeck.drawCard(currentRound)).andReturn(mockCard);
    
    mockBuyer.updateResources(Resource.ORE, -1);
    EasyMock.expectLastCall();
    mockBuyer.updateResources(Resource.WOOL, -1);
    EasyMock.expectLastCall();
    mockBuyer.updateResources(Resource.GRAIN, -1);
    EasyMock.expectLastCall();
    mockBuyer.addDevelopmentCard(mockCard);
    EasyMock.expectLastCall();

    EasyMock.replay(mockBuyer, mockDeck, mockCard);

    DevelopmentCardHandler handler = new DevelopmentCardHandler();
    DevelopmentCard result = handler.buyDevelopmentCard(mockBuyer, mockDeck, currentRound);

    EasyMock.verify(mockBuyer, mockDeck, mockCard);
    assertEquals(mockCard, result);
  }

  // TC3: buyer has 0 ORE, 1 WOOL, 1 GRAIN (insufficient ORE)
  //      -> InsufficientResourcesException: "Not enough resources to buy a development card."
  @Test
  void buyDevelopmentCard_BuyerHasInsufficientOre_ExpectInsufficientResourcesException() {
    final int currentRound = 1;

    Player mockBuyer = EasyMock.createMock(Player.class);
    DevelopmentCardDeck mockDeck = EasyMock.createMock(DevelopmentCardDeck.class);

    EasyMock.expect(mockBuyer.getResourceCount(Resource.ORE)).andReturn(0);

    EasyMock.replay(mockBuyer, mockDeck);

    DevelopmentCardHandler handler = new DevelopmentCardHandler();
    Exception exception = assertThrows(InsufficientResourcesException.class,
        () -> handler.buyDevelopmentCard(mockBuyer, mockDeck, currentRound));

    EasyMock.verify(mockBuyer, mockDeck);
    assertEquals("Not enough resources to buy a development card.", exception.getMessage());
  }

  // TC4: buyer has 1 ORE, 0 WOOL, 1 GRAIN (insufficient WOOL)
  //      -> InsufficientResourcesException: "Not enough resources to buy a development card."
  @Test
  void buyDevelopmentCard_BuyerHasInsufficientWool_ExpectInsufficientResourcesException() {
    final int currentRound = 1;

    Player mockBuyer = EasyMock.createMock(Player.class);
    DevelopmentCardDeck mockDeck = EasyMock.createMock(DevelopmentCardDeck.class);

    EasyMock.expect(mockBuyer.getResourceCount(Resource.ORE)).andReturn(1);
    EasyMock.expect(mockBuyer.getResourceCount(Resource.WOOL)).andReturn(0);

    EasyMock.replay(mockBuyer, mockDeck);

    DevelopmentCardHandler handler = new DevelopmentCardHandler();
    Exception exception = assertThrows(InsufficientResourcesException.class,
        () -> handler.buyDevelopmentCard(mockBuyer, mockDeck, currentRound));

    EasyMock.verify(mockBuyer, mockDeck);
    assertEquals("Not enough resources to buy a development card.", exception.getMessage());
  }

  // TC5: buyer has 1 ORE, 1 WOOL, 0 GRAIN (insufficient GRAIN)
  //      -> InsufficientResourcesException: "Not enough resources to buy a development card."
  @Test
  void buyDevelopmentCard_BuyerHasInsufficientGrain_ExpectInsufficientResourcesException() {
    final int currentRound = 1;

    Player mockBuyer = EasyMock.createMock(Player.class);
    DevelopmentCardDeck mockDeck = EasyMock.createMock(DevelopmentCardDeck.class);

    EasyMock.expect(mockBuyer.getResourceCount(Resource.ORE)).andReturn(1);
    EasyMock.expect(mockBuyer.getResourceCount(Resource.WOOL)).andReturn(1);
    EasyMock.expect(mockBuyer.getResourceCount(Resource.GRAIN)).andReturn(0);

    EasyMock.replay(mockBuyer, mockDeck);

    DevelopmentCardHandler handler = new DevelopmentCardHandler();
    Exception exception = assertThrows(InsufficientResourcesException.class,
        () -> handler.buyDevelopmentCard(mockBuyer, mockDeck, currentRound));

    EasyMock.verify(mockBuyer, mockDeck);
    assertEquals("Not enough resources to buy a development card.", exception.getMessage());
  }

  // TC6: buyer has 1 ORE, 1 WOOL, 1 GRAIN; deck empty (0)
  //      -> EmptyDeckException: "Cannot draw new DevelopmentCard, no cards remain."
  @Test
  void buyDevelopmentCard_DeckEmpty_ExpectEmptyDeckException() throws EmptyDeckException {
    final int currentRound = 1;

    Player mockBuyer = EasyMock.createMock(Player.class);
    DevelopmentCardDeck mockDeck = EasyMock.createMock(DevelopmentCardDeck.class);

    EasyMock.expect(mockBuyer.getResourceCount(Resource.ORE)).andReturn(1);
    EasyMock.expect(mockBuyer.getResourceCount(Resource.WOOL)).andReturn(1);
    EasyMock.expect(mockBuyer.getResourceCount(Resource.GRAIN)).andReturn(1);
    EasyMock.expect(mockDeck.drawCard(currentRound))
        .andThrow(new EmptyDeckException("Cannot draw new DevelopmentCard, no cards remain."));

    EasyMock.replay(mockBuyer, mockDeck);

    DevelopmentCardHandler handler = new DevelopmentCardHandler();
    Exception exception = assertThrows(EmptyDeckException.class,
        () -> handler.buyDevelopmentCard(mockBuyer, mockDeck, currentRound));

    EasyMock.verify(mockBuyer, mockDeck);
    assertEquals("Cannot draw new DevelopmentCard, no cards remain.", exception.getMessage());
  }

  // TC7: buyer has 1 ORE, 1 WOOL, 1 GRAIN; deck has 1 card remaining (last card)
  //      -> card returned; deck countRemaining() is 0
  @Test
  void buyDevelopmentCard_DeckHasOneCardRemaining_ExpectLastCardDrawnAndResourcesDecremented() throws EmptyDeckException {
    final int currentRound = 1;

    Player mockBuyer = EasyMock.createMock(Player.class);
    DevelopmentCardDeck mockDeck = EasyMock.createMock(DevelopmentCardDeck.class);
    DevelopmentCard mockCard = EasyMock.createMock(DevelopmentCard.class);

    EasyMock.expect(mockBuyer.getResourceCount(Resource.ORE)).andReturn(1);
    EasyMock.expect(mockBuyer.getResourceCount(Resource.WOOL)).andReturn(1);
    EasyMock.expect(mockBuyer.getResourceCount(Resource.GRAIN)).andReturn(1);
    EasyMock.expect(mockDeck.drawCard(currentRound)).andReturn(mockCard);
    
    mockBuyer.updateResources(Resource.ORE, -1);
    EasyMock.expectLastCall();
    mockBuyer.updateResources(Resource.WOOL, -1);
    EasyMock.expectLastCall();
    mockBuyer.updateResources(Resource.GRAIN, -1);
    EasyMock.expectLastCall();
    
    mockBuyer.addDevelopmentCard(mockCard);
    EasyMock.expectLastCall();

    EasyMock.replay(mockBuyer, mockDeck, mockCard);

    DevelopmentCardHandler handler = new DevelopmentCardHandler();
    DevelopmentCard result = handler.buyDevelopmentCard(mockBuyer, mockDeck, currentRound);

    EasyMock.verify(mockBuyer, mockDeck, mockCard);
    assertEquals(mockCard, result);
  }

  // TC8: card = null
  //      -> IllegalArgumentException: "Development card cannot be null."
  @Test
  void playKnightCard_CardIsNull_ExpectIllegalArgumentException() {
    final int currentRound = 1;

    Player mockPlayer = EasyMock.createMock(Player.class);
    Robber mockRobber = EasyMock.createMock(Robber.class);
    Player mockVictim = EasyMock.createMock(Player.class);

    EasyMock.replay(mockPlayer, mockRobber, mockVictim);

    DevelopmentCardHandler handler = new DevelopmentCardHandler();
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> handler.playKnightCard(mockPlayer, null, currentRound, mockRobber, 5, mockVictim));

    EasyMock.verify(mockPlayer, mockRobber, mockVictim);
    assertEquals("Development card cannot be null.", exception.getMessage());
  }

  // TC9: card type = MONOPOLY (not KNIGHT)
  //      -> IllegalArgumentException: "Card is not a Knight card."
  @Test
  void playKnightCard_CardTypeIsNotKnight_ExpectIllegalArgumentException() {
    final int currentRound = 1;

    Player mockPlayer = EasyMock.createMock(Player.class);
    DevelopmentCard mockCard = EasyMock.createMock(DevelopmentCard.class);
    Robber mockRobber = EasyMock.createMock(Robber.class);
    Player mockVictim = EasyMock.createMock(Player.class);

    EasyMock.expect(mockCard.getType()).andReturn(DevelopmentCardType.MONOPOLY);

    EasyMock.replay(mockPlayer, mockCard, mockRobber, mockVictim);

    DevelopmentCardHandler handler = new DevelopmentCardHandler();
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> handler.playKnightCard(mockPlayer, mockCard, currentRound, mockRobber, 5, mockVictim));

    EasyMock.verify(mockPlayer, mockCard, mockRobber, mockVictim);
    assertEquals("Card is not a Knight card.", exception.getMessage());
  }

  // TC10: card drawn this round (not playable, same turn)
  //       -> IllegalStateException: "Card cannot be played the same turn it was purchased."
  @Test
  void playKnightCard_CardNotPlayableSameTurn_ExpectIllegalStateException() {
    final int currentRound = 1;

    Player mockPlayer = EasyMock.createMock(Player.class);
    DevelopmentCard mockCard = EasyMock.createMock(DevelopmentCard.class);
    Robber mockRobber = EasyMock.createMock(Robber.class);
    Player mockVictim = EasyMock.createMock(Player.class);

    EasyMock.expect(mockCard.getType()).andReturn(DevelopmentCardType.KNIGHT);
    EasyMock.expect(mockCard.isPlayable(currentRound)).andReturn(false);

    EasyMock.replay(mockPlayer, mockCard, mockRobber, mockVictim);

    DevelopmentCardHandler handler = new DevelopmentCardHandler();
    Exception exception = assertThrows(IllegalStateException.class,
        () -> handler.playKnightCard(mockPlayer, mockCard, currentRound, mockRobber, 5, mockVictim));

    EasyMock.verify(mockPlayer, mockCard, mockRobber, mockVictim);
    assertEquals("Card cannot be played the same turn it was purchased.", exception.getMessage());
  }

  // TC11: player already played a dev card this turn
  //       -> IllegalStateException: "Already played a development card this turn."
  @Test
  void playKnightCard_AlreadyPlayedDevCardThisTurn_ExpectIllegalStateException() {
    final int currentRound = 1;

    Player mockPlayer = EasyMock.createMock(Player.class);
    DevelopmentCard mockCard = EasyMock.createMock(DevelopmentCard.class);
    Robber mockRobber = EasyMock.createMock(Robber.class);
    Player mockVictim = EasyMock.createMock(Player.class);

    EasyMock.expect(mockCard.getType()).andReturn(DevelopmentCardType.KNIGHT);
    EasyMock.expect(mockCard.isPlayable(currentRound)).andReturn(true);
    EasyMock.expect(mockPlayer.hasPlayedDevCardThisTurn()).andReturn(true);

    EasyMock.replay(mockPlayer, mockCard, mockRobber, mockVictim);

    DevelopmentCardHandler handler = new DevelopmentCardHandler();
    Exception exception = assertThrows(IllegalStateException.class,
        () -> handler.playKnightCard(mockPlayer, mockCard, currentRound, mockRobber, 5, mockVictim));

    EasyMock.verify(mockPlayer, mockCard, mockRobber, mockVictim);
    assertEquals("Already played a development card this turn.", exception.getMessage());
  }

  // TC12: robber = null
  //       -> IllegalArgumentException: "Robber cannot be null."
  @Test
  void playKnightCard_RobberIsNull_ExpectIllegalArgumentException() {
    final int currentRound = 1;

    Player mockPlayer = EasyMock.createMock(Player.class);
    DevelopmentCard mockCard = EasyMock.createMock(DevelopmentCard.class);
    Player mockVictim = EasyMock.createMock(Player.class);

    EasyMock.expect(mockCard.getType()).andReturn(DevelopmentCardType.KNIGHT);
    EasyMock.expect(mockCard.isPlayable(currentRound)).andReturn(true);
    EasyMock.expect(mockPlayer.hasPlayedDevCardThisTurn()).andReturn(false);

    EasyMock.replay(mockPlayer, mockCard, mockVictim);

    DevelopmentCardHandler handler = new DevelopmentCardHandler();
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> handler.playKnightCard(mockPlayer, mockCard, currentRound, null, 5, mockVictim));

    EasyMock.verify(mockPlayer, mockCard, mockVictim);
    assertEquals("Robber cannot be null.", exception.getMessage());
  }

  // TC13: targetHexId = 5 (valid, different from current hex 3), victim adjacent with 3 resources
  //       -> robber moves to hex 5; 1 resource transferred from victim to player; knight count incremented; card removed
  @Test
  void playKnightCard_ValidMoveVictimHasResources_ExpectRobberMovedAndResourceStolen() {
    final int currentRound = 2;
    final int targetHexId = 5;

    Player mockPlayer = EasyMock.createMock(Player.class);
    DevelopmentCard mockCard = EasyMock.createMock(DevelopmentCard.class);
    Robber mockRobber = EasyMock.createMock(Robber.class);
    Player mockVictim = EasyMock.createMock(Player.class);

    EasyMock.expect(mockCard.getType()).andReturn(DevelopmentCardType.KNIGHT);
    EasyMock.expect(mockCard.isPlayable(currentRound)).andReturn(true);
    EasyMock.expect(mockPlayer.hasPlayedDevCardThisTurn()).andReturn(false);
    EasyMock.expect(mockRobber.getRobberLocation()).andReturn(3);
    EasyMock.expect(mockVictim.isAdjacentToHex(targetHexId)).andReturn(true);
    mockRobber.moveRobber(targetHexId);
    EasyMock.expect(mockVictim.getTotalResourceCount()).andReturn(3);
    EasyMock.expect(mockVictim.getResources()).andReturn(Map.of(Resource.ORE, 3));
    mockVictim.updateResources(Resource.ORE, -1);
    mockPlayer.updateResources(Resource.ORE, 1);
    mockPlayer.incrementKnightCount();
    mockPlayer.removeDevelopmentCard(mockCard);
    mockPlayer.setHasPlayedDevCardThisTurn(true);

    EasyMock.replay(mockPlayer, mockCard, mockRobber, mockVictim);

    DevelopmentCardHandler handler = new DevelopmentCardHandler();
    handler.playKnightCard(mockPlayer, mockCard, currentRound, mockRobber, targetHexId, mockVictim);

    EasyMock.verify(mockPlayer, mockCard, mockRobber, mockVictim);
  }

  // TC14: targetHexId = 0 (LOW boundary), victim adjacent with resources
  //       -> robber moves to hex 0; 1 resource transferred from victim to player
  @Test
  void playKnightCard_TargetHexIdAtLowBoundary_ExpectRobberMovedAndResourceStolen() {
    final int currentRound = 2;
    final int targetHexId = 0;

    Player mockPlayer = EasyMock.createMock(Player.class);
    DevelopmentCard mockCard = EasyMock.createMock(DevelopmentCard.class);
    Robber mockRobber = EasyMock.createMock(Robber.class);
    Player mockVictim = EasyMock.createMock(Player.class);

    EasyMock.expect(mockCard.getType()).andReturn(DevelopmentCardType.KNIGHT);
    EasyMock.expect(mockCard.isPlayable(currentRound)).andReturn(true);
    EasyMock.expect(mockPlayer.hasPlayedDevCardThisTurn()).andReturn(false);
    EasyMock.expect(mockRobber.getRobberLocation()).andReturn(5);
    EasyMock.expect(mockVictim.isAdjacentToHex(targetHexId)).andReturn(true);
    mockRobber.moveRobber(targetHexId);
    EasyMock.expect(mockVictim.getTotalResourceCount()).andReturn(1);
    EasyMock.expect(mockVictim.getResources()).andReturn(Map.of(Resource.WOOL, 1));
    mockVictim.updateResources(Resource.WOOL, -1);
    mockPlayer.updateResources(Resource.WOOL, 1);
    mockPlayer.incrementKnightCount();
    mockPlayer.removeDevelopmentCard(mockCard);
    mockPlayer.setHasPlayedDevCardThisTurn(true);

    EasyMock.replay(mockPlayer, mockCard, mockRobber, mockVictim);

    DevelopmentCardHandler handler = new DevelopmentCardHandler();
    handler.playKnightCard(mockPlayer, mockCard, currentRound, mockRobber, targetHexId, mockVictim);

    EasyMock.verify(mockPlayer, mockCard, mockRobber, mockVictim);
  }

  // TC15: targetHexId = 18 (HIGH boundary), victim adjacent with resources
  //       -> robber moves to hex 18; 1 resource transferred from victim to player
  @Test
  void playKnightCard_TargetHexIdAtHighBoundary_ExpectRobberMovedAndResourceStolen() {
    final int currentRound = 2;
    final int targetHexId = 18;

    Player mockPlayer = EasyMock.createMock(Player.class);
    DevelopmentCard mockCard = EasyMock.createMock(DevelopmentCard.class);
    Robber mockRobber = EasyMock.createMock(Robber.class);
    Player mockVictim = EasyMock.createMock(Player.class);

    EasyMock.expect(mockCard.getType()).andReturn(DevelopmentCardType.KNIGHT);
    EasyMock.expect(mockCard.isPlayable(currentRound)).andReturn(true);
    EasyMock.expect(mockPlayer.hasPlayedDevCardThisTurn()).andReturn(false);
    EasyMock.expect(mockRobber.getRobberLocation()).andReturn(5);
    EasyMock.expect(mockVictim.isAdjacentToHex(targetHexId)).andReturn(true);
    mockRobber.moveRobber(targetHexId);
    EasyMock.expect(mockVictim.getTotalResourceCount()).andReturn(2);
    EasyMock.expect(mockVictim.getResources()).andReturn(Map.of(Resource.GRAIN, 2));
    mockVictim.updateResources(Resource.GRAIN, -1);
    mockPlayer.updateResources(Resource.GRAIN, 1);
    mockPlayer.incrementKnightCount();
    mockPlayer.removeDevelopmentCard(mockCard);
    mockPlayer.setHasPlayedDevCardThisTurn(true);

    EasyMock.replay(mockPlayer, mockCard, mockRobber, mockVictim);

    DevelopmentCardHandler handler = new DevelopmentCardHandler();
    handler.playKnightCard(mockPlayer, mockCard, currentRound, mockRobber, targetHexId, mockVictim);

    EasyMock.verify(mockPlayer, mockCard, mockRobber, mockVictim);
  }

  // TC16: targetHexId = robber's current hex (same hex)
  //       -> IllegalArgumentException: "Must move robber to a different hex."
  @Test
  void playKnightCard_TargetHexIsSameAsCurrentHex_ExpectIllegalArgumentException() {
    final int currentRound = 2;
    final int targetHexId = 5;

    Player mockPlayer = EasyMock.createMock(Player.class);
    DevelopmentCard mockCard = EasyMock.createMock(DevelopmentCard.class);
    Robber mockRobber = EasyMock.createMock(Robber.class);
    Player mockVictim = EasyMock.createMock(Player.class);

    EasyMock.expect(mockCard.getType()).andReturn(DevelopmentCardType.KNIGHT);
    EasyMock.expect(mockCard.isPlayable(currentRound)).andReturn(true);
    EasyMock.expect(mockPlayer.hasPlayedDevCardThisTurn()).andReturn(false);
    EasyMock.expect(mockRobber.getRobberLocation()).andReturn(5);

    EasyMock.replay(mockPlayer, mockCard, mockRobber, mockVictim);

    DevelopmentCardHandler handler = new DevelopmentCardHandler();
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> handler.playKnightCard(mockPlayer, mockCard, currentRound, mockRobber, targetHexId, mockVictim));

    EasyMock.verify(mockPlayer, mockCard, mockRobber, mockVictim);
    assertEquals("Must move robber to a different hex.", exception.getMessage());
  }

  // TC17: targetHexId valid, victim = null (no adjacent opponent)
  //       -> robber moves; no resource stolen; knight count incremented; card removed
  @Test
  void playKnightCard_VictimIsNull_ExpectRobberMovedNoResourceStolen() {
    final int currentRound = 2;
    final int targetHexId = 5;

    Player mockPlayer = EasyMock.createMock(Player.class);
    DevelopmentCard mockCard = EasyMock.createMock(DevelopmentCard.class);
    Robber mockRobber = EasyMock.createMock(Robber.class);

    EasyMock.expect(mockCard.getType()).andReturn(DevelopmentCardType.KNIGHT);
    EasyMock.expect(mockCard.isPlayable(currentRound)).andReturn(true);
    EasyMock.expect(mockPlayer.hasPlayedDevCardThisTurn()).andReturn(false);
    EasyMock.expect(mockRobber.getRobberLocation()).andReturn(3);
    mockRobber.moveRobber(targetHexId);
    mockPlayer.incrementKnightCount();
    mockPlayer.removeDevelopmentCard(mockCard);
    mockPlayer.setHasPlayedDevCardThisTurn(true);

    EasyMock.replay(mockPlayer, mockCard, mockRobber);

    DevelopmentCardHandler handler = new DevelopmentCardHandler();
    handler.playKnightCard(mockPlayer, mockCard, currentRound, mockRobber, targetHexId, null);

    EasyMock.verify(mockPlayer, mockCard, mockRobber);
  }

  // TC18: targetHexId valid, victim adjacent with 0 resource cards
  //       -> robber moves; no resource stolen; knight count incremented; card removed
  @Test
  void playKnightCard_VictimHasNoResources_ExpectRobberMovedNoResourceStolen() {
    final int currentRound = 2;
    final int targetHexId = 5;

    Player mockPlayer = EasyMock.createMock(Player.class);
    DevelopmentCard mockCard = EasyMock.createMock(DevelopmentCard.class);
    Robber mockRobber = EasyMock.createMock(Robber.class);
    Player mockVictim = EasyMock.createMock(Player.class);

    EasyMock.expect(mockCard.getType()).andReturn(DevelopmentCardType.KNIGHT);
    EasyMock.expect(mockCard.isPlayable(currentRound)).andReturn(true);
    EasyMock.expect(mockPlayer.hasPlayedDevCardThisTurn()).andReturn(false);
    EasyMock.expect(mockRobber.getRobberLocation()).andReturn(3);
    EasyMock.expect(mockVictim.isAdjacentToHex(targetHexId)).andReturn(true);
    mockRobber.moveRobber(targetHexId);
    EasyMock.expect(mockVictim.getTotalResourceCount()).andReturn(0);
    mockPlayer.incrementKnightCount();
    mockPlayer.removeDevelopmentCard(mockCard);
    mockPlayer.setHasPlayedDevCardThisTurn(true);

    EasyMock.replay(mockPlayer, mockCard, mockRobber, mockVictim);

    DevelopmentCardHandler handler = new DevelopmentCardHandler();
    handler.playKnightCard(mockPlayer, mockCard, currentRound, mockRobber, targetHexId, mockVictim);

    EasyMock.verify(mockPlayer, mockCard, mockRobber, mockVictim);
  }

  // TC19: targetHexId valid, victim adjacent with exactly 1 resource card
  //       -> robber moves; that 1 resource transferred; knight count incremented; card removed
  @Test
  void playKnightCard_VictimHasExactlyOneResource_ExpectThatResourceStolen() {
    final int currentRound = 2;
    final int targetHexId = 5;

    Player mockPlayer = EasyMock.createMock(Player.class);
    DevelopmentCard mockCard = EasyMock.createMock(DevelopmentCard.class);
    Robber mockRobber = EasyMock.createMock(Robber.class);
    Player mockVictim = EasyMock.createMock(Player.class);

    EasyMock.expect(mockCard.getType()).andReturn(DevelopmentCardType.KNIGHT);
    EasyMock.expect(mockCard.isPlayable(currentRound)).andReturn(true);
    EasyMock.expect(mockPlayer.hasPlayedDevCardThisTurn()).andReturn(false);
    EasyMock.expect(mockRobber.getRobberLocation()).andReturn(3);
    EasyMock.expect(mockVictim.isAdjacentToHex(targetHexId)).andReturn(true);
    mockRobber.moveRobber(targetHexId);
    EasyMock.expect(mockVictim.getTotalResourceCount()).andReturn(1);
    EasyMock.expect(mockVictim.getResources()).andReturn(Map.of(Resource.BRICK, 1));
    mockVictim.updateResources(Resource.BRICK, -1);
    mockPlayer.updateResources(Resource.BRICK, 1);
    mockPlayer.incrementKnightCount();
    mockPlayer.removeDevelopmentCard(mockCard);
    mockPlayer.setHasPlayedDevCardThisTurn(true);

    EasyMock.replay(mockPlayer, mockCard, mockRobber, mockVictim);

    DevelopmentCardHandler handler = new DevelopmentCardHandler();
    handler.playKnightCard(mockPlayer, mockCard, currentRound, mockRobber, targetHexId, mockVictim);

    EasyMock.verify(mockPlayer, mockCard, mockRobber, mockVictim);
  }

  // TC20: targetHexId valid, victim not adjacent to targetHexId
  //       -> IllegalArgumentException: "Victim must be adjacent to the robber's new hex."
  @Test
  void playKnightCard_VictimNotAdjacentToTargetHex_ExpectIllegalArgumentException() {
    final int currentRound = 2;
    final int targetHexId = 5;

    Player mockPlayer = EasyMock.createMock(Player.class);
    DevelopmentCard mockCard = EasyMock.createMock(DevelopmentCard.class);
    Robber mockRobber = EasyMock.createMock(Robber.class);
    Player mockVictim = EasyMock.createMock(Player.class);

    EasyMock.expect(mockCard.getType()).andReturn(DevelopmentCardType.KNIGHT);
    EasyMock.expect(mockCard.isPlayable(currentRound)).andReturn(true);
    EasyMock.expect(mockPlayer.hasPlayedDevCardThisTurn()).andReturn(false);
    EasyMock.expect(mockRobber.getRobberLocation()).andReturn(3);
    EasyMock.expect(mockVictim.isAdjacentToHex(targetHexId)).andReturn(false);

    EasyMock.replay(mockPlayer, mockCard, mockRobber, mockVictim);

    DevelopmentCardHandler handler = new DevelopmentCardHandler();
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> handler.playKnightCard(mockPlayer, mockCard, currentRound, mockRobber, targetHexId, mockVictim));

    EasyMock.verify(mockPlayer, mockCard, mockRobber, mockVictim);
    assertEquals("Victim must be adjacent to the robber's new hex.", exception.getMessage());
  }

  // TC21: card = null
  //       -> IllegalArgumentException: "Development card cannot be null."
  @Test
  void playMonopolyCard_CardIsNull_ExpectIllegalArgumentException() {
    final int currentRound = 1;

    Player mockPlayer = EasyMock.createMock(Player.class);

    EasyMock.replay(mockPlayer);

    DevelopmentCardHandler handler = new DevelopmentCardHandler();
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> handler.playMonopolyCard(mockPlayer, null, currentRound, Resource.ORE, List.of()));

    EasyMock.verify(mockPlayer);
    assertEquals("Development card cannot be null.", exception.getMessage());
  }

  // TC22: card type = KNIGHT (not MONOPOLY)
  //       -> IllegalArgumentException: "Card is not a Monopoly card."
  @Test
  void playMonopolyCard_CardTypeIsNotMonopoly_ExpectIllegalArgumentException() {
    final int currentRound = 1;

    Player mockPlayer = EasyMock.createMock(Player.class);
    DevelopmentCard mockCard = EasyMock.createMock(DevelopmentCard.class);

    EasyMock.expect(mockCard.getType()).andReturn(DevelopmentCardType.KNIGHT);

    EasyMock.replay(mockPlayer, mockCard);

    DevelopmentCardHandler handler = new DevelopmentCardHandler();
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> handler.playMonopolyCard(mockPlayer, mockCard, currentRound, Resource.ORE, List.of()));

    EasyMock.verify(mockPlayer, mockCard);
    assertEquals("Card is not a Monopoly card.", exception.getMessage());
  }

  // TC23: card drawn this round (not playable)
  //       -> IllegalStateException: "Card cannot be played the same turn it was purchased."
  @Test
  void playMonopolyCard_CardNotPlayableSameTurn_ExpectIllegalStateException() {
    final int currentRound = 1;

    Player mockPlayer = EasyMock.createMock(Player.class);
    DevelopmentCard mockCard = EasyMock.createMock(DevelopmentCard.class);

    EasyMock.expect(mockCard.getType()).andReturn(DevelopmentCardType.MONOPOLY);
    EasyMock.expect(mockCard.isPlayable(currentRound)).andReturn(false);

    EasyMock.replay(mockPlayer, mockCard);

    DevelopmentCardHandler handler = new DevelopmentCardHandler();
    Exception exception = assertThrows(IllegalStateException.class,
        () -> handler.playMonopolyCard(mockPlayer, mockCard, currentRound, Resource.ORE, List.of()));

    EasyMock.verify(mockPlayer, mockCard);
    assertEquals("Card cannot be played the same turn it was purchased.", exception.getMessage());
  }

  // TC24: player already played a dev card this turn
  //       -> IllegalStateException: "Already played a development card this turn."
  @Test
  void playMonopolyCard_AlreadyPlayedDevCardThisTurn_ExpectIllegalStateException() {
    final int currentRound = 1;

    Player mockPlayer = EasyMock.createMock(Player.class);
    DevelopmentCard mockCard = EasyMock.createMock(DevelopmentCard.class);

    EasyMock.expect(mockCard.getType()).andReturn(DevelopmentCardType.MONOPOLY);
    EasyMock.expect(mockCard.isPlayable(currentRound)).andReturn(true);
    EasyMock.expect(mockPlayer.hasPlayedDevCardThisTurn()).andReturn(true);

    EasyMock.replay(mockPlayer, mockCard);

    DevelopmentCardHandler handler = new DevelopmentCardHandler();
    Exception exception = assertThrows(IllegalStateException.class,
        () -> handler.playMonopolyCard(mockPlayer, mockCard, currentRound, Resource.ORE, List.of()));

    EasyMock.verify(mockPlayer, mockCard);
    assertEquals("Already played a development card this turn.", exception.getMessage());
  }

  // TC25: resource = null
  //       -> IllegalArgumentException: "Resource cannot be null."
  @Test
  void playMonopolyCard_ResourceIsNull_ExpectIllegalArgumentException() {
    final int currentRound = 1;

    Player mockPlayer = EasyMock.createMock(Player.class);
    DevelopmentCard mockCard = EasyMock.createMock(DevelopmentCard.class);

    EasyMock.expect(mockCard.getType()).andReturn(DevelopmentCardType.MONOPOLY);
    EasyMock.expect(mockCard.isPlayable(currentRound)).andReturn(true);
    EasyMock.expect(mockPlayer.hasPlayedDevCardThisTurn()).andReturn(false);

    EasyMock.replay(mockPlayer, mockCard);

    DevelopmentCardHandler handler = new DevelopmentCardHandler();
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> handler.playMonopolyCard(mockPlayer, mockCard, currentRound, null, List.of()));

    EasyMock.verify(mockPlayer, mockCard);
    assertEquals("Resource cannot be null.", exception.getMessage());
  }

  // TC26: resource = DESERT
  //       -> IllegalArgumentException: "Cannot monopolize DESERT."
  @Test
  void playMonopolyCard_ResourceIsDesert_ExpectIllegalArgumentException() {
    final int currentRound = 1;

    Player mockPlayer = EasyMock.createMock(Player.class);
    DevelopmentCard mockCard = EasyMock.createMock(DevelopmentCard.class);

    EasyMock.expect(mockCard.getType()).andReturn(DevelopmentCardType.MONOPOLY);
    EasyMock.expect(mockCard.isPlayable(currentRound)).andReturn(true);
    EasyMock.expect(mockPlayer.hasPlayedDevCardThisTurn()).andReturn(false);

    EasyMock.replay(mockPlayer, mockCard);

    DevelopmentCardHandler handler = new DevelopmentCardHandler();
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> handler.playMonopolyCard(mockPlayer, mockCard, currentRound, Resource.DESERT, List.of()));

    EasyMock.verify(mockPlayer, mockCard);
    assertEquals("Cannot monopolize DESERT.", exception.getMessage());
  }

  // TC27: otherPlayers = null
  //       -> IllegalArgumentException: "Other players list cannot be null."
  @Test
  void playMonopolyCard_OtherPlayersIsNull_ExpectIllegalArgumentException() {
    final int currentRound = 1;

    Player mockPlayer = EasyMock.createMock(Player.class);
    DevelopmentCard mockCard = EasyMock.createMock(DevelopmentCard.class);

    EasyMock.expect(mockCard.getType()).andReturn(DevelopmentCardType.MONOPOLY);
    EasyMock.expect(mockCard.isPlayable(currentRound)).andReturn(true);
    EasyMock.expect(mockPlayer.hasPlayedDevCardThisTurn()).andReturn(false);

    EasyMock.replay(mockPlayer, mockCard);

    DevelopmentCardHandler handler = new DevelopmentCardHandler();
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> handler.playMonopolyCard(mockPlayer, mockCard, currentRound, Resource.ORE, null));

    EasyMock.verify(mockPlayer, mockCard);
    assertEquals("Other players list cannot be null.", exception.getMessage());
  }

  // TC28: resource = BRICK, otherPlayers = [] (empty list)
  //       -> no resources transferred; card removed from hand
  @Test
  void playMonopolyCard_EmptyOtherPlayersList_ExpectNoTransferAndCardRemoved() {
    final int currentRound = 2;

    Player mockPlayer = EasyMock.createMock(Player.class);
    DevelopmentCard mockCard = EasyMock.createMock(DevelopmentCard.class);

    EasyMock.expect(mockCard.getType()).andReturn(DevelopmentCardType.MONOPOLY);
    EasyMock.expect(mockCard.isPlayable(currentRound)).andReturn(true);
    EasyMock.expect(mockPlayer.hasPlayedDevCardThisTurn()).andReturn(false);
    mockPlayer.removeDevelopmentCard(mockCard);
    mockPlayer.setHasPlayedDevCardThisTurn(true);

    EasyMock.replay(mockPlayer, mockCard);

    DevelopmentCardHandler handler = new DevelopmentCardHandler();
    handler.playMonopolyCard(mockPlayer, mockCard, currentRound, Resource.BRICK, List.of());

    EasyMock.verify(mockPlayer, mockCard);
  }

  // TC29: resource = ORE, 1 opponent has 5 ORE
  //       -> opponent loses 5 ORE; player gains 5 ORE; card removed from hand
  @Test
  void playMonopolyCard_OneOpponentHasFiveOre_ExpectAllOreTransferred() {
    final int currentRound = 2;

    Player mockPlayer = EasyMock.createMock(Player.class);
    DevelopmentCard mockCard = EasyMock.createMock(DevelopmentCard.class);
    Player mockOpponent = EasyMock.createMock(Player.class);

    EasyMock.expect(mockCard.getType()).andReturn(DevelopmentCardType.MONOPOLY);
    EasyMock.expect(mockCard.isPlayable(currentRound)).andReturn(true);
    EasyMock.expect(mockPlayer.hasPlayedDevCardThisTurn()).andReturn(false);
    EasyMock.expect(mockOpponent.getResourceCount(Resource.ORE)).andReturn(5);
    mockOpponent.updateResources(Resource.ORE, -5);
    mockPlayer.updateResources(Resource.ORE, 5);
    mockPlayer.removeDevelopmentCard(mockCard);
    mockPlayer.setHasPlayedDevCardThisTurn(true);

    EasyMock.replay(mockPlayer, mockCard, mockOpponent);

    DevelopmentCardHandler handler = new DevelopmentCardHandler();
    handler.playMonopolyCard(mockPlayer, mockCard, currentRound, Resource.ORE, List.of(mockOpponent));

    EasyMock.verify(mockPlayer, mockCard, mockOpponent);
  }
}
