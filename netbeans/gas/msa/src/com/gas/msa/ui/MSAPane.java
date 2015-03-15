/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui;

import com.gas.common.ui.tabbedpanel.TabbedPanel;
import com.gas.domain.core.msa.MSA;
import com.gas.domain.ui.editor.IZoomable;
import com.gas.domain.ui.editor.zoom.ZoomPanel;
import com.gas.msa.ui.alignment.AlignPane;
import com.gas.msa.ui.tree.TreePane;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.lang.ref.WeakReference;
import javax.swing.JPanel;
import javax.swing.JViewport;
import javax.swing.SwingConstants;

/**
 *
 * @author dq
 */
public class MSAPane extends JPanel implements IZoomable {

    private WeakReference<MSA> msaRef;
    private AlignPane alignPane;
    private TreePane treePane;
    ZoomPanel zoomPanel;
    TabbedPanel tabbedPanel;

    public MSAPane() {
        initComponents();

        hookupListeners();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        tabbedPanel = new TabbedPanel(SwingConstants.BOTTOM);
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.EAST;
        zoomPanel = new ZoomPanel(null);
        tabbedPanel.setDecorator(zoomPanel, c);

        alignPane = new AlignPane();
        tabbedPanel.addTab("Alignment", null, alignPane);

        add(tabbedPanel, BorderLayout.CENTER);
    }

    protected void initTreePane() {
        treePane = new TreePane();
        tabbedPanel.addTab("Tree", null, treePane);
    }

    protected TreePane getTreePane() {
        return treePane;
    }

    protected AlignPane getAlignPane() {
        return alignPane;
    }

    private void hookupListeners() {
        addPropertyChangeListener(new MSAPaneListeners.PtyListener());
        tabbedPanel.addChangeListener(new MSAPaneListeners.TabbedPanelSelectListener());
    }

    public void setMsa(MSA msa) {
        MSA old = getMsa();
        msaRef = new WeakReference<MSA>(msa);
        firePropertyChange("msa", old, getMsa());
    }

    MSA getMsa() {
        if (msaRef == null) {
            return null;
        } else {
            return msaRef.get();
        }
    }

    @Override
    public void setZoom(int zoom) {
        if (msaRef != null && msaRef.get() != null) {
            msaRef.get().getMsaSetting().setZoom(zoom);
        }
    }

    @Override
    public boolean canZoomOut(int zoom) {
        JViewport viewport = getAlignPane().getMsaScroll().getViewport();
        int width = viewport.getWidth();
        int widthP = getAlignPane().getMsaScroll().getViewUI().getMsaComp().getPreferredSize().width;
        int widthNormal = getAlignPane().getMsaScroll().getViewUI().getMsaComp().calculateNormalWidth();
        
        return widthP > width;
    }
}
