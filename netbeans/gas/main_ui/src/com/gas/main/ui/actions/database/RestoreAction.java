/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.actions.database;

import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.misc.CNST;
import com.gas.common.ui.progress.ProgressHandle;
import com.gas.common.ui.progress.ProgressHelper;
import com.gas.common.ui.progress.ProgRunnable;
import com.gas.common.ui.util.FileHelper;
import com.gas.common.ui.util.UIUtil;
import com.gas.database.core.backup.api.IBackupService;
import com.gas.database.core.backup.api.IRestoreService;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.LifecycleManager;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.WindowManager;

@ActionID(category = "File",
        id = "com.gas.main.ui.actions.RestoreAction")
@ActionRegistration(displayName = "Restore")/*"#CTL_RestoreAction")*/

@ActionReferences({
    @ActionReference(path = "Menu/File", position = -25)
})
@Messages("CTL_RestoreAction=Restore...")
public final class RestoreAction extends AbstractAction {

    static String TITLE = "Restore(deprecated)";
    
    public RestoreAction() {
        super(TITLE + "...", ImageHelper.createImageIcon(ImageNames.RESTORE_16));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser jc = new JFileChooser();
        jc.setAcceptAllFileFilterUsed(false);
        jc.addChoosableFileFilter(new FileNameExtensionFilter("Vector Friends Backup File", IBackupService.EXT));
        Frame frame = WindowManager.getDefault().getMainWindow();
        int answer = UIUtil.showDialog(jc, frame);
        if (answer == JFileChooser.APPROVE_OPTION) {
            final File selectedFile = jc.getSelectedFile();

            ProgressHelper.showProgressDialogAndRun(frame, new ProgRunnable() {
                @Override
                public void run(ProgressHandle handle) {
                    IRestoreService restoreService = Lookup.getDefault().lookup(IRestoreService.class);
                    handle.progress(10);
                    handle.progress("Preparing to restore...");
                    List<String> fileNames = FileHelper.unzip(selectedFile);

                    handle.progress(20);
                    handle.progress("Restoring...");
                    restoreService.restore(new File(fileNames.get(0)));
                }

                @Override
                public void done(ProgressHandle handle) {
                    final String msg = String.format(CNST.MSG_FORMAT, "Restore successfully completed!", "Please restart VectorFriends for the changes to take effect");
                    DialogDescriptor.Message mg = new DialogDescriptor.Message(msg);
                    mg.setTitle(TITLE);
                    DialogDisplayer.getDefault().notify(mg);

                    LifecycleManager.getDefault().markForRestart();
                    LifecycleManager.getDefault().exit();
                }
            }, TITLE);

        }
    }
}
