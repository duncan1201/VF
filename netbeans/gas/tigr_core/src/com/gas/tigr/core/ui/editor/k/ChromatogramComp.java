/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.tigr.core.ui.editor.k;

import com.gas.common.ui.color.IColorProvider;
import com.gas.common.ui.color.TraceColorProvider;
import com.gas.common.ui.util.ColorCnst;
import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.tigr.Kromatogram;
import com.gas.domain.core.tigr.Trace;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.swing.JComponent;
import org.biojava.bio.seq.DNATools;

/**
 *
 * @author dq
 */
public class ChromatogramComp extends JComponent {

    static Logger log = Logger.getLogger(ChromatogramComp.class.getName());
    Kromatogram kromatogram;
    private boolean adenineVisible = true;
    private boolean guanineVisible = true;
    private boolean cytosineVisible = true;
    private boolean thymineVisible = true;
    private boolean gridVisible = true;
    private IColorProvider colorProvider = new TraceColorProvider();
    final static Font FONT_BASE = FontUtil.getDefaultFont();
    final static FontMetrics FM_FONT_BASE = FontUtil.getFontMetrics(FONT_BASE);
    final static Font FONT_MARK = FONT_BASE.deriveFont(FontUtil.getSmallFontSize(0.82f));
    final static FontMetrics FM_FONT_MARK = FontUtil.getFontMetrics(FONT_MARK);

    public ChromatogramComp() {
        initComp();
        hookupListeners();
    }

    private void initComp() {
        UIUtil.setDefaultBorder(this);
    }

    private void hookupListeners() {
        addPropertyChangeListener(new ChromatogramCompListeners.PtyListener());
    }

    public boolean isAdenineVisible() {
        return adenineVisible;
    }

    public void setAdenineVisible(boolean adenineVisible) {
        this.adenineVisible = adenineVisible;
    }

    public boolean isGuanineVisible() {
        return guanineVisible;
    }

    public void setGuanineVisible(boolean guanineVisible) {
        this.guanineVisible = guanineVisible;
    }

    public boolean isCytosineVisible() {
        return cytosineVisible;
    }

    public void setCytosineVisible(boolean cytosineVisible) {
        this.cytosineVisible = cytosineVisible;
    }

    public boolean isThymineVisible() {
        return thymineVisible;
    }

    public void setThymineVisible(boolean thymineVisible) {
        this.thymineVisible = thymineVisible;
    }

    @Override
    public void paintComponent(Graphics g) {
        if (kromatogram == null) {
            return;
        }
        Graphics2D g2d = (Graphics2D) g;

        final Integer maxPeak = kromatogram.getMaxPeak();

        final Dimension size = getSize();
        final Insets insets = getInsets();
        final int traceFullHeight = size.height - insets.top - insets.right - FM_FONT_BASE.getHeight() - FM_FONT_MARK.getHeight();
        final int displayableWidth = size.width - insets.left - insets.right;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, size.width, size.height);

        List<Integer> qvs = kromatogram.getQualityValues();
        final Map<String, Trace> traces = kromatogram.getTraces();
        final Map<String, Color> colors = new HashMap<String, Color>();
        colors.put(kromatogram.getTrace4A().getName(), ColorCnst.PAKISTAN_GREEN);
        colors.put(kromatogram.getTrace4T().getName(), Color.RED);
        colors.put(kromatogram.getTrace4C().getName(), Color.BLUE);
        colors.put(kromatogram.getTrace4G().getName(), Color.BLACK);

        List<Integer> offsets = kromatogram.getOffsets();
        Integer traceLength = kromatogram.getTraceLength();
        
        // draw grids
        g2d.setColor(Color.LIGHT_GRAY);
        int y1 = size.height - FM_FONT_MARK.getHeight() - FM_FONT_BASE.getHeight() - insets.bottom;
        int y2 = insets.top;
        for (int i = 0; i < offsets.size(); i++) {
            int offset = offsets.get(i);
            int x = offset * displayableWidth / traceLength + insets.left;
            g2d.drawLine(x, y1, x, y2);
        }
        
