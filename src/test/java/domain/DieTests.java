package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DieTests {

    @Test // Test Case 1
    public void InitDie_RandomZero_ExpectOne(){
        Random randomMock = EasyMock.mock(Random.class);

        EasyMock.expect(randomMock.nextInt((6))).andReturn(0);

        EasyMock.replay(randomMock);

        Die testDie = new Die(randomMock);
        int expected = 1;
        int actual = testDie.rollOneDie();

        EasyMock.verify(randomMock);

        assertEquals(expected, actual);

    }

}
