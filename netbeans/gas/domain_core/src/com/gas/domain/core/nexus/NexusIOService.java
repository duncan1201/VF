/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.nexus;

import com.gas.common.ui.FileFormat;
import com.gas.domain.core.IFileImportService;
import com.gas.domain.core.msa.MSA;
import com.gas.domain.core.nexus.api.Blk;
import com.gas.domain.core.nexus.api.INexusIOService;
import com.gas.domain.core.nexus.api.Nexus;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import org.openide.util.Exceptions;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

/**
 *
 * @author dq
 */
@ServiceProviders(value = {
    @ServiceProvider(service = INexusIOService.class),
    @ServiceProvider(service = IFileImportService.class)})
public class NexusIOService implements INexusIOService, IFileImportService {

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
        return parse(file);
    }

    @Override
    public EnumSet<FileFormat> getSupportedFileFormats() {
        EnumSet<FileFormat> ret = EnumSet.of(FileFormat.NEXUS);
        return ret;
    }

    private enum STATE {

        UNKNOWN, READING_BLOCK, READING_COMMENT, READING_FORMAT, READING_MATRIX, READING_DIMENSION
    };

    @Override
    public Nexus parse(Class clazz, String name) {
        return parse(clazz.getResourceAsStream(name));
    }   

    @Override
    public Nexus parse(File file) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        }
        if (fileInputStream != null) {
            return parse(fileInputStream);
        } else {
            return null;
        }
    }

    public Nexus parse(InputStream inputStream) {
        Nexus ret = new Nexus();
        List<String> blocks = NexusIOUtil.getBlockStrs(inputStream);
        Iterator<String> itr = blocks.iterator();
        while (itr.hasNext()) {
            String blockStr = itr.next();
            BlkParser blkParser = new BlkParser();
            Blk blk = blkParser.parse(blockStr);
            ret.getBlks().add(blk);
        }

        return ret;
    }
}
