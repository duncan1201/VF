/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.shape;

import com.gas.common.ui.core.GeneralPathList;
import com.gas.common.ui.misc.CNST;
import com.gas.common.ui.util.PathUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.as.Feture;
import com.gas.domain.core.as.FetureKeyCnst;
import com.gas.domain.core.as.Lucation;
import com.gas.domain.core.as.Pozition;
import com.gas.domain.core.primer3.OligoElement;
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.geom.GeneralPath;
import java.util.Iterator;

/**
 *
 * @author dunqiang
 */
public class PathCreator {

    public static boolean isStartCapRightwards(Boolean forward, boolean fuzzyStart) {
        boolean rightwardsStart;
        if (forward == null) {
            rightwardsStart = fuzzyStart ? true : false;
        } else if (forward) {
            rightwardsStart = fuzzyStart ? true : false;
        } else {
            rightwardsStart = false;
        }
        return rightwardsStart;
    }

    public static boolean isEndCapRightwards(Boolean forward, boolean fuzzyEnd) {
        boolean rightwardsEnd;
        if (forward == null) {
            rightwardsEnd = true;
        } else if (forward) {
            rightwardsEnd = true;
        } else {
            rightwardsEnd = fuzzyEnd ? false : true;
        }
        return rightwardsEnd;
    }

    public static CNST.CAP getEndCAP(String fetureType, Boolean forward, boolean fuzzyStart, boolean fuzzyEnd) {
        CNST.CAP cap;
        if (forward == null) {

            cap = fuzzyEnd ? CNST.CAP.BEZIER : CNST.CAP.ROUNDED;

        } else if (forward) {
            if (FetureKeyCnst.PRIMER_BINDING_SITE.equals(fetureType)) {
                cap = CNST.CAP.HALF_TIP_UP;
            } else {
                cap = fuzzyEnd ? CNST.CAP.TWO_TIP : CNST.CAP.ONE_TIP;
            }
        } else {
            cap = fuzzyEnd ? CNST.CAP.TWO_TIP : CNST.CAP.ROUNDED;
        }
        return cap;
    }

    public static CNST.CAP getStartCAP(String fetureType, Boolean forward, boolean fuzzyStart, boolean fuzzyEnd) {
        CNST.CAP cap;
        if (forward == null) {
            cap = fuzzyStart ? CNST.CAP.BEZIER : CNST.CAP.ROUNDED;
        } else if (forward) {
            cap = fuzzyStart ? CNST.CAP.TWO_TIP : CNST.CAP.ROUNDED;
        } else {
            if (FetureKeyCnst.PRIMER_BINDING_SITE.equals(fetureType)) {
                cap = CNST.CAP.HALF_TIP_UP;
            } else {
                cap = fuzzyStart ? CNST.CAP.TWO_TIP : CNST.CAP.ONE_TIP;
            }
        }
        return cap;
    }   

    /**
     * Create path for linear arrows
     */
    public static GeneralPathList createSingleArrowPath(IShape arrow, boolean fuzzyStart, boolean fuzzyEnd, String fetureKey, Rectangle rect, BasicStroke stroke) {
        GeneralPathList ret = new GeneralPathList();
        GeneralPath path = null;
        if(fetureKey != null && fetureKey.equals(FetureKeyCnst.PRIMER_BINDING_SITE)){
            path = _createSingleArrowPathForPrimer(arrow, rect, stroke);
        }else{
            path = _createSingleArrowPath(arrow, fuzzyStart, fuzzyEnd, fetureKey, rect, stroke);
        }
        ret.add(path);
        return ret;
    }

