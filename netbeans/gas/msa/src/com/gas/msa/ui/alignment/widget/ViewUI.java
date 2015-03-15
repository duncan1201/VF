/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui.alignment.widget;

import com.gas.common.ui.misc.PosIndicator;
import com.gas.common.ui.caret.JCaret;
import com.gas.common.ui.color.IColorProvider;
import com.gas.common.ui.util.ColorCnst;
import com.gas.domain.core.msa.MSA;
import com.gas.domain.ui.brickComp.BrickComp;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;
import java.lang.ref.WeakReference;
import javax.swing.BorderFactory;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;

/**
 *
 * @author dq
 */
public class ViewUI extends JPanel implements Scrollable {

    private WeakReference<MSA> msaRef;
    private WeakReference<MSAComp> msaCompRef;
    private IColorProvider colorProvider;

    public ViewUI() {
        setBackground(ColorCnst.GRAY_250);
        setOpaque(true);
        setLayout(new BorderLayout());

        MSAComp msaComp = new MSAComp();
        PosIndicator indicator = msaComp.getPosIndicator();
        PosIndicator movingIndicator = msaComp.getMovingIndicator();
        JCaret caret = msaComp.getCaret();
        msaCompRef = new WeakReference<MSAComp>(msaComp);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.add(msaComp, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(caret, JLayeredPane.PALETTE_LAYER);
        layeredPane.add(indicator, JLayeredPane.PALETTE_LAYER);
        layeredPane.add(movingIndicator, JLayeredPane.PALETTE_LAYER);

        add(layeredPane, BorderLayout.CENTER);


        hookupListeners();
    }

    public void setPaintVisibleOnly(boolean paintVisibleOnly) {
        msaCompRef.get().setPaintVisibleOnly(paintVisibleOnly);
    }

    private void hookupListeners() {
        addPropertyChangeListener(new ViewUIListeners.PtyListener());
    }

    public void setColorProvider(IColorProvider colorProvider) {
        IColorProvider old = this.colorProvider;
        this.colorProvider = colorProvider;
        firePropertyChange("colorProvider", old, this.colorProvider);
    }

    void setMsa(MSA msa) {
        MSA old = getMsa();
        msaRef = new WeakReference<MSA>(msa);
        firePropertyChange("msa", old, getMsa());
    }

    private MSA getMsa() {
        if (msaRef == null) {
            return null;
        } else {
            return msaRef.get();
        }
    }

    public MSAComp getMsaComp() {
        return msaCompRef.get();
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        int ret = 2;
        if (orientation == SwingConstants.HORIZONTAL) {
            Insets insets = getMsaComp().getInsets();
            Rectangle panelVisibleRect = getMsaComp().getVisibleRect();

            float unitIncrement = getMsaComp().calculateUnitWidth();

            float quotient = (panelVisibleRect.x - insets.left) / unitIncrement;
            double lowerLimit = Math.floor(quotient) + 0.25;
            double upperLimit = Math.floor(quotient) + 0.75;
            boolean fuzzy;
            if (quotient > lowerLimit && quotient < upperLimit) {
                fuzzy = true;
            } else {
                fuzzy = false;
            }
            double reminder = 0;
            if (fuzzy) {
                reminder = Math.round((quotient - Math.floor(quotient)) * unitIncrement);
            }
            if (direction < 0) { // left
                if (fuzzy) {
                    ret = (int) reminder;
                } else {
                    float prevX = Math.round(quotient - 1) * unitIncrement + insets.left;
                    ret = Math.round(visibleRect.x - prevX);
                }
            } else { // right
                if (fuzzy) {
                    ret = (int) Math.round(unitIncrement - reminder);
                } else {
                    float nextX = Math.round(quotient + 1) * unitIncrement + insets.left;
                    ret = Math.round(nextX - visibleRect.x);
                }
            }
        }
        return ret;
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 5 * getScrollableUnitIncrement(visibleRect, orientation, direction);
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return false;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }
}
