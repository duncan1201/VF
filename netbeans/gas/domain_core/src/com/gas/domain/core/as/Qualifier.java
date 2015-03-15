/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.as;

import java.util.Comparator;

/**
 *
 * @author dq
 */
public class Qualifier implements Cloneable {

    public static final String VF_TYPE = "VectorFriends_type";
    
    private Integer hibernateId;
    private String key;
    private String value;

    public Qualifier() {
    }

    public Qualifier(String qStr) {
        int index = qStr.indexOf('=');
        if (index < 0) {
            throw new IllegalArgumentException();
        }
        key = qStr.substring(0, index);
        value = qStr.substring(index + 1);
    }

    @Override
    public Qualifier clone() {
        Qualifier ret = new Qualifier();
        ret.setKey(key);
        ret.setValue(value);
        return ret;
    }

    public boolean isNote() {
        return key.equalsIgnoreCase("note");
    }

    public boolean isTranslation() {
        return key.equalsIgnoreCase("translation");
    }

    public boolean isProteinId() {
        return key.equalsIgnoreCase("protein_id");
    }

    public boolean isDirectionalTOPOLabel() {
        boolean ret = false;
        if (value.matches("(?i)5'-GGTG overhang")
                || value.matches("(?i)directional TOPO(.*) overhang")
                || value.matches("(?i)dTOPO overhang")) {
            ret = true;
        }
        return ret;
    }

    public boolean isTATOPOLabel() {
        boolean ret = false;
        if (value.matches("(?i)3'-T overhang")) {
            ret = true;
        }
        return ret;
    }

    public boolean isLinkableDbXref() {
        boolean ret = false;
        boolean isDbXRef = isDbXref();
        if (isDbXRef) {
            int index = getValue().indexOf(":");

        }
        return ret;
    }

    public boolean isDbXref() {
        return key.equalsIgnoreCase("db_xref");
    }

    public Qualifier(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public Integer getHibernateId() {
        return hibernateId;
    }

    public void setHibernateId(Integer hibernateId) {
        this.hibernateId = hibernateId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append(key);
        ret.append('=');
        ret.append(value);
        return ret.toString();
    }

    public static class QualifierComparator implements Comparator<Qualifier> {

        @Override
        public int compare(Qualifier o1, Qualifier o2) {
            int ret = o1.getKey().compareToIgnoreCase(o2.getKey());
            if (ret == 0) {
                ret = o1.getValue().compareToIgnoreCase(o2.getValue());
            }
            return ret;
        }
    }
}
