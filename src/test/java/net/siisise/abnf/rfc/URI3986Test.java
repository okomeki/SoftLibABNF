package net.siisise.abnf.rfc;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author okome
 */
public class URI3986Test {
    
    public URI3986Test() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of main method, of class URI3986.
     */
    @Test
    public void testURIReference() {
        System.out.println("URIReference");
        String[] argv = null;
        // 1.1.2. Examples
        assertTrue(URI3986.URI.eq("ftp://ftp.is.co.za/rfc/rfc1808.txt"));
        assertTrue(URI3986.URI.eq("http://www.ietf.org/rfc/rfc2396.txt"));
//        Packet p = new PacketA();
//        p.write("ldap://[2001:db8::7]/c=GB?objectClass?one".getBytes("utf-8"));
//        Packet a = URI3986.URI.is(p);
//        System.out.println("謎:" + AbstractABNF.strd(a));
        assertTrue(URI3986.URI.eq("ldap://[2001:db8::7]/c=GB?objectClass?one")); //なにか
        assertTrue(URI3986.URI.eq("mailto:John.Doe@example.com"));
        assertTrue(URI3986.URI.eq("news:comp.infosystems.www.servers.unix"));
        assertTrue(URI3986.URI.eq("tel:+1-816-555-1212"));
        assertTrue(URI3986.URI.eq("telnet://192.0.2.16:80/"));
        assertTrue(URI3986.URI.eq("urn:oasis:names:specification:docbook:dtd:xml:4.1.2"));
        
        // 5.4.1. Normal Example
        assertTrue(URI3986.absoluteURI.eq("g:h"));
        assertTrue(URI3986.relativeRef.eq("g"));
        assertTrue(URI3986.relativeRef.eq("./g"));
        assertTrue(URI3986.relativeRef.eq("g/"));
        assertTrue(URI3986.relativeRef.eq("/g"));
        assertTrue(URI3986.relativeRef.eq("//g"));
        assertTrue(URI3986.relativeRef.eq("?y"));
        assertTrue(URI3986.relativeRef.eq("g?y"));
        assertTrue(URI3986.relativeRef.eq("#s"));
        assertTrue(URI3986.relativeRef.eq("g#s"));
        assertTrue(URI3986.relativeRef.eq("g?y#s"));
        assertTrue(URI3986.relativeRef.eq(";x"));
        assertTrue(URI3986.relativeRef.eq("g;x"));
        assertTrue(URI3986.relativeRef.eq("g;x?y#s"));
        assertTrue(URI3986.relativeRef.eq(""));
        assertTrue(URI3986.relativeRef.eq("."));
        assertTrue(URI3986.relativeRef.eq("./"));
        assertTrue(URI3986.relativeRef.eq(".."));
        assertTrue(URI3986.relativeRef.eq("../"));
        assertTrue(URI3986.relativeRef.eq("../g"));
        assertTrue(URI3986.relativeRef.eq("../.."));
        assertTrue(URI3986.relativeRef.eq("../../"));
        assertTrue(URI3986.relativeRef.eq("../../g"));

        assertTrue(URI3986.URIreference.eq("http://okome@siisise.net/uss"));
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
}
