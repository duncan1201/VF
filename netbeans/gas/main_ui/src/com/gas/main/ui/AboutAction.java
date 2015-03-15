/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui;

import com.gas.common.ui.appservice.api.IAppService;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;

@ActionID(category = "File",
id = "com.gas.main.ui.actions.AboutAction")
@ActionRegistration(displayName = "#CTL_AboutAction")
@ActionReferences({
    @ActionReference(path = "Menu/Help", position = 150)
})
@NbBundle.Messages("CTL_AboutAction=About VectorFriends")
public class AboutAction extends AbstractAction{
    static IAppService appService = Lookup.getDefault().lookup(IAppService.class);
    final static String TMPL = "About %s %s";
    final static String title = String.format(TMPL, appService.getAppName(), appService.getCurrentVersion());
    
    public AboutAction(){
        super(title);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        AboutPanel p = new AboutPanel();
        final Object[] options = {DialogDescriptor.OK_OPTION};
        DialogDescriptor d = new DialogDescriptor(p, title, true, options, DialogDescriptor.OK_OPTION, DialogDescriptor.DEFAULT_ALIGN, null, null);
        DialogDisplayer.getDefault().notify(d);
    }
    
}
