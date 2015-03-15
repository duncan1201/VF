/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor.as.ligate;

import com.gas.common.ui.util.BioUtil;
import com.gas.common.ui.util.ColorCnst;
import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.MathUtil;
import com.gas.common.ui.util.PathUtil;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.AsHelper;
import com.gas.domain.core.as.Overhang;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import org.jdesktop.swingx.color.ColorUtil;

/**
 *
 * @author dq
 */
class EndsView {

    private AnnotatedSeq as;
    private boolean circular;
    private boolean selected;
    AnnotatedSeq terminus5;
    AnnotatedSeq terminus3;
    Font font = FontUtil.getDefaultMSFont();
    final FontMetrics fm = FontUtil.getFontMetrics(font);
    final static String DOTS = "...";
    Color bgColor = Color.WHITE;
    String label;
    Integer x;
    Integer yMiddle;
    Integer xLeft;
    Integer xRight;
    GeneralPath outlineLinear;
    GeneralPath outlineCircularRight;
    GeneralPath outlineCircularLeft;

    EndsView(AnnotatedSeq as) {
        this(as, false);
    }

    EndsView(AnnotatedSeq as, boolean circular) {
        this.circular = circular;
        setAs(as);
    }

    AnnotatedSeq getAs() {
        return as;
    }

    void setAs(AnnotatedSeq as) {
        this.as = as;
        terminus5 = AsHelper.get5Terminus(as);
        terminus3 = AsHelper.get3Terminus(as);
    }

