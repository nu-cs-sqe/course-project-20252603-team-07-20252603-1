package domain.model.board;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EdgeTests {

    @Test
    void isOccupied_NewEdge_ExpectFalse() {
        Edge edge = new Edge();
        assertFalse(edge.isOccupied());
    }

    @Test
    void isConnectedToPlayerNetwork_NewEdge_ExpectFalse() {
        Edge edge = new Edge();
        assertFalse(edge.isConnectedToPlayerNetwork());
    }
}
