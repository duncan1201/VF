/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.as;

import com.gas.common.ui.util.ReflectHelper;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author dq
 */
public class FetureKeyCnst {

    public static final String attB1 = "attB1";
    public static final String attB1r = "attB1r";
    public static final String attB2 = "attB2";
    public static final String attB2r = "attB2r";
    public static final String attB3 = "attB3";
    public static final String attB3r = "attB3r";
    public static final String attB4 = "attB4";
    public static final String attB4r = "attB4r";
    public static final String attB5 = "attB5";
    public static final String attB5r = "attB5r";
    public static final String attP1 = "attP1";
    public static final String attP1r = "attP1r";
    public static final String attP2 = "attP2";
    public static final String attP2r = "attP2r";
    public static final String attP3 = "attP3";
    public static final String attP3r = "attP3r";
    public static final String attP4 = "attP4";
    public static final String attP4r = "attP4r";
    public static final String attP5 = "attP5";
    public static final String attP5r = "attP5r";
    public static final String attL1 = "attL1";
    public static final String attL2 = "attL2";
    public static final String attL3 = "attL3";
    public static final String attL4 = "attL4";
    public static final String attL5 = "attL5";
    public static final String attR1 = "attR1";
    public static final String attR2 = "attR2";
    public static final String attR3 = "attR3";
    public static final String attR4 = "attR4";
    public static final String attR5 = "attR5";
    public static final String BETA_STRAND_REGION = "Beta-strand region";
    public static final String BINDING_SITE_MISC = "Binding Site: Misc";
    public static final String CENTROMERE = "Centromere";
    public static final String Conflict = "Conflict";
    public static final String Connecting_Peptide = "Connecting Peptide";
    public static final String CDS = "CDS";
    public static final String Cytoplasmic = "Cytoplasmic";
    public static final String Disulfide = "Disulfide";
    public static final String DOMAIN_MISC = "Domain:_Misc";
    public static final String Enhancer = "Enhancer";
    public static final String Extracellular = "Extracellular";
    public static final String Gene = "Gene";    
    public static final String Glycosylation = "Glycosylation";
    public static final String Helical_region = "Helical region";
    public static final String Hydrogen_bonded_turn = "Hydrogen bonded turn";
    public static final String KOZAK = "Kozak";
    public static final String LIGATION = "Ligation";
    public static final String Mature_chain = "Mature chain";
    public static final String MISC_FEATURE = "Misc. Feature";
    public static final String MISC_RECOMBINATION = "Misc. Recombination";
    public static final String MISC_STRUCTURE = "Misc. Structure";
    public static final String ORF = "ORF";
    public static final String OVERHANG = "Overhang";
    public static final String OVERLAPPING_PRIMER = "Overlapping primer";
    public static final String Parent = "Parent";
    public static final String Phosphorylation = "Phosphorylation";
    public static final String PolyA_Signal = "PolyA Signal";
    public static final String PolyA_Site = "PolyA Site";
    public static final String Primer = "Primer";
    /**
     * Used for everything EXCEPT importing Vector NTI Advanced
     */
    public static final String PRIMER_BINDING_SITE = "primer bind";    
    /**
     * Used for import Vector NTI Advanced
     */
    public static final String Primer_Binding_Site = "Primer Binding Site";
    public static final String Promoter_Eukaryotic = "Promoter Eukaryotic";
    public static final String Promoter_Prokaryotic = "Promoter Prokaryotic";
    public static final String Protein = "Protein";
    public static final String Protein_Binding_Site = "Protein Binding Site";
    public static final String REGION_MISC = "Region:_Misc";
    public static final String Replication_Origin = "Replication Origin";
    public static final String RBS = "RBS";
    public static final String RECOGNITION_SITE = "REN cut site";
    public static final String Repetitive_region = "Repetitive region";
    public static final String TATA_signal = "TATA signal";
    public static final String SHINE_DALGARNO = "Shine-Dalgarno";
    public static final String SD = SHINE_DALGARNO;
    public static final String Signal_Sequence = "Signal Sequence";
    public static final String Source = "Source";    
    public static final String Splicing_Variant = "Splicing Variant";
    public static final String Terminator = "Terminator";
    public static final String Transmembrane_region = "Transmembrane region";
    public static final String Variation = "Variation";

    public static List<FetureKey> getAll() {
        List<FetureKey> ret = new ArrayList<FetureKey>();
        List<Field> fields = ReflectHelper.getDeclaredFields(FetureKeyCnst.class, String.class, Boolean.TRUE);
        for (Field fd : fields) {
            ret.add(new FetureKey(ReflectHelper.getStaticQuietly(fd, String.class)));
        }
        return ret;
    }

    public static Set<String> getAllNames() {
        Set<String> ret = new HashSet<String>();
        List<FetureKey> fks = getAll();
        for (FetureKey fk : fks) {
            ret.add(fk.getName());
        }
        return ret;
    }
}
