/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.light;

import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.util.MathUtil;
import com.gas.common.ui.color.IColorProvider;
import com.gas.common.ui.util.ColorCnst;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import org.jdesktop.swingx.color.ColorUtil;

/**
 *
 * @author dq
 */
public class BaseList extends ArrayList<Base> implements IPaintable {

    private Rectangle2D.Double rect;    
    private IColorProvider colorProvider;
    private Loc selection;
    private String name;
    private Color selectedBg = ColorCnst.SELECTED_TEXT_BG;
    private Color selectedFg = ColorCnst.SELECTED_TEXT_FG;

    public BaseList() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColorProvider(IColorProvider colorProvider) {
        this.colorProvider = colorProvider;
    }

    public void setSelection(Loc selection) {
        this.selection = selection;
    }
    
    public Loc getSelection(){
        return this.selection;
    }

    public void setStrikeout(int startPos, int endPos, boolean strikeout) {
        for (int pos = startPos; pos <= endPos; pos++) {
            Base text = get(pos - 1);
            text.setStrikeout(strikeout);
        }
    }

    @Override
    public Rectangle2D.Double getRect() {
        return rect;
    }

    @Override
    public void setRect(Rectangle2D.Double rect) {
        this.rect = rect;
    }

    public void setTexts(char[] chrs) {
        for (int i = 0; i < chrs.length; i++) {
            Base t = new Base();
            t.setText(chrs[i]);
            add(t);
        }
    }

    /*
     * @param startPos: 1-based, inclusive @param endPos: 1-base, inclusive
     */
    public void deleteRange(int startPos, int endPos, boolean maintainLength) {
        int before = size();
        for (int pos = endPos; pos >= startPos; pos--) {
            remove(pos - 1);
        }
        if (maintainLength) {
            while (size() < before) {
                add(new Base('-'));
            }
        }

    }

    public void deleteRange(int startPos, int endPos) {
        deleteRange(startPos, endPos, false);
    }

    public void paint(Graphics2D g) {
        paint(g, this.rect, 1, size());
    }

    @Override
    public void paint(Graphics2D g, Rectangle2D.Double drawingRect) {
        paint(g, drawingRect, 1, size());
    }

    @Override
    public void paint(Graphics2D g, int startPos, int endPos) {
        paint(g, this.rect, startPos, endPos);
    }

    @Override
    public void paint(Graphics2D g, Rectangle2D.Double drawingRect, int startPos, int endPos) {
        if (drawingRect == null || drawingRect.getWidth() == 0 || drawingRect.getHeight() == 0) {
            return;
        }

        this.rect = drawingRect;
        int iNext;
        final double unitWidth = rect.getWidth() / size();

        for (int index = startPos - 1; index < size() && index < endPos; index = iNext) {
            Base t = get(index);
            iNext = unitWidth >= 1 ? index + 1 : MathUtil.round(index + Math.ceil(1.0f / unitWidth));

            int x = (int) Math.round(rect.getX() + unitWidth * index);
            int x2 = (int) Math.round(rect.getX() + unitWidth * iNext);
            if (selection != null && selection.contains(index + 1)) {
                t.paint(g, x, rect.getY(), x2 - x, rect.getHeight(), selectedBg, selectedFg);
            } else {
                Color bgColor = null;
                Color fgColor = null;
                if (colorProvider != null) {
                    if (colorProvider.isBackgroundColor()) {
                        bgColor = colorProvider.getColor(t.getText());
                        if (bgColor != null) {
                            fgColor = ColorUtil.computeForeground(bgColor);
                        } else {
                            fgColor = Color.BLACK;
                        }
                    } else {
                        fgColor = colorProvider.getColor(t.getText());
                        bgColor = Color.WHITE;
                    }
                }
                t.paint(g, x, rect.getY(), x2 - x, rect.getHeight(), bgColor, fgColor);
            }
        }
    }

    public Point getCaretLoc(int pos) {
        Point ret = null;
        if (rect != null) {
            final float unitWidth = (float) (rect.getWidth() / size());
            double x = unitWidth * (pos - 1);
            ret = new Point(MathUtil.round(x + rect.getX()), MathUtil.round(rect.getY()));
        }
        return ret;
    }

    public Integer getCaretPos(int xParent) {
        Integer ret = null;
        if (rect != null) {
            final float unitWidth = (float) (rect.getWidth() / size());
            ret = MathUtil.round((xParent - rect.getX()) / unitWidth + 1);
            ret = Math.max(ret, 1);
        }
        return ret;
    }

    /*
     * @param xParent: relative x coordinate relative to its parent @ret the x
     * coordinate of the caret relative to its parent's coordinate
     */
    public Integer getCaretX(int xParent) {
        return getCaretX(xParent, 0);
    }

    public Integer getCaretX(int xParent, int posOffset) {
        Integer caretX;
        if (rect != null) {
            final float unitWidth = (float) (rect.getWidth() / size());
            caretX = MathUtil.round((xParent - rect.getX()) / unitWidth);
            caretX = MathUtil.round(caretX * unitWidth + rect.getX());
        } else {
            return null;
        }
        final float unitWidth = (float) (rect.getWidth() / size());
        int caretForwardX = Math.round(caretX + unitWidth * posOffset);
        int ret;
        if (caretForwardX > rect.getWidth() + rect.getX() || caretForwardX < rect.getX()) {
            ret = caretX;
        } else {
            ret = caretForwardX;
        }
        return ret;
    }
}
