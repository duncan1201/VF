/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.tigr.core.ui.editor.shared;

import com.gas.common.ui.core.VariantMapMdl;
import com.gas.common.ui.util.MathUtil;
import com.gas.common.ui.util.UIUtil;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;
import javax.swing.JComponent;
import javax.swing.SwingConstants;

/**
 *
 * @author dq
 */
public class VariantsMap extends JComponent {

    private VariantMapMdl mdl;
    private Color variantColor = Color.RED;
    private Color fillColor = Color.LIGHT_GRAY;
    
    public VariantsMap() {
        addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                String pName = evt.getPropertyName();
                if(pName.equalsIgnoreCase("mdl")){
                    repaint();
                }else if(pName.equals("variantColor") || pName.equals("fillColor")){
                    repaint();
                }
            }
        });
    }

    public void setFillColor(Color fillColor) {
        Color old = this.fillColor;
        this.fillColor = fillColor;
        firePropertyChange("fillColor", old, this.fillColor);
    }


    public void setVariantColor(Color variantColor) {
        Color old = this.variantColor;
        this.variantColor = variantColor;
        firePropertyChange("variantColor", old, this.variantColor);
    }
    
    public VariantMapMdl getMdl() {
        return mdl;
    }

    public void setMdl(VariantMapMdl mdl) {
        VariantMapMdl old = this.mdl;
        this.mdl = mdl;
        firePropertyChange("mdl", old, this.mdl);
    }

    @Override
    public void paintComponent(Graphics g) {
        if (mdl == null) {
            return;
        }
        Dimension size = getSize();
        Insets insets = getInsets();
        int totalDisplayWidth = size.width - insets.left - insets.right;
        int totalDisplayHeight = size.height - insets.top - insets.bottom;
        int readCount = mdl.getReadCount();
        int totalPos = mdl.getLength();
        Iterator<VariantMapMdl.Read> readItr = mdl.getSortedReads().iterator();
        int readNo = 1;
        
        while (readItr.hasNext()) {
            VariantMapMdl.Read read = readItr.next();
            int start = read.getStart();
            int end = read.getEnd();

            float x = UIUtil.toScreen(totalPos, start, totalDisplayWidth, insets.left, SwingConstants.LEFT);
            int xInt = MathUtil.round(x);
            float width = UIUtil.toScreen(totalPos, end - start + 1, totalDisplayWidth, 0, SwingConstants.LEFT);
            int widthInt = MathUtil.round(width);
            float y = UIUtil.toScreen(readCount, readNo, totalDisplayHeight, insets.top, SwingConstants.LEFT);
            int yInt = MathUtil.round(y);
            float yNext = UIUtil.toScreen(readCount, readNo + 1, totalDisplayHeight, 0, SwingConstants.LEFT);
            int yNextInt = MathUtil.round(yNext);
            int height = yNextInt - yInt + 1;
            g.setColor(fillColor);
            g.fillRect(xInt, yInt, widthInt, height);

            g.setColor(variantColor);
            Iterator<VariantMapMdl.Variant> vItr = read.getVariants().iterator();
            while(vItr.hasNext()){
                VariantMapMdl.Variant v = vItr.next();
                int pos = v.getPos() + read.getStart() - 1;
                x = UIUtil.toScreen(totalPos, pos, totalDisplayWidth, insets.left, SwingConstants.LEFT);
                xInt = MathUtil.round(x);
                
                g.drawLine(xInt, yInt, xInt, yNextInt - 1);
            }            
            readNo++;
        }        
    }
}