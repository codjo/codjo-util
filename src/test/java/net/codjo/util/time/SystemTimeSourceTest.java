package net.codjo.util.time;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
/**
 *
 */
public class SystemTimeSourceTest {
    @Test
    public void testDefaultIfNull_notNull() throws Exception {
        TimeSource expected = new TimeSource() {
            public long getTime() {
                return 0;
            }
        };

        TimeSource actual = SystemTimeSource.defaultIfNull(expected);

        assertEquals(expected, actual);
    }


    @Test
    public void testDefaultIfNull_null() throws Exception {
        TimeSource actual = SystemTimeSource.defaultIfNull(null);

        assertNotNull(actual);
        assertEquals(SystemTimeSource.class, actual.getClass());
    }


    @Test
    public void testGetTime() throws Exception {
        long expected = System.currentTimeMillis();

        long actual = SystemTimeSource.INSTANCE.getTime();

        // we expect the 2 calls to System.currentTimeMillis() will run in less than 20 ms.
        long threshold = 20L;
        try {
            assertEquals(expected, actual);
            // success
        }
        catch (AssertionError e) {
            try {
                assertTrue("time must increase", actual > expected);
                assertTrue("call to getTotalTime() should take less than " + threshold + "ms",
                           (actual - expected) < threshold);
                // probable success
            }
            catch (AssertionError e2) {
                // complete failure
                throw e;
            }
        }
    }
}
