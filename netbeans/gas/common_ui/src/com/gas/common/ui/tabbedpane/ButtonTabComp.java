package com.gas.common.ui.tabbedpane;

import com.gas.common.ui.util.UIUtil;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import javax.swing.plaf.basic.BasicButtonUI;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * Component to be used as tabComponent; Contains a JLabel to show the text and
 * a JButton to close the tab it belongs to
 */
public class ButtonTabComp extends JPanel {

    private final JTabbedPane pane;
    private int i;
    private JButton button;

    public ButtonTabComp(int i, final JTabbedPane pane) {
        //unset default FlowLayout' gaps
        super(new FlowLayout(FlowLayout.LEFT, 0, 0));
        if (pane == null) {
            throw new NullPointerException("TabbedPane is null");
        }
        this.i = i;

        this.pane = pane;
        setOpaque(false);

        //tab button
        button = new TabButton() {
            @Override
            public String getText() {
                int i = pane.indexOfTabComponent(ButtonTabComp.this);
                if (i != -1) {
                    return pane.getTitleAt(i);
                }
                return null;
            }

            @Override
            public Icon getIcon() {
                int i = pane.indexOfTabComponent(ButtonTabComp.this);
                if (i != -1) {
                    return pane.getIconAt(i);
                }
                return null;
            }

            @Override
            public String getToolTipText() {
                int i = pane.indexOfTabComponent(ButtonTabComp.this);
                if (i != -1) {
                    return pane.getToolTipTextAt(i);
                }
                return null;
            }
        };
        add(button);
        //add more space to the top of the component
        setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
    }

    public JButton getButton() {
        return button;
    }

    private class TabButton extends JButton {

        public TabButton() {
            //Make the button looks the same for all Laf's
            setUI(new BasicButtonUI());
            //Make it transparent
            setContentAreaFilled(false);
            //No need to be focusable
            setFocusable(false);
            setBorder(BorderFactory.createEmptyBorder());
            setBorderPainted(false);
            //Making nice rollover effect
            //we use the same listener for all buttons
            setRolloverEnabled(false);
        }

        private Dimension getWidest() {
            List<Dimension> sizes = new ArrayList<Dimension>();
            int count = pane.getTabCount();
            for (int i = 0; i < count; i++) {
                Dimension size = pane.getComponentAt(i).getPreferredSize();
                sizes.add(size);
            }

            Dimension widest = UIUtil.widest(sizes);
            return widest;
        }

        //we don't want to update UI for this button
        public void updateUI() {
        }
    }
}
