package domain.model.player;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import domain.model.resources.Resource;
import domain.model.resources.ResourceQuantity;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

/** Test class. */
public class TradeOfferTests {
  @Test // Test Case 1
  public void Construct_RedBrickForWool_ExpectValid() {
    Player mockRed = EasyMock.createMock(Player.class);
    ResourceQuantity giving = ResourceQuantity.create(Resource.BRICK, 1);
    ResourceQuantity receiving = ResourceQuantity.create(Resource.WOOL, 1);

    assertDoesNotThrow(() -> TradeOffer.create(mockRed, giving, receiving));
  }

  @Test // Test Case 2
  public void Construct_BlueOreForGrain_ExpectValid() {
    Player mockBlue = EasyMock.createMock(Player.class);
    ResourceQuantity giving = ResourceQuantity.create(Resource.ORE, 2);
    ResourceQuantity receiving = ResourceQuantity.create(Resource.GRAIN, 1);

    assertDoesNotThrow(() -> TradeOffer.create(mockBlue, giving, receiving));
  }

  @Test // Test Case 3
  public void Construct_LumberForLumber_ExpectError() {
    Player mockRed = EasyMock.createMock(Player.class);
    ResourceQuantity giving = ResourceQuantity.create(Resource.LUMBER, 1);
    ResourceQuantity receiving = ResourceQuantity.create(Resource.LUMBER, 1);

    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      TradeOffer.create(mockRed, giving, receiving);
    });

    String expectedMessage = "Cannot trade a resource for itself.";
    String actualMessage = exception.getMessage();
    assertEquals(expectedMessage, actualMessage);
  }

  @Test // Test Case 4
  public void Construct_TwoWoolForThreeWool_ExpectError() {
    Player mockWhite = EasyMock.createMock(Player.class);
    ResourceQuantity giving = ResourceQuantity.create(Resource.WOOL, 2);
    ResourceQuantity receiving = ResourceQuantity.create(Resource.WOOL, 3);

    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      TradeOffer.create(mockWhite, giving, receiving);
    });

    String expectedMessage = "Cannot trade a resource for itself.";
    String actualMessage = exception.getMessage();
    assertEquals(expectedMessage, actualMessage);
  }

  @Test // Test Case 5
  public void Construct_OrangeGrainForBrick_ExpectValid() {
    Player mockOrange = EasyMock.createMock(Player.class);
    ResourceQuantity giving = ResourceQuantity.create(Resource.GRAIN, 1);
    ResourceQuantity receiving = ResourceQuantity.create(Resource.BRICK, 1);

    assertDoesNotThrow(() -> TradeOffer.create(mockOrange, giving, receiving));
  }

  @Test // Test Case 6
  public void Create_ReturnValue_ExpectNonNull() {
    Player mockRed = EasyMock.createMock(Player.class);
    ResourceQuantity giving = ResourceQuantity.create(Resource.ORE, 1);
    ResourceQuantity receiving = ResourceQuantity.create(Resource.WOOL, 1);

    TradeOffer offer = TradeOffer.create(mockRed, giving, receiving);
    assertNotNull(offer);
  }

  @Test // Test Case 7
  public void GetOfferingPlayer_ExpectSamePlayerInstance() {
    Player mockRed = EasyMock.createMock(Player.class);
    ResourceQuantity giving = ResourceQuantity.create(Resource.LUMBER, 1);
    ResourceQuantity receiving = ResourceQuantity.create(Resource.GRAIN, 1);

    TradeOffer offer = TradeOffer.create(mockRed, giving, receiving);
    assertEquals(mockRed, offer.getOfferingPlayer());
  }

  @Test // Test Case 8
  public void GetGiving_ExpectSameResourceQuantity() {
    Player mockRed = EasyMock.createMock(Player.class);
    ResourceQuantity giving = ResourceQuantity.create(Resource.BRICK, 1);
    ResourceQuantity receiving = ResourceQuantity.create(Resource.WOOL, 1);

    TradeOffer offer = TradeOffer.create(mockRed, giving, receiving);
    assertSame(giving, offer.getGiving());
  }

  @Test // Test Case 9
  public void GetReceiving_ExpectSameResourceQuantity() {
    Player mockRed = EasyMock.createMock(Player.class);
    ResourceQuantity giving = ResourceQuantity.create(Resource.ORE, 1);
    ResourceQuantity receiving = ResourceQuantity.create(Resource.GRAIN, 1);

    TradeOffer offer = TradeOffer.create(mockRed, giving, receiving);
    assertSame(receiving, offer.getReceiving());
  }

  // TC6 ← REDUCES CXTY
  @Test
  public void getOfferingPlayer_ValidOffer_ExpectSamePlayer() {
    Player mockPlayer = EasyMock.createMock(Player.class);
    ResourceQuantity giving = ResourceQuantity.create(Resource.ORE, 1);
    ResourceQuantity receiving = ResourceQuantity.create(Resource.WOOL, 1);
    EasyMock.replay(mockPlayer);

    TradeOffer offer = TradeOffer.create(mockPlayer, giving, receiving);

    assertSame(mockPlayer, offer.getOfferingPlayer());
    EasyMock.verify(mockPlayer);
  }

  // TC7 ← REDUCES CXTY
  @Test
  public void getGiving_ValidOffer_ExpectSameGivingQuantity() {
    Player mockPlayer = EasyMock.createMock(Player.class);
    ResourceQuantity giving = ResourceQuantity.create(Resource.BRICK, 2);
    ResourceQuantity receiving = ResourceQuantity.create(Resource.GRAIN, 1);
    EasyMock.replay(mockPlayer);

    TradeOffer offer = TradeOffer.create(mockPlayer, giving, receiving);

    assertSame(giving, offer.getGiving());
    EasyMock.verify(mockPlayer);
  }

  // TC8 ← REDUCES CXTY
  @Test
  public void getReceiving_ValidOffer_ExpectSameReceivingQuantity() {
    Player mockPlayer = EasyMock.createMock(Player.class);
    ResourceQuantity giving = ResourceQuantity.create(Resource.LUMBER, 1);
    ResourceQuantity receiving = ResourceQuantity.create(Resource.ORE, 3);
    EasyMock.replay(mockPlayer);

    TradeOffer offer = TradeOffer.create(mockPlayer, giving, receiving);

    assertSame(receiving, offer.getReceiving());
    EasyMock.verify(mockPlayer);
  }

}

