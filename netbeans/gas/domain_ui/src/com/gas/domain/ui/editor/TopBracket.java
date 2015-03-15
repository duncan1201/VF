/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor;

import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.util.LocUtil;
import com.gas.common.ui.util.FontUtil;
import com.gas.domain.core.ren.RMap;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.JComponent;

/**
 *
 * @author dq
 */
public class TopBracket extends JComponent {

    private Object data;
    private String text;
    private Integer startPos;
    private Integer endPos;
    private boolean selected;
    private Color highlightedColor = Color.BLUE;
    private Color color = Color.BLACK;
    private Rectangle drawingRect;

    public TopBracket() {
        addPropertyChangeListener(new TopBracketListeners.PtyChangeListener(this));
    }

    @Override
    public void setBounds(Rectangle rect) {
        drawingRect = new Rectangle(rect);
        FontMetrics fm = FontUtil.getFontMetrics(getFont());
        if (text != null) {
            int strWidth = fm.stringWidth(text);
            if (strWidth > rect.width) {
                rect.grow(strWidth - rect.width, 0);
            }
        }
        super.setBounds(rect);
    }

    @Override
    public void paintComponent(Graphics g) {
        Dimension size = getSize();
        Graphics2D g2d = (Graphics2D) g;
        GeneralPath path = createPath(g2d);
        if (selected) {
            BasicStroke old = (BasicStroke) g2d.getStroke(); // backup            
            BasicStroke stroke = new BasicStroke(2);
            g2d.setStroke(stroke);
            g2d.setColor(highlightedColor);
            g2d.draw(path);
            g2d.setStroke(old); // restore
        } else {
            g2d.setColor(color);
            g2d.draw(path);
        }
        if (text != null) {
            if (selected) {
                Font font = getFont();
                if (font.getStyle() != Font.BOLD) {
                    font = font.deriveFont(Font.BOLD);
                    setFont(font);
                }
            } else {
                Font font = getFont();
                if (font.getStyle() != Font.PLAIN) {
                    font = font.deriveFont(Font.PLAIN);
                    setFont(font);
                }
            }

            FontMetrics fm = FontUtil.getFontMetrics(getFont());
            int strWidth = fm.stringWidth(text);
            int x = Math.round((size.width - strWidth) * 0.5f);
            g2d.drawString(text, x, fm.getAscent());
        }
    }

    public Object getData() {
        return data;
    }

    /**
     * @param data usually it's of type {@link com.gas.domain.core.ren.RMap.Entry}
     */
    public void setData(Object data) {
        this.data = data;
    }

    private GeneralPath createPath(Graphics2D g) {
        Rectangle bounds = getBounds();
        Dimension size = getSize();
        Font font = getFont();
        FontMetrics fm = FontUtil.getFontMetrics(font);
        final int fmHeight = fm.getHeight();
        final int y = fmHeight + 1;
        GeneralPath ret = new GeneralPath();

        BasicStroke bs = (BasicStroke)g.getStroke();
        ret.moveTo(drawingRect.x - bounds.x, size.height);
        ret.lineTo(drawingRect.x - bounds.x, y);
        ret.lineTo(drawingRect.x - bounds.x + drawingRect.width - bs.getLineWidth() , y);
        ret.lineTo(drawingRect.x - bounds.x + drawingRect.width - bs.getLineWidth() , size.height);
        return ret;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        boolean old = this.selected;
        this.selected = selected;
        firePropertyChange("selected", old, this.selected);
    }
    
    @Override
    public Font getFont(){
        Font ret = super.getFont();
        if(ret == null){
            ret = FontUtil.getDefaultSansSerifFont().deriveFont(FontUtil.getDefaultFontSize());
            //setFont(ret);
        }
        return ret;
    }

    public int getDisiredHeight() {
        Font font = getFont();        
        FontMetrics fm = FontUtil.getFontMetrics(font);
        return Math.round(fm.getHeight() * 1.6f);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        String old = this.text;
        this.text = text;
        firePropertyChange("text", old, this.text);
    }

    public Integer getEndPos() {
        return endPos;
    }

    public void setEndPos(Integer endPos) {
        this.endPos = endPos;
    }

    public Integer getStartPos() {
        return startPos;
    }

    public void setStartPos(Integer startPos) {
        this.startPos = startPos;
    }

    public static class StartPosComparator implements Comparator<TopBracket> {

        @Override
        public int compare(TopBracket o1, TopBracket o2) {
            return o1.getStartPos().compareTo(o2.getStartPos());
        }
    }

    public static List<TopBracket> compact(List<TopBracket> tbs) {
        List<TopBracket> ret = new ArrayList<TopBracket>();

        Collections.sort(tbs, new StartPosComparator());

        boolean startOver = true;
        for (int i = 0; i < tbs.size(); i++) {
            TopBracket tb = tbs.get(i);
            if (startOver) {
                ret.add(tb);
                tbs.remove(i);
                i--;
                startOver = false;
            } else {
                TopBracket last = ret.get(ret.size() - 1);
                boolean joint = LocUtil.isOverlapped(tb.getStartPos(), tb.getEndPos(), last.getStartPos(), last.getEndPos());
                if (!joint) {
                    ret.add(tb);
                    tbs.remove(i);
                    i--;
                } else if (i == 0 && i == tbs.size() - 1) {
                    ret.add(tb);
                    tbs.remove(i);
                    i--;
                }

                if (i == tbs.size() - 1 && !tbs.isEmpty()) {
                    i = -1;
                    startOver = true;
                }
            }
        }

        return ret;
    }
}
