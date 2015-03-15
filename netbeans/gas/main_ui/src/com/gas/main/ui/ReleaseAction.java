/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui;

import com.gas.common.ui.progress.ProgRunnable;
import com.gas.common.ui.progress.ProgressHandle;
import com.gas.common.ui.progress.ProgressHelper;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.windows.WindowManager;

/**
 *
 * @author dq
 */
class ReleaseAction extends AbstractAction {

    private final static String TITLE = "Release a License";
    
    ReleaseAction() {
        super(TITLE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final LicenseService ls = LicenseService.getInstance();
        String secret = ls.getSecretCode();
        if (secret == null || secret.isEmpty()) {
            String msg = "No license found";
            DialogDescriptor.Message m = new DialogDescriptor.Message(msg, DialogDescriptor.INFORMATION_MESSAGE);
            m.setTitle(TITLE);
            DialogDisplayer.getDefault().notify(m);
            return;
        } else if (TrialLicense.isTrialLicense(secret)) {
            String msg = "Cannot release a trial license";
            DialogDescriptor.Message m = new DialogDescriptor.Message(msg, DialogDescriptor.INFORMATION_MESSAGE);
            m.setTitle(TITLE);
            DialogDisplayer.getDefault().notify(m);
        } else if(FreeAcademicLicense.isFreeAcademicLicense(secret)){
            String msg = "Cannot release a free academic license";
            DialogDescriptor.Message m = new DialogDescriptor.Message(msg, DialogDescriptor.INFORMATION_MESSAGE);
            m.setTitle(TITLE);
            DialogDisplayer.getDefault().notify(m);
        } else if (ActivationCode.isActivationCode(secret)) {
            ActivationCode ac = ActivationCode.parse(secret);
            final long serialNo = ac.getSerialNo();
            Frame frame = WindowManager.getDefault().getMainWindow();
            ProgressHelper.showProgressDialogAndRun(frame, MSG.Connecting_to_remote_server, new ProgRunnable() {
                String resp;

                @Override
                public void run(ProgressHandle handle) {
                    handle.setIndeterminate(true);
                    handle.progress(MSG.Connecting_to_remote_server);
                    resp = ls.releaseActivationCode(serialNo + "");
                }

                @Override
                public void done(ProgressHandle handle) {
                    if( resp == null){
                        return;
                    }
                    if (resp.equals("good")) {
                        String msg = "Your license has been successfully released and can be reused again";
                        DialogDescriptor.Message m = new DialogDescriptor.Message(msg, DialogDescriptor.INFORMATION_MESSAGE);
                        m.setTitle(TITLE);
                        DialogDisplayer.getDefault().notify(m);
                        
                        ls.deleteSecretKey();
                        LicenseUtil.setBasicMode();
                    } else if (resp.equals("not_found")) {
                        String msg = "Your license is not found in the server";
                        DialogDescriptor.Message m = new DialogDescriptor.Message(msg, DialogDescriptor.INFORMATION_MESSAGE);
                        m.setTitle(TITLE);
                        DialogDisplayer.getDefault().notify(m);
                    }
                }
            }, MSG.Release_license);
        }
    }
}
