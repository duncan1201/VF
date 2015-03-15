/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.shape.paint;

import com.gas.common.ui.core.GeneralPathList;
import com.gas.common.ui.painter.LinearGradientPainter;
import com.gas.common.ui.painter.LinearGradientPainterFactory;
import com.gas.common.ui.util.ColorCnst;
import com.gas.common.ui.util.ColorUtil;
import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.MathUtil;
import com.gas.common.ui.util.Pref;
import com.gas.common.ui.util.StrUtil;
import com.gas.domain.core.as.Feture;
import com.gas.domain.core.as.FetureKeyCnst;
import com.gas.domain.core.as.Lucation;
import com.gas.domain.core.primer3.OligoElement;
import com.gas.domain.ui.shape.IShape;
import com.gas.domain.ui.shape.PathCreator;
import com.gas.domain.ui.shape.layout.ILayoutHelper;
import com.gas.domain.ui.shape.layout.LayoutHelperFinder;
import com.gas.domain.ui.shape.layout.PrimertLayoutHelper;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;
import java.awt.geom.RoundRectangle2D;
import java.util.Iterator;
import javax.swing.JComponent;
import org.biojavax.bio.seq.CompoundRichLocation;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IPainter.class)
public class PrimerPainter extends BasePainter implements IPainter{
    
    
    @Override
    public String getFetureType(){
        return FetureKeyCnst.PRIMER_BINDING_SITE;
    }
    
    @Override
    public boolean isDefault(){
        return false;
    }
    
    @Override
    public void paint(IShape shape, Graphics2D g2d){
        super.paint(shape, g2d);
        PrimertLayoutHelper layoutHelper = (PrimertLayoutHelper)LayoutHelperFinder.find((Feture)shape.getData());
        Rectangle rect = layoutHelper.getSeqDrawingRectangle(shape);
        if(rect != null){
            Feture feture = (Feture)shape.getData();            
            paintSequence((OligoElement)feture.getData(), rect, g2d);
        }
    }


    private void paintSequenceForward(OligoElement oe, Rectangle rect, Graphics2D g2d){
        if(oe == null){
            return;
        }
        // for later restoration
        Font oldFont = g2d.getFont();
        Color oldColor = g2d.getColor();
                
        g2d.setFont(FontUtil.getDefaultMSFont(Pref.CommonPtyPrefs.getInstance().getBaseFontSize()));
        final FontMetrics fm = g2d.getFontMetrics();
        String seq = oe.getSeq();
        if(oe.getSeqTemplate() == null || oe.getSeqTemplate().isEmpty()){
            oe.setSeqTemplate(seq);
        }
        String seqTemplate = oe.getSeqTemplate();        
        String tail = oe.getTail();
        boolean forward = oe.getForward();
        float boxWidth = (float)rect.width / (seq.length() + (tail == null ? 0 : tail.length()));
        int charWidth = fm.charWidth('A');
        if(charWidth > boxWidth){
            return;
        }
        //final int yTail = MathUtil.round(rect.y + rect.getSize().height * 0.25 + fm.getAscent() - fm.getHeight() / 2.0);
        final int yTail = MathUtil.round(rect.y + rect.getSize().height * 0.75 - (fm.getAscent() - fm.getHeight() / 2.0));
        for(int i = 0; i < tail.length(); i++){
            int x = Math.round(rect.x + boxWidth * i + (boxWidth - charWidth) * 0.5f);
            String c = forward? Character.toString(tail.charAt(i)) : Character.toString(seq.charAt(tail.length() - i - 1));
            g2d.drawString(c, x, yTail);
        }
        
        final int y = MathUtil.round(rect.y + rect.getSize().height - (fm.getAscent() - fm.getHeight() / 2.0));
        for(int i = 0; i < seq.length(); i++){
            int x = Math.round(rect.x + boxWidth * i + boxWidth * tail.length() + (boxWidth - charWidth) * 0.5f);
            String c = Character.toString(seq.charAt(i));
            if(seq.charAt(i) == seqTemplate.charAt(i)){
                g2d.setColor(oldColor);
            }else{
                g2d.setColor(ColorCnst.CRIMSON);
            }
            g2d.drawString(c, x, y);            
        }        
        
        // restore the font
        g2d.setFont(oldFont);
        g2d.setColor(oldColor);    
    }    
    