    void paintCircularBases(Graphics2D g, int xLeft, int xRight, int yMiddle) {
        final int ascent = fm.getAscent();
        final int charHeight = fm.getHeight();
        final int charWidth = fm.charWidth('A');
        String seqFull;
        String line1;
        String line2;
        int xLine1;
        int xLine2;
        int ovLength;

        Overhang overhangStart = terminus5.getStartOverhang();
        seqFull = terminus5.getSiquence().getData();
        if (overhangStart != null) {
            ovLength = overhangStart.getLength();
            if (overhangStart.isFivePrime()) {
                line1 = seqFull;
                line2 = BioUtil.complement(seqFull).substring(ovLength);

                xLine1 = xRight - ovLength * charWidth;
                xLine2 = xRight;
            } else {
                line1 = seqFull.substring(ovLength);
                line2 = BioUtil.complement(seqFull);

                xLine1 = xRight;
                xLine2 = xRight - ovLength * charWidth;
            }
        } else {
            line1 = seqFull;
            line2 = BioUtil.complement(seqFull);

            xLine1 = xRight;
            xLine2 = xRight;
        }

        Color fgColor = ColorUtil.computeForeground(bgColor);
        g.setColor(fgColor);
        g.drawString(line1, xLine1, yMiddle - (charHeight - ascent));
        g.drawString(line2, xLine2, yMiddle + ascent);

        Overhang overhangEnd = terminus3.getEndOverhang();
        seqFull = terminus3.getSiquence().getData();
        if (overhangEnd != null) {
            ovLength = overhangEnd.getLength();
            if (overhangEnd.isFivePrime()) {
                line1 = seqFull.substring(0, seqFull.length() - ovLength);
                line2 = BioUtil.complement(seqFull);
            } else {
                line1 = seqFull;
                line2 = BioUtil.complement(seqFull).substring(0, seqFull.length() - ovLength);


            }
            xLine1 = xLeft - (seqFull.length() - ovLength) * charWidth;
            xLine2 = xLeft - (seqFull.length() - ovLength) * charWidth;
        } else {
            line1 = seqFull;
            line2 = BioUtil.complement(seqFull);

            xLine1 = xLeft - seqFull.length() * charWidth;
            xLine2 = xLeft - seqFull.length() * charWidth;
        }

        g.drawString(line1, xLine1, yMiddle - (charHeight - ascent));
        g.drawString(line2, xLine2, yMiddle + ascent);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    boolean contains(Point pt) {
        boolean ret = false;
        if (circular) {
            if (outlineCircularRight != null && outlineCircularLeft != null) {
                ret = outlineCircularRight.contains(pt) || outlineCircularLeft.contains(pt);
            }
        } else {
            if (outlineLinear != null) {
                ret = outlineLinear.contains(pt);
            }
        }
        return ret;
    }

    void paintCircularOutline(Graphics2D g, int xLeft, int xRight, int yMiddle) {

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        final int charWidth = fm.charWidth('L');
        final int charHeight = fm.getHeight();
        final int ascent = fm.getAscent();
        final int leading = fm.getLeading();

        Point2D p1Right, p3Right, p2Right, p4Right;

        Overhang overhangStart = terminus5.getStartOverhang();
        int seqLength = terminus5.getLength();
        int ovLength;
        if (overhangStart != null) {
            ovLength = overhangStart.getLength();
            if (overhangStart.isFivePrime()) {
                p1Right = new Point2D.Double(xRight - ovLength * charWidth, yMiddle - charHeight);
                p2Right = new Point2D.Double(xRight + (seqLength - ovLength) * charWidth, p1Right.getY());
                p4Right = new Point2D.Double(p2Right.getX(), yMiddle + charHeight);
                p3Right = new Point2D.Double(xRight, p4Right.getY());
            } else {
                p1Right = new Point2D.Double(xRight, yMiddle - charHeight);
                p2Right = new Point2D.Double(xRight + (seqLength - ovLength) * charWidth, p1Right.getY());
                p4Right = new Point2D.Double(p2Right.getX(), yMiddle + charHeight);
                p3Right = new Point2D.Double(xRight - ovLength * charWidth, p4Right.getY());
            }
        } else {
            p1Right = new Point2D.Double(xRight, yMiddle - charHeight);
            p2Right = new Point2D.Double(xRight + terminus5.getLength() * charWidth, yMiddle - charHeight);
            p4Right = new Point2D.Double(xRight + terminus5.getLength() * charWidth, yMiddle + charHeight);
            p3Right = new Point2D.Double(xRight, yMiddle + charHeight);
        }

        outlineCircularRight = createTerminusOutline(p1Right, p2Right, p3Right, p4Right);

        Point2D p1Left, p2Left, p3Left, p4Left;
        Overhang overhangEnd = terminus3.getEndOverhang();
        seqLength = terminus3.getLength();
        if (overhangEnd != null) {
            ovLength = overhangEnd.getLength();
            if (overhangEnd.isFivePrime()) {
                p1Left = new Point2D.Double(xLeft - (seqLength - ovLength) * charWidth, yMiddle - charHeight);
                p2Left = new Point2D.Double(xLeft, p1Left.getY());
                p4Left = new Point2D.Double(xLeft + ovLength * charWidth, yMiddle + charHeight);
                p3Left = new Point2D.Double(p1Left.getX(), p4Left.getY());
            } else {
                p1Left = new Point2D.Double(xLeft - (seqLength - ovLength) * charWidth, yMiddle - charHeight);
                p2Left = new Point2D.Double(xLeft + ovLength * charWidth, p1Left.getY());
                p4Left = new Point2D.Double(xLeft, yMiddle + charHeight);
                p3Left = new Point2D.Double(p1Left.getX(), p4Left.getY());
            }
        } else {
            p1Left = new Point2D.Double(xLeft - seqLength * charWidth, yMiddle - charHeight);
            p2Left = new Point2D.Double(xLeft, p1Left.getY());
            p4Left = new Point2D.Double(xLeft, yMiddle + charHeight);
            p3Left = new Point2D.Double(p1Left.getX(), p4Left.getY());
        }
        outlineCircularLeft = createTerminusOutline(p1Left, p2Left, p3Left, p4Left);

        final int maxX = PathUtil.getMaxX(outlineCircularRight);
        final int minX = PathUtil.getMinX(outlineCircularLeft);
        int xRect = minX - charWidth * 2;
        int x2 = maxX + charWidth * 2;
        int heightRect = charHeight * 3;
        g.setColor(selected ? ColorCnst.AMBER : Color.DARK_GRAY);
        Stroke strokeOld = g.getStroke();
        g.setStroke(new BasicStroke(3));
        g.drawRoundRect(xRect, yMiddle, x2 - xRect, heightRect, charWidth * 2, charWidth * 2);
        g.setStroke(strokeOld);
        
        // draw label here
        if(label != null){
            int xLabel = Math.round(xLeft * 0.5f + xRight * 0.5f - fm.stringWidth(label) * 0.5f);
            float yLabel = yMiddle + heightRect - ascent * 0.5f;            
            g.setColor(selected? ColorCnst.AMBER : Color.BLACK);
            g.drawString(label, xLabel, yLabel);
        }

        g.setColor(bgColor);
        g.fill(outlineCircularLeft);
        g.fill(outlineCircularRight);
        g.setColor(selected ? ColorCnst.AMBER : Color.BLACK);
        g.draw(outlineCircularLeft);
        g.draw(outlineCircularRight);
        if (selected) {
            strokeOld = g.getStroke();
            g.setStroke(new BasicStroke(3));
            g.drawLine(MathUtil.round(p1Left.getX()), MathUtil.round(p1Left.getY()),
                    MathUtil.round(p2Left.getX()), MathUtil.round(p2Left.getY()));
            g.drawLine(MathUtil.round(p3Left.getX()), MathUtil.round(p3Left.getY()),
                    MathUtil.round(p4Left.getX()), MathUtil.round(p4Left.getY()));

            g.drawLine(MathUtil.round(p1Right.getX()), MathUtil.round(p1Right.getY()),
                    MathUtil.round(p2Right.getX()), MathUtil.round(p2Right.getY()));
            g.drawLine(MathUtil.round(p3Right.getX()), MathUtil.round(p3Right.getY()),
                    MathUtil.round(p4Right.getX()), MathUtil.round(p4Right.getY()));
            g.setStroke(strokeOld);
        }

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_DEFAULT);
    }

