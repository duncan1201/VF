/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.graphpane.ftrtrack.layout;

import com.gas.common.ui.core.RectList;
import com.gas.common.ui.util.MathUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.as.Feture;
import com.gas.domain.core.as.FetureKeyCnst;
import com.gas.domain.core.as.Lucation;
import com.gas.domain.ui.shape.IShape;
import com.gas.domain.ui.shape.layout.DefaultLayoutHelper;
import com.gas.domain.ui.shape.layout.ILayoutHelper;
import com.gas.domain.ui.shape.layout.LayoutHelperFinder;
import com.gas.main.ui.molpane.graphpane.GraphPane;
import com.gas.main.ui.molpane.graphpane.ftrtrack.FtrTrack;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.awt.Rectangle;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.swing.BorderFactory;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider (service=IFtrTrackLayout.class)
public class PrimerTrackLayout implements LayoutManager2, IFtrTrackLayout {

    private int maxY = 0;

    @Override
    public void addLayoutComponent(String name, Component comp) {
    }

    @Override
    public void removeLayoutComponent(Component comp) {
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        boolean set = parent.isPreferredSizeSet();
        GraphPane graphPane = UIUtil.getParent((FtrTrack) parent, GraphPane.class);
        boolean valid = parent.isValid();

        if (!set && graphPane != null && maxY == 0) {
            layoutContainer(parent);
        } else if (!valid && graphPane != null) {
            layoutContainer(parent);
        }
        Dimension ret = new Dimension(0, maxY);
        return ret;
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return preferredLayoutSize(parent);
    }

    @Override
    public void layoutContainer(Container parent) {
        FtrTrack ft = (FtrTrack) parent;

        if (ft.getTrackName() == null || ft.getTrackName().isEmpty()) {
            throw new IllegalArgumentException("track name cannot be empty");
        }

        GraphPane graphPane = UIUtil.getParent(ft, GraphPane.class);
        Insets iPadding = graphPane.getPadding();
        ft.setBorder(BorderFactory.createEmptyBorder(iPadding.top, iPadding.left, iPadding.bottom, iPadding.right));

        List<IShape> shapes = ft.getArrows();
        if (shapes.isEmpty()) {
            return;
        }
        ILayoutHelper layoutHelper = LayoutHelperFinder.find((Feture)shapes.get(0).getData());
        
        maxY = 0;
        int totalPos = graphPane.getAs().getLength();

        Map<String, Boolean> labelVisibleMap = graphPane.getLabelVisibilityMap();

        boolean isLabelVisible = labelVisibleMap.get(ft.getTrackName().toUpperCase(Locale.ENGLISH));

        Rectangle displayRect = UIUtil.getBoundsNoBorder(parent);

        Dimension displaySize = displayRect.getSize();

        if (displaySize.width < 0 || displaySize.height < 0) {
            return;
        }
        int desiredStartPixel = 0;        
        RectList rectList = new RectList();
        
        for (int i = 0; i < shapes.size(); i++) {

            IShape current = shapes.get(i);
            Feture feture = (Feture)current.getData();
            boolean forward = feture.getLucation().getStrand();
            
            Dimension minTxtSize = layoutHelper.getMinTextDimension(current);

            int desiredWidth = layoutHelper.getDesiredWidth(current, IShape.TEXT_LOC.INSIDE, displayRect, totalPos);

            if (!isLabelVisible) {
                current.setTextLoc(IShape.TEXT_LOC.NONE);
                desiredStartPixel = layoutHelper.getDesiredStartPixel(current, current.getTextLoc(), displayRect, totalPos);

            } else if (minTxtSize.width < desiredWidth) {
                if(forward){
                    current.setTextLoc(IShape.TEXT_LOC.ABOVE);
                }else{
                    current.setTextLoc(IShape.TEXT_LOC.BELOW);
                }
                desiredStartPixel = layoutHelper.getDesiredStartPixel(current, current.getTextLoc(), displayRect, totalPos);

            } else {
                current.setTextLoc(IShape.TEXT_LOC.BEFORE);
                desiredStartPixel = layoutHelper.getDesiredStartPixel(current, current.getTextLoc(), displayRect, totalPos);
                if (desiredStartPixel < 0) {
                    current.setTextLoc(IShape.TEXT_LOC.AFTER);
                    desiredStartPixel = layoutHelper.getDesiredStartPixel(current, current.getTextLoc(), displayRect, totalPos);
                }
            }

            boolean isOccupied = true;
            int j = 0;
            Rectangle arrowRect = null;
            while (isOccupied) {
                int desiredHeight = layoutHelper.getDesiredHeight(current);
                int y = maxY + desiredHeight - 1;
                y = Math.max(j, 0);
                int width = layoutHelper.getDesiredWidth(current, current.getTextLoc(), displayRect, totalPos);
                
                arrowRect = new Rectangle(desiredStartPixel, y, width, desiredHeight);
                isOccupied = rectList.intersects(arrowRect);
                j++;
            }

            rectList.add(arrowRect);
            current.setBounds(arrowRect);
            int endY = MathUtil.round(arrowRect.getY() + arrowRect.getHeight() - 1);
            if (maxY < endY) {
                maxY = endY;
            }
        }
        UIUtil.setPreferredHeight(ft, maxY);


    }

    @Override
    public void addLayoutComponent(Component comp, Object constraints) {
    }

    @Override
    public Dimension maximumLayoutSize(Container target) {
        return null;
    }

    @Override
    public float getLayoutAlignmentX(Container target) {
        return 0.5f;
    }

    @Override
    public float getLayoutAlignmentY(Container target) {
        return 0.5f;
    }

    @Override
    public void invalidateLayout(Container target) {
        maxY = 0;
    }

    @Override
    public String getFetureType() {
        return FetureKeyCnst.PRIMER_BINDING_SITE;
    }
}
