/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.pdb.util;

import com.gas.common.ui.util.CommonUtil;
import com.gas.domain.core.pdb.PDBDoc;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author dq
 */
public class PDBParserTest {

    public PDBParserTest() {
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

    @Test
    public void testParse2() throws Exception {
        PDBDoc pdbDoc = PDBParser.parse(PDBParserTest.class, "2FMM-from-pdb.pdb");
        System.out.print("");
    }
}
