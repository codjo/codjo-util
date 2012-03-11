package net.codjo.util.system;
import java.io.File;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
/**
 *
 */
public class WindowsExecTest {
    private static final String NEW_LINE = System.getProperty("line.separator");
    private WindowsExec executor;


    @Before
    public void setUp() {
        Assume.assumeTrue(isWindows());
        executor = new WindowsExec();
    }


    @Test
    public void test_exec() throws Exception {
        int returnCode = executor.exec("cmd.exe /Y /C echo toto");

        assertEquals(0, returnCode);
        assertEquals("toto" + NEW_LINE, executor.getProcessMessage());
        assertEquals("", executor.getErrorMessage());
    }


    @Test
    public void test_execKO() throws Exception {
        int returnCode = executor.exec("cmd.exe /Y /C echooo toto");

        assertEquals(1, returnCode);
        String message = executor.getErrorMessage();
        assertTrue(message.contains("'echooo' n'est pas reconnu en tant que commande interne" + NEW_LINE
                                    + "ou externe, un programme ex‚cutable ou un fichier de commandes."));
        assertEquals("", executor.getProcessMessage());
    }


    @Test
    public void test_exec_timeoutKO() throws Exception {
        int timeout = 1000;
        double begin = System.currentTimeMillis();
        int returnCode = executor.exec("cmd.exe /Y /C pause", null, timeout);

        double duration = System.currentTimeMillis() - begin;
        if (duration < timeout) {
            assertEquals(timeout, duration, 15); //Tolérance de 15ms d'avance du process killer
        }
        assertEquals(timeout, duration, 125);
        assertEquals(1, returnCode);
        assertEquals("Output message :" + NEW_LINE
                     + "Appuyez sur une touche pour continuer... ", executor.getErrorMessage());
        assertEquals("Appuyez sur une touche pour continuer... ", executor.getProcessMessage());
    }


    @Test
    public void test_exec_timeout() throws Exception {
        long begin = System.currentTimeMillis();
        int returnCode = executor.exec("cmd.exe /Y /C echo toto", null, 5000);

        assertTrue(50 > (System.currentTimeMillis() - begin) / 100);
        assertEquals(0, returnCode);
        assertEquals("toto" + NEW_LINE, executor.getProcessMessage());
        assertEquals("", executor.getErrorMessage());
    }


    @Test
    public void test_exec_workingDir() throws Exception {
        int returnCode = executor.exec("cmd.exe /Y /C dir", new File("src"));

        assertEquals(0, returnCode);
        assertTrue(executor.getProcessMessage().contains("main" + NEW_LINE));
        assertTrue(executor.getProcessMessage().contains("test" + NEW_LINE));
        assertTrue(executor.getProcessMessage().contains("0 fichier(s)"));
        assertEquals("", executor.getErrorMessage());
    }


    public static boolean isWindows() {
        return (System.getProperty("os.name").toLowerCase().contains("win"));
    }
}
