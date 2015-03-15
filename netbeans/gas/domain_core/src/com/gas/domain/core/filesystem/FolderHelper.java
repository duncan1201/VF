/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.filesystem;

import com.gas.common.ui.core.StringList;
import com.gas.common.ui.util.StrUtil;
import com.gas.domain.core.IFolderElement;
import com.gas.domain.core.as.AnnotatedSeq;
import java.util.*;
import java.util.logging.Logger;

/**
 *
 * @author dq
 */
public class FolderHelper {

    private static final Logger logger = Logger.getLogger(FolderHelper.class.getName());
    private static Folder root = buildDefaultRootFolderTree();

    public static Folder getDefaultRootFolderTree() {
        return root;
    }

    public static void filterByTypes(Folder folder, Folder.TYPE... types) {
        Iterator<Folder> itr = folder.getChildren().iterator();
        while (itr.hasNext()) {
            Folder child = itr.next();
            filterByTypes(child, types);
            if (child.isLeaf() && !child.hasDataOfTypes(types)) {
                itr.remove();
            }
        }
    }
    
    public static Folder getDescendantByHibernateId(Folder folder, final Integer _hId){
        Integer hId = folder.getHibernateId();
        if(hId.equals(_hId)){
            return folder;
        }else{
            Iterator<Folder> itr = folder.getChildren().iterator();
            while(itr.hasNext()){
                Folder child = itr.next();
                Folder ret = getDescendantByHibernateId(child, _hId);
                if(ret != null){
                    return ret;
                }
            }
        }
        return null;
    }

    public static boolean hasDataOfTypes(Folder folder, Folder.TYPE... types) {
        boolean ret = false;
        for (Folder.TYPE type : types) {
            ret = hasDataOfType(folder, type);
            if (ret) {
                break;
            }
        }
        return ret;
    }

    public static boolean hasDataOfType(Folder folder, Folder.TYPE type) {
        boolean ret = false;
        if (type == Folder.TYPE.Nucleotide) {
            Iterator<AnnotatedSeq> itr = folder.getAnnotatedSeqsItr();
            while (itr.hasNext()) {
                AnnotatedSeq as = itr.next();
                boolean isNucleotide = as.isNucleotide();
                if (isNucleotide) {
                    ret = true;
                    break;
                }
            }
        } else if (type == Folder.TYPE.DNA) {

            Iterator<AnnotatedSeq> itr = folder.getAnnotatedSeqsItr();
            while (itr.hasNext()) {
                AnnotatedSeq as = itr.next();
                boolean isDNA = as.isDNA();
                if (isDNA) {
                    ret = true;
                    break;
                }
            }
        } else if (type == Folder.TYPE.RNA) {
            Iterator<AnnotatedSeq> itr = folder.getAnnotatedSeqsItr();
            while (itr.hasNext()) {
                AnnotatedSeq as = itr.next();
                boolean isRNA = as.isRNA();
                if (isRNA) {
                    ret = true;
                    break;
                }
            }
        } else if (type == Folder.TYPE.PROTEIN) {
            Iterator<AnnotatedSeq> itr = folder.getAnnotatedSeqsItr();
            while (itr.hasNext()) {
                AnnotatedSeq as = itr.next();
                boolean isProtein = as.isProtein();
                if (isProtein) {
                    ret = true;
                    break;
                }
            }
        } else if (type == Folder.TYPE.PDB) {
            return !folder.getPdbDocs().isEmpty();
        } else if (type == Folder.TYPE.PubmedArticle) {
            return !folder.getPubmedArticles().isEmpty();
        } else if (type == Folder.TYPE.ENZYMES) {
            return !folder.getRenLists().isEmpty();
        } else if (type == Folder.TYPE.TIGR) {
            return !folder.getTigrProjects().isEmpty();
        }
        return ret;
    }

    public static String getNewElementName(Folder folder, String proposedName) {
        StringList namesExisting = new StringList();
        for (IFolderElement element : folder.getElements()) {
            String name = element.getName();
            namesExisting.add(name);
        }

        return StrUtil.getNewName(proposedName, namesExisting);
    }
    
    public static String getNewChildName(Folder folder, String proposedName){
        StringList namesExisting = new StringList();
        for (Folder element : folder.getChildren()) {
            String name = element.getName();
            namesExisting.add(name);
        }

        return StrUtil.getNewName(proposedName, namesExisting);    
    }

