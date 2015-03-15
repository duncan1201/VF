/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.consensus.service;

import com.gas.domain.core.fasta.Fasta;
import com.gas.domain.core.fasta.FastaParser;
import com.gas.domain.core.msa.ConsensusParam;
import com.gas.msa.seqlogo.service.data.DataDumb;
import java.util.Iterator;
import org.junit.*;

/**
 *
 * @author dq
 */
public class ConsensusServiceTest {

    public ConsensusServiceTest() {
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
     * Test of calculate method, of class ConsensusService.
     */
    @Test
    public void testCalculate_dna() {
        System.out.println("calculate_dna");
        FastaParser parser = new FastaParser();
        for (int i = 0; i < DataDumb.SHORT_DNAS.length; i++) {
            Fasta fasta = parser.parse(DataDumb.class, DataDumb.SHORT_DNAS[i]);
            Iterator<Fasta.Record> itr = fasta.getRecords().iterator();
            while(itr.hasNext()){
                Fasta.Record r = itr.next();
                System.out.println(r.getSequence());
            }
            ConsensusParam param = new ConsensusParam();
            param.setPlurality(false);
            param.setThreshold(0.35f);
            ConsensusService instance = new ConsensusService();            
            String result = instance.calculate(fasta, param);
            System.out.println(result);
        }
    }
    
    @Test
    public void testCalculate_protein() {
        System.out.println("testCalculate_protein");
        FastaParser parser = new FastaParser();
        for (int i = 0; i < DataDumb.SHORT_PROTEINS.length; i++) {
            Fasta fasta = parser.parse(DataDumb.class, DataDumb.SHORT_PROTEINS[i]);
            Iterator<Fasta.Record> itr = fasta.getRecords().iterator();
            while(itr.hasNext()){
                Fasta.Record r = itr.next();
                System.out.println(r.getSequence());
            }
            ConsensusParam param = new ConsensusParam();
            param.setPlurality(true);
            param.setThreshold(0.35f);
            param.setIgnoreGaps(true);
            ConsensusService instance = new ConsensusService();            
            String result = instance.calculate(fasta, param);
            System.out.println(result);
        }
    }    
}
