package com.gas.domain.core.filesystem;

import com.gas.common.ui.core.StringList;
import com.gas.common.ui.core.StringSet;
import com.gas.domain.core.IFolderElement;
import com.gas.domain.core.IFolderElementList;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.msa.MSA;
import com.gas.domain.core.pdb.PDBDoc;
import com.gas.domain.core.pubmed.PubmedArticle;
import com.gas.domain.core.ren.RENList;
import com.gas.domain.core.tigr.Kromatogram;
import com.gas.domain.core.tigr.TigrProject;
import java.util.*;

public class Folder implements Comparable<Folder> {
    public static final char[] forbiddenChars = {'\\', '/'};
    public static final String separator = "\\";
    public enum TYPE {

        Nucleotide, DNA, RNA, PROTEIN, PDB, PubmedArticle, ENZYMES, TIGR, MSA
    }
    private Integer hibernateId; // for hibernate use
    private Boolean root = false;
    private boolean deletable = true;
    private boolean NCBIFolder = false;
    private boolean recycleBin = false;
    private String name;
    private Folder parent;
    private Set<Folder> children = new TreeSet<Folder>();
    private Set<AnnotatedSeq> annotatedSeqs = new HashSet<AnnotatedSeq>();
    private Set<RENList> renLists = new HashSet<RENList>();
    private Set<PubmedArticle> pubmedArticles = new HashSet<PubmedArticle>();
    private Set<PDBDoc> pdbDocs = new HashSet<PDBDoc>();
    private Set<TigrProject> tigrProjects = new HashSet<TigrProject>();
    private Set<Kromatogram> kromatograms = new HashSet<Kromatogram>();
    private Set<MSA> msas = new HashSet<MSA>();

    public Folder() {
    } // for hibernate use

    public Folder(String name) {
        this(null, name);
    }

    public void setNCBIFolder(boolean NCBIFolder) {
        this.NCBIFolder = NCBIFolder;
    }

    public void setRecycleBin(boolean recycleBin) {
        this.recycleBin = recycleBin;
    }
    
    public boolean hasDataOfTypes(Folder.TYPE... types) {
        return FolderHelper.hasDataOfTypes(this, types);
    }

    public boolean hasDataOfType(Folder.TYPE type) {
        return FolderHelper.hasDataOfType(this, type);
    }

    public boolean isLeaf() {
        return getChildCount() == 0;
    }

    public String getNewElementName(String pName) {
        return FolderHelper.getNewElementName(this, pName);
    }
    
    public String getNewChildName(String pName){
        return FolderHelper.getNewChildName(this, pName);
    }

    public StringList getElementNames() {
        return getElementNames(null);
    }

    public StringList getElementNames(Class clazz) {
        StringList ret = new StringList();
        Iterator itr = getElements().iterator();
        while (itr.hasNext()) {
            IFolderElement element = (IFolderElement) itr.next();
            if (clazz != null) {
                if (clazz.isAssignableFrom(element.getClass())) {
                    String name = element.getName();
                    ret.add(name);
                }
            } else {
                String name = element.getName();
                ret.add(name);
            }
        }
        return ret;
    }

    public void touchData() {
        getTigrProjects().iterator();
        getAnnotatedSeqs().iterator();
        getPubmedArticles().iterator();
        getPdbDocs().iterator();
        getRenLists().iterator();
        getMsas().iterator();
        getTigrProjects().iterator();
        getKromatograms().iterator();
    }

    public void touchAll() {
        getChildren().iterator();
        touchData();
    }

    public Set<MSA> getMsas() {
        return msas;
    }

    public void setMsas(Set<MSA> msas) {
        this.msas = msas;
    }

    public Set<TigrProject> getTigrProjects() {
        return tigrProjects;
    }
    
    public Set<Kromatogram> getKromatograms(){
        return kromatograms;
    }

    public void setKromatograms(Set<Kromatogram> kromatograms) {
        this.kromatograms = kromatograms;
    }

