/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui.common;

import com.gas.common.ui.core.Arc2DX;
import com.gas.common.ui.core.Arc2DXList;
import com.gas.common.ui.core.GeneralPathList;
import com.gas.common.ui.core.Line2DX;
import com.gas.common.ui.core.Line2DXList;
import com.gas.common.ui.light.Ellipse2DX;
import com.gas.common.ui.light.Ellipse2DXList;
import com.gas.common.ui.light.TransLabel;
import com.gas.common.ui.light.TransLabelList;
import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.MathUtil;
import com.gas.common.ui.util.RectHelper;
import com.gas.msa.ui.tree.TreePane;
import java.awt.*;
import java.awt.geom.*;
import java.util.Collections;
import java.util.Iterator;
import javax.swing.JComponent;
import javax.swing.SwingConstants;

/**
 *
 * @author dq
 */
public class CircularTree extends AbstractTree {

    private ITree.SHAPE nodeShape ; 
    private GeneralPathList nodeList = new GeneralPathList();
    private Ellipse2DXList selectableNodes = new Ellipse2DXList();
    private TreeNode.Node root;
    private TreeNode.Node selected;
    private Insets insets = new Insets(4, 4, 4, 4);
    private float unitSpace;
    private Point center;
    private boolean showRoot = true;
    private boolean radialNodeLabel = true;
    private boolean radialEdgeLabel = true;
    private Float treeHeight;
    private int rootLength = 8;
    private Arc2DXList connectors = new Arc2DXList();
    private Line2DXList edgeList = new Line2DXList();
    private TransLabelList nodeLabels = new TransLabelList();
    private TransLabelList edgeLabels = new TransLabelList();
    private FontMetrics _fm;
    private Rectangle _drawingRect;
    private Rectangle _treeRect;
    private float rotation = 0;
    private boolean edgeLabelVisible = true;
    private boolean nodeLabelVisible = true;
    private int lineWidth = 1;
    private TRANSFORM transform = TRANSFORM.CLADOGRAM;
    private Integer unitHeight;
    private String[] lengthAttributeNames;
    private String selectedLengthAttribute = "length";
    private String[] nameAttributeNames;
    private String selectedNameAttribute = "name";
    private Integer sigDigits;
    private String longestName;
    private Float fontSize ;
    
    public CircularTree() {
        super();
        setOpaque(true);
        setBackground(Color.WHITE);      
        hookupListeners();
    }

    public Ellipse2DXList getSelectableNodes() {
        return selectableNodes;
    }
    
    private void hookupListeners() {
        addPropertyChangeListener(new CircularTreeListeners.PtyListener());
        
        CircularTreeListeners.Aptr aptr = new CircularTreeListeners.Aptr();        
        addMouseWheelListener(aptr);
        addMouseListener(aptr);
        addMouseMotionListener(aptr);
    }

    public void setSelected(TreeNode.Node selected) {
        TreeNode.Node old = this.selected;
        this.selected = selected;
        firePropertyChange("selected", old, this.selected);
    }
    
