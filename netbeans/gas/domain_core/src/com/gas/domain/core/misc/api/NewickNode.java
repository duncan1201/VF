/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.misc.api;

import com.gas.common.ui.core.FloatList;
import java.awt.Color;
import java.util.*;

/**
 *
 * @author dq
 */
public class NewickNode {

    private NewickName newickName;
    private NewickLength newickLength;
    private NewickNode parent;
    private int order;
    private Set<NewickNode> children = new HashSet<NewickNode>();

    public NewickNode() {
        newickName = new NewickName();
        newickLength = new NewickLength();
    }
    
    public NewickName getNewickName() {
        return newickName;
    }

    public void setNewickName(NewickName newickName) {
        this.newickName = newickName;
    }

    public NewickLength getNewickLength() {
        return newickLength;
    }

    public void setNewickLength(NewickLength newickLength) {
        this.newickLength = newickLength;
    }

    public void addChildren(List<NewickNode> nodes) {
        if (nodes != null && !nodes.isEmpty()) {
            for (NewickNode n : nodes) {
                addChild(n);
            }
        }
    }

    protected void addChild(NewickNode node) {
        if (node != null) {
            node.setOrder(children.size());
            children.add(node);
            node.setParent(this);
        }
    }

    public NewickNode getParent() {
        return parent;
    }

    public void setParent(NewickNode parent) {
        this.parent = parent;
    }

    public Set<NewickNode> getChildren() {
        return children;
    }

    public void setChildren(Set<NewickNode> children) {
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

    public boolean isInternal() {
        return parent != null && !children.isEmpty();
    }

    public Float cumulativeLength() {
        Float ret = newickLength == null ? 0 : newickLength.getLength();
        if (parent != null) {
            ret += parent.cumulativeLength();
        }
        return ret;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();

        if (newickName != null) {
            ret.append(newickName);           
        }
        if (!getChildren().isEmpty()) {
            ret.append('(');
        }
        List<NewickNode> nodeList = new ArrayList<NewickNode>(children);
        Collections.sort(nodeList, new NodeComparator());
        Iterator<NewickNode> itr = nodeList.iterator();
        while (itr.hasNext()) {
            NewickNode child = itr.next();

            String childStr = child.toString();
            ret.append(childStr);

            if (itr.hasNext()) {
                ret.append(",");
            }
        }
        if (!getChildren().isEmpty()) {
            ret.append(')');
        }
        if (newickLength != null) {
            ret.append(':');
            ret.append(newickLength);
        }

        return ret.toString();
    }

    protected void positiseLength() {
        Float minLength = getMinLength();
        if (minLength != null && minLength < 0) {
            translateLengthRecursively(-minLength);
        }
    }

    protected void translateLengthRecursively(Float delta) {
        if (newickLength != null && newickLength.getLength() != null) {
            newickLength.setLength(newickLength.getLength() + delta);
        }
        for (NewickNode child : children) {
            child.translateLengthRecursively(delta);
        }
    }

    public Float getMinLength() {
        Float ret = null;
        if (getNewickLength() != null) {
            ret = getNewickLength().getLength();
        }
        for (NewickNode child : children) {
            Float min = child.getMinLength();
            if (ret == null || min < ret) {
                ret = min;
            }
        }
        return ret;
    }

    protected NewickNode getFirstLeaf() {
        if (isLeaf()) {
            return this;
        } else {
            NewickNode child = children.iterator().next();
            return child.getFirstLeaf();
        }
    }

    protected NodeList getLeaves() {
        NodeList ret = new NodeList();
        if (isLeaf()) {
            ret.add(this);
        } else {
            for (NewickNode child : children) {
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
            for (NewickNode child : children) {
                ret += child.getNumOfLeaves();
            }
        }
        return ret;
    }

    public Float depth() {
        Float ret = newickLength.getLength() == null ? 0f : newickLength.getLength();
        if (children.isEmpty()) {
            ret = newickLength.getLength();
        } else {
            FloatList floatList = new FloatList();
            for (NewickNode child : children) {
                Float f = child.depth();
                if (f != null) {
                    floatList.add(f);
                }
            }
            Float max = floatList.getMax();
            if (max == null || ret == null) {
                System.out.print("");
            }
            ret += max;
        }
        return ret;
    }

    public static class NodeComparator implements Comparator<NewickNode> {

        @Override
        public int compare(NewickNode o1, NewickNode o2) {
            Integer order1 = o1.getOrder();
            Integer order2 = o2.getOrder();
            return order1.compareTo(order2);
        }
    }

    protected static class NodeList extends ArrayList<NewickNode> {

        public String longestName() {
            String ret = null;
            for (int i = 0; i < size(); i++) {
                NewickNode node = get(i);
                NewickName newickName = node.getNewickName();
                String name = newickName.getName();
                if (ret == null || ret.length() < name.length()) {
                    ret = name;
                    break;
                }
            }
            return ret;
        }
    }
}
