/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.util;

import com.gas.common.ui.core.StringList;
import com.gas.common.ui.misc.CNST;
import com.gas.common.ui.util.ReflectHelper;
import com.gas.common.ui.util.StrUtil;
import com.gas.database.core.as.service.api.IAnnotatedSeqService;
import com.gas.database.core.filesystem.service.api.IFolderService;
import com.gas.database.core.msa.service.api.IMSAService;
import com.gas.database.core.pdb.api.IPDBService;
import com.gas.database.core.pubmed.api.IPubmedDBService;
import com.gas.domain.core.ren.IRENListService;
import com.gas.database.core.tigr.service.api.ITigrPtService;
import com.gas.domain.core.IFolderElement;
import com.gas.domain.core.IFolderElementList;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.core.msa.MSA;
import com.gas.domain.core.msa.MSAList;
import com.gas.domain.core.pdb.PDBDoc;
import com.gas.domain.core.pubmed.PubmedArticle;
import com.gas.domain.core.ren.RENList;
import com.gas.domain.core.tigr.TigrProject;
import com.gas.domain.ui.banner.BannerTC;
import com.gas.domain.ui.editor.as.CloseASEditorAction;
import com.gas.domain.ui.editor.msa.CloseMSAEditorAction;
import com.gas.domain.ui.editor.pdb.ClosePDBEditorAction;
import com.gas.domain.ui.editor.pubmed.ClosePubmedEditorAction;
import com.gas.domain.ui.editor.renlist.CloseRENListEditorAction;
import com.gas.domain.ui.explorer.ExplorerTC;
import com.gas.database.core.api.IDomainUtil;
import com.gas.database.core.tigr.service.api.IKromatogramService;
import com.gas.domain.core.tigr.Kromatogram;
import com.gas.domain.ui.editor.as.OpenASEditorAction;
import com.gas.domain.ui.editor.kromatogram.CloseKromatogramEditorAction;
import com.gas.domain.ui.editor.kromatogram.OpenKromatogramEditorAction;
import com.gas.domain.ui.editor.msa.OpenMSAAction;
import com.gas.domain.ui.editor.pdb.OpenPDBEditorAction;
import com.gas.domain.ui.editor.pubmed.OpenPubmedEditorAction;
import com.gas.domain.ui.editor.renlist.OpenRENListEditorAction;
import com.gas.domain.ui.editor.tigr.CloseTigrPtEditorAction;
import com.gas.domain.ui.editor.tigr.OpenTigrPtEditorAction;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IDomainUtil.class)
public class DomainUtil implements IDomainUtil {

    private IAnnotatedSeqService asService = Lookup.getDefault().lookup(IAnnotatedSeqService.class);
    private IFolderService folderService = Lookup.getDefault().lookup(IFolderService.class);

    @Override
    public void udpateCurrentFolders() {
        Folder explorerFolder = ExplorerTC.getInstance().getSelectedFolder();
        Folder updatedFolder = folderService.loadWithRecursiveDataAndParentAndChildren(explorerFolder.getHibernateId());

        ExplorerTC.getInstance().updateFolder(updatedFolder);
        BannerTC.getInstance().updateFolder(updatedFolder);
    }

