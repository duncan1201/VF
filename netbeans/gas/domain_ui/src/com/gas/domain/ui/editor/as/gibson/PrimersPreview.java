/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor.as.gibson;

import com.gas.common.ui.core.StringList;
import com.gas.common.ui.light.Text;
import com.gas.common.ui.light.TextList;
import com.gas.common.ui.util.ColorCnst;
import com.gas.common.ui.util.ColorGenerator;
import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.PathUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.AnnotatedSeqList;
import com.gas.domain.core.primer3.OverlapPrimer;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.SwingConstants;

/**
 *
 * @author dq
 */
public class PrimersPreview extends JComponent {

    private AnnotatedSeqList asList = new AnnotatedSeqList();
    DoubleBarList bars = new DoubleBarList();
    OligoShapes oligoShapes = new OligoShapes();
    private TextList textList = new TextList();
    final FontMetrics fontMetrics = FontUtil.getDefaultFontMetrics();
    final int OLIGO_LENGTH = Math.round(fontMetrics.getHeight() * 1.4f);
    final int OLIGO_HEIGHT = Math.round(OLIGO_LENGTH * 0.5f);

    public PrimersPreview(AnnotatedSeqList asList) {
        this.asList = asList;
        int horizontalGap = fontMetrics.getHeight() * 2;
        setBorder(BorderFactory.createEmptyBorder(0, horizontalGap, 0, horizontalGap));
        createShapes();
        hookupListeners();
    }
    
    /**
     * @return the two colors for the overlap primer
     */
    List<Color> getColors(OverlapPrimer overlapPrimer){
        List<Color> ret = new ArrayList<Color>();
        for(int i = 0; i < oligoShapes.size(); i++){
            OligoShape oligoShape = oligoShapes.get(i);
            if(oligoShape.as.getName().equalsIgnoreCase(overlapPrimer.getName())){
                Color curColor = bars.getDoubleBar(oligoShape.as).color;
                if(overlapPrimer.isForward()){
                    AnnotatedSeq asPrev = asList.prev(oligoShape.as);
                    Color prevColor = bars.getDoubleBar(asPrev).color;
                    ret.add(prevColor);
                    ret.add(curColor);
                }else{
                    AnnotatedSeq asNext = asList.next(oligoShape.as);
                    Color nextColor = bars.getDoubleBar(asNext).color;                    
                    ret.add(curColor);
                    ret.add(nextColor);
                }
            }
        }
        return ret;
    }

    void moveLeft(AnnotatedSeq as) {
        bars.moveLeft(as);        
        oligoShapes.reorder(asList);
    }

    void moveRight(AnnotatedSeq as) {
        bars.moveRight(as);        
        oligoShapes.reorder(asList);
    }
    
    void clearSelection(){
        for (DoubleBar bar : bars) {
            if(bar.isSelected()){
                bar.setSelected(false);
            }
        }
        for (OligoShape os: oligoShapes){
            if(os.selected){
                os.selected = false;
            }
        }
        repaint();
    }
    
    void setSelectedPrimer(String asName, boolean forward){
        for(AnnotatedSeq as: asList){
            if(as.getName().equals(asName)){
                setSelectedPrimer(as, forward);
                break;
            }
        }
    }
    
    void setSelectedPrimer(AnnotatedSeq as, boolean forward){
        for (OligoShape os: oligoShapes){            
            os.selected = os.as == as && os.forward == forward;            
        }
        repaint();
    }
    
    void setSelectedPrimerPair(AnnotatedSeq as){
        for (OligoShape os: oligoShapes){            
            os.selected = os.as == as;            
        }
        repaint();
    }

    AnnotatedSeq getSelectedAs() {
        for (DoubleBar bar : bars) {
            if (bar.isSelected()) {
                return bar.as;
            }
        }
        return null;
    }

