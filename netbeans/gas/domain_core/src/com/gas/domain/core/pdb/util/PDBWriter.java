/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.pdb.util;

import com.gas.common.ui.util.FileHelper;
import com.gas.domain.core.pdb.PDBDoc;
import java.io.File;

/**
 *
 * @author dq
 */
public class PDBWriter {

    public static String toString(PDBDoc s) {
        String str = s.toString();
        return str;
    }

    public static void toFile(PDBDoc s, File f) {
        String str = toString(s);
        FileHelper.toFile(f, str);
    }
    /*
     public static PDBDoc clone(PDBDoc o){
     PDBDoc ret = null;
     String str = toString(o);
     ret = PDBParser.parse(str);
     ret.setModified(o.getModified());
     return ret;
     }
     
     */
}
