/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.vector.advanced;

import com.gas.common.ui.FileFormat;
import com.gas.common.ui.util.FileHelper;
import com.gas.common.ui.util.StrUtil;
import com.gas.domain.core.IFileImportService;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.AsHelper;
import com.gas.domain.core.primer3.IPrimer3Service;
import com.gas.domain.core.vector.advanced.api.IMolImportService;
import com.gas.domain.core.vector.advanced.api.IOligoImportService;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

/**
 *
 * @author dq
 */
@ServiceProviders(value = {
    @ServiceProvider(service = IOligoImportService.class),
    @ServiceProvider(service = IFileImportService.class)})
public class OligoImportService implements IOligoImportService {

    private static final String LAST_LINE_OF_RECORD = "11";
    private static final String NAME_REGEX = "12\\|(.+)";
    private static final String SEQ_REGEX = "13\\|(.+)";
    private static final String DESC_REGEX = "14\\|(.+)";
    private static final String SEPARATOR = "214";
    private static final String MOL_TYPE_REGEX = "223\\|(.+)"; // 1 for "DNA", 2 for "RNA"
    private static final String COMMENT_REGEX = "248\\|(.+)";

    @Override
    public String[] getExtensions() {
        return FileFormat.VNTI_OLIGOS.getExts();
    }

    @Override
    public Object receive(File file) {
        List<AnnotatedSeq> ret = new ArrayList<AnnotatedSeq>();
        String ext = FileHelper.getExt(file);
        if (!ext.equalsIgnoreCase(FileFormat.VNTI_OLIGOS.getExts()[0])) {
            throw new IllegalArgumentException();
        }
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            List<String> strs = split(fileInputStream);
            for (String str : strs) {
                AnnotatedSeq as = _receive(str);
                ret.add(as);
            }
        } catch (FileNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        }

        return ret;
    }

    private void checkPrimer(AnnotatedSeq as) {
        IPrimer3Service primer3Service = Lookup.getDefault().lookup(IPrimer3Service.class);
        primer3Service.checkPrimer(as, Boolean.TRUE);
    }

    private AnnotatedSeq _receive(File file) {
        AnnotatedSeq ret = null;
        try {
            FileInputStream inputStream = new FileInputStream(file);
            ret = _receive(inputStream);            
        } catch (FileNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        }
        return ret;
    }

    private List<String> split(InputStream inputStream) {
        List<String> ret = new ArrayList<String>();
        BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
        try {
            StringBuilder builder = new StringBuilder();
            String line = r.readLine();
            while (line != null) {
                builder.append(line);
                builder.append("\n");
                line = line.trim();
                if (line.equals(LAST_LINE_OF_RECORD)) {
                    ret.add(builder.toString());
                    builder = new StringBuilder();
                }
                line = r.readLine();
            }
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        return ret;
    }

    private AnnotatedSeq _receive(String str) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(str.getBytes(Charset.forName("UTF8")));
        return _receive(inputStream);
    }

    /**
     * it accepts "olg" files only
     */
    private AnnotatedSeq _receive(InputStream inputStream) {
        AnnotatedSeq ret = null;
        BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String line = r.readLine();
            AnnotatedSeq seq = null;
            while (line != null) {
                line = line.trim();

                if (line.matches(SEPARATOR)) {
                    seq = createIfNecessary(seq);
                } else if (line.matches(NAME_REGEX)) {
                    seq = createIfNecessary(seq);
                    String match = StrUtil.extract(NAME_REGEX, line);
                    seq.setName(match);
                } else if (line.matches(SEQ_REGEX)) {
                    String match = StrUtil.extract(SEQ_REGEX, line);
                    seq.setSequence(match);
                } else if (line.matches(DESC_REGEX)) {
                    String match = StrUtil.extract(DESC_REGEX, line);
                    seq.setDesc(match);
                } else if (line.matches(MOL_TYPE_REGEX)) {
                    String match = StrUtil.extract(MOL_TYPE_REGEX, line);
                    if (match.equals("1")) {
                        seq.getSequenceProperties().put(AsHelper.MOL_TYPE, AsHelper.DNA);
                    } else if (match.equals("2")) {
                        seq.getSequenceProperties().put(AsHelper.MOL_TYPE, AsHelper.RNA);
                    }
                } else if (line.matches(COMMENT_REGEX)) {
                    String match = StrUtil.extract(MOL_TYPE_REGEX, line);
                    seq.setComment(match);
                } else if (line.matches(LAST_LINE_OF_RECORD)) {    
                    if(seq.getSiquence().containsUracil()){
                        seq.getSequenceProperties().put(AsHelper.MOL_TYPE, AsHelper.RNA);
                    }else{
                        seq.getSequenceProperties().put(AsHelper.MOL_TYPE, AsHelper.DNA);
                    }
                    ret = seq;
                    break;
                }

                line = r.readLine();
            }
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }        
        checkPrimer(ret);
        return ret;
    }

    private AnnotatedSeq createIfNecessary(AnnotatedSeq as) {
        if (as == null) {
            as = new AnnotatedSeq();
            as.setAccession("");
            as.setOligo(true);
            as.setCircular(false);
            as.getSequenceProperties().put(AsHelper.MOL_TYPE, AsHelper.DNA);            
        }
        return as;
    }

    @Override
    public List<AnnotatedSeq> receiveOligosFromDB(File dbDir) {
        List<AnnotatedSeq> ret = new ArrayList<AnnotatedSeq>();
        File dir = new File(dbDir, "OligData");
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                continue;
            }
            AnnotatedSeq as = _receive(file);
            if (as != null) {
                ret.add(as);
            }
        }
        File dirComments = new File(dir, "");
        File[] filesComment = dirComments.listFiles();
        for (File fileComment : filesComment) {
        }
        return ret;
    }

    @Override
    public EnumSet<FileFormat> getSupportedFileFormats() {
        EnumSet<FileFormat> ret = EnumSet.of(FileFormat.VNTI_OLIGOS);
        return ret;
    }
}