    private static GeneralPath _createArrowCap(Rectangle rect, boolean forward, CNST.CAP capType, BasicStroke stroke, Integer roundness, boolean downwards) {
        final Dimension size = rect.getSize();
        GeneralPath ret = new GeneralPath();
        final double heightQuarter = rect.getHeight() * 0.25;
        final float lineWidth = stroke.getLineWidth();
        final int centerY = (int) rect.getCenterY();
        if (forward) {
            if (capType == CNST.CAP.ONE_TIP) {
                if (downwards) {
                    ret.moveTo(rect.x, rect.y);
                    ret.lineTo(rect.x + size.width - 1 - lineWidth, centerY);
                    ret.lineTo(rect.x, rect.y + size.height);
                } else {
                    ret = _createArrowCap(rect, forward, capType, stroke, roundness, !downwards);
                    ret = PathUtil.reverse(ret);
                }
            } else if (capType == CNST.CAP.HALF_TIP_UP) {
                if (downwards) {
                    ret.moveTo(rect.x, rect.y);
                    ret.lineTo(rect.x + size.width - 1 - lineWidth, rect.y + size.height);
                    ret.lineTo(rect.x, rect.y + size.height);
                } else {
                    ret = _createArrowCap(rect, forward, capType, stroke, roundness, !downwards);
                    ret = PathUtil.reverse(ret);
                }
            } else if (capType == CNST.CAP.TWO_TIP) {
                if (downwards) {
                    ret.moveTo(rect.x, rect.y);
                    ret.lineTo(rect.x + size.width - 1 - lineWidth, rect.y + heightQuarter);
                    ret.lineTo(rect.x, centerY);
                    ret.lineTo(rect.x + size.width - 1 - lineWidth, centerY + heightQuarter);
                    ret.lineTo(rect.x, rect.y + size.height);
                } else {
                    ret = _createArrowCap(rect, forward, capType, stroke, roundness, !downwards);
                    ret = PathUtil.reverse(ret);
                }
            } else if (capType == CNST.CAP.ROUNDED) {
                if (downwards) {

                    ret.moveTo(rect.x, rect.y);
                    ret.lineTo(rect.x + rect.width - 1 - lineWidth - roundness, rect.y);
                    ret.quadTo(rect.x + rect.width - 1 - lineWidth, rect.y, rect.x + rect.width - 1 - lineWidth, rect.y + roundness);
                    ret.lineTo(rect.x + rect.width - 1 - lineWidth, rect.y + rect.height - roundness);
                    ret.quadTo(rect.x + rect.width - 1 - lineWidth, rect.y + rect.height, rect.x + rect.width - roundness - 1 - lineWidth, rect.y + rect.height);
                    ret.lineTo(rect.x, rect.y + rect.height);

                } else {
                    ret = _createArrowCap(rect, forward, capType, stroke, roundness, !downwards);
                    ret = PathUtil.reverse(ret);
                }
            } else if (capType == CNST.CAP.BEZIER) {
                if (downwards) {
                    final double x1 = rect.x + rect.width - 1 - lineWidth;
                    final double x2 = x1 - 3;
                    double x = x1;
                    int y = rect.y;
                    ret.moveTo(x, rect.y);
                    y++;
                    while (y <= rect.y + rect.height) {
                        if (x == x1) {
                            x = x2;
                        } else if (x == x2) {
                            x = x1;
                        }

                        ret.lineTo(x, y);
                        y++;
                    }
                } else {
                    ret = _createArrowCap(rect, forward, capType, stroke, roundness, !downwards);
                    ret = PathUtil.reverse(ret);
                }
            }
        } else if (!forward) {
            if (!downwards) {
                ret = _createArrowCap(rect, !forward, capType, stroke, roundness, downwards);
                ret = PathUtil.flipHorizontally(ret);
            } else {
                ret = _createArrowCap(rect, forward, capType, stroke, roundness, !downwards);
                ret = PathUtil.reverse(ret);
            }
        }
        return ret;
    }
    
    private static GeneralPath _createSingleArrowPathForPrimer(IShape shape, Rectangle rect, BasicStroke stroke) {
        final Feture feture = (Feture)shape.getData();
                
        if(feture == null || feture.getData() == null || ((OligoElement)feture.getData()).getTail() == null || ((OligoElement)feture.getData()).getTail().isEmpty()){
            return _createPathForPrimerNoTail(shape, rect, stroke);
        }else{
            return _createPathForPrimerWithTail(shape, rect, stroke);
        }
    } 
    
    private static GeneralPath _createPathForPrimerWithTail(IShape shape, Rectangle rect, BasicStroke stroke){
            final Feture feture = (Feture)shape.getData();
        OligoElement oe = (OligoElement)feture.getData();
        int totalLength = oe.getSeq().length() + oe.getTail().length();
        int tailLength = oe.getTail().length();
        
        final float boxWidth = 1.0f * rect.width / totalLength;
        
        GeneralPath path = new GeneralPath();
        float lineWidth = stroke.getLineWidth();
        rect.height--;
        
        // upper tail
        path.moveTo(rect.getX(), rect.getY() + rect.getHeight() * 0.25);
        path.lineTo(rect.getX() + boxWidth * tailLength, rect.getY() + rect.getHeight() * 0.25);
        path.lineTo(rect.getX() + boxWidth * tailLength, rect.getY() + rect.getHeight() * 0.5);
        
        path.lineTo(rect.getX() + rect.getWidth() - rect.getHeight() * 0.5, rect.getY() + rect.getHeight() * 0.5);
        path.lineTo(rect.getX() + rect.getWidth() - rect.getHeight() * 0.75, rect.getY());
        path.lineTo(rect.getX() + rect.getWidth() - rect.getHeight() * 0.25, rect.getY());
        path.lineTo(rect.getX() + rect.getWidth() - lineWidth, rect.getY() + rect.getHeight() * 0.5);
        path.lineTo(rect.getX() + rect.getWidth() - lineWidth, rect.getY() + rect.getHeight());
        path.lineTo(rect.getX() + boxWidth * tailLength, rect.getY() + rect.getHeight());
        
        path.lineTo(rect.getX() + boxWidth * tailLength, rect.getY() + rect.getHeight() * 0.75);
        path.lineTo(rect.getX(), rect.getY() + rect.getHeight() * 0.75);        
        
        path.closePath();
        
        if(!shape.getForward()){
            path = PathUtil.flipHorizontally(path);
            path = PathUtil.flipVertically(path);
        }

        return path;
    }    
    
