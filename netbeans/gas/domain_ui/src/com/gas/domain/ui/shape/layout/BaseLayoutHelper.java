/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.shape.layout;

import com.gas.common.ui.util.MathUtil;
import com.gas.domain.ui.shape.IShape.TEXT_LOC;
import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.as.Lucation;
import com.gas.domain.ui.shape.Arrow;
import com.gas.domain.ui.shape.IShape;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dunqiang
 */
public abstract class BaseLayoutHelper implements ILayoutHelper {

    @Override
    public Line2D.Float getDesiredVerticalLine(Arrow arrow, TEXT_LOC txtLoc, Rectangle drawingRect, Integer totalPos) {
        Line2D.Float ret2 = new Line2D.Float();
        int startPixel = getDesiredStartPixel(arrow, txtLoc, drawingRect, totalPos);
        int width = getDesiredWidth(arrow, txtLoc, drawingRect, totalPos);
        ret2.x1 = startPixel;
        ret2.x2 = startPixel + width - 1;
        return ret2;
    }

    @Override
    public Integer getDesiredStartPixel(IShape arrow, TEXT_LOC txtLoc, Rectangle drawingRect, Integer totalPos) {
        if (totalPos == null) {
            throw new IllegalArgumentException("Must set total pos");
        }

        float ret = drawingRect.x;
        Lucation luc = arrow.getLuc();
        if (txtLoc == TEXT_LOC.AFTER || txtLoc == TEXT_LOC.INSIDE || txtLoc == TEXT_LOC.NONE || txtLoc == TEXT_LOC.BEFORE || txtLoc == TEXT_LOC.ABOVE || txtLoc == TEXT_LOC.BELOW) {
            Float width = UIUtil.toScreenWidthFloat(drawingRect.width, totalPos, luc.width());
            if (width == null) {
                return null;
            }
            int minShapeWidthPixel = getMinShapeWidth(arrow);
            Float posStartPixel = UIUtil.toScreenWidth(drawingRect.width, totalPos, luc.getStart() - 1, Float.class);
            posStartPixel += 1;
            ret += posStartPixel;
            if (width < minShapeWidthPixel) {
                float posEndPixel = UIUtil.toScreenWidth(drawingRect.width, totalPos, luc.getEnd(), Float.class);
                float posMiddlePixel = MathUtil.round(Math.abs(posEndPixel - posStartPixel) / 2.0f);
                posMiddlePixel = Math.max(0, posMiddlePixel);
                float diff = Math.abs(minShapeWidthPixel / 2.0f - posMiddlePixel);
                diff = Math.max(0, diff);
                ret -= diff;
            }

            if (txtLoc == TEXT_LOC.BEFORE) {
                ret -= getMinTextDimension(arrow).width;
            }
        } else {
            throw new IllegalArgumentException("Fix me! Developer");
        }
        return MathUtil.round(ret, Integer.class);
    }

    @Override
    public int getDesiredWidth(IShape arrow, TEXT_LOC txtLoc, Rectangle drawingRect, Integer totalPos) {
        if (txtLoc == null) {
            throw new IllegalStateException("txtLoc must be set");
        }
        if (arrow.getTextFont() == null) {
            throw new IllegalStateException("textFont must be set");
        }

        int ret = 0;
        Dimension minTxtSize = getMinTextDimension(arrow);

        int shapeWidth = calculateShapeWidth(arrow, totalPos, drawingRect);

        if (txtLoc == TEXT_LOC.INSIDE) {
            ret = shapeWidth;
        } else if (txtLoc == TEXT_LOC.BEFORE || arrow.getTextLoc() == TEXT_LOC.AFTER) {
            ret = shapeWidth + minTxtSize.width;
        } else if (txtLoc == TEXT_LOC.ABOVE || arrow.getTextLoc() == TEXT_LOC.BELOW) {
            ret = shapeWidth;
        } else if (txtLoc == TEXT_LOC.NONE) {
            ret = shapeWidth;
        } else {
            throw new IllegalStateException("Developer. Fix me");
        }
        return ret;
    }

