/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.actions.exportAsImage;

import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle;
import org.openide.util.actions.Presenter;

@ActionID(category = "File",
        id = "com.gas.main.ui.actions.saveAsImage.XImageActionToolbar")
@ActionRegistration(displayName = "#CTL_XImageActionToolbar")
@ActionReferences({
    @ActionReference(path = "Toolbars/CUSTOME", position = 2805)
})
@NbBundle.Messages("CTL_XImageActionToolbar=Export As Image File...")
public class XImageActionToolbar extends AbstractAction implements Presenter.Toolbar{

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    @Override
    public Component getToolbarPresenter() {
        final JButton ret = new JButton();        
        ret.setAction(new XImageAction());
        ret.setText("Export");
        ret.setVerticalTextPosition(SwingConstants.BOTTOM);
        ret.setHorizontalTextPosition(SwingConstants.CENTER);        
        return ret;        
    }
    
}
