package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PlayerTests {
    @Test // test case 1
    public void PlaceEmptySettlement_OnNullVertex_ExpectError() {
        Player player = new Player();
        Vertex vertex = null;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            player.placeSettlement(vertex);
        });
        assertEquals("Vertex cannot be null", exception.getMessage());
    }
}
