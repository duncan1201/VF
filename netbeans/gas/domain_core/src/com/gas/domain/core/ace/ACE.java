/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.ace;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author dq
 */
public class ACE {

    private List<Contig> contigs = new ArrayList<Contig>();

    public List<Contig> getContigs() {
        return contigs;
    }

    public Integer getTotalNumberOfReads() {
        Integer ret = 0;
        Iterator<Contig> itr = contigs.iterator();
        while (itr.hasNext()) {
            Contig contig = itr.next();
            ret += contig.getReads().size();
        }
        return ret;
    }

    public Contig getLastContig() {
        return contigs.get(contigs.size() - 1);
    }

    public void setContigs(List<Contig> contigs) {
        this.contigs = contigs;
    }

    public static class Contig implements Serializable {

        private transient Integer hibernateId;
        private String name;
        private Boolean complemented;
        private String sequence = "";
        private String baseQualities = "";
        private Map<String, Read> reads = new HashMap<String, Read>();
        private Set<BS> baseSegments = new HashSet<BS>();

        public Integer getHibernateId() {
            return hibernateId;
        }

        public void setHibernateId(Integer hibernateId) {
            this.hibernateId = hibernateId;
        }

        public Set<BS> getBaseSegments() {
            return baseSegments;
        }

        public void setBaseSegments(Set<BS> baseSegments) {
            this.baseSegments = baseSegments;
        }

        public String getBaseQualities() {
            return baseQualities;
        }

        public void setBaseQualities(String baseQualities) {
            this.baseQualities = baseQualities;
        }

        public Boolean getComplemented() {
            return complemented;
        }

        public void setComplemented(Boolean complemented) {
            this.complemented = complemented;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Map<String, Read> getReads() {
            return reads;
        }

        public void setReads(Map<String, Read> reads) {
            this.reads = reads;
        }

        public String getSequence() {
            return sequence;
        }

        public void setSequence(String sequence) {
            this.sequence = sequence;
        }
    }

    public static class Read implements Serializable {

        private transient Integer hibernateId;
        private String name;
        private Boolean complemented;
        private Integer paddedStart;
        private String sequence = "";
        private Integer qualClippingStart;
        private Integer qualClippingEnd;
        private Integer alignClippingStart;
        private Integer alignClippingEnd;
        private String dataSource = ""; // DS tag

        public Integer getHibernateId() {
            return hibernateId;
        }

        public void setHibernateId(Integer hibernateId) {
            this.hibernateId = hibernateId;
        }

        public Integer getAlignClippingEnd() {
            return alignClippingEnd;
        }

        public void setAlignClippingEnd(Integer alignClippingEnd) {
            this.alignClippingEnd = alignClippingEnd;
        }

        public Integer getAlignClippingStart() {
            return alignClippingStart;
        }

        public void setAlignClippingStart(Integer alignClippingStart) {
            this.alignClippingStart = alignClippingStart;
        }

        public Boolean getComplemented() {
            return complemented;
        }

        public void setComplemented(Boolean complemented) {
            this.complemented = complemented;
        }

        public String getDataSource() {
            return dataSource;
        }

        public void setDataSource(String dataSource) {
            this.dataSource = dataSource;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getPaddedStart() {
            return paddedStart;
        }

        public void setPaddedStart(Integer paddedStart) {
            this.paddedStart = paddedStart;
        }

        public Integer getQualClippingEnd() {
            return qualClippingEnd;
        }

        public void setQualClippingEnd(Integer qualClippingEnd) {
            this.qualClippingEnd = qualClippingEnd;
        }

        public Integer getQualClippingStart() {
            return qualClippingStart;
        }

        public void setQualClippingStart(Integer qualClippingStart) {
            this.qualClippingStart = qualClippingStart;
        }

        public String getSequence() {
            return sequence;
        }

        public void setSequence(String sequence) {
            this.sequence = sequence;
        }
    }

    public static class BS implements Serializable {

        private transient Integer hibernateId;
        //BS <padded start consensus position> <padded end consensus position> <read name>
        private Integer paddedStart;
        private Integer paddedEnd;
        private String readName;

        public Integer getHibernateId() {
            return hibernateId;
        }

        public void setHibernateId(Integer hibernateId) {
            this.hibernateId = hibernateId;
        }

        public Integer getPaddedEnd() {
            return paddedEnd;
        }

        public void setPaddedEnd(Integer paddedEnd) {
            this.paddedEnd = paddedEnd;
        }

        public Integer getPaddedStart() {
            return paddedStart;
        }

        public void setPaddedStart(Integer paddedStart) {
            this.paddedStart = paddedStart;
        }

        public String getReadName() {
            return readName;
        }

        public void setReadName(String readName) {
            this.readName = readName;
        }
    }
}
