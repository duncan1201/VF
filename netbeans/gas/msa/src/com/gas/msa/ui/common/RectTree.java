/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui.common;

import com.gas.common.ui.core.Line2DX;
import com.gas.common.ui.core.Line2DXList;
import com.gas.common.ui.light.*;
import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.MathUtil;
import com.gas.msa.ui.tree.TreePane;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.SwingConstants;

/**
 *
 * @author dq
 */
public class RectTree extends AbstractTree {

    private SHAPE nodeShape;
    private Rect2DList squareNodes = new Rect2DList();
    private Ellipse2DXList roundNodes = new Ellipse2DXList();
    private Ellipse2DXList selectableNodes = new Ellipse2DXList();
    private TreeNode.Node root;
    private TreeNode.Node selected;
    private boolean alignTips = false;
    private boolean edgeLabelVisible = true;
    private boolean nodeLabelVisible = true;
    private Insets insets;
    private Float treeHeight;
    private Integer rootHeight = 3;
    private Integer unitHeight;
    private float _unitSpace;
    private int lineWidth = 1;
    private Rectangle _treeRect;
    private Rectangle _labelRect;
    private FontMetrics _fm;
    private TransLabelList nodeLabelList = new TransLabelList();
    private TransLabelList edgeLabelList = new TransLabelList();
    private TRANSFORM transform = TRANSFORM.CLADOGRAM;
    private Line2DXList edgeList = new Line2DXList();
    private Line2DXList connectorList = new Line2DXList();
    private String[] lengthAttributeNames;
    private String selectedLengthAttribute = "length";
    private String[] nameAttributeNames;
    private String selectedNameAttribute = "name";
    private Integer sigDigits = 3;
    private Integer nodeLabelGap = 4;
    private Float fontSize;

    public RectTree() {
        super();
        setOpaque(true);
        setBackground(Color.WHITE);
        hookupListeners();
    }

    private void hookupListeners() {
        RectTreeListeners.Adpt adpt = new RectTreeListeners.Adpt();
        addMouseListener(adpt);
        addMouseMotionListener(adpt);
        addPropertyChangeListener(new RectTreeListeners.PtyListener());
    }

    public void setRoot(TreeNode.Node root) {
        TreeNode.Node old = this.root;
        this.root = root;
        firePropertyChange("root", old, this.root);
    }

    public void setSelected(TreeNode.Node selected) {
        TreeNode.Node old = this.selected;
        this.selected = selected;
        firePropertyChange("selected", old, this.selected);
    }

    protected TreeNode.Node getRoot() {
        return this.root;
    }

    @Override
    public void setFontSize(Float fontSize) {
        Float old = this.fontSize;
        this.fontSize = fontSize;
        firePropertyChange("fontSize", old, this.fontSize);
    }

    @Override
    public void setSigDigits(Integer sigDigits) {
        Integer old = this.sigDigits;
        this.sigDigits = sigDigits;
        firePropertyChange("sigDigits", old, this.sigDigits);
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

    public void setLengthAttributeNames(String[] lengthAttributeNames) {
        this.lengthAttributeNames = lengthAttributeNames;
    }

    @Override
    public String[] getLengthAttributeNames() {
        return lengthAttributeNames;
    }

    public String getSelectedLengthAttribute() {
        return selectedLengthAttribute;
    }

    @Override
    public void setSelectedLengthAttribute(String selectedLengthAttribute) {
        String old = this.selectedLengthAttribute;
        this.selectedLengthAttribute = selectedLengthAttribute;
        firePropertyChange("selectedLengthAttribute", old, this.selectedLengthAttribute);
    }

    @Override
    public void setNodeLabelVisible(boolean nodeLabelVisible) {
        boolean old = this.nodeLabelVisible;
        this.nodeLabelVisible = nodeLabelVisible;
        firePropertyChange("nodeLabelVisible", old, this.nodeLabelVisible);
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (root == null || getFont() == null || fontSize == null || nodeShape == null) {
            return;
        }
        Font font = g.getFont().deriveFont(fontSize);
        g.setFont(font);
        Dimension size = getSize();

        int numberOfLeaves = root.getNumOfLeaves();
        insets = new Insets(TreePane.NODE_WIDTH / 2 + getFontMetrics().getHeight(), 5, TreePane.NODE_WIDTH / 2 + 8, 5);
        treeHeight = root.height();
        _unitSpace = (size.height - insets.top - insets.bottom) * 1.0f / (numberOfLeaves - 1);
        if (_unitSpace < 0) {
            return;
        }
        _paintBackground(g);

        clear();

        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(lineWidth));
        TreeNode.NodeList nodeList = root.getLeaves();
        if (alignTips && nodeLabelVisible) {
            nodeList.sort();
            createAlignedNodeLabels(nodeList);
        }
        _createRecursively(_treeRect, root);

        paintUIObjects(g2d);
    }

