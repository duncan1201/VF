/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.msa.clustalw;

import com.gas.common.ui.core.StringList;
import com.gas.common.ui.util.CommonUtil;
import java.io.File;

/**
 *
 * @author dq
 */
public class GeneralParam implements Cloneable {
    /*
     ***General settings:****
     -INTERACTIVE :read command line, then enter normal interactive menus
     -QUICKTREE   :use FAST algorithm for the alignment guide tree
     -TYPE=       :PROTEIN or DNA sequences
     -NEGATIVE    :protein alignment with negative values in matrix
     -OUTFILE=    :sequence alignment file name
     -OUTPUT=     :CLUSTAL(default), GCG, GDE, PHYLIP, PIR, NEXUS and FASTA
     -OUTORDER=   :INPUT or ALIGNED
     -CASE        :LOWER or UPPER (for GDE output only)
     -SEQNOS=     :OFF or ON (for Clustal output only)
     -SEQNO_RANGE=:OFF or ON (NEW: for all output formats)
     -RANGE=m,n   :sequence range to write starting m to m+n
     -MAXSEQLEN=n :maximum allowed input sequence length
     -QUIET       :Reduce console output to minimum
     -STATS=      :Log some alignents statistics to file 
     */

    private Integer hibernateId;
    private boolean quickTree = false;
    private transient boolean quite = true;
    private String dataType;// for hibernate use only
    private transient OUTPUT output = OUTPUT.CLUSTAL;
    private transient File outfile;

    @Override
    public GeneralParam clone() {
        GeneralParam ret = CommonUtil.cloneSimple(this);
        return ret;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append("-TYPE=");
        ret.append(dataType);
        ret.append(' ');
        ret.append("-OUTPUT=");
        ret.append(output);
        ret.append(' ');
        if (outfile != null) {
            ret.append("-OUTFILE=");
            ret.append(outfile.getAbsolutePath());
        }
        ret.append(' ');
        if (quickTree) {
            ret.append("-QUICKTREE");
            ret.append(' ');
        }
        if (quite) {
            ret.append("-QUIET");
            ret.append(' ');
        }
        return ret.toString();
    }

    public Integer getHibernateId() {
        return hibernateId;
    }

    public void setHibernateId(Integer hibernateId) {
        this.hibernateId = hibernateId;
    }

    public File getOutfile() {
        return outfile;
    }

    public void setOutfile(File outfile) {
        this.outfile = outfile;
    }

    public OUTPUT getOutput() {
        return output;
    }

    public void setOutput(OUTPUT output) {
        this.output = output;
    }

    /**
     * for hibernate use only
     */
    protected String getDataType() {
        return dataType;
    }

    /**
     * for hibernate use only
     */
    protected void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public TYPE getType() {
        return TYPE.get(dataType);
    }

    public void setType(TYPE type) {
        this.dataType = type == null ? null : type.toString();
    }

    public boolean isQuickTree() {
        return quickTree;
    }

    public void setQuickTree(boolean quickTree) {
        this.quickTree = quickTree;
    }

    public void setQuite(boolean quite) {
        this.quite = quite;
    }

    public enum OUTPUT {

        CLUSTAL, GCG, GDE, PHYLIP, PIR, NEXUS, FASTA
    };

    public enum TYPE {

        DNA, PROTEIN;

        public static TYPE get(String str) {
            if (PROTEIN.toString().equalsIgnoreCase(str)) {
                return PROTEIN;
            } else if (DNA.toString().equalsIgnoreCase(str)) {
                return DNA;
            }
            return null;
        }
    };

    public enum DNA_WEIGHT_MATRIX {

        IUB, CLUSTALW;

        public static DNA_WEIGHT_MATRIX get(String str) {
            if (IUB.toString().equalsIgnoreCase(str)) {
                return IUB;
            } else if (CLUSTALW.toString().equalsIgnoreCase(str)) {
                return CLUSTALW;
            }
            return null;
        }
    };

    public enum PRO_WEIGHT_MATRIX {

        BLOSUM, PAM, GONNET, ID;

        public static PRO_WEIGHT_MATRIX get(String str) {
            if (BLOSUM.toString().equalsIgnoreCase(str)) {
                return BLOSUM;
            } else if (PAM.toString().equalsIgnoreCase(str)) {
                return PAM;
            } else if (GONNET.toString().equalsIgnoreCase(str)) {
                return GONNET;
            } else if (ID.toString().equalsIgnoreCase(str)) {
                return ID;
            }
            return null;
        }
    };

    public enum ITERATION {

        NONE, TREE, ALIGNMENT;

        public static ITERATION get(String str) {
            if (NONE.toString().equalsIgnoreCase(str)) {
                return NONE;
            } else if (TREE.toString().equalsIgnoreCase(str)) {
                return TREE;
            } else if (ALIGNMENT.toString().equalsIgnoreCase(str)) {
                return ALIGNMENT;
            }
            return null;
        }
    }

    public enum CLUSTERING {

        UPGMA("UPGMA", "UPGMA"), NJ("NJ", "Neighbour Joining");
        String name;
        String desc;

        CLUSTERING(String name, String desc) {
            this.name = name;
            this.desc = desc;
        }

        public String getName() {
            return name;
        }

        public String getDesc() {
            return desc;
        }

        public static StringList getAllNames() {
            StringList ret = new StringList();
            CLUSTERING[] all = values();
            for (CLUSTERING c : all) {
                ret.add(c.getName());
            }
            return ret;
        }

        public static StringList getAllDescs() {
            StringList ret = new StringList();
            CLUSTERING[] all = values();
            for (CLUSTERING c : all) {
                ret.add(c.getDesc());
            }
            return ret;
        }

        public static CLUSTERING get(String name) {
            CLUSTERING ret = null;
            CLUSTERING[] all = values();
            for (CLUSTERING c : all) {
                if (c.getName().equals(name)) {
                    ret = c;
                    break;
                }
            }
            return ret;
        }
    }
}
