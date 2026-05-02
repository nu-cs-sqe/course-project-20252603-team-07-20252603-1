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
}
