/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.vector.advanced;

import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.util.AnnotatedSeqWriter;
import java.io.File;
import java.util.List;
import org.junit.*;

/**
 *
 * @author dq
 */
public class MolImportServiceTest {

    public MolImportServiceTest() {
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
     * Test of receiveXA4 method, of class MolImportService.
     */
    @Test
    public void testReceiveXA4() {
        System.out.println("receiveXA4");
        File file = null;
        MolImportService instance = new MolImportService();
        List expResult = null;
        List result = instance.receiveXA4(file);
        //assertEquals(expResult, result);
        //fail("The test case is a prototype.");
    }

    /**
     * Test of receive method, of class MolImportService.
     */
    @Test
    public void testReceive() {
        System.out.println("receive");
        File file = new File("D:\\tmp\\one_vector.ma4");
        MolImportService instance = new MolImportService();
        AnnotatedSeq expResult = null;
    }
}
