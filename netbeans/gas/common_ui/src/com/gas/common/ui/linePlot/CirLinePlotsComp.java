/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.linePlot;

import com.gas.common.ui.IVisibleLocProvider;
import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.util.Pref;
import com.gas.common.ui.util.UIUtil;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.JComponent;

/**
 *
 * @author dq
 */
public class CirLinePlotsComp extends JComponent {

    public static final String GC = LinePlotCompMap.GC;
    private Map<String, CirLinePlot> plots = new HashMap<String, CirLinePlot>();
    private int width = 40;
    private float startOffset;

    public CirLinePlotsComp() {
        hookupListeners();
    }

    private void hookupListeners() {
        addPropertyChangeListener(new CirLinePlotsCompListeners.PtyListener());
    }

    @Override
    public void paintComponent(Graphics g) {
        final Dimension size = getSize();
        if (size.getWidth() <= 0 || size.getHeight() <= 0) {
            return;
        }
        final Rectangle bounds = new Rectangle(0, 0, size.width, size.height);

        IVisibleLocProvider provider = UIUtil.getParent(this, IVisibleLocProvider.class);
        Loc loc = provider.calculateVisibleLoc();
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int offset = 0;
        Iterator<String> itr = plots.keySet().iterator();
        while (itr.hasNext()) {
            String str = itr.next();
            CirLinePlot plot = plots.get(str);
            Rectangle rect = UIUtil.shrink(bounds, offset, offset);
            Rectangle2D.Double rect2d = new Rectangle2D.Double(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
            if (str.equals(GC)) {
                plot.setRingWidth(Pref.CommonPtyPrefs.getInstance().getGCHeight());
            } else {
                plot.setRingWidth(width);
            }
            plot.setStartOffset(startOffset);
            plot.paint(g2d, rect2d, loc.getStart(), loc.getEnd());

            offset += plot.getRingWidth();
        }
    }

    public void clear() {
        plots.clear();
    }

    public void setStartOffet(float startOffset) {
        float old = this.startOffset;
        this.startOffset = startOffset;
        firePropertyChange("startOffset", old, this.startOffset);
    }

    public float getStartOffset() {
        return this.startOffset;
    }

    protected Map<String, CirLinePlot> getPlots() {
        return plots;
    }

    public boolean isEmpty() {
        return plots.isEmpty();
    }

    public CirLinePlot getGC() {
        CirLinePlot ret = plots.get(GC);
        return ret;
    }

    public void setGC(CirLinePlot c) {
        plots.put(GC, c);
    }

    public int getDesiredRingWidth() {
        int ret = 0;
        Iterator<String> itr = plots.keySet().iterator();
        while (itr.hasNext()) {
            String str = itr.next();
            if (str.equals(GC)) {
                ret += Pref.CommonPtyPrefs.getInstance().getGCHeight();
            } else {
                ret += width;
            }
        }
        return ret;
    }
}