    private void paintUIObjects(Graphics2D g2d) {
        if (nodeShape == ITree.SHAPE.CIRCLE) {
            roundNodes.paint(g2d);
        } else if (nodeShape == ITree.SHAPE.SQUARE) {
            squareNodes.paint(g2d);
        }

        nodeLabelList.paint(g2d);
        edgeLabelList.paint(g2d);
        edgeList.paint(g2d);
        connectorList.paint(g2d);
        selectableNodes.paint(g2d);
    }

    private void clear() {
        edgeList.clear();
        roundNodes.clear();
        squareNodes.clear();
        selectableNodes.clear();
        nodeLabelList.clear();
        edgeLabelList.clear();
        connectorList.clear();
        _labelRect = calculateLabelRect();
        _treeRect = calculateTreeRect();
    }

    private int getTreeUnitHeight() {
        if (unitHeight == null) {
            unitHeight = root.unitHeight();
        }
        return unitHeight;
    }

    private void _paintBackground(Graphics g) {
        Color oldColor = g.getColor();
        Dimension size = getSize();
        boolean opaque = isOpaque();
        if (opaque) {
            g.setColor(getBackground());
            g.fillRect(0, 0, size.width, size.height);
        }
        g.setColor(oldColor);
    }

    private void createAlignedNodeLabels(TreeNode.NodeList nodeList) {
        final int fontHeight = getFontMetrics().getHeight();
        
        for (int i = 0; i < nodeList.size(); i++) {
            TreeNode.Node node = nodeList.get(i);
            Object name = node.getTreeNodeName().getAttribute(selectedNameAttribute);
            float x = _labelRect.x + insets.left;
            float y = i * _unitSpace + insets.top - fontHeight * 0.5f;
            TransLabel rectX = new TransLabel(SwingConstants.CENTER, SwingConstants.CENTER);
            rectX.setX(x);
            rectX.setY(y);
            rectX.setWidth(_labelRect.width * 1.0f);
            rectX.setHeight(fontHeight * 1.0f);
            rectX.setData(name);
            nodeLabelList.add(rectX);
        }
    }

    private void _createRecursively(Rectangle rect, TreeNode.Node node) {
        int x = rect.x;
        float branchLength = getBranchLength(node);

        // draw the edge and node
        createEdgesAndNodes(rect, node, branchLength);
        // draw the line connecting to the children
        if (!node.getChildren().isEmpty()) {
            createConnectors(rect, node, branchLength);
        }

        Set<TreeNode.Node> children = node.getChildren();
        List<TreeNode.Node> tmp = new ArrayList<TreeNode.Node>(children);
        Collections.sort(tmp, new TreeNode.NodeComparator());
        int numOfNephews = 0;
        Iterator<TreeNode.Node> itr = tmp.iterator();
        while (itr.hasNext()) {
            TreeNode.Node child = itr.next();
            Rectangle newRect = new Rectangle();
            newRect.setLocation(Math.round(x + branchLength), Math.round(rect.y + numOfNephews * _unitSpace));

            newRect.setSize(Math.round(rect.width - branchLength), Math.round((child.getNumOfLeaves() - 1) * _unitSpace));
            _createRecursively(newRect, child);
            numOfNephews += child.getNumOfLeaves();
        }
    }

