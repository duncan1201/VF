/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.tigr.core.service;

import com.gas.common.ui.FileFormat;
import com.gas.domain.core.IFileImportService;
import com.gas.domain.core.tigr.Kromatogram;
import com.gas.domain.core.tigr.util.KromatogramParser;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IFileImportService.class)
public class AbxScfImportService implements IFileImportService {

    
    @Override
    public EnumSet<FileFormat> getSupportedFileFormats() {
        EnumSet<FileFormat> ret = EnumSet.of(FileFormat.ABX, FileFormat.SCF);
        return ret;
    }
    
    @Override
    public String[] getExtensions() {
        List<String> ret = new ArrayList<String>();
        EnumSet<FileFormat> formats = getSupportedFileFormats();
        Iterator<FileFormat> itr = formats.iterator();
        while(itr.hasNext()){           
            ret.addAll(Arrays.asList(itr.next().getExts()));
        }
        return ret.toArray(new String[ret.size()]);
    }

    @Override
    public Object receive(File file) {
        Kromatogram ret = KromatogramParser.parse(file);
        String fileName = ret.getFileName();
        int index = fileName.indexOf(".");
        if(index > -1){
            ret.setName(fileName.substring(0, index));
        }else{
            ret.setName(fileName);
        }
        String desc = String.format("Imported from %s", file.getAbsolutePath());
        ret.setDesc(desc);
        ret.setLastModifiedDate(new Date());
        
        return ret;
    }
    
}
