/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.ringpane;

import com.gas.common.ui.core.LocList;
import com.gas.common.ui.linePlot.CirLinePlotsComp;
import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.util.MathUtil;
import com.gas.common.ui.util.RectHelper;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.as.Feture;
import com.gas.domain.core.as.Lucation;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.*;

/**
 *
 * @author dq
 */
public class RingGraphPanelLayout implements LayoutManager {

    protected static final int RING_GAP = 4;
    protected static final int TRACK_GAP = 4;
    private RingPane ringPane;

    @Override
    public void addLayoutComponent(String name, Component comp) {
    }

    @Override
    public void removeLayoutComponent(Component comp) {
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        Dimension ret = new Dimension();
        return ret;
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        Dimension ret = calculateMinimumSize(parent);
        return ret;
    }

    protected Rectangle getDrawingRect(Container parent) {
        return getDrawingRect(parent, calculateMinimumSize(parent));
    }

    protected Rectangle getDrawingRect(Container parent, final Dimension minSize) {
        final Dimension size = parent.getSize();
        final Insets insets = parent.getInsets();

        int squareLength = Math.min(size.height - insets.top - insets.bottom, size.width - insets.left - insets.right);
        squareLength = Math.max(squareLength, minSize.width);


        final Rectangle square = new Rectangle(squareLength, squareLength);
        square.x = MathUtil.round((size.width - squareLength) * 0.5);
        square.y = MathUtil.round((size.height - squareLength) * 0.5);
        return square;
    }

