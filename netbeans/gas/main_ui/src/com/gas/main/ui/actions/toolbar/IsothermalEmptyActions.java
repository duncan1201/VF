/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.actions.toolbar;

import com.gas.domain.ui.editor.as.gibson.GibsonAssemblyAction;
import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle;
import org.openide.util.actions.Presenter;

@ActionID(category = "Tools",
        id = "com.gas.main.ui.actions.toolbar.IsothermalEmptyActions")
@ActionRegistration(displayName = "#CTL_IsothermalEmptyActions")
@ActionReferences({
    @ActionReference(path = "Toolbars/CUSTOME", position = 3335)
})
@NbBundle.Messages("CTL_IsothermalEmptyActions=")
public class IsothermalEmptyActions extends AbstractAction implements Presenter.Toolbar{

    @Override
    public void actionPerformed(ActionEvent e) {
        
    }

    @Override
    public Component getToolbarPresenter() {
        JButton ret = new JButton();       
        ret.setAction(new GibsonAssemblyAction("Isothermal"));        
        ret.setVerticalTextPosition(SwingConstants.BOTTOM);
        ret.setHorizontalTextPosition(SwingConstants.CENTER);        
        return ret;
    }
    
}