        Map<String, GeneralPath> paths = new HashMap<String, GeneralPath>();        
        Iterator<String> itr = traces.keySet().iterator();
        while (itr.hasNext()) {
            String nameTrace = itr.next();
            if (nameTrace.equals(DNATools.a().getName()) && !isAdenineVisible()) {
                continue;
            }
            if (nameTrace.equals(DNATools.c().getName()) && !isCytosineVisible()) {
                continue;
            }
            if (nameTrace.equals(DNATools.t().getName()) && !isThymineVisible()) {
                continue;
            }
            if (nameTrace.equals(DNATools.g().getName()) && !isGuanineVisible()) {
                continue;
            }
            Trace trace = traces.get(nameTrace);
            GeneralPath path = new GeneralPath();
            for (int i = 0; i < traceLength; i++) {
                int d = trace.getData()[i];
                float traceHeight = 1.0f * d / maxPeak * traceFullHeight;

                final float x = i * displayableWidth / traceLength + insets.left;
                final float y = size.height - traceHeight - insets.bottom - FM_FONT_BASE.getHeight() - FM_FONT_MARK.getHeight();
                if (path.getCurrentPoint() == null) {
                    path.moveTo(0, y);
                    path.lineTo(x, y);
                } else {
                    path.lineTo(x, y);
                }
            }
            paths.put(nameTrace, path);
        }
        itr = paths.keySet().iterator();
        while (itr.hasNext()) {
            final String key = itr.next();
            GeneralPath path = paths.get(key);
            Color c2 = colorProvider.getColor(key);
            //g2d.setColor(colors.get(key));
            g2d.setColor(c2);
            g2d.draw(path);
        }

        g2d.setFont(FONT_MARK);
        g2d.setColor(Color.BLACK);
        for (int i = 0; i < offsets.size(); i++) {
            int offset = offsets.get(i);
            final String mark = (i + 1) + "";

            float xMark = offset * displayableWidth / traceLength - FM_FONT_MARK.stringWidth(mark) * 0.5f + insets.left;
            float y = size.height - (FM_FONT_MARK.getHeight() - FM_FONT_MARK.getAscent()) - insets.bottom;

            g2d.drawString(mark, xMark, y);
        }

        List<Character> bases = kromatogram.getBases();
        g2d.setFont(FONT_BASE);
        for (int i = 0; i < bases.size(); i++) {
            int offset = offsets.get(i);
            final Character base = bases.get(i);
            if (base == 'A' || base == 'a') {
                Color color = colors.get(DNATools.a().getName());
                g2d.setColor(color);
            } else if (base == 'T' || base == 't') {
                Color color = colors.get(DNATools.t().getName());
                g2d.setColor(color);
            } else if (base == 'C' || base == 'c') {
                Color color = colors.get(DNATools.c().getName());
                g2d.setColor(color);
            } else if (base == 'G' || base == 'g') {
                Color color = colors.get(DNATools.g().getName());
                g2d.setColor(color);
            }
            float xBase = offset * displayableWidth / traceLength - FM_FONT_MARK.charWidth(base) * 0.5f + insets.left;
            float y = size.height - FM_FONT_MARK.getHeight() - (FM_FONT_BASE.getHeight() - FM_FONT_BASE.getAscent()) - insets.bottom;

            g2d.drawString(base.toString(), xBase, y);
        }
    }

    public void setKromatogram(Kromatogram kromatogram) {
        Kromatogram old = this.kromatogram;
        this.kromatogram = kromatogram;
        firePropertyChange("kromatogram", old, this.kromatogram);
    }

    int getBaseline() {
        Insets insets = getInsets();
        int ret = FM_FONT_BASE.getHeight();
        ret += FM_FONT_MARK.getHeight();
        ret += insets.bottom;
        return ret;
    }
}