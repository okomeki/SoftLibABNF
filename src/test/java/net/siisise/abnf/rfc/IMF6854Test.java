package net.siisise.abnf.rfc;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author okome
 */
public class IMF6854Test {
    
    public IMF6854Test() {
    }

    @Test
    public void testSomeMethod() {
        assertTrue(IMF6854.from.eq("From: okome@siisise.net\r\n"));
        assertTrue(IMF6854.from.eq("From: Nightly Monitor Robot:;\r\n"));
        assertTrue(IMF6854.from.eq("From: ben@example.com,carol@example.com\r\n"));
        assertTrue(IMF6854.sender.eq("Sender: dave@example.com\r\n"));
        assertTrue(IMF6854.from.eq("From: Managing Partners:ben@example.com,carol@example.com;\r\n"));
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
}
