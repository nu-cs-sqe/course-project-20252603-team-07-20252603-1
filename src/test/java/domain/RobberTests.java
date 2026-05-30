package domain;

import org.junit.jupiter.api.Test;

import domain.model.Robber;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RobberTests {
    @Test // Test Case 1
    public void moveRobberToHexZero() {
        Robber robber = new Robber(9);

        robber.moveRobber(0);

        int expected = 0;
        int actual = robber.getRobberLocation();
        assertEquals(expected, actual);
    }

    @Test // Test Case 2
    public void moveRobberToLastHex() {
        Robber robber = new Robber(9);

        robber.moveRobber(18);

        int expected = 18;
        int actual = robber.getRobberLocation();
        assertEquals(expected, actual);
    }

    @Test // Test Case 3
    public void moveRobberToNegativeHex() {
        Robber robber = new Robber(9);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            robber.moveRobber(-1);
        });

        String expectedMessage = "Cannot move Robber to invalid HexId";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test // Test Case 4
    public void moveRobberToOutOfUpperBoundHex() {
        Robber robber = new Robber(9);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            robber.moveRobber(19);
        });

        String expectedMessage = "Cannot move Robber to invalid HexId";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test // Test Case 5
    public void validateInitialRobberState() {
        Robber robber = new Robber(9);

        int expected = 9;
        int actual = robber.getRobberLocation();
        assertEquals(expected, actual);
    }
}


