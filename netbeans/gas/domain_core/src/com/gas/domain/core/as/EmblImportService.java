/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.as;

import com.gas.common.ui.FileFormat;
import com.gas.domain.core.IFileImportService;
import com.gas.domain.core.as.util.AnnotatedSeqParser;
import com.gas.domain.core.flexembl.FlexEMBLFormat;
import com.gas.domain.core.flexrs.FlexGenbankFormat;
import java.io.File;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import org.apache.commons.lang3.ArrayUtils;
import org.biojavax.bio.seq.io.EMBLFormat;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IFileImportService.class)
public class EmblImportService implements IFileImportService {

    @Override
    public String[] getExtensions() {
        Set<String> ret = new HashSet<String>();
        EnumSet<FileFormat> formats = getSupportedFileFormats();
        for (FileFormat format : formats) {
            ret.addAll(Arrays.asList(format.getExts()));
        }
        return ret.toArray(new String[ret.size()]);
    }

    @Override
    public EnumSet<FileFormat> getSupportedFileFormats() {
        EnumSet<FileFormat> ret = EnumSet.of(FileFormat.EMBL);
        return ret;
    }

    @Override
    public Object receive(File file) {
        Object ret = null;
        final String name = file.getName();
        final String ext = name.substring(name.lastIndexOf(".") + 1);
        final String[] exts = getExtensions();
        if (ArrayUtils.contains(exts, ext.toLowerCase(Locale.ENGLISH))) {
            ret = AnnotatedSeqParser.parse(file, new FlexEMBLFormat());
        }
        return ret;
    }
}
