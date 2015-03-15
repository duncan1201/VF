/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.ringpane;

import com.gas.common.ui.util.FontUtil;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;
import javax.swing.JComponent;

/**
 *
 * @author dq
 */
public class Label extends JComponent {

    private String text ;
    private Insets margin = new Insets(0, 2, 0, 2);
    private Object data;
    private Color borderColor;
    private Color textColor = Color.BLACK;
    private Font textFont;
    private java.util.List<ActionListener> actionListeners = new ArrayList<ActionListener>();
    private boolean selected;

    public Label() {
        hookupListeners();        
    }

    private void hookupListeners() {
        addPropertyChangeListener(new LabelListeners.PtyChangeListener());
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        boolean old = this.selected;
        this.selected = selected;
        firePropertyChange("selected", old, this.selected);
    }

    public Font getTextFont() {
        if(textFont == null){
            textFont = FontUtil.getDefaultSansSerifFont().deriveFont(FontUtil.getDefaultFontSize());
        }
        return textFont;
    }

    public void setTextFont(Font textFont) {
        this.textFont = textFont;
    }

    public java.util.List<ActionListener> getActionListeners() {
        return actionListeners;
    }
    
    public void setTextColor(Color c){
        this.textColor = c;
    }
    
    public Color getTextColor(){
        return this.textColor;
    }

    public void setBorderColor(Color borderColor) {
        Color old = this.borderColor;
        this.borderColor = borderColor;
        firePropertyChange("borderColor", old, this.borderColor);
    }

    public void addActionListener(ActionListener actionListener) {
        actionListeners.add(actionListener);
    }

    public Object getData() {
        return data;
    }

    /*
     * It's the thing it points to. Usually it's of type "Ring"
     * 
     */
    public void setData(Object data) {
        this.data = data;
    }
    
    public void paint2(Graphics2D g){
        if(text == null){
            return;
        }

        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        final Dimension size = getSize();
        final Point loc = getLocation();
        if(borderColor != null){
            g.setColor(borderColor);            
            g.drawRect(loc.x, loc.y, size.width - 1, size.height - 1);
        }
        
        final FontMetrics fm = FontUtil.getFontMetrics(getTextFont());
        final int ascent = fm.getAscent();
        final int descent = fm.getDescent();
        final int height = fm.getHeight();
        final int strWidth = fm.stringWidth(text);
        int x ;
        if(size.width > strWidth){
            x = 1;
        }else{
            x = 1;
        }
        int y = Math.round(ascent + (size.height - height) * 0.5f);
        if(isOpaque()){
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, size.width, size.height);
        }             
        g.setColor(Color.BLACK);
        Container parent = getParent();
        g.setFont(getTextFont());
        g.setColor(textColor);
        if(parent == null){
            g.drawString(text, loc.x + x, loc.y + y);        
        }else{
            g.drawString(text, x, y);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        paint2((Graphics2D)g);
    }

    public Color getBorderColor() {        
        return borderColor;
    }

    public Point getLeftMiddle() {
        final Rectangle rect = getBounds();
        final Point loc = rect.getLocation();
        loc.y = loc.y + Math.round(rect.height * 0.5f);
        return loc;
    }

    public Point getRightMiddle() {
        final Rectangle rect = getBounds();
        final Point loc = rect.getLocation();
        loc.x = loc.x + rect.width;
        loc.y = loc.y + Math.round(rect.height * 0.5f);
        return loc;
    }

    public Point getCenter() {
        final Point ret = getLocation();
        final Dimension size = getSize();
        ret.x = ret.x + Math.round(size.width * 0.5f);
        ret.y = ret.y + Math.round(size.height * 0.5f);
        return ret;
    }

    public Point getTopMiddle() {
        final Rectangle rect = getBounds();
        final Point loc = rect.getLocation();
        loc.x = loc.x + Math.round(rect.width * 0.5f);
        return loc;
    }

    public Point getBottomMiddle() {
        final Rectangle rect = getBounds();
        final Point loc = rect.getLocation();
        loc.x = loc.x + Math.round(rect.width * 0.5f);
        loc.y = loc.y + rect.height;
        return loc;
    }

    public Dimension getDesiredSize() {
        final FontMetrics fm = FontUtil.getFontMetrics(getTextFont());         
        final int strWidth = fm.stringWidth(text == null ? "" : text);
        int x = strWidth + margin.left + margin.right;
        int y = fm.getHeight() + margin.top + margin.bottom;
        Dimension ret = new Dimension(x, y);
        return ret;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    
    protected static class CenterAngleComparator implements Comparator<Label>{

        @Override
        public int compare(Label o1, Label o2) {
            int ret = 0;
            Object obj1 = o1.getData();
            Object obj2 = o2.getData();
            if(obj1 instanceof Ring && obj2 instanceof Ring){
                Ring ring1 = (Ring)obj1;
                Ring ring2 = (Ring)obj2;
                Float a1 = ring1.getCenterAngle();
                Float a2 = ring2.getCenterAngle();

                ret = a1.compareTo(a2);
            }
            return ret;
        }
    }
}
