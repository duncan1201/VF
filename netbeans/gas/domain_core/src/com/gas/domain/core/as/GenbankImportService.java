/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.as;

import com.gas.common.ui.FileFormat;
import com.gas.domain.core.IFileImportService;
import com.gas.domain.core.as.api.INucleotideImportService;
import com.gas.domain.core.as.util.AnnotatedSeqParser;
import com.gas.domain.core.flexrs.FlexGenbankFormat;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.apache.commons.lang3.ArrayUtils;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

/**
 *
 * @author dq
 */
@ServiceProviders(value = {
    @ServiceProvider(service = GenbankImportService.class),
    @ServiceProvider(service = IFileImportService.class)})
public class GenbankImportService implements INucleotideImportService {
    
    @Override
    public String[] getExtensions() {
        Set<String> ret = new HashSet<String>();
        EnumSet<FileFormat> formats = getSupportedFileFormats();
        for(FileFormat format: formats){            
            ret.addAll(Arrays.asList(format.getExts()));
        }
        return ret.toArray(new String[ret.size()]);                
    }

    @Override
    public Object receive(File file) {
        Object ret = null;
        final String name = file.getName();
        final String ext = name.substring(name.lastIndexOf(".") + 1);
        final String[] exts = getExtensions();        
        if (ArrayUtils.contains(exts, ext.toLowerCase(Locale.ENGLISH))) { 
            ret = AnnotatedSeqParser.parse(file, new FlexGenbankFormat());
        }
        return ret;
    }
   
    @Override
    public EnumSet<FileFormat> getSupportedFileFormats() {
        EnumSet<FileFormat> ret = EnumSet.of(FileFormat.GenBank, FileFormat.GenPept);
        return ret;
    }
}
