/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.msa;

import com.gas.common.ui.FileFormat;
import com.gas.domain.core.IFileImportService;
import java.io.File;
import java.util.EnumSet;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IFileImportService.class)
public class AprImportService implements IFileImportService{

    @Override
    public String[] getExtensions() {        
        return FileFormat.APR.getExts();
    }

    @Override
    public Object receive(File file) {       
        Apr apr = AprParser.parse(file);
        return apr;
    }   

    @Override
    public EnumSet<FileFormat> getSupportedFileFormats() {
        EnumSet<FileFormat> ret = EnumSet.of(FileFormat.APR);
        return ret;
    }
}