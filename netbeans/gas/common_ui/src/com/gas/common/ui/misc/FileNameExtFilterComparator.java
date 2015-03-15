/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.misc;

import java.util.Comparator;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author dq
 */
public class FileNameExtFilterComparator implements Comparator<FileNameExtensionFilter> {

    @Override
    public int compare(FileNameExtensionFilter o1, FileNameExtensionFilter o2) {
        return o1.getDescription().compareTo(o2.getDescription());
    }
}
