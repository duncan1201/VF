/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.ringpane;

import com.gas.common.ui.core.RectList;
import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.MathUtil;
import com.gas.common.ui.util.RectHelper;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.as.Feture;
import com.gas.common.ui.util.Pref;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Collections;
import java.util.Iterator;
import javax.swing.JComponent;

/**
 *
 * @author dq
 */
public class LabelListComp extends JComponent {

    private LabelList labelList = new LabelList();
    private Float fontSize = Pref.CommonPtyPrefs.getInstance().getAnnotationLabelSize();

    public LabelListComp() {
        hookupListeners();
    }

    private void hookupListeners() {
        addPropertyChangeListener(new LabelListCompListeners.PtyListener());
    }
    
    public void setSeedColor(String featureKey, Color color){
        boolean repaint = false;
        for(Label label: labelList){
            Object data = label.getData();
            if(data != null){
                Ring ring = (Ring)data;
                Feture feture = (Feture)ring.getData();
                if(feture.getKey().equalsIgnoreCase(featureKey)){
                    label.setTextColor(color);
                    repaint = true;
                }
            }
        }
        if(repaint){
            repaint();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        Dimension size = getSize();
        if (size.width < 0 || size.height < 0) {
            return;
        }
        labelList.paint((Graphics2D) g);
    }

    public void setFontSize(Float size) {
        Float old = this.fontSize;
        this.fontSize = size;
        firePropertyChange("fontSize", old, this.fontSize);
    }

    public Float getFontSize() {
        return fontSize;
    }

    public LabelList getLabelList() {
        return labelList;
    }

    public void setSelected(Label lable) {
        labelList.setSelected(lable);
        repaint();
    }

    public void setLabelList(LabelList labelList) {
        LabelList old = this.labelList;
        this.labelList = labelList;
        firePropertyChange("labelList", old, this.labelList);
    }

    public Label updateSelection(Feture feture){
        Label ret = labelList.updateSelection(feture);
        repaint();
        return ret;
    }
    
    public Label updateSelection(Point ptScreen) {
        Point pt = UIUtil.convertPointFromScreen(ptScreen, this);
        Label ret = labelList.updateSelection(pt);
        repaint();
        return ret;
    }
    
    public Label getLabel(Ring ring){
        return labelList.getLabel(ring);
    }
    
    public Label getSelected(){
        return labelList.getSelected();
    }

    public Label getLabel(Point ptScreen) {
        Point pt = UIUtil.convertPointFromScreen(ptScreen, this);
        Label ret = labelList.getLabel(pt);
        return ret;
    }

    public void layoutLabels(float radius) {
        RectList rectList = new RectList();
        if (labelList.isEmpty()) {
            return;
        }
        // layout the labels
        final Rectangle bounds = getBounds();
        final Point2D.Double center = new Point2D.Double(bounds.width * 0.5, bounds.height * 0.5);
        Collections.sort(labelList, new Label.CenterAngleComparator());
        Iterator<Label> labelItr = labelList.iterator();
        final FontMetrics fm = FontUtil.getFontMetrics(labelList.get(0).getTextFont());
        final double degDelta = MathUtil.getAngle(fm.getHeight() * 0.5, radius, false);
        while (labelItr.hasNext()) {
            Label label = labelItr.next();
            Ring ring = (Ring) label.getData();
            final Dimension dSize = label.getDesiredSize();
            final float angle = ring.getCenterAngle();
            AffineTransform tx = AffineTransform.getRotateInstance(Math.toRadians(angle), center.getX(), center.getY());

            Point srcPt = new Point((int) center.getX(), (int) Math.round(bounds.getCenterY() - radius));
            Point loc = UIUtil.transform(tx, srcPt);
            Double adjustedAngle = MathUtil.normalizeDegree(angle);

            translate(loc, adjustedAngle, dSize);
            Rectangle rect = new Rectangle(loc, dSize);
            rect = RectHelper.getNewRect2(bounds, adjustedAngle.floatValue(), radius, degDelta, rect, rectList);
            if (rect != null) {
                rectList.add(rect);
            }

            if (rect != null) {
                label.setBounds(rect);
            }else{
                label.setSize(0, 0);
            }
        }
    }

    private void translate(Point pt, double angle, Dimension dSize) {
        if (angle >= 0 && angle <= 90) {
            pt.translate(0, -dSize.height);

        } else if (angle > 90 && angle <= 180) {
            pt.translate(0, dSize.height);

        } else if (angle > 180 && angle <= 270) {
            pt.translate(-dSize.width, dSize.height);

        } else if (angle > 270 && angle <= 360) {
            pt.translate(-dSize.width, -dSize.height);

        }
    }
}