    public boolean isDescendentOf(Folder folder) {
        String path2 = folder.getAbsolutePath();
        String path = getAbsolutePath();
        return path.startsWith(path2) && !path.equals(path2);
    }

    public boolean isAncestorOf(Folder folder) {
        String path2 = folder.getAbsolutePath();
        String path = getAbsolutePath();
        return path2.startsWith(path) && !path.equals(path2);
    }

    public <T> T getElement(String name) {
        return getElement(name, null);
    }

    public <T> T getElement(String name, Class<T> clazz) {
        T ret = null;
        Iterator itr = getElements().iterator();
        while (itr.hasNext()) {
            IFolderElement fe = (IFolderElement) itr.next();
            if (fe.getName().equals(name)) {
                if (clazz == null) {
                    ret = (T) fe;
                    break;
                } else if (clazz.isAssignableFrom(fe.getClass())) {
                    ret = (T) fe;
                    break;
                }
            }
        }
        return ret;
    }

    public Iterator<TigrProject> getTigrProjectsItr() {
        return tigrProjects.iterator();
    }

    public void setTigrProjects(Set<TigrProject> tigrProjects) {
        this.tigrProjects = tigrProjects;
    }

    public boolean delete(Folder folder) {
        boolean ret = false;
        for (Folder child : getChildren()) {
            if (child.equals(folder)) {
                child.setParent(null);
                ret = getChildren().remove(child);
                break;
            }
        }
        return ret;
    }

    public void clearContents() {
        getAnnotatedSeqs().clear();
        getPdbDocs().clear();
        getPubmedArticles().clear();
        getTigrProjects().clear();
        getRenLists().clear();
        getMsas().clear();
        getKromatograms().clear();
        getChildren().clear();
    }

    public Folder(Folder parent) {
        this(parent, null);
    }

    public void addObjects(Collection c) {
        for (Object o : c) {
            addObject((IFolderElement)o);
        }
    }

    public void addObject(IFolderElement o) {
        if (o instanceof AnnotatedSeq) {
            AnnotatedSeq as = (AnnotatedSeq) o;
            if (as.getFolder() != null) {
                as.setPrevFolderPath(as.getFolder().getAbsolutePath());
            }
            as.setFolder(this);
            getAnnotatedSeqs().add(as);
        } else if (o instanceof PDBDoc) {
            PDBDoc doc = (PDBDoc) o;
            if (doc.getFolder() != null) {
                doc.setPrevFolderPath(doc.getFolder().getAbsolutePath());
            }
            doc.setFolder(this);
            getPdbDocs().add(doc);
        } else if (o instanceof PubmedArticle) {
            PubmedArticle a = (PubmedArticle) o;
            if (a.getFolder() != null) {
                a.setPrevFolderPath(a.getFolder().getAbsolutePath());
            }
            a.setFolder(this);
            getPubmedArticles().add(a);
        } else if (o instanceof RENList) {
            RENList r = (RENList) o;
            if (r.getFolder() != null) {
                r.setPrevFolderPath(r.getFolder().getAbsolutePath());
            }
            r.setFolder(this);
            getRenLists().add(r);
        } else if (o instanceof MSA) {
            MSA m = (MSA) o;
            if (m.getFolder() != null) {
                m.setPrevFolderPath(m.getFolder().getAbsolutePath());
            }
            m.setFolder(this);
            getMsas().add(m);
        } else if (o instanceof TigrProject) {
            TigrProject p = (TigrProject) o;
            if (p.getFolder() != null) {
                p.setPrevFolderPath(p.getFolder().getAbsolutePath());
            }
            p.setFolder(this);
            getTigrProjects().add(p);
        } else if(o instanceof Kromatogram){
            Kromatogram k = (Kromatogram) o;
            if(k.getFolder() != null){
                k.setPrevFolderPath(k.getFolder().getAbsolutePath());
            }
            k.setFolder(this);
            getKromatograms().add(k);
        } else {
            throw new IllegalArgumentException(String.format("Unknown data type: %s", o.getClass().getName()));
        }
    }
    
