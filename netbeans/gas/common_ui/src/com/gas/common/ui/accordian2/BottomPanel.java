/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.accordian2;

import com.gas.common.ui.button.FlatBtn;
import com.gas.common.ui.misc.CompLocComparator;
import com.gas.common.ui.util.UIUtil;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.swing.Icon;
import javax.swing.JPanel;

/**
 *
 * @author dq
 */
class BottomPanel extends JPanel {

    CenterPanel centerPanel;
    DockedPanel dockedPanel;
    Integer rowHeight;

    BottomPanel() {
        setLayout(new BorderLayout());
        centerPanel = new CenterPanel();
        centerPanel.setLayout(new GridBagLayout());
        add(centerPanel, BorderLayout.CENTER);

        dockedPanel = new DockedPanel();
        add(dockedPanel, BorderLayout.SOUTH);
    }

    void removeBarPanel(String name) {
        centerPanel.removeBarPanel(name);
    }

    void shrink(int num) {
        while (num > 0) {
            Component[] comps = centerPanel.getComponents();
            if (comps.length == 0) {
                return;
            }
            boolean showing = UIUtil.areShowing(comps);
            if (!showing) {
                return;
            }
            Arrays.sort(comps, new CompLocComparator());
            BarPanel last = (BarPanel) comps[comps.length - 1];
            centerPanel.remove(last);
            dockedPanel.addBar(last.getIcon(), last.getText());
            num--;
        }
    }

    void expand(int num) {
        List<FlatBtn> comps = dockedPanel.getDockedBtns();
        Collections.sort(comps, new CompLocComparator(false));

        while (num > 0 && comps.size() > 0) {
            FlatBtn flatBtn = comps.remove(0);
            dockedPanel.remove(flatBtn);
            addBar(flatBtn.getActionCommand());

            num--;
        }
    }

    int getRowHeight() {
        if (rowHeight == null) {
            Dimension size = centerPanel.getPreferredSize();
            int count = centerPanel.getComponentCount();
            rowHeight = Math.round(size.height * 1.0f / count);
        }
        return rowHeight;
    }

    @Override
    public Dimension getMinimumSize() {
        return dockedPanel.getMinimumSize();
    }

    void addBar(String barName) {
        if (barName == null) {
            return;
        }
        OutlookPane pane = UIUtil.getParent(this, OutlookPane.class);
        String barNameSelected = pane.topPanel.getBarName();
        Icon icon = pane.iconsMap.get(barName);
        BarPanel barPanel = new BarPanel(barName, icon, barName.equals(barNameSelected), barName.equals(barNameSelected));
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        centerPanel.add(barPanel, c);
    }

    void setSelected(String name) {
        centerPanel.setSelected(name);
    }

    void setExpanded(String name) {
        centerPanel.setExpanded(name);
    }
}

class CenterPanel extends JPanel {

    BarPanel removeBarPanel(String name) {
        BarPanel ret = getBarPanel(name);
        if (ret != null) {
            remove(ret);
        }
        return ret;
    }

    void setExpanded(String name) {
        for (int i = 0; i < getComponentCount(); i++) {
            Component comp = getComponent(i);
            if (comp instanceof BarPanel) {
                BarPanel p = (BarPanel) comp;
                p.setExpanded(p.getText().equals(name));
            }
        }
    }

    void setSelected(String name) {
        for (int i = 0; i < getComponentCount(); i++) {
            Component comp = getComponent(i);
            if (comp instanceof BarPanel) {
                BarPanel p = (BarPanel) comp;
                p.setSelected(p.getText().equals(name));
            }
        }
    }

    BarPanel getBarPanel(String name) {
        BarPanel ret = null;
        for (int i = 0; i < getComponentCount(); i++) {
            Component comp = getComponent(i);
            if (comp instanceof BarPanel) {
                BarPanel p = (BarPanel) comp;
                if (p.getText().equals(name)) {
                    ret = p;
                    break;
                }
            }
        }
        return ret;
    }
}