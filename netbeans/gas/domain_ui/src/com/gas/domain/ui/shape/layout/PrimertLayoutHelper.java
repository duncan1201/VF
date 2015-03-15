/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.shape.layout;

import com.gas.common.ui.util.MathUtil;
import com.gas.domain.ui.shape.IShape.TEXT_LOC;
import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.as.Feture;
import com.gas.domain.core.as.FetureKeyCnst;
import com.gas.domain.core.as.Lucation;
import com.gas.domain.core.primer3.OligoElement;
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
@ServiceProvider(service = ILayoutHelper.class)
public class PrimertLayoutHelper extends BaseLayoutHelper implements ILayoutHelper {

    @Override
    public String getFetureType() {
        return FetureKeyCnst.PRIMER_BINDING_SITE;
    }
    
/*
     * Get the text drawing rectangle without HORIZONTAL paddings
     * Because the text will be vertically center-aligned and horizontally. The 
     */
    @Override
    public Rectangle getTextDrawingRectangle(IShape shape) {
        Rectangle bounds = shape.getBounds();        
        Rectangle ret = new Rectangle(0, 0, bounds.width, bounds.height);
        
        Dimension minTxtSize = getMinTextDimension(shape);
        TEXT_LOC txtLoc = shape.getTextLoc();

        if (txtLoc == TEXT_LOC.BEFORE) {
            ret.x = shape.getTextLeftPadding();
            ret.y = Math.min(shape.getTextTopPadding(), shape.getVerticalPadding());
            ret.width = minTxtSize.width - shape.getTextLeftPadding() + shape.getTextRightPadding();
            ret.height = bounds.height - 2 * Math.min(shape.getTextTopPadding(), shape.getVerticalPadding());
        } else if (txtLoc == TEXT_LOC.INSIDE) {
            ret.x = shape.getTextLeftPadding();
            ret.y = shape.getTextTopPadding() + shape.getVerticalPadding();
            ret.width = bounds.getSize().width - shape.getTextLeftPadding() + shape.getTextRightPadding();
            ret.height = minTxtSize.height - shape.getTextTopPadding() - shape.getTextBottomPadding();
        } else if (txtLoc == TEXT_LOC.ABOVE) {
            Feture feture = (Feture)shape.getData();
            OligoElement oe = (OligoElement)feture.getData();
            if(oe != null){
                if(oe.getTail() == null || oe.getTail().isEmpty()){
                    ret.x = shape.getTextLeftPadding();
                }else{                    
                    final float boxWidth = bounds.width / (oe.getSeq().length() + oe.getTail().length()) ;
                    ret.x = shape.getTextLeftPadding() + Math.round(boxWidth * oe.getTail().length());
                }
            }else{
                ret.x = shape.getTextLeftPadding();
            }
            ret.y = shape.getTextTopPadding();
            ret.width = minTxtSize.width - shape.getTextLeftPadding() - shape.getTextRightPadding();
            ret.height = minTxtSize.height - shape.getTextTopPadding() - shape.getTextBottomPadding();
        } else if (txtLoc == TEXT_LOC.AFTER) {

            Rectangle shapeRect = getShapeDrawingRectangle(shape);
            ret.x = shapeRect.x + shapeRect.width;

            ret.y = shape.getTextTopPadding();
            ret.width = minTxtSize.width - shape.getTextLeftPadding() - shape.getTextRightPadding();
            ret.height = bounds.height - shape.getTextTopPadding() - shape.getTextBottomPadding();
        } else if (txtLoc == TEXT_LOC.NONE) {
            ret.x = ret.y = ret.width = ret.height = 0;
        } else if (txtLoc == TEXT_LOC.BELOW){
            ret.x = bounds.getSize().width - shape.getTextLeftPadding() - minTxtSize.width;
            ret.y = Math.round(bounds.getSize().height * 0.5f);
            ret.width = minTxtSize.width - shape.getTextLeftPadding() - shape.getTextRightPadding();
            ret.height = Math.round(bounds.getSize().height * 0.5f);
        } else {
            throw new IllegalArgumentException("Fix me. Developer");
        }
        return ret;
    }    
    
    @Override
    public Rectangle getShapeDrawingRectangle(IShape arrow) {

        Rectangle ret = new Rectangle(0, 0, 0, 0);

        Dimension txtSize = getMinTextDimension(arrow);
        Rectangle bounds = arrow.getBounds();


        TEXT_LOC txtLoc = arrow.getTextLoc();
        if (txtLoc == TEXT_LOC.ABOVE || txtLoc == TEXT_LOC.BELOW) {
            ret.x = 0;
            ret.y = arrow.getVerticalPadding();
            ret.width = bounds.width;
            ret.height = bounds.height - arrow.getVerticalPadding() * 2;            
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
    
    public Rectangle getSeqDrawingRectangle(IShape shape) {
        
        Feture feture = (Feture)shape.getData();
        if(feture == null){
            return null;
        }
        OligoElement oe = (OligoElement)feture.getData();
        final boolean forward = feture.getLucation().getStrand();
        
        Rectangle ret = new Rectangle(0, 0, 0, 0);

        Dimension txtSize = getMinTextDimension(shape);
        Rectangle bounds = shape.getBounds();
        
        if(txtSize.getWidth() > bounds.width){
            return null;
        }

        TEXT_LOC txtLoc = shape.getTextLoc();
        if (txtLoc == TEXT_LOC.ABOVE) {
            
            if(oe != null && (oe.getTail() == null || oe.getTail().isEmpty())){
                ret.x = 0;
                ret.y = shape.getVerticalPadding() + txtSize.height;
                ret.width = bounds.width;
                ret.height = bounds.height - shape.getVerticalPadding() * 2 - txtSize.height;      
            }else{
                ret.x = 0;
                ret.y = Math.round(bounds.height * 0.25f);
                ret.width = bounds.width;
                ret.height = Math.round(bounds.height - shape.getVerticalPadding() * 2 - bounds.height * 0.25f);
            }
        } else if (txtLoc == TEXT_LOC.BEFORE) {
            ret.x = txtSize.width;
            ret.y = forward ? shape.getVerticalPadding() + txtSize.height: shape.getVerticalPadding();
            ret.width = bounds.width - txtSize.width;
            ret.height = bounds.height - shape.getVerticalPadding() * 2 - txtSize.height;
        } else if (txtLoc == TEXT_LOC.INSIDE || txtLoc == TEXT_LOC.NONE) {
            // no need to draw sequence
            return null;
        } else if (txtLoc == TEXT_LOC.AFTER) {
            ret.x = 0;
            ret.y = forward ? shape.getVerticalPadding() + txtSize.height: shape.getVerticalPadding();
            ret.width = bounds.width - txtSize.width;
            ret.height = bounds.height - shape.getVerticalPadding() * 2 - txtSize.height;
        } else if (txtLoc == TEXT_LOC.BELOW){
            ret.x = 0;
            ret.y = shape.getVerticalPadding();
            ret.width = bounds.width;
            ret.height = bounds.height - shape.getVerticalPadding();            
        } else {
            throw new IllegalStateException("Fix me. Developer");
        }
        return ret;
    }
    
    @Override
    public boolean isDefault(){
        return false;
    }

    @Override
    public ILayoutHelper newInstance() {
        return new PrimertLayoutHelper();
    }
}