/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui;

import com.gas.common.ui.core.StringList;

/**
 *
 * @author dq
 */
public enum FileFormat {

    ABX(new String[]{"abi", "ab1"}, "ABI sequencer trace files(*.abi, *.ab1)"),
    APR(new String[]{"apr"}, "Vector NTI Alignment Projects(*.apr)"),
    CLUSTAL(new String[]{"clustal"}, "Clustal(*.clustal)"),
    EMBL(new String[]{"embl", "txt"}, "EMBL(*.embl, *.txt)"),
    FASTA(new String[]{"fasta", "fas", "fa", "txt"}, "Fasta(*.fasta, *.fas, *.fa, *.txt)"),
    GenBank(new String[]{"gb", "txt"}, "Genbank(*.gb, *.txt)"),
    GenPept(new String[]{"gp", "txt"}, "Genbank(*.gp, *.txt)"),
    NEWICK(new String[]{"newick"}, "Newick(*.newick)"),
    NEXUS(new String[]{"nexus", "nex", "nxs"}, "Nexus(*.nexus, *.nex, *.nxs)"), 
    SCF(new String[]{"scf"}, "Standard sequencer trace files(*.scf)"),
    VNTI_NUCLEOTIDES(new String[]{"ma4"}, "Vector NTI DNA/RNA Archives(*.ma4)"),
    VNTI_PROTEINS(new String[]{"pa4"}, "Vector NTI Protein Archives(*.pa4)"),
    VNTI_OLIGOS(new String[]{"oa4"}, "Vector NTI Oligo Archives(*.oa4)");
    String[] exts;
    String desc;

    /**
     * @param ext: example values are: gb, fasta
     */
    FileFormat(String[] exts, String desc) {
        this.exts = exts;
        this.desc = desc;
    }

    public String[] getExts() {
        return this.exts;
    }

    public String getDesc() {
        return this.desc;
    }

    public static FileFormat get(String ext) {
        FileFormat ret = null;
        FileFormat[] fts = values();
        for (FileFormat ft : fts) {
            StringList _exts = new StringList(ft.getExts());
            if (_exts.contains(ext)) {
                ret = ft;
            }
        }
        return ret;
    }
    
    public static FileFormat getByDesc(String desc){
        FileFormat ret = null;
        FileFormat[] fts = values();
        for (FileFormat ft : fts) {
            if(desc.equals(ft.getDesc())){
                ret = ft;
                break;
            }
        }
        return ret;
    }
}
