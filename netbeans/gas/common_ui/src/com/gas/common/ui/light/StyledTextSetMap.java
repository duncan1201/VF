/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.light;

import com.gas.common.ui.color.IColorProvider;
import com.gas.common.ui.util.BioUtil;
import com.gas.common.ui.util.FontUtil;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.util.Collections;
import java.util.Iterator;
import java.util.TreeMap;

/**
 *
 * @author dq
 */
public class StyledTextSetMap extends TreeMap<Integer, StyledTextList> implements IPaintable {

    private Double maxBits;
    private Integer maxPos;
    private boolean dna;
    private boolean scaleUp;
    private Rectangle2D.Double rect;
    private IColorProvider colorProvider;

    public void add(Integer pos, StyledText t) {

        if (!containsKey(pos)) {
            put(pos, new StyledTextList());
        }
        get(pos).add(t);
        updateMetaData(pos, get(pos));
    }

    public void sort() {
        Iterator<Integer> itr = keySet().iterator();
        while (itr.hasNext()) {
            Integer pos = itr.next();
            StyledTextList tList = get(pos);
            Collections.sort(tList, new StyledText.BitSorter());
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

    public void setHeight(double height) {
        this.rect.setRect(rect.getX(), rect.getY(), rect.getWidth(), height);
    }

    public void setColorProvider(IColorProvider colorProvider) {
        this.colorProvider = colorProvider;
    }

    @Override
    public StyledTextList remove(Object pos) {
        StyledTextList ret = super.remove(pos);
        if (ret != null) {
            Double bits = ret.getTotalBits();
            if (bits == maxBits) {
                reinitMetaData();
            }
            if (pos.equals(maxPos)) {
                maxPos--;
            }
        }
        return ret;
    }

    private void reinitMetaData() {
        maxBits = null;
        maxPos = null;
        Iterator<Integer> itr = keySet().iterator();
        while (itr.hasNext()) {
            Integer pos = itr.next();
            StyledTextList tList = get(pos);
            if (maxBits == null || maxBits < tList.getTotalBits()) {
                maxBits = tList.getTotalBits();
            }
            if (maxPos == null || maxPos < pos) {
                maxPos = pos;
            }
        }
    }

    private void updateMetaData(Integer pos, StyledTextList tList) {
        if (maxBits == null || maxBits < tList.getTotalBits()) {
            maxBits = tList.getTotalBits();
        }
        if (maxPos == null || maxPos < pos) {
            maxPos = pos;
        }
    }

    public Double getMaxBits() {
        return maxBits;
    }

    public void setDna(boolean dna) {
        this.dna = dna;
    }

    public void setScaleUp(boolean scaleUp) {
        this.scaleUp = scaleUp;
    }

    @Override
    public void paint(Graphics2D g, Rectangle2D.Double rect) {
        paint(g, rect, 1, maxPos);
    }

    @Override
    public void paint(Graphics2D g, int startPos, int endPos) {
        paint(g, this.rect, startPos, endPos);
    }

    @Override
    public void paint(Graphics2D g, Rectangle2D.Double rect, int startPos, int endPos) {
        int max = 0;
        if (dna) {
            max = 4;
        } else {
            max = 20;
        }
        if (rect == null || rect.getHeight() == 0 || rect.getWidth() == 0) {
            return;
        }
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        this.rect = new Rectangle2D.Double(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());

        final Double unitWidth = 1.0 * rect.getWidth() / maxPos;
        boolean paintLetters = unitWidth > FontUtil.getDefaultFontMetrics().charWidth('T');

        for (int pos = startPos; pos <= maxPos && pos <= endPos; pos++) {
            StyledTextList tList = get(pos);
            if (tList == null) {
                continue;
            }
            Iterator<StyledText> itr = tList.iterator();
            double totalHeight = 0;
            while (itr.hasNext()) {
                StyledText styledText = itr.next();
                Double x = rect.getX() + unitWidth * (pos - 1);
                Double height;
                if (scaleUp) {
                    height = rect.getHeight() * styledText.getBits() / maxBits;
                } else {
                    height = rect.getHeight() * styledText.getBits() / max;
                }
                Double y = rect.getY() + rect.getHeight() - height - totalHeight;
                Color fgColor = Color.BLACK;
                if (colorProvider != null) {
                    fgColor = colorProvider.getColor(styledText.getText());
                }

                styledText.paint(g, x, y, unitWidth, height, fgColor, paintLetters);
                totalHeight += height;
            }
        }

    }

    private void handleVisible(Graphics2D g, int startPos, int endPos, double unitWidth, int max) {
    }
}
