/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.actions;

import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.domain.ui.editor.as.gibson.GibsonAssemblyAction;
import com.gas.main.ui.actions.new_.NewSeqAction;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.util.actions.Presenter;

@ActionID(
        category = "Tools",
        id = "com.gas.main.ui.actions.NewSeqEmptyAction")
@ActionRegistration(
        displayName = "#CTL_NewSeqEmptyAction")
@ActionReferences({
    @ActionReference(path = "Toolbars/CUSTOME", position = 190),
    @ActionReference(path = "Shortcuts", name = "D-N")
})
@Messages("CTL_NewSeqEmptyAction=New...")
public final class NewSeqEmptyAction extends AbstractAction implements Presenter.Toolbar {

    private NewSeqAction newSeqAction;
    
    public NewSeqEmptyAction(){
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {        
        newSeqAction.actionPerformed(e);
    }

    @Override
    public Component getToolbarPresenter() {
        JButton ret = new JButton();
        ret.setToolTipText("New sequence...");
        newSeqAction = new NewSeqAction("New");
        ret.setAction(newSeqAction);        
        ret.setVerticalTextPosition(SwingConstants.BOTTOM);
        ret.setHorizontalTextPosition(SwingConstants.CENTER);        
        return ret;
    }
}
