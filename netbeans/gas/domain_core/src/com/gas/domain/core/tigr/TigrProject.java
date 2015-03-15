/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.tigr;

import com.gas.common.ui.util.CommonUtil;
import com.gas.domain.core.IFolderElement;
import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.core.tasm.Condig;
import com.gas.domain.core.tasm.Rid;
import com.gas.domain.core.tigr.util.KromatogramHelper;
import java.beans.PropertyChangeSupport;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author dq
 */
public class TigrProject implements Cloneable, Serializable, IFolderElement {

    private static final long serialVersionUID = 1L;
    
    private transient String hibernateId;
    private transient PropertyChangeSupport propertyChangeSupport;
    private String name;
    private String desc;
    private Date creationDate;
    private Date lastModifiedDate;    
    private Set<Condig> condigs = new HashSet<Condig>();
    private Set<Kromatogram> kromatograms = new HashSet<Kromatogram>();
    private TIGRSettings settings;
    private String prevFolderPath;
    private Folder folder;
    private boolean read;

    public TigrProject() {
    }
    
    @Override
    public TigrProject clone(){
        TigrProject ret = CommonUtil.cloneSimple(this);
        ret.setCondigs(CommonUtil.copyOf(condigs));
        ret.setFolder(folder);
        ret.setKromatograms(CommonUtil.copyOf(kromatograms));
        ret.setSettings(settings.clone());
        return ret;
    }

    @Override
    public boolean isRead() {
        return read;
    }

    @Override
    public void setRead(boolean read) {
        this.read = read;
    }

    @Override
    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    @Override
    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
    
    @Override
    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    @Override
    public Folder getFolder() {
        return folder;
    }

    private PropertyChangeSupport getPropertyChangeSupport() {
        if (propertyChangeSupport == null) {
            propertyChangeSupport = new PropertyChangeSupport(this);
        }
        return propertyChangeSupport;
    }

    public void createAlteredKromatograms() {
        if (kromatograms.isEmpty() || condigs.isEmpty()) {
            return;
        }

        Iterator<Condig> cItr = condigs.iterator();
        while (cItr.hasNext()) {
            Condig condig = cItr.next();
            Iterator<Rid> itr = condig.getRids().iterator();
            while (itr.hasNext()) {
                Rid rid = itr.next();
                Kromatogram k = getKromatogramByFileName(rid.getSeqName());
                Kromatogram altered = KromatogramHelper.alterKromatogram(rid, k);
                rid.setKromatogram(altered);
            }
        }
    }

    private Kromatogram getKromatogramByFileName(String fileName) {
        Kromatogram ret = null;
        Iterator<Kromatogram> itr = kromatograms.iterator();
        while (itr.hasNext()) {
            Kromatogram k = itr.next();
            if (k.getFileName().equals(fileName)) {
                ret = k;
                break;
            }
        }
        return ret;
    }

    public void addCondigs(Collection<Condig> condigs) {
        for (Condig condig : condigs) {
            addCondig(condig);
        }
    }

    public void addCondig(Condig condig) {
        condigs.add(condig);
        getPropertyChangeSupport().firePropertyChange("condig", null, condig);
    }
    
    public boolean isCondigPresent(){
        return !condigs.isEmpty();
    }

    protected Set<Condig> getCondigs() {
        return condigs;
    }

    public Set<Condig> getUnmodifiableCondigs() {
        return Collections.unmodifiableSet(condigs);
    }

    public void setCondigs(Set<Condig> condigs) {
        this.condigs = condigs;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getHibernateId() {
        return hibernateId;
    }

    public void setHibernateId(String hibernateId) {
        this.hibernateId = hibernateId;
    }

    /*
     * For hibernate use
     */
    public Set<Kromatogram> getKromatograms() {
        return kromatograms;
    }

    public Iterator<Kromatogram> getKromatogramItr() {
        return kromatograms.iterator();
    }

    public void addKromatograms(Collection<Kromatogram> ks) {
        Iterator<Kromatogram> itr = ks.iterator();
        while (itr.hasNext()) {
            Kromatogram kromatogram = itr.next();
            addKromatogram(kromatogram);
        }
    }

    public void addKromatogram(Kromatogram kromatogram) {
        kromatograms.add(kromatogram);
        getPropertyChangeSupport().firePropertyChange("kromatogram", null, kromatogram);
    }

    public Map<String, Kromatogram> getKromatogramsMap() {
        Map<String, Kromatogram> ret = new HashMap<String, Kromatogram>();
        Iterator<Kromatogram> itr = kromatograms.iterator();
        while (itr.hasNext()) {
            Kromatogram k = itr.next();
            ret.put(k.getFileName(), k);
        }
        return ret;
    }

    /*
     * For hibernate use
     */
    protected void setKromatograms(Set<Kromatogram> kromatograms) {
        this.kromatograms = kromatograms;
    }

    public TIGRSettings getSettings() {
        return settings;
    }

    public void setSettings(TIGRSettings settings) {
        this.settings = settings;
    }

    public ByteArrayOutputStream to() {
        ByteArrayOutputStream ret = new ByteArrayOutputStream();

        return ret;
    }

    @Override
    public String getPrevFolderPath() {
        return prevFolderPath;
    }

    @Override
    public void setPrevFolderPath(String p) {
        this.prevFolderPath = p;
    }
}