    /**
     * <pre>
     * p1 p2
     * p3 p4
     * </pre>
     */
    private GeneralPath createTerminusOutline(Point2D p1, Point2D p2, Point2D p3, Point2D p4) {
        double yMiddle = p1.getY() * 0.5 + p3.getY() * 0.5;
        GeneralPath ret = new GeneralPath();
        ret.moveTo(p1.getX(), p1.getY());
        ret.lineTo(p2.getX(), p2.getY());

        ret.lineTo(p2.getX(), yMiddle);
        ret.lineTo(p4.getX(), yMiddle);

        ret.lineTo(p4.getX(), p4.getY());
        ret.lineTo(p3.getX(), p3.getY());
        ret.lineTo(p3.getX(), yMiddle);
        ret.lineTo(p1.getX(), yMiddle);

        ret.closePath();
        return ret;
    }

    void paintCircular(Graphics2D g) {
        if (xLeft != null && xRight != null && yMiddle != null) {
            paintCircular(g, xLeft, xRight, yMiddle, label);
        }
    }

    /**
     * <pre>
     *  left   right
     *  --|     |--
     * |           |
     *  -----------
     *    p1    p2
     *    p3    p4
     * </pre>
     *
     * @param xLeft the 3' overhang will extend beyond xLeft
     * @param xRight the 5' overhang will extend beyond xRight
     */
    void paintCircular(Graphics2D g, int xLeft, int xRight, int yMiddle, String label) {
        this.xLeft = xLeft;
        this.xRight = xRight;
        this.yMiddle = yMiddle;
        this.label = label;
        g.setFont(font);
        paintCircularOutline(g, xLeft, xRight, yMiddle);
        paintCircularBases(g, xLeft, xRight, yMiddle);
    }

    Integer paintLinear(Graphics2D g) {
        if (x != null && yMiddle != null) {
            return paintLinear(g, this.x, this.yMiddle, this.label);
        } else {
            return null;
        }
    }

