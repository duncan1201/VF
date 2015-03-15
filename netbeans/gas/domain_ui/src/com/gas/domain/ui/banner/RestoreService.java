/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.banner;

import com.gas.common.ui.progress.ProgRunnable;
import com.gas.common.ui.progress.ProgressHandle;
import com.gas.common.ui.progress.ProgressHelper;
import com.gas.database.core.api.IDomainUtil;
import com.gas.database.core.filesystem.service.api.IFolderService;
import com.gas.domain.core.IFolderElement;
import com.gas.domain.core.IFolderElementList;
import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.core.filesystem.FolderNames;
import com.gas.domain.ui.explorer.ExplorerTC;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import org.openide.windows.WindowManager;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IRestoreService.class)
public class RestoreService implements IRestoreService {

    @Override
    public void restore(final IFolderElementList objs) {
        final Folder rootFolder = ExplorerTC.getInstance().getRootFolder();
        for (Object o : objs) {
            IFolderElement r = (IFolderElement) o;
            String prevPath = r.getPrevFolderPath();
            Folder toFolder = rootFolder.getFolder(prevPath);
            if (toFolder == null) {
                toFolder = rootFolder.getChild(FolderNames.MY_DATA);
                DialogDescriptor.Message m = new DialogDescriptor.Message(String.format("Folder '%s' does not exist. Moving to %s", prevPath, toFolder.getAbsolutePath()), DialogDescriptor.INFORMATION_MESSAGE);
                DialogDisplayer.getDefault().notify(m);
                r.setPrevFolderPath(toFolder.getAbsolutePath());
            }
        }

        Frame frame = WindowManager.getDefault().getMainWindow();
        final IDomainUtil domainUtil = Lookup.getDefault().lookup(IDomainUtil.class);
        final IFolderService folderService = Lookup.getDefault().lookup(IFolderService.class);
        ProgressHelper.showProgressDialogAndRun(frame, new ProgRunnable() {
            Folder recycleBin;
            List<Folder> toFoldersUpdated = new ArrayList<Folder>();

            @Override
            public void run(ProgressHandle handle) {


                recycleBin = rootFolder.getChild(FolderNames.MY_DATA).getChild(FolderNames.RECYCLE_BIN);
                recycleBin = folderService.loadWithDataAndChildren(recycleBin.getHibernateId());
                List<Folder> toFolders = new ArrayList<Folder>();
                for (int i = 0; i < objs.size(); i++) {
                    handle.progress(String.format("Restoring %d of %d", i + 1, objs.size()));
                    int progress = Math.round((i + 1) * 100f / objs.size());
                    handle.progress(progress - 1);
                    IFolderElement r = objs.get(i);
                    String prevPath = r.getPrevFolderPath();
                    Folder toFolder = rootFolder.getFolder(prevPath);
                    toFolder = folderService.loadWithDataAndChildren(toFolder.getHibernateId());
                    IFolderElementList fes = new IFolderElementList(r);
                    fes = domainUtil.checkOverwrites(fes, toFolder, false);
                    if (fes.isEmpty()) {
                        continue;
                    }
                    domainUtil.move(r, toFolder);
                    toFolders.add(toFolder);

                }
                recycleBin = folderService.loadWithDataAndChildren(recycleBin.getHibernateId());
                for (Folder toFolder : toFolders) {
                    Folder toFolderUpdated = folderService.loadWithDataAndChildren(toFolder.getHibernateId());
                    toFoldersUpdated.add(toFolderUpdated);
                }
            }

            @Override
            public void done(ProgressHandle handle) {
                handle.progress(100);
                handle.progress("Refreshing");
                for (Folder toFolder : toFoldersUpdated) {
                    ExplorerTC.getInstance().updateFolder(toFolder);
                    BannerTC.getInstance().updateFolder(toFolder);
                }

                ExplorerTC.getInstance().updateFolder(recycleBin);
                BannerTC.getInstance().updateFolder(recycleBin);
            }
        }, "Restoring...");
    }
}
