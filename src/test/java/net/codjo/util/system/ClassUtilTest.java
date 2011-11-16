package net.codjo.util.system;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
/**
 *
 */
public class ClassUtilTest {

    @Test
    public void test_main() throws Exception {
        main();
    }


    private void main() {
        Class aClass = ClassUtil.getMainClass();
        assertEquals(ClassUtilTest.class, aClass);
    }
}
