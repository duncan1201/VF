/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.treecombo;

import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author dq
 */
public class IndentBorder extends EmptyBorder {

    int indent = UIManager.getInt("Tree.leftChildIndent");

    public IndentBorder() {
        super(0, 0, 0, 0);
    }

    public void setDepth(int depth) {
        left = indent * depth;
    }
}
