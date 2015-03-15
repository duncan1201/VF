/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.tigr.core.ui.editor.shared;

import com.gas.common.ui.core.VariantMapMdl;
import com.gas.common.ui.jcomp.IOverlayParent;
import com.gas.common.ui.jcomp.Overlay;
import com.gas.common.ui.util.MathUtil;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

/**
 *
 * @author dq
 */
public class MiniMap extends JPanel implements IOverlayParent {

    private VariantsMap variantsMap;
    private Overlay overlay;
    private Overlay eastOverlay;
    private Overlay westOverlay;
    Float overlayStart;
    Float overlayWidth;
    protected JPanel cardPanel;
    protected JLayeredPane layeredPane;

    public MiniMap() {
        LayoutManager layout = null;
        layout = new BorderLayout();
        setLayout(layout);


        variantsMap = new VariantsMap();
        cardPanel = new JPanel(new CardLayout());
        cardPanel.add(variantsMap, "aaa");

        eastOverlay = new Overlay(0.6f, 0.5f);

        westOverlay = new Overlay(0.6f, 0.5f);

        overlay = new Overlay(0.0f, 1.0f);
        overlay.setEastBorder(true);
        overlay.setWestBorder(true);
        overlay.setNorthBorder(true);
        overlay.setSouthBorder(true);
        overlay.setDraggable(true);
        overlay.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                Dimension size = MiniMap.this.getSize();
                String pName = evt.getPropertyName();
                if (pName.equals("bounds")) {
                    Rectangle rect = (Rectangle) evt.getNewValue();
                    float x = 1.0f * rect.x / size.width;
                    setOverlayStart(x);
                }
            }
        });

        layeredPane = new JLayeredPane();
        layeredPane.add(cardPanel);
        layeredPane.setLayer(cardPanel, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(eastOverlay);
        layeredPane.setLayer(eastOverlay, JLayeredPane.PALETTE_LAYER);
        layeredPane.add(westOverlay);
        layeredPane.setLayer(westOverlay, JLayeredPane.PALETTE_LAYER);
        layeredPane.add(overlay);
        layeredPane.setLayer(overlay, JLayeredPane.PALETTE_LAYER);
        add(layeredPane, BorderLayout.CENTER);

        hookupListeners();
    }

    private void hookupListeners() {
        addComponentListener(new MiniMapListeners.CompAdpt());

        Toolkit.getDefaultToolkit().addAWTEventListener(
                new MiniMapListeners.AwtListener(this), AWTEvent.MOUSE_EVENT_MASK);

        addPropertyChangeListener(new MiniMapListeners.PtyListener());
    }

    void updateOverlayPositions() {
        if (overlayStart != null && overlayWidth != null && overlayStart >= 0) {

            Dimension size = getSize();

            final int x = MathUtil.round(size.width * overlayStart);
            final int width = MathUtil.round(size.width * overlayWidth);
            overlay.setBounds(x, 0, width, size.height);

            int eastX = x + width;
            int eastWidth = size.width - x + width;
            eastOverlay.setBounds(eastX, 0, eastWidth, size.height);

            int westWidth = x;
            westOverlay.setBounds(0, 0, westWidth, size.height);
        }
    }

    public void setOverlayStart(Float overlayStart) {
        Float old = this.overlayStart;
        this.overlayStart = overlayStart;
        firePropertyChange("overlayStart", old, this.overlayStart);
    }

    public void setOverlayWidth(Float overlayWidth) {
        Float old = this.overlayWidth;
        this.overlayWidth = overlayWidth;
        firePropertyChange("overlayWidth", old, this.overlayWidth);
    }

    public void setVariantMapMdl(VariantMapMdl mdl) {
        variantsMap.setMdl(mdl);
    }
}
