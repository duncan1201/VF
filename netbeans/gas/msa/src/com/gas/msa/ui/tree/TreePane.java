/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui.tree;

import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.misc.api.Newick;
import com.gas.domain.core.msa.MSA;
import com.gas.domain.ui.editor.msa.api.IMSAEditor;
import com.gas.domain.ui.pref.MSAPref;
import com.gas.msa.ui.common.CircularTree;
import com.gas.msa.ui.common.ITree;
import com.gas.msa.ui.common.ITree.TRANSFORM;
import com.gas.msa.ui.common.RadialTree;
import com.gas.msa.ui.common.RectTree;
import com.gas.msa.ui.common.TreeNode;
import com.gas.msa.ui.common.TreeUtil;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.lang.ref.WeakReference;
import javax.swing.JPanel;

/**
 *
 * @author dq
 */
public class TreePane extends JPanel {

    public enum FORM {

        CIRCULAR, RADIAL, RECTANGLE;

        public static FORM get(String str) {
            FORM ret = null;
            if (CIRCULAR.toString().equalsIgnoreCase(str)) {
                ret = CIRCULAR;
            } else if (RADIAL.toString().equalsIgnoreCase(str)) {
                ret = RADIAL;
            } else if (RECTANGLE.toString().equalsIgnoreCase(str)) {
                ret = RECTANGLE;
            }
            return ret;
        }
    };
    private WeakReference<CircularTree> circularTreeRef;
    private WeakReference<RadialTree> radialTreeRef;
    private WeakReference<RectTree> rectTreeRef;
    private WeakReference<CardLayout> cardLayoutRef;
    private WeakReference<JPanel> treePanelRef;
    private WeakReference<SidePanel> sidePanelRef;
    private Boolean nodeLabelVisible;
    private String selectedEdgeAtt = "length";
    private String selectedNodeAtt = "name";
    private Integer sigDigits;
    private Integer lineWidth;
    private Float fontSize;
    private ITree.SHAPE nodeShape;
    private TreeNode.Node selected;
    public static int NODE_WIDTH = 8;
    public static Color SELECTED_COLOR = Color.BLUE;

    public TreePane() {
        setLayout(new BorderLayout());

        JPanel treePanel = createTreePanel();
        add(treePanel, BorderLayout.CENTER);
        treePanelRef = new WeakReference<JPanel>(treePanel);

        SidePanel sidePanel = new SidePanel();
        add(sidePanel, BorderLayout.EAST);
        sidePanelRef = new WeakReference<SidePanel>(sidePanel);

        hookupListeners();

    }

    private MSA getMsa() {
        IMSAEditor editor = UIUtil.getParent(this, IMSAEditor.class);
        return editor.getMsa();
    }

    public void refreshUI(MSA msa) {
        if (msa == null || msa.getNewick() == null) {
            return;
        }
        initValues(msa);
        Newick newick = msa.getNewick();
        TreeNode.Node root = TreeUtil.to(newick);

        String[] attNames = root.getNameAttributesNames();
        getSidePanel().getNodePanel().setNameAttributeNames(attNames);

        attNames = root.getLengthAttributesNames();
        getSidePanel().getBranchPanel().setAttNames(attNames);

        getSidePanel().populateUI(msa);
    }

    private void initValues(MSA msa) {

        String treeForm = msa.getMsaSetting().getTreeForm();
        if (treeForm.equalsIgnoreCase(TreePane.FORM.CIRCULAR.toString())) {
            getSidePanel().getGeneralPanel().getCircularButton().setSelected(true);
        } else if (treeForm.equalsIgnoreCase(TreePane.FORM.RADIAL.toString())) {
            getSidePanel().getGeneralPanel().getRadialButton().setSelected(true);
        } else if (treeForm.equalsIgnoreCase(TreePane.FORM.RECTANGLE.toString())) {
            getSidePanel().getGeneralPanel().getRectButton().setSelected(true);
        }

        setSigDigits(msa.getMsaSetting().getSigDigits());
        setFontSize(MSAPref.getInstance().getFontSize());
        setNodeShape(ITree.SHAPE.get(MSAPref.getInstance().getNodeShape()));

        Newick newick = msa.getNewick();
        TreeNode.Node root = TreeUtil.to(newick);
        getRectTree().setRoot(root);
        getRadialTree().setRoot(root);
        getCircularTree().setRoot(root);

        setEdgeLabelVisible(msa.getMsaSetting().isTreeEdgeLabelDisplay());
        setNodeLabelVisible(msa.getMsaSetting().isNodeLabelDisplay());

        ITree.TRANSFORM transform = ITree.TRANSFORM.get(msa.getMsaSetting().getTreeEdgeTransform());

        getRectTree().setTransform(transform);
        getRadialTree().setTransform(transform);
        getCircularTree().setTransform(transform);
    }

