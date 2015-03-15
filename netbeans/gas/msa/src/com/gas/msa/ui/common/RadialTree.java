/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui.common;

import com.gas.common.ui.light.Ellipse2DXList;
import com.gas.common.ui.light.Rect2DList;
import com.gas.common.ui.core.Line2DX;
import com.gas.common.ui.core.Line2DXList;
import com.gas.common.ui.light.*;
import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.MathUtil;
import com.gas.msa.ui.tree.TreePane;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.SwingConstants;

/**
 *
 * @author dq
 */
public class RadialTree extends AbstractTree {

    private SHAPE nodeShape;
    private Insets insets = new Insets(5, 5, 5, 5);
    private TreeNode.Node root;
    private TreeNode.Node selected;
    private float _unitSpace;
    private Rectangle _treeRect;
    private FontMetrics _fm;
    private float _depth;
    private float rotation = 0;
    private Line2DXList edges = new Line2DXList();
    private Rect2DList squareNodes = new Rect2DList();
    private Ellipse2DXList roundNodes = new Ellipse2DXList();
    private Ellipse2DXList selectableNodes = new Ellipse2DXList();
    private TransLabelList nodeLabels = new TransLabelList();
    private TransLabelList edgeLabels = new TransLabelList();
    private boolean radialNodeLabel = true;
    private boolean edgeLabelVisible = true;
    private boolean nodeLabelVisible = true;
    private int lineWidth = 1;
    private Integer treeUnitHeight;
    private TRANSFORM transform = TRANSFORM.CLADOGRAM;
    private String[] lengthAttributeNames;
    private String selectedLenghtAttribute = "length";
    private String[] nameAttributeNames;
    private String selectedNameAttribute = "name";
    private Integer sigDigits;
    private String longestName;
    private Float fontSize;

    public RadialTree() {
        super();
        setOpaque(true);
        setBackground(Color.WHITE);
        hookupListeners();
    }

    private void hookupListeners() {
        addPropertyChangeListener(new RadialTreeListeners.PtyListener());
        RadialTreeListeners.MouseApt mouseApt = new RadialTreeListeners.MouseApt();
        addMouseWheelListener(mouseApt);
        addMouseMotionListener(mouseApt);
        addMouseListener(mouseApt);
    }

    @Override
    public void setFontSize(Float fontSize) {
        Float old = this.fontSize;
        this.fontSize = fontSize;
        firePropertyChange("fontSize", old, this.fontSize);
    }

    public void setSelected(TreeNode.Node selected) {
        TreeNode.Node old = this.selected;
        this.selected = selected;
        firePropertyChange("selected", old, this.selected);
    }

    public Ellipse2DXList getSelectableNodes() {
        return selectableNodes;
    }

    @Override
    public String[] getNameAttributeNames() {
        return nameAttributeNames;
    }

    public void setNameAttributeNames(String[] nameAttributeNames) {
        this.nameAttributeNames = nameAttributeNames;
    }

    public String getSelectedNameAttribute() {
        return selectedNameAttribute;
    }

    @Override
    public void setSelectedNameAttribute(String selectedNameAttribute) {
        String old = this.selectedNameAttribute;
        this.selectedNameAttribute = selectedNameAttribute;
        firePropertyChange("selectedNameAttribute", old, this.selectedNameAttribute);
    }

    @Override
    public String[] getLengthAttributeNames() {
        return lengthAttributeNames;
    }

    public void setLengthAttributeNames(String[] lengthAttributeNames) {
        this.lengthAttributeNames = lengthAttributeNames;
    }

    public String getSelectedLenghtAttribute() {
        return selectedLenghtAttribute;
    }

    @Override
    public void setSelectedLengthAttribute(String selectedLenghtAttribute) {
        String old = this.selectedLenghtAttribute;
        this.selectedLenghtAttribute = selectedLenghtAttribute;
        firePropertyChange("selectedLengthAttribute", old, this.selectedLenghtAttribute);
    }

    @Override
    public void setNodeShape(SHAPE nodeShape) {
        SHAPE old = this.nodeShape;
        this.nodeShape = nodeShape;
        firePropertyChange("nodeShape", old, this.nodeShape);
    }

    @Override
    public void setNodeLabelVisible(boolean visible) {
        boolean old = this.nodeLabelVisible;
        this.nodeLabelVisible = visible;
        firePropertyChange("nodeLabelVisible", old, this.nodeLabelVisible);
    }

