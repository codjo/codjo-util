package net.codjo.util.time;

import java.util.Arrays;
import org.junit.Test;
import org.junit.experimental.theories.Theory;

import static junit.framework.Assert.assertEquals;
import static net.codjo.util.time.SimpleStatistics.max;
import static net.codjo.util.time.SimpleStatistics.min;
/**
 * Test for class {@link SimpleStatistics}.
 */
public class SimpleStatisticsTest extends AbstractStatisticsTest<SimpleStatistics> {
    @Override
    SimpleStatistics createStatistics() {
        return new SimpleStatistics();
    }


    @Override
    void addTime(TestData currentData, int currentDataIndex, SimpleStatistics actualStats, long duration) {
        actualStats.addTime(duration);
    }


    @Test
    public void testIsEmpty_afterAddOtherStats() {
        SimpleStatistics stats = createStatistics();
        stats.add(createOneQuery(ONE_VALUE));
        assertEquals("isEmpty", false, stats.isEmpty());
    }


    @Theory
    public void testAdd(TestData testData1, TestData testData2) {
        new AddTestTemplate() {
            @Override
            protected SimpleStatistics runTest(SimpleStatistics stats1, SimpleStatistics stats2) {
                stats1.add(stats2);
                return stats1;
            }
        }.runTest(testData1, testData2);
    }


    @Theory
    public void testAggregate(TestData testData1, TestData testData2) {
        new AddTestTemplate() {
            @Override
            protected SimpleStatistics runTest(final SimpleStatistics stats1, final SimpleStatistics stats2) {
                StatisticsOwner owner1 = new StatisticsOwner<SimpleStatistics>() {
                    public SimpleStatistics getStatistics() {
                        return stats1;
                    }
                };
                StatisticsOwner owner2 = new StatisticsOwner<SimpleStatistics>() {
                    public SimpleStatistics getStatistics() {
                        return stats2;
                    }
                };
                return SimpleStatistics.aggregate(Arrays.asList(owner1, owner2));
            }
        }.runTest(testData1, testData2);
    }


    private abstract class AddTestTemplate {
        public void runTest(TestData testData1, TestData testData2) {
            SimpleStatistics stats1 = createOneQuery(testData1);
            SimpleStatistics stats2 = createOneQuery(testData2);
            int stats1InitialCount = stats1.getCount();

            Statistics stats = runTest(stats1, stats2);

            long expectedTotalTime = testData1.expectedTotalTime + testData2.expectedTotalTime;
            long expectedCount = stats1InitialCount + stats2.getCount();
            assertEquals(expectedTotalTime, stats.getTotalTime());
            assertEquals(min(testData1.expectedMinTime, testData2.expectedMinTime), stats.getMinTime());
            assertEquals(max(testData1.expectedMaxTime, testData2.expectedMaxTime), stats.getMaxTime());
            assertEquals(expectedCount, stats.getCount());

            final double expectedMeanTime;
            final double expectedMeanFrequency;
            if (expectedTotalTime > 0) {
                expectedMeanTime = (double)expectedTotalTime / expectedCount; // assume value in milliseconds
                expectedMeanFrequency = 1000d / expectedMeanTime; // number of operations/second
            }
            else {
                expectedMeanTime = 0d;
                expectedMeanFrequency = 0d;
            }
            assertEquals(expectedMeanTime, stats.getMeanTime());
            assertEquals(expectedMeanFrequency, stats.getMeanFrequency());
        }


        abstract protected Statistics runTest(SimpleStatistics stats1, SimpleStatistics stats2);
    }


    @Test
    public void testMin() {
        assertEquals(-1, min(-1, -1));
        assertEquals(3, min(3, -1));
        assertEquals(2, min(-1, 2));
        assertEquals(4, min(6, 4));
    }


    @Test
    public void testMax() {
        assertEquals(-1, max(-1, -1));
        assertEquals(3, max(3, -1));
        assertEquals(2, max(-1, 2));
        assertEquals(9, max(9, 4));
    }
}

