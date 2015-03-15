/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui.alignment.widget;

import com.gas.common.ui.light.Histogram;
import com.gas.common.ui.light.VerticalRuler;
import com.gas.common.ui.util.ColorCnst;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.msa.MSA;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.lang.ref.WeakReference;
import javax.swing.JComponent;

/**
 *
 * @author dq
 */
public class CornerUI extends JComponent {

    private MSAScroll msaScroll;
    VerticalRuler consensusRuler;
    private VerticalRuler coverageRuler;
    private VerticalRuler seqLogoRuler;
    private VerticalRuler qualityRuler;

    public CornerUI() {
        consensusRuler = new VerticalRuler();
        consensusRuler.setLabel("Consensus");

        seqLogoRuler = new VerticalRuler();
        seqLogoRuler.setLabel("Seq logo");
        seqLogoRuler.setStartPos(0);

        coverageRuler = new VerticalRuler();
        coverageRuler.setLabel("Coverage");

        qualityRuler = new VerticalRuler();
        qualityRuler.setLabel("Quality Scores");

        hookupListeners();
    }      
    
    void resetPreferredWidth(){
        Integer width = calculatePreferredWidth();
        UIUtil.setPreferredWidth(this, width);
    }
    
    Integer calculatePreferredWidth(){
        Integer ret = null;
        VerticalRuler[] rulers = {consensusRuler, coverageRuler, seqLogoRuler, qualityRuler};
        for(VerticalRuler ruler: rulers){
            int width = ruler.calculatePreferredWidth();
            if(ret == null || ret < width){
                ret = width;
            }
        }
        return ret;
    }

    void selectedConsensus(boolean selected) {
        consensusRuler.setSelected(selected);
        repaint();
    }

    private void hookupListeners() {
    }

    @Override
    public void paintComponent(Graphics g) {
        Dimension size = getSize();
        Graphics2D g2d = (Graphics2D) g;

        g.setColor(ColorCnst.GRAY_250);
        g.fillRect(0, 0, size.width, size.height);

        g.setColor(Color.BLACK);

        paintConsensusRuler(g2d);
        paintSeqLogoRuler(g2d);
        paintCoverageRuler(g2d);
        paintQualityRuler(g2d);
    }

    private void paintConsensusRuler(Graphics2D g2d) {
        MSAScroll msaScroll = getMSAScroll();
        ColumnHeaderComp columnHeaderComp = msaScroll.getColumnHeaderUI().getColumnHeaderComp();
        Rectangle2D.Double consensusRect = columnHeaderComp.getConsensusList().getRect();
        if (consensusRect == null) {
            return;
        }
        Dimension size = getSize();
        consensusRuler.setRect(new Rectangle2D.Double(0, consensusRect.getY(), size.width, consensusRect.getHeight()));
        consensusRuler.paint(g2d, consensusRuler.getRect());
    }

    private void paintQualityRuler(Graphics2D g2d) {
        MSAScroll msaScroll = getMSAScroll();
        ColumnHeaderComp columnHeaderComp = msaScroll.getColumnHeaderUI().getColumnHeaderComp();
        Histogram qualityPlot = columnHeaderComp.getQualityPlot();
        if (qualityPlot == null) {
            return;
        }
        Rectangle2D.Double qualityRect = qualityPlot.getRect();
        if (!getMSAScroll().getMsa().getMsaSetting().isQualityDisplay() || qualityRect == null) {
            return;
        }

        Dimension size = getSize();
        qualityRuler.setRect(new Rectangle2D.Double(0, qualityRect.getY(), size.getWidth(), qualityRect.getHeight()));
        qualityRuler.paint(g2d, qualityRuler.getRect());
    }

    private void paintCoverageRuler(Graphics2D g2d) {
        MSAScroll msaScroll = getMSAScroll();
        MSA msa = msaScroll.getMsa();
        ColumnHeaderComp columnHeaderComp = msaScroll.getColumnHeaderUI().getColumnHeaderComp();
        Rectangle2D.Double coveragePlotRect = columnHeaderComp.getCoveragePlot().getRect();
        if (!getMSAScroll().getMsa().getMsaSetting().isCoverageDisplay() || coveragePlotRect == null) {
            return;
        }
        Dimension size = getSize();

        int coverage = msa.getEntriesCount();
        coverageRuler.setStartPos(0);
        coverageRuler.setEndPos(coverage);
        coverageRuler.setTicksPos(new int[]{0, coverage});
        coverageRuler.setPositions(new int[]{0, coverage});
        coverageRuler.setRect(new Rectangle2D.Double(0, coveragePlotRect.getY(), size.getWidth(), coveragePlotRect.getHeight()));
        coverageRuler.paint(g2d, 0, coverage);
    }

    private void paintSeqLogoRuler(Graphics2D g2d) {
        final Dimension size = getSize();
        MSAScroll msaScroll = getMSAScroll();
        ColumnHeaderComp columnHeaderComp = msaScroll.getColumnHeaderUI().getColumnHeaderComp();
        Rectangle2D seqLogoRect = columnHeaderComp.getSeqLogoUI().getRect();
        if (seqLogoRect == null || seqLogoRect.getHeight() == 0 || !getMSAScroll().getMsa().getMsaSetting().isSeqLogoDisplay()) {
            return;
        }
        Rectangle2D.Double rect = new Rectangle2D.Double(0, seqLogoRect.getY(), size.width, seqLogoRect.getHeight());
        MSA msa = msaScroll.getMsa();
        if (msa == null) {
            return;
        }
        boolean dna = msa.isDnaByGuess();
        if (dna) {
            seqLogoRuler.setEndPos(2);
            seqLogoRuler.setPositions(new int[]{0, 1, 2});
            seqLogoRuler.setTicksPos(new int[]{0, 1, 2});
            seqLogoRuler.setRect(rect);
            seqLogoRuler.paint(g2d, 0, 2);
        } else {
            seqLogoRuler.setEndPos(4);
            seqLogoRuler.setPositions(new int[]{0, 1, 2, 3, 4});
            seqLogoRuler.setTicksPos(new int[]{0, 1, 2, 3, 4});
            seqLogoRuler.setRect(rect);
            seqLogoRuler.paint(g2d, 0, 4);
        }
    }

    private MSAScroll getMSAScroll() {
        if (msaScroll == null) {
            msaScroll = UIUtil.getParent(this, MSAScroll.class);
        }
        return msaScroll;
    }
}
