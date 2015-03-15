/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.phylo.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.util.actions.Presenter;

/**
 *
 * @author dq
 */
@ActionID(category = "Tools",
        id = "com.gas.phylo.actions.TreeEmptyAction")
@ActionRegistration(displayName = "#CTL_TreeEmptyAction")
@ActionReferences({
    @ActionReference(path = "Toolbars/CUSTOME", position = 3350)
})
@Messages("CTL_TreeEmptyAction=Tree")
public class TreeEmptyAction extends AbstractAction implements Presenter.Toolbar {

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    @Override
    public Component getToolbarPresenter() {
        JButton ret = new JButton();       
        ret.setAction(new TreeAction());
        ret.setVerticalTextPosition(SwingConstants.BOTTOM);
        ret.setHorizontalTextPosition(SwingConstants.CENTER);        
        return ret;
    }
}