    public void paintChildren() {
        getRectTree().repaint();
        getRadialTree().repaint();
        getCircularTree().repaint();
    }

    public void setSelected(TreeNode.Node node) {
        TreeNode.Node old = this.selected;
        this.selected = node;
        firePropertyChange("selected", old, node);
    }

    public TreeNode.Node getSelected() {
        return this.selected;
    }

    public void setNodeShape(ITree.SHAPE nodeShape) {
        ITree.SHAPE old = this.nodeShape;
        this.nodeShape = nodeShape;
        firePropertyChange("nodeShape", old, this.nodeShape);
    }

    public void setFontSize(Float fontSize) {
        Float old = this.fontSize;
        this.fontSize = fontSize;
        firePropertyChange("fontSize", old, this.fontSize);
    }

    public void setLineWidth(Integer lineWidth) {
        getRectTree().setLineWidth(lineWidth);
        getRadialTree().setLineWidth(lineWidth);
        getCircularTree().setLineWidth(lineWidth);
    }

    public Integer getSigDigits() {
        return sigDigits;
    }

    public void setSigDigits(Integer sigDigits) {
        Integer old = this.sigDigits;
        this.sigDigits = sigDigits;
        firePropertyChange("sigDigits", old, this.sigDigits);
    }

    public String getSelectedEdgeAtt() {
        return selectedEdgeAtt;
    }

    public void setSelectedEdgeAtt(String selectedEdgeAtt) {
        String old = this.selectedEdgeAtt;
        this.selectedEdgeAtt = selectedEdgeAtt;
        firePropertyChange("selectedEdgeAtt", old, this.selectedEdgeAtt);
    }

    public String getSelectedNodeAtt() {
        return selectedNodeAtt;
    }

    public void setSelectedNodeAtt(String selectedNodeAtt) {
        String old = this.selectedNodeAtt;
        this.selectedNodeAtt = selectedNodeAtt;
        firePropertyChange("selectedNodeAtt", old, this.selectedNodeAtt);
    }

    public Boolean isNodeLabelVisible() {
        return nodeLabelVisible;
    }

    public void setNodeLabelVisible(Boolean nodeLabelVisible) {
        getRectTree().setNodeLabelVisible(nodeLabelVisible);
        getRadialTree().setNodeLabelVisible(nodeLabelVisible);
        getCircularTree().setNodeLabelVisible(nodeLabelVisible);
    }

    public void setEdgeLabelVisible(Boolean edgeLabelVisible) {
        getRectTree().setEdgeLabelVisible(edgeLabelVisible);
        getRadialTree().setEdgeLabelVisible(edgeLabelVisible);
        getCircularTree().setEdgeLabelVisible(edgeLabelVisible);
    }

    protected SidePanel getSidePanel() {
        return sidePanelRef.get();
    }

    public void setTransform(TRANSFORM transform) {
        getRectTree().setTransform(transform);
        getRadialTree().setTransform(transform);
        getCircularTree().setTransform(transform);
    }

    public void setForm(FORM form) {
        getCardLayout().show(getTreePanel(), form.toString());
    }

    public ITree getVisibleTree() {
        if (circularTreeRef.get().isVisible()) {
            return circularTreeRef.get();
        } else if (radialTreeRef.get().isVisible()) {
            return radialTreeRef.get();
        } else if (rectTreeRef.get().isVisible()) {
            return rectTreeRef.get();
        } else {
            return null;
        }
    }

    protected JPanel getTreePanel() {
        return treePanelRef.get();
    }

    private JPanel createTreePanel() {
        JPanel ret = new JPanel();

        CardLayout cardLayout = new CardLayout();
        ret.setLayout(cardLayout);
        cardLayoutRef = new WeakReference<CardLayout>(cardLayout);

        RectTree rectTree = new RectTree();
        ret.add(rectTree, TreePane.FORM.RECTANGLE.toString());
        rectTreeRef = new WeakReference<RectTree>(rectTree);

        RadialTree radialTree = new RadialTree();
        ret.add(radialTree, TreePane.FORM.RADIAL.toString());
        radialTreeRef = new WeakReference<RadialTree>(radialTree);

        CircularTree circularTree = new CircularTree();
        ret.add(circularTree, TreePane.FORM.CIRCULAR.toString());

        circularTreeRef = new WeakReference<CircularTree>(circularTree);

        return ret;
    }

    protected CardLayout getCardLayout() {
        return cardLayoutRef.get();
    }

    private void hookupListeners() {
        addPropertyChangeListener(new TreePaneListeners.PtyListener());
        MSAPref.getInstance().addPropertyChangeListener(new TreePaneListeners.PrefPtyListener(this));
    }

    protected CircularTree getCircularTree() {
        return circularTreeRef.get();
    }

    protected RadialTree getRadialTree() {
        return radialTreeRef.get();
    }

    protected RectTree getRectTree() {
        return rectTreeRef.get();
    }
}