    /*
     * Layouts the outermost features first
     */
    @Override
    public void layoutContainer(Container parent) {
        if (ringPane == null) {
            ringPane = UIUtil.getParent(parent, RingPane.class);
        }
        if (ringPane == null) {
            return;
        }
        RingGraphPanel ringGraphPanel = (RingGraphPanel) parent;
        boolean initUIDone = ringGraphPanel.isInitUIDone();
        if (!initUIDone) {
            return;
        }

        final Insets insets = parent.getInsets();
        final Dimension minSize = calculateMinimumSize(parent);
        final Dimension size = parent.getSize();

        final Rectangle square = getDrawingRect(parent, minSize);
        int offset = 0;

        // layout the ruler
        if (ringPane.isBaseNumberShown()) {
            CircularRuler ruler = ringGraphPanel.getCircularRuler();
            ruler.setBounds(UIUtil.shrink(square, offset, offset));
            offset += ruler.getRingThickness();
        } else {
            CircularRuler ruler = ringGraphPanel.getCircularRuler();
            ruler.setSize(0, 0);
        }

        // layout the restriction sites
        CurvedBracketList brackets = ringGraphPanel.getCurvedBrackets();
        int width = layoutBrackets(brackets, UIUtil.shrink(square, offset, offset), ringGraphPanel.getBrickRing());
        offset += width;

        // layout the sequence
        BrickRing brickRing = ringGraphPanel.getBrickRing();
        if (brickRing == null) {
            return;
        }
        brickRing.setBounds(UIUtil.shrink(square, offset, offset));
        offset += brickRing.getRingThickness();
        
        // add the triangle height of curved brackets
        offset += MathUtil.round(CurvedBracket.calculateTriangleHeight() * 1.1f);

        // layout the overlay
        ringGraphPanel.getOverlay().setBounds(brickRing.getBounds());

        // layout the forward translated proteins
        BrickRingList bringList = ringGraphPanel.getTranslatedBrickRings(true);
        Iterator<BrickRing> itr = bringList.iterator();
        while (itr.hasNext()) {
            BrickRing ring = itr.next();
            ring.setBounds(UIUtil.shrink(square, offset, offset));
            offset += ring.getRingThickness();
        }

        // layout the reverse translated proteins
        bringList = ringGraphPanel.getTranslatedBrickRings(false);
        itr = bringList.iterator();
        while (itr.hasNext()) {
            BrickRing ring = itr.next();
            ring.setBounds(UIUtil.shrink(square, offset, offset));
            offset += ring.getRingThickness();
        }

        // layout the plots        
        ringGraphPanel.reinitCirLinePlotsComp();
        CirLinePlotsComp cirLinePlotsComp = ringGraphPanel.getCirLinePlotsComp();
        if (!cirLinePlotsComp.isEmpty()) {
            offset += TRACK_GAP;
            cirLinePlotsComp.setBounds(UIUtil.shrink(square, offset, offset));
            cirLinePlotsComp.repaint();
            offset += cirLinePlotsComp.getDesiredRingWidth();
        } else {
            cirLinePlotsComp.setBounds(0, 0, 0, 0);
        }


        // layout the features
        offset += RING_GAP;
        Rectangle rect = UIUtil.shrink(square, offset, offset);
        RingListMapComp ringListMapComp = ringGraphPanel.getRingListMapComp();
        ringListMapComp.setRingMap(ringGraphPanel.getSortedRingMap());
        ringListMapComp.setStartOffset(-ringGraphPanel.getOffset());
        ringListMapComp.setBounds(rect);
        ringListMapComp.layoutRings();
        ringListMapComp.repaint();

        int newPHeight = Math.max(minSize.height + insets.top + insets.bottom, size.height);
        int newPWidth = Math.max(minSize.width + insets.left + insets.right, size.width);
        parent.setPreferredSize(new Dimension(newPWidth, newPHeight));

        Rectangle outermostBound = null;
        if (ringPane.isBaseNumberShown()) {
            outermostBound = ringGraphPanel.getCircularRuler().getBounds();
        } else {
            outermostBound = ringGraphPanel.getBrickRing().getBounds();
        }

        if (outermostBound.y > 0 && outermostBound.x > 0) {
            LabelListComp labelListComp = ringGraphPanel.getLabelListComp();
            labelListComp.setBounds(0, 0, parent.getSize().width, parent.getSize().height);
            labelListComp.setLabelList(ringGraphPanel.getLabelList());

            labelListComp.layoutLabels(outermostBound.width * 0.5f);
            labelListComp.repaint();

            ConnectorsComp connectorsComp = ringGraphPanel.getConnectorsComp();
            connectorsComp.setBounds(0, 0, parent.getSize().width, parent.getSize().height);
            connectorsComp.setConnectors(ringGraphPanel.getConnectors());
            connectorsComp.layoutConnectors(ringGraphPanel);
            connectorsComp.repaint();

            layoutCenterLabels(ringGraphPanel, outermostBound);
        }

        Point caretPos = ringGraphPanel.getCaret().getPos();
        if (caretPos != null) {
            ringGraphPanel.getCaret().setPos(new Point(caretPos.x, 1), true);
        }
    }

    private void layoutCenterLabels(final RingGraphPanel ringGraphPanel, Rectangle rect) {
        final Rectangle vRect = ringGraphPanel.getVisibleRect();
        final Label nameLabel = ringGraphPanel.getNameLabel();
        final Point center = RectHelper.getCenter(rect);
        nameLabel.setText(ringGraphPanel.getAs().getName());
        Dimension dSize = nameLabel.getDesiredSize();
        Rectangle lRect = new Rectangle();
        lRect.setSize(dSize);
        lRect.setLocation(Math.round(center.x - dSize.width * 0.5f), Math.round(center.y - dSize.height));
        if (!vRect.contains(lRect)) {
            int dy = -(lRect.getLocation().y - (vRect.y + vRect.height) + dSize.height + dSize.height);
            lRect.translate(0, dy);
        }
        nameLabel.setBounds(lRect);

        final Label lengthLabel = ringGraphPanel.getLengthLabel();
        lengthLabel.setText(String.format("%sbp", ringGraphPanel.getAs().getLength().toString()));
        dSize = lengthLabel.getDesiredSize();
        lRect.setSize(dSize);
        lRect.setLocation(Math.round(center.x - dSize.width * 0.5f), center.y);
        if (!vRect.contains(lRect)) {
            int dy = -(lRect.getLocation().y - (vRect.y + vRect.height) + dSize.height);
            lRect.translate(0, dy);
        }
        lengthLabel.setBounds(lRect);
    }

