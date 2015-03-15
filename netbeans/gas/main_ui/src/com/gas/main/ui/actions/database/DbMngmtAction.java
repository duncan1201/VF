/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.actions.database;

import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.progress.ProgressHandle;
import com.gas.common.ui.progress.ProgressHelper;
import com.gas.common.ui.util.FileHelper;
import com.gas.database.core.backup.api.IBackupService;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.WindowManager;

@ActionID(category = "File",
        id = "com.gas.main.ui.actions.DbMngmtAction")
@ActionRegistration(displayName = "#CTL_DbConfigAction")
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 3)
})
@Messages("CTL_DbConfigAction=Database Management...")
public final class DbMngmtAction extends AbstractAction {

    static final String TITLE = "Database Management";

    public DbMngmtAction() {
        super(TITLE + "...", ImageHelper.createImageIcon(ImageNames.DB_16));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        DbConfigPanel dbConfigPanel = new DbConfigPanel();
        SwingUtilities.updateComponentTreeUI(dbConfigPanel);
        DialogDescriptor dd = new DialogDescriptor(dbConfigPanel, DbConfigPanel.TITLE);
        DialogDisplayer.getDefault().notify(dd);

    }
}
