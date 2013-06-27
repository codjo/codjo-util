package net.codjo.util.time;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static java.lang.Math.max;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
/**
 *
 */
@RunWith(Theories.class)
public class MockTimeSourceTest {
    @DataPoint
    public static final long AUTO_INC_MINUS_TWO = -2;
    @DataPoint
    public static final long AUTO_INC_MINUS_ONE = -1;
    @DataPoint
    public static final long AUTO_INC_ZERO = 0;
    @DataPoint
    public static final long AUTO_INC_ONE = 1;
    @DataPoint
    public static final long AUTO_INC_TWO = 2;

    private MockTimeSource timeSource;


    @Before
    public void setUp() {
        timeSource = new MockTimeSource();
    }


    @Test
    public void testGetTime() {
        // assert that implementation is not time dependant
        for (int i = 0; i < 100; i++) {
            assertEquals(0, timeSource.getTime());
            try {
                Thread.sleep(10);
            }
            catch (InterruptedException e) {
                // ignore
            }
            assertEquals(0, timeSource.getTime());
        }
    }


    @Theory
    public void testGetTime_autoIncrement(long autoIncrementValue) {
        MockTimeSource timeSource = new MockTimeSource(autoIncrementValue);
        testGetTime_autoIncrement(timeSource, autoIncrementValue);
    }


    @Theory
    public void testSetAutoIncrement(long autoIncrementValue) {
        MockTimeSource timeSource = new MockTimeSource();
        timeSource.setAutoIncrement(autoIncrementValue);
        testGetTime_autoIncrement(timeSource, autoIncrementValue);
    }


    private void testGetTime_autoIncrement(MockTimeSource timeSource, long autoIncrementValue) {
        autoIncrementValue = max(autoIncrementValue, 0);
        for (int i = 0; i < 10; i++) {
            assertEquals(i * autoIncrementValue, timeSource.getTime());
        }
    }


    @Test
    public void testInc() {
        assertEquals(0, timeSource.getTime());

        timeSource.inc();

        assertEquals(1, timeSource.getTime());
    }


    @Theory
    public void testInc_parameter(long increment) {
        assertEquals(0, timeSource.getTime());

        if (increment >= 0) {
            timeSource.inc(increment);
            assertEquals(increment, timeSource.getTime());
        }
        else {
            try {
                timeSource.inc(increment);
                fail("inc(value) must fail when value < 0");
            }
            catch (IllegalArgumentException iae) {
                assertEquals("increment (" + increment + ") < 0", iae.getMessage());
            }
        }
    }


    @Test
    public void testReset() {
        assertEquals(0, timeSource.getTime());
        timeSource.inc();
        assertEquals(1, timeSource.getTime());

        timeSource.reset();

        assertEquals(0, timeSource.getTime());
    }
}

