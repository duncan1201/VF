/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui.alignment.widget;

import com.gas.common.ui.caret.JCaret;
import com.gas.common.ui.util.UIUtil;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

/**
 *
 * @author dq
 */
public class ColumnHeaderUILayout implements LayoutManager {

    @Override
    public void addLayoutComponent(String name, Component comp) {        
    }

    @Override
    public void removeLayoutComponent(Component comp) {        
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        Dimension ret = new Dimension(0,0);
        return ret;
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        Dimension ret = new Dimension(0,0);
        return ret;
    }

    @Override
    public void layoutContainer(Container parent) {
        Dimension size = parent.getSize();
        ColumnHeaderUI ui = (ColumnHeaderUI)parent;
        MSAScroll msaScroll = UIUtil.getParent(ui, MSAScroll.class);
        ui.getColumnHeaderComp().setBounds(0, 0, msaScroll.getViewUI().getMsaComp().getPreferredSize().width, size.height);        
    }
    
}
