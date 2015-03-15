/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.tigr.core.ui.editor.shared;

import com.gas.common.ui.light.VerticalRuler;
import com.gas.common.ui.util.ColorCnst;
import com.gas.common.ui.util.UIUtil;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.JComponent;

/**
 *
 * @author dq
 */
public class CornerUI extends JComponent {

    AssemblyScroll scrollPane;
    VerticalRuler consensusRuler;
    VerticalRuler qualityRuler;

    public CornerUI() {
        consensusRuler = new VerticalRuler();        
        consensusRuler.setLabel("Consensus");

        qualityRuler = new VerticalRuler();      
        qualityRuler.setLabel("Quality Scores");
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Dimension size = getSize();
        
        g.setColor(ColorCnst.GRAY_250);
        g.fillRect(0, 0, size.width, size.height);
        
        AssemblyScroll pane = getAssemblyScrollPane();
        ColumnHeaderView columnHeaderView = pane.getColumnHeaderView();
        Rectangle rect = columnHeaderView.getConcensus().getBounds();
        rect.width = size.width;
        consensusRuler.setRect(rect);
        consensusRuler.paint(g2d, consensusRuler.getRect());

        rect = columnHeaderView.getQualityHistogram().getBounds();
        qualityRuler.setRect(rect);
        qualityRuler.paint(g2d, qualityRuler.getRect());
    }

    void resetPreferredWidth() {
        Integer width = calculatePreferredWidth();
        UIUtil.setPreferredWidth(this, width);
    }

    Integer calculatePreferredWidth() {
        Integer ret = null;
        VerticalRuler[] rulers = {consensusRuler, qualityRuler};
        for (VerticalRuler ruler : rulers) {
            int width = ruler.calculatePreferredWidth();
            if (ret == null || ret < width) {
                ret = width;
            }
        }
        return ret;
    }

    private AssemblyScroll getAssemblyScrollPane() {
        if (scrollPane == null) {
            scrollPane = UIUtil.getParent(this, AssemblyScroll.class);
        }
        return scrollPane;
    }
}
