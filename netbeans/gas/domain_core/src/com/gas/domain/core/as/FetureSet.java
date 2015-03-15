/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.as;

import com.gas.common.ui.core.LocList;
import com.gas.common.ui.core.StringList;
import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.util.CommonUtil;
import com.gas.common.ui.util.LocUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 *
 * @author dq
 */
public class FetureSet implements Cloneable {

    private Integer hibernateId;
    private Set<Feture> fetures = new HashSet<Feture>();

    /**
     *
     */
    public void touchAll() {
        Iterator<Feture> itr = iterator();
        while (itr.hasNext()) {
            Feture ftr = itr.next();
            ftr.getQualifierSet().touchAll();
        }
    }

    public boolean isEmpty() {
        return fetures.isEmpty();
    }

    public int size() {
        return fetures.size();
    }

    /**
     *
     * @param feture
     */
    public void add(Feture feture) {
        fetures.add(feture);
    }

    /**
     *
     * @param all
     */
    public void addAll(Collection<Feture> all) {
        fetures.addAll(all);
    }

    /**
     *
     */
    public void clear() {
        fetures.clear();
    }

    public void circularize(int totalLength) {
        Iterator<Feture> itr = fetures.iterator();
        while (itr.hasNext()) {
            Feture feture = itr.next();
            feture.getLucation().circularize(totalLength);
        }
    }

    /**
     *
     * @param feture
     */
    public void remove(Feture feture) {
        fetures.remove(feture);
    }

    public void remove(FetureSet fetureSet) {
        fetures.removeAll(fetureSet.getFetures());
    }

    /**
     *
     * @return
     */
    public Integer getHibernateId() {
        return hibernateId;
    }

    /**
     *
     * @param hibernateId
     */
    public void setHibernateId(Integer hibernateId) {
        this.hibernateId = hibernateId;
    }

    /**
     *
     * @return
     */
    public Set<Feture> getFetures() {
        return fetures;
    }

    /**
     *
     * @param fetures
     */
    public void setFetures(Set<Feture> fetures) {
        this.fetures = fetures;
    }

    /**
     *
     * @param startPos
     * @param endPos
     */
    public void removeByLoc(int startPos, int endPos) {
        Iterator<Feture> itr = iterator();
        while (itr.hasNext()) {
            Feture feture = itr.next();
            Lucation lucation = feture.getLucation();
            Loc loc = lucation.toLoc();
            if (loc.isOverlapped(startPos, endPos)) {
                itr.remove();
            }
        }
    }

    /**
     *
     * @param keys
     */
    public void removeByFetureKeys(String... keys) {
        StringList strList = new StringList(keys);
        Iterator<Feture> itr = iterator();
        while (itr.hasNext()) {
            final Feture feture = itr.next();
            final String key = feture.getKey();
            if (strList.containsIgnoreCase(key)) {
                itr.remove();
            }
        }
    }

    public void removeByQualifierKeys(String... qualifierKeys) {
        FetureSet toBeRemoved = getByQualifierKeys(qualifierKeys);
        fetures.removeAll(toBeRemoved.getFetures());
    }
    
    public void removeQualifierKeys(String... qualifierKeys){
        Iterator<Feture> itr = iterator();
        while(itr.hasNext()){
            Feture feture = itr.next();
            feture.getQualifierSet().remove(qualifierKeys);
        }
    }

    public FetureSet getByQualifierKeys(String... qualifierKeys) {
        final StringList keys = new StringList(qualifierKeys);
        FetureSet ret = new FetureSet();
        Iterator<Feture> itr = iterator();
        while (itr.hasNext()) {
            Feture feture = itr.next();
            for (String k : keys) {
                boolean contains = feture.getQualifierSet().containsKey(k);
                if (contains) {
                    ret.add(feture);
                    break;
                }
            }
        }
        return ret;
    }

