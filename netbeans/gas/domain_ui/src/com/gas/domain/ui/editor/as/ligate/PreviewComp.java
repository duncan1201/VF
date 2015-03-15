/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor.as.ligate;

import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.AnnotatedSeqList;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.JComponent;

/**
 *
 * @author dq
 */
class PreviewComp extends JComponent {

    AnnotatedSeqList asList;    
    private String selectedName;
    EndsViewList endsViewList;

    PreviewComp(AnnotatedSeqList asList) {
        this.asList = asList;
        FontMetrics fm = FontUtil.getDefaultFontMetrics();
        int charWidth = fm.charWidth('A');
        setBorder(BorderFactory.createEmptyBorder(charWidth, charWidth, charWidth, charWidth));
                
        hookupListeners();
    }
    
    private void hookupListeners(){
        addMouseListener(new PreviewCompListeners.MouseAdpt());
        addMouseMotionListener(new PreviewCompListeners.MouseAdpt());
    }
    
    void setSelected(String asName){
        endsViewList.setSelected(asName);
    }

    void setSelectedName(String selectedName) {
        this.selectedName = selectedName;
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Dimension size = getSize();
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        LigatePanel ligatePanel = UIUtil.getParent(this, LigatePanel.class);
        boolean compatible = ligatePanel.getLigateTable().compatible();
        boolean circular = ligatePanel.isCircularize();
        if (!compatible) {
            final String noPreview = "Oops! No preview available";
            final FontMetrics fm = g2d.getFontMetrics();
            final int strWidth = fm.stringWidth(noPreview);
            final int charHeight = fm.getHeight();
            g2d.drawString(noPreview, size.width * 0.5f - strWidth * 0.5f, size.height * 0.5f - charHeight);
            return;
        }        
        Insets insets = getInsets();
        int charHeight = FontUtil.getDefaultMSFontMetrics().getHeight();
        AnnotatedSeqList modifiedList = ligatePanel.getModifiedData();

        endsViewList = new EndsViewList(modifiedList, circular);
        endsViewList.setSelected(selectedName);
        int linearWidth = endsViewList.getLinearWidth();
        int x = Math.round(size.width * 0.5f - linearWidth * 0.5f);
        if (circular) {
            endsViewList.paint(g2d, x, insets.top + charHeight * 2);
        } else {
            endsViewList.paint(g2d, x, Math.round(size.height * 0.4f));
        }
    }    

    @Override
    public Dimension getPreferredSize() {
        Dimension ret = super.getPreferredSize();
        Insets insets = getInsets();
        FontMetrics fm = FontUtil.getDefaultFontMetrics();
        EndsViewList tmp = new EndsViewList(asList, false);
        ret.width = tmp.getDesiredWidth() + insets.left + insets.right;
        ret.height = fm.getHeight() * 7 + insets.top + insets.bottom;
        return ret;
    }
}