    private void createEdgesAndNodes(Rectangle rect, TreeNode.Node node, float branchLength) {
        float y;
        int x1 = rect.x;
        float x2 = x1 + branchLength;
        Line2DX.Float edge;
        if (!node.isLeaf()) {
            y = rect.y + rect.height * 0.5f;
            edge = new Line2DX.Float(x1, y, x2, y);
        } else {
            y = rect.y;
            edge = new Line2DX.Float(x1, y, x2, y);
        }
        edge.setData(node);
        edge.setColor(node.getColor());

        edgeList.add(edge);

        //if (nodeShape == SHAPE.CIRCLE) {
        Ellipse2DX.Float circle = new Ellipse2DX.Float(x2 - TreePane.NODE_WIDTH / 2, y - TreePane.NODE_WIDTH / 2, TreePane.NODE_WIDTH, TreePane.NODE_WIDTH);
        roundNodes.add(circle);
        circle.setData(node);

        Ellipse2DX.Float circle2 = new Ellipse2DX.Float(x2 - TreePane.NODE_WIDTH / 2, y - TreePane.NODE_WIDTH / 2, TreePane.NODE_WIDTH, TreePane.NODE_WIDTH);
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
        Rectangle2D.Float r2d = new Rectangle2D.Float(x2 - TreePane.NODE_WIDTH / 2, y - TreePane.NODE_WIDTH / 2, TreePane.NODE_WIDTH, TreePane.NODE_WIDTH);
        squareNodes.add(r2d);
        //} else if (nodeShape == SHAPE.NONE) {
        //}        

        if (!alignTips && nodeLabelVisible) {
            if (node.isLeaf()) {
                final Object name = node.getTreeNodeName().getAttribute(selectedNameAttribute);
                TransLabel nodeLabel = createUnalignedNodeLabel(name, x2, y);
                nodeLabel.setColor(node.getColor());
                nodeLabelList.add(nodeLabel);
            }
        }

        if (edgeLabelVisible) {
            if (node.getTreeNodeLength() != null && node.getTreeNodeLength().getLength() != null) {
                TransLabel edgeLabel = new TransLabel(SwingConstants.CENTER, SwingConstants.CENTER);
                Object attribute = node.getTreeNodeLength().getAttribute(selectedLengthAttribute);
                String attStr = null;
                if (attribute instanceof Float) {
                    Float attributeFloat = (Float) attribute;
                    attStr = MathUtil.toString(attributeFloat.doubleValue(), sigDigits);
                } else if (attribute instanceof TreeNodeLength.HPD) {
                    TreeNodeLength.HPD hpd = (TreeNodeLength.HPD) attribute;
                    attStr = hpd.toString(sigDigits);
                }
                edgeLabel.setData(attStr);
                edgeLabel.setX(x2 * 0.5f + x1 * 0.5f - getFontMetrics().stringWidth(attStr) * 0.5f);
                edgeLabel.setY(y - getFontMetrics().getHeight());
                edgeLabel.setWidth(getFontMetrics().stringWidth(attStr.toString()) + insets.left * 1.0f);
                edgeLabel.setHeight(1.0f * getFontMetrics().getHeight());
                edgeLabel.setColor(node.getColor());
                edgeLabelList.add(edgeLabel);
            }
        }
    }

