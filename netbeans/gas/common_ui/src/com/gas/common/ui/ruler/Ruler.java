/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.ruler;

import com.gas.common.ui.core.LocList;
import com.gas.common.ui.core.RectList;
import com.gas.common.ui.util.MathUtil;
import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.util.LocUtil;
import com.gas.common.ui.util.UIUtil;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.SwingConstants;

/**
 *
 * @author dunqiang
 */
public class Ruler extends JComponent {

    private final int RULER_GAP = 2;
    private RulerLocList locList = new RulerLocList();
    private Loc selection;
    private Integer indicator = null;
    private Color color = Color.DARK_GRAY;
    private Color selectedColor = Color.BLUE;
    private Font textFont;
    private float tickMarkHeightRatio = 0.3f; /* percentage of the font height*/

    private double minMarkDistanceInCM = 1.5; /* min mark distance in centimeter*/

    private RulerLabelList selectedList = new RulerLabelList();
    private RulerLabelListMap labelListMap = new RulerLabelListMap();
    // for internal use
    private static List<Integer> MARK_POS = new ArrayList<Integer>();

    static {
        for (int i = 0; i < 1000;) {
            if (i < 10) {
                i = i + 1;
                MARK_POS.add(i);
            } else if (i < 100) {
                i = i + 10;
                MARK_POS.add(i);
            } else if (i < 1000) {
                i = i + 50;
                MARK_POS.add(i / 100 * 100 + 50);
            } else if (i < 10000) {
                i = i + 50;
                MARK_POS.add(i / 1000 * 1000 + 50);
            }
        }
    }

    public Ruler() {
        hookupListeners();
    }

    private void hookupListeners() {
        addPropertyChangeListener(new RulerListeners.PtyListener());
    }

    Integer getIndicator() {
        return indicator;
    }

    public void setIndicator(Integer indicator) {
        Integer old = this.indicator;
        this.indicator = indicator;
        firePropertyChange("indicator", old, this.indicator);
    }

    public Loc getSelection() {
        return selection;
    }

    public void setSelection(Loc selection) {
        Loc old = this.selection;
        this.selection = selection;
        firePropertyChange("selection", old, this.selection);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(Color selectedColor) {
        this.selectedColor = selectedColor;
    }

    public int getDesiredHeight() {
        if (getTextFont() == null) {
            throw new IllegalArgumentException("Must set textFont");
        }
        int ret = 0;
        Insets insets = getInsets();
        FontMetrics _fontMetrics = FontUtil.getFontMetrics(textFont);
        ret += insets.top + insets.bottom + _fontMetrics.getHeight() * Math.max(locList.getRowCount(), 1);
        ret += getTickMarkHeight() * Math.max(locList.getRowCount(), 1);
        ret += RULER_GAP * Math.max(locList.getRowCount() - 1, 0);
        return ret;
    }

    private int getTickMarkHeight() {
        int fontHeight = FontUtil.getFontMetrics(textFont).getHeight();
        return MathUtil.round(getTickMarkHeightRatio() * fontHeight);
    }

    public float getTickMarkHeightRatio() {
        return tickMarkHeightRatio;
    }

    public void setTickMarkHeightRatio(float tickMarkHeightRatio) {
        this.tickMarkHeightRatio = tickMarkHeightRatio;
    }

    public Font getTextFont() {
        return textFont;
    }

    public void setTextFont(Font textFont) {
        Font old = this.textFont;
        this.textFont = textFont;
        firePropertyChange("textFont", old, this.textFont);
    }

    private boolean validateInput() {
        boolean ret = true;
        if (getLocList().isEmpty()) {
            ret = false;
        }
        if (getTextFont() == null) {
            ret = false;
        }
        return ret;
    }

    @Override
    public void paintComponent(Graphics g) {
        if (!validateInput()) {
            return;
        }
        Rectangle drawingRect = UIUtil.getBoundsNoBorder(this);
        IRulerParent rulerParent = (IRulerParent) UIUtil.getParent(this, IRulerParent.class);
        if (rulerParent == null) {
            return;
        }
        int[] xs = rulerParent.getHorizontalBounds();
        drawingRect.setFrameFromDiagonal(xs[0], drawingRect.getMinY(), xs[1], drawingRect.getMaxY());
        paintComponent(g, drawingRect);
    }

    public double getMinMarkDistanceInCM() {
        return minMarkDistanceInCM;
    }

    public void setMinMarkDistanceInCM(double minMarkDistanceInCM) {
        this.minMarkDistanceInCM = minMarkDistanceInCM;
    }

    private void initLabelListMap() {
        labelListMap.clear();
        int rowCount = locList.getRowCount();
        for (int i = 1; i <= rowCount; i++) {
            labelListMap.put(i, new RulerLabelList());
        }
    }

    private void paintComponent(Graphics g, Rectangle displayRect) {
        final Dimension size = getSize();
        Integer markDistance = calculateMarkPosDistance(minMarkDistanceInCM);
        if (markDistance == null || size.width <= 0 || size.height <= 0) {
            return;
        }
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, size.width, size.height);
        selectedList.clear();
        initLabelListMap();
        Collections.sort(locList, new RulerLoc.Sorter());

        for (int locIndex = 0; locIndex < locList.size(); locIndex++) {
            RulerLoc loc = locList.get(locIndex);
            if (selection != null) {

                // start
                if (LocUtil.contains(loc.getAbsoluteStart(), loc.getAbsoluteEnd(), selection.getStart())) {
                    RulerLabel rulerLabel = createRulerLabel(loc.toRelativePos(selection.getStart()), displayRect, textFont, selectedColor, locIndex);
                    selectedList.add(rulerLabel);
                }

                // end
                if (LocUtil.contains(loc.getAbsoluteStart(), loc.getAbsoluteEnd(), selection.getEnd())) {
                    RulerLabel rulerLabel = createRulerLabel(loc.toRelativePos(selection.getEnd()), displayRect, textFont, selectedColor, locIndex);
                    selectedList.add(rulerLabel);
                }
            }

            for (Integer pos = loc.getStart(); LocUtil.contains(loc.getStart(), loc.getEnd(), pos);) {
                int rowNo = locList.getRowNo(loc);
                RulerLabelList labelList = labelListMap.get(rowNo);                
                Integer relativePos = calculateDistanceFromStart(pos, loc);
                if (pos.equals(loc.getStart()) || pos.equals(loc.getEnd())) {
                    RulerLabel rulerLabel = createRulerLabel(pos, displayRect, textFont, color, locIndex);
                    if (pos.equals(loc.getEnd()) && labelList != null) {                        
                        labelList.remove(rulerLabel.getBounds());
                    }
                    if(labelList != null){
                        labelList.add(rulerLabel);
                    }
                } else if (pos % markDistance == 0 || relativePos == 0) {
                    RulerLabel rulerLabel = createRulerLabel(pos, displayRect, textFont, color, locIndex);
                    if (labelList != null && !labelList.get(0).intersects(rulerLabel.getBounds())) {
                        labelList.add(rulerLabel);
                    }
                }

                if (loc.getStart() <= loc.getEnd()) {
                    pos++;
                } else {
                    if (pos < loc.getTotalPos()) {
                        pos++;
                    } else if (pos.equals(loc.getTotalPos())) {
                        pos = 1;
                    }
                }
            }
        }
        Graphics2D g2d = (Graphics2D) g;
        selectedList.paint(g2d);
        RectList rectList = selectedList.getRectList();
        labelListMap.remove(rectList);
        labelListMap.paint(g2d);
    }

