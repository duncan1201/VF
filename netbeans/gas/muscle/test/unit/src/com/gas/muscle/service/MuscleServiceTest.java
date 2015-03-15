/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.muscle.service;

import com.gas.common.ui.util.FileHelper;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.util.AnnotatedSeqParser;
import com.gas.domain.core.as.AsHelper;
import com.gas.domain.core.fasta.Fasta;
import com.gas.domain.core.fasta.FastaParser;
import com.gas.domain.core.flexrs.FlexGenbankFormat;
import com.gas.muscle.service.api.IMuscleService;
import com.gas.domain.core.msa.muscle.MuscleParam;
import com.gas.muscle.service.proteins.AA_DATA;
import com.gas.muscle.service.seqs.SeqsDumb;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.junit.*;

/**
 *
 * @author dq
 */
public class MuscleServiceTest {

    static File inDNA;
    static File outDNA;
    static File inAA;
    static File outAA;

    public MuscleServiceTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {

        FastaParser parser = new FastaParser();
        Fasta fasta = parser.parse(SeqsDumb.class, "fasta_01.fasta");
        inDNA = FileHelper.toFile(fasta.toString());
        outDNA = new File("D:\\tmp\\muscle_dna.txt");

        Collection<AnnotatedSeq> asList = getSeqs(AA_DATA.class, AA_DATA.NAMES);
        Fasta proteinFasta = AsHelper.toFasta(asList);
        inAA = FileHelper.toFile(proteinFasta.toString());
        FileHelper.toFile(new File("D:\\tmp\\protein_fasta.txt"), proteinFasta.toString());
        outAA = new File("D:\\tmp\\muscle_aa.txt");
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
     * Test of align method, of class MuscleService.
     */
    @Test
    public void testAlign_DNA() {
        System.out.println("testAlign_DNA");
        MuscleParam params = new MuscleParam();
        params.setIn(inDNA);
        params.setOut(outDNA);
        params.setSeqType(MuscleParam.SEQ_TYPE.AUTO);
        params.setOutFormat(MuscleParam.OUT_FORMAT.fasta);
        MuscleService instance = new MuscleService();
        instance.align(params);
    }
    
    /**
     * Test of align method, of class MuscleService.
     */
    @Test
    public void testAlign_AA() {
        System.out.println("testAlign_AA");
        MuscleParam params = new MuscleParam();        
        params.setIn(inAA);
        params.setOut(outAA);
        params.setSeqType(MuscleParam.SEQ_TYPE.PROTEIN);
        params.setOutFormat(MuscleParam.OUT_FORMAT.fasta);
        MuscleService instance = new MuscleService();
        instance.align(params);
    }    

    private static List<AnnotatedSeq> getSeqs(Class clazz, String[] names) {
        List<AnnotatedSeq> ret = new ArrayList<AnnotatedSeq>();
        for (String name : names) {
            AnnotatedSeq as = AnnotatedSeqParser.singleParse(clazz, name, new FlexGenbankFormat());
            ret.add(as);
        }
        return ret;
    }
}
