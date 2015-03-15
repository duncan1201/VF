/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.tigr.core.ui.editor.shared;

import com.gas.common.ui.light.Text;
import com.gas.common.ui.light.TextList;
import com.gas.common.ui.util.ColorCnst;
import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.tigr.core.ui.ckpanel.ChromatogramComp2;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.SwingConstants;

public class RowHeaderView extends JComponent {

    private Font textFont = FontUtil.getDefaultSansSerifFont();
    TextList textList = new TextList();
    List<ChromatogramComp2.Read> reads;

    public RowHeaderView() {
        hookupListeners();
    }

    private void hookupListeners() {
        addPropertyChangeListener(new RowHeaderViewListeners.PtyListener());
    }

    @Override
    public void paintComponent(Graphics g) {
        Dimension size = getSize();    
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        AssemblyScroll assemblyScroll = UIUtil.getParent(this, AssemblyScroll.class);
        MainComp mainComp = (MainComp)assemblyScroll.getViewport().getView();
        List<Rectangle> rects = mainComp.getRects();               
        for(int i = 0; i < rects.size(); i++){
            Rectangle rect = rects.get(i);
            Text text = textList.get(i);
            if(i % 2 == 0){
                text.setBgColor(ColorCnst.ALICE_BLUE);
            }else{
                text.setBgColor(Color.WHITE);
            }
            text.setFgColor(Color.BLACK);
            text.setX(0);            
            text.setY(rect.y);
            text.setWidth(size.width);            
            text.setHeight(rect.height);            
        }        
        textList.setFont(textFont);
        textList.paint(g2d, SwingConstants.CENTER, false);
    }

    public List<ChromatogramComp2.Read> getReads() {
        return reads;
    }

    public void setReads(List<ChromatogramComp2.Read> reads) {
        List<ChromatogramComp2.Read> old = this.reads;
        this.reads = reads;
        firePropertyChange("reads", old, reads);
    }

    protected Integer calculatePreferredWidth() {
        final Insets insets = UIUtil.getDefaultInsets();
        final FontMetrics fm = FontUtil.getFontMetrics(textFont);
        String longest = textList.getLongest();
        if (longest != null) {
            Integer ret = fm.stringWidth(longest) + insets.left + insets.right;
            return ret;
        } else {
            return null;
        }
    }
}