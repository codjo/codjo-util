package net.codjo.util.time;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
/**
 *
 */
@RunWith(Theories.class)
public abstract class AbstractStatisticsTest<T extends Statistics> {
    @DataPoint
    public static TestData NO_VALUE = new TestData(-1, -1, 0);
    @DataPoint
    public static TestData ONE_VALUE = new TestData(1, 1, 1, 1);
    @DataPoint
    public static TestData TWO_VALUES = new TestData(2, 7, 9, 7, 2);
    @DataPoint
    public static TestData THREE_VALUES = new TestData(2, 8, 15, 8, 2, 5);


    abstract T createStatistics();


    abstract void addTime(TestData currentData, int currentDataIndex, T actualStats, long duration);


    @Test
    public void testIsEmpty_init() {
        T stats = createStatistics();
        assertEquals("isEmpty", true, stats.isEmpty());
    }


    @Test
    public void testIsEmpty_afterAddTime() {
        T actualStats = createStatistics();
        addTime(null, -1, actualStats, 1);
        assertEquals("isEmpty", false, actualStats.isEmpty());
    }


    @Theory
    public void testGetTotalTime(TestData data) {
        Statistics actualStats = createOneQuery(data);
        assertEquals(data.expectedTotalTime, actualStats.getTotalTime());
    }


    @Theory
    public void testGetMinTime(TestData data) {
        Statistics actualStats = createOneQuery(data);
        assertEquals(data.expectedMinTime, actualStats.getMinTime());
    }


    @Theory
    public void testGetMaxTime(TestData data) {
        Statistics actualStats = createOneQuery(data);
        assertEquals(data.expectedMaxTime, actualStats.getMaxTime());
    }


    @Theory
    public void testGetMeanTime(TestData data) {
        Statistics actualStats = createOneQuery(data);
        assertEquals(data.expectedMeanTime, actualStats.getMeanTime());
    }


    @Theory
    public void testGetMeanFrequency(TestData data) {
        Statistics actualStats = createOneQuery(data);
        assertEquals(data.expectedMeanFrequency, actualStats.getMeanFrequency());
    }


    @Theory
    public void testGetCount(TestData data) {
        Statistics actualStats = createOneQuery(data);
        assertEquals(data.times.length, actualStats.getCount());
    }


    protected T createOneQuery(TestData data) {
        T actualStats = createStatistics();
        for (int i = 0; i < data.times.length; i++) {
            addTime(data, i, actualStats, data.times[i]);
        }
        return actualStats;
    }


    protected static class TestData {
        final long[] times;
        final long expectedMinTime;
        final long expectedMaxTime;
        final long expectedTotalTime;
        final double expectedMeanTime;
        final double expectedMeanFrequency;


        public TestData(long expectedMinTime, long expectedMaxTime, long expectedTotalTime,
                        long... times) {
            this.times = times;
            this.expectedMinTime = expectedMinTime;
            this.expectedMaxTime = expectedMaxTime;
            this.expectedTotalTime = expectedTotalTime;

            // in milliseconds
            this.expectedMeanTime = (times.length == 0) ? 0 : (double)expectedTotalTime / times.length;

            // in operations/second
            this.expectedMeanFrequency = (times.length == 0) ? 0 : (1000.0d / expectedMeanTime);
        }
    }
}

