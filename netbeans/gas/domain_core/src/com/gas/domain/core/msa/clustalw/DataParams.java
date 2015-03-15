/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.msa.clustalw;

import java.io.File;

/**
 *
 * @author dq
 */
public class DataParams {

    /**
     * DATA (sequences)
     *
     * -INFILE=file.ext :input sequences. -PROFILE1=file.ext and
     * -PROFILE2=file.ext :profiles (old alignment).
     */
    private transient File infile;
    private transient File profile1;
    private transient File profile2;

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        if (infile != null) {
            ret.append("-INFILE=");
            ret.append(infile.getAbsolutePath());
        }
        if (profile1 != null) {
            ret.append("-PROFILE1=");
            ret.append(profile1.getAbsolutePath());
            ret.append(' ');
        }
        if (profile2 != null) {
            ret.append("-PROFILE2=");
            ret.append(profile2.getAbsolutePath());
            ret.append(' ');
        }
        return ret.toString();
    }

    public File getInfile() {
        return infile;
    }

    public File getProfile1() {
        return profile1;
    }

    public File getProfile2() {
        return profile2;
    }

    public void setInfile(File infile) {
        this.infile = infile;
    }

    public void setProfile1(File profile1) {
        this.profile1 = profile1;
    }

    public void setProfile2(File profile2) {
        this.profile2 = profile2;
    }
}
