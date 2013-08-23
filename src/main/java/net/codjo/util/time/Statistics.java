package net.codjo.util.time;
/**
 * Represents a set of computed statistics.
 */
public interface Statistics {
    /**
     * A singleton instance of {@link Statistics}, representing an absence of statistics due to a lack of data.
     */
    public static final Statistics NONE = new Statistics() {
        public int getCount() {
            return 0;
        }


        public long getTotalTime() {
            return 0;
        }


        public long getMinTime() {
            return -1;
        }


        public long getMaxTime() {
            return -1;
        }


        public double getMeanTime() {
            return 0;
        }


        public double getMeanFrequency() {
            return 0;
        }


        public boolean isEmpty() {
            return true;
        }
    };


    /**
     * @return The number of added times.
     */
    int getCount();


    /**
     * @return The sum of all added times.
     */
    long getTotalTime();


    /**
     * @return The minimum of all added times.
     */
    long getMinTime();


    /**
     * @return The maximum of all added times.
     */
    long getMaxTime();


    /**
     * @return The mean of all added times.
     */
    double getMeanTime();


    /**
     * @return The mean frequency (number of operations per seconds) by assuming all given times were in milliseconds.
     */
    double getMeanFrequency();


    /**
     * Does this set contain any time ?
     *
     * @return true if this statistics doesn't yet contains any time, false otherwise.
     */
    boolean isEmpty();
}
