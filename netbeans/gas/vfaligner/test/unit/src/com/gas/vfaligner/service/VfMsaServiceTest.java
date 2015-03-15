/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.vfaligner.service;

import com.gas.domain.core.msa.MSA;
import com.gas.domain.core.msa.vfmsa.VfMsaParam;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.biojava3.core.sequence.ProteinSequence;
import org.biojava3.core.sequence.io.FastaReaderHelper;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author dq
 */
public class VfMsaServiceTest {
    
    public VfMsaServiceTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @Before
    public void setUp() {
    }    
    
    public void testGetMSA2(){
        
    }

    /**
     * Test of getMSA method, of class VfMsaService.
     */
    @Test
    public void testGetMSA() throws Exception {
        System.out.println("getMSA");
        ArrayList<ProteinSequence> sequence = new ArrayList<ProteinSequence>();
        LinkedHashMap<String, ProteinSequence> tmp = FastaReaderHelper.readFastaProteinSequence(VfMsaServiceTest.class.getResourceAsStream("O48771.fasta"));        
        sequence.addAll(tmp.values());
        
        tmp = FastaReaderHelper.readFastaProteinSequence(VfMsaServiceTest.class.getResourceAsStream("Q21495.fasta"));
        sequence.addAll(tmp.values());
        
        tmp = FastaReaderHelper.readFastaProteinSequence(VfMsaServiceTest.class.getResourceAsStream("Q21691.fasta"));
        sequence.addAll(tmp.values());
        VfMsaParam params = new VfMsaParam();
        
        VfMsaService instance = new VfMsaService();
        MSA expResult = null;
        MSA result = instance._getMSA(sequence, params);
        System.out.println();
    }
}