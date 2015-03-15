/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.actions.print;

import com.gas.common.ui.util.UIUtil;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
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
        id = "com.gas.main.ui.actions.print.PrintActionToolbar")
@ActionRegistration(displayName = "#CTL_PrintActionToolbar")
@ActionReferences({
    @ActionReference(path = "Toolbars/CUSTOME", position = 2800)
})
@NbBundle.Messages("CTL_PrintActionToolbar=Print")
public class PrintActionToolbar extends AbstractAction implements Presenter.Toolbar {

    @Override
    public Component getToolbarPresenter() {
        final Insets insets = UIUtil.getDefaultInsets();
        final JButton ret = new JButton();
        PrintAction p = new PrintAction();
        ret.setAction(p);
        ret.setText("Print");
        ret.setVerticalTextPosition(SwingConstants.BOTTOM);
        ret.setHorizontalTextPosition(SwingConstants.CENTER);
        Dimension pSize = ret.getPreferredSize();
        UIUtil.setPreferredHeight(ret, pSize.height + insets.top);
        return ret;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
}
