/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.filesystem;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dq
 */
public class FolderNames {

    public static final String ROOT = "Root";
    public static final String MY_DATA = "My Data";
    public static final String SAMPLE_DATA = "Sample Data";
    public static final String DEFAULT_NUCLEOTIDES_FOLDER = "Nucleotides";
    public static final String DEFAULT_PROTEINS_FOLDER = "Proteins";
    public static final String DEFAULT_ENZYMES_FOLDER = "Enzymes";
    public static final String DEFAULT_ALIGNMENTS_FOLDER = "Alignments";
    public static final String DEFAULT_STRUCTURES_FOLDER = "Structures";
    public static final String DEFAULT_NEB_FOLDER = "NEB plasmids";
    public static final String DEFAULT_ABSTRACTS_FOLDER = "Abstracts";
    public static final String DEFAULT_SHORTGUN_FOLDER = "Shortgun assemblies";    
    public static final String RECYCLE_BIN = "Recycle Bin";
    public static final String NCBI_ROOT = "NCBI";
    public static final String NCBI_NUCLEOTIDE = "Nucleotide";
    public static final String NCBI_PUBMED = "Pubmed";
    public static final String NCBI_PROTEIN = "Protein";
    public static final String NCBI_STRUCTURE = "Structure";
    public static final String NCBI_GENOME = "Genome";
    public static final List<String> NCBI_LEAVES = new ArrayList<String>();

    static {
        NCBI_LEAVES.add(NCBI_NUCLEOTIDE);
        NCBI_LEAVES.add(NCBI_PUBMED);
        NCBI_LEAVES.add(NCBI_PROTEIN);
        NCBI_LEAVES.add(NCBI_STRUCTURE);
        //NCBI_LEAVES.add(NCBI_GENOME);
    }
    public static final String DEFAULT_PDB = "PDB";
}
