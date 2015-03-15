/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.tigr.core.actions;

import com.gas.common.ui.progress.ProgRunnable;
import com.gas.common.ui.progress.ProgressHandle;
import com.gas.common.ui.progress.ProgressHelper;
import com.gas.database.core.filesystem.service.api.IFolderService;
import com.gas.database.core.tigr.service.api.IKromatogramService;
import com.gas.database.core.tigr.service.api.ITigrPtService;
import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.core.tigr.Kromatogram;
import com.gas.domain.core.tigr.KromatogramList;
import com.gas.domain.core.tigr.TIGRSettings;
import com.gas.domain.core.tigr.TigrProject;
import com.gas.domain.ui.banner.BannerTC;
import com.gas.domain.ui.editor.tigr.OpenTigrPtEditorAction;
import com.gas.domain.ui.explorer.ExplorerTC;
import com.gas.tigr.core.service.ITigrExecuteService;
import com.gas.tigr.core.ui.ctrl.settings.SettingsContentPanel;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.Lookup;
import org.openide.windows.WindowManager;

/**
 *
 * @author dq
 */
public class TigrAction extends AbstractAction {

    final static String TITLE = "LIGR Assembler";

    public TigrAction() {
        super(TITLE + "...");
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        IKromatogramService kService = Lookup.getDefault().lookup(IKromatogramService.class);
        List<Kromatogram> kromatograms = BannerTC.getInstance().getCheckedObjects(Kromatogram.class);
        final KromatogramList kList = new KromatogramList();
        for (Kromatogram k : kromatograms) {
            Kromatogram full = kService.getFullByHibernateId(k.getHibernateId());
            kList.add(full.clone());
        }
        final SettingsContentPanel settingsPanel = new SettingsContentPanel();
        final TIGRSettings settings = new TIGRSettings();
        settingsPanel.setTigrSettings(settings);
        DialogDescriptor dd = new DialogDescriptor(settingsPanel, TITLE);
        final Object answer = DialogDisplayer.getDefault().notify(dd);
        if (!answer.equals(DialogDescriptor.OK_OPTION)) {
            return;
        }
        Frame frame = WindowManager.getDefault().getMainWindow();
        ProgressHelper.showProgressDialogAndRun(frame, new ProgRunnable() {
            TigrProject tigrProject;
            Folder updatedFolder;

            @Override
            public void run(ProgressHandle handle) {
                handle.setIndeterminate(true);
                handle.progress("Assembling sequences...");

                IFolderService folderSvc = Lookup.getDefault().lookup(IFolderService.class);
                Folder folder = ExplorerTC.getInstance().getSelectedFolder();

                ITigrExecuteService tigrExecuteSvc = Lookup.getDefault().lookup(ITigrExecuteService.class);
                TIGRSettings settingsUpdated = settingsPanel.getSettingsFromUI();
                tigrProject = tigrExecuteSvc.assembly(kList, null, settingsUpdated);
                if (!tigrProject.isCondigPresent()) {
                    String msg = "No contigs found!";
                    DialogDescriptor.Message m = new DialogDescriptor.Message(msg, DialogDescriptor.INFORMATION_MESSAGE);
                    m.setTitle(TITLE);
                    DialogDisplayer.getDefault().notify(m);
                    return;
                }
                String name = folder.getNewElementName("Assembly");
                tigrProject.setName(name);
                tigrProject.setDesc(String.format("assembly of %d chromatograms", kList.size()));
                tigrProject.setFolder(folder);

                ITigrPtService tigrSvc = Lookup.getDefault().lookup(ITigrPtService.class);
                tigrSvc.persist(tigrProject);

                updatedFolder = folderSvc.loadWithDataAndParentAndChildren(folder.getHibernateId());
            }

            @Override
            public void done(ProgressHandle handle) {
                if (updatedFolder != null) {
                    BannerTC.getInstance().updateFolder(updatedFolder);
                    ExplorerTC.getInstance().updateFolder(updatedFolder);

                    if (tigrProject != null && tigrProject.isCondigPresent()) {
                        OpenTigrPtEditorAction open = new OpenTigrPtEditorAction(tigrProject);
                        open.actionPerformed(null);
                    }
                }
            }
        }, TITLE);
    }
}
