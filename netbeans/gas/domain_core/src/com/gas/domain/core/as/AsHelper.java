/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.as;

import com.gas.common.ui.core.FloatList;
import com.gas.common.ui.core.LocList;
import com.gas.common.ui.core.StringComparator;
import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.util.BioUtil;
import com.gas.common.ui.util.LocUtil;
import com.gas.common.ui.util.StrUtil;
import com.gas.domain.core.ren.RMap;
import com.gas.domain.core.fasta.Fasta;
import com.gas.domain.core.gc.api.IGCService;
import com.gas.domain.core.geneticCode.api.IGeneticCodeTableService;
import com.gas.domain.core.orf.api.IORFService;
import com.gas.domain.core.orf.api.ORFParam;
import com.gas.domain.core.orf.api.ORFResult;
import com.gas.domain.core.primer3.GbOutput;
import com.gas.domain.core.primer3.P3Output;
import com.gas.domain.core.ren.IRENListService;
import com.gas.domain.core.ren.IRMapService;
import com.gas.domain.core.ren.REN;
import com.gas.domain.core.ren.RENList;
import com.gas.domain.core.ren.RENSet;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.biojava.bio.seq.DNATools;
import org.biojava.bio.seq.ProteinTools;
import org.biojava.bio.seq.RNATools;
import org.biojava.bio.symbol.IllegalSymbolException;
import org.biojava.bio.symbol.SymbolList;
import org.openide.util.Lookup;

/**
 *
 * @author dunqiang
 */
public class AsHelper {

    // RNAs
    public static final String GENOMIC_RNA = "genomic RNA";
    public static final String mRNA = "mRNA";
    public static final String OTHER_RNA = "other RNA";
    public static final String RNA = "RNA";
    public static final String rRNA = "rRNA";
    public static final String tRNA = "tRNA";
    public static final String TRANSCRIBED_RNA = "transcribed RNA";
    public static final String UNASSIGNED_RNA = "unassigned RNA";
    public static final String VIRAL_cRNA = "viral cRNA";
    public static String[] ALL_RNAs = {
        GENOMIC_RNA.toLowerCase(Locale.ENGLISH),
        mRNA.toLowerCase(Locale.ENGLISH), 
        OTHER_RNA.toLowerCase(Locale.ENGLISH),
        RNA.toLowerCase(Locale.ENGLISH),
        rRNA.toLowerCase(Locale.ENGLISH),
        tRNA.toLowerCase(Locale.ENGLISH),
        TRANSCRIBED_RNA.toLowerCase(Locale.ENGLISH),
        UNASSIGNED_RNA.toLowerCase(Locale.ENGLISH),
        VIRAL_cRNA.toLowerCase(Locale.ENGLISH)
        };
    
    // DNAs 
    public static final String DNA = "DNA";    
    public static final String GENOMIC_DNA = "genomic DNA";
    public static final String OTHER_DNA = "other DNA";
    public static final String UNASSIGNED_DNA = "unassigned DNA";
    
    public static String[] ALL_DNAs= {
        DNA.toLowerCase(Locale.ENGLISH), 
        GENOMIC_DNA.toLowerCase(Locale.ENGLISH),
        OTHER_DNA.toLowerCase(Locale.ENGLISH),
        UNASSIGNED_DNA.toLowerCase(Locale.ENGLISH)
    };
    
    // Amino acids
    public static final String AA = "aa";
    public static final String[] ALL_AAs = {AA.toLowerCase(Locale.ENGLISH)};
    
    static{
        Arrays.sort(ALL_RNAs);
        Arrays.sort(ALL_DNAs);
        Arrays.sort(ALL_AAs);                
    }
    
    public static final String MOL_TYPE = "moltype";
    //http://www.ebi.ac.uk/ena/WebFeat/qualifiers/mol_type.html
    
    
    
    static {
    }
    
    /*
     * Convenience method
     */
    public static boolean isRNA(AnnotatedSeq as) {
        boolean ret = false;
        Map<String, String> properties = as.getSequenceProperties();
        Iterator<String> keyItr = properties.keySet().iterator();
        while (keyItr.hasNext()) {
            String key = keyItr.next();
            if (key.equalsIgnoreCase(MOL_TYPE)) {
                String v = properties.get(key);
                if(Arrays.binarySearch(ALL_RNAs, v.toLowerCase(Locale.ENGLISH)) > -1){
                    ret = true;
                }
            }
        }
        return ret;
    }
    
    public static AnnotatedSeq get5Terminus(AnnotatedSeq as) {
        AnnotatedSeq ret = null;
        Overhang overhangStart = as.getStartOverhang();
        if (overhangStart != null) {
            ret = as.subAs(1, overhangStart.getLength() + 3);
        } else {
            ret = as.subAs(1, 4);
        }
        return ret;
    }

