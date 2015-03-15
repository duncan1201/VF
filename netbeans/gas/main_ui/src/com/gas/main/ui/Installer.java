/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui;

import com.gas.common.ui.misc.CNST;
import com.gas.common.ui.util.CommonUtil;
import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.database.core.misc.api.VF_PARAMS;
import com.gas.database.core.conn.api.DbConnSettings;
import com.gas.database.core.conn.api.IDbConnSettingsService;
import com.gas.database.core.filesystem.service.api.IFolderService;
import com.gas.database.core.misc.api.IVfParamService;
import com.gas.database.core.schema.api.ISchemaUpgrader;
import com.gas.database.core.schema.api.IDatabaseSchemaUpgradeService;
import com.gas.database.core.service.api.IDefaultDatabaseService;
import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.core.filesystem.FolderNames;
import com.gas.main.ui.exceptionhandler.NewFunctionExceptionHandler;
import com.gas.main.ui.molpane.IMolPaneFactory;
import com.gas.update.service.api.ISoftProductService;
import com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.Component;
import java.io.File;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.LifecycleManager;
import org.openide.NotifyDescriptor;
import org.openide.awt.Toolbar;
import org.openide.awt.ToolbarPool;
import org.openide.modules.ModuleInstall;
import org.openide.util.Lookup;
import org.openide.util.Utilities;
import org.openide.windows.WindowManager;

/**
 *
 * @author dunqiang
 */
public class Installer extends ModuleInstall {

    static Logger log = Logger.getLogger(Installer.class.getName());
    private IDbConnSettingsService dbConnSvc = Lookup.getDefault().lookup(IDbConnSettingsService.class);

