/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.misc;

import java.io.File;
import java.io.FilenameFilter;

/**
 *
 * @author dq
 */
public class FilenameExtFilter implements FilenameFilter {

    private String ext;

    public FilenameExtFilter(String ext) {
        this.ext = ext;
    }

    @Override
    public boolean accept(File dir, String name) {
        int indexPeriod = name.indexOf(".");;
        if (indexPeriod < 0) {
            return false;
        } else if (indexPeriod == name.length() - 1) {
            return false;
        } else {
            String _ext = name.substring(indexPeriod + 1);
            return _ext.equalsIgnoreCase(ext);
        }
    }
}
