package com.gas.domain.core.primer3;

import com.gas.common.ui.util.CommonUtil;
import com.gas.common.ui.util.LocUtil;
import java.text.NumberFormat;
import java.util.Locale;

public class OligoElement implements Cloneable {

    private Integer hibernateId;
    private String name;
    private Integer no;
    private Boolean forward;
    private String problems;
    private int start;
    private int length;
    private Float tm;
    private Float gc;
    private Float selfAny;
    private Float selfEnd;
    private Float hairpin;
    private Float dntpConc;
    private Float monovalentCationsConc;
    private Float divalentCationsConc;
    private Float annealingOligoConc;
    private String seq;
    private String seqTemplate;
    // 5' to 3'
    private String tail = "";

    public OligoElement() {
    }

    public Float getAnnealingOligoConc() {
        return annealingOligoConc;
    }

    public void setAnnealingOligoConc(Float annealingOligoConc) {
        this.annealingOligoConc = annealingOligoConc;
    }

    public Float getDntpConc() {
        return dntpConc;
    }

    public void setDntpConc(Float dntpConc) {
        this.dntpConc = dntpConc;
    }

    public Float getMonovalentCationsConc() {
        return monovalentCationsConc;
    }

    public void setMonovalentCationsConc(Float monovalentCationsConc) {
        this.monovalentCationsConc = monovalentCationsConc;
    }

    public Float getDivalentCationsConc() {
        return divalentCationsConc;
    }

    public void setDivalentCationsConc(Float divalentCationsConc) {
        this.divalentCationsConc = divalentCationsConc;
    }

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
    }

    public String getProblems() {
        return problems;
    }    

    public void setProblems(String problems) {
        this.problems = problems;
    }
    
    public int getProblemCount(){
        int ret = 0;
        if(hasProblems()){
            for(int i = 0; i < problems.length(); i++){
                char c = problems.charAt(i);
                if(c == ';'){
                    ret++;
                }
            }
        }
        return ret;
    }
    
    public boolean hasProblems(){
        return problems != null && !problems.isEmpty();
    }
    
    public void translate(int x) {
        start += x;
    }

    public String getTail() {
        return tail;
    }

    public void setTail(String tail) {
        this.tail = tail;
    }

    /**
     * @param name only possible values are: "LEFT", "RIGHT", "INTERNAL"
     */
    public OligoElement(String name) {
        this.name = name;
    }

    @Override
    public OligoElement clone() {
        OligoElement ret = CommonUtil.cloneSimple(this);
        return ret;
    }

    public Integer getHibernateId() {
        return hibernateId;
    }

    public void setHibernateId(Integer hibernateId) {
        this.hibernateId = hibernateId;
    }

    /**
     * @param name only possible values are: "LEFT", "RIGHT", "INTERNAL"
     */
    public String getName() {
        return name;
    }

    /**
     * @param name only possible values are: "LEFT", "RIGHT", "INTERNAL"
     */
    public void setName(String name) {
        this.name = name;
    }

    public boolean isSupersetOf(int start, int end, boolean proper) {
        return LocUtil.isSupersetOf(calculateStart(), calculateEnd(), start, end, proper);
    }

    public boolean isSubsetOf(int start, int end) {
        return LocUtil.isSubsetOf(calculateStart(), calculateEnd(), start, end);
    }

    public boolean intersect(int start, int end) {
        return !LocUtil.intersect(calculateStart(), calculateEnd(), start, end).isEmpty();
    }

    public Float getHairpin() {
        return hairpin;
    }

    public void setHairpin(Float hairpin) {
        this.hairpin = hairpin;
    }

    public Boolean getForward() {
        return forward;
    }

    public void setForward(Boolean forward) {
        this.forward = forward;
    }

    public boolean isInternal() {
        return getName().equalsIgnoreCase("internal");
    }

    public boolean isForwardPrimer() {
        return getName().equalsIgnoreCase("left");
    }

    public boolean isReversePrimer() {
        return getName().equalsIgnoreCase("right");
    }

    public Integer calculateEnd() {
        Integer ret = null;
        if (forward != null && forward) {
            ret = start + length - 1;
        } else if (forward != null && !forward) {
            ret = start + (tail == null? 0 : tail.length());
        }
        return ret;
    }

    public Integer calculateStart() {
        Integer ret = null;
        if (forward != null && forward) {
            ret = start - (tail == null ? 0 : tail.length());
        } else if (forward != null && !forward) {
            ret = start - length + 1;
        }
        return ret;
    }

    public int getStart() {
        return start;
    }

    /**
     * @param value in the format of "<start>,<length>"
     */
    public void setStartLength(String value) {
        int start = Integer.parseInt(value.substring(0, value.indexOf(',')));
        int length = Integer.parseInt(value.substring(value.indexOf(',') + 1));
        setStart(start);
        setLength(length);
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public Float getTm() {
        return tm;
    }

    public void setTm(Float tm) {
        this.tm = tm;
    }

    public Float getGc() {
        return gc;
    }

    public void setGc(Float gc) {
        this.gc = gc;
    }

    public String getSeq() {
        return seq;
    }

    public String getSeqTemplate() {
        return seqTemplate;
    }

    public void setSeqTemplate(String seqTemplate) {
        this.seqTemplate = seqTemplate;
    }
    
    /**
     * 5'->3' direction
     */
    public void setSeq(String seq) {
        this.seq = seq;
    }

    public Float getSelfAny() {
        return selfAny;
    }

    public void setSelfAny(Float selfAny) {
        this.selfAny = selfAny;
    }

    public Float getSelfEnd() {
        return selfEnd;
    }

    public void setSelfEnd(Float selfEnd) {
        this.selfEnd = selfEnd;
    }
    
    @Override
    public String toString(){
        StringBuilder ret = new StringBuilder();        
        ret.append(start);
        ret.append(",");
        ret.append(length);
        ret.append(' ');
        ret.append("TM:");
        ret.append(tm);
        ret.append(' ');
        ret.append("GC:");
        ret.append(gc);        
        if(problems != null && !problems.isEmpty()){
            ret.append(' ');
            ret.append(problems);
        }
        return ret.toString();
    }

    public String toHtml() {
        NumberFormat nf = NumberFormat.getInstance(Locale.ENGLISH);
        NumberFormat nfPercent = NumberFormat.getPercentInstance(Locale.ENGLISH);
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        nfPercent.setMaximumFractionDigits(2);
        nfPercent.setMinimumFractionDigits(2);

        StringBuilder ret = new StringBuilder();
        ret.append("TM:");
        ret.append(nf.format(tm));
        ret.append("GC:");
        ret.append(nfPercent.format(gc / 100));
        ret.append("Self Complementarity:");
        ret.append(nf.format(selfAny));
        ret.append("3' Self Complementarity:");
        ret.append(nf.format(selfEnd));
        ret.append("Hairpin:");
        ret.append(nf.format(hairpin));
        return ret.toString();
    }
}
