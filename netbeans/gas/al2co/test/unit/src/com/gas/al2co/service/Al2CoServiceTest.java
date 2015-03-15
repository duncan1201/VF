/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.al2co.service;

import com.gas.al2co.service.api.Al2CoParams;
import com.gas.al2co.service.data.DataDumb;
import com.gas.common.ui.core.FloatList;
import com.gas.common.ui.util.FileHelper;
import java.io.File;
import org.junit.*;

/**
 *
 * @author dq
 */
public class Al2CoServiceTest {

    private static File in;
    private static File out;

    public Al2CoServiceTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        in = FileHelper.toFile(DataDumb.class.getResourceAsStream(DataDumb.NAMES[0]));        
        out = new File("D:\\tmp\\al2co_out.txt");
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
     * Test of calculateConservation method, of class Al2CoService.
     */
    @Test
    public void testCalculateConservation() {
        System.out.println("calculateConservation");
        Al2CoParams params = new Al2CoParams();
        params.setIn(in);
        params.setOut(out);
        Al2CoService instance = new Al2CoService();
        FloatList ret = instance.calculateConservation(params);
        // TODO review the generated test code and remove the default call to fail.
        System.out.print("");
    }
}