    boolean isSame(IFolderElement one, IFolderElement another){
        return one == another || (one.getHibernateId() != null && another.getHibernateId() != null && one.getHibernateId().equals(another.getHibernateId()));
    }

    public void removeObject(IFolderElement fe) {
        if (fe instanceof AnnotatedSeq) {
            AnnotatedSeq as = (AnnotatedSeq) fe;
            Iterator<AnnotatedSeq> itr = annotatedSeqs.iterator();
            while (itr.hasNext()) {
                AnnotatedSeq _as = itr.next();
                if (isSame(as, _as)) {
                    _as.setFolder(null);
                    itr.remove();
                    break;
                }
            }

        } else if (fe instanceof PDBDoc) {
            PDBDoc doc = (PDBDoc) fe;
            Iterator<PDBDoc> itr = pdbDocs.iterator();
            while (itr.hasNext()) {
                PDBDoc _doc = itr.next();
                if (isSame(doc, _doc)) {
                    _doc.setFolder(null);
                    itr.remove();
                    break;
                }
            }

        } else if (fe instanceof PubmedArticle) {
            PubmedArticle a = (PubmedArticle) fe;
            Iterator<PubmedArticle> itr = pubmedArticles.iterator();
            while (itr.hasNext()) {
                PubmedArticle _a = itr.next();
                if (isSame(a, _a)) {
                    _a.setFolder(null);
                    itr.remove();
                    break;
                }
            }

        } else if (fe instanceof RENList) {
            RENList r = (RENList) fe;
            Iterator<RENList> itr = renLists.iterator();
            while (itr.hasNext()) {
                RENList _r = itr.next();
                if (_r.getHibernateId().equals(r.getHibernateId())) {
                    _r.setFolder(null);
                    itr.remove();
                    break;
                }
            }
        } else if (fe instanceof TigrProject) {
            TigrProject p = (TigrProject) fe;
            Iterator<TigrProject> itr = tigrProjects.iterator();
            while (itr.hasNext()) {
                TigrProject _p = itr.next();
                if (isSame(p, _p)) {
                    _p.setFolder(null);
                    itr.remove();
                    break;
                }
            }
        } else if (fe instanceof MSA) {
            MSA msa = (MSA) fe;
            Iterator<MSA> itr = msas.iterator();
            while (itr.hasNext()) {
                MSA _msa = itr.next();
                if (isSame(msa, _msa)) {
                    _msa.setFolder(null);
                    itr.remove();
                    break;
                }
            }
        } else if (fe instanceof Kromatogram) {
            Kromatogram k = (Kromatogram) fe;
            Iterator<Kromatogram> itr = kromatograms.iterator();
            while (itr.hasNext()) {
                Kromatogram _k = itr.next();
                if (isSame(k, _k)) {
                    _k.setFolder(null);
                    itr.remove();
                    break;
                }
            }
        } else {
            throw new IllegalArgumentException(String.format("Unknown data type: %s", fe.getClass().getName()));
        }
    }

    protected Set<PDBDoc> getPdbDocs() {
        return pdbDocs;
    }
    
    public Iterator<PDBDoc> getPdbDocsItr() {
        return pdbDocs.iterator();
    }

    protected void setPdbDocs(Set<PDBDoc> pdbDocs) {
        this.pdbDocs = pdbDocs;
    }

    public boolean getDeletable() {
        return deletable;
    }

    public void setDeletable(boolean deletable) {
        this.deletable = deletable;
    }

