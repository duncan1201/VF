/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui;

import com.gas.common.ui.util.FontUtil;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.JSeparator;
import org.openide.util.actions.Presenter;

/**
 *
 * @author dq
 */
public class ToolbarSpacer extends AbstractAction implements Presenter.Toolbar {

    private boolean visible;

    public ToolbarSpacer() {
        this(true);
    }

    public ToolbarSpacer(boolean visible) {
        this.visible = visible;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    @Override
    public Component getToolbarPresenter() {
        if (visible) {
            JSeparator separator = new JSeparator();
            separator.setOrientation(JSeparator.VERTICAL);
            return separator;
        } else {
            FontMetrics fm = FontUtil.getDefaultFontMetrics();
            Component ret = Box.createRigidArea(new Dimension(fm.getHeight() / 2, fm.getHeight() / 2));
            return ret;
        }
    }
}
