/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui;

import com.gas.common.ui.util.UIUtil;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.openide.DialogDescriptor;
import org.openide.NotificationLineSupport;

/**
 *
 * @author dq
 */
class GenerateRegisPanel extends JPanel implements DocumentListener {
    
    JTextField email;
    JPasswordField pwd;
    DialogDescriptor dialogDescriptor;
    NotificationLineSupport notificationLineSupport;
    
    GenerateRegisPanel(){
        UIUtil.setDefaultBorder(this);
        setLayout(new GridBagLayout());
        GridBagConstraints c;
        
        int gridy = 0;
        
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.EAST;
        c.gridy = gridy;
        add(new JLabel("Email"), c);
        
        c = new GridBagConstraints();
        c.gridy = gridy++;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        email = new JTextField();
        UIUtil.setPreferredWidthByPrototype(email, "firstnamelastname[at]mycompany.com");
        add(email, c);
        
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.EAST;
        c.gridx = 0;
        c.gridy = gridy;
        add(new JLabel(MSG.Password), c);
        
        c = new GridBagConstraints();
        c.gridy = gridy;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;        
        pwd = new JPasswordField();
        add(pwd, c);
        
        hookupListeners();
    }
    
    String getEmail(){
        return email.getText();
    }
    
    void setEmail(String e){
        email.setText(e);
    }
    
    String getPassword(){
        String ret = new String(pwd.getPassword());
        return ret;
    }
    
    void hookupListeners(){
        email.getDocument().addDocumentListener(this);
        pwd.getDocument().addDocumentListener(this);
    }

    void setDialogDescriptor(DialogDescriptor dialogDescriptor) {
        this.dialogDescriptor = dialogDescriptor;
        notificationLineSupport = dialogDescriptor.createNotificationLineSupport();
    }
    
    void validateInput(){
        if(dialogDescriptor == null){
            return;
        }
        if(email.getText()==null || email.getText().isEmpty()){
            dialogDescriptor.setValid(false);
            notificationLineSupport.setInformationMessage("Email cannot be empty");
        }else if(pwd.getPassword() == null || pwd.getPassword().length == 0){
            dialogDescriptor.setValid(false);
            notificationLineSupport.setInformationMessage("Password cannot be empty");
        }else{
            dialogDescriptor.setValid(true);
            notificationLineSupport.clearMessages();
        }
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        validateInput();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        validateInput();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        validateInput();
    }
    
}