    @Override
    public void setFontSize(Float fontSize){
        Float old = this.fontSize;
        this.fontSize = fontSize;
        firePropertyChange("fontSize", old, this.fontSize);
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

    public String getSelectedLenghtAttribute() {
        return selectedLengthAttribute;
    }

    @Override
    public void setSelectedLengthAttribute(String selectedLengthAttribute) {
        String old = this.selectedLengthAttribute;
        this.selectedLengthAttribute = selectedLengthAttribute;
        firePropertyChange("selectedLengthAttribute", old, this.selectedLengthAttribute);
    }

    public void setLengthAttributeNames(String[] lengthAttributeNames) {
        this.lengthAttributeNames = lengthAttributeNames;
    }

    @Override
    public void setEdgeLabelVisible(boolean visible) {
        boolean old = this.edgeLabelVisible;
        this.edgeLabelVisible = visible;
        firePropertyChange("edgeLabelVisible", old, this.edgeLabelVisible);
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

    @Override
    public void setLineWidth(int lineWidth) {
        int old = this.lineWidth;
        this.lineWidth = lineWidth;
        firePropertyChange("lineWidth", old, this.lineWidth);
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (root == null || fontSize == null || nodeShape == null) {
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
        unitSpace = (float) (Math.PI * 2.0f / root.getNumOfLeaves());
        treeHeight = root.height();
        _drawingRect = calculateDrawingRect();
        _treeRect = calculateTreeRect();
        if (_drawingRect.width <= 0 || _drawingRect.height <= 0) {
            return;
        }
        clearUIShapes();
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        center = RectHelper.getCenter(_drawingRect);
        _create(root, 0f + getRotation(), (float) Math.PI * 2 + getRotation());

        g2d.setStroke(new BasicStroke(lineWidth));
        drawShapes(g2d);
    }

    private void clearUIShapes() {
        nodeList.clear();
        nodeLabels.clear();
        edgeLabels.clear();
        edgeList.clear();
        connectors.clear();
        selectableNodes.clear();
    }

    private Rectangle calculateDrawingRect() {
        Dimension size = getSize();
        Rectangle ret = new Rectangle(insets.top, insets.left, size.width - insets.left - insets.right, size.height - insets.top - insets.bottom);
        return ret;
    }

    private void drawShapes(Graphics2D g) {
        edgeList.paint(g);
        connectors.paint(g);
        if (nodeShape == SHAPE.SQUARE || nodeShape == SHAPE.CIRCLE) {
            nodeList.paint(g);
        }
        nodeLabels.paint(g);
        edgeLabels.paint(g);
        selectableNodes.paint(g);
    }

    /*
     * @param startAngle in radians: starting from x-axis, counter-clockwise
     * @param endAngle in radians: starting from y-axis, counter-clockwise
     */
    private void _create(TreeNode.Node node, float startAngle, float endAngle) {
        float branchLength = getBranchLength(node);

        float distanceToCenter = distanceToCenter(node);

        Float middleAngle = (startAngle + endAngle) * 0.5f;

        createEdgeRelatedUIObjects(node, distanceToCenter, branchLength, middleAngle);

        // draw the connector that connects the children
        createConnectors(node, startAngle, endAngle, distanceToCenter, branchLength);

        java.util.List<TreeNode.Node> tmp2 = new java.util.ArrayList<TreeNode.Node>(node.getChildren());
        Collections.sort(tmp2, new TreeNode.NodeComparator());
        Iterator<TreeNode.Node> itr = tmp2.iterator();
        float newStartAngle = startAngle;
        while (itr.hasNext()) {
            TreeNode.Node child = itr.next();
            int numOfLeaves = child.getNumOfLeaves();
            float newEndAngle = newStartAngle + numOfLeaves * unitSpace;
            _create(child, newStartAngle, newEndAngle);
            newStartAngle = newEndAngle;
        }
    }

    private void createEdgeRelatedUIObjects(TreeNode.Node node, final float distanceToCenter, final float branchLength, final float middleAngle) {

        final float x1 = center.x + (float) (distanceToCenter * Math.cos(middleAngle));
        final float y1 = center.y - (float) (distanceToCenter * Math.sin(middleAngle));
        final float x2 = center.x + (float) ((distanceToCenter + branchLength) * Math.cos(middleAngle));
        final float y2 = center.y - (float) ((distanceToCenter + branchLength) * Math.sin(middleAngle));
        if (!node.isRoot() || showRoot) {
            //g2d.drawLine(Math.round(x1), Math.round(y1), Math.round(x2), Math.round(y2));
            Line2DX.Float edge = new Line2DX.Float(x1, y1, x2, y2);
            edge.setColor(node.getColor());
            edgeList.add(edge);
            Ellipse2DX.Float circle = new Ellipse2DX.Float();
            circle.setFrame(x2 - TreePane.NODE_WIDTH / 2, y2 - TreePane.NODE_WIDTH / 2, TreePane.NODE_WIDTH, TreePane.NODE_WIDTH);
            if(node.isDescendantOf(selected) || node == selected){
                circle.setVisible(true);
                circle.setSelected(true);
                circle.setColor(TreePane.SELECTED_COLOR);
            }else{
                circle.setVisible(false);
            }
            circle.setData(node);
            selectableNodes.add(circle);
            GeneralPath p = null;            
            //if (nodeShape == SHAPE.CIRCLE) {
                Ellipse2D.Float e2d = new Ellipse2D.Float();
                e2d.x = x2 - TreePane.NODE_WIDTH / 2;
                e2d.y = y2 - TreePane.NODE_WIDTH / 2;
                e2d.width = e2d.height = TreePane.NODE_WIDTH;
                p = new GeneralPath();
                p.append(e2d, false);
            //} else if (nodeShape == SHAPE.SQUARE) {
                Rectangle2D.Float r2d = new Rectangle2D.Float();
                r2d.x = x2 - TreePane.NODE_WIDTH / 2;
                r2d.y = y2 - TreePane.NODE_WIDTH / 2;
                r2d.width = r2d.height = TreePane.NODE_WIDTH;
                p = new GeneralPath();
                p.append(r2d, false);
            //}
            if (p != null) {
                nodeList.add(p);
            }

            if (node.isLeaf() && nodeLabelVisible) {
                TransLabel nodeLabel = createNodeLabel(node.getTreeNodeName(), x2, y2, middleAngle, distanceToCenter, branchLength);
                nodeLabel.setColor(node.getColor());
                nodeLabels.add(nodeLabel);
            }

            if (node.getTreeNodeLength() != null && edgeLabelVisible) {
                TransLabel edgeLabel = createEdgeLabel(node.getTreeNodeLength(), (x1 + x2) * 0.5f, (y1 + y2) * 0.5f, middleAngle, distanceToCenter, branchLength);
                edgeLabel.setColor(node.getColor());
                edgeLabels.add(edgeLabel);
            }
        }
    }

    private TransLabel createEdgeLabel(TreeNodeLength nodeLength, float xMiddle, float yMiddle, float angrad, float distanceToRoot, float branchLengh) {
        int angDegree = MathUtil.round(Math.toDegrees(angrad));
        int modDegree = angDegree % 360;
        TransLabel ret = new TransLabel(SwingConstants.CENTER, SwingConstants.CENTER);        
        Object att = nodeLength.getAttribute(selectedLengthAttribute);
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
        ret.setData(attStr);
        ret.setHeight(1.0f * getFontMetrics().getHeight());
        ret.setWidth(1.0f * getFontMetrics().stringWidth(attStr) + insets.left);
        if (radialEdgeLabel) {
            ret.setX(center.x + distanceToRoot + branchLengh * 0.5f - ret.getWidth() * 0.5f);
            ret.setY(center.y - ret.getHeight());
            AffineTransform at = AffineTransform.getRotateInstance(-angrad, center.x, center.y);
            ret.setAnchor(center);
            ret.setTheta(-1.0 * angrad);
        } else {
            if (modDegree >= 0 && modDegree <= 90) {
                ret.setX(xMiddle - ret.getWidth());
                ret.setY(yMiddle - ret.getHeight());
            } else if (modDegree > 90 && modDegree <= 180) {
                ret.setX(xMiddle - ret.getWidth());
                ret.setY(yMiddle);
            } else if (modDegree > 180 && modDegree <= 270) {
                ret.setX(xMiddle - ret.getWidth());
                ret.setY(yMiddle);
            } else if (modDegree > 270 && modDegree <= 360) {
                ret.setX(xMiddle);
                ret.setY(yMiddle - ret.getHeight());
            }
        }
        return ret;
    }

    private TransLabel createNodeLabel(TreeNodeName treeNodeName, float x2, float y2, float angrad, float distanceToCenter, float branchLength) {
        int angDegree = MathUtil.round(Math.toDegrees(angrad));
        int modDegree = angDegree % 360;
        TransLabel ret = new TransLabel(SwingConstants.CENTER, SwingConstants.CENTER);
        Object name = treeNodeName.getAttribute(selectedNameAttribute);
        ret.setData(name);
        ret.setWidth(1.0f * getFontMetrics().stringWidth(name.toString()) + insets.left);
        ret.setHeight(1.0f * getFontMetrics().getHeight());
        if (radialNodeLabel) {
            ret.setX(center.x + distanceToCenter + branchLength + insets.left);
            ret.setY(center.y - ret.getHeight() / 2.0f);
            AffineTransform at = AffineTransform.getRotateInstance(angrad * -1, center.x, center.y);
            ret.setAnchor(center);
            ret.setTheta(-1.0 * angrad);
        } else {
            if (modDegree >= 0 && modDegree <= 90) {
                ret.setX(x2);
                ret.setY(y2 - ret.getHeight());
            } else if (modDegree > 90 && modDegree <= 180) {
                ret.setX(x2 - ret.getWidth());
                ret.setY(y2 - ret.getHeight());
            } else if (modDegree > 180 && modDegree <= 270) {
                ret.setX(x2 - ret.getWidth());
                ret.setY(y2);
            } else if (modDegree > 270 && modDegree <= 360) {
                ret.setX(x2);
                ret.setY(y2);
            }
        }

        return ret;
    }

    public void setRadialNodeLabel(boolean radialNodeLabel) {
        boolean old = this.radialNodeLabel;
        this.radialNodeLabel = radialNodeLabel;
        firePropertyChange("radialNodeLabel", old, this.radialNodeLabel);
    }

    public void setRadialEdgeLabel(boolean radialEdgeLabel) {
        boolean old = this.radialEdgeLabel;
        this.radialEdgeLabel = radialEdgeLabel;
        firePropertyChange("radialEdgeLabel", old, this.radialEdgeLabel);
    }

    private FontMetrics getFontMetrics() {
        if (_fm == null) {
            _fm = FontUtil.getFontMetrics(this);
        }
        return _fm;
    }

    private void createConnectors(TreeNode.Node node, final float startAngle, final float endAngle, final float distanceToCenter, final float branchLength) {
        if (node.isLeaf()) {
            return;
        }
        Arc2DX.Float arc = new Arc2DX.Float();

        arc.width = arc.height = (distanceToCenter + branchLength) * 2;
        arc.x = center.x - arc.width * 0.5f;
        arc.y = center.y - arc.height * 0.5f;

        java.util.List<TreeNode.Node> tmp = new java.util.ArrayList<TreeNode.Node>(node.getChildren());
        Collections.sort(tmp, new TreeNode.NodeComparator());
        final int firstNumOfLeaves = tmp.get(0).getNumOfLeaves();
        final int lastNumOfLeaves = tmp.get(tmp.size() - 1).getNumOfLeaves();

        final float startOffset = firstNumOfLeaves * unitSpace * 0.5f;
        final float endOffset = (lastNumOfLeaves) * unitSpace * 0.5f;
        arc.setAngleStart(Math.toDegrees(startAngle + startOffset));
        arc.setAngleExtent(Math.toDegrees(Math.abs(endAngle - startAngle) - startOffset - endOffset));
        arc.setColor(node.getColor());
        connectors.add(arc);
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        float old = this.rotation;
        this.rotation = rotation;
        firePropertyChange("rotation", old, rotation);
    }

    public SHAPE getNodeShape() {
        return nodeShape;
    }

    @Override
    public void setNodeShape(SHAPE nodeShape) {
        SHAPE old = this.nodeShape;
        this.nodeShape = nodeShape;
        firePropertyChange("nodeShape", old, this.nodeShape);
    }

    public boolean isShowRoot() {
        return showRoot;
    }

    public void setShowRoot(boolean showRoot) {
        this.showRoot = showRoot;
    }

    private int getUnitHeight() {
        if (unitHeight == null) {
            unitHeight = root.unitHeight();
        }
        return unitHeight;
    }

    private float getBranchLength(TreeNode.Node node) {
        final float totalInPixels = getTotalBranchLengthInPixels();
        float ret;
        if (node.isRoot()) {
            ret = 0f;
        } else if (transform == TRANSFORM.NONE) {
            Float nodeLength = node.getTreeNodeLength() == null || node.getTreeNodeLength().getLength() == null ? 0 : node.getTreeNodeLength().getLength();
            ret = totalInPixels * nodeLength / treeHeight;
        } else if (transform == TRANSFORM.EQUAL) {
            ret = totalInPixels * 1f / getUnitHeight();
        } else if (transform == TRANSFORM.CLADOGRAM) {
            if (node.isLeaf()) {
                int unitDepth = node.unitDepth();
                ret = totalInPixels * (getUnitHeight() - unitDepth + 1) * 1.0f / getUnitHeight();
            } else {
                ret = totalInPixels * 1f / getUnitHeight();
            }
        } else {
            throw new IllegalArgumentException(String.format("Unknown argument: %s", transform.toString()));
        }
        return ret;
    }

    /**
     * @return the total branch length in pixels
     */
    private float getTotalBranchLengthInPixels() {
        final Dimension size = getTreeRect().getSize();
        int strWidth = getFontMetrics().stringWidth(getLongestName());
        final float ret = Math.min(size.width, size.height) * 0.5f - rootLength - strWidth * 0.5f;
        return ret;
    }

    private float distanceToCenter(TreeNode.Node node) {
        float ret;
        if (node.isRoot()) {
            ret = rootLength;
        } else {
            final Float length = node.getTreeNodeLength() == null || node.getTreeNodeLength().getLength() == null ? 0 : node.getTreeNodeLength().getLength();
            final Float depth = node.depth();
            int unitDepth = node.unitDepth();
            final float totalPixels = getTotalBranchLengthInPixels();

            if (transform == TRANSFORM.NONE) {
                ret = totalPixels * (depth - length) / treeHeight + rootLength;
            } else if (transform == TRANSFORM.EQUAL) {
                ret = totalPixels * 1f * (unitDepth - 1) / getUnitHeight() + rootLength;
            } else if (transform == TRANSFORM.CLADOGRAM) {
                ret = totalPixels * 1f * (unitDepth - 1) / getUnitHeight() + rootLength;
            } else {
                throw new IllegalArgumentException(String.format("Unknown argument: %s", transform.toString()));
            }
        }
        return ret;
    }

    @Override
    public String[] getLengthAttributeNames() {
        return lengthAttributeNames;
    }

    private String getLongestName() {
        if (longestName == null) {
            TreeNode.NodeList leaves = root.getLeaves();
            longestName = leaves.longestName();
        }
        return longestName;
    }

    private Rectangle calculateTreeRect() {
        String name = getLongestName();
        int strWidth = getFontMetrics().stringWidth(name);
        Dimension size = getSize();
        Rectangle ret = new Rectangle(insets.top + strWidth, insets.left, size.width - insets.left - insets.right, size.height - insets.top - insets.bottom - strWidth - strWidth);
        return ret;
    }

    private Rectangle getTreeRect() {
        if (_treeRect == null) {
            _treeRect = calculateTreeRect();
        }
        return _treeRect;
    }

    public TreeNode.Node getRoot() {
        return root;
    }

    public void setRoot(TreeNode.Node root) {
        this.root = root;
    }

    @Override
    public void setSigDigits(Integer sigDigits) {
        Integer old = this.sigDigits;
        this.sigDigits = sigDigits;
        firePropertyChange("sigDigits", old, this.sigDigits);
    }

    public Integer getSigDigits() {
        return this.sigDigits;
    }
 
}