    private int calculateShapeWidth(IShape arrow, Integer totalPos, Rectangle drawingRect) {
        if (drawingRect == null) {
            throw new IllegalArgumentException("Must set drawing rect");
        }
        if (arrow.getLuc() == null) {
            throw new IllegalArgumentException("Must set loc");
        }
        if (totalPos == null) {
            throw new IllegalArgumentException("Must set totalPos");
        }
        int ret = 0;

        Integer lucWidth = arrow.getLuc().width();
        if (lucWidth == null) {
            return 0;
        }
        Integer calculatedPixel = UIUtil.toScreenWidth(drawingRect.width, totalPos, lucWidth);

        int minShapeWidth = getMinShapeWidth(arrow);
        if (calculatedPixel == null || calculatedPixel < minShapeWidth) {
            ret = minShapeWidth;
        } else {
            ret = calculatedPixel;
        }


        return ret;
    }

    /*
     * Get the text drawing rectangle without HORIZONTAL paddings
     * Because the text will be vertically center-aligned and horizontally. The 
     */
    @Override
    public Rectangle getTextDrawingRectangle(IShape arrow) {
        Rectangle bounds = arrow.getBounds();
        Rectangle ret = new Rectangle(0, 0, bounds.width, bounds.height);

        Dimension minTxtSize = getMinTextDimension(arrow);
        TEXT_LOC txtLoc = arrow.getTextLoc();

        if (txtLoc == TEXT_LOC.BEFORE) {
            ret.x = arrow.getTextLeftPadding();
            ret.y = Math.min(arrow.getTextTopPadding(), arrow.getVerticalPadding());
            ret.width = minTxtSize.width - arrow.getTextLeftPadding() + arrow.getTextRightPadding();
            ret.height = bounds.height - 2 * Math.min(arrow.getTextTopPadding(), arrow.getVerticalPadding());
        } else if (txtLoc == TEXT_LOC.INSIDE) {
            ret.x = arrow.getTextLeftPadding();
            ret.y = arrow.getTextTopPadding() + arrow.getVerticalPadding();
            ret.width = bounds.getSize().width - arrow.getTextLeftPadding() + arrow.getTextRightPadding();
            ret.height = minTxtSize.height - arrow.getTextTopPadding() - arrow.getTextBottomPadding();
        } else if (txtLoc == TEXT_LOC.ABOVE) {
            ret.x = arrow.getTextLeftPadding();
            ret.y = arrow.getTextTopPadding();
            ret.width = minTxtSize.width - arrow.getTextLeftPadding() - arrow.getTextRightPadding();
            ret.height = minTxtSize.height - arrow.getTextTopPadding() - arrow.getTextBottomPadding();
        } else if (txtLoc == TEXT_LOC.AFTER) {

            Rectangle shapeRect = getShapeDrawingRectangle(arrow);
            ret.x = shapeRect.x + shapeRect.width;

            ret.y = arrow.getTextTopPadding();
            ret.width = minTxtSize.width - arrow.getTextLeftPadding() - arrow.getTextRightPadding();
            ret.height = bounds.height - arrow.getTextTopPadding() - arrow.getTextBottomPadding();
        } else if (txtLoc == TEXT_LOC.NONE) {
            ret.x = ret.y = ret.width = ret.height = 0;
        } else {
            throw new IllegalArgumentException("Fix me. Developer");
        }
        return ret;
    }

    private int getMinShapeWidth(IShape arrow) {
        int ret = 2 * arrow.getArrowCapWidth() + arrow.getRoundness();
        return ret;
    }

