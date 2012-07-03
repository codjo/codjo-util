package net.codjo.util.network;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
/**
 *
 */
public class IpValidatorTest {

    @Test
    public void test_ipValidator() throws Exception {
        assertTrue(IpValidator.isValid("1.1.1.1"));
        assertTrue(IpValidator.isValid("255.255.255.255"));
        assertTrue(IpValidator.isValid("192.168.1.1"));
        assertTrue(IpValidator.isValid("10.10.1.1"));
        assertTrue(IpValidator.isValid("132.254.111.10"));
        assertTrue(IpValidator.isValid("26.10.2.10"));
        assertTrue(IpValidator.isValid("127.0.0.1"));

        assertFalse(IpValidator.isValid("10.10.10"));
        assertFalse(IpValidator.isValid("10.10"));
        assertFalse(IpValidator.isValid("10"));
        assertFalse(IpValidator.isValid("a.A.a.A"));
        assertFalse(IpValidator.isValid("10.0.0.a"));
        assertFalse(IpValidator.isValid("10.10.10.256"));
        assertFalse(IpValidator.isValid("222.222.2.999"));
        assertFalse(IpValidator.isValid("999.10.10.20"));
        assertFalse(IpValidator.isValid("2222.22.22.22"));
        assertFalse(IpValidator.isValid("22.2222.22.2"));
    }
}
