/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.ren;

import com.gas.common.ui.core.StringList;
import com.gas.common.ui.util.CommonUtil;
import com.gas.domain.core.IFolderElement;
import com.gas.domain.core.filesystem.Folder;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author dunqiang
 */
public class RENList implements Cloneable, IFolderElement {

    private String hibernateId; // for hibernate use only
    private Folder folder; // don't persist
    private String name;
    private String desc;
    private boolean deletable;
    private Date creationDate;
    private Date lastModifiedDate;
    private int size;
    private String prevFolderPath;
    private Set<REN> rens = new HashSet<REN>();
    private boolean read;

    // for hibernate use
    public RENList() {
    }

    @Override
    public boolean isRead() {
        return read;
    }

    @Override
    public void setRead(boolean read) {
        this.read = read;
    }

    public boolean isDeletable() {
        return deletable;
    }

    public void setDeletable(boolean deletable) {
        this.deletable = deletable;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @Override
    public String getHibernateId() {
        return hibernateId;
    }

    @Override
    public RENList clone() {
        RENList ret = CommonUtil.cloneSimple(this);
        ret.setSize(0);
        ret.setFolder(getFolder());
        Iterator<REN> itr = rens.iterator();
        while (itr.hasNext()) {
            REN ren = itr.next();
            ret.addRenIfNecessary(new REN(ren));
        }
        return ret;
    }

    @Override
    public Folder getFolder() {
        return folder;
    }

    @Override
    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setHibernateId(String hibernateId) {
        this.hibernateId = hibernateId;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    @Override
    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public String getName() {
        return name;
    }
    
    public void removeByNames(List<String> names){
        Iterator<REN> itr = rens.iterator();
        while(itr.hasNext()){
            REN ren = itr.next();
            if(names.contains(ren.getName())){
                itr.remove();
            }
        }
    }
    
    public StringList getNames(){
        StringList ret = new StringList();
        Iterator<REN> itr = rens.iterator();
        while(itr.hasNext()){
            REN ren = itr.next();
            ret.add(ren.getName());
        }
        return ret;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    protected Set<REN> getRens() { // for hibernate use only
        return rens;
    }

    public RENSet getRens(Collection<String> names) {
        RENSet ret = new RENSet();
        Iterator<REN> itr = rens.iterator();
        while (itr.hasNext()) {
            REN ren = itr.next();
            if (names.contains(ren.getName())) {
                ret.add(ren);
            }
        }
        return ret;
    }
    
    public boolean isEmpty(){
        return rens.isEmpty();
    }

    public Set<REN> getReadOnlyRens() {
        Set<REN> ret = Collections.unmodifiableSet(rens);
        return ret;
    }

    public void addRenIfNecessary(REN ren) {
        List<String> names = getNames();
        if(!names.contains(ren.getName())){
            rens.add(ren);
            size++;
        }
    }

    protected void setRens(Set<REN> rens) { // for hibernate use only
        this.rens = rens;
    }

    public void setRensAndSize(Set<REN> rens) {
        this.rens = rens;
        size = rens.size();
    }

    public void removeREN(String name) {
        for (REN ren : rens) {
            if (ren.getName().equals(name)) {
                rens.remove(ren);
                size--;
                break;
            }
        }
    }

    public Iterator<REN> getIterator() {
        return getIterator(new RENComparators.NameComparator());
    }

    public Iterator<REN> getIterator(Comparator<REN> c) {
        Set<REN> ret = null;
        if (c == null) {
            ret = new TreeSet();
        } else {
            ret = new TreeSet(c);
        }
        ret.addAll(rens);

        return ret.iterator();
    }

    @Override
    public String toString() {
        return String.format("%s(%d)", name, size);
    }

    @Override
    public String getPrevFolderPath() {
        return prevFolderPath;
    }

    @Override
    public void setPrevFolderPath(String p) {
        this.prevFolderPath = p;
    }

    public static class NameComparator implements Comparator<RENList> {

        @Override
        public int compare(RENList o1, RENList o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }
}