    @Override
    public void openEditor(Object obj) {
        if (obj instanceof AnnotatedSeq) {
            AnnotatedSeq as = (AnnotatedSeq) obj;
            AnnotatedSeq seq = null;
            if (as.getHibernateId() != null) {
                IAnnotatedSeqService asService = Lookup.getDefault().lookup(IAnnotatedSeqService.class);
                final Folder rootFolder = ExplorerTC.getInstance().getRootFolder();
                seq = asService.getFullByHibernateId(as.getHibernateId(), rootFolder);
            } else {
                seq = as;
            }
            OpenASEditorAction action = new OpenASEditorAction(seq);
            action.actionPerformed(null);
        } else if (obj instanceof PubmedArticle) {
            PubmedArticle pa = (PubmedArticle) obj;
            OpenPubmedEditorAction action;
            if (pa.getHibernateId() != null) {
                IPubmedDBService pubmedService = Lookup.getDefault().lookup(IPubmedDBService.class);
                PubmedArticle full = pubmedService.getFullByHibernateId(pa.getHibernateId());
                action = new OpenPubmedEditorAction(full);
            } else {
                action = new OpenPubmedEditorAction(pa);
            }
            action.actionPerformed(null);
        } else if (obj instanceof PDBDoc) {
            PDBDoc pdbDoc = (PDBDoc) obj;
            OpenPDBEditorAction action;
            if (pdbDoc.getHibernateId() != null) {
                IPDBService pdbService = Lookup.getDefault().lookup(IPDBService.class);
                PDBDoc full = pdbService.getFullByHibernateId(pdbDoc.getHibernateId());
                action = new OpenPDBEditorAction(full);
            } else {
                action = new OpenPDBEditorAction(pdbDoc);
            }
            action.actionPerformed(null);
        } else if (obj instanceof RENList) {
            RENList renList = (RENList) obj;
            IRENListService r = Lookup.getDefault().lookup(IRENListService.class);
            RENList full = r.getFullRENListByHibernateId(renList.getHibernateId());

            OpenRENListEditorAction action = new OpenRENListEditorAction(full);
            action.actionPerformed(null);
        } else if (obj instanceof TigrProject) {
            TigrProject tigrPt = (TigrProject) obj;
            ITigrPtService tService = Lookup.getDefault().lookup(ITigrPtService.class);
            TigrProject full = tService.getFullByHibernateId(tigrPt.getHibernateId());
            OpenTigrPtEditorAction action = new OpenTigrPtEditorAction(full);
            action.actionPerformed(null);
        } else if (obj instanceof MSA) {
            MSA msa = (MSA) obj;
            IMSAService msaService = Lookup.getDefault().lookup(IMSAService.class);
            msa = msaService.getByHibernateIds(msa.getHibernateId()).get(0);
            OpenMSAAction action = new OpenMSAAction(msa);
            action.actionPerformed(null);
        } else if (obj instanceof Kromatogram) {
            Kromatogram kromatogram = (Kromatogram) obj;
            OpenKromatogramEditorAction action = new OpenKromatogramEditorAction(kromatogram);
            action.actionPerformed(null);
        } else {
            throw new IllegalArgumentException(String.format("class '%s' not supported!", obj.getClass().toString()));
        }
    }

    @Override
    public StringList getNames(List<IFolderElement> objs) {
        StringList ret = new StringList();
        Iterator<IFolderElement> itr = objs.iterator();
        while (itr.hasNext()) {
            IFolderElement o = itr.next();
            ret.add(o.getName());
        }

        return ret;
    }

    @Override
    public void closeEditor(IFolderElement obj) {
        if (obj instanceof AnnotatedSeq) {
            CloseASEditorAction closeAction = new CloseASEditorAction((AnnotatedSeq) obj);
            closeAction.actionPerformed(null);
        } else if (obj instanceof PubmedArticle) {
            ClosePubmedEditorAction closeAction = new ClosePubmedEditorAction((PubmedArticle) obj);
            closeAction.actionPerformed(null);
        } else if (obj instanceof RENList) {
            CloseRENListEditorAction closeAction = new CloseRENListEditorAction((RENList) obj);
            closeAction.actionPerformed(null);
        } else if (obj instanceof PDBDoc) {
            ClosePDBEditorAction closeAction = new ClosePDBEditorAction((PDBDoc) obj);
            closeAction.actionPerformed(null);
        } else if (obj instanceof MSA) {
            CloseMSAEditorAction closeAction = new CloseMSAEditorAction((MSA) obj);
            closeAction.actionPerformed(null);
        } else if (obj instanceof TigrProject) {
            CloseTigrPtEditorAction closeAction = new CloseTigrPtEditorAction((TigrProject) obj);
            closeAction.actionPerformed(null);
        } else if (obj instanceof Kromatogram) {
            CloseKromatogramEditorAction closeAction = new CloseKromatogramEditorAction((Kromatogram) obj);
            closeAction.actionPerformed(null);
        } else {
            throw new IllegalArgumentException(String.format("Class '%s' not supported!", obj.getClass().toString()));
        }
    }

