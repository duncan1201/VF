/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.msa.vfmsa;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dq
 */
public enum SubMatrix {

    Blosum30("blosum30", "blosum30", true),
    Blosum35("blosum35", "blosum35", true),
    Blosum40("blosum40", "blosum40", true),
    Blosum45("blosum45", "blosum45", true),
    Blosum50("blosum50", "blosum50", true),
    Blosum55("blosum55", "blosum55", true),
    Blosum60("blosum60", "blosum60", true),
    Blosum62("blosum62", "blosum62", true),
    Blosum65("blosum65", "blosum65", true),
    Blosum70("blosum70", "blosum70", true),
    Blosum75("blosum75", "blosum75", true),
    Blosum80("blosum80", "blosum80", true),
    Blosum85("blosum85", "blosum85", true),
    Blosum90("blosum90", "blosum90", true),
    Blosum100("blosum100", "blosum100", true),
    Gonnet250("gonnet250", "gonnet250", true),
    PAM250("blosum100", "blosum100", true),
    nuc4_2("nuc-4_2", "nuc-4_2", false),
    nuc4_4("nuc-4_4", "nuc-4_4", false);
    String internalName;
    String displayName;
    boolean aminoAcid;// amino acid

    SubMatrix(String internalName, String displayName, boolean aminoAcid) {
        this.internalName = internalName;
        this.displayName = displayName;
        this.aminoAcid = aminoAcid;
    }
    
    public String getInternalName(){
        return this.internalName;
    }

    public static SubMatrix getByDisplayName(String displayName) {
        SubMatrix ret = null;
        SubMatrix[] matrixes = SubMatrix.values();
        for (SubMatrix matrix : matrixes) {
            if (matrix.getDisplayName().equalsIgnoreCase(displayName)) {
                ret = matrix;
                break;
            }
        }
        return ret;
    }

    public boolean isAminoAcid() {
        return aminoAcid;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static String[] getDisplayNames(boolean aminoAcids) {
        List<String> ret = new ArrayList<String>();
        SubMatrix[] all = SubMatrix.values();
        for (SubMatrix m : all) {
            if (aminoAcids) {
                if (m.isAminoAcid()) {
                    ret.add(m.getDisplayName());
                }
            } else {
                if (!m.isAminoAcid()) {
                    ret.add(m.getDisplayName());
                }
            }

        }
        return ret.toArray(new String[ret.size()]);
    }
}
