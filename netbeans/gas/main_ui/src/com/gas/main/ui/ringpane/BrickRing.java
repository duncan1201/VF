/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.ringpane;

import com.gas.common.ui.core.Arc2DX;
import com.gas.common.ui.light.CircularBrick;
import com.gas.common.ui.light.CircularBrickList;
import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.util.BioUtil;
import com.gas.common.ui.util.LocUtil;
import com.gas.common.ui.util.MathUtil;
import com.gas.common.ui.color.IColorProvider;
import com.gas.common.ui.light.TransLabel;
import com.gas.common.ui.light.TransLabelList;
import com.gas.common.ui.util.ColorUtil;
import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.UIUtil;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.*;
import java.lang.ref.WeakReference;
import java.util.Locale;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

/**
 *
 * @author dq
 */
public class BrickRing extends JComponent {

    private static final int BASE_GAP = 2;
    private static final int LINE_GAP = 0;
    private String bases;
    private Object data;
    private Boolean doubleStranded;
    private float rotateOffset = 0;
    private WeakReference<RingGraphPanel> ringGraphPanelRef;
    private IColorProvider colorProvider;
    private Integer dataStart;
    private Integer endPos;
    private Integer totalPos;
    private Loc selection;
    private CircularBrickList circularBrickList = new CircularBrickList();

    public BrickRing() {
        addPropertyChangeListener(new BrickRingListeners.PtyChangeListener());
        setFocusable(false);
    }

    @Override
    public Font getFont() {
        Font ret = super.getFont();
        if (ret == null) {
            ret = FontUtil.getDefaultSansSerifFont().deriveFont(FontUtil.getDefaultFontSize());
            //setFont(ret);
        }
        return ret;
    }

    public CircularBrickList getCircularBrickList() {
        if (circularBrickList == null || circularBrickList.isEmpty()) {
            circularBrickList = createCircularBrickList();
        }
        return circularBrickList;
    }

    public Loc getSelection() {
        return selection;
    }

    public void setSelection(Loc selection) {
        Loc old = this.selection;
        this.selection = selection;
        firePropertyChange("selection", old, this.selection);
    }

    public Integer getTotalPos() {
        return totalPos;
    }

    public void insertAtPos(Character c, Integer pos) {
        bases = bases.substring(0, pos) + c.toString() + bases.substring(pos - 1);
        totalPos = totalPos + 1;
        endPos = endPos + 1;
    }

    protected void setTotalPos(Integer totalPos) {
        this.totalPos = totalPos;
    }

    public Integer getEndPos() {
        return endPos;
    }

    public void setEndPos(Integer endPos) {
        Integer old = this.endPos;
        this.endPos = endPos;
        firePropertyChange("endPos", old, this.endPos);
    }

    public Integer getStartPos() {
        return dataStart;
    }

    public void setStartPos(Integer startPos) {
        Integer old = this.dataStart;
        this.dataStart = startPos;
        firePropertyChange("startPos", old, this.dataStart);
    }

    public float getRotateOffset() {
        return rotateOffset;
    }

    public void setRotateOffset(float rotateOffset) {
        float old = this.rotateOffset;
        this.rotateOffset = rotateOffset;
        firePropertyChange("rotateOffset", old, this.rotateOffset);
    }

    public IColorProvider getColorProvider() {
        return colorProvider;
    }

    public void setColorProvider(IColorProvider colorProvider) {
        IColorProvider old = this.colorProvider;
        this.colorProvider = colorProvider;
        firePropertyChange("colorProvider", old, this.colorProvider);
    }

    public Boolean isDoubleStranded() {
        return doubleStranded;
    }

