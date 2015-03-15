package org.biojava;

import org.biojava.bio.seq.DNATools;
import org.biojava.bio.seq.ProteinTools;
import org.biojava.bio.seq.RNATools;
import org.biojava.bio.symbol.IllegalSymbolException;

public class SymbolsValidator {

    public static boolean validateDNA(String dna) {
        boolean ret = false;
        try {
            DNATools.createDNA(dna);
            ret = true;
        } catch (IllegalSymbolException e) {
            ret = false;
        }
        return ret;
    }

    public static boolean validateRNA(String rna) {
        boolean ret = false;
        try {
            RNATools.createRNA(rna);
            ret = true;
        } catch (IllegalSymbolException e) {
            ret = false;
        }
        return ret;
    }

    public static boolean validateProtein(String protein) {
        boolean ret = false;
        try {
            ProteinTools.createProtein(protein);
            ret = true;
        } catch (IllegalSymbolException e) {
            ret = false;
        }
        return ret;
    }

    public static void aaa() {
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        String a = "[a|t|c|g|]+";
        String dna = "aaattt";
        String dna2 = "aaatttx";
        System.out.println(dna.matches(a));
        System.out.println(dna2.matches(a));

    }
}
