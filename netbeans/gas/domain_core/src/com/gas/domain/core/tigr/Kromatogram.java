/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.tigr;

import com.gas.common.ui.core.IntList;
import com.gas.domain.core.fasta.Fasta;
import com.gas.common.ui.util.BioUtil;
import com.gas.common.ui.util.CommonUtil;
import com.gas.common.ui.util.MathUtil;
import com.gas.common.ui.util.StrUtil;
import com.gas.domain.core.IFolderElement;
import com.gas.domain.core.filesystem.Folder;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.biojava.bio.seq.DNATools;

/**
 *
 * @author dq
 */
public class Kromatogram implements Serializable, Cloneable, IFolderElement {

    private transient String hibernateId;
    private transient List<Integer> qualityValues = new ArrayList<Integer>();
    private String qualityValuesStr; // for hibernate use
    private transient List<Integer> offsets;
    private String offsetsStr; // for hibernate use
    private transient List<Character> bases; // do not persist
    private String sequence; // for hibernate use
    private String fileName;
    private transient Integer maxQv;
    private transient Integer maxPeak;
    private Map<String, Trace> traces = new HashMap<String, Trace>();
    private String name;
    private transient Folder folder;
    private String desc;
    private Date lastModifiedDate;
    private String prevFolderPath;
    private boolean read;

    public Kromatogram(){}
    
    @Override
    public String getHibernateId() {
        return hibernateId;
    }

    public void setHibernateId(String hibernateId) {
        this.hibernateId = hibernateId;
    }

    @Override
    public Kromatogram clone() {
        Kromatogram ret = CommonUtil.cloneSimple(this);                
        ret.setFolder(folder);
        ret.setOffsetsStr(offsetsStr);
        ret.getOffsets(); // to init the offsets
        ret.setPrevFolderPath(prevFolderPath);
        ret.setQualityValuesStr(qualityValuesStr);
        ret.getQualityValues();// to init the quality values
        ret.setSequence(sequence);
        ret.getBases();// to init the bases
        ret.setTraces(CommonUtil.copyOf(traces));
        return ret;
    }

    /**
     * for hibernate use only
     */    
    protected String getQualityValuesStr() {
        return qualityValuesStr;
    }

    /**
     * for hibernate use only
     */    
    protected void setQualityValuesStr(String qualityValuesStr) {
        this.qualityValuesStr = qualityValuesStr;
    }

    /**
     * for hibernate use only
     */
    protected String getOffsetsStr() {
        return offsetsStr;
    }

    /**
     * for hibernate use only
     */
    protected void setOffsetsStr(String offsetsStr) {
        this.offsetsStr = offsetsStr;
    }

    protected String getSequence() {
        return sequence;
    }
    
    public Integer getLength(){
        Integer ret = null;
        if(sequence != null){
            ret = sequence.length();
        }
        return ret;
    }

    /*
     * For hibernate use only
     */
    protected void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public List<Character> getBases() {
        if (bases == null) {
            bases = new ArrayList<Character>();
        }
        if (bases.isEmpty() && sequence != null && sequence.length() > 0) {
            bases = StrUtil.toChars(sequence);
        }
        return bases;
    }

    public void setBases(List<Character> bases) {
        this.bases = bases;
        sequence = StrUtil.toString(bases);
    }

    public Integer getMaxPeak() {
        if (maxPeak == null) {
            maxPeak = Integer.MIN_VALUE;
            int[] tmp = new int[offsets.size()];
            for (int i = 0; i < offsets.size(); i++) {
                char base = bases.get(i);
                if(base == 'N' || base == 'n'){
                    continue;
                }
                Trace trace = getTrace(base);
                tmp[i] = trace.getData()[offsets.get(i)];
            }

            maxPeak = MathUtil.max(tmp);
        }
        return maxPeak;
    }

    private Trace getTrace(Character c) {
        return getTrace(c.toString());
    }

    private Trace getTrace(String s) {
        if (s.equalsIgnoreCase("a") || s.equals(DNATools.a().getName())) {
            return getTrace4A();
        } else if (s.equalsIgnoreCase("t") || s.equals(DNATools.t().getName())) {
            return getTrace4T();
        } else if (s.equalsIgnoreCase("c") || s.equals(DNATools.c().getName())) {
            return getTrace4C();
        } else if (s.equalsIgnoreCase("g") || s.equals(DNATools.g().getName())) {
            return getTrace4G();
        } else {
            return null;
        }
    }

    public Integer getMaxQv() {
        if (maxQv == null && qualityValues != null) {
            maxQv = MathUtil.max(qualityValues);
        }
        return maxQv;
    }

    public boolean hasQvs() {
        return qualityValues != null && qualityValues.size() > 0;
    }

    public List<Integer> getQualityValues() {
        if (qualityValues == null) {
            qualityValues = new ArrayList<Integer>();
        }
        if (qualityValues.isEmpty() && qualityValuesStr != null) {
            qualityValues = CommonUtil.toIntList(qualityValuesStr, 10);
        }
        return qualityValues;
    }

    public void setQualityValues(List<Integer> qualityValues) {
        this.qualityValues = qualityValues;
        qualityValuesStr = CommonUtil.toString(qualityValues);
    }

