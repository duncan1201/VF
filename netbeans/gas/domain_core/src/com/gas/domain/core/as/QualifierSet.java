/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.as;

import com.gas.common.ui.core.StringList;
import com.gas.common.ui.util.CommonUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 *
 * @author dq
 */
public class QualifierSet implements Cloneable {

    private Integer hibernateId;
    private Set<Qualifier> qualifiers = new HashSet<Qualifier>();

    public QualifierSet() {
    }

    public Integer getHibernateId() {
        return hibernateId;
    }

    public void setHibernateId(Integer hibernateId) {
        this.hibernateId = hibernateId;
    }

    public void touchAll() {
        Iterator<Qualifier> itr = iterator();
        while (itr.hasNext()) {
            itr.next();
        }
    }

    public void add(Qualifier a) {
        this.qualifiers.add(a);
    }

    public void add(String str) {
        this.qualifiers.add(new Qualifier(str));
    }

    public void clear() {
        qualifiers.clear();
    }

    public void remove(Qualifier q) {
        this.qualifiers.remove(q);
    }
    
    public void remove(String... keys){
        StringList keyList = new StringList(keys);
        Iterator<Qualifier> itr = qualifiers.iterator();
        while(itr.hasNext()){
            Qualifier qualifier = itr.next();
            if(keyList.contains(qualifier.getKey())){
                itr.remove();
            }            
        }
    }

    public Iterator<Qualifier> iterator() {
        return this.qualifiers.iterator();
    }

    public Set<String> getQualifierStrs() {
        Set<String> ret = new HashSet<String>();
        Iterator<Qualifier> itr = iterator();
        while (itr.hasNext()) {
            Qualifier q = itr.next();
            ret.add(q.toString());
        }
        return ret;
    }

    public List<Qualifier> getSortedQualifiers() {
        List<Qualifier> ret = new ArrayList<Qualifier>(qualifiers);
        Collections.sort(ret, new QualifierComparator());
        return ret;
    }

    public Set<Qualifier> getQualifiers() {
        return qualifiers;
    }

    public void setQualifiers(Set<Qualifier> qualifiers) {
        this.qualifiers = qualifiers;
    }

    public QualifierSet(Collection<Qualifier> q) {
        qualifiers.addAll(q);
    }

    public boolean containsKey(final String key) {
        boolean ret = false;
        Iterator<Qualifier> itr = qualifiers.iterator();
        while (itr.hasNext()) {
            Qualifier qualifier = itr.next();
            if (qualifier.getKey().equalsIgnoreCase(key)) {
                ret = true;
                break;
            }
        }
        return ret;
    }

    public Qualifier getQualifier(final String key) {
        Qualifier ret = null;
        Iterator<Qualifier> itr = qualifiers.iterator();
        while (itr.hasNext()) {
            Qualifier q = itr.next();
            if (q.getKey().equalsIgnoreCase(key)) {
                ret = q;
                break;
            }
        }
        return ret;
    }

    @Override
    public QualifierSet clone() {
        QualifierSet ret = new QualifierSet();
        ret.setQualifiers(CommonUtil.copyOf(qualifiers));
        return ret;
    }
}
