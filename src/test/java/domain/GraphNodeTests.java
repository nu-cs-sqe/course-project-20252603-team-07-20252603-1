package domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class GraphNodeTests {
    @ParameterizedTest
    @EnumSource(value = PlayerColor.class, names = {"RED", "WHITE", "ORANGE", "BLUE"})
    void setColor_ExpectTrue(PlayerColor color) {
        GraphNode g1 = new GraphNode(0);
        assertTrue(g1.setGraphNodeColor(color));
    }
}
