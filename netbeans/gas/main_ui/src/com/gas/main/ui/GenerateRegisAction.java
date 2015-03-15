/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui;

import com.gas.common.ui.progress.ProgRunnable;
import com.gas.common.ui.progress.ProgressHandle;
import com.gas.common.ui.progress.ProgressHelper;
import static com.gas.main.ui.MSG.ACTIVATE_A_LICENSE;
import static com.gas.main.ui.MSG.Get_a_license;
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
class GenerateRegisAction extends AbstractAction {

    static String GENERATED_MSG = "<html>Dear %s,<br/><br/>The license code has been sent to your email. <br/><br/>Please download and import it to VectorFriends. <br/><br/>Your sincerely,<br/>VectorFriends Team</html>";
    static String NO_LICENSE_MSG = "<html>Dear %s</html>";
    static String TITLE = MSG.Get_a_license;
    
    GenerateRegisAction() {
        super(TITLE + "...");
    }

    @Override
    public void actionPerformed(ActionEvent e) {                
        final GenerateRegisPanel ap = new GenerateRegisPanel();
        final LicenseService ls = LicenseService.getInstance();
        String email = ls.getEmail();
        if(email != null){
            ap.setEmail(email);
        }
        DialogDescriptor dd = new DialogDescriptor(ap, TITLE);
        ap.setDialogDescriptor(dd);
        ap.validateInput();
        Object answer = DialogDisplayer.getDefault().notify(dd);
        if (answer.equals(DialogDescriptor.OK_OPTION)) {
            Frame owner = WindowManager.getDefault().getMainWindow();
            ProgressHelper.showProgressDialogAndRun(owner, MSG.Connecting_to_remote_server, new ProgRunnable() {
                String resp;

                @Override
                public void run(ProgressHandle handle) {
                    handle.setIndeterminate(true);
                    handle.progress(MSG.Connecting_to_remote_server);                    

                    //NO_SUCH_USER, WRONG_PWD, NO_LICENSE, CREATED, NO_ACTIVATABLE_LICENSE
                    resp = ls.createStaticRegis(ap.getEmail(), ap.getPassword());
                    System.out.println();
                }

                @Override
                public void done(ProgressHandle handle) {
                    //String title = null;
                    String msg = null;
                    if (resp.equals(MSG.CREATED)) {
                        msg = String.format(GENERATED_MSG, ap.getEmail());
                    } else if (resp.equals(MSG.NO_SUCH_USER) || resp.equals(MSG.WRONG_PWD)) {
                        // prompt for retry            
                        if (resp.equals(MSG.WRONG_PWD)) {
                            msg = "Wrong password";
                        } else {
                            msg = "No such user: " + ap.getEmail();
                        }
                    } else if (resp.equals(MSG.NO_LICENSE)) {
                        msg = String.format("No license", "");
                    } else if (resp.equals("NO_ACTIVATABLE_LICENSE")) {
                        msg = String.format("No activatable license", "");
                    } else{
                        throw new IllegalArgumentException(String.format("Unexpected response: %s", resp));
                    }

                    DialogDescriptor.Message m = new DialogDescriptor.Message(msg, DialogDescriptor.INFORMATION_MESSAGE);
                    m.setTitle(TITLE);
                    DialogDisplayer.getDefault().notify(m);

                    if (resp.equals(MSG.NO_SUCH_USER) || resp.equals(MSG.WRONG_PWD)) {
                        actionPerformed(null);
                    }else if(resp.equals(MSG.CREATED)){
                        ImportLicenseAction a = new ImportLicenseAction();
                        a.actionPerformed(null);
                    }
                }
            }, MSG.Generating_license);

        }
    }
}