    int paintLinear(Graphics2D g, int x, int yMiddle, String label) {
        final int charWidth = fm.charWidth('A');
        final int ascent = fm.getAscent();
        final int charHeight = fm.getHeight();
        this.x = x;
        this.yMiddle = yMiddle;
        this.label = label;
        int yTop = yMiddle - charHeight;
        final int centerX = Math.round(this.x + getDesiredWidth() * 0.5f);
        outlineLinear = createLinearOutline(x, yMiddle);
        g.setColor(bgColor);
        g.fill(outlineLinear);
        g.setColor(selected ? ColorCnst.AMBER : Color.BLACK);
        g.draw(outlineLinear);
        if (selected) {
            Stroke strokeOld = g.getStroke();
            g.setStroke(new BasicStroke(3));
            List<Point2D> pts = createLinearOutlinePts(x, yMiddle);
            g.drawLine(MathUtil.round(pts.get(0).getX()),
                    MathUtil.round(pts.get(0).getY()),
                    MathUtil.round(pts.get(1).getX()),
                    MathUtil.round(pts.get(1).getY()));
            g.drawLine(MathUtil.round(pts.get(2).getX()),
                    MathUtil.round(pts.get(2).getY()),
                    MathUtil.round(pts.get(3).getX()),
                    MathUtil.round(pts.get(3).getY()));
            g.setStroke(strokeOld);
        }

        g.setFont(font);

        String line1;
        String line2;
        int overhangLength = 0;

        // draw 5' terminus
        Overhang overhangStart = terminus5.getStartOverhang();
        String seqFull = terminus5.getSiquence().getData();
        int xLine1 = x;
        int xLine2 = x;
        if (overhangStart != null) {
            overhangLength = overhangStart.getLength();
            if (overhangStart.isFivePrime()) {
                line1 = seqFull;
                line2 = BioUtil.complement(seqFull).substring(overhangLength);

                xLine1 = x;
                xLine2 = x + charWidth * overhangLength;
            } else {
                line1 = seqFull.substring(overhangLength);
                line2 = BioUtil.complement(seqFull);

                xLine1 = x + charWidth * overhangLength;
                xLine2 = x;
            }
        } else {
            overhangLength = 0;
            line1 = seqFull;
            line2 = BioUtil.complement(seqFull);

            xLine1 = x;
            xLine2 = x;
        }

        Color fgColor = ColorUtil.computeForeground(bgColor);
        g.setColor(fgColor);
        g.drawString(line1, xLine1, yMiddle - (charHeight - ascent));
        g.drawString(line2, xLine2, yMiddle + ascent);
        x += seqFull.length() * charWidth;

        // draw the "..."
        g.drawString(DOTS, x, yMiddle - charHeight * 0.5f);
        g.drawString(DOTS, x, yMiddle + charHeight * 0.5f);
        x += DOTS.length() * charWidth;

        // draw 3' terminus
        seqFull = terminus3.getSiquence().getData();
        Overhang overhangEnd = terminus3.getEndOverhang();
        if (overhangEnd != null) {
            overhangLength = overhangEnd.getLength();
            if (overhangEnd.isFivePrime()) {
                line1 = seqFull.substring(0, seqFull.length() - overhangLength);
                line2 = BioUtil.complement(seqFull);

                xLine1 = x;
                xLine2 = x;
            } else {

                line1 = seqFull;
                line2 = BioUtil.complement(seqFull).substring(0, seqFull.length() - overhangLength);

                xLine1 = x;
                xLine2 = x;
            }
        } else {
            overhangLength = 0;
            line1 = seqFull;
            line2 = BioUtil.complement(seqFull);

            xLine1 = x;
            xLine2 = x;
        }

        g.drawString(line1, xLine1, yMiddle - (charHeight - ascent));
        g.drawString(line2, xLine2, yMiddle + ascent);
        x += (seqFull.length() - overhangLength) * charWidth;

        int ovalHeight = Math.round(charWidth * 1.5f);
        float ovalY = yTop - charHeight * 1.3f;
        float ovalCenterY = ovalY + ovalHeight * 0.5f;
        g.setColor(Color.BLACK);
        //g.fillOval(centerX - 5, Math.round(ovalY), ovalHeight, ovalHeight);        
        g.setColor(selected ? ColorCnst.AMBER : Color.BLACK);
        g.drawString(this.label, centerX - charWidth * 0.5f, ovalCenterY + (charHeight - ascent));
        
        return x;
    }

    /**
     * <pre>
     * 0 1
     * 2 3
     * </pre>
     */
    List<Point2D> createLinearOutlinePts(int x, int yMiddle) {
        List<Point2D> ret = new ArrayList<Point2D>();
        int fontHeight = fm.getHeight();
        int charWidth = fm.charWidth('A');
        final int desiredWidth = getDesiredWidth();
        final int yTop = yMiddle - fontHeight;
        final int yBottom = yMiddle + fontHeight;
        final int xEnd = x + desiredWidth;

        Point2D p1;
        Point2D p2;
        Point2D p3;
        Point2D p4;

        Overhang overhangStart = terminus5.getStartOverhang();
        if (overhangStart != null) {

            if (overhangStart.isFivePrime()) {
                /**
                 * <pre>
                 * 5--3
                 * 3 -5
                 * </pre>
                 */
                p1 = new Point2D.Double(x, yTop);
                p3 = new Point2D.Double(x + overhangStart.getLength() * charWidth, yBottom);
            } else {
                /**
                 * <pre>
                 * 5 -3
                 * 3--5
                 * </pre>
                 */
                p1 = new Point2D.Double(x + overhangStart.getLength() * charWidth, yTop);
                p3 = new Point2D.Double(x, yBottom);
            }
        } else {
            p1 = new Point2D.Double(x, yTop);
            p3 = new Point2D.Double(x, yBottom);
        }

        Overhang overhangEnd = terminus3.getEndOverhang();
        if (overhangEnd != null) {
            if (overhangEnd.isFivePrime()) {
                /**
                 * <pre>
                 * 5- 3
                 * 3--5
                 * </pre>
                 */
                p2 = new Point2D.Double(xEnd - overhangEnd.getLength() * charWidth, yTop);
                p4 = new Point2D.Double(xEnd, yBottom);
            } else {
                /**
                 * <pre>
                 * 5--3
                 * 3- 5
                 * </pre>
                 */
                p2 = new Point2D.Double(xEnd, yTop);
                p4 = new Point2D.Double(xEnd - overhangEnd.getLength() * charWidth, yBottom);
            }
        } else {
            p2 = new Point2D.Double(xEnd, yTop);
            p4 = new Point2D.Double(xEnd, yBottom);
        }
        ret.add(p1);
        ret.add(p2);
        ret.add(p3);
        ret.add(p4);

        return ret;
    }

