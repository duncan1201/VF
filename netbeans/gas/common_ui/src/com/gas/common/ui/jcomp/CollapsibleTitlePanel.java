/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.jcomp;

import com.gas.common.ui.IReclaimable;
import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.util.UIUtil;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.jdesktop.swingx.JXTitledSeparator;

/**
 *
 * @author dunqiang
 */
public class CollapsibleTitlePanel extends JPanel implements IReclaimable {

    public final static String TOGGLE_ACTION = "toggle";
    private boolean collapsed = false;
    private String title;
    private Icon collapsedIcon;
    private Icon expandedIcon;
    private IconButton titleComp;
    private JXTitledSeparator separator;
    private JPanel contentPanel;
    private WeakReference<JPanel> titlePanelRef;

    public CollapsibleTitlePanel() {
        this("");
    }

    public CollapsibleTitlePanel(String title) {
        super(new BorderLayout());

        getActionMap().put(TOGGLE_ACTION, new ToggleAction());

        // title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanelRef = new WeakReference<JPanel>(titlePanel);
        titlePanel.addMouseListener(new MouseAdpt());

        //titleComp.setText("Title");
        createDefaultIcons();
        titleComp = new IconButton(getIcon(), 1);
        titleComp.setIcon(getIcon(), 1);
        //titleComp.setHorizontalAlignment(SwingConstants.LEFT);

        titlePanel.add(titleComp, BorderLayout.WEST);
        separator = new JXTitledSeparator(title);
        titlePanel.add(separator, BorderLayout.CENTER);

        titleComp.addActionListener(getActionMap().get(TOGGLE_ACTION));
        add(titlePanel, BorderLayout.NORTH);

        // content panel
        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());

        add(contentPanel, BorderLayout.CENTER);
    }

    @Override
    public void setEnabled(boolean enable) {
        super.setEnabled(enable);
        UIUtil.enabledRecursively(contentPanel, enable);
    }

    public void setRightDecoratioin(JComponent c) {
        titlePanelRef.get().add(c, BorderLayout.EAST);
    }

    @Override
    public void cleanup() {
        collapsedIcon = null;
        expandedIcon = null;
        titleComp = null;
        separator = null;
        contentPanel = null;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        separator.setTitle(title);
    }

    public JPanel getContentPane() {
        return contentPanel;
    }

    public void setContentPane(JPanel panel) {
        contentPanel = panel;
        add(panel, BorderLayout.CENTER);
        revalidate();
    }

    private Icon getIcon() {
        if (collapsed) {
            return collapsedIcon;
        } else {
            return expandedIcon;
        }
    }

    private void createDefaultIcons() {
        collapsedIcon = ImageHelper.createImageIcon(ImageNames.ARROW_COLLAPSED_16);
        expandedIcon = ImageHelper.createImageIcon(ImageNames.ARROW_EXPANDED_16);
    }

    public boolean isCollapsed() {
        return collapsed;
    }

    public void setCollapsed(boolean collapsed) {
        if (collapsed == this.collapsed) {
            return;
        }

        if (collapsed) {
            remove(contentPanel);
        } else {
            //if (!isAncestorOf(contentPanel)) {
            add(contentPanel, BorderLayout.CENTER);
            //}
        }
        revalidate();
        repaint();

        Dimension tSize = titlePanelRef.get().getPreferredSize();
        Dimension cSize = contentPanel.getPreferredSize();
        if (collapsed) {
            UIUtil.setPreferredHeight(this, tSize.height);
        } else {
            UIUtil.setPreferredHeight(this, tSize.height + cSize.height);
        }

        boolean old = this.collapsed;
        this.collapsed = collapsed;
        firePropertyChange("collapsed", old, collapsed);
    }

    private class MouseAdpt extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            ToggleAction a = new ToggleAction();
            a.actionPerformed(null);
        }
    }

    private class ToggleAction extends AbstractAction implements PropertyChangeListener {

        public ToggleAction() {
            super();
            CollapsibleTitlePanel.this.addPropertyChangeListener("collapsed", this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            CollapsibleTitlePanel.this.setCollapsed(!isCollapsed());
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            titleComp.setIcon(getIcon());
        }
    }
}
