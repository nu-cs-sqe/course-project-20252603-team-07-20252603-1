package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DiceHandlerTests {

    @Test // Test Case 1
    public void InitDiceHandler_BothRollOne_ExpectTwo(){
        Die dieOneMock = EasyMock.mock(Die.class);
        Die dieTwoMock = EasyMock.mock(Die.class);

        EasyMock.expect(dieOneMock.rollOneDie()).andReturn(1);
        EasyMock.expect(dieTwoMock.rollOneDie()).andReturn(1);

        EasyMock.replay(dieOneMock, dieTwoMock);

        DiceHandler testDiceHandler = new DiceHandler(dieOneMock, dieTwoMock);
        int expected = 2;
        int actual = testDiceHandler.rollTwoDice();

        EasyMock.verify(dieOneMock, dieTwoMock);

        assertEquals(expected, actual);
    }

    @Test // Test Case 2
    public void InitDiceHandler_BothRollSix_ExpectTwelve(){
        Die dieOneMock = EasyMock.mock(Die.class);
        Die dieTwoMock = EasyMock.mock(Die.class);

        EasyMock.expect(dieOneMock.rollOneDie()).andReturn(6);
        EasyMock.expect(dieTwoMock.rollOneDie()).andReturn(6);

        EasyMock.replay(dieOneMock, dieTwoMock);

        DiceHandler testDiceHandler = new DiceHandler(dieOneMock, dieTwoMock);
        int expected = 12;
        int actual = testDiceHandler.rollTwoDice();

        EasyMock.verify(dieOneMock, dieTwoMock);

        assertEquals(expected, actual);
    }

    @Test // Test Case 3
    public void InitDiceHandler_RollOne_AndSix_ExpectSeven(){
        Die dieOneMock = EasyMock.mock(Die.class);
        Die dieTwoMock = EasyMock.mock(Die.class);

        EasyMock.expect(dieOneMock.rollOneDie()).andReturn(1);
        EasyMock.expect(dieTwoMock.rollOneDie()).andReturn(6);

        EasyMock.replay(dieOneMock, dieTwoMock);

        DiceHandler testDiceHandler = new DiceHandler(dieOneMock, dieTwoMock);
        int expected = 7;
        int actual = testDiceHandler.rollTwoDice();

        EasyMock.verify(dieOneMock, dieTwoMock);

        assertEquals(expected, actual);
    }
}
