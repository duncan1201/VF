package com.gas.main.ui.actions;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import org.openide.actions.GarbageCollectAction;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle;
import org.openide.util.actions.Presenter;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

@ActionID(category = "File",
        id = "com.gas.main.ui.actions.GCToolbar")
@ActionRegistration(displayName = "#CTL_GCToolbar")
@ActionReferences({
    @ActionReference(path = "Toolbars/CUSTOME", position = 3900)
})
@NbBundle.Messages("CTL_GCToolbar=Memory")
public class GCToolbar extends GarbageCollectAction implements Presenter.Toolbar{
    @Override
    public java.awt.Component getToolbarPresenter() {
        Component comp = super.getToolbarPresenter();
        JPanel ret = new JPanel(new BorderLayout());
        ret.setOpaque(false);
        ret.add(comp, BorderLayout.CENTER);                
        
        JLabel label = new JLabel("Memory");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        ret.add(label, BorderLayout.SOUTH);
        return ret;
    }        
}