    /*
     * Get the min height of the component
     * 1. If the txt is inside the shape
     *      min height = the height of min text size + 2 vertical paddings
     * 2. If the txt is before or after the shape
     *      min height = the height of min text size + 2 vertical paddings
     * 3. If the txt if above or below the shape
     *      min height = the height of min text size + 1 vertical padding + font height
     */
    @Override
    public int getDesiredHeight(IShape arrow) {        
        if (arrow.getTextLoc() == null) {
            throw new IllegalStateException("txtLoc must be set");
        }
        if (arrow.getTextFont() == null) {
            throw new IllegalStateException("textFont must be set");
        }
        int ret = 0;
        Dimension minTxtSize = getMinTextDimension(arrow);

        if (arrow.getTextLoc() == TEXT_LOC.INSIDE || arrow.getTextLoc() == TEXT_LOC.NONE) {
            ret = minTxtSize.height + arrow.getVerticalPadding() * 2;
        } else if (arrow.getTextLoc() == TEXT_LOC.BEFORE || arrow.getTextLoc() == TEXT_LOC.AFTER) {
            ret = minTxtSize.height + arrow.getVerticalPadding() * 2;
        } else if (arrow.getTextLoc() == TEXT_LOC.ABOVE || arrow.getTextLoc() == TEXT_LOC.BELOW) {
            ret = 2 * minTxtSize.height + arrow.getVerticalPadding() * 2;
        } else {
            throw new IllegalStateException("Developer. Fix me");
        }
        return ret;
    }

    @Override
    public int getMinimumHeight(IShape arrow) {
        int ret = 0;
        Dimension minTxtSize = getMinTextDimension(arrow);
        ret = minTxtSize.height + arrow.getVerticalPadding() * 2;
        return ret;
    }

    /*
     * Get the drawing rectangle for shapes excluding padding
     * 1. If the txt loc is above
     * 2. If the txt loc is before
     *      
     */
    @Override
    public Rectangle getShapeDrawingRectangle(IShape arrow) {

        Rectangle ret = new Rectangle(0, 0, 0, 0);

        Dimension txtSize = getMinTextDimension(arrow);
        Rectangle bounds = arrow.getBounds();


        TEXT_LOC txtLoc = arrow.getTextLoc();
        if (txtLoc == TEXT_LOC.ABOVE) {
            ret.x = 0;
            ret.y = arrow.getVerticalPadding();
            ret.width = bounds.width;
            ret.height = bounds.height - txtSize.height - arrow.getVerticalPadding();            
        } else if (txtLoc == TEXT_LOC.BEFORE) {
            ret.x = txtSize.width;
            ret.y = arrow.getVerticalPadding();
            ret.width = bounds.width - txtSize.width;
            ret.height = bounds.height - arrow.getVerticalPadding() * 2;
        } else if (txtLoc == TEXT_LOC.INSIDE || txtLoc == TEXT_LOC.NONE) {
            ret.x = 0;
            ret.y = arrow.getVerticalPadding();
            ret.width = bounds.width;
            ret.height = bounds.height - arrow.getVerticalPadding() * 2;
        } else if (txtLoc == TEXT_LOC.AFTER) {
            ret.x = 0;
            ret.y = arrow.getVerticalPadding();
            ret.width = bounds.width - txtSize.width;

            ret.height = bounds.height - arrow.getVerticalPadding() * 2;
        } else {
            throw new IllegalStateException("Fix me. Developer");
        }
        return ret;
    }

    public Dimension getMinTextDimension(IShape arrow) {
        if (arrow.getTextFont() == null) {
            throw new IllegalStateException("Must set the \'textFont\' first");
        }
        if (arrow.getDisplayText() == null) {
            throw new IllegalStateException("Must set the \'text\' first");
        }
        boolean includePaddings = true;
        Dimension ret = new Dimension();

        int stringWidth = FontUtil.getMSFontCharWidth(arrow.getTextFont()) * arrow.getDisplayText().length();

        ret.width = stringWidth + arrow.getTextLeftPadding() + arrow.getTextRightPadding();
        if (includePaddings) {
            ret.width += arrow.getTextLeftPadding() + arrow.getTextRightPadding();
        }
        ret.height = FontUtil.getFontMetrics(arrow.getTextFont()).getHeight();
        if (includePaddings) {
            ret.height += arrow.getTextTopPadding() + arrow.getTextBottomPadding();
        }
        return ret;
    }

    @Override
    public abstract String getFetureType();
    
    @Override
    public abstract boolean isDefault();
}