/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.aln;

import com.gas.domain.core.aln.Aln;
import com.gas.domain.core.aln.AlnIOService;
import com.gas.domain.core.misc.data.MiscDataDumb;
import org.junit.*;

/**
 *
 * @author dq
 */
public class AlnIOServiceTest {

    public AlnIOServiceTest() {
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
     * Test of parse method, of class AlnIOService.
     */
    //@Test
    public void testParse_Class_String() {
        System.out.println("testParse_Class_String");
        Class clazz = MiscDataDumb.class;
        String name = "dnaOutFile.aln";
        AlnIOService instance = new AlnIOService();
        Aln expResult = null;
        Aln result = instance.parse(clazz, name);
        System.out.println(result.getLength());
        System.out.println(result.getConservationLength());
        System.out.println(result);
    }

    @Test
    public void testParse_Class_String_Protein() {
        System.out.println("testParse_Class_String_Protein");
        Class clazz = MiscDataDumb.class;
        String name = "proteinOut.aln";
        AlnIOService instance = new AlnIOService();
        Aln result = instance.parse(clazz, name);
        System.out.println(result.getLength());
        System.out.println(result.getConservationLength());
        System.out.println(result);
    }
}
