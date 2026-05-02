package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ResourceQuantityTests {
    @Test // Test Case 1
    public void Construct_BrickAtLowerBoundary_ExpectValid() {
        assertDoesNotThrow(() -> new ResourceQuantity(Resource.BRICK, 1));
    }
}
