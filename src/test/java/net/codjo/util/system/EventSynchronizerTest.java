package net.codjo.util.system;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsSame.sameInstance;
import static org.junit.Assert.assertThat;
import org.junit.Test;
/**
 *
 */
public class EventSynchronizerTest {
    private StringBuffer logString = new StringBuffer();
    private EventSynchronizer<String> synchronizer = new EventSynchronizer<String>();
    private static final String AN_EVENT = "an event";


    @Test
    public void test_eventReceivedBeforeMethodCall() throws Exception {
        synchronizer.receivedEvent(AN_EVENT);

        assertThat(synchronizer.waitEvent(), sameInstance(AN_EVENT));
    }


    @Test
    public void test_expectedWorkflow() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(1);

        FutureTask<String> future = waitInAnotherThread(executor, "after waitEvent");

        logString.append("before received, ");
        synchronizer.receivedEvent(AN_EVENT);

        assertThat(future.get(), is(sameInstance(AN_EVENT)));

        assertThat(logString.toString(), is("before received, after waitEvent"));

        executor.shutdownNow();
    }


    private FutureTask<String> waitInAnotherThread(ExecutorService executor, final String afterWaitMessage) {
        FutureTask<String> future = new FutureTask<String>(new Callable<String>() {
            public String call() throws InterruptedException {
                String result = synchronizer.waitEvent();
                logString.append(afterWaitMessage);
                return result;
            }
        });

        executor.execute(future);
        return future;
    }
}