    private void hookupListeners() {
        addMouseListener(new PrimersPreviewListeners.MouseAdpt(this));
        addMouseMotionListener(new PrimersPreviewListeners.MouseAdpt(this));
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (asList.isEmpty()) {
            return;
        }

        Dimension size = getSize();
        final Insets insets = getInsets();

        int unitWidth = (int) Math.floor((size.width - insets.left - insets.right) * 1.0 / asList.size());
        Graphics2D g2d = (Graphics2D) g;

        // arrange 
        for (int i = 0; i < bars.size(); i++) {
            DoubleBar bar = bars.get(i);
            bar.x = i * unitWidth + insets.left;
            bar.y = Math.round(size.height / 3.0f);
            bar.width = unitWidth;
            bar.height = Math.round(size.height / 3.0f);
        }

        for (int i = 0; i < oligoShapes.size(); i++) {
            DoubleBar bar = bars.get(i / 2);

            OligoShape os = oligoShapes.get(i);
            if (i % 2 == 0) {
                os.x = bar.x - Math.round(OLIGO_LENGTH * 0.5f);
                os.y = bar.y - OLIGO_HEIGHT;
                os.forward = true;
            } else {
                os.x = bar.x + bar.width - Math.round(OLIGO_LENGTH * 0.5f);
                os.y = bar.y + bar.height;
                os.forward = false;
            }
            os.width = OLIGO_LENGTH;
            os.height = OLIGO_HEIGHT;
        }

        FontMetrics myFm = g2d.getFontMetrics();
        int fontHeight = myFm.getHeight();
        for (int i = 0; i < textList.size(); i++) {
            DoubleBar bar = bars.get(i);
            AnnotatedSeq as = asList.get(i);
            Text text = textList.get(i);
            int strWidth = myFm.stringWidth(as.getName());
            if (bar.isSelected()) {
                text.setFgColor(ColorCnst.AMBER);
            } else {
                text.setFgColor(Color.BLACK);
            }
            text.setX(Math.round(bar.centerX() - strWidth * 0.5f));
            text.setY(size.height - fontHeight);
            text.setWidth(strWidth);
            text.setHeight(fontHeight);
            text.setStr(as.getName());
        }
        textList.setFont(myFm.getFont());

        oligoShapes.paint(g2d);
        textList.paint(g2d, SwingConstants.CENTER, false);
        bars.paint(g2d);
    }