    @Override
    public void closeEditors(List<IFolderElement> objs) {
        for (IFolderElement obj : objs) {
            closeEditor(obj);
        }
    }

    @Override
    public IFolderElementList checkOverwrites(IFolderElementList ret, Folder toFolder, boolean skipIfSameFolder) {
        String ANCESTOR_TEMPLATE = "<html>This folder already contains a file named \"%s\""
                + " with the following descendant%s<br/>  %s "
                + "Replacing the existing file will <b>DEACTIVATE</b> the ancestral relationship <b>IRREVERSIBLY</b><br/><br/><br/>"
                + "Would you like to replace the existing file with the new one?</html>";
        final boolean isRecycleBin = toFolder.isRecycleBin();

        if (!isRecycleBin) {
            List<IFolderElement> toBeRemoved = new ArrayList<IFolderElement>();
            for (IFolderElement fe : ret) {
                boolean sameFolder = toFolder.getHibernateId().equals(fe.getFolder().getHibernateId());
                if (sameFolder && skipIfSameFolder) {
                    continue;
                }
                StringList names = toFolder.getElementNames(fe.getClass());
                if (names.contains(fe.getName())) {
                    String msgNoDescendants = String.format("<html>This folder already contains a file named \"%s\"<br/><br/>Would you like to replace the existing file with the new one?</html>", fe.getName());
                    String msg;
                    IFolderElement beingReplaced = toFolder.getElement(fe.getName(), fe.getClass());
                    if (beingReplaced instanceof AnnotatedSeq) {
                        StringList strList = asService.getDescendants((AnnotatedSeq) beingReplaced, StringList.class);
                        if (!strList.isEmpty()) {
                            msg = String.format(ANCESTOR_TEMPLATE, fe.getName(), strList.size() > 1 ? "s" : "", strList.toHtmList());
                        } else {
                            msg = msgNoDescendants;
                        }
                    } else {
                        msg = msgNoDescendants;
                    }
                    DialogDescriptor.Confirmation m = new DialogDescriptor.Confirmation(msg, String.format("Paste File \"%s\"", fe.getName()), DialogDescriptor.YES_NO_CANCEL_OPTION);
                    Object answer = DialogDisplayer.getDefault().notify(m);
                    if (answer.equals(DialogDescriptor.NO_OPTION)) {
                        toBeRemoved.add(fe);
                    } else if (answer.equals(DialogDescriptor.CANCEL_OPTION)) {
                        ret.clear();
                        return ret;
                    }
                }
            }
            ret.removeAll(toBeRemoved);
        } else {
            checkDesendants(ret, "Move to the Recycle bin");
        }

        return ret;
    }

    @Override
    public void checkDesendants(IFolderElementList fes) {
        checkDesendants(fes, "Ancestral relationship detected");
    }

    @Override
    public void checkDesendants(IFolderElementList fes, String title) {
        List<IFolderElement> toBeRemoved = new ArrayList<IFolderElement>();
        for (IFolderElement fe : fes) {
            if (fe instanceof AnnotatedSeq) {
                AnnotatedSeq as = (AnnotatedSeq) fe;
                // protein has no descendants
                if (as.isProtein()) {
                    continue;
                }
                StringList descendants = asService.getDescendants((AnnotatedSeq) fe, StringList.class);
                if (!descendants.isEmpty()) {
                    String line1 = String.format("The file %s is the ancestor of the following document%s%s", fe.getName(), descendants.size() > 1 ? "s" : "", StrUtil.toHtmlList(descendants));
                    String msg = String.format(CNST.MSG_FORMAT, line1, "Deleting or recycling the document will <b>IRREVERSIBLY DEACTIVATE</b> the ancestral relationship<br/><br/>Do you want to continue?");
                    DialogDescriptor.Confirmation c = new DialogDescriptor.Confirmation(msg, title, DialogDescriptor.YES_NO_OPTION);
                    Object answer = DialogDisplayer.getDefault().notify(c);
                    if (answer.equals(DialogDescriptor.NO_OPTION)) {
                        toBeRemoved.add(fe);
                    }
                }
            }
        }
        fes.removeAll(toBeRemoved);
    }

