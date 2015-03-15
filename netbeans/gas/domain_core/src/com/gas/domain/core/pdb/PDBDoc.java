/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.pdb;

import com.gas.common.ui.util.CommonUtil;
import com.gas.domain.core.IFolderElement;
import com.gas.domain.core.filesystem.Folder;
import java.beans.PropertyChangeSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dq
 */
public class PDBDoc implements Cloneable, IFolderElement {

    private String hibernateId;
    private Boolean modified = false;
    private Folder folder; // don't persist
    private String PrevFolderPath;
    private HEADER header;
    private OBSLTE obslte;
    private TITLE title;
    private COMPND compnd;
    private SOURCE source;
    private KEYWDS keywds;
    private EXPDTA expdta;
    private NUMMDL nummdl;
    private JRNL jrnl;
    private DBREF dbref;
    private SEQRES seqRes;
    private MODRES modres;
    private HET het;
    private HETNAM hetnam;
    private FORMUL formul;
    private HELIX helix;
    private SHEET sheet;
    private SSBOND ssbond;
    private SITE site;
    private CRYST1 cryst1;
    private ORIGX1 origx1;
    private ORIGX2 origx2;
    private ORIGX3 origx3;
    private SCALE1 scale1;
    private SCALE2 scale2;
    private SCALE3 scale3;
    private MTRIX1 mtrix1;
    private MTRIX2 mtrix2;
    private MTRIX3 mtrix3;
    private Set<MODEL> models = new HashSet<MODEL>();
    private CONECT conect;
    private END end = new END();
    private boolean read;
    private Date lastModifiedDate;
    private transient String name;
    private transient PropertyChangeSupport propertyChangeSupport;

