/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.tigr.core.ui.ckpanel;

import com.gas.common.ui.core.IntList;
import com.gas.common.ui.util.MathUtil;
import com.gas.common.ui.util.UIUtil;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.SwingConstants;

/**
 *
 * @author dq
 */
public class CenterGraph extends JComponent {

    //private Kromatogram k;
    private List<Integer> qvs = new ArrayList<Integer>();
    private List<Integer> offsets = new ArrayList<Integer>();
    private Set<IntList> integerLists = new HashSet<IntList>();

    public Set<IntList> getIntegerLists() {
        return integerLists;
    }

    public void setIntegerLists(Set<IntList> integerLists) {
        this.integerLists = integerLists;
    }

    public List<Integer> getOffsets() {
        return offsets;
    }

    public void setOffsets(List<Integer> offsets) {
        this.offsets = offsets;
    }

    public List<Integer> getQvs() {
        return qvs;
    }

    public void setQvs(List<Integer> qvs) {
        this.qvs = qvs;
    }

    @Override
    public void paintComponent(Graphics g) {
        if (offsets == null) {
            return;
        }
        Graphics2D g2d = (Graphics2D) g;
        Dimension size = getSize();
        Insets insets = getInsets();

        int displayWidth = size.width - insets.left - insets.right;


        if (displayWidth <= offsets.size()) {
            simplePaint(g2d);
        } else {
            paint(g2d);
        }
    }

    private void paint(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Dimension size = getSize();
        Insets insets = getInsets();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, size.width, size.height);
        ChromatogramComp2 comp2 = UIUtil.getParent(this, ChromatogramComp2.class);
        int totalDisplayWidth = size.width - insets.left - insets.right;
        final int totalDisplayHeight = size.height - insets.top - insets.bottom;
        final float unitWidth = totalDisplayWidth * 1.0f / offsets.size();

        if (comp2.isVisibleQv() && !qvs.isEmpty()) {
            List<GeneralPath> outlinePaths = createQVOutlinePath(qvs, unitWidth, totalDisplayHeight);

            for (GeneralPath p : outlinePaths) {
                GeneralPath closedPath = createQVClosedPath((GeneralPath) p.clone());
                g2d.setColor(ChromatogramComp2.QV_FILL_COLOR);
                g2d.fill(closedPath);

                g2d.setColor(ChromatogramComp2.QV_OUTLINE_COLOR);
                g2d.draw(p);
            }

        }

        if (comp2.isOffsetVisible()) {
            GeneralPath path = createOffsetPath(offsets.size());
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.draw(path);
        }