    private void paintSequenceReverse(OligoElement oe, Rectangle rect, Graphics2D g2d){
        if(oe == null){
            return;
        }
        // for later restoration
        Font oldFont = g2d.getFont();
        Color oldColor = g2d.getColor();
                
        g2d.setFont(FontUtil.getDefaultMSFont(Pref.CommonPtyPrefs.getInstance().getBaseFontSize()));
        final FontMetrics fm = g2d.getFontMetrics();
        String seq = oe.getSeq();
        if(oe.getSeqTemplate() == null || oe.getSeqTemplate().isEmpty()){
            oe.setSeqTemplate(seq);
        }
        String seqTemplate = oe.getSeqTemplate();        
        String tail = oe.getTail();
        float boxWidth = (float)rect.width / (seq.length() + (tail == null ? 0 : tail.length()));
        int charWidth = fm.charWidth('A');
        if(charWidth > boxWidth){
            return;
        }        
                
        //final int y = MathUtil.round(rect.y + fm.getAscent() + (fm.getAscent() - fm.getHeight() / 2.0));
        final int y = MathUtil.round(rect.y + fm.getAscent());
        for(int i = 0; i < seq.length(); i++){
            int x = Math.round(rect.x + boxWidth * i + (boxWidth - charWidth) * 0.5f);
            String c = Character.toString(seq.charAt(seq.length() - i - 1));
            if(c.charAt(0) == seqTemplate.charAt(seq.length() - i - 1)){
                g2d.setColor(oldColor);
            }else{
                g2d.setColor(ColorCnst.CRIMSON);
            }
            g2d.drawString(c, x, y);            
        }        
        
        //final int yTail = MathUtil.round(rect.y + rect.getSize().height * 0.25 + fm.getAscent() - fm.getHeight() / 2.0);
        g2d.setColor(oldColor);
        final int yTail = MathUtil.round(y + rect.height * 0.25f);
        for(int i = 0; i < tail.length(); i++){
            int x = Math.round(rect.x + boxWidth * (i + seq.length()) + (boxWidth - charWidth) * 0.5f);
            String c = Character.toString(tail.charAt(tail.length() - i - 1));
            g2d.drawString(c, x, yTail);
        }
        
        // restore the font
        g2d.setFont(oldFont);
        g2d.setColor(oldColor);    
    }    
    
    private void paintSequence(OligoElement oe, Rectangle rect, Graphics2D g2d){
        if(oe == null){
            return;
        }
        if(oe.getForward()){
            paintSequenceForward(oe, rect, g2d);
        }else{
            paintSequenceReverse(oe, rect, g2d);
        }
    }
    