    private Integer calculateDistanceFromStart(int pos, Loc loc) {
        Integer ret = null;
        if (loc.getStart() <= loc.getEnd()) {
            ret = pos - loc.getStart() + 1;
        } else {
            Integer totalPos = loc.getTotalPos();
            if (totalPos == null) {
                throw new IllegalArgumentException("totalPos cannot be null");
            }
            if (pos >= loc.getStart()) {
                ret = pos - loc.getStart() + 1;
            } else if (pos < loc.getStart()) {
                ret = pos + (totalPos - loc.getStart() + 1);
            }
        }
        return ret;
    }

    /**
     * @param pos relative pos of RulerLoc indicated by index locIndex
     */
    private RulerLabel createRulerLabel(Integer pos, Rectangle displayRect, Font font, Color color, int locIndex) {
        final FontMetrics fm = FontUtil.getFontMetrics(font);
        final int fontHeight = fm.getHeight();
        int totalPos = locList.get(0).width();
        RulerLoc loc = locList.get(locIndex);
        RulerLabel ret = null;

        Integer relativePos = calculateDistanceFromStart(pos, loc);
        ret = new RulerLabel();
        ret.setPos(pos);
        float x = getX(relativePos, displayRect, loc.getOffset(), totalPos);

        int stringWidth = fm.stringWidth(ret.getPos().toString());
        ret.setX(MathUtil.round(x - stringWidth * 0.5f));
        int y = getY(locIndex, fontHeight);
        ret.setY(y);
        ret.setFont(font);
        ret.setColor(color);
        ret.setTickGap(1);
        ret.setTickHeight(getTickMarkHeight());

        return ret;
    }

    private int getY(int locIndex, int fontHeight) {
        int rowNo = locList.getRowNo(locList.get(locIndex));
        return getInsets().top + (rowNo - 1) * fontHeight + getTickMarkHeight() + (rowNo - 1) * RULER_GAP;
    }

    private float getX(int relativePos, Rectangle displayRect, int offsetPos, int totalPos) {
        float ret = 0;
        float offset = UIUtil.toScreenWidth(displayRect.width, totalPos, offsetPos, Float.class);
        float width = UIUtil.toScreen(totalPos, relativePos, displayRect, SwingConstants.CENTER);
        ret = offset + width;
        return ret;
    }

    private Integer calculateMarkPosDistance(double minMarkDistanceInCM) {
        Integer ret = null;
        int totalPos = getLocList().get(0).width();
        Rectangle rect = UIUtil.getBoundsNoBorder(this);
        for (int i = 0; i < MARK_POS.size(); i++) {
            int pos = MARK_POS.get(i);
            int pixels = UIUtil.toScreenWidth(rect.width, totalPos, pos);
            float cm = UIUtil.toCentimeter(pixels);
            if (cm > minMarkDistanceInCM) {
                ret = pos;
                break;
            }
        }
        if (ret == null) {
            return calculateMarkPosDistance(minMarkDistanceInCM * 0.9);
        }
        return ret;
    }

    public void addLoc(RulerLoc loc) {
        locList.add(loc);
        Collections.sort(locList, new RulerLoc.Sorter());
    }

    private RulerLocList getLocList() {
        return locList;
    }

    public void setLocList(RulerLocList locList) {
        RulerLocList old = this.locList;
        this.locList = locList;
        firePropertyChange("locList", old, this.locList);
    }
}
