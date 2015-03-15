/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.actions;

import com.gas.common.ui.proxysetting.api.IProxyInternetService;
import com.gas.common.ui.proxysetting.api.ProxySetting;
import com.gas.main.ui.actions.pref.PrefsPanel;
import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.Lookup;
import org.openide.util.actions.Presenter;

/**
 *
 * @author dq
 */
public class PreferencesAction extends AbstractAction implements Presenter.Toolbar{

    public PreferencesAction(){
        super("Preferences...");
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        PrefsPanel prefPanel = new PrefsPanel();
        DialogDescriptor d = new DialogDescriptor(prefPanel, "Preferences");
        prefPanel.setDialogDescriptor(d);
        prefPanel.validateInput();
        Object answer = DialogDisplayer.getDefault().notify(d);
        if(answer.equals(DialogDescriptor.OK_OPTION)){
            IProxyInternetService svc = Lookup.getDefault().lookup(IProxyInternetService.class);
            ProxySetting proxySetting = prefPanel.getProxyConfigPanel().getProxySetting();
            svc.saveProxySetting(proxySetting);
        }
    }

    @Override
    public Component getToolbarPresenter() {
        JButton ret = new JButton();       
        ret.setAction(this);
        ret.setVerticalTextPosition(SwingConstants.BOTTOM);
        ret.setHorizontalTextPosition(SwingConstants.CENTER);        
        return ret;
    }
    
}