        if (totalDisplayWidth > offsets.size() * 2) {

            Iterator<IntList> itr = integerLists.iterator();
            while (itr.hasNext()) {
                IntList intList = itr.next();

                String key = intList.getName();
                boolean visible = comp2.isTraceVisible(key);
                if (visible) {
                    Color color = comp2.getColorProvider().getColor(key);
                    g2d.setColor(color);
                    List<GeneralPath> paths = createTraceGeneralPath(offsets, intList, unitWidth / 2.0f);
                    for (GeneralPath p : paths) {

                        g2d.draw(p);
                    }
                }
            }
        }
    }

    private GeneralPath createOffsetPath(int count) {
        GeneralPath ret = new GeneralPath();
        Dimension size = getSize();
        Insets insets = getInsets();
        int totalDisplayWidth = size.width - insets.left - insets.right;
        final float unitWidth = totalDisplayWidth * 1.0f / count;



        for (int i = 0; i < count; i++) {
            float x = i * unitWidth + unitWidth / 2.0f + insets.left;
            ret.moveTo(x, 0);
            ret.lineTo(x, size.height);
        }

        return ret;
    }

    private List<GeneralPath> createQVOutlinePath(List<Integer> qvs, final float unitWidth, int totalDisplayHeight) {
        Insets insets = getInsets();
        Dimension size = getSize();

        List<GeneralPath> ret = new ArrayList<GeneralPath>();
        GeneralPath path = new GeneralPath();
        Integer maxQv = MathUtil.max(qvs);
        for (int i = 0; i < qvs.size(); i++) {
            Integer qv = qvs.get(i);

            if (qv == null) {
                ret.add(path);
                path = new GeneralPath();
                continue;
            }

            int x = MathUtil.round(i * unitWidth) + insets.left;
            int endX = MathUtil.round((i + 1) * unitWidth + insets.left - 1);

            float y = UIUtil.toScreen(maxQv, qv, totalDisplayHeight, 0, SwingConstants.RIGHT);

            if (path.getCurrentPoint() == null) {
                path.moveTo(x, size.height - insets.bottom - y);
            }

            path.lineTo(x, size.height - insets.bottom - y);
            path.lineTo(endX, size.height - insets.bottom - y);

        }
        if (path.getCurrentPoint() != null) {
            ret.add(path);
        }
        return ret;
    }

    private GeneralPath createQVClosedPath(GeneralPath ret) {
        Insets insets = getInsets();
        Dimension size = getSize();


        Point2D cPoint = ret.getCurrentPoint();
        if (cPoint != null) {


            ret.lineTo(cPoint.getX(), size.height - insets.bottom);
            ret.lineTo(insets.left, size.height - insets.bottom);

            PathIterator itr = ret.getPathIterator(null);
            float[] coords = null;
            while (!itr.isDone()) {
                coords = new float[6];
                int type = itr.currentSegment(coords);
                break;
            }
            ret.lineTo(coords[0], size.height - insets.bottom);
            ret.closePath();
        }
        return ret;
    }

    private List<GeneralPath> createTraceGeneralPath(List<Integer> offsets, IntList trace, final float unitWidth) {

        List<GeneralPath> ret = new ArrayList<GeneralPath>();
        GeneralPath path = new GeneralPath();
        Integer startPos = null;
        Integer endPos = null;
        for (int i = 0; i < offsets.size(); i++) {
            if (startPos == null) {
                startPos = Math.max(offsets.get(0) - MathUtil.round(unitWidth), 0);
            }


            Integer offsetPos = offsets.get(i);
            if (offsetPos == null || offsetPos < 0) {
                if (path.getCurrentPoint() != null) {
                    ret.add(path);
                    path = new GeneralPath();
                }
                continue;
            }

            Integer nextOffset = null;
            nextOffset = getNextNonNullOffset(offsets, i);
            if (nextOffset == null) {
                endPos = Math.min(trace.size() - 1, offsetPos + 20);
            } else {
                endPos = MathUtil.avg(new Integer[]{offsetPos, nextOffset}, Integer.class);
            }

            final float displayWidth = unitWidth;

            addHalf(trace, path, startPos, offsetPos, displayWidth, i * unitWidth * 2);
            addHalf(trace, path, offsetPos, endPos, displayWidth, i * unitWidth * 2 + unitWidth);
            startPos = endPos + 1;
        }

        if (path.getCurrentPoint() != null) {
            ret.add(path);
        }
        return ret;
    }

    private Integer getNextNonNullOffset(List<Integer> offsets, int curIndex) {
        Integer ret = null;
        curIndex++;
        while (curIndex < offsets.size() - 1 && (ret = offsets.get(curIndex)) == null) {
            curIndex++;
        }
        return ret;
    }

    private void addHalf(IntList trace, GeneralPath path, int startPos, int endPos, float displayWidth, float startX) {
        Dimension size = getSize();
        Insets insets = getInsets();
        int totalDisplayHeight = size.height - insets.bottom - insets.top;
        for (int j = startPos; j <= endPos; j++) {
            int totalPos = endPos - startPos + 1;
            int pos = j - startPos + 1;

            int maxTrace = trace.getMax();

            Float dX = null;
            if (j == startPos) {
                dX = UIUtil.toScreen(totalPos, pos, displayWidth, 0, SwingConstants.LEFT);
            } else if (j == endPos) {
                dX = UIUtil.toScreen(totalPos, pos, displayWidth, 0, SwingConstants.RIGHT);
            } else {
                dX = UIUtil.toScreen(totalPos, pos, displayWidth, 0, SwingConstants.CENTER);
            }
            float x = dX + startX;
            x += insets.left;
            float dY = UIUtil.toScreen(maxTrace, trace.get(j), totalDisplayHeight, 0, SwingConstants.RIGHT);
            float y = size.height - dY - insets.bottom;
            if (path.getCurrentPoint() == null) {
                path.moveTo(x, y);
            }

            path.lineTo(x, y);
        }
    }

    private void simplePaint(Graphics2D g2d) {
        Dimension size = getSize();
        Insets insets = getInsets();
        int displayWidth = size.width - insets.left - insets.right;
        int displayHeight = size.height - insets.top - insets.bottom;

        Iterator<IntList> itr = integerLists.iterator();
        while (itr.hasNext()) {
            IntList intList = itr.next();
            int maxTrace = intList.getMax();
            int traceCount = intList.size();
            for (int i = 0; i < traceCount; i++) {
                float x = UIUtil.toScreen(traceCount, i, displayWidth, insets.left, SwingConstants.RIGHT);
                int xInt = MathUtil.round(x);
                int traceHeight = intList.get(i);
                float y = UIUtil.toScreen(maxTrace, traceHeight, displayHeight, insets.top, SwingConstants.RIGHT);
                int yInt = MathUtil.round(y);
                g2d.drawLine(xInt, size.height - insets.bottom, xInt, insets.top + displayHeight - yInt);
            }
        }
    }

    public void delete(int pos, int length) {
        while (length > 0) {
            qvs.remove(pos - 1);
            offsets.remove(pos - 1);
            Iterator<IntList> itr = integerLists.iterator();
            //while (itr.hasNext()) {
            //    IntegerList intList = itr.next();
            //    intList.deleteAt(pos);
            //}
            length--;
        }
        revalidate();
        repaint();
    }

    /*
     * @param 1-based
     */
    public void insert(int pos, String data) {
    }
}