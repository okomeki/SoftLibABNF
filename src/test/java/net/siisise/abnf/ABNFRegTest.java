package net.siisise.abnf;

import java.util.List;
import net.siisise.abnf.parser7405.ABNF7405;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
/**
 *
 * @author okome
 */
public class ABNFRegTest {
    
    public ABNFRegTest() {
    }

    /**
     * Test of ref method, of class ABNFReg.
     */
/*    @Test
    public void testRef() {
        System.out.println("ref");
        String name = "";
        ABNFReg reg = new ABNFReg();
        ABNF expResult = null;
        ABNF result = reg.ref(name);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
*/
    /**
     * Test of href method, of class ABNFReg.
     */
/*    @Test
    public void testHref() {
        System.out.println("href");
        String name = "";
        ABNFReg reg = new ABNFReg();
        ABNF expResult = null;
        ABNF result = reg.href(name);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
*/
    /**
     * Test of rule method, of class ABNFReg.
     */
/*    @Test
    public void testRule_String_ABNF() {
        System.out.println("rule");
        String name = "";
        ABNF abnf = null;
        ABNFReg reg = new ABNFReg();
        ABNF expResult = null;
        ABNF result = reg.rule(name, abnf);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
*/
    /**
     * Test of rule method, of class ABNFReg.
     */
/*    @Test
    public void testRule_3args_1() {
        System.out.println("rule");
        String name = "";
        Class<? extends ABNFParser> cl = null;
        ABNF abnf = null;
        ABNFReg reg = new ABNFReg();
        ABNF expResult = null;
        ABNF result = reg.rule(name, cl, abnf);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
*/
    /**
     * Test of rule method, of class ABNFReg.
     */
/*    @Test
    public void testRule_3args_2() {
        System.out.println("rule");
        String name = "";
        Class<? extends ABNFParser> cl = null;
        String rule = "";
        ABNFReg reg = new ABNFReg();
        ABNF expResult = null;
        ABNF result = reg.rule(name, cl, rule);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
*/
    /**
     * Test of rule method, of class ABNFReg.
     */
    @Test
    public void testRule_String() {
        System.out.println("rule");
        String rule = "a = b\r\n";
        ABNFReg instance = new ABNFReg();
        //ABNF expResult = null;
        ABNF result = instance.rule(rule);
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of baseParse method, of class ABNFReg.
     */
/*    @Test
    public void testBaseParse() {
        System.out.println("baseParse");
        String name = "";
        String src = "";
        ABNFReg reg = new ABNFReg();
        Object expResult = null;
        Object result = reg.baseParse(name, src);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
*/
    /**
     * Test of parse method, of class ABNFReg.
     */
/*    @Test
    public void testParse() {
        System.out.println("parse");
        String name = "";
        String src = "";
        ABNFReg reg = new ABNFReg();
        Object expResult = null;
        Object result = reg.parse(name, src);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
*/
    /**
     * Test of rule method, of class ABNFReg.
     */
/*    @Test
    public void testRule_String_String() {
        System.out.println("rule");
        String name = "";
        String elements = "";
        ABNFReg reg = new ABNFReg();
        ABNF expResult = null;
        ABNF result = reg.rule(name, elements);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
*/
    /**
     * Test of rulelist method, of class ABNFReg.
     */
    @Test
    public void testRulelist() {
        System.out.println("rulelist");
        String rulelist = "a = b\r\nv = d\r\n";
        ABNFReg reg = new ABNFReg();
//        List<ABNF> expResult = null;
        List<ABNF> result = reg.rulelist(rulelist);
        ABNF a = reg.href("a");
//        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }
    
    @Test
    public void test7() {
        System.out.println("ABNF7405");
        String rulelist = "a = b\r\nv = %s\"dE\"\r\n";
//        String rulelist = "a = b\r\nv = d\r\n";
        ABNFReg<?> reg = new ABNFReg((ABNFReg<?>)null, ABNF7405.REG);
        reg.rulelist(rulelist);
        ABNF a = reg.href("a");
        boolean aref = a instanceof ABNFReg.ABNFRef;
        assertEquals(aref, false);
        ABNF v = reg.href("v");
        assertEquals(v.is("dE"), true);
        assertEquals(v.is("DE"), false);
        
    }
    
}
