package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ResourceQuantityTests {
    @Test // Test Case 1
    public void Construct_BrickAtLowerBoundary_ExpectValid() {
        assertDoesNotThrow(() -> new ResourceQuantity(Resource.BRICK, 1));
    }

    @Test // Test Case 2
    public void Construct_WoolAtUpperBoundary_ExpectValid() {
        assertDoesNotThrow(() -> new ResourceQuantity(Resource.WOOL, Integer.MAX_VALUE));
    }

    @Test // Test Case 3
    public void Construct_GrainBelowLowerBoundary_ExpectError() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new ResourceQuantity(Resource.GRAIN, 0);
        });

        String expectedMessage = "Quantity must be at least 1.";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test // Test Case 4
    public void Construct_Desert_ExpectError() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new ResourceQuantity(Resource.DESERT, 1);
        });

        String expectedMessage = "Resource must be tradeable.";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}