    private void createShapes() {
        List<Color> colors = ColorGenerator.generate(asList.size());
        for (int i = 0; i < asList.size(); i++) {
            AnnotatedSeq as = asList.get(i);
            DoubleBar bar = new DoubleBar();
            bar.color = colors.get(i);
            bar.name = as.getName();
            bar.as = as;
            bars.add(bar);
        }

        for (int i = 0; i < bars.size(); i++) {
            DoubleBar bar = bars.get(i);

            OligoShape forward = new OligoShape();
            forward.as = bar.as;
            forward.x = bar.x - Math.round(OLIGO_LENGTH * 0.5f);
            forward.y = bar.y - OLIGO_HEIGHT;
            forward.width = OLIGO_LENGTH;
            forward.height = OLIGO_HEIGHT;

            oligoShapes.add(forward);

            OligoShape reverse = new OligoShape();
            reverse.as = bar.as;
            reverse.x = bar.x + bar.width - Math.round(OLIGO_LENGTH * 0.5f);
            reverse.y = bar.y + bar.height;
            reverse.width = OLIGO_LENGTH;
            reverse.height = OLIGO_HEIGHT;
            reverse.forward = false;
            oligoShapes.add(reverse);
        }

        for (int i = 0; i < bars.size(); i++) {
            AnnotatedSeq as = asList.get(i);
            Text text = new Text();
            text.setStr(as.getName());
            textList.add(text);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        final FontMetrics fm = FontUtil.getFontMetrics(this);
        Insets insets = getInsets();
        final int fontHeight = fm.getHeight();
        int minWidth = fontHeight * asList.size() * 6 + insets.left + insets.right;
        StringList strList = asList.getNames();
        String longestName = strList.longest();
        int optWidth = fm.stringWidth(longestName) * asList.size() + insets.left + insets.right;
        
        int _width = Math.max(minWidth, optWidth);
        int height = fontHeight * 5;
        Dimension ret = new Dimension(_width, height);
        return ret;
    }

    static class DoubleBar {

        int x;
        int y;
        int width;
        int height;
        String name;
        Color color = Color.BLACK;
        float gapRatio = 0.2f;
        AnnotatedSeq as;
        boolean selected;

        DoubleBar() {
        }

        boolean contains(Point pt) {
            return UIUtil.contains(x, y, width, height, pt);
        }

        int centerX() {
            return Math.round(x + width * 0.5f);
        }

        public boolean isSelected() {
            return selected;
        }

        void setSelected(boolean selected) {
            this.selected = selected;
        }

        void paint(Graphics2D g2d) {
            g2d.setColor(color);
            int barHeight = Math.round(height * (1 - gapRatio) * 0.5f);
            g2d.fillRect(x, y, width, barHeight);
            if (selected) {
                g2d.setColor(ColorCnst.AMBER);
                g2d.drawRect(x, y, width, barHeight);
                g2d.setColor(color);
            }
            g2d.fillRect(x, y + Math.round(height * gapRatio + barHeight), width, barHeight);
            if (selected) {
                Stroke old = g2d.getStroke();
                g2d.setStroke(new BasicStroke(2f));
                g2d.setColor(ColorCnst.AMBER);
                g2d.drawRect(x, y + Math.round(height * gapRatio + barHeight), width, barHeight);
                g2d.setColor(color);
                g2d.setStroke(old);
            }
        }
    }

    static class DoubleBarList extends ArrayList<DoubleBar> {

        public void paint(Graphics2D g2d) {
            for (DoubleBar bar : this) {
                bar.paint(g2d);
            }
        }
        
        DoubleBar getDoubleBar(AnnotatedSeq _as){
            for (DoubleBar b: this){
                if(b.as == _as){
                    return b;
                }
            }
            return null;
        }

        DoubleBar getDoubleBar(Point pt) {
            for (DoubleBar b : this) {
                boolean contains = b.contains(pt);
                if (contains) {
                    return b;
                }
            }
            return null;
        }

        void setSelected(DoubleBar doubleBar) {
            for (DoubleBar b : this) {
                b.setSelected(doubleBar == b);
            }
        }

        void moveLeft(AnnotatedSeq as) {
            for (int i = 0; i < size(); i++) {
                DoubleBar bar = get(i);
                if (bar.as == as) {
                    bar = remove(i);
                    if (i - 1 > -1) {
                        add(i - 1, bar);
                    } else {
                        add(bar);
                    }
                    break;
                }
            }
        }

        void moveRight(AnnotatedSeq as) {
            for (int i = 0; i < size(); i++) {
                DoubleBar bar = get(i);
                if (bar.as == as) {
                    bar = remove(i);
                    if (i + 1 == size()) {
                        add(bar);
                    } else if (i + 1 < size()) {
                        add(i + 1, bar);
                    } else {
                        add(0, bar);
                    }
                    break;
                }
            }
        }
    }

    /**
     * -----| ------|
     */
    static class OligoShape {

        int x;
        int y;
        int width;
        int height;
        boolean forward = true;
        boolean selected;
        AnnotatedSeq as;

        OligoShape() {
        }
        
        boolean contains(Point pt){
            boolean ret = UIUtil.contains(x, y, width, height, pt);
            return ret;
        }

        void paint(Graphics2D g2d) {
            g2d.setColor(ColorCnst.DARTMOUTH_GREEN);
            GeneralPath path;
            if (forward) {
                path = createForwardPath();
            } else {
                path = createReversePath();
            }
            g2d.fill(path);
            if (selected) {
                Stroke old = g2d.getStroke();
                BasicStroke stroke = new BasicStroke(2.0f);
                g2d.setStroke(stroke);
                g2d.setColor(ColorCnst.AMBER);
                g2d.draw(path);
                g2d.setStroke(old);
            }
        }

        private GeneralPath createReversePath() {
            GeneralPath path = new GeneralPath();
            path.moveTo(x + width, y);
            path.lineTo(x, y);
            Point2D curPt = PathUtil.relativelyLineTo(path, height, height);
            if (!selected) {
                curPt = PathUtil.relativelyLineTo(path, 0, -height * 0.5);
            }
            path.lineTo(x + width, curPt.getY());
            path.closePath();
            return path;
        }

        private GeneralPath createForwardPath() {
            GeneralPath path = new GeneralPath();
            path.moveTo(x, y + height);
            path.lineTo(x + width, y + height);
            Point2D curPt = PathUtil.relativelyLineTo(path, -height, -height);
            if (!selected) {
                curPt = PathUtil.relativelyLineTo(path, 0, height * 0.5);
            }
            path.lineTo(x, curPt.getY());
            path.closePath();
            return path;
        }
    }

    static class OligoShapes extends ArrayList<OligoShape> {

        OligoShape getOligoShape(Point pt){
            for(OligoShape os: this){
                boolean contains = os.contains(pt);
                if(contains){
                    return os;
                }
            }
            return null;
        }
        
        void reorder(AnnotatedSeqList asList){
            List<OligoShape> tmp = new ArrayList<OligoShape>();
            for(AnnotatedSeq as: asList){
                tmp.addAll(getOligoShapes(as));
            }
            clear();
            addAll(tmp);
        }   
        
        /**
         * @return forward first, then reverse
         */
        List<OligoShape> getOligoShapes(AnnotatedSeq as){
            List<OligoShape> ret = new ArrayList<OligoShape>();
            for(OligoShape os: this){
                if(os.as == as){
                    if(os.forward){
                        ret.add(0, os);
                    }else{
                        ret.add(os);
                    }
                }
            }
            return ret;
        }
        
        void clearSelection(){
            for(OligoShape os: this){
                os.selected = false;
            }
        }
        
        void setSelected(OligoShape _os){
            for(OligoShape os: this){                
                os.selected = os == _os;                
            }
        }
        
        void paint(Graphics2D g2d) {
            for (OligoShape os : this) {
                os.paint(g2d);
            }
        }                
    }
}
