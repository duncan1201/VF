/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.action;

import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.util.UIUtil;
import com.gas.main.ui.molpane.MolPane;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JComponent;

/**
 *
 * @author dq
 */

    public class SelectAllAction extends AbstractAction {

        JComponent comp;

        public SelectAllAction(JComponent comp) {
            this.comp = comp;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            MolPane molPane = UIUtil.getParent(comp, MolPane.class);
            if (molPane == null) {
                return;
            }
            Integer length = molPane.getAs().getLength();
            molPane.setSelection(new Loc(1, length));
        }
    }

