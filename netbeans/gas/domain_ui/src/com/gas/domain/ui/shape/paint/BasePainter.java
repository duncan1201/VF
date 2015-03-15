/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.shape.paint;

import com.gas.common.ui.core.GeneralPathList;
import com.gas.common.ui.painter.LinearGradientPainter;
import com.gas.common.ui.painter.LinearGradientPainterFactory;
import com.gas.common.ui.util.ColorUtil;
import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.MathUtil;
import com.gas.common.ui.util.StrUtil;
import com.gas.domain.core.as.Feture;
import com.gas.domain.core.as.Lucation;
import com.gas.domain.ui.shape.IShape;
import com.gas.domain.ui.shape.PathCreator;
import com.gas.domain.ui.shape.layout.ILayoutHelper;
import com.gas.domain.ui.shape.layout.LayoutHelperFinder;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
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

/**
 *
 * @author dq
 */

public abstract class BasePainter implements IPainter {
    
    @Override
    public abstract String getFetureType();
    
    @Override
    public abstract boolean isDefault();
    
    @Override
    public void paint(IShape shape, Graphics2D g2d){
        if (shape.getTextLoc() == null) {
            throw new IllegalStateException("txtLoc must be set");
        }
        if (shape.getLuc() == null) {
            return;
        }

        paintBackground(shape, g2d);

        paintForeground(shape, g2d);

        //g2d.drawRect(0, 0, size.width, size.height);
        if (shape.getTextLoc() != IShape.TEXT_LOC.NONE) {
            ILayoutHelper layoutHelper = LayoutHelperFinder.find((Feture)shape.getData());
            paintText(shape, g2d, layoutHelper.getTextDrawingRectangle(shape));
        }        
    }
    
    
/*
     * @param rect the drawing recangle excluding horizontal paddings
     */
    protected void paintText(IShape shape, Graphics2D g2d, Rectangle rect) {
        Color textColor;
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

        LinearGradientPainter painter = LinearGradientPainterFactory.create(shape.getSeedColor());

        Iterator<GeneralPath> itr = shape.getArrowPaths().iterator();
        while (itr.hasNext()) {
            GeneralPath gp = itr.next();
            painter.setGeneralPath(gp);
            painter.paint(g2d, (JComponent)shape, shape.getSize().width, shape.getSize().height);
        }

        Color borderColor = ColorUtil.changeSB(shape.getSeedColor(), 1.4f, 0.7f);
        g2d.setColor(borderColor);
        shape.getArrowPaths().setFill(false);
        shape.getArrowPaths().paint(g2d);
    }
    
    

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
    
    
    protected GeneralPathList createArrowPath(IShape shape, BasicStroke stroke, Rectangle rect, Lucation luc, String fetureKey) {
        GeneralPathList ret = new GeneralPathList();
        if (luc.isEmpty()) {
            return ret;
        }
        if (luc.isContiguous()) {
            ret = PathCreator.createSingleArrowPath(shape, luc.isContiguousFuzzyStart(), luc.isContiguousFuzzyEnd(), fetureKey, rect, stroke);            
        } else if (CompoundRichLocation.getJoinTerm().toString().equalsIgnoreCase(luc.getTerm())) {
            ret = PathCreator.createJoinArrowPath(shape, rect, stroke, shape.getLuc());
        } else if (CompoundRichLocation.getOrderTerm().toString().equalsIgnoreCase(luc.getTerm())) {
            //throw new IllegalArgumentException("Fix me" + luc.getTerm());
            ret = PathCreator.createJoinArrowPath(shape, rect, stroke, shape.getLuc());
        }
        return ret;
    }

}