    private int layoutBrackets(CurvedBracketList brackets, Rectangle bounds, BrickRing brickRing) {
        int layer = 0;// innermost is 1, second layer is 2...
        if (!brackets.isEmpty()) {
            layer = 1;
        }
        Map<Integer, List<CurvedBracket>> layeredBrackets = new HashMap<Integer, List<CurvedBracket>>();

        Iterator<CurvedBracket> itr = brackets.iterator();
        LocList locList = new LocList();
        Integer thickness = null;
        while (itr.hasNext()) {
            CurvedBracket bracket = itr.next();
            if (thickness == null) {
                thickness = bracket.getThickness();
            }
            boolean intersect = locList.intersect(bracket.getStartPos(), bracket.getEndPos()) != null;

            if (intersect) {
                layer++;
                locList.clear();
            }
            locList.add(new Loc(bracket.getStartPos(), bracket.getEndPos()));

            if (!layeredBrackets.containsKey(layer)) {
                layeredBrackets.put(layer, new ArrayList<CurvedBracket>());
            }
            layeredBrackets.get(layer).add(bracket);
        }

        final int ringThickness = brickRing.getRingThickness();

        final int ret = thickness == null ? 0 : thickness * (layer + 1);

        final float upperRadius = bounds.width * 0.5f - ret;
        final float lowerRadius = bounds.width * 0.5f - ret - ringThickness;

        Iterator<Integer> intItr = layeredBrackets.keySet().iterator();
        while (intItr.hasNext()) {
            Integer key = intItr.next();
            int shrickAmt = (layer - key) * thickness;
            Rectangle rect = UIUtil.shrink(bounds, shrickAmt, shrickAmt);
            Iterator<CurvedBracket> cItr = layeredBrackets.get(key).iterator();
            while (cItr.hasNext()) {
                CurvedBracket b = cItr.next();
                b.setRingUpperRadius(upperRadius);
                b.setRingLowerRadius(lowerRadius);
                b.setBounds(rect);
            }
        }

        return ret;
    }

    private Dimension calculateMinimumSize(Container parent) {
        Dimension ret = new Dimension();
        RingGraphPanel ringPanel = (RingGraphPanel) parent;
        SortedRingListMap ringMap = ringPanel.getSortedRingMap();

        LocList locList = new LocList();

        Iterator<String> itr = ringMap.keySet().iterator();
        int layerCount = 1;
        Integer ringWidth = null;
        while (itr.hasNext()) {
            String key = itr.next();
            List<Ring> rings = ringMap.get(key);
            Iterator<Ring> ringItr = rings.iterator();
            while (ringItr.hasNext()) {
                Ring ring = ringItr.next();
                if (ringWidth == null) {
                    ringWidth = ring.getRingThickness();
                }
                Feture feture = (Feture) ring.getData();
                Lucation luc = feture.getLucation();
                if (luc == null) {
                    System.out.print("");
                }
                Loc loc = new Loc(luc.getStart(), luc.getEnd());
                boolean intersect = locList.intersect(loc);
                if (intersect) {
                    layerCount++;
                    locList.clear();
                }

                locList.add(loc);
            }
        }

        if (!ringMap.isEmpty()) {
            ret.width = ret.height = (layerCount * 2 + 4) * (ringWidth + RING_GAP);
        }
        if (Boolean.TRUE.equals(ringPanel.isDoubleStranded())) {
            ret.width = ret.height += ringWidth == null? 0: ringWidth + RING_GAP;
        }
        ret.width = ret.height += ringPanel.getCircularRuler().getRingThickness() * 2;
        return ret;
    }
}
