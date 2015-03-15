/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author dq
 */
public class VariantMapMdl {

    private List<Read> reads = new ArrayList<Read>();
    private Integer length;
    private transient Integer transitionCount;
    private transient Integer transversionCount;
    private transient Integer gapCount;
    private transient Integer ambiguityCount;
    private transient Integer insertionCount;

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getAmbiguityCount() {
        if (ambiguityCount == null) {
            Iterator<Read> itr = reads.iterator();
            while (itr.hasNext()) {
                Read read = itr.next();
                Integer c = read.getAmbiguityCount();
                if (c != null) {
                    if (ambiguityCount == null) {
                        ambiguityCount = 0;
                    }
                    ambiguityCount += c;
                }
            }
        }
        return ambiguityCount;
    }

    public Integer getInsertionCount() {
        if (insertionCount == null) {
            Iterator<Read> itr = reads.iterator();
            while (itr.hasNext()) {
                Read read = itr.next();
                Integer c = read.getInsertionCount();
                if (c != null) {
                    if (insertionCount == null) {
                        insertionCount = 0;
                    }
                    insertionCount += c;
                }
            }
        }
        return insertionCount;
    }

    public Integer getGapCount() {
        if (gapCount == null) {
            Iterator<Read> itr = reads.iterator();
            while (itr.hasNext()) {
                Read read = itr.next();
                Integer c = read.getGapCount();
                if (c != null) {
                    if (gapCount == null) {
                        gapCount = 0;
                    }
                    gapCount += c;
                }
            }
        }
        return gapCount;
    }

    public Integer getTransitionCount() {
        if (transitionCount == null) {
            Iterator<Read> itr = reads.iterator();
            while (itr.hasNext()) {
                Read read = itr.next();
                Integer c = read.getTransitionCount();
                if (c != null) {
                    if (transitionCount == null) {
                        transitionCount = 0;
                    }
                    transitionCount += c;
                }
            }
        }
        return transitionCount;
    }

    public Integer getTransversionCount() {
        if (transversionCount == null) {
            Iterator<Read> itr = reads.iterator();
            while (itr.hasNext()) {
                Read read = itr.next();
                Integer c = read.getTransversionCount();
                if (c != null) {
                    if (transversionCount == null) {
                        transversionCount = 0;
                    }
                    transversionCount += c;
                }
            }
        }
        return transversionCount;
    }

    public List<Read> getReads() {
        return reads;
    }

    public Map<String, Read> getReadMap() {
        Map<String, Read> ret = new HashMap<String, Read>();
        Iterator<Read> itr = reads.iterator();
        while (itr.hasNext()) {
            Read read = itr.next();
            ret.put(read.getReadName(), read);
        }
        return ret;
    }

    public List<Read> getSortedReads() {
        Collections.sort(reads, new ReadSorter());
        return reads;
    }

    public List<Variant> getSortedVariants() {
        List<Variant> ret = new ArrayList<Variant>();
        Iterator<Read> itr = reads.iterator();
        while (itr.hasNext()) {
            Read read = itr.next();
            ret.addAll(read.getVariants());
        }
        Collections.sort(ret, new VariantSorter());
        return ret;
    }

    public int getReadCount() {
        return reads.size();
    }

    public int getVariantsCount() {
        int i = 0;
        Iterator<Read> itr = reads.iterator();
        while (itr.hasNext()) {
            Read read = itr.next();
            i += read.getVariants().size();
        }
        return i;
    }

    public void setReads(List<Read> reads) {
        this.reads = reads;
    }

    public static class Read {

        private String readName;
        private Integer start;
        private Integer end;
        private List<Variant> variants = new ArrayList<Variant>();
        private transient Integer transitionCount;
        private transient Integer transversionCount;
        private transient Integer gapCount;
        private transient Integer ambiguityCount;
        private transient Integer insertionCount;

        public Integer getAmbiguityCount() {
            if (ambiguityCount == null) {
                Iterator<Variant> itr = variants.iterator();
                while (itr.hasNext()) {
                    Variant v = itr.next();
                    if (ambiguityCount == null) {
                        ambiguityCount = 0;
                    }
                    if (v.getAny()) {
                        ambiguityCount++;
                    }
                }

            }
            return ambiguityCount;
        }