    private static GeneralPath _createPathForPrimerNoTail(IShape shape, Rectangle rect, BasicStroke stroke){        
        GeneralPath path = new GeneralPath();
        float lineWidth = stroke.getLineWidth();
        rect.height--;
        
        path.moveTo(rect.getX(), rect.getY() + rect.getHeight() * 0.5);
        path.lineTo(rect.getX() + rect.getWidth() - rect.getHeight() * 0.5, rect.getY() + rect.getHeight() * 0.5);
        path.lineTo(rect.getX() + rect.getWidth() - rect.getHeight() * 0.75, rect.getY());
        path.lineTo(rect.getX() + rect.getWidth() - rect.getHeight() * 0.25, rect.getY());
        path.lineTo(rect.getX() + rect.getWidth() - lineWidth, rect.getY() + rect.getHeight() * 0.5);
        path.lineTo(rect.getX() + rect.getWidth() - lineWidth, rect.getY() + rect.getHeight());
        path.lineTo(rect.getX(), rect.getY() + rect.getHeight());
        
        path.closePath();
        
        if(!shape.getForward()){
            path = PathUtil.flipHorizontally(path);
            path = PathUtil.flipVertically(path);
        }

        return path;
    }

    private static GeneralPath _createSingleArrowPath(IShape arrow, boolean fuzzyStart, boolean fuzzyEnd, String fetureKey, Rectangle rect, BasicStroke stroke) {
        GeneralPath path = new GeneralPath();

        rect.height--;
        final int capWidth = arrow.getArrowCapWidth();

        Rectangle capRect = new Rectangle();
        capRect.setFrame(rect.getX() + rect.getWidth() - capWidth, rect.getY(), capWidth, rect.getHeight());
        boolean rightwardsEnd = isEndCapRightwards(arrow.getForward(), fuzzyEnd);
        boolean downwards = true;
        CNST.CAP endCap = getEndCAP(fetureKey, arrow.getForward(), fuzzyStart, fuzzyEnd);
        GeneralPath pathEndCap = _createArrowCap(capRect, rightwardsEnd, endCap, stroke, arrow.getRoundness(), downwards);

        capRect = new Rectangle();
        capRect.setFrame(rect.getX(), rect.getY(), capWidth, rect.getHeight());

        boolean rightwardsStart = isStartCapRightwards(arrow.getForward(), fuzzyStart);
        downwards = false;
        CNST.CAP startCap = getStartCAP(fetureKey, arrow.getForward(), fuzzyStart, fuzzyEnd);;
        GeneralPath pathStartCap = _createArrowCap(capRect, rightwardsStart, startCap, stroke, arrow.getRoundness(), downwards);

        path.append(pathEndCap, true);
        path.append(pathStartCap, true);
        path.closePath();

        return path;
    }

    /**
     * For linear DNA
     */
    public static GeneralPathList createJoinArrowPath(IShape arrow, final Rectangle rect, BasicStroke stroke, Lucation luc) {
        GeneralPathList ret = new GeneralPathList();
        int start = luc.getStart();
        int end = luc.getEnd();
        int posWidth = end - start + 1;
        Iterator<Pozition> itr = luc.getPozitionsItr();
        while (itr.hasNext()) {
            Pozition poz = itr.next();
            final int x1 = UIUtil.toScreenWidth(rect.width, posWidth, poz.getStart() - start);
            final int x2 = UIUtil.toScreenWidth(rect.width, posWidth, poz.getEnd() - start + 1);
            Rectangle r = new Rectangle();
            r.x = x1;
            r.y = rect.y;
            r.width = x2 - x1 + 1;
            r.height = rect.height;

            GeneralPath path = _createSingleArrowPath(arrow, poz.isFuzzyStart(), poz.isFuzzyEnd(), null, r, stroke);
            ret.add(path);
        }

        GeneralPathList connectors = ret.createConnectors();
        ret.addAll(connectors);

        return ret;
    }

    // TODO finish the implementation
    public static GeneralPathList createOrderArrowPath(IShape arrow, Rectangle rect, BasicStroke stroke) {
        GeneralPathList ret = new GeneralPathList();
        throw new UnsupportedOperationException();
        //return ret;
    }
}
