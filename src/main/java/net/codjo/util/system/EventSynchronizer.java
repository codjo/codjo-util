package net.codjo.util.system;
/**
 *
 */
public class EventSynchronizer<T> {
    private static final int TEN_SECONDS = 10000;
    private final Object lock = new Object();
    private int timeout = TEN_SECONDS;
    private T event;


    public T waitEvent() throws InterruptedException {
        synchronized (lock) {
            if (event == null) {
                lock.wait(timeout);
            }
        }
        return event;
    }


    public void receivedEvent(T receivedEvent) {
        synchronized (lock) {
            this.event = receivedEvent;
            lock.notifyAll();
        }
    }


    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