    public void setDoubleStranded(Boolean doubleStranded) {
        Boolean old = this.doubleStranded;
        this.doubleStranded = doubleStranded;
        firePropertyChange("doubleStranded", old, this.doubleStranded);
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getBases() {
        return bases;
    }

    public void setBases(String bases) {
        String old = this.bases;
        this.bases = bases;
        firePropertyChange("bases", old, this.bases);
    }

    public Point2D getCaretLocationByPos(Integer pos) {
        Point2D ret = null;
        boolean visible = isDataVisible();
        if (visible) {
            ret = getCircularBrickList().getCaretLocation(pos);
        } else {
            throw new UnsupportedOperationException();
        }
        return ret;
    }

    public Double getCaretAngleByPos(Integer pos) {
        return getCircularBrickList().getCaretAngle(pos);
    }

    public Rectangle getCaretRectByPos(Integer pos, int caretWidth) {
        Point2D caretLoc2d = getCircularBrickList().getCaretLocation(pos);
        Point caretLoc = new Point();
        caretLoc.setLocation(caretLoc2d.getX(), caretLoc2d.getY());
        SwingUtilities.convertPointToScreen(caretLoc, this);
        return getCaretRect(caretLoc, caretWidth);
    }

    public Rectangle getCaretRect(Point ptScreen, int caretWidth) {
        Rectangle ret = null;
        Point2D caretLoc = getCaretLocation(ptScreen);
        if (caretLoc != null) {
            final Dimension size = getSize();
            final Point2D.Double center = new Point2D.Double(size.width * 0.5, size.height * 0.5);
            final double angleRad = MathUtil.getAngleInRadians(caretLoc, center);
            int thickness = getFullRingThickness();
            double distance = caretLoc.distance(center);
            Point2D.Double coords = MathUtil.getCoordsRad(center, distance - 2 * thickness, angleRad);            
                ret = new Rectangle();           
                ret.setFrameFromDiagonal(caretLoc, coords);
                if(angleRad >= 0 && angleRad < Math.PI / 2){
                    //ret.width += caretWidth;
                }
            
        }
        return ret;
    }

    public Integer getCaretPos(Double angdeg) {
        Integer pos = null;
        if (angdeg != null) {
            boolean visible = isDataVisible();
            if (visible) {
                pos = getCircularBrickList().getCaretPos(angdeg);
            } else {   
                RingGraphPanel ringGraphPanel = getRingGraphPanel();
                final int length = ringGraphPanel.getAs().getLength();            
                final float offset = ringGraphPanel.getOffset();
                final double angle = MathUtil.normalizeDegree(90-angdeg + offset);
                pos = UIUtil.toPos(angle, 360, length);               
            }
        }
        return pos;
    }

    public Point2D getCaretLocation(Point pScreen) {
        Point2D ret = null;
        Point pt = new Point(pScreen);
        SwingUtilities.convertPointFromScreen(pt, this);
        Point2D origin = new Point2D.Double();
        Dimension size = getSize();
        origin.setLocation(size.width * 0.5f, size.height * 0.5f);
        double angle = MathUtil.getAngleInDegrees(pt, origin);
        if (circularBrickList != null) {
            ret = circularBrickList.getCaretLocation(angle);
        }
        return ret;
    }

    @Override
    public void paintComponent(Graphics g) {
        if (bases == null || bases.isEmpty()) {
            return;
        }
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        boolean dataVisible = isDataVisible();
        if (dataVisible) {
            paintDataVisible(g2d);
        } else {
            paintDataInvisible(g2d);
        }
    }

    private void paintDataInvisible(Graphics2D g2d) {
        Rectangle bounds = getBounds();
        Rectangle square = new Rectangle();

        square.width = square.height = Math.min(bounds.width, bounds.height);
        square.x += Math.round((bounds.width - square.width) * 0.5f);
        square.y += Math.round((bounds.height - square.height) * 0.5f);

        BasicStroke stroke = new BasicStroke(2);
        g2d.setStroke(stroke);       
        g2d.drawOval(square.x, square.y, Math.round(square.width - stroke.getLineWidth() - 1), Math.round(square.height - stroke.getLineWidth() - 1));
    }

    @Override
    public void repaint() {
        circularBrickList.clear();
        super.repaint();
    }

    private void paintDataVisible(Graphics2D g2d) {
        final AffineTransform old = g2d.getTransform(); // backup 

        getCircularBrickList().paint(g2d);

        g2d.setTransform(old);// restore 
    }
    
    private RingGraphPanel getRingGraphPanel(){
        if(ringGraphPanelRef == null || ringGraphPanelRef.get() == null){
            RingGraphPanel ringGraphPanel = UIUtil.getParent(this, RingGraphPanel.class);           
            ringGraphPanelRef = new WeakReference<RingGraphPanel>(ringGraphPanel);
        }
        return ringGraphPanelRef.get();
    }

    private CircularBrickList createCircularBrickList() {
        CircularBrickList ret = new CircularBrickList();
        if (endPos == null || dataStart == null || bases == null || colorProvider == null || bases.length() == 0) {
            return ret;
        }
        RingGraphPanel ringGraphPanel = getRingGraphPanel();
        if(ringGraphPanel == null){
            return ret;
        }
        int length = endPos - dataStart + 1;
        Loc visibleLoc = ringGraphPanel.calculateVisibleLoc();
        if (visibleLoc == null) {
            return ret;
        }
        //int width = LocUtil.width(visibleLoc.getStart(), visibleLoc.getEnd(), totalPos);
        
        visibleLoc = LocUtil.expand(visibleLoc, 3, 3, true);

        final int baseSpan = (endPos - dataStart + 1) / bases.length();

        Rectangle bounds = getBounds();
        bounds.x = bounds.y = 0;
        int thickness = getRingThickness();
        Rectangle2D middleBounds = null;
        Rectangle2D innerBounds = UIUtil.shrink(bounds, thickness, thickness);

        if (Boolean.TRUE.equals(isDoubleStranded())) {
            middleBounds = UIUtil.shrink(bounds, thickness * 0.5, thickness * 0.5);
        }

        for (int pos = visibleLoc.getStart(); pos != visibleLoc.getEnd() + 1;) {

            int reminder = (pos - dataStart + 1) % baseSpan;
            double dataIndex = Math.ceil(1.0 * (pos - dataStart + 1) / baseSpan) - 1;

            if ((reminder == 1 || baseSpan == 1) && dataIndex < bases.length()) {

                Character c = bases.charAt((int) dataIndex);

                if (Boolean.TRUE.equals(isDoubleStranded())) {
                    Color color = colorProvider.getColor(c);
                    CircularBrick bBrick = createCircularBrick(bounds, middleBounds, pos, c, baseSpan);
                    if(colorProvider.isBackgroundColor()){
                        bBrick.setBgColor(color);
                    }else{
                        bBrick.setFgColor(color);
                        bBrick.setBgColor(Color.WHITE);
                    }
                    ret.add(bBrick);

                    color = colorProvider.getColor(BioUtil.complement(c));
                    bBrick = createCircularBrick(middleBounds, innerBounds, pos, BioUtil.complement(c), baseSpan);
                    if(colorProvider.isBackgroundColor()){
                        bBrick.setBgColor(color);
                    }else{
                        bBrick.setFgColor(color);
                        bBrick.setBgColor(Color.WHITE);
                    }
                    ret.add(bBrick);
                } else {
                    Color color = colorProvider.getColor(c);
                    CircularBrick bBrick = createCircularBrick(bounds, innerBounds, pos, c, baseSpan);
                    if(colorProvider.isBackgroundColor()){
                        bBrick.setBgColor(color);
                    }else{
                        bBrick.setFgColor(color);
                        bBrick.setBgColor(Color.WHITE);
                    }

                    ret.add(bBrick);
                }
            }

            if (visibleLoc.getStart() > visibleLoc.getEnd()) {
                if (pos < length) {
                    pos++;
                } else {
                    pos = 1;
                }
            } else {
                pos++;
            }
        }
        ret.setSelection(selection);
        return ret;
    }

    private Double getUnitIncrementRad(boolean inRad, Integer pos) {
        Double ret = 0d;
        if (inRad) {
            if (pos != null) {
                ret = 2 * (pos - 1) * Math.PI / totalPos;
            } else {
                ret = Math.PI * 2 / totalPos;
            }
        } else {
            if (pos != null) {
                ret = 360.0 * (pos - 1) / totalPos;
            } else {
                ret = 360.0 / totalPos;
            }
        }
        return ret;
    }

    protected Double getUnitIncrementRad(boolean inRad) {
        return getUnitIncrementRad(inRad, null);
    }

    private CircularBrick createCircularBrick(Rectangle2D bounds, Rectangle2D iBounds, int pos, Character c, int baseSpan) {
        Double increment = getUnitIncrementRad(false);
        Arc2DX.Float out = new Arc2DX.Float(bounds, -(pos + baseSpan - 1) * increment.floatValue() + 90 - rotateOffset, increment.floatValue() * baseSpan, Arc2D.OPEN);
        Arc2DX.Float inner = new Arc2DX.Float(iBounds, -(pos + baseSpan - 1) * increment.floatValue() + 90 - rotateOffset + increment.floatValue() * baseSpan, -increment.floatValue() * baseSpan, Arc2D.OPEN);

        
        CircularBrick bBrick = new CircularBrick();
        bBrick.setInnerArc(inner);
        bBrick.setOuterArc(out);
        bBrick.setText(c.toString());
        bBrick.setPos(pos);
        return bBrick;
    }

    private boolean isDataVisible() {
        if (bases == null || bases.isEmpty()) {
            return false;
        }
        Dimension size = getSize();
        int sLength = Math.min(size.width, size.height);

        Dimension square = new Dimension(sLength, sLength);
        FontMetrics fm = FontUtil.getFontMetrics(this);
        int charWidth = fm.charWidth('A');
        int fullRingWidth = getFullRingThickness();
        double innerRadius = square.height * 0.5 - fullRingWidth;
        double perimeter = 2 * Math.PI * innerRadius;
        double aaa = perimeter / bases.length();
        boolean ret = aaa >= charWidth + BASE_GAP;
        return ret;
    }

    protected int getFullRingThickness() {
        int ret = 0;

        FontMetrics fm = FontUtil.getFontMetrics(this);
        ret = fm.getHeight();
        if (Boolean.TRUE.equals(isDoubleStranded())) {
            ret += fm.getHeight();
            ret += LINE_GAP;
        }
        ret += 2;

        return ret;
    }

    protected int getRingThickness() {
        int ret = 0;
        if (isDataVisible()) {
            ret += getFullRingThickness();
        } else {
            ret = 3;
        }
        return ret;
    }

    protected Dimension getFullSize() {
        FontMetrics fm = FontUtil.getFontMetrics(this);
        int charWidth = fm.charWidth('A');
        int requiredPerimeter = (charWidth + BASE_GAP) * bases.length();
        double innerRadius = requiredPerimeter / (2 * Math.PI);
        int fullRingWidth = getRingThickness();
        int radius = (int) Math.ceil(innerRadius * 2 + fullRingWidth * 2);
        Dimension ret = new Dimension(radius * 2, radius * 2);
        return ret;
    }
}