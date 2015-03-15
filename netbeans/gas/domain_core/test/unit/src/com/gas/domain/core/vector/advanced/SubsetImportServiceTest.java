/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.vector.advanced;

import java.io.File;
import java.util.Map;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author dq
 */
public class SubsetImportServiceTest {

    public SubsetImportServiceTest() {
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
     * Test of importProteinSubsets method, of class SubsetImportService.
     */
    @Test
    public void testImportProteinSubsets() {
        System.out.println("importProteinSubsets");
        File dbDir = new File("C:\\VNTI Database");
        SubsetImportService instance = new SubsetImportService();
        Map expResult = null;
        Map result = instance.importProteinSubsets(dbDir);
        System.out.print("");
    }

    /**
     * Test of importNucleotideSubsets method, of class SubsetImportService.
     */
    @Test
    public void testImportNucleotideSubsets() {
        System.out.println("importNucleotideSubsets");
        File dbDir = new File("C:\\VNTI Database");
        SubsetImportService instance = new SubsetImportService();
        Map expResult = null;
        Map result = instance.importNucleotideSubsets(dbDir);
        System.out.print("");
    }
}