    @Override
    public void move(IFolderElement fe, Folder toFolder) {
        Folder fromFolder = fe.getFolder();
        if (fromFolder != null && fromFolder.getHibernateId().equals(toFolder.getHibernateId())) {
            return;
        }
        StringList elementNames = toFolder.getElementNames(fe.getClass());
        if (!toFolder.isRecycleBin()) {
            if (elementNames.containsIgnoreCase(fe.getName())) {
                Object ele = toFolder.getElement(fe.getName(), fe.getClass());
                toFolder = folderService.loadWithDataAndChildren(toFolder.getHibernateId());
                folderService.deleteData(toFolder, (IFolderElement) ele);
            }
        }

        if (fromFolder != null) {
            if (fe instanceof AnnotatedSeq) {
                asService.move((AnnotatedSeq) fe, toFolder);
            } else {
                if (fromFolder.getHibernateId() != null) {
                    if (!fromFolder.isNCBIFolder()) {
                        fromFolder = folderService.loadWithParentAndChildren(fromFolder.getHibernateId());
                    } else {
                        fromFolder.removeObject(fe);
                    }
                }
                fe.setPrevFolderPath(fromFolder.getAbsolutePath());
                fe.setFolder(toFolder);
                fe.setRead(false);
                merge(fe);
            }
        } else {
            fe.setFolder(toFolder);
            persist(fe);
        }
    }

    @Override
    public Object reloadFullyFromDB(IFolderElement o) {
        return reloadFromDB(o, true);
    }

    @Override
    public void persist(Collection<IFolderElement> co) {
        for (Object obj : co) {
            persist((IFolderElement) obj);
        }
    }

    @Override
    public void persist(IFolderElement o) {
        IPubmedDBService articleService = Lookup.getDefault().lookup(IPubmedDBService.class);
        IPDBService pdbService = Lookup.getDefault().lookup(IPDBService.class);
        IRENListService rlService = Lookup.getDefault().lookup(IRENListService.class);
        ITigrPtService tpService = Lookup.getDefault().lookup(ITigrPtService.class);
        IMSAService msaService = Lookup.getDefault().lookup(IMSAService.class);
        IKromatogramService kromatogramService = Lookup.getDefault().lookup(IKromatogramService.class);
        if (o instanceof AnnotatedSeq) {
            AnnotatedSeq as = (AnnotatedSeq) o;
            asService.persist(as);
        } else if (o instanceof PubmedArticle) {
            PubmedArticle a = (PubmedArticle) o;
            articleService.persist(a);
        } else if (o instanceof PDBDoc) {
            PDBDoc pdb = (PDBDoc) o;
            pdbService.persist(pdb);
        } else if (o instanceof RENList) {
            RENList rl = (RENList) o;
            rlService.persist(rl);
        } else if (o instanceof MSA) {
            MSA msa = (MSA) o;
            msaService.persist(msa);
        } else if (o instanceof TigrProject) {
            TigrProject tp = (TigrProject) o;
            tpService.persist(tp);
        } else if (o instanceof Kromatogram) {
            Kromatogram k = (Kromatogram) o;
            kromatogramService.persist(k);
        } else {
            throw new IllegalArgumentException(String.format("Unknown class: %s", o.getClass().getName()));
        }
    }

    @Override
    public void merge(Object o) {
        IPubmedDBService articleService = Lookup.getDefault().lookup(IPubmedDBService.class);
        IPDBService pdbService = Lookup.getDefault().lookup(IPDBService.class);
        IRENListService rlService = Lookup.getDefault().lookup(IRENListService.class);
        ITigrPtService tpService = Lookup.getDefault().lookup(ITigrPtService.class);
        IMSAService msaService = Lookup.getDefault().lookup(IMSAService.class);
        IKromatogramService kromatogramService = Lookup.getDefault().lookup(IKromatogramService.class);
        if (o instanceof AnnotatedSeq) {
            AnnotatedSeq as = (AnnotatedSeq) o;
            asService.merge(as);
        } else if (o instanceof PubmedArticle) {
            PubmedArticle a = (PubmedArticle) o;
            articleService.merge(a);
        } else if (o instanceof PDBDoc) {
            PDBDoc pdb = (PDBDoc) o;
            pdbService.merge(pdb);
        } else if (o instanceof RENList) {
            RENList rl = (RENList) o;
            rlService.merge(rl);
        } else if (o instanceof TigrProject) {
            TigrProject tp = (TigrProject) o;
            tpService.merge(tp);
        } else if (o instanceof MSA) {
            MSA msa = (MSA) o;
            msaService.merge(msa);
        } else if (o instanceof Kromatogram) {
            Kromatogram kromatogram = (Kromatogram) o;
            kromatogramService.merge(kromatogram);
        } else {
            throw new IllegalArgumentException(String.format("Unknown class: %s", o.getClass().getName()));
        }
    }

