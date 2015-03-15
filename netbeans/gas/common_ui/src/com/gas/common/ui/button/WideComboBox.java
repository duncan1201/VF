package com.gas.common.ui.button;

import java.awt.Dimension;
import java.util.List;
import javax.swing.*;
import java.util.Vector;

// got this workaround from the following bug: 
//      http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4618607 
public class WideComboBox extends JComboBox {

    public WideComboBox() {
    }

    public WideComboBox(final Object items[]) {
        super(items);
    }

    public WideComboBox(List list) {
        super(list.toArray(new Object[list.size()]));
    }

    public WideComboBox(Vector items) {
        super(items);
    }

    public WideComboBox(ComboBoxModel aModel) {
        super(aModel);
    }
    private boolean layingOut = false;

    public void doLayout() {
        try {
            layingOut = true;
            super.doLayout();
        } finally {
            layingOut = false;
        }
    }

    @Override
    public Dimension getSize() {
        Dimension dim = super.getSize();
        if (!layingOut) {
            dim.width = Math.max(dim.width, getPreferredSize().width);
        }
        return dim;
    }
}