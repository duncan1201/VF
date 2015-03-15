/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.misc.api;

import com.gas.common.ui.core.FloatList;
import com.gas.domain.core.misc.NewickIOUtil;
import com.gas.domain.core.misc.NewickParser;
import java.util.*;

/**
 *
 * @author dq
 */
public class Newick {

    private NewickNode root = new NewickNode();

    public Newick() {
    }

    public Newick(String newickStr) {
        NewickParser parser = new NewickParser();
        parser.parse(newickStr, this);
    }

    public NewickNode getRoot() {
        return root;
    }

    public void setRoot(NewickNode root) {
        this.root = root;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append(root.toString());
        ret.append(";");
        return ret.toString();
    }

    public String[] getLengthAttributeNames() {
        String[] ret = null;
        NewickNode newickNode = root.getFirstLeaf();

        return ret;
    }

    public int getNumOfLeaves() {
        return root.getNumOfLeaves();
    }

    public Float getMinLength() {
        return root.getMinLength();
    }

    public Float depth() {
        return root.depth();
    }

    public List<NewickNode> getLeaves() {
        return root.getLeaves();
    }

    public void positiseLength() {
        root.positiseLength();
    }
}
