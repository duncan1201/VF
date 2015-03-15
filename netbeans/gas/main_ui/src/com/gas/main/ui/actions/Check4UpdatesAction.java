/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.actions;

import com.gas.update.service.api.ISoftProductService;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;

@ActionID(category = "File",
id = "com.gas.main.ui.actions.Check4UpdatesAction")
@ActionRegistration(displayName = "#CTL_Check4UpdatesAction")
@ActionReferences({
    @ActionReference(path = "Menu/Help", position = 100)
})
@NbBundle.Messages("CTL_Check4UpdatesAction=Check for Updates")
public class Check4UpdatesAction extends AbstractAction {

    public Check4UpdatesAction(){
        super("Check for Updates...");
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        ISoftProductService svc = Lookup.getDefault().lookup(ISoftProductService.class);
        svc.setCheckAtStartup(true);
        svc.checkNewRelease(true, false);
    }
    
}
