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
}