    public FetureSet getByNotes(String... notes) {
        FetureSet ret = new FetureSet();
        StringList strList = new StringList(notes);
        Iterator<Feture> itr = iterator();
        while (itr.hasNext()) {
            final Feture feture = itr.next();
            Qualifier qualifier = feture.getQualifierSet().getQualifier("note");
            if (qualifier != null) {
                if (strList.containsIgnoreCase(qualifier.getValue())) {
                    ret.add(feture);
                }
            }
        }
        return ret;
    }

    public FetureSet getByLabels(String... labels) {
        FetureSet ret = new FetureSet();
        StringList strList = new StringList(labels);
        Iterator<Feture> itr = iterator();
        while (itr.hasNext()) {
            final Feture feture = itr.next();
            Qualifier qualifier = feture.getQualifierSet().getQualifier("label");
            if (qualifier != null) {
                if (strList.containsIgnoreCase(qualifier.getValue())) {
                    ret.add(feture);
                }
            }
        }
        return ret;
    }

    public FetureSet toTriplet() {
        FetureSet ret = new FetureSet();
        Iterator<Feture> itr = iterator();
        while (itr.hasNext()) {
            Feture feture = itr.next().clone();
            Lucation lucation = feture.getLucation();
            lucation.toTriplet();
            ret.add(feture);
        }
        return ret;
    }

    public FetureSet getByFetureKeys(String... keys) {
        FetureSet ret = new FetureSet();
        StringList strList = new StringList(keys);
        Iterator<Feture> itr = iterator();
        while (itr.hasNext()) {
            final Feture feture = itr.next();
            final String key = feture.getKey();
            if (strList.containsIgnoreCase(key)) {
                ret.add(feture);
            }
        }
        return ret;
    }

    /**
     *
     * @param keys
     */
    public void removeByFetureKeysStartingWith(String... keys) {
        StringList strList = new StringList(keys);
        strList.toUpperCase();
        Iterator<Feture> itr = iterator();
        while (itr.hasNext()) {
            final Feture feture = itr.next();
            final String key = feture.getKey().toUpperCase(Locale.ENGLISH);
            for (String str : strList) {
                if (key.startsWith(str)) {
                    itr.remove();
                    break;
                }
            }
        }
    }

    /**
     * @param start2
     * @param end2
     * @param totalLength
     * @param circular
     */
    public void subSeq(final int start2, final int end2, boolean circular, final int totalLength) {
        List<Feture> toBeAdded = new ArrayList<Feture>();
        // fetures
        Iterator<Feture> itr = iterator();
        while (itr.hasNext()) {
            Feture feture = itr.next();
            Lucation luc = feture.getLucation();
            int start = luc.getStart();
            int end = luc.getEnd();
            LocList locList = LocUtil.intersect(start, end, start2, end2);
            if (locList.size() == 2) {
                Feture another = feture.clone();

                Loc loc1 = locList.get(0);
                feture.getLucation().sub(loc1.getStart(), loc1.getEnd(), circular, totalLength);

                Loc loc2 = locList.get(1);
                another.getLucation().sub(loc2.getStart(), loc2.getEnd(), circular, totalLength);

                /**
                 * cannot add while iterating, otherwise
                 * ConcurrentModificationException
                 */
                toBeAdded.add(another);
            } else if (locList.size() == 1) {
                feture.getLucation().sub(start2, end2, circular, totalLength);
                if (feture.getLucation().isEmpty()) {
                    itr.remove();
                }
            } else if (locList.size() == 0) {
                itr.remove();
            }
        }

        addAll(toBeAdded);
    }

    /**
     *
     * @param keys
     * @return
     */
    public List<Feture> getByFeturesByKeys(final String... keys) {
        List<Feture> ret = new ArrayList<Feture>();
        if (keys == null) {
            return ret;
        }
        StringList strList = new StringList(keys);
        Iterator<Feture> itr = iterator();
        while (itr.hasNext()) {
            Feture feture = itr.next();
            if (strList.containsIgnoreCase(feture.getKey())) {
                ret.add(feture);
            }
        }
        return ret;
    }