    @Override
    public void restored() {
        // suppress the Unexpected Exception dialog
        System.setProperty("netbeans.exception.report.min.level", "99999");
        
        UIUtil.setNimbusLookAndFeel();
        boolean isSet = dbConnSvc.isDatabaseConnSet();        
        if (isSet) {
            DbConnSettings dbConnSettings = dbConnSvc.getDbConnSettings();
            boolean isDbPresent = dbConnSettings.isDatabaseFilePresent();
            if (!isDbPresent) {
                
                String line = String.format("Database file <b>%s</b> not found", dbConnSettings.getDatabaseFile().getAbsolutePath());
                String line2 = "It will create a new database";
                DialogDescriptor.Message m = new DialogDescriptor.Message(String.format(CNST.MSG_FORMAT, line, line2), DialogDescriptor.INFORMATION_MESSAGE);                
                DialogDisplayer.getDefault().notify(m);
                
                dbConnSvc.setDbConnSettings(dbConnSvc.getDefaultDbConnSettings());
            }
        } else {
            dbConnSvc.setDbConnSettings(dbConnSvc.getDefaultDbConnSettings());
        }
        
        DbConnSettings dbConnSettings = dbConnSvc.getDbConnSettings();
        File baseDir = dbConnSettings.getBaseDir();
        System.setProperty("h2.baseDir", baseDir.getAbsolutePath());
        final IDefaultDatabaseService databaseService = Lookup.getDefault().lookup(IDefaultDatabaseService.class);
        final IDatabaseSchemaUpgradeService schemaUpgradeSvc = Lookup.getDefault().lookup(IDatabaseSchemaUpgradeService.class);
        if (!databaseService.isDefaultDatabaseRunning()) {
            databaseService.startDefaultDatabaseServer();
        }
        if (!databaseService.isDefaultDatabaseSchemaPresent()) {
            databaseService.initDefaultDatabaseSchema();
            IFolderService folderService = Lookup.getDefault().lookup(IFolderService.class);
            folderService.importInitialData();
            IVfParamService paramSvc = Lookup.getDefault().lookup(IVfParamService.class);
            
            paramSvc.merge(VF_PARAMS.Database_version.name(), schemaUpgradeSvc.getMaxVersion().toString());
        } else {
            List<ISchemaUpgrader> upgraders = schemaUpgradeSvc.getISchemaUpgraders();
            if(!upgraders.isEmpty()){
                boolean success = schemaUpgradeSvc.upgradeDatabaseSchema(upgraders);
            }
            // upgrade database schema if necessary
            // update initial data if necessary

            //IFolderService folderService = Lookup.getDefault().lookup(IFolderService.class);
            //folderService.createNCBIChildFolderIfNeeded(FolderNames.NCBI_GENOME);
            //Folder root = folderService.getFolderTree();
            //Folder ncbiRoot = root.getChild(FolderNames.NCBI_ROOT);
            //Folder genomeFolder = new Folder(FolderNames.NCBI_GENOME);
            //ncbiRoot.addFolder(genomeFolder);
            //folderService.merge(ncbiRoot);
            //System.out.println();
        }

        // initialize the fonts 
        FontUtil.initAllFonts();
        FontUtil.initMSFonts();
        // pre-created at least one MolPane here
        Lookup.getDefault().lookup(IMolPaneFactory.class);
        WindowManager.getDefault().invokeWhenUIReady(new Runnable() {
            @Override
            public void run() {
                Logger.getLogger("").addHandler(new NewFunctionExceptionHandler());

                boolean f = ToolbarPool.getDefault().isFinished();
                CommonUtil.maximizeMainFrame();

                LicenseService licenseService = LicenseService.getInstance();

                String secretCode = licenseService.getSecretCode();
                if (!TrialLicense.isTrialLicense(secretCode) && !ActivationCode.isActivationCode(secretCode) && !FreeAcademicLicense.isFreeAcademicLicense(secretCode)) {
                    secretCode = null;
                    licenseService.deleteSecretKey();
                    LicenseUtil.setBasicMode();
                }

                if (secretCode == null || secretCode.isEmpty()) {
                    NoLicensesPanel noLicensePanel = new NoLicensesPanel();
                    DialogDescriptor c = new DialogDescriptor(noLicensePanel, NoLicensesPanel.title, true, NotifyDescriptor.OK_CANCEL_OPTION, DialogDescriptor.OK_OPTION, null);
                    Object answer = DialogDisplayer.getDefault().notify(c);
                    if (answer.equals(DialogDescriptor.OK_OPTION)) {
                        if (noLicensePanel.isImportLicense()) {
                            ImportLicenseAction ilAction = new ImportLicenseAction();
                            ilAction.actionPerformed(null);
                        } else if (noLicensePanel.isRequestTrialLicense()) {
                            RequestTrialAction stAction = new RequestTrialAction();
                            stAction.actionPerformed(null);
                        } else if (noLicensePanel.isBasicMode()) {
                            LicenseUtil.setBasicMode();
                        }
                    } else {
                        LicenseUtil.setBasicMode();
                    }
                } else {
                    LicenseUtil.processSecretCode(secretCode);
                    ISoftProductService softProductSvc = Lookup.getDefault().lookup(ISoftProductService.class);
                    if (softProductSvc.isCheckAtStartup()) {
                        softProductSvc.checkNewRelease(false, true);
                    }
                }
                configureToolbar();
            }

            private void configureToolbar() {
                LicenseService ls = LicenseService.getInstance();
                ToolbarPool.getDefault().waitFinished();
                Toolbar toolbar = ToolbarPool.getDefault().findToolbar("CUSTOME");
                toolbar.setBorder(BorderFactory.createEmptyBorder(2, 4, 0, 4));
                toolbar.setFloatable(false);
                Component[] comps = toolbar.getComponents();
                for (Component comp : comps) {
                    if (comp instanceof AbstractButton) {
                        AbstractButton btn = (AbstractButton) comp;
                        String text = btn.getText();
                        if (text == null || text.isEmpty()) {
                            btn.setHorizontalTextPosition(SwingConstants.CENTER);
                            btn.setVerticalTextPosition(SwingConstants.BOTTOM);
                            btn.setText("Save");
                        } else if (text.equalsIgnoreCase(MSG.Print)
                                || text.equalsIgnoreCase(MSG.Import)
                                || text.equalsIgnoreCase(MSG.Export)
                                || text.equalsIgnoreCase(MSG.TOPO)
                                || text.equalsIgnoreCase(MSG.Gateway)
                                || text.equalsIgnoreCase(MSG.Alignment)
                                || text.equalsIgnoreCase(MSG.Tree)) {
                            btn.setEnabled(!ls.isBasicMode());
                        }
                    }
                }
            }
        });


    }
}
