/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core;

import com.gas.common.ui.FileFormat;
import java.util.Arrays;
import java.util.EnumSet;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public class FileImportServiceFinder {

    private FileImportServiceFinder(){};
    
    public static IFileImportService find(FileFormat format){
        IFileImportService ret = null;
        for (IFileImportService s : Lookup.getDefault().lookupAll(IFileImportService.class)) {
            EnumSet<FileFormat> formats = s.getSupportedFileFormats();
            if(formats.contains(format)){
                ret = s;
                break;
            }
        }
        return ret;
    }
    
    public static IFileImportService findByExtension(final String ext) {
        IFileImportService ret = null;
        for (IFileImportService s : Lookup.getDefault().lookupAll(IFileImportService.class)) {
            String[] extensions = s.getExtensions();
            Arrays.sort(extensions);
            int index = Arrays.binarySearch(extensions, ext);
            if (index > -1) {
                ret = s;
                break;
            }
        }
        return ret;
    }
}