    public int getUnreadCount() {
        int count = 0;
        Iterator<PubmedArticle> itr = getPubmedArticles().iterator();
        while (itr.hasNext()) {
            if (!itr.next().isRead()) {
                count++;
            }
        }
        Iterator<AnnotatedSeq> a_itr = getAnnotatedSeqs().iterator();
        while (a_itr.hasNext()) {
            if (!a_itr.next().isRead()) {
                count++;
            }
        }
        Iterator<RENList> r_itr = getRenLists().iterator();
        while (r_itr.hasNext()) {
            if (!r_itr.next().isRead()) {
                count++;
            }
        }
        Iterator<PDBDoc> p_itr = getPdbDocs().iterator();
        while (p_itr.hasNext()) {
            if (!p_itr.next().isRead()) {
                count++;
            }
        }
        Iterator<TigrProject> t_itr = getTigrProjects().iterator();
        while (t_itr.hasNext()) {
            if (!t_itr.next().isRead()) {
                count++;
            }
        }
        
        Iterator<Kromatogram> k_itr = getKromatograms().iterator();
        while (k_itr.hasNext()) {
            if (!k_itr.next().isRead()) {
                count++;
            }
        }
        return count;
    }

    public int getCount() {
        int count = 0;
        count += getPubmedArticles().size();
        count += getAnnotatedSeqs().size();
        count += getRenLists().size();
        count += getPdbDocs().size();
        count += getTigrProjects().size();
        count += getMsas().size();
        count += getKromatograms().size();
        return count;
    }

    public Folder getRootAncestor() {
        Folder ret = this;
        Folder p = ret.getParent();
        while (p != null) {
            ret = p;
            p = ret.getParent();
        }
        return ret;
    }

    /*
     * @param generation 1-based
     */
    public Folder getAncestor(int generation) {

        List<Folder> ancestors = new ArrayList<Folder>();
        ancestors.add(this);
        Folder ret = null;
        Folder _parent = getParent();
        while (_parent != null) {
            ancestors.add(0, _parent);
            _parent = _parent.getParent();
        }
        if (ancestors.size() >= generation) {
            ret = ancestors.get(generation - 1);
        }
        return ret;
    }

    /*
     * @return 1-based
     */
    public int getDepth() {
        int ret = 1;
        Folder _parent = getParent();
        while (_parent != null) {
            ret++;
            _parent = _parent.getParent();
        }
        return ret;
    }

    protected Set<PubmedArticle> getPubmedArticles() {
        return pubmedArticles;
    }

    public Iterator<PubmedArticle> getPubmedArticlesItr() {
        return pubmedArticles.iterator();
    }

    public void setPubmedArticles(Set<PubmedArticle> pubmedArticles) {
        this.pubmedArticles = pubmedArticles;
    }

    protected Set<RENList> getRenLists() {
        return renLists;
    }

    public IFolderElementList getElements() {
        IFolderElementList ret = new IFolderElementList();
        if(!getRenLists().isEmpty()){
            ret.addAll(getRenLists());
        }
        if(!getAnnotatedSeqs().isEmpty()){
            ret.addAll(getAnnotatedSeqs());
        }
        if(!getPubmedArticles().isEmpty()){
            ret.addAll(getPubmedArticles());
        }
        if(!getPdbDocs().isEmpty()){
            ret.addAll(getPdbDocs());
        }
        if(!getTigrProjects().isEmpty()){
            ret.addAll(getTigrProjects());
        }
        if(!getMsas().isEmpty()){
            ret.addAll(getMsas());
        }
        if(!getKromatograms().isEmpty()){
            ret.addAll(getKromatograms());
        }
        return ret;
    }

    public Iterator<RENList> getRenListsItr() {
        return renLists.iterator();
    }

    public void setRenLists(Set<RENList> renLists) {
        this.renLists = renLists;
    }

    protected Set<AnnotatedSeq> getAnnotatedSeqs() {
        return annotatedSeqs;
    }

    public Iterator<AnnotatedSeq> getAnnotatedSeqsItr() {
        return annotatedSeqs.iterator();
    }

    public void setAnnotatedSeqs(Set<AnnotatedSeq> annotatedSeqs) {
        this.annotatedSeqs = annotatedSeqs;
    }

    public boolean hasParent() {
        return parent != null;
    }

    public Folder(Folder parent, String name) {
        this.name = name;
    }

    /*
     * For hibernate use only
     *
     */
    public Integer getHibernateId() {
        return hibernateId;
    }

    /*
     * For hibernate use only
     *
     */
    protected void setHibernateId(Integer hibernateId) {
        this.hibernateId = hibernateId;
    }

