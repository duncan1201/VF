package com.gas.domain.core.primer3;

import com.gas.common.ui.util.BioUtil;
import com.gas.common.ui.util.CommonUtil;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.biojava.BioJavaHelper;

public class Oligo implements Cloneable {

    public enum WHICH {
        left, right, internal;
        
        public static WHICH get(String s){
            WHICH ret = null;
            if(s.equalsIgnoreCase("left")){
                ret = left;
            }else if(s.equalsIgnoreCase("right")){
                ret = right;
            }else if(s.equalsIgnoreCase("internal")){
                ret = internal;
            }
            return ret;
        }
    };
    
    private Integer hibernateId;
    private Set<OligoElement> oligoElements = new HashSet<OligoElement>();
    private Integer productSize;
    private Float complAny;
    private Float complEnd;
    private Integer no; // 0-based

    public Oligo() {
    }

    public Oligo(Integer no) {
        this.no = no;
    }
    
    void updateConc(UserInput userInput) {
        for(OligoElement oe: oligoElements){
            if(oe.isInternal()){
                oe.setDivalentCationsConc(userInput.getFloat("PRIMER_INTERNAL_SALT_DIVALENT"));
                oe.setMonovalentCationsConc(userInput.getFloat("PRIMER_INTERNAL_SALT_MONOVALENT"));
                oe.setDntpConc(userInput.getFloat("PRIMER_INTERNAL_DNTP_CONC"));
                oe.setAnnealingOligoConc(userInput.getFloat("PRIMER_INTERNAL_DNA_CONC"));
            }else if(oe.isForwardPrimer() || oe.isReversePrimer()){
                oe.setDivalentCationsConc(userInput.getFloat("PRIMER_SALT_DIVALENT"));
                oe.setMonovalentCationsConc(userInput.getFloat("PRIMER_SALT_MONOVALENT"));
                oe.setDntpConc(userInput.getFloat("PRIMER_DNTP_CONC"));
                oe.setAnnealingOligoConc(userInput.getFloat("PRIMER_DNA_CONC"));
            }
        }
    }
    
    public void setStart(int start){
        for(OligoElement oe: oligoElements){
            oe.setStart(start);
        }
    }
    
    public void setLength(int length){
        for(OligoElement oe: oligoElements){
            oe.setLength(length);
        }
    }

    @Override
    public Oligo clone() {
        Oligo ret = CommonUtil.cloneSimple(this);
        ret.setOligoElements(CommonUtil.copyOf(oligoElements));
        return ret;
    }

    public Integer getHibernateId() {
        return hibernateId;
    }

    public void setHibernateId(Integer hibernateId) {
        this.hibernateId = hibernateId;
    }

    protected Set<OligoElement> getOligoElements() {
        return oligoElements;
    }

    protected void setOligoElements(Set<OligoElement> oligoElements) {
        this.oligoElements = oligoElements;
    }
    
    public void setSeqTemplateForOligoElements(String seqTemplate){
        Iterator<OligoElement> itr = this.oligoElements.iterator();
        while(itr.hasNext()){
            OligoElement oe = itr.next();            
            final int calculatedStart = oe.calculateStart();
            final int calculatedEnd = oe.calculateEnd();
            final String subSeqTemplate = seqTemplate.substring(calculatedStart - 1, calculatedEnd);
            if(oe.getForward()){
                oe.setSeqTemplate(subSeqTemplate);
            }else{                
                String rc = BioUtil.reverseComplement(subSeqTemplate);
                oe.setSeqTemplate(rc);
            }
        }
    }

    /**
     * @return 0-based
     */
    public Integer getNo() {
        return no;
    }

    /**
     * @param no: 0-based
     */
    public void setNo(Integer no) {
        this.no = no;
    }

    public OligoElement getLeft() {
        return get("Left");
    }

    public void setLeft(OligoElement left) {
        left.setName("left");
        left.setForward(true);
        remove("left");
        oligoElements.add(left);
    }

    private void remove(String name) {
        Iterator<OligoElement> itr = oligoElements.iterator();
        while (itr.hasNext()) {
            OligoElement oe = itr.next();
            if (oe.getName().equalsIgnoreCase(name)) {
                itr.remove();
            }
        }
    }

    public OligoElement get(String name) {
        OligoElement ret = null;
        Iterator<OligoElement> itr = oligoElements.iterator();
        while (itr.hasNext()) {
            OligoElement oe = itr.next();
            if (oe.getName().equalsIgnoreCase(name)) {
                ret = oe;
                break;
            }
        }
        return ret;
    }

    public OligoElement getRight() {
        return get("Right");
    }

    public void setRight(OligoElement right) {
        right.setName("right");
        right.setForward(false);
        remove("right");
        oligoElements.add(right);
    }

    public OligoElement getInternal() {
        return get("Internal");
    }

    public void setInternal(OligoElement internal) {
        internal.setName("Internal");
        internal.setForward(true);
        remove("internal");
        oligoElements.add(internal);
    }

    public Integer getProductSize() {
        return productSize;
    }

    public Integer calculateProductSize() {
        Integer ret = productSize;
        OligoElement left = getLeft();
        if (left != null && left.getTail() != null) {
            ret += left.getTail().length();
        }
        OligoElement right = getRight();
        if (right != null && right.getTail() != null) {
            ret += right.getTail().length();
        }
        return ret;
    }

    public void setProductSize(Integer productSize) {
        this.productSize = productSize;
    }

    public Float getComplAny() {
        return complAny;
    }

    public void setComplAny(Float complAny) {
        this.complAny = complAny;
    }

    public Float getComplEnd() {
        return complEnd;
    }

    public void setComplEnd(Float complEnd) {
        this.complEnd = complEnd;
    }
    
    @Override
    public String toString(){
        StringBuilder ret = new StringBuilder();
        Iterator<OligoElement> itr = oligoElements.iterator();
        while(itr.hasNext()){
            OligoElement oe = itr.next();
            if(oe.isForwardPrimer()){
                ret.append("F");
            }else if(oe.isReversePrimer()){
                ret.append("R");
            }
            ret.append("["+oe+"]");
            if(itr.hasNext()){
                ret.append(' ');
            }
        }
        return ret.toString();
    }

    public String toHtml() {
        StringBuilder ret = new StringBuilder();
        ret.append("<html>");
        OligoElement left = getLeft();
        if (left != null) {
            String htmlLeft = left.toHtml();
            ret.append(htmlLeft);
        }
        ret.append("</html>");
        return ret.toString();
    }

    public void touchAll() {
        Iterator<OligoElement> itr = oligoElements.iterator();
        while (itr.hasNext()) {
            itr.next();
        }
    }

    public static class NoComparator implements Comparator<Oligo> {

        @Override
        public int compare(Oligo o1, Oligo o2) {
            return o1.getNo().compareTo(o2.getNo());
        }
    }
}