        public Integer getInsertionCount() {
            if (insertionCount == null) {
                Iterator<Variant> itr = variants.iterator();
                while (itr.hasNext()) {
                    Variant v = itr.next();
                    if (insertionCount == null) {
                        insertionCount = 0;
                    }
                    if (v.getInsertion()) {
                        insertionCount++;
                    }
                }

            }
            return insertionCount;
        }

        public Integer getGapCount() {
            if (gapCount == null) {
                Iterator<Variant> itr = variants.iterator();
                while (itr.hasNext()) {
                    Variant v = itr.next();
                    if (gapCount == null) {
                        gapCount = 0;
                    }
                    if (v.getGap()) {
                        gapCount++;
                    }
                }

            }
            return gapCount;
        }

        public Integer getTransitionCount() {
            if (transitionCount == null) {
                Iterator<Variant> itr = variants.iterator();
                while (itr.hasNext()) {
                    Variant v = itr.next();
                    if (transitionCount == null) {
                        transitionCount = 0;
                    }
                    if (v.getTransition()) {
                        transitionCount++;
                    }
                }

            }
            return transitionCount;
        }

        public Integer getTransversionCount() {
            if (transversionCount == null) {
                Iterator<Variant> itr = variants.iterator();
                while (itr.hasNext()) {
                    Variant v = itr.next();
                    if (transversionCount == null) {
                        transversionCount = 0;
                    }
                    if (v.getTransversion()) {
                        transversionCount++;
                    }
                }
            }
            return transversionCount;
        }

        public Integer getEnd() {
            return end;
        }

        public void setEnd(Integer end) {
            this.end = end;
        }

        public String getReadName() {
            return readName;
        }

        public void setReadName(String readName) {
            this.readName = readName;
        }

        public Integer getStart() {
            return start;
        }

        public void setStart(Integer start) {
            this.start = start;
        }

        public List<Variant> getVariants() {
            return variants;
        }

        public void setVariants(List<Variant> variants) {
            this.variants = variants;
        }
    }

    public static class Variant {

        private String readName;
        private Integer readStart; // relative to the consensus
        private Integer pos; // 1-based. Relative to the read
        private Boolean gap = false;
        private Boolean any = false;
        private Boolean insertion = false;
        private Boolean transition = false; // a<->g , c<->t
        private Boolean transversion = false; // a<->c, a<->t, c<->g, g<->t
        private Character toBase;
        private Character fromBase;

        public String getReadName() {
            return readName;
        }

        public void setReadName(String readName) {
            this.readName = readName;
        }

        public Boolean getInsertion() {
            return insertion;
        }

        public void setInsertion(Boolean insertion) {
            this.insertion = insertion;
        }

        public Integer getReadStart() {
            return readStart;
        }

        public void setReadStart(Integer readStart) {
            this.readStart = readStart;
        }

        public Boolean getGap() {
            return gap;
        }

        public void setGap(Boolean gap) {
            this.gap = gap;
        }

        public Boolean getAny() {
            return any;
        }

        public void setAny(Boolean any) {
            this.any = any;
        }

        public Integer getPos() {
            return pos;
        }

        public Character getToBase() {
            return toBase;
        }

        public void setToBase(Character toBase) {
            this.toBase = toBase;
        }

        public Character getFromBase() {
            return fromBase;
        }

        public void setFromBase(Character fromBase) {
            this.fromBase = fromBase;
        }

        public Boolean getTransition() {
            return transition;
        }

        public void setTransition(Boolean transition) {
            this.transition = transition;
        }

        public Boolean getTransversion() {
            return transversion;
        }

        public void setTransversion(Boolean transversion) {
            this.transversion = transversion;
        }

        /*
         * @param pos: 1-based. Relative to the read
         */
        public void setPos(Integer pos) {
            this.pos = pos;
        }
    }

    public static class VariantSorter implements Comparator<Variant> {

        @Override
        public int compare(Variant o1, Variant o2) {
            int ret = 0;
            Integer p1 = o1.getPos() + o1.getReadStart();
            Integer p2 = o2.getPos() + o2.getReadStart();
            ret = p1.compareTo(p2);
            return ret;
        }
    }

    public static class ReadSorter implements Comparator<Read> {

        @Override
        public int compare(Read o1, Read o2) {
            int ret = 0;
            ret = o1.getStart().compareTo(o2.getStart());
            if (ret == 0) {
                ret = o1.getReadName().compareTo(o2.getReadName());
            }
            if (ret == 0) {
                ret = o1.getEnd().compareTo(o2.getEnd());
            }
            return ret;
        }
    }
}
