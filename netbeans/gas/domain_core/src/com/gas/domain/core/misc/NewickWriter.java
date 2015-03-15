/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.misc;

import com.gas.common.ui.util.FileHelper;
import com.gas.domain.core.misc.api.Newick;
import java.io.File;

/**
 *
 * @author dq
 */
public class NewickWriter {

    public String toString(Newick n) {
        return n.toString();
    }

    public void toFile(Newick n, File file) {
        FileHelper.toFile(file, n.toString());
    }
}