    /**
     *
     * @param pos
     * @param insertLength
     * @param circular
     * @param originalLength
     */
    public void insertSeq(int pos, int insertLength, boolean circular, int originalLength) {
        Iterator<Feture> itr = iterator();
        while (itr.hasNext()) {
            Feture feture = itr.next();
            Lucation luc = feture.getLucation();
            luc.insert(pos, insertLength, circular, originalLength);
        }
    }

    /**
     *
     * @param start
     * @param end
     * @param totalLength the total length before deletion
     */
    public void removeSeq(final int start, final int end, final int totalLength) {
        Iterator<Feture> itr = iterator();
        while (itr.hasNext()) {
            Feture feture = itr.next();
            Lucation lucation = feture.getLucation();
            lucation.deleteSeq(start, end, totalLength);
            if (lucation.isEmpty()) {
                itr.remove();
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        Iterator<Feture> itr = iterator();
        while (itr.hasNext()) {
            Feture feture = itr.next();
            Lucation luc = feture.getLucation();
            ret.append(feture.getKey());
            ret.append("\t: ");
            ret.append(luc);
            ret.append('\n');
        }
        return ret.toString();
    }

    /**
     *
     * @param totalLength
     */
    public void flip(int totalLength) {
        Iterator<Feture> itr = iterator();
        while (itr.hasNext()) {
            Feture feture = itr.next();
            Lucation luc = feture.getLucation();
            Lucation flipLuc = luc.clone();
            flipLuc.flip(totalLength);
            feture.setLucation(flipLuc);
        }
    }

    /**
     *
     * @param newStart
     * @param totalLength
     */
    public void linearize(int newStart, int totalLength) {
        List<Feture> toBeAdded = new ArrayList<Feture>();
        Iterator<Feture> itr = iterator();
        while (itr.hasNext()) {
            Feture feture = itr.next();
            Lucation luc = feture.getLucation();
            final Loc loc = luc.toLoc();
            if (loc.isSubset(newStart, totalLength)) {
                luc.translate(1 - newStart, false, totalLength);
            } else if (loc.isSubset(1, newStart - 1)) {
                luc.translate(totalLength - newStart + 1, false, totalLength);
            } else {
                if (luc.isContiguous()) {
                    Feture one = feture.clone();
                    one.getLucation();
                    one.getLucation().setContiguousMin(totalLength - (newStart - loc.getStart() + 1));
                    one.getLucation().setContiguousMax(totalLength);
                    one.getLucation().setContiguousFuzzyEnd(true);
                    toBeAdded.add(one);

                    Feture other = feture.clone();
                    other.getLucation().setContiguousFuzzyStart(true);
                    other.getLucation().setContiguousMin(1);
                    other.getLucation().setContiguousMax(loc.getEnd() - newStart + 1);
                    other.getLucation();
                    toBeAdded.add(one);
                } else {
                    itr.remove();
                }
            }
        }
        addAll(toBeAdded);
    }

    private Iterator<Feture> iterator() {
        return fetures.iterator();
    }

    /**
     *
     * @param amount
     */
    public void translate(int amount, boolean circular, Integer totalLength) {
        translate(amount, 0, circular, totalLength);
    }

    /**
     *
     * @param amount
     * @param startPos
     */
    public void translate(int amount, int startPos, boolean circular, Integer totalLength) {
        Iterator<Feture> itr = iterator();
        while (itr.hasNext()) {
            Feture feture = itr.next();
            Lucation lucation = feture.getLucation();
            Loc loc = lucation.toLoc();
            if (loc.getStart() >= startPos) {
                lucation.translate(amount, circular, totalLength);
            }
        }
    }

    @Override
    public FetureSet clone() {
        FetureSet ret = new FetureSet();
        ret.setFetures(CommonUtil.copyOf(fetures));
        return ret;
    }
}