    /*
     * @param rect the drawing recangle excluding horizontal paddings
     */
    @Override
    protected void paintText(IShape shape, Graphics2D g2d, Rectangle rect) {
        Color textColor ;
        if (shape.getTextLoc() == IShape.TEXT_LOC.INSIDE) {
            textColor = ColorUtil.getForeground(shape.getSeedColor(), Color.class, ColorUtil.LUMA_THRESHOLD);
        } else {
            textColor = Color.BLACK;
        }       
        
        g2d.setPaint(textColor);
        g2d.setFont(shape.getTextFont());
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, // Anti-alias!
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        FontMetrics fontMetrics = g2d.getFontMetrics();
        int ascent = fontMetrics.getAscent();
        int descent = fontMetrics.getDescent();
        //int fontHeight = fontMetrics.getHeight();
        int fontHeight = MathUtil.round((ascent + descent));
        //g2d.drawString(displayText, getTextLeftPadding(), ascent);
        int y = MathUtil.round(rect.y + rect.getSize().height / 2.0 + ascent - fontHeight / 2.0);

        String trimmed;
        if (shape.getTextLoc() == IShape.TEXT_LOC.INSIDE) {
            int strWidth = FontUtil.getMSFontCharWidth(shape.getTextFont()) * shape.getDisplayText().length();
            if (strWidth > rect.width) {
                int n = rect.width / fontMetrics.stringWidth("a") - 1;
                n = Math.max(0, n);
                trimmed = StrUtil.trim(shape.getDisplayText(), n) + ".";
            } else {
                trimmed = shape.getDisplayText();
            }
        } else {
            trimmed = shape.getDisplayText();
        }

        g2d.drawString(trimmed, rect.x, y);
        //g2d.drawRect(rect.x, rect.y, rect.width, rect.height);
    }    

    @Override
    protected void paintForeground(IShape shape, Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        BasicStroke stroke = new BasicStroke(1);

        g2d.setStroke(stroke);

        Feture feture = (Feture) shape.getData();
        
        ILayoutHelper layoutHelper = LayoutHelperFinder.find(feture);
        
        Rectangle shapeDrawingRect = layoutHelper.getShapeDrawingRectangle(shape);
        
        if (feture == null) {            
            shape.setArrowPaths(createArrowPath(shape, stroke, shapeDrawingRect, shape.getLuc(), null));
        } else {            
            shape.setArrowPaths(createArrowPath(shape, stroke, shapeDrawingRect, shape.getLuc(), feture.getKey()));
        }

        Color borderColor = ColorUtil.changeSB(shape.getSeedColor(), 1.4f, 0.7f);
        g2d.setColor(borderColor);
        shape.getArrowPaths().setFill(false);
        shape.getArrowPaths().paint(g2d);
    }
    
    

    @Override
    protected void paintBackground(IShape shape, Graphics2D g2d) {

        final Dimension size = shape.getSize();
        BasicStroke stroke = (BasicStroke) g2d.getStroke();

        Color darker = ColorUtil.changeSB(shape.getSeedColor(), 1.4f, 0.7f);
        Color ligher = ColorUtil.changeSB(shape.getSeedColor(), 1.0f, 1.3f);
        final RoundRectangle2D roundRect = new RoundRectangle2D.Float(0, 0, size.width - 1 - stroke.getLineWidth(), size.height - 1 - stroke.getLineWidth(), size.height * shape.getHighlightRoundness(), size.height * shape.getHighlightRoundness());

        if (shape.isMouseHover() && !shape.isSelected()) {
            g2d.setPaint(ligher);
            g2d.fill(roundRect);
            g2d.setPaint(darker);
            g2d.draw(roundRect);
        } else if (shape.isSelected()) {
            g2d.setPaint(ligher);
            g2d.fill(roundRect);
            g2d.setPaint(darker);
            g2d.draw(roundRect);
        } else if (!shape.isSelected()) {
            /*
             * FtrTrack ftrTrack = UIUtil.getParent(this, FtrTrack.class); Color
             * bg = ftrTrack.getBackground(); g2d.setPaint(bg); g2d.fillRect(0,
             * 0, size.width, size.height);
             */
        }
    }    
    
    
    @Override
    protected GeneralPathList createArrowPath(IShape shape, BasicStroke stroke, Rectangle rect, Lucation luc, String fetureKey) {
        GeneralPathList ret = new GeneralPathList();
        if (luc.isEmpty()) {
            return ret;
        }
        
        ret = PathCreator.createSingleArrowPath(shape, luc.isContiguousFuzzyStart(), luc.isContiguousFuzzyEnd(), fetureKey, rect, stroke);            
        
        return ret;
    }

}