    protected Ellipse2DXList getSelectableNodes() {
        return selectableNodes;
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

    public SHAPE getNodeShape() {
        return nodeShape;
    }

    @Override
    public void setNodeShape(SHAPE nodeShape) {
        SHAPE old = this.nodeShape;
        this.nodeShape = nodeShape;
        firePropertyChange("nodeShape", old, this.nodeShape);
    }

    private TransLabel createUnalignedNodeLabel(Object str, float x, float y) {
        TransLabel ret = new TransLabel(SwingConstants.CENTER, SwingConstants.CENTER);
        ret.setData(str);
        ret.setX(x + nodeLabelGap);
        ret.setY(y - getFontMetrics().getHeight() / 2);
        ret.setWidth(getFontMetrics().stringWidth(str.toString()) * 1.0f);
        ret.setHeight(getFontMetrics().getHeight() * 1.0f);
        return ret;
    }

    @Override
    public void setTransform(TRANSFORM transform) {
        TRANSFORM old = this.transform;
        this.transform = transform;
        firePropertyChange("transform", old, this.transform);
    }

    private void createConnectors(Rectangle rect, TreeNode.Node node, float branchLength) {
        Set<TreeNode.Node> nodes = node.getChildren();
        List<TreeNode.Node> tmp = new ArrayList<TreeNode.Node>(nodes);
        Collections.sort(tmp, new TreeNode.NodeComparator());
        TreeNode.Node first = tmp.get(0);
        TreeNode.Node last = tmp.get(tmp.size() - 1);
        float y1 = rect.y + (first.getNumOfLeaves() - 1) * _unitSpace * 0.5f;
        float y2 = rect.y + rect.height - 1 - (last.getNumOfLeaves() - 1) * _unitSpace * 0.5f;
        Line2DX.Float connector = new Line2DX.Float(rect.x + branchLength, y1, rect.x + branchLength, y2);
        connector.setData(node);
        connector.setColor(node.getColor());
        connectorList.add(connector);
    }

    public int getLineWidth() {
        return lineWidth;
    }

    @Override
    public void setLineWidth(int lineWidth) {
        int old = this.lineWidth;
        this.lineWidth = lineWidth;
        firePropertyChange("lineWidth", old, this.lineWidth);
    }

    private int getTotalBranchLength() {
        int ret = _treeRect.width - _labelRect.width - nodeLabelGap - rootHeight;
        return ret;
    }

    private Float getBranchLength(TreeNode.Node node) {
        Float ret;
        if (node.isRoot()) {
            ret = rootHeight.floatValue();
        } else {
            if (transform == TRANSFORM.NONE) {
                Float nodeLength = node.getTreeNodeLength() == null ? 0 : node.getTreeNodeLength().getLength();
                ret = getTotalBranchLength() * nodeLength / treeHeight;
            } else if (transform == TRANSFORM.EQUAL) {
                ret = getTotalBranchLength() * 1f / getTreeUnitHeight();
            } else if (transform == TRANSFORM.CLADOGRAM) {
                if (node.isLeaf()) {
                    int unitDepth = node.unitDepth();
                    ret = getTotalBranchLength() * (getTreeUnitHeight() - unitDepth + 1) * 1.0f / getTreeUnitHeight();
                } else {
                    ret = getTotalBranchLength() * 1f / getTreeUnitHeight();
                }
            } else {
                throw new IllegalArgumentException(String.format("Unknown argument: %s", transform.toString()));
            }
        }
        return ret;
    }

    private Rectangle calculateTreeRect() {
        if (_labelRect == null) {
            _labelRect = calculateLabelRect();
        }
        Dimension size = getSize();
        Rectangle rect = new Rectangle();
        rect.setLocation(insets.left, insets.top);
        rect.width = size.width - insets.left - _labelRect.width - insets.right - nodeLabelGap;
        rect.height = size.height - insets.top - insets.bottom;
        return rect;
    }

    private Rectangle calculateLabelRect() {
        Rectangle ret = new Rectangle();
        Dimension size = getSize();
        TreeNode.NodeList nodeList = root.getLeaves();
        String longestName = nodeList.longestName();

        int strWidth = getFontMetrics().stringWidth(longestName);
        ret.x = size.width - insets.right - strWidth - insets.left;
        ret.y = insets.top;
        ret.width = strWidth;
        ret.height = size.height - insets.top - insets.bottom;
        return ret;
    }

    private FontMetrics getFontMetrics() {
        if (_fm == null) {
            _fm = FontUtil.getFontMetrics(this);
        }
        return _fm;
    }
}
