package domain;

import org.junit.jupiter.api.Test;

import org.easymock.EasyMock;

import static org.junit.jupiter.api.Assertions.*;

public class TradeOfferTests {
    @Test // Test Case 1
    public void Construct_RedBrickForWool_ExpectValid() {
        Player mockRed = EasyMock.createMock(Player.class);
        ResourceQuantity giving = new ResourceQuantity(Resource.BRICK, 1);
        ResourceQuantity receiving = new ResourceQuantity(Resource.WOOL, 1);

        assertDoesNotThrow(() -> new TradeOffer(mockRed, giving, receiving));
    }

    @Test // Test Case 2
    public void Construct_BlueOreForGrain_ExpectValid() {
        Player mockBlue = EasyMock.createMock(Player.class);
        ResourceQuantity giving = new ResourceQuantity(Resource.ORE, 2);
        ResourceQuantity receiving = new ResourceQuantity(Resource.GRAIN, 1);

        assertDoesNotThrow(() -> new TradeOffer(mockBlue, giving, receiving));
    }
}
