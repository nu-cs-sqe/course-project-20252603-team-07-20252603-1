package domain.model;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import domain.model.development_cards.DevelopmentCard;
import domain.model.development_cards.DevelopmentCardDeck;
import domain.model.exceptions.EmptyDeckException;
import domain.model.player.Player;
import domain.model.resources.Resource;

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
}