    @Override
    public void setTransform(TRANSFORM transform) {
        TRANSFORM old = this.transform;
        this.transform = transform;
        firePropertyChange("transform", old, this.transform);
    }

    private Integer getTreeUnitHeight() {
        if (treeUnitHeight == null) {
            treeUnitHeight = root.unitHeight();
        }
        return treeUnitHeight;
    }

    @Override
    public void paintComponent(Graphics g) {
        if (root == null || nodeShape == null || fontSize == null || sigDigits == null) {
            return;
        }
        Font font = g.getFont().deriveFont(fontSize);
        g.setFont(font);
        final Dimension size = getSize();
        Color oldColor = g.getColor();
        if (isOpaque()) {
            g.setColor(getBackground());
            g.fillRect(0, 0, size.width, size.height);
        }
        g.setColor(oldColor);
        _unitSpace = (float) (Math.PI * 2 / root.getNumOfLeaves());
        _treeRect = calculateTreeRect();
        if (_treeRect.width < 0 || _treeRect.height < 0) {
            return;
        }
        clear();
        _depth = root.height();
        createEdges(root, getRotation(), getRotation() + (float) Math.PI * 2, new Point2D.Float((float) _treeRect.getCenterX(), (float) _treeRect.getCenterY()));
        scaleEdges();
        translateEdges();
        if (nodeLabelVisible) {
            createNodeLabels(edges);
        }
        if (edgeLabelVisible) {
            createEdgeLabels(edges);
        }
        createNodes();
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        BasicStroke bs = new BasicStroke(lineWidth);
        g2d.setStroke(bs);
        paintUIObjects(g2d);
    }

    private void paintUIObjects(Graphics2D g2d) {
        edges.paint(g2d);        
        if (nodeShape == SHAPE.SQUARE) {
            squareNodes.paint(g2d);
        } else if (nodeShape == SHAPE.CIRCLE) {
            roundNodes.paint(g2d);
        }
        nodeLabels.paint(g2d);
        edgeLabels.paint(g2d);
        selectableNodes.paint(g2d);
    }

    private void clear() {
        edges.clear();
        squareNodes.clear();
        roundNodes.clear();
        nodeLabels.clear();
        edgeLabels.clear();
        selectableNodes.clear();
    }

    @Override
    public void setLineWidth(int lineWidth) {
        int old = this.lineWidth;
        this.lineWidth = lineWidth;
        firePropertyChange("lineWidth", old, this.lineWidth);
    }


    /*
     * @param startAngRad
     */
    private void createEdges(TreeNode.Node node, final float startAngRad, final float endAngRad, final Point2D.Float center) {

        Float x2 = null, y2 = null;
        //if (!node.isRoot()) {
        float branchLength = getBranchLength(node);
        double angleRad = (startAngRad + endAngRad) * 0.5;
        x2 = center.x + (float) Math.cos(angleRad) * branchLength;
        y2 = center.y - (float) Math.sin(angleRad) * branchLength;
        Line2DX.Float edge = new Line2DX.Float(center.x, center.y, x2, y2);
        edge.setData(node);
        edge.setAngleRad(angleRad);
        edge.setColor(node.getColor());
        edges.add(edge);
        //}
        List<TreeNode.Node> tmp = new ArrayList<TreeNode.Node>(node.getChildren());
        Collections.sort(tmp, new TreeNode.NodeComparator());
        Iterator<TreeNode.Node> itr = tmp.iterator();
        float newStartAngRad = startAngRad;
        Point2D.Float newCenter = null;
        if (!node.isRoot()) {
            newCenter = new Point2D.Float(x2, y2);
        } else {
            newCenter = new Point2D.Float(center.x, center.y);
        }

        while (itr.hasNext()) {
            TreeNode.Node child = itr.next();
            int childLeavesNo = child.getNumOfLeaves();
            createEdges(child, newStartAngRad, newStartAngRad + childLeavesNo * _unitSpace, newCenter);
            newStartAngRad = newStartAngRad + childLeavesNo * _unitSpace;
        }
    }

    public boolean isEdgeLabelVisible() {
        return edgeLabelVisible;
    }

    @Override
    public void setEdgeLabelVisible(boolean edgeLabelVisible) {
        boolean old = this.edgeLabelVisible;
        this.edgeLabelVisible = edgeLabelVisible;
        firePropertyChange("edgeLabelVisible", old, this.edgeLabelVisible);
    }

