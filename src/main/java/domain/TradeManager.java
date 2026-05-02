package domain;

import java.util.ArrayList;
import java.util.List;

public class TradeManager {
    private final List<TradeOffer> offers = new ArrayList<>();

    public void offerTrade(TradeOffer offer) {
        offers.add(offer);
    }

    public List<TradeOffer> listTrades() {
        return offers;
    }
}
