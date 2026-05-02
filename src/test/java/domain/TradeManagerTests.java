package domain;

import org.junit.jupiter.api.Test;

import org.easymock.EasyMock;

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
}
