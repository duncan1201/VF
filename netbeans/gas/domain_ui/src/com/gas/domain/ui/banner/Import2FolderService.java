/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.banner;

import com.gas.common.ui.progress.ProgRunnable;
import com.gas.common.ui.progress.ProgressHandle;
import com.gas.common.ui.progress.ProgressHelper;
import com.gas.common.ui.util.CommonUtil;
import com.gas.database.core.api.IDomainUtil;
import com.gas.database.core.filesystem.service.api.IFolderService;
import com.gas.domain.core.IFolderElement;
import com.gas.domain.core.IFolderElementList;
import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.ui.dynamicTable.TableTransferHandler;
import com.gas.domain.ui.dynamicTable.TableTransferable;
import com.gas.domain.ui.explorer.ExplorerTC;
import java.awt.Frame;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import javax.swing.TransferHandler;
import org.openide.util.Lookup;
import org.openide.util.datatransfer.MultiTransferObject;
import org.openide.util.lookup.ServiceProvider;
import org.openide.windows.WindowManager;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IImport2FolderService.class)
public class Import2FolderService implements IImport2FolderService{
    
    @Override
    public boolean import2Folder(TransferHandler.TransferSupport support, final Folder toFolder, final boolean move) {
        final IFolderService folderService = Lookup.getDefault().lookup(IFolderService.class);
        final IDomainUtil domainUtil = Lookup.getDefault().lookup(IDomainUtil.class);

        boolean ret = true;

        final Transferable transferable = support.getTransferable();
        DataFlavor[] dataFlavors = transferable.getTransferDataFlavors();
        if(dataFlavors.length == 0){
            return ret;
        }
        DataFlavor dataFlavor = dataFlavors[0];
        final MultiTransferObject mto = CommonUtil.getTransferData(transferable, dataFlavor);
        if (mto == null || mto.getCount() == 0) {
            return false;
        }
        IFolderElementList allFes = toFolderElementList(mto);
        if (allFes.isEmpty()) {
            return ret;
        }
        final IFolderElementList fes = domainUtil.checkOverwrites(allFes, toFolder, true);
        if (fes.isEmpty()) {
            return ret;
        }
        final String msgPrototype = String.format("Pasting 10 of 10 %s", fes.getNames().longest());
        Frame frame = WindowManager.getDefault().getMainWindow();
        ProgressHelper.showProgressDialogAndRun(frame, msgPrototype, new ProgRunnable() {
            private IFolderElementList toBeClosed = new IFolderElementList();

            @Override
            public void run(ProgressHandle handle) {

                for (int i = 0; i < fes.size(); i++) {
                    IFolderElement fe = fes.get(i);
                    handle.progress(String.format("Pasting %d of %d: %s", i + 1, fes.size(), fe.getName()));
                    int progress = Math.round((i + 1) * 100f / fes.size());
                    handle.progress(progress);

                    Folder fromFolder = fe.getFolder();
                    if (move) {
                        domainUtil.move(fe, toFolder);
                        toBeClosed.add(fe);
                    } else {
                        domainUtil.copy(fe, toFolder);
                    }
                    if (move) {
                        if (fromFolder.getHibernateId() != null && !fromFolder.isNCBIFolder()) {
                            fromFolder = folderService.loadWithDataAndChildren(fromFolder.getHibernateId());
                        }
                        BannerTC.getInstance().updateFolder(fromFolder);
                        ExplorerTC.getInstance().updateFolder(fromFolder);
                    }
                }
            }

            @Override
            public void done(ProgressHandle handle) {
                
                Folder toFolderUpdated = folderService.loadWithDataAndChildren(toFolder.getHibernateId());
                boolean contains = BannerTC.getInstance().containsFolder(toFolderUpdated);
                if (!contains) {
                    BannerTC.getInstance().updateFolder(toFolderUpdated, true);
                }
                BannerTC.getInstance().updateFolder(toFolderUpdated);
                ExplorerTC.getInstance().updateFolder(toFolderUpdated);
                ExplorerTC.getInstance().setSelectedFolder(toFolderUpdated);
                for (IFolderElement fe : toBeClosed) {
                    domainUtil.closeEditor(fe);
                }
                clear(mto);
                
            }
        }, "Pasting...");
        return ret;
    }

    private IFolderElementList toFolderElementList(MultiTransferObject mto) {
        IFolderElementList ret = new IFolderElementList();
        for (int i = 0; i < mto.getCount(); i++) {
            TableTransferable ttf = (TableTransferable) mto.getTransferableAt(i);
            IFolderElement fe = (IFolderElement) ttf.getDomainTransferData();
            if (fe != null) {
                ret.add(fe);
            }
        }
        return ret;
    }

    private void clear(MultiTransferObject mto) {
        for (int i = 0; i < mto.getCount(); i++) {
            TableTransferable ttf = (TableTransferable) mto.getTransferableAt(i);
            ttf.clear();
        }
    }
}
