/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.misc;

import com.gas.domain.core.misc.api.Newick;
import java.io.File;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author dq
 */
public class NewickWriterTest {

    public NewickWriterTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
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
     * Test of toString method, of class NewickWriter.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        String data = "(Adam:0.030822,	(Bob:0.023240999999999998,Jane:0.005673000000000001):0.011836,	(Harry:0.005520000000000001,Sally:0.011163):0.007141999999999999);";
        NewickParser parser = new NewickParser();
        Newick newick = parser.parse(data);
        NewickWriter writer = new NewickWriter();
        System.out.print(writer.toString(newick));
    }
}