    public static AnnotatedSeq get3Terminus(AnnotatedSeq as) {
        AnnotatedSeq ret = null;
        Overhang overhangEnd = as.getEndOverhang();
        if (overhangEnd != null) {
            ret = as.subAs(as.getLength() - 2 - overhangEnd.getLength(), as.getLength());
        } else {
            ret = as.subAs(as.getLength() - 3, as.getLength());
        }
        return ret;
    }    

    public static AnnotatedSeq createNewByInsertAs(final AnnotatedSeq as, AnnotatedSeq insert, int pos, boolean updateAnalysisResult) {
        return createNewByInsertAs(as, insert, pos, updateAnalysisResult, new AnnotatedSeq.ELEMENT[]{});
    }

    public static Fasta toFasta(Collection<AnnotatedSeq> seqs) {
        Fasta ret = new Fasta();
        for (AnnotatedSeq as : seqs) {
            ret.add(as.getName(), as.getSiquence().getData());
        }
        return ret;
    }

    public static void removeSeq(final AnnotatedSeq as, final int start, final int end, boolean updateAnalysisResult) {
        int oldLength = as.getLength();

        as.getSiquence().remove(start, end);

        as.getFetureSet().removeSeq(start, end, oldLength);

        as.setLength(as.getSiquence().getData().length());

        if (as.getP3output() != null) {
            as.getP3output().clear();
        }

        as.getParentLocSet().removeSeq(start, end);
        if (updateAnalysisResult) {
            updateAsResults(as);
        }
    }

    public static AnnotatedSeq insertAs(AnnotatedSeq as, int pos, String bases) {
        AnnotatedSeq insert = new AnnotatedSeq();
        insert.setSequence(bases);
        return insertAs(as, pos, insert);
    }

    public static AnnotatedSeq insertAs(AnnotatedSeq as, int insertPos, final AnnotatedSeq insert) {
        return insertAs(as, insertPos, insert, true);
    }

    public static AnnotatedSeq insertAs(AnnotatedSeq as, int insertPos, final AnnotatedSeq insert, boolean updateAnalysisResult) {
        int originalLength = as.getLength();
        as.getSiquence().insert(insertPos, insert.getSiquence().getData());
        as.setLength(as.getSiquence().getData().length());
        as.getFetureSet().insertSeq(insertPos, insert.getLength(), as.isCircular(), originalLength);

        FetureSet fetureSet = insert.getFetureSet().clone();
        fetureSet.translate(insertPos - 1, as.isCircular(), as.getLength() + insert.getLength());
        as.getFetureSet().addAll(fetureSet.getFetures());

        if (as.getP3output() != null) {
            as.getP3output().clear();
        }

        as.getParentLocSet().insertSeq(insertPos, insert.getLength());
        if (updateAnalysisResult) {
            updateAsResults(as);
        }
        return as;
    }

    public static AnnotatedSeq createNewByInsertAs(final AnnotatedSeq as, AnnotatedSeq insert, int insertPos, boolean updateAnalysisResult, AnnotatedSeq.ELEMENT... elements) {
        AnnotatedSeq ret;
        if (elements != null && elements.length > 0) {
            ret = as.clone(elements);
        } else {
            ret = as.clone();
        }
        return insertAs(ret, insertPos, insert, updateAnalysisResult);
    }

    public static AnnotatedSeq createByRemoving(final AnnotatedSeq as, int start, int end, boolean updateAnalysisResult) {
        AnnotatedSeq ret = as.clone();
        removeSeq(ret, start, end, updateAnalysisResult);
        ret.setCircular(false);
        return ret;
    }

    public static LocList findSeq(final AnnotatedSeq as, String target) {
        LocList ret = new LocList();

        final String seq = as.getSiquence().getData().toUpperCase(Locale.ENGLISH);
        final String rCompSeq = BioUtil.reverseComplement(seq).toUpperCase(Locale.ENGLISH);

        if (seq.length() < target.length()) {
            return ret;
        }

        final int totalLength = seq.length();
        target = target.toUpperCase(Locale.ENGLISH);
        Pattern pattern = Pattern.compile(target);
        Matcher matcher;
        Matcher rcompMatcher;
        if (as.isCircular()) {
            String psudoSeq = seq + seq.substring(target.length() - 1);
            String psudoRcompSeq = rCompSeq + rCompSeq.substring(target.length() - 1);
            matcher = pattern.matcher(psudoSeq);
            rcompMatcher = pattern.matcher(psudoRcompSeq);
        } else {
            matcher = pattern.matcher(seq);
            rcompMatcher = pattern.matcher(rCompSeq);
        }
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            if (start < totalLength) {
                if (end > totalLength) {
                    end -= totalLength;
                }
                ret.add(new Loc(start + 1, end, totalLength, true));
            }
        }
        while (rcompMatcher.find()) {
            int start = rcompMatcher.start() + 1;
            int end = rcompMatcher.end();
            if (start <= totalLength) {
                if (end > totalLength) {
                    end -= totalLength;
                }
                Loc loc = new Loc(start, end, totalLength).reverseComplement().StartEndSwitch();
                loc.setStrand(false);
                ret.add(loc);
            }
        }

