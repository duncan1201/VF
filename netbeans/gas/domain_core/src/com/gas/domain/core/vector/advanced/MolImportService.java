/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.vector.advanced;

import com.gas.common.ui.FileFormat;
import com.gas.common.ui.core.StringList;
import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.util.DateUtil;
import com.gas.common.ui.util.FileHelper;
import com.gas.common.ui.util.StrUtil;
import com.gas.domain.core.IFileImportService;
import com.gas.domain.core.as.*;
import com.gas.domain.core.as.AsHelper;
import com.gas.domain.core.vector.advanced.api.IFeatListService;
import com.gas.domain.core.vector.advanced.api.IMolImportService;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.EnumSet;
import java.util.Iterator;
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
    @ServiceProvider(service = IMolImportService.class),
    @ServiceProvider(service = IFileImportService.class)})
public class MolImportService implements IMolImportService {

    private IFeatListService featListService = Lookup.getDefault().lookup(IFeatListService.class);
    private static final String LOCUS_REGEX = "25\\|(.+)";
    private static final String FORM_REGEX = "27\\|(.+)"; // 0: circular; 1: linear
    private static final String MOL_TYPE_REGEX = "28\\|(.+)"; // 1: RNA; 0: DNA/Protein
    private static final String LENGTH_REGEX = "33\\|(.+)";
    private static final String DESC_REGEX = "34\\|(.+)";
    private static final String FEATURE_START_REGEX = "45";
    private static final String FEATURE_END_REGEX = "50";
    private static final String FEATURE_KEY_REGEX = "51\\|(.*)";
    private static final String FEATURE_LABEL_REGEX = "52\\|(.*)";
    private static final String FETURE_STRAND_REGEX = "53\\|(.*)";  // 0: forward; 1: reverse
    //private static final String FETURE_KEYNAME_REGEX = "54|(.+)";    
    private static final String FETURE_LOC_START_REGEX = "55\\|(.+)";
    private static final String FETURE_LOC_END_REGEX = "56\\|(.+)";
    private static final String SEQ_REGEX = "209\\|(.*)";
    private static final String MOL_END_REGEX = "210";
    private static final String MOL_START_REGEX = "212";
    /*
     * base date 11/30/94 00:00 create/modify date is the number of seconds
     * since the base date
     */
    private static final String CREATION_DATE_REGEX = "236\\|(.+)";
    private static final String MODIFIED_DATE_REGEX = "237\\|(.+)";
    private static final String FETURE_QUALIFIER_REGEX = "286\\|(.+)";
    private static final String DIVISION_REGEX = "1008\\|(.+)";

    /*
     * Import files with "ma4" or "pa4" file extensions. If with "pa4"
     * extensions, then protein; if with "ma4" extensions, then DNA/RNA.
     */
    @Override
    public List<AnnotatedSeq> receiveXA4(File file) {
        List<AnnotatedSeq> ret = new ArrayList<AnnotatedSeq>();
        try {
            FileInputStream inputStream = new FileInputStream(file);
            boolean isAminoAcid = file.getName().endsWith("pa4");

            ret = receiveXA4(inputStream, isAminoAcid);
        } catch (FileNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            return ret;
        }
    }

    /*
     * Import files with "ma4" or "pa4" file extensions. If with "pa4"
     * extensions, then protein; if with "ma4" extensions, then DNA/RNA.
     */
    private List<AnnotatedSeq> receiveXA4(InputStream inputStream, boolean aminoAcid) {
        List<AnnotatedSeq> ret = new ArrayList<AnnotatedSeq>();
        BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
        String line = null;
        try {
            StringBuilder builder = new StringBuilder();
            while ((line = r.readLine()) != null) {
                line = line.trim();
                if (line.matches(MOL_START_REGEX)) {
                    builder.append(line);
                    builder.append("\n");
                } else if (line.matches(MOL_END_REGEX)) {
                    AnnotatedSeq as = receive(builder.toString());
                    if (aminoAcid) {
                        as.getSequenceProperties().put(AsHelper.MOL_TYPE, AsHelper.AA);
                    }
                    ret.add(as);
                    builder = new StringBuilder();
                } else {
                    builder.append(line);
                    builder.append("\n");
                }
            }
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            return ret;
        }
    }

