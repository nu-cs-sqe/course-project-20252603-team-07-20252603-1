package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RobberTests {
    @Test // Test Case 1
    public void moveRobberToHexZero() {
        Robber robber = new Robber();

        robber.moveRobber(0);

        int expected = 0;
        int actual = robber.getRobberLocation();
        assertEquals(expected, actual);
    }
}
