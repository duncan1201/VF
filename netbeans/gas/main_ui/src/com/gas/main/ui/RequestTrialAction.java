/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui;

import com.gas.common.ui.progress.ProgRunnable;
import com.gas.common.ui.progress.ProgressHandle;
import com.gas.common.ui.progress.ProgressHelper;
import com.gas.common.ui.util.StrUtil;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.Locale;
import javax.swing.AbstractAction;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.windows.WindowManager;

/**
 *
 * @author dq
 */
class RequestTrialAction extends AbstractAction {

    static String TRIAL_NO_ELIGIBLE = MSG.TRIAL_NO_ELIGIBLE;
    static final String TITLE = MSG.REQUEST_A_TRIAL;
    
    public RequestTrialAction() {
        super(TITLE + "...");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final LicenseService ls = LicenseService.getInstance();
        final String code = ls.getSecretCode();
        if (code != null && !code.isEmpty()) {
            boolean isTrial = TrialLicense.isTrialLicense(code);
            boolean isAct = ActivationCode.isActivationCode(code);
            if (isTrial) {
                TrialLicense tl = TrialLicense.parse(code);
                if(!tl.isExpired()){
                    String msg = MSG.CAN_NOT_REQUEST_ANOTHER_TRIAL;
                    DialogDescriptor.Message m = new DialogDescriptor.Message(msg, DialogDescriptor.INFORMATION_MESSAGE);
                    m.setTitle(TITLE);
                    DialogDisplayer.getDefault().notify(m);
                    return;
                }
            }
        }

        Frame frame = WindowManager.getDefault().getMainWindow();
        ProgressHelper.showProgressDialogAndRun(frame, MSG.Connecting_to_remote_server, new ProgRunnable() {
            String response;

            @Override
            public void run(ProgressHandle handle) {
                handle.setIndeterminate(true);
                handle.progress(MSG.Connecting_to_remote_server);
               
                response = ls.createTrialLicense();                                    
            }

            @Override
            public void done(ProgressHandle handle) {
                if(response == null){                    
                    if(code == null || code.isEmpty()){
                        LicenseUtil.setBasicMode();
                    }else{
                        LicenseUtil.processSecretCode(code);
                    }                    
                    return;
                }
                response = response.toLowerCase(Locale.ENGLISH);
                if (response.equalsIgnoreCase(MSG.NOT_ELIGIBLE)) {
                    String msg = String.format(TRIAL_NO_ELIGIBLE, DiskUtil.getComputerFingerprint());
                    DialogDescriptor.Message dd3 = new DialogDescriptor.Message(msg, DialogDescriptor.INFORMATION_MESSAGE);
                    dd3.setTitle(TITLE);
                    DialogDisplayer.getDefault().notify(dd3);
                    if (!ls.hasSecretCode()) {
                        LicenseUtil.setBasicMode();
                    }else{
                        LicenseUtil.processSecretCode(ls.getSecretCode());
                    }
                } else if(response.startsWith(MSG.success)){                   
                    boolean canInstall = false;
                    if (!ls.hasSecretCode()) {
                        canInstall = true;                        
                    }else{
                        if(FreeAcademicLicense.isFreeAcademicLicense(ls.getSecretCode())){
                            FreeAcademicLicense academicLicense = FreeAcademicLicense.parse(ls.getSecretCode());
                            String fingerprintLicense = academicLicense.getFingerprint();
                            String fingerprintComputer = DiskUtil.getComputerFingerprint();
                            if(!fingerprintComputer.equals(fingerprintLicense)){
                                canInstall = true;
                            }
                        }                 
                    }
                    if(canInstall){
                        String[] splits = response.split(MSG.SPACE);
                        ImportLicenseAction a = new ImportLicenseAction(RequestTrialPanel.TITLE, splits[1]);
                        a.actionPerformed(null);
                    }
                } else{
                    LicenseUtil.unexpectedServerError();
                    if(code == null || code.isEmpty()){
                        LicenseUtil.setBasicMode();
                    }
                }
            }
        }, RequestTrialPanel.TITLE);

        //}
    }
}