    private void createNodes() {
        for (int i = 0; i < edges.size(); i++) {
            Line2DX.Float edge = edges.get(i);
            TreeNode.Node node = (TreeNode.Node) edge.getData();
            double x2 = edge.getP2().getX();
            double y2 = edge.getP2().getY();
            //if (nodeShape == SHAPE.CIRCLE) {
            Ellipse2DX.Float circle = new Ellipse2DX.Float();
            circle.setFrame(x2 - TreePane.NODE_WIDTH / 2, y2 - TreePane.NODE_WIDTH / 2, TreePane.NODE_WIDTH, TreePane.NODE_WIDTH);
            roundNodes.add(circle);

            Ellipse2DX.Float circle2 = new Ellipse2DX.Float();
            circle2.setFrame(x2 - TreePane.NODE_WIDTH / 2, y2 - TreePane.NODE_WIDTH / 2, TreePane.NODE_WIDTH, TreePane.NODE_WIDTH);
            if (node.isDescendantOf(selected) || node == selected) {
                circle2.setVisible(true);
                circle2.setSelected(true);
                circle2.setColor(TreePane.SELECTED_COLOR);
            } else {
                circle2.setVisible(false);
            }
            circle2.setData(node);
            selectableNodes.add(circle2);
            //} else if (nodeShape == SHAPE.SQUARE) {
            Rectangle2D rect2d = new Rectangle2D.Float();
            rect2d.setRect(x2 - TreePane.NODE_WIDTH / 2, y2 - TreePane.NODE_WIDTH / 2, TreePane.NODE_WIDTH, TreePane.NODE_WIDTH);
            squareNodes.add(rect2d);
            //}
        }
    }

    private void createNodeLabels(Line2DXList _edges) {
        for (int i = 0; i < _edges.size(); i++) {
            Line2DX.Float edge = _edges.get(i);
            TreeNode.Node node = (TreeNode.Node) edge.getData();
            if (node.isLeaf()) {
                TransLabel nodeLabel = createNodeLabel(edge);
                nodeLabel.setColor(node.getColor());
                nodeLabels.add(nodeLabel);
            }
        }
    }

    private void createEdgeLabels(Line2DXList _edges) {
        for (int i = 0; i < _edges.size(); i++) {
            Line2DX.Float edge = _edges.get(i);
            TreeNode.Node node = (TreeNode.Node) edge.getData();
            if (node.getTreeNodeLength() != null) {
                TransLabel transLabel = createEdgeLabel(edge);
                transLabel.setColor(node.getColor());
                edgeLabels.add(transLabel);
            }
        }
    }

    private TransLabel createEdgeLabel(Line2DX.Float edge) {
        TreeNode.Node node = (TreeNode.Node) edge.getData();
        Object att = node.getTreeNodeLength().getAttribute(selectedLenghtAttribute);

        String attStr = null;
        if (att instanceof Float) {
            Float attFloat = (Float) att;
            attStr = MathUtil.toString(attFloat.doubleValue(), sigDigits);
        } else if (att instanceof TreeNodeLength.HPD) {
            TreeNodeLength.HPD hpd = (TreeNodeLength.HPD) att;
            attStr = hpd.toString(sigDigits);
        } else {
            throw new UnsupportedOperationException();
        }
        Point2D start = edge.getP1();
        Point2D end = edge.getP2();
        float xEnd = (float) end.getX();
        float yEnd = (float) end.getY();

        double angRad = MathUtil.getAngleInRadians(end, start);

        int degree = MathUtil.round(Math.toDegrees(angRad));
        int mod = degree % 360;
        if (mod < 0) {
            mod += 360;
        }
        TransLabel ret = new TransLabel(SwingConstants.CENTER, SwingConstants.CENTER);
        ret.setData(attStr);
        ret.setWidth(getFontMetrics().stringWidth(attStr) * 1.0f);
        ret.setHeight(getFontMetrics().getHeight() * 1.0f);

        double radius = start.distance(xEnd, yEnd) * 0.5 - getFontMetrics().stringWidth(attStr) * 0.5;
        ret.setAnchor(start);
        ret.setTheta(angRad * -1.0);
        ret.setX((float) start.getX() + (float) radius + insets.left);
        ret.setY((float) start.getY() - ret.getHeight());

        return ret;
    }

