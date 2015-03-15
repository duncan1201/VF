package com.gas.domain.core.as;

import com.gas.common.ui.util.CommonUtil;
import java.util.Comparator;

public class Reference implements Cloneable {

    private Integer hibernateId;
    private Integer start;
    private Integer end;
    private Integer rank;
    private String title;
    private String location;
    private String authors;
    private String db;
    private String accession;
    private String doi = "";
    private String remark = "";
    private boolean doiVerified = false;

    protected Integer getHibernateId() {
        return hibernateId;
    }

    @Override
    public Reference clone() {
        Reference ret = new Reference();
        ret.setAccession(accession);
        ret.setAuthors(authors);
        ret.setDb(db);
        ret.setDoi(doi);
        ret.setDoiVerified(doiVerified);
        ret.setEnd(end);
        ret.setLocation(location);
        ret.setRank(rank);
        ret.setRemark(remark);
        ret.setStart(start);
        ret.setTitle(title);
        return ret;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDoi() {
        return doi;
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }

    public boolean isDoiVerified() {
        return doiVerified;
    }

    public void setDoiVerified(boolean doiVerified) {
        this.doiVerified = doiVerified;
    }

    protected void setHibernateId(Integer hibernateId) {
        this.hibernateId = hibernateId;
    }

    public Integer getEnd() {
        return end;
    }

    public void setEnd(Integer end) {
        this.end = end;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public String getAccession() {
        return accession;
    }

    public void setAccession(String accession) {
        this.accession = accession;
    }

    public static class RankComparator implements Comparator<Reference> {

        @Override
        public int compare(Reference o1, Reference o2) {
            int ret = o1.getRank().compareTo(o2.getRank());
            return ret;
        }
    }

    public static class PubYearComparator implements Comparator<Reference> {

        private boolean ascending;

        public PubYearComparator(boolean ascending) {
            this.ascending = ascending;
        }

        @Override
        public int compare(Reference o1, Reference o2) {
            int ret = 0;
            String loc1 = o1.getLocation();
            int start = loc1.lastIndexOf("(");
            int end = loc1.lastIndexOf(")");
            if (start < 0 || end < 0) {
                return -1;
            }
            Integer y1 = CommonUtil.parseInt(loc1.substring(start + 1, end));

            loc1 = o2.getLocation();
            start = loc1.lastIndexOf("(");
            end = loc1.lastIndexOf(")");
            if (start < 0 || end < 0) {
                ret = 1;
                return ret;
            }
            Integer y2 = CommonUtil.parseInt(loc1.substring(start + 1, end));

            if (y1 != null && y2 != null) {
                ret = y1.compareTo(y2);
            } else {
                ret = 0;
            }

            if (!ascending) {
                ret *= -1;
            }

            return ret;
        }
    }
}
