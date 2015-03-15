/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.jcomp;

import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 *
 * @author dunqiang
 */
public class IconBar extends JPanel {

    private ActionListener listener = new InternalActionListener();

    public IconBar() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    @Override
    public Component add(Component c) {
        Component ret = null;
        ret = super.add(c);
        IconButton ib = (IconButton) c;
        ib.addActionListener(listener);
        return ret;
    }

    public void setTopEmptyBorder(int width) {
        Insets insets = getInsets();
        setBorder(BorderFactory.createEmptyBorder(width, insets.left, insets.bottom, insets.right));
    }

    private class InternalActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            IconButton obj = (IconButton) e.getSource();
            int count = IconBar.this.getComponentCount();
            for (int i = 0; i < count; i++) {
                IconButton comp = (IconButton) IconBar.this.getComponent(i);
                if (comp != obj) {
                    comp.setChosen(false);
                }
            }
            obj.setChosen(true);
        }
    }
}