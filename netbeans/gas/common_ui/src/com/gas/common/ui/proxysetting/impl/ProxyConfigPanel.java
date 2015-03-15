/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.proxysetting.impl;

import com.gas.common.ui.proxysetting.api.IProxyConfigPanel;
import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.jcomp.TitledPanel;
import com.gas.common.ui.proxysetting.api.IProxyInternetService;
import com.gas.common.ui.proxysetting.api.ProxySetting;
import com.gas.common.ui.util.StrUtil;
import com.gas.common.ui.util.UIUtil;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import org.openide.DialogDescriptor;
import org.openide.NotificationLineSupport;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public class ProxyConfigPanel extends JPanel implements IProxyConfigPanel {
    
    public static final String CMD_NO_PROXY = "cmd_no_proxy";
    public static final String CMD_SYSTEM_PROXY = "cmd_system_proxy";
    public static final String CMD_MANUAL_PROXY = "cmd_manual_proxy";
    
    private JRadioButton noProxyOption;
    private JRadioButton systemProxyOption;
    private JRadioButton manualProxyOption;
    private JPanel manualProxyOptionPanel;
    private JTextField hostField;
    private JTextField portField;
    
    // dialog descriptor
    private DialogDescriptor dialogDescriptor;
    private NotificationLineSupport notificationLineSupport;
    
    // manual proxy authentication panel
    private TitledPanel manualProxyAuthPanel;
    private JCheckBox proxyAuthBox;
    private JTextField usernameField;
    private JPasswordField pwdField;
    
    // 
    private JButton testConnBtn;
    private JLabel testResultLabel;
    
    public ProxyConfigPanel(){
        createComponents();        
        hookupListeners();
        initComponents();
    }
    
    private void initComponents(){
        IProxyInternetService svc = Lookup.getDefault().lookup(IProxyInternetService.class);
        if(svc != null){
            ProxySetting proxySetting = svc.getProxySetting();
            ProxySetting.TYPE type = proxySetting.getType();
            if(type == ProxySetting.TYPE.NO_PROXY){
                noProxyOption.setSelected(true);
            }else if(type == ProxySetting.TYPE.SYSTEM_SETTING){
                systemProxyOption.setSelected(true);
            }else if(type == ProxySetting.TYPE.MANUAL){
                manualProxyOption.setSelected(true);                
            }
            hostField.setText(proxySetting.getHostName());
            portField.setText(proxySetting.getPort().toString());
                                
            proxyAuthBox.setSelected(proxySetting.isAuthRequired());
            usernameField.setText(proxySetting.getUsername());
            pwdField.setText(proxySetting.getPassword());
                
            updateManualProxyPanelEnablement();
        }else{
            throw new UnsupportedOperationException("IProxyInternetService null");
        }
    }

    @Override
    public void setNotificationLineSupport(NotificationLineSupport notificationLineSupport) {
        this.notificationLineSupport = notificationLineSupport;
    }
    
    @Override
    public void setDialogDescriptor(DialogDescriptor dialogDescriptor){
        this.dialogDescriptor = dialogDescriptor;
    }

    
    
    @Override
    public void validateInput(){
        if(notificationLineSupport == null){
            return ;
        }
        if(manualProxyOption.isSelected()){
            if(hostField.getText().isEmpty()){
                notificationLineSupport.setInformationMessage("Please enter the hostname");                
            }else if(!StrUtil.isInteger(portField.getText())){                
                notificationLineSupport.setInformationMessage("Please enter the port number");                
            }else if(proxyAuthBox.isSelected()){
                if(usernameField.getText().isEmpty()){
                    notificationLineSupport.setInformationMessage("Please enter the username");                    
                }else if(pwdField.getPassword().length == 0){
                    notificationLineSupport.setInformationMessage("Please enter the password");                    
                }else{
                    notificationLineSupport.clearMessages();
                }
            }else{
                notificationLineSupport.clearMessages();
            }
            dialogDescriptor.setValid(notificationLineSupport.getInformationMessage() == null || 
                    notificationLineSupport.getInformationMessage().isEmpty());
        }else{
            dialogDescriptor.setValid(true);
            notificationLineSupport.clearMessages();            
        }
        testConnBtn.setEnabled(dialogDescriptor.isValid());
    }
    
    @Override
    public ProxySetting getProxySetting(){
        ProxySetting ret = new ProxySetting();
        if(noProxyOption.isSelected()){
            ret.setType(ProxySetting.TYPE.NO_PROXY);             
        }else if(systemProxyOption.isSelected()){
            ret.setType(ProxySetting.TYPE.SYSTEM_SETTING);            
        }else if(manualProxyOption.isSelected()){
            ret.setType(ProxySetting.TYPE.MANUAL);            
        }
        
        ret.setProxyType(ProxySetting.PROXY_TYPE.HTTP);
        ret.setHostName(hostField.getText());
        ret.setPort(Integer.parseInt(portField.getText()));
            
        ret.setAuthRequired(proxyAuthBox.isSelected());
        ret.setUsername(usernameField.getText());
        String password = String.valueOf(pwdField.getPassword());
        ret.setPassword(password);
            
        
        return ret;
    }
    
    private void createComponents(){
        Insets insets = new Insets(4, 6, 4, 6);
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right));
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        c.anchor = GridBagConstraints.NORTH;
        add(new JLabel("Proxy Settings:"), c);
        
        JPanel panel = createOptionsPanel();
        c = new GridBagConstraints();
        c.gridy = 0;
        add(panel, c);
        
        c = new GridBagConstraints();        
        c.gridx = 0;
        c.gridy = 1;
        add(Box.createRigidArea(new Dimension(1,1)), c);
        
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        JPanel testPanel = createTestPanel();
        add(testPanel, c);
    }       
    
    private JPanel createTestPanel(){
        JPanel ret = new JPanel(new GridBagLayout());
        //ret.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        GridBagConstraints c = new GridBagConstraints();
        testConnBtn = new JButton("Test Connection");
        ret.add(testConnBtn, c);
        
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.insets = new Insets(0, 5, 0, 0);
        testResultLabel = new JLabel();
        testResultLabel.setHorizontalAlignment(SwingConstants.LEFT);
        //testResultLabel.setBorder(BorderFactory.createLineBorder(Color.yellow));
        ret.add(testResultLabel, c);
        
        return ret;
    }

    public JButton getTestConnBtn() {
        return testConnBtn;
    }

    JLabel getTestResultLabel() {
        return testResultLabel;
    }
    
    private JPanel createOptionsPanel(){
        JPanel ret = new JPanel();
        ret.setLayout(new GridBagLayout());
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.anchor = GridBagConstraints.WEST;        
        noProxyOption = new JRadioButton("No Proxy");
        noProxyOption.setActionCommand(CMD_NO_PROXY);
        ret.add(noProxyOption, c);
        
        c = new GridBagConstraints();
        c.gridx = 0;
        c.anchor = GridBagConstraints.WEST;
        systemProxyOption = new JRadioButton("Use System Proxy Settings");
        systemProxyOption.setActionCommand(CMD_SYSTEM_PROXY);
        ret.add(systemProxyOption, c);
        
        c = new GridBagConstraints();
        c.gridx = 0;
        c.anchor = GridBagConstraints.WEST;
        manualProxyOption = new JRadioButton("Manual Proxy Settings");
        manualProxyOption.setActionCommand(CMD_MANUAL_PROXY);
        ret.add(manualProxyOption, c);
        
        c = new GridBagConstraints();
        c.gridx = 0;
        c.anchor = GridBagConstraints.WEST;
        manualProxyOptionPanel = createManualProxyOptionPanel();
        ret.add(manualProxyOptionPanel, c);
        
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(noProxyOption);
        buttonGroup.add(systemProxyOption);
        buttonGroup.add(manualProxyOption);
        return ret;
    }
    
    private JPanel createManualProxyOptionPanel(){
        Insets insets = new Insets(0, 9, 0, 0);
        JPanel ret = new JPanel(new GridBagLayout());
        ret.setBorder(BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right));
        GridBagConstraints c = new GridBagConstraints();        
        c.gridy = 0;
        ret.add(new JLabel("HTTP Proxy:"), c);
        
        c = new GridBagConstraints();
        c.gridy = 0;
        hostField = new JTextField();
        UIUtil.setPreferredWidthByPrototype(hostField, "http://www.channelnewsasia12345.com");
        ret.add(hostField, c);
        
        c = new GridBagConstraints();
        c.gridy = 0;
        ret.add(new JLabel("Port:"), c);
        
        c = new GridBagConstraints();
        c.gridy = 0;
        portField = new JTextField();
        UIUtil.setPreferredWidthByPrototype(portField, "8888");
        ret.add(portField, c);                
        
        manualProxyAuthPanel = createProxyAuthenticationPanel();
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 4;
        c.anchor = GridBagConstraints.LINE_START;
        ret.add(manualProxyAuthPanel, c);
        
        return ret;
    }
    
    void updateManualProxyPanelEnablement(){
        boolean manualSelected = manualProxyOption.isSelected();
        boolean authSelected = proxyAuthBox.isSelected();
        UIUtil.enabledRecursively(manualProxyOptionPanel, manualSelected);
        if(manualSelected){
            UIUtil.enabledRecursively(manualProxyAuthPanel.getContentPane(), authSelected);
        }
    }
    
    private TitledPanel createProxyAuthenticationPanel(){
        TitledPanel ret = new TitledPanel("");
        proxyAuthBox = new JCheckBox("Proxy Requires Authentication");
        ret.setLeftDecoration(proxyAuthBox);
        
        JPanel usernamePwdPanel = new JPanel(new GridBagLayout());
        
        int gridy = 0;
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = gridy;
        usernamePwdPanel.add(new JLabel("Username:"), c);
        
        c = new GridBagConstraints();
        c.gridy = gridy;
        usernameField = new JTextField(12);
        usernamePwdPanel.add(usernameField, c);
              
        gridy = 1;
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        usernamePwdPanel.add(new JLabel("Password:"), c);
        
        c = new GridBagConstraints();
        c.gridy = gridy;
        pwdField = new JPasswordField(12);
        usernamePwdPanel.add(pwdField, c);
        
        ret.getContentPane().add(usernamePwdPanel, BorderLayout.CENTER);
        return ret;
    }
    
    private void hookupListeners(){
        ProxyConfigPanelListeners.ProxyOptionListener proxyOptionListener = new ProxyConfigPanelListeners.ProxyOptionListener();
        
        noProxyOption.addActionListener(proxyOptionListener);
        systemProxyOption.addActionListener(proxyOptionListener);
        manualProxyOption.addActionListener(proxyOptionListener);
        
        ProxyConfigPanelListeners.ManualProxyAuthListener l = new ProxyConfigPanelListeners.ManualProxyAuthListener();
        proxyAuthBox.addItemListener(l);
        
        ProxyConfigPanelListeners.DocListener tl = new ProxyConfigPanelListeners.DocListener(this);
        hostField.getDocument().addDocumentListener(tl);
        portField.getDocument().addDocumentListener(tl);
        usernameField.getDocument().addDocumentListener(tl);
        pwdField.getDocument().addDocumentListener(tl);
        
        ProxyConfigPanelListeners.TestBtnListener testListener = new ProxyConfigPanelListeners.TestBtnListener();
        testConnBtn.addActionListener(testListener);
    }
}
