/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.tigr.core.ui.editor.k;

import com.gas.common.ui.util.UIUtil;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JScrollPane;

/**
 *
 * @author dq
 */
public class RowHeaderUI extends JComponent {
    
    private Integer max;
    
    public RowHeaderUI(){
        final Insets insets = UIUtil.getDefaultInsets();
        setBorder(BorderFactory.createEmptyBorder(insets.top, insets.left, 0, 0));        
        hookupListeners();
    }
    
    private void hookupListeners(){
        addPropertyChangeListener(new RowHeaderUIListeners.PtyListener());
    }
    
    @Override
    public void paintComponent(Graphics g){
        if(max == null){
            return;
        }
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        final Dimension size = getSize();
        final Insets insets = getInsets();
        final FontMetrics fm = g.getFontMetrics();
        
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, size.width, size.height);
        
        JScrollPane pane = UIUtil.getParent(this, JScrollPane.class);
        ChromatogramComp comp = (ChromatogramComp)pane.getViewport().getView();       
        int baseline = comp.getBaseline();
        
        g.setColor(Color.BLACK);        
        g.drawLine(size.width - 1, insets.top, size.width - 1, size.height - baseline);
        g.drawString(max.toString(), insets.left, insets.top + fm.getAscent());
    } 

    public void setMax(Integer max) {
        Integer old = this.max;
        this.max = max;
        firePropertyChange("max", old, this.max);
    }
    
}