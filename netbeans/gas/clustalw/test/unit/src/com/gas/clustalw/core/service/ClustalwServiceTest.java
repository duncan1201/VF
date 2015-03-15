/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.clustalw.core.service;

import com.gas.domain.core.msa.clustalw.ClustalwParam;
import com.gas.domain.core.msa.clustalw.DataParams;
import com.gas.domain.core.msa.clustalw.GeneralParam;
import com.gas.domain.core.msa.clustalw.ClustalTreeParam;
import com.gas.clustalw.core.service.aln.AlnDumb;
import com.gas.clustalw.core.service.fasta.FastaDumb;
import com.gas.clustalw.core.service.proteins.ProteinsDumb;
import com.gas.clustalw.core.service.seqs.SeqsDumb;
import com.gas.clustalw.core.service.seqs2.Seqs2;
import com.gas.common.ui.util.FileHelper;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.util.AnnotatedSeqParser;
import com.gas.domain.core.as.AsHelper;
import com.gas.domain.core.fasta.Fasta;
import com.gas.domain.core.fasta.FastaParser;
import com.gas.domain.core.flexrs.FlexGenbankFormat;
import com.gas.domain.core.aln.Aln;
import com.gas.domain.core.aln.IAlnIOService;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.junit.*;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public class ClustalwServiceTest {

    public ClustalwServiceTest() {
    }
    private static Fasta fasta;
    private static Fasta fasta2;
    private static List<Aln> alns = null;
    private static File alnFile;
    private static File alnFile2;
    private static File treeOutFile;
    private static File treeOutFile2;
    private static File fastaFile;
    private static File fastaFile2;
    private static File fastaFile3;
    private static File fasta_aligned;
    private static File out_file_fasta;
    private static File out_file_clustal_2;
    private static File out_file_clustal_3;
    private static File out_file_phylip;
    private static File msa_out_file_nexus;
    private static File profile_outfile;

    @BeforeClass
    public static void setUpClass() throws Exception {
        List<AnnotatedSeq> seqs = getSeqs(SeqsDumb.class, SeqsDumb.NAMES);
        List<AnnotatedSeq> seqs2 = getSeqs(Seqs2.class, Seqs2.NAMES);
        List<AnnotatedSeq> seqs3 = getSeqs(ProteinsDumb.class, ProteinsDumb.NAMES);
        alns = getAlns();
        alnFile = FileHelper.toFile(alns.get(0).toString());
        alnFile2 = FileHelper.toFile(alns.get(1).toString());
        treeOutFile = new File("D:\\tmp\\tree.txt");
        treeOutFile2 = new File("D:\\tmp\\tree2.txt");
        fasta = AsHelper.toFasta(seqs);
        fasta2 = AsHelper.toFasta(seqs2);
        fastaFile = FileHelper.toFile(fasta.toString(60));
        fastaFile2 = FileHelper.toFile(fasta2.toString(60));
        fastaFile3 = FileHelper.toFile(AsHelper.toFasta(seqs3).toString(60));

        out_file_fasta = new File("D:\\tmp\\dnaOutFile.fasta");
        out_file_clustal_2 = new File("D:\\tmp\\dnaOutFile2.aln");
        out_file_clustal_3 = new File("D:\\tmp\\proteinOutFile.aln");
        out_file_phylip = new File("D:\\tmp\\dnaOutFile.phylip");
        msa_out_file_nexus = new File("D:\\tmp\\dnaOutFile.nexus");

        profile_outfile = new File("D:\\tmp\\profile.txt");
        
        List<Fasta> fastas = getFastas();       
        fasta_aligned = FileHelper.toFile(fastas.get(0).toString(60));
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testMsa() {
        System.out.println("msa");
        _testMsa(fastaFile, GeneralParam.OUTPUT.FASTA, out_file_fasta);
        _testMsa(fastaFile, GeneralParam.OUTPUT.PHYLIP, out_file_phylip);
        _testMsa(fastaFile, GeneralParam.OUTPUT.NEXUS, msa_out_file_nexus);
        _testMsa(fastaFile2, GeneralParam.OUTPUT.CLUSTAL, out_file_clustal_2);
        _testMsa(fastaFile3, GeneralParam.OUTPUT.CLUSTAL, out_file_clustal_3);
        System.out.print("");
    }

    //@Test
    public void testPhylogeneticTree_fasta() {
        System.out.println("testPhylogeneticTree_fasta");
        ClustalTreeParam treeParams = new ClustalTreeParam();

        treeParams.setInfile(fasta_aligned);

        ClustalwService instance = new ClustalwService();
        instance.phylogeneticTree(treeParams);
    }

    //@Test
    public void testPhylogeneticTree_aln() {
        System.out.println("testPhylogeneticTree_aln");
        ClustalTreeParam treeParams = new ClustalTreeParam();

        treeParams.setInfile(alnFile);

        ClustalwService instance = new ClustalwService();
        instance.phylogeneticTree(treeParams);
    }

    private void _testMsa(File infile, GeneralParam.OUTPUT output, File outfile) {
        ClustalwParam msaParams = new ClustalwParam();
        DataParams dataParams = msaParams.getDataParams();
        GeneralParam generalParams = msaParams.getGeneralParam();
        ClustalwParam.Fast fast = msaParams.getFast();
        ClustalwParam.Multiple multiple = msaParams.getMultiple();

        dataParams.setInfile(infile);
        generalParams.setQuickTree(true);
        generalParams.setOutput(output);
        generalParams.setOutfile(outfile);

        ClustalwService instance = new ClustalwService();
        instance.msa(msaParams);
    }

    private static List<Aln> getAlns() {
        IAlnIOService service = Lookup.getDefault().lookup(IAlnIOService.class);
        List<Aln> ret = new ArrayList<Aln>();
        for (String name : AlnDumb.NAMES) {
            Aln aln = service.parse(AlnDumb.class, name);
            ret.add(aln);
        }
        return ret;
    }

    private static List<Fasta> getFastas() {
        List<Fasta> ret = new ArrayList<Fasta>();
        for (String name : FastaDumb.OUT_DNA) {
            FastaParser parser = new FastaParser();
            Fasta fasta = parser.parse(FastaDumb.class, name);
            ret.add(fasta);
        }
        return ret;
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