    public Boolean getRoot() {
        return root;
    }

    public void setRoot(Boolean root) {
        this.root = root;
    }

    public boolean isNCBIFolder() {
        return NCBIFolder;
    }

    public boolean isRecycleBin(){        
        return recycleBin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Folder getParent() {
        return parent;
    }

    public void setParent(Folder parent) {
        this.parent = parent;
    }

    protected Set<Folder> getChildren() {
        return children;
    }

    public int getChildCount() {
        return children.size();
    }

    public Iterator<Folder> getChildrenItr() {
        return children.iterator();
    }

    public Set<Folder> getReadOnlyChildren() {
        return Collections.unmodifiableSet(children);
    }

    public StringSet getChildrenNames() {
        StringSet ret = new StringSet();
        Iterator<Folder> itr = children.iterator();
        while (itr.hasNext()) {
            Folder folder = itr.next();
            ret.add(folder.getName());
        }
        return ret;
    }
    
    public void addOrReplaceFolder(Folder _folder){
        removeChild(_folder.getName());
        children.add(_folder);
        _folder.setParent(this);
    }

    public void addFolder(Folder folder) {
        children.add(folder);
        folder.setParent(this);
    }

    public void removeChild(final String name) {
        Iterator<Folder> itr = children.iterator();
        while (itr.hasNext()) {
            Folder folder = itr.next();
            if (folder.getName().equals(name)) {
                itr.remove();
                break;
            }
        }
    }

    public boolean hasChild(String name){
        return getChild(name) != null;
    }
    
    public Folder getChild(String name) {
        Folder ret = null;
        Iterator<Folder> itr = children.iterator();
        while (itr.hasNext()) {
            Folder folder = itr.next();
            if (folder.getName().equals(name)) {
                ret = folder;
                break;
            }
        }
        return ret;
    }    

    public void setChildren(Set<Folder> children) {
        this.children = children;
    }

    /*
     * @ret The folder path in the form of "\[name]\[name]\[name]"
     */
    public String getAbsolutePath() {
        StringBuilder ret = new StringBuilder();
        ret.insert(0, getName());
        Folder _parent = getParent();

        while (_parent != null) {
            ret.insert(0, '\\');
            if (!_parent.getRoot()) {
                ret.insert(0, _parent.getName());
            }
            _parent = _parent.getParent();
        }
        return ret.toString();
    }

    public boolean contains(String fullyQualifiedName) {
        Folder f = getFolder(fullyQualifiedName);
        return f != null;
    }

    /**
     * @param absolutePath the format is "\node name\node name2"
     *
     */
    public Folder getFolder(String absolutePath) {
        Folder ret = null;
        if (absolutePath.startsWith(Folder.separator)) {
            absolutePath = new String(absolutePath.substring(1));
        }
        String[] nodeNames = absolutePath.split(Folder.separator + Folder.separator);
        if (nodeNames.length == 1) {
            for (Folder child : getChildren()) {
                if (child.getName().equals(nodeNames[0])) {
                    ret = child;
                    break;
                }
            }
        } else if (nodeNames.length > 1) {
            for (Folder child : getChildren()) {
                if (child.getName().equalsIgnoreCase(nodeNames[0])) {
                    String newPath = new String(absolutePath.substring(absolutePath.indexOf(Folder.separator)));
                    ret = child.getFolder(newPath);
                    if (ret != null) {
                        break;
                    }
                }
            }
        }
        return ret;
    }
    /*
     * @Override public boolean equals(Object o) { if (o == null) { return
     * false; } if (o instanceof Folder) { if (o == this) { return true; }
     * Folder another = (Folder) o; return
     * another.getAbsolutePath().equals(getAbsolutePath()); } else { return
     * false; } }
     *
     * @Override public int hashCode() { return getAbsolutePath().hashCode(); }
     */

    @Override
    public int compareTo(Folder o) {
        return getName().compareTo(o.getName());
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append(name);
        return ret.toString();
    }
}