    private static Folder buildDefaultRootFolderTree() {
        root = new Folder(FolderNames.ROOT);

        root.addFolder(getDefaultMyDataFolderTree());
        root.addFolder(getDefaultNCBIFolderTree());

        root.setRoot(true);
        return root;
    }

    public static Folder getNCBIRoot() {
        return getDefaultRootFolderTree().getChild(FolderNames.NCBI_ROOT);
    }
    
    public static Folder getMyDataRoot(){
        return getDefaultRootFolderTree().getChild(FolderNames.MY_DATA);
    }

    private static Folder getDefaultMyDataFolderTree() {
        Folder myDataFolder = new Folder(FolderNames.MY_DATA);
        myDataFolder.setDeletable(false);
        Folder sampleDataFolder = new Folder(FolderNames.SAMPLE_DATA);
        Folder recycleBinFolder = new Folder(FolderNames.RECYCLE_BIN);
        recycleBinFolder.setRecycleBin(true);
        recycleBinFolder.setDeletable(false);

        myDataFolder.addFolder(sampleDataFolder);
        myDataFolder.addFolder(recycleBinFolder);

        sampleDataFolder.addFolder(new Folder(FolderNames.DEFAULT_ALIGNMENTS_FOLDER));       
        sampleDataFolder.addFolder(new Folder(FolderNames.DEFAULT_ENZYMES_FOLDER));
        sampleDataFolder.addFolder(new Folder(FolderNames.DEFAULT_NUCLEOTIDES_FOLDER));
        sampleDataFolder.addFolder(new Folder(FolderNames.DEFAULT_PROTEINS_FOLDER));
        sampleDataFolder.addFolder(new Folder(FolderNames.DEFAULT_STRUCTURES_FOLDER));
        sampleDataFolder.addFolder(new Folder(FolderNames.DEFAULT_ABSTRACTS_FOLDER));
        sampleDataFolder.addFolder(new Folder(FolderNames.DEFAULT_NEB_FOLDER));
        sampleDataFolder.addFolder(new Folder(FolderNames.DEFAULT_SHORTGUN_FOLDER));

        return myDataFolder;
    }

    private static Folder getDefaultNCBIFolderTree() {
        Folder ncbiRootFolder = new Folder(FolderNames.NCBI_ROOT);
        ncbiRootFolder.setNCBIFolder(true);
        ncbiRootFolder.setDeletable(false);
        Iterator<String> itr = FolderNames.NCBI_LEAVES.iterator();
        while (itr.hasNext()) {
            String str = itr.next();
            Folder f = new Folder(str);
            f.setNCBIFolder(true);
            f.setDeletable(false);
            ncbiRootFolder.addFolder(f);
        }
        return ncbiRootFolder;
    }

    /*
     * @return int 1 if one is ancestor of two; -1 if two is ancestor of one; 0
     * if unrelated
     */
    public static int getRelationship(Folder one, Folder two) {
        int ret = 0;
        String p1 = one.getAbsolutePath();
        String p2 = two.getAbsolutePath();
        if (p2.startsWith(p1) && !p1.equals(p2)) {
            ret = 1;
        } else if (p1.startsWith(p2) && !p1.equals(p2)) {
            ret = -1;
        }
        return ret;
    }

    public static FolderList getLeaves(Folder folder) {
        FolderList ret = new FolderList();
        Iterator<Folder> itr = folder.getChildren().iterator();
        if (!itr.hasNext()) {
            ret.add(folder);
        } else {
            while (itr.hasNext()) {
                ret.addAll(getLeaves(itr.next()));
            }
        }
        return ret;
    }
    
    public static boolean isMyDataRoot(Folder folder){
        return folder.getName().equalsIgnoreCase(FolderNames.MY_DATA) && folder.getParent().getName().equalsIgnoreCase(FolderNames.ROOT);
    }

    public static boolean isNCBIRoot(Folder folder) {
        return folder.getName().equalsIgnoreCase(FolderNames.NCBI_ROOT) && folder.getParent().getName().equalsIgnoreCase(FolderNames.ROOT);
    }

    public static boolean isMyDataFolder(Folder folder) {
        boolean ret = false;
        if (folder == null) {
            int a = 1;
        }
        Folder ancestor = folder.getAncestor(2);
        if (ancestor != null) {
            ret = ancestor.getName().equals(FolderNames.MY_DATA);
        }

        return ret;
    }
}
