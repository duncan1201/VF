/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.geneticCode;

import com.gas.domain.core.geneticCode.api.GeneticCodeTable;
import java.util.Comparator;
import java.util.List;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author dq
 */
public class GeneticCodeTableServiceTest {

    private String nucleotides_01 = "GGAUACAGAAGUGAUGUGAGUGUCUGCGAGCGUGGUCUCGCGGGCGGGUGACGUCUUAAUAAUCAGAACAGACCCAAACUUUA";
    private String nucleotides_gap = "G--GA-UA--C-AGAAG-UG-AUGU---GAGUGUC-UG-CGA--GCGUGG--UCUC-GCGG--GCG-G-GUGACGUCU-UAAUAAUCAGAACAGACCCAAACUUUA";

    public GeneticCodeTableServiceTest() {
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
     * Test of translate method, of class GeneticCodeTableService.
     */
    @Test
    public void testTranslate_String_int() {
        System.out.println("translate");
        String[] dnas = {nucleotides_01, nucleotides_gap};
        int[] tableIds = {1};
        GeneticCodeTableService instance = new GeneticCodeTableService();
        for (String dna : dnas) {
            String result = instance.translate(dna, tableIds[0]);
            System.out.println(dna);
            System.out.println(result);
        }
    }
}
