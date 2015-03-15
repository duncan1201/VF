/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.seqlogo.service;

import com.gas.domain.core.fasta.Fasta;
import com.gas.domain.core.fasta.FastaParser;
import com.gas.seqlogo.service.api.Heights;
import com.gas.seqlogo.service.data.DataDumb;
import java.util.List;
import org.junit.*;

/**
 *
 * @author dq
 */
public class SeqLogoServiceTest {

    public SeqLogoServiceTest() {
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
     * Test of calculateHeights method, of class SeqLogoService.
     */
    //@Test
    public void testCalculateHeights_short_proteins() {
        System.out.println("testCalculateHeights_short_proteins");
        _run(DataDumb.class, DataDumb.SHORT_PROTEINS);
    }

    @Test
    public void testCalculateHeights_short_dnas() {
        System.out.println("testCalculateHeights_short_dnas");
        _run(DataDumb.class, DataDumb.SHORT_DNAS);
    }

    private void _run(Class clazz, String[] NAMES) {
        FastaParser parser = new FastaParser();
        for (int i = 0; i < NAMES.length; i++) {
            Fasta fasta = parser.parse(clazz, NAMES[i]);
            SeqLogoService instance = new SeqLogoService();
            List<Heights> result = instance.calculateHeights(fasta);
            for (int j = 0; j < result.size(); j++) {
                System.out.println(result.get(j));
            }
        }
    }
}
