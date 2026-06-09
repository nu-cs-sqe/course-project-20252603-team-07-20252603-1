package domain.model;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import domain.model.development_cards.DevelopmentCard;
import domain.model.development_cards.DevelopmentCardDeck;
import domain.model.exceptions.EmptyDeckException;
import domain.model.player.Player;
import domain.model.resources.Resource;

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
}
