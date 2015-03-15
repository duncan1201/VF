package com.gas.domain.core.as;

import com.gas.common.ui.util.CommonUtil;
import java.util.Comparator;
import javax.persistence.Transient;

public class Feture implements Cloneable {

    private Integer hibernateId;
    private String key;
    private transient String displayName;
    private QualifierSet qualifierSet = new QualifierSet();
    private Lucation lucation;
    
    
    @Transient
    private boolean derived;
    @Transient
    private Object data;

    public Feture() {
        this(null);
    }

    public Feture(String key) {
        this.key = key;
    }

    public boolean isDerived() {
        return derived;
    }

    public void setDerived(boolean derived) {
        this.derived = derived;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
    
    public boolean isKeylegal(){
        return key.length() < 16 && !key.contains(" ");
    }

    @Override
    public Feture clone() {
        Feture ret = CommonUtil.cloneSimple(this);
        if (lucation != null) {
            ret.setLucation(lucation.clone());
        }
        ret.setQualifierSet(qualifierSet.clone());
        return ret;
    }

    public QualifierSet getQualifierSet() {
        return qualifierSet;
    }
  
    public void setQualifierSet(QualifierSet qualifierSet) {
        this.qualifierSet = qualifierSet;
    }

    protected Integer getHibernateId() {
        return hibernateId;
    }

    protected void setHibernateId(Integer hibernateId) {
        this.hibernateId = hibernateId;
    }

    public Lucation getLucation() {
        return lucation;
    }

    public void setLucation(Lucation lucation) {
        this.lucation = lucation;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void addQualifier(String qualifierStr) {
        if (qualifierStr.indexOf('=') < 0) {
            throw new IllegalArgumentException("Qualifier must contain symbol '='");
        }
        this.getQualifierSet().add(new Qualifier(qualifierStr));
    }

    public void setDisplayName(String s) {
        this.displayName = s;
    }

    private String deriveDisplayName() {
        StringBuilder ret = new StringBuilder();
        boolean appendKey = true;

        if (getQualifierSet().containsKey("label")) {
            ret.append(getQualifierSet().getQualifier("label").getValue());
            appendKey = false;
        } else {
            String gene = null;
            if (getQualifierSet().containsKey("gene")) {
                gene = getQualifierSet().getQualifier("gene").getValue();
            }

            if (key != null && key.equalsIgnoreCase("STS")) {
                if (getQualifierSet().containsKey("standard_name")) {
                    ret.append(getQualifierSet().getQualifier("standard_name").getValue());
                    appendKey = false;
                }
            } else if (key != null && key.equalsIgnoreCase("REGION")) {
                if (getQualifierSet().containsKey("region_name")) {
                    ret.append(getQualifierSet().getQualifier("region_name").getValue());
                    appendKey = false;
                }
            } else if (gene != null) {
                ret.append(gene);
                ret.append(" ");
            }

            if (key != null && key.equalsIgnoreCase("mat_peptide")) {
                ret.append("peptide");
                appendKey = false;
            }
        }

        if (appendKey) {
            if (key != null && key.equalsIgnoreCase("misc_feature")) {
                ret.append("misc");
            } else if (key != null) {
                ret.append(key);
            }
        }
        return ret.toString();
    }

    public String getDisplayName() {
        if (displayName == null || displayName.isEmpty()) {
            displayName = deriveDisplayName();
        }
        return displayName;
    }
    
    public static class StartComparator implements Comparator<Feture> {

        @Override
        public int compare(Feture o1, Feture o2) {
            int ret = 0;
            ret = o1.getLucation().getStart().compareTo(o2.getLucation().getStart());            
            if (ret == 0) {
                ret = o1.getKey().compareToIgnoreCase(o2.getKey());
            }
            return ret;
        }
    }    

    public static class FComparator implements Comparator<Feture> {

        @Override
        public int compare(Feture o1, Feture o2) {
            int ret = 0;
            ret = o1.getKey().compareToIgnoreCase(o2.getKey());
            if (ret == 0) {
                ret = o1.getLucation().getStart().compareTo(o2.getLucation().getStart());
            }
            return ret;
        }
    }

    public static class FtrNameComparator implements Comparator<String> {

        @Override
        public int compare(String o1, String o2) {
            int ret = 0;

            if (o1.equalsIgnoreCase(FetureKeyCnst.RECOGNITION_SITE) || o2.equalsIgnoreCase(FetureKeyCnst.RECOGNITION_SITE)) {
                if (o1.equalsIgnoreCase(FetureKeyCnst.RECOGNITION_SITE) && o2.equalsIgnoreCase(FetureKeyCnst.RECOGNITION_SITE)) {
                    ret = 0;
                } else if (o1.equalsIgnoreCase(FetureKeyCnst.RECOGNITION_SITE)) {
                    ret = -1;
                } else if (o2.equalsIgnoreCase(FetureKeyCnst.RECOGNITION_SITE)) {
                    ret = 1;
                }
            } else if (o1.equalsIgnoreCase(FetureKeyCnst.OVERHANG) || o2.equalsIgnoreCase(FetureKeyCnst.OVERHANG)) {
                if (o1.equalsIgnoreCase(FetureKeyCnst.OVERHANG) && o2.equalsIgnoreCase(FetureKeyCnst.OVERHANG)) {
                    ret = 0;
                } else if (o1.equalsIgnoreCase(FetureKeyCnst.OVERHANG)) {
                    ret = -1;
                } else if (o2.equalsIgnoreCase(FetureKeyCnst.OVERHANG)) {
                    ret = 1;
                }
            }

            if (ret == 0) {
                ret = o1.compareToIgnoreCase(o2);
            }

            return ret;
        }
    }
}
