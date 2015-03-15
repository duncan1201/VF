/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.actions.pref;

import com.gas.common.ui.proxysetting.api.IProxyConfigPanel;
import com.gas.common.ui.proxysetting.api.IProxyInternetService;
import java.awt.BorderLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import org.openide.DialogDescriptor;
import org.openide.NotificationLineSupport;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public class PrefsPanel extends JPanel{
    
    static Insets insets = new Insets(4, 6, 4, 6);
    //private GeneralPanel generalPanel;
    private IProxyConfigPanel proxyConfigPanel;
    private DialogDescriptor dialogDescriptor;
    private NotificationLineSupport notificationLineSupport;
    private IProxyInternetService internetSvc = Lookup.getDefault().lookup(IProxyInternetService.class);
    
    public PrefsPanel(){
        super(new BorderLayout());        
        JTabbedPane tabbedPane = new JTabbedPane();
        //generalPanel = new GeneralPanel();
        proxyConfigPanel = internetSvc.createProxyConfigPanel();
        tabbedPane.add((JComponent)proxyConfigPanel, "General");
        
        add(tabbedPane, BorderLayout.CENTER);
        hookupListeners();
    }
    
    private void hookupListeners(){
        PrefsPanelListeners.PtyListener ptyListener = new PrefsPanelListeners.PtyListener();
        addPropertyChangeListener(ptyListener);
    }
    
    public void validateInput(){
        proxyConfigPanel.validateInput();     
    }
    
    public void setDialogDescriptor(DialogDescriptor dialogDescriptor) {
        DialogDescriptor old = this.dialogDescriptor;        
        this.dialogDescriptor = dialogDescriptor;
        this.firePropertyChange("dialogDescriptor", old, this.dialogDescriptor);
    }
    
    public IProxyConfigPanel getProxyConfigPanel(){
        return proxyConfigPanel;
    }
}
