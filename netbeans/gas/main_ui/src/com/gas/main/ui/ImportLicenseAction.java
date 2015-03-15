/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui;

import com.gas.common.ui.progress.ProgRunnable;
import com.gas.common.ui.progress.ProgressHandle;
import com.gas.common.ui.progress.ProgressHelper;
import com.gas.common.ui.proxysetting.api.IProxyInternetService;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import javax.swing.AbstractAction;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.LifecycleManager;
import org.openide.util.Lookup;
import org.openide.windows.WindowManager;

/**
 *
 * @author dq
 */
class ImportLicenseAction extends AbstractAction {

    String initText;
    static final String DEFAULT_TITLE = MSG.Import_A_LICENSE;
    String title;

    ImportLicenseAction() {
        this(DEFAULT_TITLE + "...", null);
    }

    ImportLicenseAction(String title, String initText) {
        super(title);
        this.title = title;
        this.initText = initText;
    }

    private String getUserInput() {
        String ret = null;
        if (initText != null) {
            ret = initText;
        } else {
            ImportLicensePanel p = new ImportLicensePanel();
            DialogDescriptor dd = new DialogDescriptor(p, ImportLicensePanel.TITLE);
            p.setDialogDescriptor(dd);
            p.validateInput();
            Object answer = DialogDisplayer.getDefault().notify(dd);
            if (answer.equals(DialogDescriptor.OK_OPTION)) {
                ret = p.textArea.getText();
            }
        }
        return ret;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final LicenseService ls = LicenseService.getInstance();

        final String code = getUserInput();//p.textArea.getText();
        if (code == null) {
            String hex = ls.getSecretCode();
            if (hex != null && !hex.isEmpty()) {
                LicenseUtil.processSecretCode(hex);
            } else {
                LicenseUtil.setBasicMode();
            }
            return;
        }
        if (TrialLicense.isTrialLicense(code)) {
            LicenseUtil.processSecretCode(code);
            if (!ls.hasSecretCode()) {
                ls.saveSecretCode(code);
                displayRestartReminder();
            }
        } else if (RegisCode.isRegisCode(code)) {
            RegisCode regisCode = RegisCode.parse(code);

            Date now = new Date();
            if (now.after(regisCode.getMaxProductReleaseDate())) {
                DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.ENGLISH);
                String msg = String.format(LicenseUtil.REGIS_EXPIRED_FORMAT, dateFormat.format(regisCode.getMaxProductReleaseDate()));
                DialogDescriptor.Message m = new DialogDescriptor.Message(msg, DialogDescriptor.INFORMATION_MESSAGE);
                m.setTitle("Cannot activate the registration code");
                DialogDisplayer.getDefault().notify(m);
                return;
            } else {
                Frame frame = WindowManager.getDefault().getMainWindow();
                ProgressHelper.showProgressDialogAndRun(frame, MSG.Connecting_to_remote_server, new ProgRunnable() {
                    String resp;

                    @Override
                    public void run(ProgressHandle handle) {
                        handle.setIndeterminate(true);
                        handle.progress(MSG.Connecting_to_remote_server);
                        resp = ls.activateStaticRegis(code);
                    }

                    @Override
                    public void done(ProgressHandle handle) {
                        if (resp == null) {
                            IProxyInternetService internetSvc = Lookup.getDefault().lookup(IProxyInternetService.class);
                            internetSvc.displayConnProblem(true);                            
                        } else {
                            resp = resp.toLowerCase(Locale.ENGLISH);
                            if (resp.equals(MSG.consumed)) {
                                String msg = "<html>Cannot activate the registration code<br/><br/>The registration code have been consumed</html>";
                                DialogDescriptor.Message m = new DialogDescriptor.Message(msg, DialogDescriptor.INFORMATION_MESSAGE);
                                m.setTitle("Cannot activate the registration code");
                                DialogDisplayer.getDefault().notify(m);
                                String secretCode = ls.getSecretCode();
                                if (secretCode != null) {
                                    LicenseUtil.processSecretCode(secretCode);
                                } else {
                                    LicenseUtil.setBasicMode();
                                }
                            } else if (resp.startsWith(MSG.created)) {
                                String[] splits = resp.split(" ");
                                LicenseUtil.processSecretCode(splits[1]);
                                ls.saveSecretCode(splits[1]);
                                displayRestartReminder();
                            }
                        }
                    }
                }, "Activating");
            }
        } else if (FreeAcademicLicense.isFreeAcademicLicense(code)) {
            FreeAcademicLicense freeAcademicLicense = FreeAcademicLicense.parse(code);
            String fingerprint = freeAcademicLicense.getFingerprint();
            String selfFp = DiskUtil.getComputerFingerprint();
            if (!selfFp.equals(fingerprint)) {
                String msg = "The computer id in the license doesnot match your computer id";
                DialogDescriptor.Message m = new DialogDescriptor.Message(msg, DialogDescriptor.INFORMATION_MESSAGE);
                if (title.endsWith("...")) {
                    m.setTitle(title.substring(0, title.length() - 3));
                } else {
                    m.setTitle(title);
                }
                DialogDisplayer.getDefault().notify(m);
                return;
            }else{
                ls.saveSecretCode(code);
                displayRestartReminder();
            }
        } else {
            DialogDescriptor.Message m = new DialogDescriptor.Message("Unrecognized license", DialogDescriptor.INFORMATION_MESSAGE);
            if (title.endsWith("...")) {
                m.setTitle(title.substring(0, title.length() - 3));
            } else {
                m.setTitle(title);
            }
            DialogDisplayer.getDefault().notify(m);
        }
    }

    private void displayRestartReminder() {
        String msg = MSG.PLEASE_RESTART_VF;
        DialogDescriptor.Message m = new DialogDescriptor.Message(msg, DialogDescriptor.INFORMATION_MESSAGE);
        if (title.endsWith("...")) {
            m.setTitle(title.substring(0, title.length() - 3));
        } else {
            m.setTitle(title);
        }
        DialogDisplayer.getDefault().notify(m);
        LifecycleManager lm = LifecycleManager.getDefault();
        lm.markForRestart();
        lm.exit();
    }
}
