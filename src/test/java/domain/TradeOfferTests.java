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

    @Test // Test Case 3
    public void Construct_LumberForLumber_ExpectError() {
        Player mockRed = EasyMock.createMock(Player.class);
        ResourceQuantity giving = new ResourceQuantity(Resource.LUMBER, 1);
        ResourceQuantity receiving = new ResourceQuantity(Resource.LUMBER, 1);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new TradeOffer(mockRed, giving, receiving);
        });

        String expectedMessage = "Cannot trade a resource for itself.";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test // Test Case 4
    public void Construct_TwoWoolForThreeWool_ExpectError() {
        Player mockWhite = EasyMock.createMock(Player.class);
        ResourceQuantity giving = new ResourceQuantity(Resource.WOOL, 2);
        ResourceQuantity receiving = new ResourceQuantity(Resource.WOOL, 3);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new TradeOffer(mockWhite, giving, receiving);
        });

        String expectedMessage = "Cannot trade a resource for itself.";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}