    /**
     * <pre>
     * p1 p2
     * p3 p4
     * </pre>
     */
    GeneralPath createLinearOutline(int x, int yMiddle) {
        GeneralPath ret = new GeneralPath();
        int fontHeight = fm.getHeight();
        int charWidth = fm.charWidth('A');
        final int desiredWidth = getDesiredWidth();
        final int yTop = yMiddle - fontHeight;
        final int yBottom = yMiddle + fontHeight;
        final int xEnd = x + desiredWidth;

        Point2D p1;
        Point2D p2;
        Point2D p3;
        Point2D p4;

        Overhang overhangStart = terminus5.getStartOverhang();
        if (overhangStart != null) {

            if (overhangStart.isFivePrime()) {
                /**
                 * <pre>
                 * 5--3
                 * 3 -5
                 * </pre>
                 */
                p1 = new Point2D.Double(x, yTop);
                p3 = new Point2D.Double(x + overhangStart.getLength() * charWidth, yBottom);
            } else {
                /**
                 * <pre>
                 * 5 -3
                 * 3--5
                 * </pre>
                 */
                p1 = new Point2D.Double(x + overhangStart.getLength() * charWidth, yTop);
                p3 = new Point2D.Double(x, yBottom);
            }
        } else {
            p1 = new Point2D.Double(x, yTop);
            p3 = new Point2D.Double(x, yBottom);
        }

        Overhang overhangEnd = terminus3.getEndOverhang();
        if (overhangEnd != null) {
            if (overhangEnd.isFivePrime()) {
                /**
                 * <pre>
                 * 5- 3
                 * 3--5
                 * </pre>
                 */
                p2 = new Point2D.Double(xEnd - overhangEnd.getLength() * charWidth, yTop);
                p4 = new Point2D.Double(xEnd, yBottom);
            } else {
                /**
                 * <pre>
                 * 5--3
                 * 3- 5
                 * </pre>
                 */
                p2 = new Point2D.Double(xEnd, yTop);
                p4 = new Point2D.Double(xEnd - overhangEnd.getLength() * charWidth, yBottom);
            }
        } else {
            p2 = new Point2D.Double(xEnd, yTop);
            p4 = new Point2D.Double(xEnd, yBottom);
        }

        List<Point2D> pts = createLinearOutlinePts(x, yMiddle);

        ret.moveTo(pts.get(0).getX(), pts.get(0).getY());
        ret.lineTo(pts.get(1).getX(), pts.get(1).getY());

        ret.lineTo(pts.get(1).getX(), yMiddle);
        ret.lineTo(pts.get(3).getX(), yMiddle);

        ret.lineTo(pts.get(3).getX(), pts.get(3).getY());
        ret.lineTo(pts.get(2).getX(), pts.get(2).getY());

        ret.lineTo(pts.get(2).getX(), yMiddle);
        ret.lineTo(pts.get(0).getX(), yMiddle);
        ret.closePath();
        return ret;
    }

    public boolean isCircular() {
        return circular;
    }

    public void setCircular(boolean circular) {
        this.circular = circular;
    }

    int getDesiredWidth() {
        int ret = terminus5.getLength() + terminus3.getLength() + "...".length();
        int charWidth = fm.charWidth('A');
        return ret * charWidth;
    }

    int getStartOverhangLength() {
        Overhang o = as.getStartOverhang();
        if (o != null) {
            return o.getLength();
        } else {
            return 0;
        }
    }

    int getEndOverhangLength() {
        Overhang o = as.getEndOverhang();
        if (o != null) {
            return o.getLength();
        } else {
            return 0;
        }
    }
}
