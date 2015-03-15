/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.mafft.service.api;

import java.io.File;

/**
 *
 * @author dq
 */
public class MafftParams {
    private File in;
    private File out;
    // options
    private Integer retree ;
    private Integer maxiterate ;
    private Boolean parttree ;
    private Boolean nofft ;
    private Boolean localpair;
    private Boolean globalpair;
    private Boolean genafpair ;
    
    // parameters
    private Float op = 1.53f; // Gap opening penalty at group-to-group alignment
    private Float ep = 0.123f; // Offset value for group-to-group alignment
    private Float lop = -2.0f; //Gap opening penalty at local pairwise alignment
    private Float lep = 0.1f; //Offset value at local pairwise alignment
    private Float lexp = -0.1f; //Gap extension penalty at local pairwise alignment
    private Float genafOP = -6.00f; //Gap opening penalty. Valid when the --genafpair option is on
    private Float genafEXP = 0f; //Gap extension penalty. Valid when the --genafpair option is on
    private File aamatrix; //a user-defined AA scoring matrix
    
    @Override
    public String toString(){
        StringBuilder ret = new StringBuilder();  
        if (ep!= null){
            ret.append(" --ep ");
            ret.append(ep);
        }
        if(localpair != null && localpair){
            ret.append(" --localpair ");
        }
        if(globalpair != null && globalpair){
            ret.append(" --globalpair ");
        }
        if(genafpair != null && genafpair){
            ret.append(" --genafpair ");
        }
        if(retree != null){
            ret.append(" --retree ");
            ret.append(retree);
        }
        if (maxiterate != null){
            ret.append(" --maxiterate ");        
            ret.append(maxiterate);
        }
        if(parttree != null && parttree){
            ret.append(" --parttree ");
        }
        if(nofft != null && nofft){
            ret.append(" --nofft ");
        }
        ret.append(" --out ");
        ret.append(out.getAbsolutePath());
        ret.append(" ");
        ret.append(in.getAbsolutePath());
        return ret.toString();
    }

    public File getIn() {
        return in;
    }

    public void setIn(File in) {
        this.in = in;
    }

    public Integer getMaxiterate() {
        return maxiterate;
    }

    public void setMaxiterate(Integer maxiterate) {
        this.maxiterate = maxiterate;
    }

    public Boolean getNofft() {
        return nofft;
    }

    public void setNofft(Boolean nofft) {
        this.nofft = nofft;
    }

    public File getOut() {
        return out;
    }

    public void setOut(File out) {
        this.out = out;
    }

    public Boolean getParttree() {
        return parttree;
    }

    public void setParttree(Boolean parttree) {
        this.parttree = parttree;
    }

    public Integer getRetree() {
        return retree;
    }

    public void setRetree(Integer retree) {
        this.retree = retree;
    }
    
}