    private TransLabel createNodeLabel(Line2DX.Float edge) {
        TreeNode.Node node = (TreeNode.Node) edge.getData();
        Object data = node.getTreeNodeName().getAttribute(selectedNameAttribute);
        final Point2D start = edge.getP1();
        final Point2D end = edge.getP2();
        final float xEnd = (float) end.getX();
        final float yEnd = (float) end.getY();

        double angRad = edge.getAngleRad();
        int degree = MathUtil.round(Math.toDegrees(angRad));
        int mod = degree % 360;
        if (mod < 0) {
            mod += 360;
        }
        TransLabel ret = new TransLabel(SwingConstants.CENTER, SwingConstants.CENTER);
        ret.setData(data);
        ret.setWidth(getFontMetrics().stringWidth(data.toString()) * 1.0f);
        ret.setHeight(getFontMetrics().getHeight() * 1.0f);

        if (radialNodeLabel) {
            double radius = start.distance(xEnd, yEnd);
            ret.setAnchor(start);
            ret.setTheta(angRad * -1.0);
            ret.setX((float) start.getX() + (float) radius + insets.left);
            ret.setY((float) start.getY() - ret.getHeight() * 0.5f);
        } else {
            if (mod >= 0 && mod <= 90) {
                ret.setX(xEnd);
                ret.setY(yEnd - ret.getHeight());
            } else if (mod > 90 && mod <= 180) {
                ret.setX(xEnd - ret.getWidth());
                ret.setY(yEnd - ret.getHeight());
            } else if (mod > 180 && mod <= 270) {
                ret.setX(xEnd - ret.getWidth());
                ret.setY(yEnd);
            } else if (mod > 270 && mod <= 360) {
                ret.setX(xEnd);
                ret.setY(yEnd);
            }
        }

        return ret;
    }

    private int anyWidth() {
        return getTreeUnitHeight() * 6;
    }

    private float getBranchLength(TreeNode.Node node) {
        float ret;
        if (node.isRoot()) {
            ret = 0;
        } else {
            if (transform == TRANSFORM.NONE) {
                ret = anyWidth() * node.getTreeNodeLength().getLength() / _depth;
            } else if (transform == TRANSFORM.EQUAL) {
                ret = anyWidth();
            } else if (transform == TRANSFORM.CLADOGRAM) {
                int unitDepth = node.unitDepth();
                getTreeUnitHeight();
                if (node.isLeaf()) {
                    ret = anyWidth() * (getTreeUnitHeight() - unitDepth + 1);
                } else {
                    ret = anyWidth();
                }
            } else {
                throw new IllegalArgumentException(String.format("Unknown argument: %s", transform.toString()));
            }
        }
        return ret;
    }

    private void translateEdges() {
        Point2D center = edges.getCenter();
        double cX = _treeRect.getCenterX();
        double cY = _treeRect.getCenterY();
        edges.translate(cX - center.getX(), cY - center.getY());
    }

    private void scaleEdges() {
        Double width = edges.getWidth();
        Double height = edges.getHeight();
        String lName = getLongestName();
        int strWidth = getFontMetrics().stringWidth(lName);
        double scaleX = (_treeRect.width - 1 * strWidth) / width;
        double scaleY = (_treeRect.height - 1 * strWidth) / height;

        float scale = (float) Math.min(scaleX, scaleY);

        edges.scale(scale, scale);
    }

    private String getLongestName() {
        if (longestName == null) {
            longestName = root.getLeaves().longestName();
        }
        return longestName;
    }

    private Rectangle calculateTreeRect() {
        String name = getLongestName();
        final int strWidth = getFontMetrics().stringWidth(name);
        Rectangle ret = new Rectangle();
        Dimension size = getSize();
        ret.x = insets.left;
        ret.y = insets.top + strWidth;
        ret.width = size.width - insets.left - insets.right;
        ret.height = size.height - insets.top - insets.bottom - strWidth - strWidth;

        return ret;
    }

    public void setRoot(TreeNode.Node root) {
        TreeNode.Node old = this.root;
        this.root = root;
        firePropertyChange("root", old, this.root);
    }

    private FontMetrics getFontMetrics() {
        if (_fm == null) {
            _fm = FontUtil.getFontMetrics(this);
        }
        return _fm;
    }

    public float getRotation() {
        return rotation;
    }

    /*
     * @param rotation: in radians
     */
    public void setRotation(float rotation) {
        float old = this.rotation;
        this.rotation = rotation;
        firePropertyChange("rotation", old, this.rotation);
    }

    public Integer getSigDigits() {
        return this.sigDigits;
    }

    @Override
    public void setSigDigits(Integer sigDigits) {
        Integer old = this.sigDigits;
        this.sigDigits = sigDigits;
        firePropertyChange("sigDigits", old, this.sigDigits);
    }
}
