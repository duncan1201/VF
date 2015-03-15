/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.jcomp;

import com.gas.common.ui.misc.CNST;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;

/**
 *
 * @author dunqiang
 */
public class StringListPanel extends JPanel {

    private WeakReference<JScrollPane> scrollPaneRef;
    private WeakReference<InternalPanel> internalPanelRef;

    public StringListPanel() {
        super();
        BorderLayout layout = new BorderLayout();
        setLayout(layout);

        InternalPanel internalPanel = new InternalPanel();
        internalPanelRef = new WeakReference<InternalPanel>(internalPanel);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.getViewport().setBackground(CNST.BG);
        scrollPaneRef = new WeakReference<JScrollPane>(scrollPane);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        scrollPane.setViewportView(internalPanel);


        add(scrollPane, BorderLayout.CENTER);
    }

    public List<String> getSelected() {
        List<String> ret = internalPanelRef.get().getSelected();
        return ret;
    }

    public void setSelections(Set<String> str) {
        internalPanelRef.get().setSelections(str);
    }

    public void addString(String datum) {
        internalPanelRef.get().add(datum);
    }

    private class InternalPanel extends JPanel implements Scrollable {

        private WeakHashMap<String, JCheckBox> data = new WeakHashMap<String, JCheckBox>();
        // for internal use
        Dimension standardSize;

        public InternalPanel() {
            InternalPanelLayout layout = new InternalPanelLayout();
            setLayout(layout);
        }

        public void setSelections(Collection<String> selections) {
            Iterator<String> itr = data.keySet().iterator();
            while (itr.hasNext()) {
                String key = itr.next();
                data.get(key).setSelected(selections.contains(key));
            }
        }

        public void add(String datum) {
            JCheckBox btn = new JCheckBox(datum);
            btn.setSelected(true);
            Dimension pSize = btn.getPreferredSize();
            btn.setSize(pSize);
            boolean makeSameSize = false;
            if (standardSize != null && standardSize.width != pSize.width) {
                makeSameSize = true;
            }

            if (standardSize == null) {
                standardSize = pSize;
            } else if (standardSize.width < pSize.width) {
                standardSize = pSize;
            }

            add(btn);

            if (makeSameSize) {
                _makeSameSize(standardSize);
            }
            data.put(datum, btn);
        }

        public String[] getData() {
            return data.keySet().toArray(new String[data.size()]);
        }

        public List<String> getSelected() {
            List<String> ret = new ArrayList<String>();
            int count = getComponentCount();
            for (int i = 0; i < count; i++) {
                Component comp = getComponent(i);
                if (comp instanceof JCheckBox) {
                    JCheckBox btn = (JCheckBox) comp;
                    if (btn.isSelected()) {
                        ret.add(btn.getText());
                    }
                }
            }
            return ret;
        }

        private void _makeSameSize(Dimension size) {
            int count = getComponentCount();

            for (int i = 0; i < count; i++) {
                Component comp = getComponent(i);
                comp.setPreferredSize(size);
                comp.setSize(size);
            }
        }

        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        @Override
        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
            int ret = 0;
            if (orientation == SwingConstants.VERTICAL) {
                if (getComponentCount() > 0) {
                    Component comp = getComponent(0);
                    int compHeight = comp.getSize().height;
                    int remainder = visibleRect.y % compHeight;
                    if (remainder != 0) {
                        if (direction > 0) { // down
                            ret = compHeight - remainder;
                        } else { // up
                            ret = remainder;
                        }
                    } else {
                        ret = compHeight;
                    }

                } else {
                    ret = 1;
                }
            }
            return ret;
        }

        @Override
        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
            return getScrollableUnitIncrement(visibleRect, orientation, direction) * 2;
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return true;
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            return false;
        }
    }

    private class InternalPanelLayout implements LayoutManager {

        @Override
        public void addLayoutComponent(String name, Component comp) {
        }

        @Override
        public void removeLayoutComponent(Component comp) {
        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            Dimension ret = new Dimension();
            int width = calculatePreferredWidth();
            int height = calculatePreferredHeight(parent);
            ret.height = height;
            ret.width = width;
            return ret;
        }

        protected int calculatePreferredWidth() {
            int width = scrollPaneRef.get().getSize().width;
            JScrollBar scrollBar = scrollPaneRef.get().getVerticalScrollBar();
            if (scrollBar.isShowing()) {
                width -= scrollBar.getSize().width;
            }
            return width;
        }

        protected int calculatePreferredHeight(Container parent) {
            int height = 0;
            int count = parent.getComponentCount();

            if (count > 0) {
                Dimension pSize = parent.getComponent(0).getPreferredSize();
                int perRow = scrollPaneRef.get().getSize().width / pSize.width;
                int row = (int) Math.ceil(count * 1.0 / perRow);
                height = row * pSize.height;
            }
            return height;
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            return preferredLayoutSize(parent);
        }

        @Override
        public void layoutContainer(Container parent) {
            int count = parent.getComponentCount();
            int perRow = 0;
            if (count > 0) {
                Dimension pSize = parent.getComponent(0).getPreferredSize();
                int barWidth = scrollPaneRef.get().getVerticalScrollBar().getSize().width;
                perRow = (scrollPaneRef.get().getSize().width - barWidth) / pSize.width;
            }
            if (perRow == 0) {
                return;
            }

            for (int i = 0; i < count; i++) {
                Component comp = parent.getComponent(i);
                int col = i % perRow; // col is 0-based
                int row = i / perRow; // row is 0-based

                int x = col * comp.getSize().width;
                int y = row * comp.getSize().height;

                comp.setLocation(x, y);
            }
        }
    }
}
