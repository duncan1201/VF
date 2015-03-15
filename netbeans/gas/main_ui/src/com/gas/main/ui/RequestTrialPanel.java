/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui;

import com.gas.common.ui.appservice.api.IAppService;
import com.gas.common.ui.util.UIUtil;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.openide.DialogDescriptor;
import org.openide.NotificationLineSupport;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
class RequestTrialPanel extends JPanel {

    static String TITLE = MSG.REUQEST_A_TRIAL_LICENSE;
    
    private JTextField emailField;
    private DialogDescriptor dd;
    private NotificationLineSupport notificationSupport;

    public RequestTrialPanel() {
        UIUtil.setDefaultBorder(this);
        setLayout(new GridBagLayout());
        GridBagConstraints c;

        c = new GridBagConstraints();
        add(new JLabel("<html>Please enter your <b>non-public</b> email address</html>"), c);

        c = new GridBagConstraints();
        c.gridy = 1;
        c.anchor = GridBagConstraints.WEST;
        emailField = new JTextField();
        UIUtil.setPreferredWidthByPrototype(emailField, "123iamseniorceo@vectorfriends.com");
        add(emailField, c);

        hookupListeners();
    }

    private void hookupListeners() {
        emailField.getDocument().addDocumentListener(new RequestTrialPanelListeners.EmailListener(this));
    }

    void setDialogDescriptor(DialogDescriptor dd) {
        this.dd = dd;
        notificationSupport = this.dd.createNotificationLineSupport();
    }

    public String getEmail() {
        return emailField.getText();
    }

    void validateInput() {        
        IAppService appService = Lookup.getDefault().lookup(IAppService.class);
        if (dd == null) {
            return;
        }
        String email = getEmail();
        if (!LicenseUtil.isValidEmail(email)) {
            notificationSupport.setInformationMessage("Not a valid email address");
            dd.setValid(false);
        } else if (LicenseUtil.isPublicEmail(email)) {
            notificationSupport.setInformationMessage("Not a non-public email address");
            dd.setValid(false);
        } else {
            notificationSupport.clearMessages();
            dd.setValid(true);
        }
    }
}
