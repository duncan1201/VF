/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.graphpane.ftrtrack.layout;

import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.as.Feture;
import com.gas.domain.core.as.Lucation;
import com.gas.domain.ui.shape.ArrowComparators;
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
import java.awt.Point;
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
public class AdjacentConservedLayout implements LayoutManager2, IFtrTrackLayout {

    private boolean adjustingPreferredLayoutSize = false;
    private int y = 0;

    @Override
    public void addLayoutComponent(String name, Component comp) {
    }

    @Override
    public void removeLayoutComponent(Component comp) {
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        adjustingPreferredLayoutSize = true;
        Dimension ret = new Dimension();

        layoutContainer(parent);
        ret.height = y;
        adjustingPreferredLayoutSize = false;
        return ret;
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        Dimension ret = new Dimension();

        return ret;
    }

    @Override
    public void layoutContainer(Container parent) {
        FtrTrack ftrTrack = (FtrTrack) parent;

        GraphPane graphPane = UIUtil.getParent(ftrTrack, GraphPane.class);
        if (graphPane == null) {
            return;
        }
        
        if (ftrTrack.getArrows().isEmpty()) {
            return;
        }
        ILayoutHelper layoutHelper = LayoutHelperFinder.find((Feture)ftrTrack.getArrows().get(0).getData());

        if (ftrTrack.getTrackName() == null || ftrTrack.getTrackName().isEmpty()) {
            throw new IllegalArgumentException("track name cannot be empty");
        }

        Insets iPadding = GraphPane.getPadding();
        ftrTrack.setBorder(BorderFactory.createEmptyBorder(iPadding.top, iPadding.left, iPadding.bottom, iPadding.right));

        List<IShape> arrows = ArrowComparators.adjacentConservedCompact(ftrTrack.getArrows());

        int totalPos = graphPane.getAs().getLength();

        Map<String, Boolean> labelVisibleMap = graphPane.getLabelVisibilityMap();

        boolean isLabelVisible = labelVisibleMap.get(ftrTrack.getTrackName().toUpperCase(Locale.ENGLISH));

        Rectangle displayRect = UIUtil.getBoundsNoBorder(parent);

        Dimension displaySize = displayRect.getSize();
        if (displaySize.width < 0 || displaySize.height < 0) {
            return;
        }

        int desiredHeight = 0;
        int lastX = Integer.MIN_VALUE;

        y = 0;
        final int vGap = 4;

        //Integer trackStartY = null;
        IShape previous = null;
        IShape current = null;
        IShape next = null;

        boolean adjacentPrevious = false;
        boolean adjacentNext = false;

        for (int i = 0; i < arrows.size(); i++) {

            current = arrows.get(i);


            if (i + 1 < arrows.size()) {
                next = arrows.get(i + 1);
            } else {
                next = null;
            }

            adjacentPrevious = false;
            if (previous != null) {
                adjacentPrevious = previous.getLuc().getEnd() + 1 == current.getLuc().getStart();
            } else {
                adjacentPrevious = false;
            }
            if (next != null) {
                adjacentNext = current.getLoc().getEnd() + 1 == next.getLoc().getStart();
            } else {
                adjacentNext = false;
            }

            if (lastX > displayRect.width + displayRect.x) {
                y += desiredHeight;
                y += vGap;
                lastX = Integer.MIN_VALUE;
            }

            Dimension minTxtSize = layoutHelper.getMinTextDimension(current);
            Lucation currentLoc = current.getLuc();

            int calculatedShapeStart = 1 + UIUtil.toScreenWidth(displaySize.width, totalPos, currentLoc.getStart() - 1, Integer.class);
            int calculatedShapeEnd = UIUtil.toScreenWidth(displaySize.width, totalPos, currentLoc.getEnd(), Integer.class);
            int calculatedShapeWidth = UIUtil.toScreenWidth(displaySize.width, totalPos, currentLoc.width(), Integer.class);

            // update TXT_LOC first
            boolean txtLocSet = false;
            if (!isLabelVisible) {
                current.setTextLoc(IShape.TEXT_LOC.NONE);
                txtLocSet = true;
            }

            // attempt to setTextLoc = TEXT_LOC.INSIDE
            if (!txtLocSet && minTxtSize.width < calculatedShapeWidth) {
                current.setTextLoc(IShape.TEXT_LOC.INSIDE);
                txtLocSet = true;
            }

            // attempt to setTextLoc = TEXT_LOC.BEFORE
            if (!txtLocSet) {
                int previousX = 0;
                if (previous != null) {

                    Rectangle previousBounds = previous.getBounds();
                    UIUtil.toScreenWidth(displayRect.width, totalPos, current.getLoc().getStart());
                    Point upperRight = UIUtil.getUpperRight(previousBounds);
                    previousX = upperRight.x;

                } else if (previous == null) {
                    previousX = 0;
                }

                if (minTxtSize.width < calculatedShapeStart - previousX) {
                    current.setTextLoc(IShape.TEXT_LOC.BEFORE);
                    txtLocSet = true;
                }
            }

            // attempt to setTextLoc = TEXT_LOC.AFTER
            if (!txtLocSet) {
                int nextX;

                nextX = UIUtil.getUpperRight(displayRect).x;

                if (!adjacentNext && minTxtSize.width < Math.abs(calculatedShapeEnd - nextX)) {
                    current.setTextLoc(IShape.TEXT_LOC.AFTER);
                    txtLocSet = true;
                }
            }

            if (!txtLocSet) {
                current.setTextLoc(IShape.TEXT_LOC.INSIDE);
                txtLocSet = true;
            }

            int desiredStartPixel = layoutHelper.getDesiredStartPixel(current, current.getTextLoc(), displayRect, totalPos);
            int desiredWidth = layoutHelper.getDesiredWidth(current, current.getTextLoc(), displayRect, totalPos);
            desiredHeight = layoutHelper.getDesiredHeight(current);

            if (!adjacentPrevious && desiredStartPixel < lastX) {
                y += desiredHeight;
                y += vGap;
                lastX = Integer.MIN_VALUE;
            }

            Rectangle arrowRect = new Rectangle(desiredStartPixel, y, desiredWidth, desiredHeight);

            current.setBounds(arrowRect);

            if (i == arrows.size() - 1) {

                lastX = Integer.MIN_VALUE;
                y += desiredHeight;
                y += vGap;
            } else {
                lastX = desiredStartPixel + desiredWidth;
            }

            previous = current;
        }

        if (!adjustingPreferredLayoutSize) {
            UIUtil.setPreferredHeight(ftrTrack, y);
        }
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
        y = 0;
    }

    @Override
    public String getFetureType() {
        return IFtrTrackLayout.DEFAULT_LAYOUT;
    }
}
