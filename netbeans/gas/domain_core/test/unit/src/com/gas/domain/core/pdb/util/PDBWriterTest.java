/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.pdb.util;

import com.gas.domain.core.pdb.PDBDoc;
import java.io.File;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author dq
 */
public class PDBWriterTest {

    public PDBWriterTest() {
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
     * Test of toString method, of class PDBWriter.
     */
    @Test
    public void testToString() {
        PDBDoc s = PDBParser.parse(PDBWriterTest.class, "2FMM-from-pdb.pdb");
        String expResult = "";
        String result = PDBWriter.toString(s);
        System.out.println(result);
    }

    @Test
    public void testToFile() {
        PDBDoc s = PDBParser.parse(PDBWriterTest.class, "2FMM-from-pdb.pdb");
        PDBWriter.toFile(s, new File("D:\\tmp\\pdb\\my-2fmm.pdb"));

        s = PDBParser.parse(PDBWriterTest.class, "2YHH.pdb");
        PDBWriter.toFile(s, new File("D:\\tmp\\pdb\\my-2yhh.pdb"));
    }
}
