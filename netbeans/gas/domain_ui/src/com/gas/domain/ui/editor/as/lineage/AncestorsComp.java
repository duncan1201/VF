/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor.as.lineage;

import com.gas.common.ui.core.IntList;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.Operation;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.text.DateFormat;
import java.util.Iterator;
import java.util.Locale;
import java.util.TreeSet;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

/**
 *
 * @author dq
 */
public class AncestorsComp extends JComponent {

    AnnotatedSeq as;
    DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, Locale.US);
    OpeShapeList opeShapeList = new OpeShapeList();
    EntityShapeList entityShapeList = new EntityShapeList();
    float unitHeight;
    FontMetrics fm;
    IntList widthList = new IntList();
    ConnectorList connectors = new ConnectorList();

    public AncestorsComp() {
        hookupListeners();
    }

    private void hookupListeners() {
        AncestorsCompListeners.MouseAdpt mouseAdpt = new AncestorsCompListeners.MouseAdpt();
        addMouseListener(mouseAdpt);
        addMouseMotionListener(mouseAdpt);
    }

    public void setAs(AnnotatedSeq as) {
        AnnotatedSeq old = this.as;
        this.as = as;
        firePropertyChange("as", old, this.as);
    }

    /**
     * Left to right
     */
    @Override
    protected void paintComponent(Graphics g) {
        final Dimension size = getSize();
        if (size.width == 0 || size.height == 0) {
            return;
        }

        widthList.clear();
        final Insets insets = UIUtil.getDefaultInsets();
        opeShapeList.clear();
        entityShapeList.clear();
        connectors.clear();

        fm = g.getFontMetrics();

        Operation ope = as.getOperation();
        int y1 = insets.top;
        int y2 = size.height - insets.top - insets.bottom;
        int x1 = insets.left;

        final int totalLeafCount = ope.getLeafParticipantCount();
        unitHeight = (y2 - y1) / (totalLeafCount + 0.0f);

        createShapesRecursively(as.getName(), null, ope, false, y1, y2, x1);

        Graphics2D g2d = (Graphics2D) g;
        boolean preferredSizeSet = isPreferredSizeSet();
        if (!preferredSizeSet) {
            configurePreferredSize(totalLeafCount);
            return;
        }
        boolean backgroundSet = isBackgroundSet();
        if (backgroundSet) {
            g.setColor(getBackground());
            g.fillRect(0, 0, size.width, size.height);
        }
        g.setColor(Color.BLACK);

        entityShapeList.paint(g2d, this);
        opeShapeList.paint(g2d);
        connectors.paint(g2d);
    }

    private void configurePreferredSize(int totalLeafCount) {
        Insets insets = UIUtil.getDefaultInsets();
        int fontHeight = fm.getHeight();
        int minHeight = fontHeight + insets.top;
        if (unitHeight < minHeight) {            
            setPreferredSize(new Dimension(widthList.getMax(), minHeight * totalLeafCount + 10));
        } else {
            UIUtil.setPreferredWidth(this, widthList.getMax());
        }

        repaint();
        revalidate();
    }

    /**
     * @param yCenter: the y center of the entity shape
     */
    int createEntityShape(EntityShape ret, String entityName, String absolutePath, boolean active, int yCenter, int x1) {
        int charWidth = fm.charWidth('A');
        ret.name = entityName;
        ret.active = active;
        ret.absolutePath = absolutePath;
        ret.x = x1;
        ret.height = Math.round(fm.getHeight() * 1.2f);
        ret.y = Math.round((yCenter - ret.height * 0.5f));
        ret.width = fm.stringWidth(entityName) + charWidth * 3 + 16;
        if (absolutePath == null || absolutePath.isEmpty()) {
            ret.width = fm.stringWidth(entityName) + charWidth * 2;
        } else {
            ret.width = fm.stringWidth(entityName) + charWidth * 3 + 16;
        }
        return x1 + ret.width;
    }

    int createOpeShape(OpeShape ret, Operation ope, int y1, int y2, int x1) {
        final int fontHeight = fm.getHeight();
        final String name = ope.getNameEnum().value;
        final String dateStr = dateFormat.format(ope.getDate());
        ret.name = name;
        ret.date = dateStr;
        ret.x = x1;
        ret.height = Math.round(fontHeight * 2.2f);
        ret.y = Math.round((y1 + y2) * 0.5f - ret.height * 0.5f);
        ret.width = Math.max(Math.round(fm.stringWidth(name) * 1.5f), Math.round(fm.stringWidth(dateStr) * 1.5f));

        return x1 + ret.width;
    }

    int createConnector(Connector ret, int yMin, int yMax, int x1, TreeSet<Operation.Participant> parts) {
        Operation.Participant partFirst = parts.first();
        final int leafCountFirst = partFirst.getLeafParticipantCount();
        Operation.Participant partLast = parts.last();
        final int leafCountLast = partLast.getLeafParticipantCount();
        int y1 = yMin + Math.round(unitHeight * leafCountFirst * 0.5f);
        int y2 = yMax - Math.round(unitHeight * leafCountLast * 0.5f);

        final int width = fm.charWidth('A');
        ret.x = x1;

        ret.y = y1;
        ret.width = width;
        ret.height = Math.round(y2 - y1);

        return x1 + width;
    }

    private void createShapesRecursively(String entityName, String absolutePath, Operation ope, boolean active, int yMin, int yMax, int x1) {

        // entity shape
        EntityShape entityShape = new EntityShape();
        x1 = createEntityShape(entityShape, entityName, absolutePath, active, Math.round((yMin + yMax) * 0.5f), x1);
        entityShapeList.add(entityShape);

        // operation shape
        OpeShape opeShape = new OpeShape();
        x1 = createOpeShape(opeShape, ope, yMin, yMax, x1);
        opeShapeList.add(opeShape);

        TreeSet<Operation.Participant> parts = ope.getSortedParticipants();

        // connector if any
        if (parts.size() > 1) {
            Connector connector = new Connector();
            x1 = createConnector(connector, yMin, yMax, x1, parts);
            connectors.add(connector);
        }

        // more related operations

        Iterator<Operation.Participant> itr = parts.iterator();
        int yStart = yMin;
        int y2Child;
        while (itr.hasNext()) {
            Operation.Participant part = itr.next();
            String absolutePathChild = part.getAbsolutePath();
            String nameChild = part.getName();
            boolean activeChild = part.isActive();
            Operation opeChild = part.getOperation();
            if (opeChild != null) {
                int childLeafCount = opeChild.getLeafParticipantCount();
                int y1Child = yStart;
                y2Child = Math.round(yStart + unitHeight * childLeafCount);
                createShapesRecursively(nameChild, absolutePathChild, opeChild, activeChild, y1Child, y2Child, x1);
                yStart = Math.round(yStart + unitHeight * childLeafCount);
            } else {
                int yChild = Math.round(yStart + unitHeight * 0.5f);
                EntityShape eShape = new EntityShape();
                int xNew = createEntityShape(eShape, nameChild, absolutePathChild, activeChild, yChild, x1);
                widthList.add(xNew);
                entityShapeList.add(eShape);
                yStart = Math.round(yStart + unitHeight);
            }
        }
    }
}
