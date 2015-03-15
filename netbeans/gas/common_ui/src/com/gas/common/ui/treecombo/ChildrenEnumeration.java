/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.treecombo;

import java.util.Enumeration;
import javax.swing.tree.TreeModel;

/**
 *
 * @author dq
 */
class ChildrenEnumeration implements Enumeration {

    TreeModel treeModel;
    Object node;
    int depth;
    int index = -1;

    public ChildrenEnumeration(TreeModel treeModel, Object node) {
        this.treeModel = treeModel;
        this.node = node;
    }

    @Override
    public boolean hasMoreElements() {
        return index < treeModel.getChildCount(node) - 1;
    }

    @Override
    public Object nextElement() {
        return treeModel.getChild(node, ++index);
    }
}