    private AnnotatedSeq receive(String str) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(str.getBytes());
        return receive(inputStream);
    }

    /**
     * imports everything EXCEPT the sequence
     */
    private AnnotatedSeq receive(InputStream inputStream) {
        AnnotatedSeq ret = new AnnotatedSeq();
        BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
        String line = null;
        try {
            Feture feture = null;
            while ((line = r.readLine()) != null) {
                line = line.trim();
                String match = null;
                if (line.matches(LOCUS_REGEX)) {
                    match = StrUtil.extract(LOCUS_REGEX, line);
                    if (match != null) {
                        ret.setName(match);
                    }
                } else if (line.matches(FORM_REGEX)) { // 0: circular; 1: linear
                    match = StrUtil.extract(FORM_REGEX, line);
                    if (match != null) {
                        ret.setCircular(match.equals("0"));
                    }
                } else if (line.matches(CREATION_DATE_REGEX)) {
                    match = StrUtil.extract(CREATION_DATE_REGEX, line);
                    final Date baseDate = DateUtil.create(1994, 10, 30);
                    Date date = DateUtil.create(baseDate.getTime() + Long.parseLong(match) * 1000);
                    ret.setCreationDate(date);
                } else if (line.matches(MODIFIED_DATE_REGEX)) {
                    ret.setLastModifiedDate(new Date());
                } else if (line.matches(MOL_TYPE_REGEX)) { // 1: RNA; 0: DNA/Protein
                    match = StrUtil.extract(MOL_TYPE_REGEX, line);
                    if (match != null) {
                        final String molType = match.equals("1") ? AsHelper.mRNA : AsHelper.DNA;
                        ret.getSequenceProperties().put(AsHelper.MOL_TYPE, molType);
                    }
                } else if (line.matches(LENGTH_REGEX)) {
                    match = StrUtil.extract(LENGTH_REGEX, line);
                    if (match != null) {
                        ret.setLength(Integer.parseInt(match));
                    }
                } else if (line.matches(DESC_REGEX)) {
                    match = StrUtil.extract(DESC_REGEX, line);
                    if (match != null) {
                        ret.setDesc(match);
                    }
                } else if (line.matches(FEATURE_START_REGEX)) {
                    if (feture != null && !feture.getKey().equals("-1")) {
                        ret.getFetureSet().add(feture);
                    }
                    feture = new Feture();
                } else if (line.matches(FEATURE_KEY_REGEX)) {
                    match = StrUtil.extract(FEATURE_KEY_REGEX, line);
                    if (match != null) {
                        final String keyName = featListService.getFeatureKeyName(Integer.parseInt(match));
                        if(keyName.equals("-1")){
                            System.out.println();                            
                        }
                        feture.setKey(keyName);
                    }
                } else if (line.matches(FEATURE_LABEL_REGEX)) {
                    match = StrUtil.extract(FEATURE_LABEL_REGEX, line);
                    if (match != null && match.toLowerCase().indexOf("invitrogen") < 0) {
                        if (match.matches("(?i)Source(.+)")) {
                            match = "Source";
                        }
                        feture.getQualifierSet().add(new Qualifier(String.format("label=%s", match)));
                    }
                } else if (line.matches(FETURE_STRAND_REGEX)) { // 0: forward
                    match = StrUtil.extract(FETURE_STRAND_REGEX, line);
                    if (match != null) {
                        Lucation luc = feture.getLucation();
                        if (luc == null) {
                            luc = new Lucation();
                        }
                        luc.setStrand(match.equals("0") ? true : false);
                        feture.setLucation(luc);
                    }
                } else if (line.matches(FETURE_LOC_START_REGEX)) {
                    match = StrUtil.extract(FETURE_LOC_START_REGEX, line);
                    if (match != null) {
                        Lucation luc = feture.getLucation();
                        luc.setContiguousMin(Integer.parseInt(match));
                    }
                } else if (line.matches(FETURE_QUALIFIER_REGEX)) {
                    match = StrUtil.extract(FETURE_QUALIFIER_REGEX, line);
                    if (match.toLowerCase().indexOf("invitrogen") < 0) {

                        match = match.replaceAll("/", "");
                        String[] splits = match.split("\\|");
                        for (String split : splits) {
                            if (!split.isEmpty() && split.indexOf("=") > -1) {
                                feture.getQualifierSet().add(new Qualifier(split));
                            }
                        }
                    }
                } else if (line.matches(FETURE_LOC_END_REGEX)) {
                    match = StrUtil.extract(FETURE_LOC_END_REGEX, line);
                    if (match != null) {
                        Lucation luc = feture.getLucation();
                        luc.setContiguousMax(Integer.parseInt(match));
                    }
                } else if (line.matches(FEATURE_END_REGEX)) {
                    if (feture != null && !feture.getKey().equals("-1")) {
                        ret.getFetureSet().add(feture);
                    }
                } else if (line.matches(SEQ_REGEX)) {
                    match = StrUtil.extract(SEQ_REGEX, line);
                    if (match != null) {
                        ret.appendData(match);
                    }
                } else if (line.matches(DIVISION_REGEX)) {
                    match = StrUtil.extract(DIVISION_REGEX, line);
                    if (match != null) {
                        ret.setDivision(match);
                    }
                }
            }
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } catch (Exception e) {
            Exceptions.printStackTrace(e);
        } finally {
            if (ret.getAccession() == null) {
                ret.setAccession("");
            }
            if (ret.getDesc() == null) {
                ret.setDesc("");
            }
            removeOutOfBoundFeatures(ret);
            convertOverhangFeatures(ret);
            convertAttSites(ret);
            return ret;
        }
    }

    private void removeOutOfBoundFeatures(AnnotatedSeq as) {
        int length = as.getLength();
        Iterator<Feture> itr = as.getFetureSet().getFetures().iterator();
        while (itr.hasNext()) {
            Feture feture = itr.next();
            Loc loc = feture.getLucation().toLoc();
            if (loc.getMax() > length) {
                itr.remove();
            }
        }
    }

    private void convertAttSites(AnnotatedSeq as) {
        Iterator<Feture> itr = as.getFetureSet().getByFeturesByKeys(FetureKeyCnst.MISC_RECOMBINATION).iterator();
        while (itr.hasNext()) {
            Feture feture = itr.next();

            QualifierSet list = feture.getQualifierSet();
            Qualifier q = list.getQualifier("label");
            if (q != null) {
                String value = q.getValue();
                if (value.startsWith("att")) {
                    feture.setKey(value);
                }
            }

        }
    }

    /**
     * convert overhang features to new ones and delete old ones
     */
    private void convertOverhangFeatures(AnnotatedSeq as) {
        final String[] keys = {FetureKeyCnst.OVERHANG, FetureKeyCnst.Protein_Binding_Site};
        Iterator<Feture> itr = as.getFetureSet().getByFeturesByKeys(keys).iterator();
        while (itr.hasNext()) {
            Feture feture = itr.next();
            Qualifier qualifier = feture.getQualifierSet().getQualifier("label");
            if (qualifier == null) {
                return;
            }

            if (qualifier.isDirectionalTOPOLabel() || qualifier.isTATOPOLabel()) {
                feture.getQualifierSet().remove(qualifier);
            }

            Lucation luc = feture.getLucation();
            int min = luc.getStart();
            Overhang overhang = new Overhang();
            if (qualifier.isDirectionalTOPOLabel()) {
                overhang.setLength(4);
                overhang.setFivePrime(true);
                overhang.setStrand(min == 1);
                as.addOverhang(overhang);
                as.getFetureSet().remove(feture);
            } else if (qualifier.isTATOPOLabel()) {
                overhang.setLength(1);
                overhang.setFivePrime(false);
                overhang.setStrand(min != 1);
                as.addOverhang(overhang);
                as.getFetureSet().remove(feture);
            }

        }
    }

    private AnnotatedSeq receiveSingle(final File file) {
        AnnotatedSeq ret = null;
        try {
            FileInputStream inputStream = new FileInputStream(file);
            ret = receive(inputStream);
            final String fileName = file.getName();
            if (fileName.endsWith(".mol") || fileName.endsWith(".pro")) {
                final String nameWithExt = file.getName();
                final String nameNoExt = nameWithExt.substring(0, nameWithExt.lastIndexOf("."));
                final File seqFile = new File(file.getParent(), String.format("%s%s%s%s", "Seq", File.separatorChar, nameNoExt, ".seq"));
                final String seq = FileHelper.toStringBuffer(seqFile).toString();
                ret.setSequence(seq);

                if (fileName.endsWith(".pro")) {
                    ret.getSequenceProperties().put(AsHelper.MOL_TYPE, AsHelper.AA);
                }
            }
        } catch (FileNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            return ret;
        }
    }

    @Override
    public Object receive(File file) {
        Object ret = null;
        final String name = file.getName();
        final String ext = name.substring(name.lastIndexOf(".") + 1);
        String[] exts = getExtensions();
        Arrays.sort(exts);
        int index = Arrays.binarySearch(exts, ext);
        if (index > -1) {
            ret = receiveXA4(file);
        }
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
    public List<AnnotatedSeq> receiveProteinsFromDB(final File dbDir) {
        List<AnnotatedSeq> ret = new ArrayList<AnnotatedSeq>();
        File dir = new File(dbDir, "ProData");
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.getName().endsWith(".pro")) {
                AnnotatedSeq as = receiveSingle(file);
                if(as != null){
                    ret.add(as);
                }
            }
        }
        return ret;
    }

    @Override
    public List<AnnotatedSeq> receiveNucleotidesFromDB(final File dbDir) {
        List<AnnotatedSeq> ret = new ArrayList<AnnotatedSeq>();
        File dir = new File(dbDir, "MolData");
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.getName().endsWith(".mol")) {
                AnnotatedSeq as = receiveSingle(file);
                if(as != null){
                    ret.add(as);
                }
            }
        }
        return ret;
    }

    @Override
    public EnumSet<FileFormat> getSupportedFileFormats() {
        EnumSet<FileFormat> ret = EnumSet.of(FileFormat.VNTI_NUCLEOTIDES, FileFormat.VNTI_PROTEINS);
        return ret;
    }
}