        return ret;
    }

    /**
     * Only AnnotatedSeq.ELEMENT.FEATURE, AnnotatedSeq.ELEMENT.SEQ,
     * AnnotatedSeq.ELEMENT.OVERHANG are kept.
     *
     * @param list at least 2 the input sequences
     */
    public static AnnotatedSeq concatenate(AnnotatedSeq... list) {
        if (list == null || list.length < 2) {
            throw new IllegalArgumentException();
        }
        AnnotatedSeq ret = null;
        for (int i = 0; i < list.length; i++) {
            AnnotatedSeq toBeAdded = list[i];
            if (ret == null) {
                ret = toBeAdded.clone(AnnotatedSeq.ELEMENT.FEATURE, AnnotatedSeq.ELEMENT.SEQ, AnnotatedSeq.ELEMENT.OVERHANG);
            } else {
                boolean ligatable = ret.ligatable(toBeAdded);
                if (!ligatable) {
                    throw new IllegalArgumentException(String.format("cannot ligate %s with %s", ret.getName(), toBeAdded.getName()));
                }
                final Overhang endOverhang = ret.removeEndOverhang();
                int translateAmount;
                int newLength;
                if (endOverhang == null) {
                    translateAmount = ret.getSiquence().getData().length();
                    newLength = ret.getLength() + toBeAdded.getLength();
                } else {
                    translateAmount = ret.getSiquence().getData().length() - endOverhang.getLength();
                    newLength = ret.getLength() + toBeAdded.getLength() - endOverhang.getLength();
                }

                FetureSet fetureSet2beAdded = toBeAdded.getFetureSet().clone();
                fetureSet2beAdded.translate(translateAmount, false, newLength);
                ret.getFetureSet().addAll(fetureSet2beAdded.getFetures());

                String asData = toBeAdded.getSiquence().getData();
                String data = ret.getSiquence().getData();
                String newData = null;
                if (endOverhang != null) {
                    newData = data + asData.substring(endOverhang.getLength(), asData.length());
                } else {
                    newData = data + asData;
                }
                ret.setSequence(newData);


                final Overhang endOV = toBeAdded.getEndOverhang();
                if (endOV != null) {
                    ret.setEndOverhang(endOV);
                }
            }
        }
        ret.setLastModifiedDate(new Date());
        ret.setCreationDate(new Date());
        return ret;
    }

    public static void remove5Overhang(AnnotatedSeq as) {
        Iterator<Overhang> itr = as.getOverhangItr();
        while (itr.hasNext()) {
            Overhang overhang = itr.next();
            if (overhang.isFivePrime()) {
                itr.remove();
            }
        }
    }

    public static void remove3Overhang(AnnotatedSeq as) {
        Iterator<Overhang> itr = as.getOverhangItr();
        while (itr.hasNext()) {
            Overhang overhang = itr.next();
            if (overhang.isThreePrime()) {
                itr.remove();
            }
        }
    }

    public static Overhang removeStartOverhang(AnnotatedSeq as) {
        Overhang ret = null;
        Iterator<Overhang> itr = as.getOverhangItr();
        while (itr.hasNext()) {
            Overhang overhang = itr.next();
            if (overhang.isStartOverhang()) {
                ret = overhang;
                itr.remove();
                break;
            }
        }
        return ret;
    }

    public static void replace(AnnotatedSeq as, int startPos, int endPos, String replacement) {
        final int originalLength = as.getLength();
        final int deleteLength = LocUtil.width(startPos, endPos, originalLength).intValue();
        as.getSiquence().replace(startPos, endPos, replacement);

        int lengthDelta = replacement.length() - deleteLength;
        as.setLength(originalLength + lengthDelta);
        if (lengthDelta > 0) {
            as.getFetureSet().insertSeq(startPos, lengthDelta, true, originalLength);
        } else if (lengthDelta < 0) {
            as.getFetureSet().removeSeq(startPos, startPos + Math.abs(lengthDelta) - 1, originalLength);
        }
        as.getParentLocSet().replace(startPos, endPos, replacement.length());
        as.getP3output().clear();
        updateAsResults(as);
    }

    public static void clearAsResults(AnnotatedSeq as) {
        if (as.getOrfResult() != null && as.getOrfResult().getOrfSet() != null) {
            as.getOrfResult().getOrfSet().clear();
        }
        if (as.getRmap() != null) {
            as.getRmap().clearResults();
        }
        if (as.getGcResult() != null) {
            as.getGcResult().clearResult();
        }
    }

    private static void updateAsResults(AnnotatedSeq as) {
        if (as.getOrfResult() != null && as.getOrfResult().getOrfSet() != null && !as.getOrfResult().getOrfSet().isEmpty()) {
            as.getOrfResult().getOrfSet().clear();
            ORFParam orfParams = as.getOrfResult().getOrfParams();
            orfParams.setSequence(as.getSiquence().getData());
            IORFService serviceORF = Lookup.getDefault().lookup(IORFService.class);
            ORFResult orfResult = serviceORF.findORFResult(orfParams);
            as.setOrfResult(orfResult);
        }
        if (as.getRmap() != null) {
            RMap.InputParams inputParams = as.getRmap().getInputParams();
            IRENListService serviceRen = Lookup.getDefault().lookup(IRENListService.class);
            IRMapService serviceRmap = Lookup.getDefault().lookup(IRMapService.class);
            RENList renList = serviceRen.getRENList(inputParams.getRenListName());
            RENSet renSet = renList.getRens(inputParams.getRenNames());
            LocList locList = new LocList();
            locList.add(new Loc(inputParams.getStartPos(), inputParams.getEndPos()));

            RMap rmapNew = serviceRmap.findRM(as.getSiquence().getData(), as.isCircular(), inputParams.getRenListName(), renSet, inputParams.getMinOccurence(), inputParams.getMaxOccurence(), locList, inputParams.getAllow());
            as.getRmap().clearResults();
            as.getRmap().addEntries(rmapNew.getReadOnlyEntries());
        }
        if (as.getGcResult() != null) {
            IGCService gcService = Lookup.getDefault().lookup(IGCService.class);
            FloatList floatList = gcService.calculateGC(as.getSiquence().getData(), as.getGcResult().getWindowSize(), as.isCircular());
            as.getGcResult().setContents(floatList.toPrimitiveArray());
        }
        if (!as.getTranslationResults().isEmpty()) {
            TranslationResult tr = as.getTranslationResults().iterator().next();
            String tableName = tr.getTableName();
            int[] frames = TranslationResult.getFrames(as.getTranslationResults()).toPrimitives();
            IGeneticCodeTableService translateService = Lookup.getDefault().lookup(IGeneticCodeTableService.class);
            List<TranslationResult> trs = translateService.translate(as.getSiquence().getData(), tableName, frames);
            as.setTranslationResults(new HashSet(trs));
        }
    }

    public static void removeEndOverhang(AnnotatedSeq as) {
        Iterator<Overhang> itr = as.getOverhangItr();
        while (itr.hasNext()) {
            Overhang overhang = itr.next();
            if (overhang.isEndOverhang()) {
                itr.remove();
                break;
            }
        }
    }

    public static List<AnnotatedSeq> clone(List<AnnotatedSeq> list, boolean includeHId) {
        List<AnnotatedSeq> ret = new ArrayList<AnnotatedSeq>();
        for (AnnotatedSeq as : list) {
            AnnotatedSeq clone = as.clone();
            if (includeHId) {
                clone.setHibernateId(as.getHibernateId());
            }
            ret.add(clone);
        }
        return ret;
    }

    public static List<AnnotatedSeq> clone(List<AnnotatedSeq> list) {
        return clone(list, false);
    }

    public static AnnotatedSeq flip(AnnotatedSeq as) {
        AnnotatedSeq ret = as.clone(AnnotatedSeq.NO_ANALYSIS);
        String seq = ret.getSiquence().getData();
        seq = BioUtil.reverseComplement(seq);
        ret.setSequence(seq);

        ret.getFetureSet().flip(ret.getLength());
        // overhang
        Iterator<Overhang> oItr = ret.getReadOnlyOverhangs().iterator();
        while (oItr.hasNext()) {
            Overhang o = oItr.next();
            o.flip();
        }
        return ret;
    }

    public static boolean ligatable(AnnotatedSeq as, AnnotatedSeq next) {
        boolean ret = false;
        final Overhang endOverhang = as.getEndOverhang();
        final Overhang startOverhang = next.getStartOverhang();
        if (as.isCircular() || next.isCircular()) {
            ret = false;
        } else if (startOverhang == null && endOverhang == null) {
            ret = true;
        } else if (startOverhang != null && endOverhang != null) {
            final String end1 = as.getOverhangSeq(endOverhang);
            final String end2 = next.getOverhangSeq(startOverhang);
            String end2ReverseComplement = BioUtil.reverseComplement(end2);
            boolean sameStrand = endOverhang.isFivePrime() == startOverhang.isFivePrime();
            ret = sameStrand && end1.equalsIgnoreCase(end2ReverseComplement);
        } else {
            ret = false;
        }
        return ret;
    }

    public static String getOverhangSeq(AnnotatedSeq as, Overhang overhang) {
        String ret = "";
        final String data = as.getSiquence().getData();
        if (overhang != null && overhang.isStartOverhang()) {
            String seq = data.substring(0, overhang.getLength());
            if (overhang.isStrand()) {
                ret = seq;
            } else {
                ret = BioUtil.reverseComplement(seq);
            }
        } else if (overhang != null) {
            String seq = data.substring(data.length() - overhang.getLength(), data.length());
            if (overhang.isStrand()) {
                ret = seq;
            } else {
                ret = BioUtil.reverseComplement(seq);
            }
        }
        return ret;
    }

    /**
     *
     */
    public static Overhang getStartOverhang(AnnotatedSeq as) {
        Overhang ret = null;
        Iterator<Overhang> itr = as.getReadOnlyOverhangs().iterator();
        while (itr.hasNext()) {
            Overhang overhang = itr.next();
            if (overhang.isFivePrime() && overhang.isStrand()) {
                ret = overhang;
                break;
            } else if (!overhang.isFivePrime() && !overhang.isStrand()) {
                ret = overhang;
                break;
            }
        }
        return ret;
    }

    public static Map<String, List<Feture>> getFetureMap(AnnotatedSeq as, boolean includeOverhangs) {
        Map<String, List<Feture>> ret = new TreeMap<String, List<Feture>>(new Feture.FtrNameComparator());
        Collection<Feture> fetures = getAllFetures(as, includeOverhangs);
        Iterator<Feture> itr = fetures.iterator();
        while (itr.hasNext()) {
            Feture feture = itr.next();
            String key = feture.getKey();
            if (!ret.containsKey(key)) {
                ret.put(key, new ArrayList<Feture>());
            }
            ret.get(key).add(feture);
        }
        return ret;
    }

    public static List<Feture> getAllFetures(AnnotatedSeq as, boolean includesOverhang) {
        List<Feture> ret = new ArrayList<Feture>();
        if (as != null) {
            ret.addAll(as.getFetureSet().getFetures());
            if (includesOverhang) {
                if (!as.getReadOnlyOverhangs().isEmpty()) {
                    List<Feture> fetures = getOverhangAsFetures(as);
                    ret.addAll(fetures);
                }
                if (as.getP3output() != null) {
                    List<Feture> fetures = as.getP3output().toFetures(as.getLength(), as.isCircular());
                    ret.addAll(fetures);
                }
                if (as.getOrfResult() != null) {
                    List<Feture> fetures = as.getOrfResult().toFetures();
                    ret.addAll(fetures);
                }
                if (as.getGbOutput() != null){
                    List<Feture> fetures = as.getGbOutput().toFetures(as.getLength());
                    ret.addAll(fetures);
                }
            }
        }
        return ret;
    }

    public static List<Feture> getOverhangAsFetures(AnnotatedSeq as) {
        List<Feture> ret = new ArrayList<Feture>();
        Overhang startOverhang = getStartOverhang(as);
        if (startOverhang != null) {
            Feture f = new Feture();
            f.setKey(FetureKeyCnst.OVERHANG);
            Lucation luc = new Lucation(1, startOverhang.getLength(), startOverhang.isStrand());
            f.setLucation(luc);
            if (startOverhang.getName() != null) {
                f.addQualifier(String.format("label=%s overhang", startOverhang.getName()));
            } else {
                f.addQualifier("label=Overhang");
            }
            ret.add(f);
        }
        Overhang endOverhang = getEndOverhang(as);
        if (endOverhang != null) {
            Feture f = new Feture();
            f.setKey(FetureKeyCnst.OVERHANG);
            Lucation luc = new Lucation(as.getLength() - endOverhang.getLength() + 1, as.getLength(), endOverhang.isStrand());
            f.setLucation(luc);
            if (endOverhang.getName() != null) {
                f.addQualifier(String.format("label=%s overhang", endOverhang.getName()));
            } else {
                f.addQualifier("label=Overhang");
            }
            ret.add(f);
        }
        return ret;
    }

    public static Overhang getEndOverhang(AnnotatedSeq as) {
        Overhang ret = null;
        Iterator<Overhang> itr = as.getReadOnlyOverhangs().iterator();
        while (itr.hasNext()) {
            Overhang overhang = itr.next();
            if (overhang.isFivePrime() && !overhang.isStrand()) {
                ret = overhang;
                break;
            } else if (!overhang.isFivePrime() && overhang.isStrand()) {
                ret = overhang;
                break;
            }
        }
        return ret;
    }

    public static void touchAll(AnnotatedSeq as) {
        as.getAsPref().touchAll();
        as.getComments().iterator();
        as.getDbrefs().iterator();
        as.getFetureSet().touchAll();
        if (as.getGcResult() != null) {
            as.getGcResult().touchAll();
        }
        Iterator itr = as.getGbOutputs().iterator();
        while(itr.hasNext()){
            GbOutput output = (GbOutput)itr.next();
            output.touchAll();
        }
        as.get_keywords().iterator();
        if (as.getOrfResult() != null) {
            as.getOrfResult().touchAll();
        }
        itr = as.getP3outputs().iterator();
        while (itr.hasNext()) {
            P3Output p3output = (P3Output) itr.next();
            p3output.touchAll();
        }
        as.getParentLocSet().touchAll();
        as.getReadOnlyOverhangs().size();
        as.getReferences().size();
        if (as.getRmap() != null) {
            as.getRmap().getSize();
            Iterator<RMap.Entry> rItr = as.getRmap().getEntriesIterator();
            while (rItr.hasNext()) {
                RMap.Entry entry = rItr.next();
                entry.getDownstreamCutPos();
                entry.getUpstreamCutPos();
            }
            if (as.getRmap().getInputParams() != null) {
                as.getRmap().getInputParams().getRenNames().size();
            }
        }
        as.get_sequenceProperties().keySet();
        as.getSiquence().getData();
        Iterator<StructuredComment> scItr = as.getStructuredComments().iterator();
        while(scItr.hasNext()){
            scItr.next().touchAll();
        }
    }

    public static void removeFeaturesByType(AnnotatedSeq as, String type) {
        List<Feture> toBeRemoved = new ArrayList<Feture>();

        Set<Feture> fetures = as.getFetureSet().getFetures();
        Iterator<Feture> itr = fetures.iterator();
        while (itr.hasNext()) {
            Feture feture = itr.next();
            if (feture.getKey().equalsIgnoreCase(type)) {
                toBeRemoved.add(feture);
            }
        }

        fetures.removeAll(toBeRemoved);
        as.getFetureSet().setFetures(fetures);
    }

    /*
     * Convenience method
     */
    public static boolean isDNA(AnnotatedSeq as) {
        boolean ret = false;
        Map<String, String> properties = as.getSequenceProperties();
        Iterator<String> keyItr = properties.keySet().iterator();
        while (keyItr.hasNext()) {
            String key = keyItr.next();
            if (key.equalsIgnoreCase(MOL_TYPE)) {
                String v = properties.get(key);
                if(Arrays.binarySearch(ALL_DNAs, v.toLowerCase(Locale.ENGLISH)) > -1){
                    ret = true;
                }
            }
        }
        return ret;
    }

    public static boolean isFragment(AnnotatedSeq as) {
        return isNucleotide(as) && !as.getReadOnlyOverhangs().isEmpty();
    }

    public static boolean isNucleotide(AnnotatedSeq as) {
        return isDNA(as) || isRNA(as);
    }

    /*
     * Convenience method
     */
    public static boolean isAminoAcid(AnnotatedSeq as) {
        boolean ret = false;
        Map<String, String> properties = as.getSequenceProperties();
        Iterator<String> keyItr = properties.keySet().iterator();
        while (keyItr.hasNext()) {
            String key = keyItr.next();
            if (key.equalsIgnoreCase(MOL_TYPE)) {
                String v = properties.get(key);
                if (v.equalsIgnoreCase(AA)) {
                    ret = true;
                }
            }
        }
        return ret;
    }

    public static Set<TranslationResult> getTranslationResults(AnnotatedSeq as) {
        return getTranslationResults(as, 1, 2, 3, -1, -2, -3);
    }

    public static Set<TranslationResult> getTranslationResults(AnnotatedSeq as, int... frames) {
        Arrays.sort(frames);

        Set<TranslationResult> ret = new HashSet<TranslationResult>();
        Iterator<Comment> commentItr = as.getComments().iterator();
        while (commentItr.hasNext()) {
            Comment comment = commentItr.next();
            String data = comment.getData();
            if (data.startsWith(TranslationResult.HEADER_PREFIX)) {
                String[] splits = data.split("=");
                String k = splits[0];
                String v = splits[1];
                TranslationResult tr = TranslationResult.parse(data);
                if (Arrays.binarySearch(frames, tr.getFrame()) > -1) {
                    ret.add(tr);
                }
            }
        }
        return ret;
    }

    public static void addTranslationResult(AnnotatedSeq as, TranslationResult tr) {
        Comment c = new Comment();
        c.setRank(Comment.RANK);
        c.setData(tr.toString());
        as.getComments().add(c);
    }

    public static Map<String, List<Feture>> getSortedFetureMap(AnnotatedSeq as) {
        Map<String, List<Feture>> ret = new TreeMap<String, List<Feture>>(new StringComparator(true, false));

        Set<Feture> fetureSet = as.getFetureSet().getFetures();
        List<Feture> fetureList = new ArrayList<Feture>();
        fetureList.addAll(fetureSet);
        Collections.sort(fetureList, new Feture.FComparator());

        Iterator<Feture> fetureItr = fetureList.iterator();
        while (fetureItr.hasNext()) {
            Feture feture = fetureItr.next();
            List<Feture> list = ret.get(feture.getKey());
            if (list == null) {
                list = new ArrayList<Feture>();
            }
            list.add(feture);
            ret.put(feture.getKey(), list);
        }

        return ret;
    }

    public static RMap getRestrictionMap(AnnotatedSeq as) {
        RMap ret = new RMap();
        Iterator<Comment> commentItr = as.getComments().iterator();
        while (commentItr.hasNext()) {
            Comment comment = commentItr.next();
            String data = comment.getData();
            if (data.startsWith(RMap.HEADER_PREFIX)) {
                ret = RMap.parse(data);
                break;
            }
        }
        return ret;
    }

    public static AnnotatedSeq circularize(AnnotatedSeq as) {
        boolean circularizable = circularizable(as);
        if (!circularizable) {
        }
        AnnotatedSeq ret = as.clone();
        ret.setCircular(Boolean.TRUE);
        final Overhang startOverhang = ret.removeStartOverhang();
        final Overhang endOverhang = ret.removeEndOverhang();

        if (startOverhang != null && endOverhang != null) {
            String data = as.getSiquence().getData();
            String newData = data.substring(0, data.length() - startOverhang.getLength());
            ret.setSequence(newData);
            ret.getFetureSet().circularize(ret.getLength());
        } else if (startOverhang == null || endOverhang == null) {
            // nothing to do here
        }
        return ret;
    }

    private static boolean circularizable(AnnotatedSeq as) {
        boolean ret = false;
        final Overhang startOverhang = as.getStartOverhang();
        final Overhang endOverhang = as.getEndOverhang();
        if (startOverhang == null && endOverhang == null) {
            ret = true;
        } else if (startOverhang != null && endOverhang != null) {
            final String startSeq = AsHelper.getOverhangSeq(as, startOverhang);
            final String endSeq = AsHelper.getOverhangSeq(as, endOverhang);
            final String compEndSeq = BioUtil.complement(endSeq);
            ret = startSeq.equalsIgnoreCase(compEndSeq);
        }
        return ret;
    }

    public static boolean areEndsCompatible(AnnotatedSeq as) {
        boolean ret;
        final String data = as.getSiquence().getData();

        Overhang startOverhang = as.getStartOverhang();
        Overhang endOverhang = as.getEndOverhang();
        if (startOverhang == null && endOverhang == null) {
            ret = true;
        } else if (startOverhang != null && endOverhang != null) {
            final String startOverhangSeq = data.substring(0, startOverhang.getLength());
            final String endOverhangSeq = BioUtil.reverseComplement(data.substring(data.length() - endOverhang.getLength(), data.length()));
            ret = startOverhangSeq.equalsIgnoreCase(endOverhangSeq);
        } else {
            ret = false;
        }
        return ret;
    }

    public static AnnotatedSeq linearize(AnnotatedSeq as, RMap.Entry entry) {
        AnnotatedSeq.ELEMENT[] elts = {
            AnnotatedSeq.ELEMENT.COMMENT,
            AnnotatedSeq.ELEMENT.DESC,
            AnnotatedSeq.ELEMENT.FEATURE,
            AnnotatedSeq.ELEMENT.FOLDER,
            AnnotatedSeq.ELEMENT.OVERHANG,
            AnnotatedSeq.ELEMENT.REF,
            AnnotatedSeq.ELEMENT.SEQ
        };        
        
        final int cutType = entry.getDownstreamCutType();
        final int[] cutPos = entry.getDownstreamCutPos();
        final int overhangLength = entry.getOverhangLength();
        final int newStart = entry.getStart() + Math.min(cutPos[0], cutPos[1]);
        AnnotatedSeq ret = as.clone(elts);
        ret.setCircular(false);
        ret.setLength(overhangLength + ret.getLength());
        ret.getSiquence().linearize(entry);

        ret.getFetureSet().linearize(newStart, ret.getLength());

        ret.setCreationDate(new Date());
        ret.setLastModifiedDate(new Date());

        if (cutType != REN.BLUNT) {
            // start overhang
            Overhang overhang = new Overhang();
            overhang.setName(entry.getName());
            overhang.setLength(entry.getOverhangLength());
            overhang.setFivePrime(cutType == REN.OVERHANG_5PRIME);
            overhang.setStrand(overhang.isFivePrime());
            ret.setStartOverhang(overhang);

            // end overhang
            overhang = new Overhang();
            overhang.setName(entry.getName());
            overhang.setLength(entry.getOverhangLength());
            overhang.setFivePrime(cutType == REN.OVERHANG_5PRIME);
            overhang.setStrand(overhang.isThreePrime());
            ret.setEndOverhang(overhang);
        }

        return ret;
    }

    public static AnnotatedSeq subAs(AnnotatedSeq as, int start, int end) {
        AnnotatedSeq.ELEMENT[] elts = {
            AnnotatedSeq.ELEMENT.COMMENT,
            AnnotatedSeq.ELEMENT.DESC,
            AnnotatedSeq.ELEMENT.FEATURE,
            AnnotatedSeq.ELEMENT.FOLDER,
            AnnotatedSeq.ELEMENT.OVERHANG,
            AnnotatedSeq.ELEMENT.REF,
            AnnotatedSeq.ELEMENT.SEQ
        };
        return subAs(as, start, end, elts);
    }

    public static AnnotatedSeq subAs(AnnotatedSeq as, int start, int end, AnnotatedSeq.ELEMENT... elements) {
        AnnotatedSeq ret = as.clone(elements);
        int oldLength = ret.getLength();

        ret.getSiquence().subsiqence(start, end);
        ret.setLength(ret.getSiquence().getData().length());

        // fetures
        ret.getFetureSet().subSeq(start, end, false, oldLength);

        // overhang
        Iterator<Overhang> oItr = ret.getOverhangItr();
        while (oItr.hasNext()) {
            Overhang o = oItr.next();
            if (o.isStartOverhang() && start > 1) {
                if (start < o.getLength()) {
                    o.setLength(o.getLength() - start);
                } else {
                    oItr.remove();
                }
            } else if (o.isEndOverhang() && end < as.getLength()) {
                if (as.getLength() - end < o.getLength()) {
                    o.setLength(o.getLength() - (as.getLength() - end));
                } else {
                    oItr.remove();
                }
            }
        }

        // rmap result
        if (ret.getRmap() != null) {
            ret.getRmap().sub(start, end);
        }

        //primer3 result
        if (ret.getP3output() != null) {
            P3Output p3output = ret.getP3output().sub(start, end);
            if (p3output.getOligoSize() == 0) {
                ret.setP3output(null);
            } else {
                ret.setP3output(p3output);
            }
        }

        ret.setLastModifiedDate(new Date());
        ret.setCreationDate(new Date());

        ret.setCircular(false);

        return ret;
    }

    public static void setRestrictionMap(AnnotatedSeq as, RMap rm) {
        Comment comment = new Comment();
        comment.setRank(Comment.RANK);
        comment.setData(rm.toString());
        as.getComments().add(comment);
    }

    public static SymbolList toSymbolList(AnnotatedSeq as) {
        SymbolList ret = null;

        try {
            // for historical reasons, even if it's RNA, it use symbol "T" rather than "U"
            if (isDNA(as)) {
                ret = DNATools.createDNA(as.getSiquence().getData());
            } else if (isRNA(as)) {
                String data = as.getSiquence().getData().toUpperCase(Locale.ENGLISH);
                data = StrUtil.replaceAll(data, "T", "U");
                ret = RNATools.createRNA(data);
            } else if (isAminoAcid(as)) {
                ret = ProteinTools.createProtein(as.getSiquence().getData());
            } else {
                throw new IllegalArgumentException("Unknown Molecule Type");
            }
        } catch (IllegalSymbolException ex) {
            Logger.getLogger(AsHelper.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ret;
    }

    public static void print(AnnotatedSeq as) {
        System.out.println(as.getLength());
        System.out.println(as.getFetureSet());
    }
}
