package net.siisise.abnf.rfc;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author okome
 */
public class URI2396Test {
    
    public URI2396Test() {
    }

    @Test
    public void testSomeMethod() {
        assertTrue(URI2396.domainlabel.eq("a"));
        assertTrue(URI2396.domainlabel.eq("ab"));
        assertTrue(URI2396.domainlabel.eq("abc"));
        assertTrue(URI2396.domainlabel.eq("abc--d"));
        assertTrue(URI2396.domainlabel.eq("ab-cc-d"));
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }
    
}
