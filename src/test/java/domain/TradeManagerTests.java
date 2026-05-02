package domain;

import org.junit.jupiter.api.Test;

import org.easymock.EasyMock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TradeManagerTests {
    @Test // Test Case 1
    public void OfferTrade_IntoEmptyList_ExpectListSizeOne() {
        TradeOffer mockOffer = EasyMock.createMock(TradeOffer.class);

        TradeManager tm = new TradeManager();
        tm.offerTrade(mockOffer);

        assertEquals(1, tm.listTrades().size());
    }

    @Test // Test Case 2
    public void OfferTrade_IntoListOfSizeOne_ExpectListSizeTwo() {
        TradeOffer mockOfferA = EasyMock.createMock(TradeOffer.class);
        TradeOffer mockOfferB = EasyMock.createMock(TradeOffer.class);

        TradeManager tm = new TradeManager();
        tm.offerTrade(mockOfferA);
        tm.offerTrade(mockOfferB);

        assertEquals(2, tm.listTrades().size());
    }

    @Test // Test Case 3
    public void OfferTrade_DuplicateOfferTwice_ExpectListSizeTwo() {
        TradeOffer mockOffer = EasyMock.createMock(TradeOffer.class);

        TradeManager tm = new TradeManager();
        tm.offerTrade(mockOffer);
        tm.offerTrade(mockOffer);

        assertEquals(2, tm.listTrades().size());
    }

    @Test // Test Case 4
    public void ClearOffers_OnEmptyList_ExpectEmpty() {
        TradeManager tm = new TradeManager();
        tm.clearOffers();

        assertEquals(0, tm.listTrades().size());
    }

    @Test // Test Case 5
    public void ClearOffers_OnListOfSizeOne_ExpectEmpty() {
        TradeOffer mockOffer = EasyMock.createMock(TradeOffer.class);

        TradeManager tm = new TradeManager();
        tm.offerTrade(mockOffer);
        tm.clearOffers();

        assertEquals(0, tm.listTrades().size());
    }

    @Test // Test Case 6
    public void ClearOffers_OnThreeOffers_ExpectEmpty() {
        TradeOffer mockOfferA = EasyMock.createMock(TradeOffer.class);
        TradeOffer mockOfferB = EasyMock.createMock(TradeOffer.class);
        TradeOffer mockOfferC = EasyMock.createMock(TradeOffer.class);

        TradeManager tm = new TradeManager();
        tm.offerTrade(mockOfferA);
        tm.offerTrade(mockOfferB);
        tm.offerTrade(mockOfferC);
        tm.clearOffers();

        assertEquals(0, tm.listTrades().size());
    }

    @Test // Test Case 7
    public void ClearOffers_OnDuplicateOffers_ExpectEmpty() {
        TradeOffer mockOffer = EasyMock.createMock(TradeOffer.class);

        TradeManager tm = new TradeManager();
        tm.offerTrade(mockOffer);
        tm.offerTrade(mockOffer);
        tm.clearOffers();

        assertEquals(0, tm.listTrades().size());
    }

    @Test // Test Case 8
    public void ListTrades_OnEmptyList_ExpectEmpty() {
        TradeManager tm = new TradeManager();

        assertEquals(0, tm.listTrades().size());
    }

    @Test // Test Case 9
    public void ListTrades_AfterOneOffer_ExpectListSizeOneContainsOffer() {
        TradeOffer mockOffer = EasyMock.createMock(TradeOffer.class);

        TradeManager tm = new TradeManager();
        tm.offerTrade(mockOffer);

        List<TradeOffer> trades = tm.listTrades();
        assertEquals(1, trades.size());
        assertSame(mockOffer, trades.get(0));
    }

    @Test // Test Case 10
    public void ListTrades_AfterThreeOffers_ExpectListSizeThreeInOrder() {
        TradeOffer mockOfferA = EasyMock.createMock(TradeOffer.class);
        TradeOffer mockOfferB = EasyMock.createMock(TradeOffer.class);
        TradeOffer mockOfferC = EasyMock.createMock(TradeOffer.class);

        TradeManager tm = new TradeManager();
        tm.offerTrade(mockOfferA);
        tm.offerTrade(mockOfferB);
        tm.offerTrade(mockOfferC);

        List<TradeOffer> trades = tm.listTrades();
        assertEquals(3, trades.size());
        assertSame(mockOfferA, trades.get(0));
        assertSame(mockOfferB, trades.get(1));
        assertSame(mockOfferC, trades.get(2));
    }

    @Test // Test Case 11
    public void ListTrades_WithDuplicates_ExpectListSizeTwoWithDuplicates() {
        TradeOffer mockOffer = EasyMock.createMock(TradeOffer.class);

        TradeManager tm = new TradeManager();
        tm.offerTrade(mockOffer);
        tm.offerTrade(mockOffer);

        List<TradeOffer> trades = tm.listTrades();
        assertEquals(2, trades.size());
        assertSame(mockOffer, trades.get(0));
        assertSame(mockOffer, trades.get(1));
    }

    @Test // Test Case 12
    public void AcceptTrade_BlueAcceptsRedBrickForWool_ExpectListSizeZero() {
        Player mockRed = EasyMock.createMock(Player.class);
        Player mockBlue = EasyMock.createMock(Player.class);

        ResourceQuantity giving = new ResourceQuantity(Resource.BRICK, 1);
        ResourceQuantity receiving = new ResourceQuantity(Resource.WOOL, 1);

        TradeOffer mockOffer = EasyMock.createMock(TradeOffer.class);
        EasyMock.expect(mockOffer.getOfferingPlayer()).andStubReturn(mockRed);
        EasyMock.expect(mockOffer.getGiving()).andStubReturn(giving);
        EasyMock.expect(mockOffer.getReceiving()).andStubReturn(receiving);

        EasyMock.expect(mockRed.getResourceCount(Resource.BRICK)).andStubReturn(1);
        EasyMock.expect(mockBlue.getResourceCount(Resource.WOOL)).andStubReturn(1);

        mockRed.updateResources(Resource.BRICK, -1);
        mockRed.updateResources(Resource.WOOL, 1);
        mockBlue.updateResources(Resource.WOOL, -1);
        mockBlue.updateResources(Resource.BRICK, 1);

        EasyMock.replay(mockOffer, mockRed, mockBlue);

        TradeManager tm = new TradeManager();
        tm.offerTrade(mockOffer);
        tm.acceptTrade(mockOffer, mockBlue);

        assertEquals(0, tm.listTrades().size());
        EasyMock.verify(mockOffer, mockRed, mockBlue);
    }

    @Test // Test Case 13
    public void AcceptTrade_WhiteAcceptsOrangeFromThree_ExpectListSizeTwo() {
        Player mockOrange = EasyMock.createMock(Player.class);
        Player mockWhite = EasyMock.createMock(Player.class);

        ResourceQuantity giving = new ResourceQuantity(Resource.ORE, 2);
        ResourceQuantity receiving = new ResourceQuantity(Resource.GRAIN, 1);

        TradeOffer mockOfferA = EasyMock.createMock(TradeOffer.class);
        TradeOffer mockOfferB = EasyMock.createMock(TradeOffer.class);
        TradeOffer mockOfferC = EasyMock.createMock(TradeOffer.class);

        EasyMock.expect(mockOfferB.getOfferingPlayer()).andStubReturn(mockOrange);
        EasyMock.expect(mockOfferB.getGiving()).andStubReturn(giving);
        EasyMock.expect(mockOfferB.getReceiving()).andStubReturn(receiving);

        EasyMock.expect(mockOrange.getResourceCount(Resource.ORE)).andStubReturn(2);
        EasyMock.expect(mockWhite.getResourceCount(Resource.GRAIN)).andStubReturn(1);

        mockOrange.updateResources(Resource.ORE, -2);
        mockOrange.updateResources(Resource.GRAIN, 1);
        mockWhite.updateResources(Resource.GRAIN, -1);
        mockWhite.updateResources(Resource.ORE, 2);

        EasyMock.replay(mockOfferA, mockOfferB, mockOfferC, mockOrange, mockWhite);

        TradeManager tm = new TradeManager();
        tm.offerTrade(mockOfferA);
        tm.offerTrade(mockOfferB);
        tm.offerTrade(mockOfferC);

        tm.acceptTrade(mockOfferB, mockWhite);

        List<TradeOffer> remaining = tm.listTrades();
        assertEquals(2, remaining.size());
        assertSame(mockOfferA, remaining.get(0));
        assertSame(mockOfferC, remaining.get(1));
        EasyMock.verify(mockOfferA, mockOfferB, mockOfferC, mockOrange, mockWhite);
    }

    @Test // Test Case 14
    public void AcceptTrade_OneOfTwoDuplicates_ExpectListSizeOne() {
        Player mockRed = EasyMock.createMock(Player.class);
        Player mockBlue = EasyMock.createMock(Player.class);

        ResourceQuantity giving = new ResourceQuantity(Resource.BRICK, 1);
        ResourceQuantity receiving = new ResourceQuantity(Resource.WOOL, 1);

        TradeOffer mockOffer = EasyMock.createMock(TradeOffer.class);
        EasyMock.expect(mockOffer.getOfferingPlayer()).andStubReturn(mockRed);
        EasyMock.expect(mockOffer.getGiving()).andStubReturn(giving);
        EasyMock.expect(mockOffer.getReceiving()).andStubReturn(receiving);

        EasyMock.expect(mockRed.getResourceCount(Resource.BRICK)).andStubReturn(1);
        EasyMock.expect(mockBlue.getResourceCount(Resource.WOOL)).andStubReturn(1);

        mockRed.updateResources(Resource.BRICK, -1);
        mockRed.updateResources(Resource.WOOL, 1);
        mockBlue.updateResources(Resource.WOOL, -1);
        mockBlue.updateResources(Resource.BRICK, 1);

        EasyMock.replay(mockOffer, mockRed, mockBlue);

        TradeManager tm = new TradeManager();
        tm.offerTrade(mockOffer);
        tm.offerTrade(mockOffer);
        tm.acceptTrade(mockOffer, mockBlue);

        assertEquals(1, tm.listTrades().size());
        EasyMock.verify(mockOffer, mockRed, mockBlue);
    }

    @Test // Test Case 15
    public void AcceptTrade_SelfAccept_ExpectError() {
        Player mockRed = EasyMock.createMock(Player.class);

        ResourceQuantity giving = new ResourceQuantity(Resource.BRICK, 1);
        ResourceQuantity receiving = new ResourceQuantity(Resource.WOOL, 1);

        TradeOffer mockOffer = EasyMock.createMock(TradeOffer.class);
        EasyMock.expect(mockOffer.getOfferingPlayer()).andStubReturn(mockRed);
        EasyMock.expect(mockOffer.getGiving()).andStubReturn(giving);
        EasyMock.expect(mockOffer.getReceiving()).andStubReturn(receiving);

        EasyMock.replay(mockOffer, mockRed);

        TradeManager tm = new TradeManager();
        tm.offerTrade(mockOffer);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            tm.acceptTrade(mockOffer, mockRed);
        });

        String expectedMessage = "A player cannot accept their own trade.";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        assertEquals(1, tm.listTrades().size());
        EasyMock.verify(mockOffer, mockRed);
    }

    @Test // Test Case 16
    public void AcceptTrade_OfferNotInList_ExpectError() {
        Player mockRed = EasyMock.createMock(Player.class);
        Player mockBlue = EasyMock.createMock(Player.class);

        ResourceQuantity giving = new ResourceQuantity(Resource.BRICK, 1);
        ResourceQuantity receiving = new ResourceQuantity(Resource.WOOL, 1);

        TradeOffer mockOffer = EasyMock.createMock(TradeOffer.class);
        EasyMock.expect(mockOffer.getOfferingPlayer()).andStubReturn(mockRed);
        EasyMock.expect(mockOffer.getGiving()).andStubReturn(giving);
        EasyMock.expect(mockOffer.getReceiving()).andStubReturn(receiving);

        EasyMock.replay(mockOffer, mockRed, mockBlue);

        TradeManager tm = new TradeManager();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            tm.acceptTrade(mockOffer, mockBlue);
        });

        String expectedMessage = "Trade not found.";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        assertEquals(0, tm.listTrades().size());
        EasyMock.verify(mockOffer, mockRed, mockBlue);
    }

    @Test // Test Case 17
    public void AcceptTrade_OffererInsufficient_ExpectError() {
        Player mockRed = EasyMock.createMock(Player.class);
        Player mockBlue = EasyMock.createMock(Player.class);

        ResourceQuantity giving = new ResourceQuantity(Resource.BRICK, 5);
        ResourceQuantity receiving = new ResourceQuantity(Resource.WOOL, 1);

        TradeOffer mockOffer = EasyMock.createMock(TradeOffer.class);
        EasyMock.expect(mockOffer.getOfferingPlayer()).andStubReturn(mockRed);
        EasyMock.expect(mockOffer.getGiving()).andStubReturn(giving);
        EasyMock.expect(mockOffer.getReceiving()).andStubReturn(receiving);

        EasyMock.expect(mockRed.getResourceCount(Resource.BRICK)).andReturn(0);

        EasyMock.replay(mockOffer, mockRed, mockBlue);

        TradeManager tm = new TradeManager();
        tm.offerTrade(mockOffer);

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            tm.acceptTrade(mockOffer, mockBlue);
        });

        String expectedMessage = "Offering player has insufficient resources.";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        assertEquals(1, tm.listTrades().size());
        EasyMock.verify(mockOffer, mockRed, mockBlue);
    }
}
