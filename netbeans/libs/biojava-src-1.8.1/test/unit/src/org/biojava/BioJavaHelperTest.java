/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.biojava;

import org.biojava.bio.symbol.Symbol;
import org.biojava.bio.symbol.SymbolList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author dq
 */
public class BioJavaHelperTest {
    
    public BioJavaHelperTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getToken method, of class BioJavaHelper.
     */
    //@Test
    public void testGetToken() {
        System.out.println("getToken");
        Symbol symbol = null;
        Character expResult = null;
        Character result = BioJavaHelper.getToken(symbol);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toAA3LetterCode method, of class BioJavaHelper.
     */
    @Test
    public void testToAA3LetterCode() {
        System.out.println("toAA3LetterCode");
        Character c = 'a';
        String expResult = "ALA";
        String result = BioJavaHelper.toAA3LetterCode(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of toSymbolList method, of class BioJavaHelper.
     */
    //@Test
    public void testToSymbolList() {
        System.out.println("toSymbolList");
        String symbols = "";
        SymbolList expResult = null;
        SymbolList result = BioJavaHelper.toSymbolList(symbols);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toDNASymbolList method, of class BioJavaHelper.
     */
    //@Test
    public void testToDNASymbolList() {
        System.out.println("toDNASymbolList");
        String symbols = "";
        SymbolList expResult = null;
        SymbolList result = BioJavaHelper.toDNASymbolList(symbols);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}