    @Override
    public Object reloadFromDB(IFolderElement o, boolean full) {
        IPubmedDBService articleService = Lookup.getDefault().lookup(IPubmedDBService.class);
        IPDBService pdbService = Lookup.getDefault().lookup(IPDBService.class);
        IRENListService rlService = Lookup.getDefault().lookup(IRENListService.class);
        ITigrPtService tpService = Lookup.getDefault().lookup(ITigrPtService.class);
        IMSAService msaService = Lookup.getDefault().lookup(IMSAService.class);
        IKromatogramService kromatogramService = Lookup.getDefault().lookup(IKromatogramService.class);
        Object ret = null;
        if (o instanceof AnnotatedSeq) {
            AnnotatedSeq as = (AnnotatedSeq) o;
            ret = asService.getByHibernateId(as.getHibernateId(), full);
        } else if (o instanceof PubmedArticle) {
            PubmedArticle a = (PubmedArticle) o;
            ret = articleService.getByHibernateId(a.getHibernateId(), full);
        } else if (o instanceof PDBDoc) {
            PDBDoc pdb = (PDBDoc) o;
            ret = pdbService.getByHibernateId(pdb.getHibernateId(), full);
        } else if (o instanceof RENList) {
            RENList rl = (RENList) o;
            ret = rlService.getByHibernateId(rl.getHibernateId(), full);
        } else if (o instanceof TigrProject) {
            TigrProject tp = (TigrProject) o;
            ret = tpService.getByHibernateId(tp.getHibernateId(), full);
        } else if (o instanceof MSA) {
            MSA msa = (MSA) o;
            MSAList msaList = msaService.getByHibernateIds(msa.getHibernateId());
            if (!msaList.isEmpty()) {
                ret = msaList.get(0);
            }
        } else if (o instanceof Kromatogram) {
            ret = kromatogramService.getFullByHibernateId(o.getHibernateId());
        } else {
            throw new IllegalArgumentException(String.format("Unknown class: %s", o.getClass().getName()));
        }
        return ret;
    }

    @Override
    public void copy(IFolderElement fe, Folder toFolder) {
        final Folder fromFolder = fe.getFolder();
        boolean sameFolder = (fromFolder != null && fromFolder.getHibernateId().equals(toFolder.getHibernateId()));

        StringList elementNames = toFolder.getElementNames(fe.getClass());
        if (!sameFolder && elementNames.containsIgnoreCase(fe.getName())) {
            Object ele = toFolder.getElement(fe.getName(), fe.getClass());
            toFolder = folderService.loadWithDataAndChildren(toFolder.getHibernateId());
            folderService.deleteData(toFolder, (IFolderElement) ele);
        }
        IFolderElement full;
        if (fe.getHibernateId() == null) {
            full = fe;
        } else {
            full = (IFolderElement) reloadFullyFromDB(fe);
        }
        full.setFolder(toFolder);
        Method cloneMethod = ReflectHelper.getMethodQuietly(full.getClass(), "clone");
        Object cloned = ReflectHelper.invoke(full, cloneMethod);
        if (fromFolder.getHibernateId().equals(toFolder.getHibernateId())) {
            String newName = toFolder.getNewElementName(full.getName());
            ((IFolderElement) cloned).setName(newName);
        }
        ((IFolderElement) cloned).setRead(false);
        persist((IFolderElement) cloned);
    }
}
