/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.misc.api;

import com.gas.domain.core.misc.NewickParser;
import com.gas.domain.core.misc.data.MiscDataDumb;
import java.util.List;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author dq
 */
public class NewickTest {

    private static Newick negative;
    private static Newick complex;

    public NewickTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        NewickParser parser = new NewickParser();
        negative = parser.parse(MiscDataDumb.class, MiscDataDumb.NEGATIVE);
        complex = parser.parse(MiscDataDumb.class, MiscDataDumb.COMPLEX);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getRoot method, of class Newick.
     */
    @Test
    public void testGetRoot() {
    }

    /**
     * Test of setRoot method, of class Newick.
     */
    @Test
    public void testSetRoot() {
    }

    /**
     * Test of getNumOfLeaves method, of class Newick.
     */
    @Test
    public void testGetNumOfLeaves() {
    }

    /**
     * Test of depth method, of class Newick.
     */
    @Test
    public void testDepth() {
    }

    /**
     * Test of getLeaves method, of class Newick.
     */
    @Test
    public void testGetMinLength() {
        Float min = negative.getMinLength();
        assertEquals(-0.00564f, min.floatValue(), 0);
    }

    @Test
    public void testToString() {
        String str = complex.toString();
        System.out.println(str);
    }
}
