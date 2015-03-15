/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.aln;

import com.gas.domain.core.aln.Aln;
import com.gas.domain.core.aln.AlnIOService;
import com.gas.domain.core.aln.AlnIOServiceTest;
import org.junit.*;

/**
 *
 * @author dq
 */
public class AlnTest {

    public AlnTest() {
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
     * Test of toString method, of class Aln.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        AlnIOService service = new AlnIOService();
        Aln aln = service.parse(AlnIOServiceTest.class, "dnaOutFile.aln");
        String result = aln.toString();
        System.out.println(result);
    }
}
