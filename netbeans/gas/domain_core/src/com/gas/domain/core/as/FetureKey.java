/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.as;

import com.gas.common.ui.util.StrUtil;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author dq
 */
public class FetureKey {

    private Integer hibernateId; // for hibernate use only
    private String name;
    private String definition;
    private Set<String> qualifiers = new HashSet<String>();

    public Integer getHibernateId() {
        return hibernateId;
    }

    public void setHibernateId(Integer hibernateId) {
        this.hibernateId = hibernateId;
    }

    public FetureKey() {
    }

    public FetureKey(String name) {
        this(name, null);
    }
    
    public FetureKey(String name, String definition) {
        this.name = name;
        this.definition = definition;
    }

    public Set<String> getQualifiers() {
        return qualifiers;
    }

    public void setQualifiers(Set<String> qualifiers) {
        this.qualifiers = qualifiers;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        boolean ret = false;
        if (o instanceof FetureKey) {
            FetureKey fk = (FetureKey) o;
            ret = this.getName().equals(fk.getName());
        }
        return ret;
    }

    @Override
    public int hashCode() {
        int ret = 0;
        ret = this.getName().hashCode();
        return ret;
    }

    public static class NameComparator implements Comparator<FetureKey> {

        @Override
        public int compare(FetureKey o1, FetureKey o2) {
            int ret = 0;
            String name1 = o1.getName();
            String name2 = o2.getName();

            char char1 = name1.charAt(0);
            char char2 = name2.charAt(0);

            if (StrUtil.isAlphabetic(char1) && StrUtil.isAlphabetic(char2)) {
                ret = name1.compareToIgnoreCase(name2);
            } else if (StrUtil.isAlphabetic(char1)) {
                ret = 1;
            } else if (StrUtil.isAlphabetic(char2)) {
                ret = -1;
            } else {
                ret = 0;
            }

            return ret;
        }
    }
}
