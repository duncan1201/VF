/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.tigr.core.ui.ctrl.settings;

import com.gas.common.ui.progress.ProgRunnable;
import com.gas.common.ui.progress.ProgressHandle;
import com.gas.common.ui.progress.ProgressHelper;
import com.gas.common.ui.util.UIUtil;
import com.gas.database.core.filesystem.service.api.IFolderService;
import com.gas.database.core.tigr.service.api.ITigrPtService;
import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.core.tigr.KromatogramList;
import com.gas.domain.core.tigr.TIGRSettings;
import com.gas.domain.core.tigr.TigrProject;
import com.gas.domain.ui.banner.BannerTC;
import com.gas.domain.ui.editor.tigr.OpenTigrPtEditorAction;
import com.gas.domain.ui.explorer.ExplorerTC;
import com.gas.tigr.core.service.ITigrExecuteService;
import com.gas.tigr.core.ui.editor.TigrPtEditor;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import org.openide.DialogDescriptor;
import org.openide.util.Lookup;
import org.openide.windows.WindowManager;

/**
 *
 * @author dq
 */
public class SettingsPanelListeners {

    private static IFolderService folderSvc = Lookup.getDefault().lookup(IFolderService.class);
    private static ITigrPtService tigrPtSvc = Lookup.getDefault().lookup(ITigrPtService.class);

    static class AssemblyListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent e) {

            Frame frame = WindowManager.getDefault().getMainWindow();
            ProgressHelper.showProgressDialogAndRun(frame, "Assembling Sequence...", new ProgRunnable() {
                
                TigrProject tigrNew;
                Folder folderUpdated;

                @Override
                public void run(ProgressHandle handle) {
                    handle.setIndeterminate(true);
                    handle.progress("Assembling sequences...");
                    JButton src = (JButton) e.getSource();
                    SettingsPanel settingsPanel = UIUtil.getParent(src, SettingsPanel.class);
                    TigrPtEditor editor = UIUtil.getParent(src, TigrPtEditor.class);
                    TigrProject tigrPtOld = editor.getTigrPt();
                    TIGRSettings settings = settingsPanel.getSettings();
                    KromatogramList kList = new KromatogramList(tigrPtOld.getKromatograms());

                    Folder folder = ExplorerTC.getInstance().getSelectedFolder();
                    ITigrExecuteService tigrService = Lookup.getDefault().lookup(ITigrExecuteService.class);
                    tigrNew = tigrService.assembly(kList.clone(), null, settings.clone());
                    if(!tigrNew.isCondigPresent()){
                        String msg = "No contigs found!";
                        DialogDescriptor.Message m = new DialogDescriptor.Message(msg, DialogDescriptor.INFORMATION_MESSAGE);
                        m.setTitle("LIGR Assembler");
                        return;
                    }
                    String name = folder.getNewElementName(tigrNew.getName());
                    tigrNew.setName(name);
                    tigrNew.setFolder(folder);
                    tigrPtSvc.persist(tigrNew);

                    folderUpdated = folderSvc.loadWithDataAndParentAndChildren(folder.getHibernateId());
                }

                @Override
                public void done(ProgressHandle handle) {
                    if(folderUpdated != null){
                        ExplorerTC.getInstance().updateFolder(folderUpdated);
                        BannerTC.getInstance().updateFolder(folderUpdated);
                    
                        if(tigrNew != null && tigrNew.isCondigPresent()){
                            OpenTigrPtEditorAction action = new OpenTigrPtEditorAction(tigrNew);
                            action.actionPerformed(null);
                        }
                    }
                }
            }, "LIGR Assembler");
        }
    }
}