    public List<Integer> getOffsets() {
        if (offsets == null) {
            offsets = new ArrayList<Integer>();
        }
        if (offsets.isEmpty() && offsetsStr != null) {
            offsets = CommonUtil.toIntList(offsetsStr, 10);
        }
        return offsets;
    }

    public void setOffsets(List<Integer> offsets) {
        this.offsets = offsets;
        offsetsStr = CommonUtil.toString(offsets);
    }

    public int getPeak(int index) {
        char base = bases.get(index);
        int offset = offsets.get(index);
        Trace trace = getTrace(base);
        int peak = trace.getData()[offset];
        return peak;
    }

    /*
     * convenience method
     */
    public Trace getTrace4A() {
        return traces.get(DNATools.a().getName());
    }

    /*
     * convenience method
     */
    public Integer getTraceLength() {
        Integer ret = null;
        if (!traces.isEmpty()) {
            ret = getTrace4A().getData().length;
        }
        return ret;
    }

    /*
     * convenience method
     */
    public Trace getTrace4T() {
        return traces.get(DNATools.t().getName());
    }

    /*
     * convenient method
     */
    public Trace getTrace4C() {
        return traces.get(DNATools.c().getName());
    }

    /*
     * convenient method
     */
    public Trace getTrace4G() {
        return traces.get(DNATools.g().getName());
    }

    public Map<String, Trace> getTraces() {
        return traces;
    }

    public void setTrace(char base, Trace trace) {
        if (base == 'a' || base == 'A') {
            getTraces().put(DNATools.a().getName(), trace);
        }
    }

    public void setTraces(Map<String, Trace> traces) {
        this.traces = traces;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /*
     * gap postions are 1-based
     */
    public void addGaps(List<Integer> gaps) {
        if (gaps.size() > offsets.size()) {
            throw new IllegalArgumentException();
        }
        Integer[] gapsArr = gaps.toArray(new Integer[gaps.size()]);
        Arrays.sort(gapsArr);

        for (int i = 0; i < gaps.size(); i++) {
            Integer gap = gapsArr[i];
            qualityValues.add(gap - 1, null);
            offsets.add(gap - 1, null);
            bases.add(gap - 1, '-');
        }
        setOffsets(offsets);
        setBases(bases);
        setQualityValues(qualityValues);
    }

    /*
     * @param pos 1-based
     */
    public void trimStart(final int length) {
        if (length > getOffsets().size()) {
            throw new IllegalArgumentException(String.format("Position '%d' > offset size '%d'", length, offsets.size()));
        }

        for (int i = 0; i < length; i++) {
            offsets.remove(0);   
            if(!qualityValues.isEmpty()){
                qualityValues.remove(0);
            }
            bases.remove(0);
        }
        maxQv = null;
        maxPeak = null;
    }

    public void trimEnd(final int length) {
        if (length > offsets.size()) {
            throw new IllegalArgumentException(String.format("Position '%d' > offset size '%d'", length, offsets.size()));
        }

        for (int i = 0; i < length; i++) {
            offsets.remove(offsets.size() - 1);
            if(!qualityValues.isEmpty()){
                qualityValues.remove(qualityValues.size() - 1);
            }
            bases.remove(bases.size() - 1);
        }
        maxQv = null;
        maxPeak = null;
    }

    public Kromatogram reverseComplement() {
        Kromatogram ret = new Kromatogram();
        String reverseComp = BioUtil.reverseComplement(getBases());
        ret.setBases(StrUtil.toChars(reverseComp));

        ret.setFileName(getFileName());
        int traceLength = getTraceLength();
        List<Integer> reversedOffsets = new ArrayList<Integer>();
        for (int i = offsets.size() - 1; i > -1; i--) {
            Integer rOffset = traceLength - 1 - offsets.get(i);
            reversedOffsets.add(rOffset);
        }
        ret.setOffsets(reversedOffsets);

        Iterator<String> keyItr = getTraces().keySet().iterator();
        while (keyItr.hasNext()) {
            String key = keyItr.next();
            Trace reversedTrace = getTraces().get(key).reverse();
            String reversedKey = BioUtil.reverseComplementByName(key);
            ret.getTraces().put(reversedKey, reversedTrace);
        }

        ret.setQualityValues(CommonUtil.reverse(getQualityValues()));
        return ret;
    }
    
    public void touchAll(){
        Iterator<String> itr = getTraces().keySet().iterator();
        while(itr.hasNext()){
            String key = itr.next();
            // must assign the trace to a variable
            Trace trace = getTraces().get(key);
            int dataLength = trace.getData().length;
        }
        
        Iterator<Integer> itr2 = getOffsets().iterator();      
        
        Iterator<Character> itr3 = getBases().iterator();
    }

    public Fasta.Record toFastaRecord() {
        Fasta.Record r = new Fasta.Record();
        r.setDefinitionLine(fileName);
        r.setSequence(StrUtil.toString(getBases()));

        return r;
    }

    @Override
    public Folder getFolder() {
        return folder;
    }

    @Override
    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    @Override
    public String getPrevFolderPath() {
        return prevFolderPath;
    }

    @Override
    public void setPrevFolderPath(String p) {
        this.prevFolderPath = p;       
    }

    @Override
    public boolean isRead() {
        return read;
    }

    @Override
    public void setRead(boolean read) {
        this.read = read;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    @Override
    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
    
    @Override
    public Date getLastModifiedDate(){
        return this.lastModifiedDate;
    }  
}