    public PDBDoc() {
        lastModifiedDate = new Date();
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @Override
    public boolean isRead() {
        return read;
    }

    @Override
    public void setRead(boolean read) {
        this.read = read;
    }

    private PropertyChangeSupport getPropertyChangeSupport() {
        if (propertyChangeSupport == null) {
            propertyChangeSupport = new PropertyChangeSupport(this);
        }
        return propertyChangeSupport;
    }
    
    @Override
    public void setDesc(String desc) {
    }

    @Override
    public String getDesc() {
        return null;
    }    

    @Override
    public PDBDoc clone() {
        PDBDoc ret = new PDBDoc();
        if (compnd != null) {
            ret.setCompnd(compnd.clone());
        }
        if (conect != null) {
            ret.setConect(conect.clone());
        }
        if (cryst1 != null) {
            ret.setCryst1(cryst1.clone());
        }
        if (dbref != null) {
            ret.setDbref(dbref.clone());
        }
        if (expdta != null) {
            ret.setExpdta(expdta.clone());
        }
        ret.setFolder(folder);
        if (formul != null) {
            ret.setFormul(formul.clone());
        }
        if (header != null) {
            ret.setHeader(header.clone());
        }
        if (helix != null) {
            ret.setHelix(helix.clone());
        }
        if (het != null) {
            ret.setHet(het.clone());
        }
        if (hetnam != null) {
            ret.setHetnam(hetnam.clone());
        }
        if (jrnl != null) {
            ret.setJrnl(jrnl.clone());
        }
        if (keywds != null) {
            ret.setKeywds(keywds.clone());
        }
        if (models.size() > 0) {
            ret.setModels(CommonUtil.copyOf(models));
        }
        ret.setModified(modified);
        if (modres != null) {
            ret.setModres(modres.clone());
        }
        if (mtrix1 != null) {
            ret.setMtrix1(mtrix1.clone());
        }
        if (mtrix2 != null) {
            ret.setMtrix2(mtrix2.clone());
        }
        if (mtrix3 != null) {
            ret.setMtrix3(mtrix3.clone());
        }
        if (nummdl != null) {
            ret.setNummdl(nummdl.clone());
        }
        if (obslte != null) {
            ret.setObslte(obslte.clone());
        }
        if (origx1 != null) {
            ret.setOrigx1(origx1.clone());
        }
        if (origx2 != null) {
            ret.setOrigx2(origx2.clone());
        }
        if (origx3 != null) {
            ret.setOrigx3(origx3.clone());
        }
        if (scale1 != null) {
            ret.setScale1(scale1.clone());
        }
        if (scale2 != null) {
            ret.setScale2(scale2.clone());
        }
        if (scale3 != null) {
            ret.setScale3(scale3.clone());
        }
        if (seqRes != null) {
            ret.setSeqRes(seqRes.clone());
        }
        if (sheet != null) {
            ret.setSheet(sheet.clone());
        }
        if (site != null) {
            ret.setSite(site.clone());
        }
        if (source != null) {
            ret.setSource(source.clone());
        }
        if (ssbond != null) {
            ret.setSsbond(ssbond.clone());
        }
        if (title != null) {
            ret.setTitle(title.clone());
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

    @Override
    public String getHibernateId() {
        return hibernateId;
    }

    public void setHibernateId(String hibernateId) {
        this.hibernateId = hibernateId;
    }

    public Boolean getModified() {
        return modified;
    }

    public void setModified(Boolean modified) {
        this.modified = modified;
    }

    public DBREF getDbref() {
        return dbref;
    }

    public void setDbref(DBREF dbref) {
        this.dbref = dbref;
    }

    public String getClassification() {
        return header.getClassification();
    }

    /*
     * @return date in "dd-MM-yy" format, (29-JUL-90)
     */
    public <T> T getDepDate(Class<T> retType) {
        T ret = null;
        String str = header.getDepDate();
        if (retType.isAssignableFrom(String.class)) {
            ret = (T) str;
        } else if (retType.isAssignableFrom(Date.class)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);
            Date retDate = null;
            try {
                retDate = dateFormat.parse(str);
            } catch (ParseException ex) {
                Logger.getLogger(PDBDoc.class.getName()).log(Level.SEVERE, null, ex);
            }
            ret = (T) retDate;
        } else {
            throw new IllegalArgumentException(String.format("Class '%s' not supported", retType.toString()));
        }

        return ret;
    }

    public JRNL getJrnl() {
        return jrnl;
    }

    public void setJrnl(JRNL jrnl) {
        this.jrnl = jrnl;
    }

    public void touchAll() {
        if (compnd != null) {
            compnd.getElements();
        }
        if (conect != null) {
            conect.getElements();
        }
        if (cryst1 != null) {
            cryst1.getA();
        }
        if (expdta != null) {
            expdta.getElements();
        }
        if (formul != null) {
            formul.getElements();
        }
        if (helix != null) {
            helix.getElements();
        }
        if (het != null) {
            het.getElements();
        }
        if (hetnam != null) {
            hetnam.getElements();
        }
        if (keywds != null) {
            keywds.getElements();
        }
        if (jrnl != null) {
            jrnl.getElements();
        }
        if (models != null) {
            Iterator<MODEL> itr = models.iterator();
            while (itr.hasNext()) {
                MODEL m = itr.next();
                m.getAtom().getElements();
                m.getHetatm().getElements();
                m.getTer().getElements();
            }
        }
        if (modres != null) {
            modres.getElements();
        }
        if (mtrix1 != null) {
            mtrix1.getM1();
        }
        if (mtrix2 != null) {
            mtrix2.getM1();
        }
        if (mtrix3 != null) {
            mtrix3.getM1();
        }
        if (nummdl != null) {
            nummdl.getModelNumber();
        }
        if (obslte != null) {
            obslte.getElements();
        }
        if (origx1 != null) {
            origx1.getO1();
        }
        if (origx2 != null) {
            origx2.getO1();
        }
        if (origx3 != null) {
            origx3.getO1();
        }
        if (scale1 != null) {
            scale1.getS1();
        }
        if (scale2 != null) {
            scale2.getS1();
        }
        if (scale3 != null) {
            scale3.getS1();
        }
        if (seqRes != null) {
            seqRes.getElements();
        }
        if (sheet != null) {
            sheet.getElements();
        }
        if (site != null) {
            site.getElements();
        }
        if (source != null) {
            source.getElements();
        }
        if (ssbond != null) {
            ssbond.getElements();
        }
    }

    @Override
    public String getName() {
        if (name == null) {
            name = title.getTitle();
        }
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
        title.setTitle(name);
    }

    public String getDoi() {
        return jrnl.getDoi();
    }

    public String getPdbId() {
        return header.getIdCode();
    }

    public SSBOND getSsbond() {
        return ssbond;
    }

    public void setSsbond(SSBOND ssbond) {
        this.ssbond = ssbond;
    }

    public MTRIX1 getMtrix1() {
        return mtrix1;
    }

    public void setMtrix1(MTRIX1 mtrix1) {
        this.mtrix1 = mtrix1;
    }

    public MTRIX2 getMtrix2() {
        return mtrix2;
    }

    public void setMtrix2(MTRIX2 mtrix2) {
        this.mtrix2 = mtrix2;
    }

    public MTRIX3 getMtrix3() {
        return mtrix3;
    }

    public void setMtrix3(MTRIX3 mtrix3) {
        this.mtrix3 = mtrix3;
    }

    public ORIGX1 getOrigx1() {
        return origx1;
    }

    public void setOrigx1(ORIGX1 origx1) {
        this.origx1 = origx1;
    }

    public ORIGX2 getOrigx2() {
        return origx2;
    }

    public void setOrigx2(ORIGX2 origx2) {
        this.origx2 = origx2;
    }

    public ORIGX3 getOrigx3() {
        return origx3;
    }

    public void setOrigx3(ORIGX3 origx3) {
        this.origx3 = origx3;
    }

    public SITE getSite() {
        return site;
    }

    public void setSite(SITE site) {
        this.site = site;
    }

    public HET getHet() {
        return het;
    }

    public void setHet(HET het) {
        this.het = het;
    }

    public MODRES getModres() {
        return modres;
    }

    public void setModres(MODRES modres) {
        this.modres = modres;
    }

    public SEQRES getSeqRes() {
        return seqRes;
    }

    public void setSeqRes(SEQRES seqRes) {
        this.seqRes = seqRes;
    }

    public OBSLTE getObslte() {
        return obslte;
    }

    public void setObslte(OBSLTE obslte) {
        this.obslte = obslte;
    }

    public Set<MODEL> getModels() {
        return models;
    }

    public void setModels(Set<MODEL> models) {
        this.models = models;
    }

    public NUMMDL getNummdl() {
        return nummdl;
    }

    public void setNummdl(NUMMDL nummdl) {
        this.nummdl = nummdl;
    }

    public FORMUL getFormul() {
        return formul;
    }

    public void setFormul(FORMUL formul) {
        this.formul = formul;
    }

    public EXPDTA getExpdta() {
        return expdta;
    }

    public void setExpdta(EXPDTA expdta) {
        this.expdta = expdta;
    }

    public SCALE1 getScale1() {
        return scale1;
    }

    public void setScale1(SCALE1 scale1) {
        this.scale1 = scale1;
    }

    public SCALE2 getScale2() {
        return scale2;
    }

    public void setScale2(SCALE2 scale2) {
        this.scale2 = scale2;
    }

    public SCALE3 getScale3() {
        return scale3;
    }

    public void setScale3(SCALE3 scale3) {
        this.scale3 = scale3;
    }

    public CRYST1 getCryst1() {
        return cryst1;
    }

    public void setCryst1(CRYST1 cryst1) {
        this.cryst1 = cryst1;
    }

    public SHEET getSheet() {
        return sheet;
    }

    public void setSheet(SHEET sheet) {
        this.sheet = sheet;
    }

    public HELIX getHelix() {
        return helix;
    }

    public void setHelix(HELIX helix) {
        this.helix = helix;
    }

    public CONECT getConect() {
        return conect;
    }

    public void setConect(CONECT conect) {
        this.conect = conect;
    }

    public HETNAM getHetnam() {
        return hetnam;
    }

    public void setHetnam(HETNAM hetnam) {
        this.hetnam = hetnam;
    }

    public COMPND getCompnd() {
        return compnd;
    }

    public void setCompnd(COMPND compnd) {
        this.compnd = compnd;
    }

    public HEADER getHeader() {
        return header;
    }

    public void setHeader(HEADER header) {
        this.header = header;
    }

    public KEYWDS getKeywds() {
        return keywds;
    }

    public void setKeywds(KEYWDS keywds) {
        this.keywds = keywds;
    }

    public SOURCE getSource() {
        return source;
    }

    public void setSource(SOURCE source) {
        this.source = source;
    }

    public TITLE getTitle() {
        return title;
    }

    public void setTitle(TITLE title) {
        this.title = title;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();

        String headerStr = header.toString();
        ret.append(headerStr);

        if (obslte != null) {
            String str = obslte.toString();
            ret.append(str);
        }

        if (title != null) {
            String str = title.toString();
            ret.append(str);
        }

        if (compnd != null) {
            String str = compnd.toString();
            ret.append(str);
        }

        if (source != null) {
            String str = source.toString();
            ret.append(str);
        }

        if (keywds != null) {
            String str = keywds.toString();
            ret.append(str);
        }

        if (expdta != null) {
            String str = expdta.toString();
            ret.append(str);
        }

        if (nummdl != null) {
            String str = nummdl.toString();
            ret.append(str);
        }

        if (jrnl != null) {
            String str = jrnl.toString();
            ret.append(str);
        }

        if (dbref != null) {
            String str = dbref.toString();
            ret.append(str);
        }

        if (seqRes != null) {
            String str = seqRes.toString();
            ret.append(str);
        }

        if (modres != null) {
            String str = modres.toString();
            ret.append(str);
        }

        if (het != null) {
            String str = het.toString();
            ret.append(str);
        }

        if (hetnam != null) {
            String str = hetnam.toString();
            ret.append(str);
        }

        if (formul != null) {
            String str = formul.toString();
            ret.append(str);
        }

        if (helix != null) {
            String str = helix.toString();
            ret.append(str);
        }

        if (sheet != null) {
            String str = sheet.toString();
            ret.append(str);
        }

        if (ssbond != null) {
            String str = ssbond.toString();
            ret.append(str);
        }

        if (site != null) {
            String str = site.toString();
            ret.append(str);
        }

        if (cryst1 != null) {
            String str = cryst1.toString();
            ret.append(str);
        }

        if (origx1 != null) {
            String str = origx1.toString();
            ret.append(str);
        }

        if (origx2 != null) {
            String str = origx2.toString();
            ret.append(str);
        }

        if (origx3 != null) {
            String str = origx3.toString();
            ret.append(str);
        }

        if (scale1 != null) {
            String str = scale1.toString();
            ret.append(str);
        }

        if (scale2 != null) {
            String str = scale2.toString();
            ret.append(str);
        }

        if (scale3 != null) {
            String str = scale3.toString();
            ret.append(str);
        }

        if (mtrix1 != null) {
            String str = mtrix1.toString();
            ret.append(str);
        }

        if (mtrix2 != null) {
            String str = mtrix2.toString();
            ret.append(str);
        }

        if (mtrix3 != null) {
            String str = mtrix3.toString();
            ret.append(str);
        }

        boolean footerHeader = nummdl != null && nummdl.getModelNumber() > 0;
        Iterator<MODEL> itr = models.iterator();
        while (itr.hasNext()) {
            MODEL model = itr.next();
            String str = model.toString(footerHeader);
            ret.append(str);
        }

        if (conect != null) {
            String str = conect.toString();
            ret.append(str);
        }

        if (end != null) {
            String str = end.toString();
            ret.append(str);
        }

        return ret.toString();
    }

    @Override
    public String getPrevFolderPath() {
        return this.PrevFolderPath;
    }

    @Override
    public void setPrevFolderPath(String p) {
        this.PrevFolderPath = p;
    }
}
