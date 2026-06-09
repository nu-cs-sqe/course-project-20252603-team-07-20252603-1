package domain;

import org.junit.jupiter.api.Test;

import domain.model.player.Player;
import domain.model.resources.Resource;
import domain.model.resources.ResourceQuantity;

import org.easymock.EasyMock;

import static org.junit.jupiter.api.Assertions.*;

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
}
