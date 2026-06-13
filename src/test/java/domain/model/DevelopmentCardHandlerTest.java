package domain.model;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import domain.model.developmentcards.DevelopmentCard;
import domain.model.developmentcards.DevelopmentCardDeck;
import domain.model.exceptions.EmptyDeckException;
import domain.model.player.Player;
import domain.model.resources.Resource;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import domain.model.developmentcards.DevelopmentCardType;
import domain.model.exceptions.InsufficientResourcesException;
import domain.model.gamepieces.Robber;

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

  // TC30: resource = WOOL, 1 opponent has 0 WOOL
  //       -> no WOOL transferred; card removed from hand
  @Test
  void playMonopolyCard_OneOpponentHasZeroWool_ExpectNoTransferAndCardRemoved() {
    final int currentRound = 2;

    Player mockPlayer = EasyMock.createMock(Player.class);
    DevelopmentCard mockCard = EasyMock.createMock(DevelopmentCard.class);
    Player mockOpponent = EasyMock.createMock(Player.class);

    EasyMock.expect(mockCard.getType()).andReturn(DevelopmentCardType.MONOPOLY);
    EasyMock.expect(mockCard.isPlayable(currentRound)).andReturn(true);
    EasyMock.expect(mockPlayer.hasPlayedDevCardThisTurn()).andReturn(false);
    EasyMock.expect(mockOpponent.getResourceCount(Resource.WOOL)).andReturn(0);
    mockPlayer.removeDevelopmentCard(mockCard);
    mockPlayer.setHasPlayedDevCardThisTurn(true);

    EasyMock.replay(mockPlayer, mockCard, mockOpponent);

    DevelopmentCardHandler handler = new DevelopmentCardHandler();
    handler.playMonopolyCard(mockPlayer, mockCard, currentRound, Resource.WOOL, List.of(mockOpponent));

    EasyMock.verify(mockPlayer, mockCard, mockOpponent);
  }

  // TC31: resource = GRAIN, 3 opponents have 2, 0, and 4 GRAIN respectively
  //       -> opponents lose 2, 0, 4; player gains 6 GRAIN total; card removed
  @Test
  void playMonopolyCard_ThreeOpponentsWithVaryingGrain_ExpectAllGrainTransferred() {
    final int currentRound = 2;

    Player mockPlayer = EasyMock.createMock(Player.class);
    DevelopmentCard mockCard = EasyMock.createMock(DevelopmentCard.class);
    Player mockOpponent1 = EasyMock.createMock(Player.class);
    Player mockOpponent2 = EasyMock.createMock(Player.class);
    Player mockOpponent3 = EasyMock.createMock(Player.class);

    EasyMock.expect(mockCard.getType()).andReturn(DevelopmentCardType.MONOPOLY);
    EasyMock.expect(mockCard.isPlayable(currentRound)).andReturn(true);
    EasyMock.expect(mockPlayer.hasPlayedDevCardThisTurn()).andReturn(false);
    EasyMock.expect(mockOpponent1.getResourceCount(Resource.GRAIN)).andReturn(2);
    mockOpponent1.updateResources(Resource.GRAIN, -2);
    mockPlayer.updateResources(Resource.GRAIN, 2);
    EasyMock.expect(mockOpponent2.getResourceCount(Resource.GRAIN)).andReturn(0);
    EasyMock.expect(mockOpponent3.getResourceCount(Resource.GRAIN)).andReturn(4);
    mockOpponent3.updateResources(Resource.GRAIN, -4);
    mockPlayer.updateResources(Resource.GRAIN, 4);
    mockPlayer.removeDevelopmentCard(mockCard);
    mockPlayer.setHasPlayedDevCardThisTurn(true);

    EasyMock.replay(mockPlayer, mockCard, mockOpponent1, mockOpponent2, mockOpponent3);

    DevelopmentCardHandler handler = new DevelopmentCardHandler();
    handler.playMonopolyCard(mockPlayer, mockCard, currentRound, Resource.GRAIN,
        List.of(mockOpponent1, mockOpponent2, mockOpponent3));

    EasyMock.verify(mockPlayer, mockCard, mockOpponent1, mockOpponent2, mockOpponent3);
  }

  // TC32: resource = LUMBER, 1 opponent has 1 LUMBER (minimum transferable)
  //       -> opponent loses 1 LUMBER; player gains 1 LUMBER; card removed
  @Test
  void playMonopolyCard_OneOpponentHasOneLumber_ExpectOneLumberTransferred() {
    final int currentRound = 2;

    Player mockPlayer = EasyMock.createMock(Player.class);
    DevelopmentCard mockCard = EasyMock.createMock(DevelopmentCard.class);
    Player mockOpponent = EasyMock.createMock(Player.class);

    EasyMock.expect(mockCard.getType()).andReturn(DevelopmentCardType.MONOPOLY);
    EasyMock.expect(mockCard.isPlayable(currentRound)).andReturn(true);
    EasyMock.expect(mockPlayer.hasPlayedDevCardThisTurn()).andReturn(false);
    EasyMock.expect(mockOpponent.getResourceCount(Resource.LUMBER)).andReturn(1);
    mockOpponent.updateResources(Resource.LUMBER, -1);
    mockPlayer.updateResources(Resource.LUMBER, 1);
    mockPlayer.removeDevelopmentCard(mockCard);
    mockPlayer.setHasPlayedDevCardThisTurn(true);

    EasyMock.replay(mockPlayer, mockCard, mockOpponent);

    DevelopmentCardHandler handler = new DevelopmentCardHandler();
    handler.playMonopolyCard(mockPlayer, mockCard, currentRound, Resource.LUMBER, List.of(mockOpponent));

    EasyMock.verify(mockPlayer, mockCard, mockOpponent);
  }

  // TC33: card = null
  //       -> IllegalArgumentException: "Development card cannot be null."
  @Test
  void playRoadBuildingCard_CardIsNull_ExpectIllegalArgumentException() {
    final int currentRound = 1;

    Player mockPlayer = EasyMock.createMock(Player.class);
    GameModel mockModel = EasyMock.createMock(GameModel.class);

    EasyMock.replay(mockPlayer, mockModel);

    DevelopmentCardHandler handler = new DevelopmentCardHandler();
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> handler.playRoadBuildingCard(mockPlayer, null, currentRound, mockModel, 0, 1, 1, 2));

    EasyMock.verify(mockPlayer, mockModel);
    assertEquals("Development card cannot be null.", exception.getMessage());
  }

  // TC34: card type = VICTORY_POINT (not ROAD_BUILDER)
  //       -> IllegalArgumentException: "Card is not a Road Builder card."
  @Test
  void playRoadBuildingCard_CardTypeIsNotRoadBuilder_ExpectIllegalArgumentException() {
    final int currentRound = 1;

    Player mockPlayer = EasyMock.createMock(Player.class);
    DevelopmentCard mockCard = EasyMock.createMock(DevelopmentCard.class);
    GameModel mockModel = EasyMock.createMock(GameModel.class);

    EasyMock.expect(mockCard.getType()).andReturn(DevelopmentCardType.VICTORY_POINT);

    EasyMock.replay(mockPlayer, mockCard, mockModel);

    DevelopmentCardHandler handler = new DevelopmentCardHandler();
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> handler.playRoadBuildingCard(mockPlayer, mockCard, currentRound, mockModel, 0, 1, 1, 2));

    EasyMock.verify(mockPlayer, mockCard, mockModel);
    assertEquals("Card is not a Road Builder card.", exception.getMessage());
  }

  // TC35: card drawn this round (not playable)
  //       -> IllegalStateException: "Card cannot be played the same turn it was purchased."
  @Test
  void playRoadBuildingCard_CardNotPlayableSameTurn_ExpectIllegalStateException() {
    final int currentRound = 1;

    Player mockPlayer = EasyMock.createMock(Player.class);
    DevelopmentCard mockCard = EasyMock.createMock(DevelopmentCard.class);
    GameModel mockModel = EasyMock.createMock(GameModel.class);

    EasyMock.expect(mockCard.getType()).andReturn(DevelopmentCardType.ROAD_BUILDER);
    EasyMock.expect(mockCard.isPlayable(currentRound)).andReturn(false);

    EasyMock.replay(mockPlayer, mockCard, mockModel);

    DevelopmentCardHandler handler = new DevelopmentCardHandler();
    Exception exception = assertThrows(IllegalStateException.class,
        () -> handler.playRoadBuildingCard(mockPlayer, mockCard, currentRound, mockModel, 0, 1, 1, 2));

    EasyMock.verify(mockPlayer, mockCard, mockModel);
    assertEquals("Card cannot be played the same turn it was purchased.", exception.getMessage());
  }

  // TC36: player already played a dev card this turn
  //       -> IllegalStateException: "Already played a development card this turn."
  @Test
  void playRoadBuildingCard_AlreadyPlayedDevCardThisTurn_ExpectIllegalStateException() {
    final int currentRound = 1;

    Player mockPlayer = EasyMock.createMock(Player.class);
    DevelopmentCard mockCard = EasyMock.createMock(DevelopmentCard.class);
    GameModel mockModel = EasyMock.createMock(GameModel.class);

    EasyMock.expect(mockCard.getType()).andReturn(DevelopmentCardType.ROAD_BUILDER);
    EasyMock.expect(mockCard.isPlayable(currentRound)).andReturn(true);
    EasyMock.expect(mockPlayer.hasPlayedDevCardThisTurn()).andReturn(true);

    EasyMock.replay(mockPlayer, mockCard, mockModel);

    DevelopmentCardHandler handler = new DevelopmentCardHandler();
    Exception exception = assertThrows(IllegalStateException.class,
        () -> handler.playRoadBuildingCard(mockPlayer, mockCard, currentRound, mockModel, 0, 1, 1, 2));

    EasyMock.verify(mockPlayer, mockCard, mockModel);
    assertEquals("Already played a development card this turn.", exception.getMessage());
  }

  // TC37: road1 node IDs out of bounds -> boardHandler.addRoad throws IllegalArgumentException
  //       -> IllegalArgumentException relayed
  @Test
  void playRoadBuildingCard_Road1NodeOutOfBounds_ExpectIllegalArgumentExceptionRelayed() {
    final int currentRound = 2;

    Player mockPlayer = EasyMock.createMock(Player.class);
    DevelopmentCard mockCard = EasyMock.createMock(DevelopmentCard.class);
    GameModel mockModel = EasyMock.createMock(GameModel.class);

    EasyMock.expect(mockCard.getType()).andReturn(DevelopmentCardType.ROAD_BUILDER);
    EasyMock.expect(mockCard.isPlayable(currentRound)).andReturn(true);
    EasyMock.expect(mockPlayer.hasPlayedDevCardThisTurn()).andReturn(false);
    mockModel.attemptBuildRoad(-1, 1);
    EasyMock.expectLastCall().andThrow(new IllegalArgumentException("Edge nodeId out of bounds. Must be within [0, 53]."));

    EasyMock.replay(mockPlayer, mockCard, mockModel);

    DevelopmentCardHandler handler = new DevelopmentCardHandler();
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> handler.playRoadBuildingCard(mockPlayer, mockCard, currentRound, mockModel, -1, 1, 1, 2));

    EasyMock.verify(mockPlayer, mockCard, mockModel);
    assertEquals("Edge nodeId out of bounds. Must be within [0, 53].", exception.getMessage());
  }

  // TC38: road1 valid, road2 valid, roads placed = 0 (15 remaining)
  //       -> 2 roads placed; card removed
  @Test
  void playRoadBuildingCard_TwoValidRoads_ExpectTwoRoadsPlacedAndCardRemoved() {
    final int currentRound = 2;

    Player mockPlayer = EasyMock.createMock(Player.class);
    DevelopmentCard mockCard = EasyMock.createMock(DevelopmentCard.class);
    GameModel mockModel = EasyMock.createMock(GameModel.class);

    EasyMock.expect(mockCard.getType()).andReturn(DevelopmentCardType.ROAD_BUILDER);
    EasyMock.expect(mockCard.isPlayable(currentRound)).andReturn(true);
    EasyMock.expect(mockPlayer.hasPlayedDevCardThisTurn()).andReturn(false);
    mockModel.attemptBuildRoad(0, 1);
    mockModel.attemptBuildRoad(1, 2);
    mockPlayer.removeDevelopmentCard(mockCard);
    mockPlayer.setHasPlayedDevCardThisTurn(true);

    EasyMock.replay(mockPlayer, mockCard, mockModel);

    DevelopmentCardHandler handler = new DevelopmentCardHandler();
    handler.playRoadBuildingCard(mockPlayer, mockCard, currentRound, mockModel, 0, 1, 1, 2);

    EasyMock.verify(mockPlayer, mockCard, mockModel);
  }

  // TC39: road1 valid, road2 valid, roads placed = 13 (2 remaining — last pair that fits)
  //       -> 2 roads placed; card removed
  @Test
  void playRoadBuildingCard_TwoValidRoadsLastPairThatFits_ExpectTwoRoadsPlacedAndCardRemoved() {
    final int currentRound = 2;

    Player mockPlayer = EasyMock.createMock(Player.class);
    DevelopmentCard mockCard = EasyMock.createMock(DevelopmentCard.class);
    GameModel mockModel = EasyMock.createMock(GameModel.class);

    EasyMock.expect(mockCard.getType()).andReturn(DevelopmentCardType.ROAD_BUILDER);
    EasyMock.expect(mockCard.isPlayable(currentRound)).andReturn(true);
    EasyMock.expect(mockPlayer.hasPlayedDevCardThisTurn()).andReturn(false);
    mockModel.attemptBuildRoad(0, 1);
    mockModel.attemptBuildRoad(1, 2);
    mockPlayer.removeDevelopmentCard(mockCard);
    mockPlayer.setHasPlayedDevCardThisTurn(true);

    EasyMock.replay(mockPlayer, mockCard, mockModel);

    DevelopmentCardHandler handler = new DevelopmentCardHandler();
    handler.playRoadBuildingCard(mockPlayer, mockCard, currentRound, mockModel, 0, 1, 1, 2);

    EasyMock.verify(mockPlayer, mockCard, mockModel);
  }

  // TC40: road1 valid, road2 = null, roads placed = 14 (only 1 remaining)
  //       -> 1 road placed; card removed
  @Test
  void playRoadBuildingCard_Road2NullOnlyOneRoadRemaining_ExpectOneRoadPlacedAndCardRemoved() {
    final int currentRound = 2;

    Player mockPlayer = EasyMock.createMock(Player.class);
    DevelopmentCard mockCard = EasyMock.createMock(DevelopmentCard.class);
    GameModel mockModel = EasyMock.createMock(GameModel.class);

    EasyMock.expect(mockCard.getType()).andReturn(DevelopmentCardType.ROAD_BUILDER);
    EasyMock.expect(mockCard.isPlayable(currentRound)).andReturn(true);
    EasyMock.expect(mockPlayer.hasPlayedDevCardThisTurn()).andReturn(false);
    mockModel.attemptBuildRoad(0, 1);
    mockPlayer.removeDevelopmentCard(mockCard);
    mockPlayer.setHasPlayedDevCardThisTurn(true);

    EasyMock.replay(mockPlayer, mockCard, mockModel);

    DevelopmentCardHandler handler = new DevelopmentCardHandler();
    handler.playRoadBuildingCard(mockPlayer, mockCard, currentRound, mockModel, 0, 1, null, null);

    EasyMock.verify(mockPlayer, mockCard, mockModel);
  }

  // TC41: roads placed = 15 (no roads remaining)
  //       -> IllegalStateException: "No roads remaining."
  @Test
  void playRoadBuildingCard_NoRoadsRemaining_ExpectIllegalStateException() {
    final int currentRound = 2;

    Player mockPlayer = EasyMock.createMock(Player.class);
    DevelopmentCard mockCard = EasyMock.createMock(DevelopmentCard.class);
    GameModel mockModel = EasyMock.createMock(GameModel.class);

    EasyMock.expect(mockCard.getType()).andReturn(DevelopmentCardType.ROAD_BUILDER);
    EasyMock.expect(mockCard.isPlayable(currentRound)).andReturn(true);
    EasyMock.expect(mockPlayer.hasPlayedDevCardThisTurn()).andReturn(false);
    mockModel.attemptBuildRoad(0, 1);
    EasyMock.expectLastCall().andThrow(new IllegalStateException("No roads remaining."));

    EasyMock.replay(mockPlayer, mockCard, mockModel);

    DevelopmentCardHandler handler = new DevelopmentCardHandler();
    Exception exception = assertThrows(IllegalStateException.class,
        () -> handler.playRoadBuildingCard(mockPlayer, mockCard, currentRound, mockModel, 0, 1, 1, 2));

    EasyMock.verify(mockPlayer, mockCard, mockModel);
    assertEquals("No roads remaining.", exception.getMessage());
  }

  // TC42: road1 edge is already occupied, roads placed = 0
  //       -> IllegalArgumentException: "Edge is already occupied."
  @Test
  void playRoadBuildingCard_Road1AlreadyOccupied_ExpectIllegalArgumentException() {
    final int currentRound = 2;

    Player mockPlayer = EasyMock.createMock(Player.class);
    DevelopmentCard mockCard = EasyMock.createMock(DevelopmentCard.class);
    GameModel mockModel = EasyMock.createMock(GameModel.class);

    EasyMock.expect(mockCard.getType()).andReturn(DevelopmentCardType.ROAD_BUILDER);
    EasyMock.expect(mockCard.isPlayable(currentRound)).andReturn(true);
    EasyMock.expect(mockPlayer.hasPlayedDevCardThisTurn()).andReturn(false);
    mockModel.attemptBuildRoad(0, 1);
    EasyMock.expectLastCall().andThrow(new IllegalArgumentException("Edge is already occupied."));

    EasyMock.replay(mockPlayer, mockCard, mockModel);

    DevelopmentCardHandler handler = new DevelopmentCardHandler();
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> handler.playRoadBuildingCard(mockPlayer, mockCard, currentRound, mockModel, 0, 1, 1, 2));

    EasyMock.verify(mockPlayer, mockCard, mockModel);
    assertEquals("Edge is already occupied.", exception.getMessage());
  }

  // TC43: road1 not connected to player's network, roads placed = 0
  //       -> IllegalArgumentException: "Road must connect to player's existing network."
  @Test
  void playRoadBuildingCard_Road1NotConnected_ExpectIllegalArgumentException() {
    final int currentRound = 2;

    Player mockPlayer = EasyMock.createMock(Player.class);
    DevelopmentCard mockCard = EasyMock.createMock(DevelopmentCard.class);
    GameModel mockModel = EasyMock.createMock(GameModel.class);

    EasyMock.expect(mockCard.getType()).andReturn(DevelopmentCardType.ROAD_BUILDER);
    EasyMock.expect(mockCard.isPlayable(currentRound)).andReturn(true);
    EasyMock.expect(mockPlayer.hasPlayedDevCardThisTurn()).andReturn(false);
    mockModel.attemptBuildRoad(0, 1);
    EasyMock.expectLastCall().andThrow(new IllegalArgumentException("Road must connect to player's existing network."));

    EasyMock.replay(mockPlayer, mockCard, mockModel);

    DevelopmentCardHandler handler = new DevelopmentCardHandler();
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> handler.playRoadBuildingCard(mockPlayer, mockCard, currentRound, mockModel, 0, 1, 1, 2));

    EasyMock.verify(mockPlayer, mockCard, mockModel);
    assertEquals("Road must connect to player's existing network.", exception.getMessage());
  }

  // TC44: road1 valid, road2 edge is already occupied, roads placed = 0
  //       -> IllegalArgumentException: "Edge is already occupied."
  @Test
  void playRoadBuildingCard_Road2AlreadyOccupied_ExpectIllegalArgumentException() {
    final int currentRound = 2;

    Player mockPlayer = EasyMock.createMock(Player.class);
    DevelopmentCard mockCard = EasyMock.createMock(DevelopmentCard.class);
    GameModel mockModel = EasyMock.createMock(GameModel.class);

    EasyMock.expect(mockCard.getType()).andReturn(DevelopmentCardType.ROAD_BUILDER);
    EasyMock.expect(mockCard.isPlayable(currentRound)).andReturn(true);
    EasyMock.expect(mockPlayer.hasPlayedDevCardThisTurn()).andReturn(false);
    mockModel.attemptBuildRoad(0, 1);
    mockModel.attemptBuildRoad(1, 2);
    EasyMock.expectLastCall().andThrow(new IllegalArgumentException("Edge is already occupied."));

    EasyMock.replay(mockPlayer, mockCard, mockModel);

    DevelopmentCardHandler handler = new DevelopmentCardHandler();
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> handler.playRoadBuildingCard(mockPlayer, mockCard, currentRound, mockModel, 0, 1, 1, 2));

    EasyMock.verify(mockPlayer, mockCard, mockModel);
    assertEquals("Edge is already occupied.", exception.getMessage());
  }

  // TC45: road1 valid, road2 not connected to player's network (including road1), roads placed = 0
  //       -> IllegalArgumentException: "Road must connect to player's existing network."
  @Test
  void playRoadBuildingCard_Road2NotConnected_ExpectIllegalArgumentException() {
    final int currentRound = 2;

    Player mockPlayer = EasyMock.createMock(Player.class);
    DevelopmentCard mockCard = EasyMock.createMock(DevelopmentCard.class);
    GameModel mockModel = EasyMock.createMock(GameModel.class);

    EasyMock.expect(mockCard.getType()).andReturn(DevelopmentCardType.ROAD_BUILDER);
    EasyMock.expect(mockCard.isPlayable(currentRound)).andReturn(true);
    EasyMock.expect(mockPlayer.hasPlayedDevCardThisTurn()).andReturn(false);
    mockModel.attemptBuildRoad(0, 1);
    mockModel.attemptBuildRoad(1, 2);
    EasyMock.expectLastCall().andThrow(new IllegalArgumentException("Road must connect to player's existing network."));

    EasyMock.replay(mockPlayer, mockCard, mockModel);

    DevelopmentCardHandler handler = new DevelopmentCardHandler();
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> handler.playRoadBuildingCard(mockPlayer, mockCard, currentRound, mockModel, 0, 1, 1, 2));

    EasyMock.verify(mockPlayer, mockCard, mockModel);
    assertEquals("Road must connect to player's existing network.", exception.getMessage());
  }

  // TC46: card = null
  //       -> IllegalArgumentException: "Development card cannot be null."
  @Test
  void playYearOfPlentyCard_CardIsNull_ExpectIllegalArgumentException() {
    final int currentRound = 3;

    Player mockPlayer = EasyMock.createMock(Player.class);

    EasyMock.replay(mockPlayer);

    DevelopmentCardHandler handler = new DevelopmentCardHandler();
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> handler.playYearOfPlentyCard(mockPlayer, null, currentRound, Resource.BRICK, Resource.WOOL));

    EasyMock.verify(mockPlayer);
    assertEquals("Development card cannot be null.", exception.getMessage());
  }

  // TC47: card type = KNIGHT (not YEAR_OF_PLENTY)
  //       -> IllegalArgumentException: "Card is not a Year of Plenty card."
  @Test
  void playYearOfPlentyCard_CardTypeIsKnight_ExpectIllegalArgumentException() {
    final int currentRound = 3;

    Player mockPlayer = EasyMock.createMock(Player.class);
    DevelopmentCard mockCard = EasyMock.createMock(DevelopmentCard.class);

    EasyMock.expect(mockCard.getType()).andReturn(DevelopmentCardType.KNIGHT);

    EasyMock.replay(mockPlayer, mockCard);

    DevelopmentCardHandler handler = new DevelopmentCardHandler();
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> handler.playYearOfPlentyCard(mockPlayer, mockCard, currentRound, Resource.BRICK, Resource.WOOL));

    EasyMock.verify(mockPlayer, mockCard);
    assertEquals("Card is not a Year of Plenty card.", exception.getMessage());
  }

  // TC48: card drawn this round (not playable, same turn)
  //       -> IllegalStateException: "Card cannot be played the same turn it was purchased."
  @Test
  void playYearOfPlentyCard_CardDrawnThisRound_ExpectIllegalStateException() {
    final int currentRound = 3;

    Player mockPlayer = EasyMock.createMock(Player.class);
    DevelopmentCard mockCard = EasyMock.createMock(DevelopmentCard.class);

    EasyMock.expect(mockCard.getType()).andReturn(DevelopmentCardType.YEAR_OF_PLENTY);
    EasyMock.expect(mockCard.isPlayable(currentRound)).andReturn(false);

    EasyMock.replay(mockPlayer, mockCard);

    DevelopmentCardHandler handler = new DevelopmentCardHandler();
    Exception exception = assertThrows(IllegalStateException.class,
        () -> handler.playYearOfPlentyCard(mockPlayer, mockCard, currentRound, Resource.BRICK, Resource.WOOL));

    EasyMock.verify(mockPlayer, mockCard);
    assertEquals("Card cannot be played the same turn it was purchased.", exception.getMessage());
  }

  // TC49: player already played a dev card this turn
  //       -> IllegalStateException: "Already played a development card this turn."
  @Test
  void playYearOfPlentyCard_AlreadyPlayedDevCard_ExpectIllegalStateException() {
    final int currentRound = 3;

    Player mockPlayer = EasyMock.createMock(Player.class);
    DevelopmentCard mockCard = EasyMock.createMock(DevelopmentCard.class);

    EasyMock.expect(mockCard.getType()).andReturn(DevelopmentCardType.YEAR_OF_PLENTY);
    EasyMock.expect(mockCard.isPlayable(currentRound)).andReturn(true);
    EasyMock.expect(mockPlayer.hasPlayedDevCardThisTurn()).andReturn(true);

    EasyMock.replay(mockPlayer, mockCard);

    DevelopmentCardHandler handler = new DevelopmentCardHandler();
    Exception exception = assertThrows(IllegalStateException.class,
        () -> handler.playYearOfPlentyCard(mockPlayer, mockCard, currentRound, Resource.BRICK, Resource.WOOL));

    EasyMock.verify(mockPlayer, mockCard);
    assertEquals("Already played a development card this turn.", exception.getMessage());
  }

  // TC50: resource1 = null, resource2 = BRICK
  //       -> IllegalArgumentException: "Resource cannot be null."
  @Test
  void playYearOfPlentyCard_Resource1IsNull_ExpectIllegalArgumentException() {
    final int currentRound = 3;

    Player mockPlayer = EasyMock.createMock(Player.class);
    DevelopmentCard mockCard = EasyMock.createMock(DevelopmentCard.class);

    EasyMock.expect(mockCard.getType()).andReturn(DevelopmentCardType.YEAR_OF_PLENTY);
    EasyMock.expect(mockCard.isPlayable(currentRound)).andReturn(true);
    EasyMock.expect(mockPlayer.hasPlayedDevCardThisTurn()).andReturn(false);

    EasyMock.replay(mockPlayer, mockCard);

    DevelopmentCardHandler handler = new DevelopmentCardHandler();
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> handler.playYearOfPlentyCard(mockPlayer, mockCard, currentRound, null, Resource.BRICK));

    EasyMock.verify(mockPlayer, mockCard);
    assertEquals("Resource cannot be null.", exception.getMessage());
  }

  // TC51: resource1 = BRICK, resource2 = null
  //       -> IllegalArgumentException: "Resource cannot be null."
  @Test
  void playYearOfPlentyCard_Resource2IsNull_ExpectIllegalArgumentException() {
    final int currentRound = 3;

    Player mockPlayer = EasyMock.createMock(Player.class);
    DevelopmentCard mockCard = EasyMock.createMock(DevelopmentCard.class);

    EasyMock.expect(mockCard.getType()).andReturn(DevelopmentCardType.YEAR_OF_PLENTY);
    EasyMock.expect(mockCard.isPlayable(currentRound)).andReturn(true);
    EasyMock.expect(mockPlayer.hasPlayedDevCardThisTurn()).andReturn(false);

    EasyMock.replay(mockPlayer, mockCard);

    DevelopmentCardHandler handler = new DevelopmentCardHandler();
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> handler.playYearOfPlentyCard(mockPlayer, mockCard, currentRound, Resource.BRICK, null));

    EasyMock.verify(mockPlayer, mockCard);
    assertEquals("Resource cannot be null.", exception.getMessage());
  }

  // TC52: resource1 = DESERT, resource2 = ORE
  //       -> IllegalArgumentException: "Cannot take DESERT as a resource."
  @Test
  void playYearOfPlentyCard_Resource1IsDesert_ExpectIllegalArgumentException() {
    final int currentRound = 3;

    Player mockPlayer = EasyMock.createMock(Player.class);
    DevelopmentCard mockCard = EasyMock.createMock(DevelopmentCard.class);

    EasyMock.expect(mockCard.getType()).andReturn(DevelopmentCardType.YEAR_OF_PLENTY);
    EasyMock.expect(mockCard.isPlayable(currentRound)).andReturn(true);
    EasyMock.expect(mockPlayer.hasPlayedDevCardThisTurn()).andReturn(false);

    EasyMock.replay(mockPlayer, mockCard);

    DevelopmentCardHandler handler = new DevelopmentCardHandler();
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> handler.playYearOfPlentyCard(mockPlayer, mockCard, currentRound, Resource.DESERT, Resource.ORE));

    EasyMock.verify(mockPlayer, mockCard);
    assertEquals("Cannot take DESERT as a resource.", exception.getMessage());
  }

  // TC53: resource1 = LUMBER, resource2 = DESERT
  //       -> IllegalArgumentException: "Cannot take DESERT as a resource."
  @Test
  void playYearOfPlentyCard_Resource2IsDesert_ExpectIllegalArgumentException() {
    final int currentRound = 3;

    Player mockPlayer = EasyMock.createMock(Player.class);
    DevelopmentCard mockCard = EasyMock.createMock(DevelopmentCard.class);

    EasyMock.expect(mockCard.getType()).andReturn(DevelopmentCardType.YEAR_OF_PLENTY);
    EasyMock.expect(mockCard.isPlayable(currentRound)).andReturn(true);
    EasyMock.expect(mockPlayer.hasPlayedDevCardThisTurn()).andReturn(false);

    EasyMock.replay(mockPlayer, mockCard);

    DevelopmentCardHandler handler = new DevelopmentCardHandler();
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> handler.playYearOfPlentyCard(mockPlayer, mockCard, currentRound, Resource.LUMBER, Resource.DESERT));

    EasyMock.verify(mockPlayer, mockCard);
    assertEquals("Cannot take DESERT as a resource.", exception.getMessage());
  }

  // TC54: resource1 = ORE, resource2 = ORE (same type)
  //       -> player gains 2 ORE; card removed from hand
  @Test
  void playYearOfPlentyCard_SameResourceType_ExpectBothTransferred() {
    final int currentRound = 3;

    Player mockPlayer = EasyMock.createMock(Player.class);
    DevelopmentCard mockCard = EasyMock.createMock(DevelopmentCard.class);

    EasyMock.expect(mockCard.getType()).andReturn(DevelopmentCardType.YEAR_OF_PLENTY);
    EasyMock.expect(mockCard.isPlayable(currentRound)).andReturn(true);
    EasyMock.expect(mockPlayer.hasPlayedDevCardThisTurn()).andReturn(false);
    mockPlayer.updateResources(Resource.ORE, 1);
    mockPlayer.updateResources(Resource.ORE, 1);
    mockPlayer.removeDevelopmentCard(mockCard);
    mockPlayer.setHasPlayedDevCardThisTurn(true);

    EasyMock.replay(mockPlayer, mockCard);

    DevelopmentCardHandler handler = new DevelopmentCardHandler();
    handler.playYearOfPlentyCard(mockPlayer, mockCard, currentRound, Resource.ORE, Resource.ORE);

    EasyMock.verify(mockPlayer, mockCard);
  }

  // TC55: resource1 = BRICK, resource2 = WOOL (different types)
  //       -> player gains 1 BRICK and 1 WOOL; card removed from hand
  @Test
  void playYearOfPlentyCard_DifferentResourceTypes_ExpectBothTransferred() {
    final int currentRound = 3;

    Player mockPlayer = EasyMock.createMock(Player.class);
    DevelopmentCard mockCard = EasyMock.createMock(DevelopmentCard.class);

    EasyMock.expect(mockCard.getType()).andReturn(DevelopmentCardType.YEAR_OF_PLENTY);
    EasyMock.expect(mockCard.isPlayable(currentRound)).andReturn(true);
    EasyMock.expect(mockPlayer.hasPlayedDevCardThisTurn()).andReturn(false);
    mockPlayer.updateResources(Resource.BRICK, 1);
    mockPlayer.updateResources(Resource.WOOL, 1);
    mockPlayer.removeDevelopmentCard(mockCard);
    mockPlayer.setHasPlayedDevCardThisTurn(true);

    EasyMock.replay(mockPlayer, mockCard);

    DevelopmentCardHandler handler = new DevelopmentCardHandler();
    handler.playYearOfPlentyCard(mockPlayer, mockCard, currentRound, Resource.BRICK, Resource.WOOL);

    EasyMock.verify(mockPlayer, mockCard);
  }

  // TC56: hand is empty -> 0
  @Test
  void countVictoryPointCards_EmptyHand_ExpectZero() {
    final int expectedCount = 0;

    DevelopmentCardHandler handler = new DevelopmentCardHandler();
    assertEquals(expectedCount, handler.countVictoryPointCards(List.of()));
  }

  // TC57: hand contains 1 VICTORY_POINT card only -> 1
  @Test
  void countVictoryPointCards_OneVictoryPointCard_ExpectOne() {
    final int expectedCount = 1;

    DevelopmentCard mockCard = EasyMock.createMock(DevelopmentCard.class);
    EasyMock.expect(mockCard.getType()).andReturn(DevelopmentCardType.VICTORY_POINT);
    EasyMock.replay(mockCard);

    DevelopmentCardHandler handler = new DevelopmentCardHandler();
    assertEquals(expectedCount, handler.countVictoryPointCards(List.of(mockCard)));

    EasyMock.verify(mockCard);
  }

  // TC58: hand contains 3 KNIGHT and 2 VICTORY_POINT cards -> 2
  @Test
  void countVictoryPointCards_MixedCardsWithTwoVP_ExpectTwo() {
    final int expectedCount = 2;

    DevelopmentCard mockKnight1 = EasyMock.createMock(DevelopmentCard.class);
    DevelopmentCard mockKnight2 = EasyMock.createMock(DevelopmentCard.class);
    DevelopmentCard mockKnight3 = EasyMock.createMock(DevelopmentCard.class);
    DevelopmentCard mockVP1 = EasyMock.createMock(DevelopmentCard.class);
    DevelopmentCard mockVP2 = EasyMock.createMock(DevelopmentCard.class);

    EasyMock.expect(mockKnight1.getType()).andReturn(DevelopmentCardType.KNIGHT);
    EasyMock.expect(mockKnight2.getType()).andReturn(DevelopmentCardType.KNIGHT);
    EasyMock.expect(mockKnight3.getType()).andReturn(DevelopmentCardType.KNIGHT);
    EasyMock.expect(mockVP1.getType()).andReturn(DevelopmentCardType.VICTORY_POINT);
    EasyMock.expect(mockVP2.getType()).andReturn(DevelopmentCardType.VICTORY_POINT);

    EasyMock.replay(mockKnight1, mockKnight2, mockKnight3, mockVP1, mockVP2);

    DevelopmentCardHandler handler = new DevelopmentCardHandler();
    assertEquals(expectedCount, handler.countVictoryPointCards(
        List.of(mockKnight1, mockKnight2, mockKnight3, mockVP1, mockVP2)));

    EasyMock.verify(mockKnight1, mockKnight2, mockKnight3, mockVP1, mockVP2);
  }

  // TC59: hand contains 5 VICTORY_POINT cards (maximum) -> 5
  @Test
  void countVictoryPointCards_FiveVictoryPointCards_ExpectFive() {
    final int expectedCount = 5;

    DevelopmentCard mockVP1 = EasyMock.createMock(DevelopmentCard.class);
    DevelopmentCard mockVP2 = EasyMock.createMock(DevelopmentCard.class);
    DevelopmentCard mockVP3 = EasyMock.createMock(DevelopmentCard.class);
    DevelopmentCard mockVP4 = EasyMock.createMock(DevelopmentCard.class);
    DevelopmentCard mockVP5 = EasyMock.createMock(DevelopmentCard.class);

    EasyMock.expect(mockVP1.getType()).andReturn(DevelopmentCardType.VICTORY_POINT);
    EasyMock.expect(mockVP2.getType()).andReturn(DevelopmentCardType.VICTORY_POINT);
    EasyMock.expect(mockVP3.getType()).andReturn(DevelopmentCardType.VICTORY_POINT);
    EasyMock.expect(mockVP4.getType()).andReturn(DevelopmentCardType.VICTORY_POINT);
    EasyMock.expect(mockVP5.getType()).andReturn(DevelopmentCardType.VICTORY_POINT);

    EasyMock.replay(mockVP1, mockVP2, mockVP3, mockVP4, mockVP5);

    DevelopmentCardHandler handler = new DevelopmentCardHandler();
    assertEquals(expectedCount, handler.countVictoryPointCards(
        List.of(mockVP1, mockVP2, mockVP3, mockVP4, mockVP5)));

    EasyMock.verify(mockVP1, mockVP2, mockVP3, mockVP4, mockVP5);
  }

  // TC61: victim has {BRICK: 0, WOOL: 1}; the boundary `entry.getValue() > 0` means only
  // WOOL (non-zero) goes into the available list; with the mutant `>= 0` BRICK (zero)
  // would also be included, changing available.size() from 1 to 2 and causing
  // random.nextInt(2) to be called instead of random.nextInt(1), which EasyMock rejects.
  @Test
  void playKnightCard_VictimHasMixedZeroAndNonZeroResources_StealNonZeroResource() {
    final int currentRound = 2;
    final int targetHexId = 5;

    Random mockRandom = EasyMock.createMock(Random.class);
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
    EasyMock.expectLastCall();
    EasyMock.expect(mockVictim.getTotalResourceCount()).andReturn(1);

    // BRICK first so the mutant would include it (0 >= 0), making available.size() == 2
    Map<Resource, Integer> victimResources = new LinkedHashMap<>();
    victimResources.put(Resource.BRICK, 0);
    victimResources.put(Resource.WOOL, 1);
    EasyMock.expect(mockVictim.getResources()).andReturn(victimResources);

    // original: only WOOL is available (size=1), so nextInt(1) is called
    EasyMock.expect(mockRandom.nextInt(1)).andReturn(0);

    mockVictim.updateResources(Resource.WOOL, -1);
    EasyMock.expectLastCall();
    mockPlayer.updateResources(Resource.WOOL, 1);
    EasyMock.expectLastCall();
    mockPlayer.incrementKnightCount();
    EasyMock.expectLastCall();
    mockPlayer.removeDevelopmentCard(mockCard);
    EasyMock.expectLastCall();
    mockPlayer.setHasPlayedDevCardThisTurn(true);
    EasyMock.expectLastCall();

    EasyMock.replay(mockRandom, mockPlayer, mockCard, mockRobber, mockVictim);

    DevelopmentCardHandler handler = new DevelopmentCardHandler(mockRandom);
    handler.playKnightCard(mockPlayer, mockCard, currentRound, mockRobber, targetHexId, mockVictim);

    EasyMock.verify(mockRandom, mockPlayer, mockCard, mockRobber, mockVictim);
  }

  // TC60: hand contains 3 KNIGHT, 1 MONOPOLY, 0 VICTORY_POINT cards -> 0
  @Test
  void countVictoryPointCards_NonVPCardsOnly_ExpectZero() {
    final int expectedCount = 0;

    DevelopmentCard mockKnight1 = EasyMock.createMock(DevelopmentCard.class);
    DevelopmentCard mockKnight2 = EasyMock.createMock(DevelopmentCard.class);
    DevelopmentCard mockKnight3 = EasyMock.createMock(DevelopmentCard.class);
    DevelopmentCard mockMonopoly = EasyMock.createMock(DevelopmentCard.class);

    EasyMock.expect(mockKnight1.getType()).andReturn(DevelopmentCardType.KNIGHT);
    EasyMock.expect(mockKnight2.getType()).andReturn(DevelopmentCardType.KNIGHT);
    EasyMock.expect(mockKnight3.getType()).andReturn(DevelopmentCardType.KNIGHT);
    EasyMock.expect(mockMonopoly.getType()).andReturn(DevelopmentCardType.MONOPOLY);

    EasyMock.replay(mockKnight1, mockKnight2, mockKnight3, mockMonopoly);

    DevelopmentCardHandler handler = new DevelopmentCardHandler();
    assertEquals(expectedCount, handler.countVictoryPointCards(
        List.of(mockKnight1, mockKnight2, mockKnight3, mockMonopoly)));

    EasyMock.verify(mockKnight1, mockKnight2, mockKnight3, mockMonopoly);
  }
}
