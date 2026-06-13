package domain.model.board;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VertexTests {

    @Test
    void isOccupied_NewVertex_ExpectFalse() {
        Vertex vertex = new Vertex();
        assertFalse(vertex.isOccupied());
    }

    @Test
    void hasAdjacentSettlementViolatingDistanceRule_NewVertex_ExpectFalse() {
        Vertex vertex = new Vertex();
        assertFalse(vertex.hasAdjacentSettlementViolatingDistanceRule());
    }
}
