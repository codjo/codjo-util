package net.codjo.util.file;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
/**
 *
 */
public class PathUtilTest {

    @Test
    public void test_normalize() throws Exception {
        String windowsLikePath = "C:\\dev\\projects\\codjo\\A_FILE.txt";
        assertEquals(windowsLikePath, PathUtil.normalize(windowsLikePath, '\\'));

        assertEquals("C:/dev/projects/codjo/A_FILE.txt", PathUtil.normalize(windowsLikePath, '/'));

        String unixLikePath = "/home/dev/projects/codjo/A_FILE.txt";
        assertEquals(unixLikePath, PathUtil.normalize(unixLikePath, '/'));
    }
}
