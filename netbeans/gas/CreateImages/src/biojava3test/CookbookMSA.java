/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package biojava3test;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.biojava3.alignment.Alignments;
import org.biojava3.alignment.Alignments.PairwiseSequenceScorerType;
import org.biojava3.alignment.SimpleGapPenalty;
import org.biojava3.alignment.SubstitutionMatrixHelper;
import org.biojava3.alignment.template.GapPenalty;
import org.biojava3.alignment.template.Profile;
import org.biojava3.alignment.template.SubstitutionMatrix;
import org.biojava3.core.sequence.ProteinSequence;
import org.biojava3.core.sequence.compound.AminoAcidCompound;
import org.biojava3.core.sequence.io.FastaReaderHelper;
import org.biojava3.core.sequence.template.CompoundSet;
import org.biojava3.core.util.ConcurrencyTools;

/**
 *
 * @author dq
 */
public class CookbookMSA {
 
    public static void main(String[] args) {
        String[] ids = new String[] {"Q21691", "Q21495", "O48771"};
        try {
            multipleSequenceAlignment(ids);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
 
    private static void multipleSequenceAlignment(String[] ids) throws Exception {
        List<ProteinSequence> lst = new ArrayList<ProteinSequence>();
        for (String id : ids) {
            lst.add(getSequenceForId(id));
        }
        
        //PairwiseSequenceScorerType scorerType = PairwiseSequenceScorerType.GLOBAL_SIMILARITIES;
        PairwiseSequenceScorerType scorerType = PairwiseSequenceScorerType.LOCAL;
        GapPenalty gapPenalty = new SimpleGapPenalty();
        SubstitutionMatrix matrix = SubstitutionMatrixHelper.getBlosum62();
        Object[] settings = new Object[]{scorerType, gapPenalty, matrix};
        
        CompoundSet cs1 = lst.get(0).getCompoundSet();
        CompoundSet cs2 = matrix.getCompoundSet();
        if(cs1 == cs2){
            System.out.println("cs1 == cs2");
        } else {
            System.out.println("cs1 != cs2");
        }
        Profile<ProteinSequence, AminoAcidCompound> profile = Alignments.getMultipleSequenceAlignment(lst, settings);
        //System.out.printf("Clustalw:%n%s%n", profile);
        ConcurrencyTools.shutdown();
    }
 
    private static ProteinSequence getSequenceForId(String uniProtId) throws Exception {
        URL uniprotFasta = new URL(String.format("http://www.uniprot.org/uniprot/%s.fasta", uniProtId));
        ProteinSequence seq = FastaReaderHelper.readFastaProteinSequence(uniprotFasta.openStream()).get(uniProtId);
        System.out.printf("id : %s %s%n%s%n", uniProtId, seq, seq.getOriginalHeader());
        return seq;
    }
 
}
