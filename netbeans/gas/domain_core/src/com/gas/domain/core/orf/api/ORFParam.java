package com.gas.domain.core.orf.api;

import com.gas.common.ui.core.StringList;
import com.gas.common.ui.util.BioUtil;
import com.gas.common.ui.util.CommonUtil;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

public class ORFParam implements Cloneable {

    private Integer hibernateId;
    private int minLength = 100;
    private Integer geneticCodeTableId;
    private boolean interiorCounted = false;
    private boolean stopCodonIncluded = false;
    private boolean circular;
    private Set<Integer> frames = new HashSet<Integer>();
    private String sequence;
    private Set<String> startCodons = new HashSet<String>();
    private Set<String> stopCodons = new HashSet<String>();

    public Integer getHibernateId() {
        return hibernateId;
    }

    public void setHibernateId(Integer hibernateId) {
        this.hibernateId = hibernateId;
    }

    @Override
    public ORFParam clone() {
        ORFParam ret = CommonUtil.cloneSimple(this);
        ret.setFrames(CommonUtil.copyOf(frames));
        ret.setStartCodons(CommonUtil.copyOf(startCodons));
        ret.setStopCodonIncluded(stopCodonIncluded);
        ret.setStopCodons(CommonUtil.copyOf(stopCodons));
        return ret;
    }

    public Integer getGeneticCodeTableId() {
        return geneticCodeTableId;
    }

    public void setGeneticCodeTableId(Integer geneticCodeTableId) {
        this.geneticCodeTableId = geneticCodeTableId;
    }

    public boolean isCircular() {
        return circular;
    }

    public Set<Integer> getFrames() {
        return frames;
    }

    void touchAll() {
        Iterator<Integer> itrFrame = getFrames().iterator();
        while (itrFrame.hasNext()) {
            itrFrame.next();
        }
        Iterator<String> itrStr = getStartCodons().iterator();
        while (itrStr.hasNext()) {
            itrStr.next();
        }
        itrStr = getStopCodons().iterator();
        while (itrStr.hasNext()) {
            itrStr.next();
        }
    }

    public void setFrames(Set<Integer> frames) {
        this.frames = frames;
    }

    public void setFrames(int[] frames) {
        this.frames.clear();
        for (int frame : frames) {
            this.frames.add(frame);
        }
    }

    public void setCircular(boolean circular) {
        this.circular = circular;
    }

    public boolean isStopCodonIncluded() {
        return stopCodonIncluded;
    }

    public void setStopCodonIncluded(boolean stopCodonIncluded) {
        this.stopCodonIncluded = stopCodonIncluded;
    }

    public int getMinLength() {
        return minLength;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    public boolean isInteriorCounted() {
        return interiorCounted;
    }

    public void setInteriorCounted(boolean interiorCounted) {
        this.interiorCounted = interiorCounted;
    }

    public Set<String> getStartCodons() {
        return startCodons;
    }

    public String getStartCodonsStr() {
        StringList strList = new StringList();
        strList.addAll(startCodons);
        return strList.toString(' ');
    }

    public void setStartCodons(Set<String> startCodons) {
        this.startCodons = startCodons;
    }

    public void setStartCodons(String codons) {
        this.startCodons.clear();
        StringTokenizer tokenzier = new StringTokenizer(codons);
        while (tokenzier.hasMoreTokens()) {
            String token = tokenzier.nextToken();
            startCodons.add(token);
        }
    }

    public void setStopCodons(String codons) {
        this.stopCodons.clear();
        StringTokenizer tokenzier = new StringTokenizer(codons);
        while (tokenzier.hasMoreTokens()) {
            String token = tokenzier.nextToken();
            stopCodons.add(token);
        }
    }

    public Set<String> getStopCodons() {
        return stopCodons;
    }

    public String getStopCodonsStr() {
        StringList ret = new StringList();
        ret.addAll(stopCodons);
        return ret.toString(' ');
    }

    public void setStopCodons(Set<String> stopCodons) {
        this.stopCodons = stopCodons;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public List<String> validate() {
        List<String> ret = new ArrayList();
        ret.addAll(checkFrames());
        ret.addAll(checkCodons());
        return ret;
    }

    private List<String> checkCodons() {
        List<String> ret = new ArrayList<String>();
        if (getStartCodons().isEmpty()) {
            ret.add("Cannot have empty start codons");
        } else {
            for (String codon : getStartCodons()) {
                if (codon.length() != 3) {
                    ret.add(String.format("Start codon '%s' not the length of 3", codon));
                } else if (!BioUtil.areNonambiguousDNAs(codon)) {
                    ret.add(String.format("Start codon '%s' cannot have ambiguous bases", codon));
                }
            }
        }
        if (getStopCodons().isEmpty()) {
            ret.add("Cannot have empty stop codons");
        } else {
            for (String codon : getStopCodons()) {
                if (codon.length() != 3) {
                    ret.add(String.format("Stop codon '%s' not the length of 3", codon));
                } else if (!BioUtil.areNonambiguousDNAs(codon)) {
                    ret.add(String.format("Stop codon '%s' cannot have ambiguous bases", codon));
                }
            }
        }
        return ret;
    }

    private List<String> checkFrames() {
        List<String> ret = new ArrayList<String>();
        if (frames != null && !frames.isEmpty()) {
            for (int frame : frames) {
                if (frame == 0 || frame > 3 || frame < -3) {
                    ret.add(String.format("Wrong frame: %d", frame));
                }
            }
        } else {
            ret.add(String.format("No frames selected", "Please select at least one frame"));            
        }
        return ret;
    }

    @Override
    public String toString() {
        return "[length:" + sequence.length() + "|isCirculr:" + circular + "]";
    }
}
