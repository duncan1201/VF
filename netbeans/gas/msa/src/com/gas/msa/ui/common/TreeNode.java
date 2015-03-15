/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui.common;

import com.gas.common.ui.core.FloatList;
import com.gas.common.ui.core.IntList;
import com.gas.domain.core.misc.api.NewickNode;
import java.awt.Color;
import java.util.*;

/**
 *
 * @author dq
 */
public class TreeNode {

    public static class Node {

        private TreeNodeName treeNodeName;
        private TreeNodeLength treeNodeLength;
        private Node parent;
        private int order;
        private Color color;
        private boolean selected;
        private Set<Node> children = new HashSet<Node>();
        /**
         * NewickNode
         */
        private NewickNode data;

        public Node() {
        }

        public TreeNodeName getTreeNodeName() {
            return treeNodeName;
        }

        public Object getData() {
            return data;
        }

        public void setData(NewickNode data) {
            this.data = data;
        }
        
        public void setTreeNodeName(TreeNodeName treeNodeName) {
            this.treeNodeName = treeNodeName;
        }

        public void setSelected(boolean s, boolean recursively) {
            setSelected(s);            
            if (recursively) {
                for (Node child : children) {
                    child.setSelected(s, recursively);
                }
            }
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public void setColor(Color color) {           
            setColor(color, false);
        }

        public void setColor(Color color, boolean recursively) {
            this.color = color;
            if(data != null){
                data.getNewickName().setRgb(color.getRGB());
            }
            if (recursively) {
                for (TreeNode.Node child : children) {
                    child.setColor(color, recursively);
                }
            }
        }

        public Color getColor() {
            return color;
        }

        public TreeNodeLength getTreeNodeLength() {
            return treeNodeLength;
        }

        public void setTreeNodeLength(TreeNodeLength treeNodeLength) {
            this.treeNodeLength = treeNodeLength;
        }

        public void addChildren(List<Node> nodes) {
            if (nodes != null && !nodes.isEmpty()) {
                for (Node n : nodes) {
                    addChild(n);
                }
            }
        }

        protected void addChild(Node node) {
            if (node != null) {
                node.setOrder(children.size());
                children.add(node);
                node.setParent(this);
            }
        }

        public Node getParent() {
            return parent;
        }

        public void setParent(Node parent) {
            this.parent = parent;
        }

        public Set<Node> getChildren() {
            return children;
        }

        public void setChildren(Set<Node> children) {
            this.children = children;
        }

        /*
         * @ret order: starts from "0"
         */
        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }

        public boolean isLeaf() {
            return children.isEmpty();
        }

        public boolean isRoot() {
            return parent == null;
        }

        public boolean isDescendantOf(TreeNode.Node node) {
            boolean ret = false;
            if (node == null) {
                return ret;
            }
            TreeNode.Node p;
            TreeNode.Node cur = this;
            while ((p = cur.getParent()) != null) {
                if (p != null) {
                    if (p == node) {
                        ret = true;
                        break;
                    }
                }
                cur = p;
            }
            return ret;
        }

        public boolean isInternal() {
            return parent != null && !children.isEmpty();
        }

        public Float depth() {
            Float ret = treeNodeLength == null || treeNodeLength.getLength() == null ? 0 : treeNodeLength.getLength();
            if (parent != null) {
                ret += parent.depth();
            }
            return ret;
        }

        @Override
        public String toString() {
            StringBuilder ret = new StringBuilder();

            if (treeNodeName != null && treeNodeName.getName() != null) {
                ret.append(treeNodeName.getName());
            }
            if (!getChildren().isEmpty()) {
                ret.append('(');
            }
            List<Node> nodeList = new ArrayList<Node>(children);
            Collections.sort(nodeList, new NodeComparator());
            Iterator<Node> itr = nodeList.iterator();
            while (itr.hasNext()) {
                Node child = itr.next();

                String childStr = child.toString();
                ret.append(childStr);

                if (itr.hasNext()) {
                    ret.append(",");
                }
            }
            if (!getChildren().isEmpty()) {
                ret.append(')');
            }
            if (treeNodeLength != null && treeNodeLength.getLength() != null) {
                ret.append(':');
                ret.append(treeNodeLength.getLength());
            }

            return ret.toString();
        }

        protected Node getFirstLeaf() {
            Node ret = null;
            if (isLeaf()) {
                ret = this;
            } else {
                Node child = getChildren().iterator().next();
                ret = child.getFirstLeaf();
            }
            return ret;
        }

        public String[] getNameAttributesNames() {
            return getFirstLeaf().getTreeNodeName().getAttributeNames();
        }

        public String[] getLengthAttributesNames() {
            return getFirstLeaf().getTreeNodeLength().getAttributeNames();
        }

        protected NodeList getLeaves() {
            NodeList ret = new NodeList();
            if (isLeaf()) {
                ret.add(this);
            } else {
                NodeList tmp = new NodeList();
                tmp.addAll(children);
                Collections.sort(tmp, new NodeComparator());
                for (Node child : tmp) {
                    ret.addAll(child.getLeaves());
                }
            }
            return ret;
        }

        protected int getNumOfLeaves() {
            int ret = 0;
            if (children.isEmpty()) {
                ret = 1;
            } else {
                for (Node child : children) {
                    ret += child.getNumOfLeaves();
                }
            }
            return ret;
        }

        public Integer unitHeight() {
            Integer ret;
            if (isRoot()) {
                ret = 0;
            } else {
                ret = 1;
            }
            if (!children.isEmpty()) {
                IntList intList = new IntList();
                for (Node child : children) {
                    Integer f = child.unitHeight();
                    if (f != null) {
                        intList.add(f);
                    }
                }
                Integer max = intList.getMax();
                ret += max;
            }
            return ret;
        }

        public int unitDepth() {
            int ret = 0;
            Node p = getParent();
            while (p != null) {
                ret++;
                p = p.getParent();
            }
            return ret;
        }

        /*
         * the length of the longest downward path to a leaf from that node
         */
        public Float height() {
            Float ret = treeNodeLength == null || treeNodeLength.getLength() == null ? 0f : treeNodeLength.getLength();
            if (children.isEmpty()) {
                ret = treeNodeLength.getLength();
            } else {
                FloatList floatList = new FloatList();
                for (Node child : children) {
                    Float f = child.height();
                    if (f != null) {
                        floatList.add(f);
                    }
                }
                Float max = floatList.getMax();
                ret += max;
            }
            return ret;
        }
    }

    public static class NodeList extends ArrayList<Node> {

        public String longestName() {
            String ret = null;
            for (int i = 0; i < size(); i++) {
                Node node = get(i);
                String name = node.getTreeNodeName().getName();
                if (ret == null || ret.length() < name.length()) {
                    ret = name;
                    break;
                }
            }
            return ret;
        }

        public void sort() {
            Collections.sort(this, new NodeComparator());
        }

        public void sort(Comparator<Node> c) {
            Collections.sort(this, c);
        }

        protected void print() {
            for (int i = 0; i < size(); i++) {
                System.out.println(get(i).toString());
            }
        }
    }

    public static class NodeComparator implements Comparator<Node> {

        @Override
        public int compare(Node o1, Node o2) {
            Integer order1 = o1.getOrder();
            Integer order2 = o2.getOrder();
            return order1.compareTo(order2);
        }
    }
}
