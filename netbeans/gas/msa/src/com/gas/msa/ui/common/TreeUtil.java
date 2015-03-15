/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui.common;

import com.gas.domain.core.misc.api.Newick;
import com.gas.domain.core.misc.api.NewickNode;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author dq
 */
public class TreeUtil {
    
    public static TreeNode.Node to(Newick newick){
        NewickNode nNode = newick.getRoot();
        TreeNode.Node ret = toRecursively(nNode);
        return ret;
    }
    
    private static TreeNode.Node toRecursively(NewickNode nNode){
        TreeNode.Node ret = new TreeNode.Node();
        ret.setData(nNode);
        ret.setTreeNodeName(new TreeNodeName(nNode.getNewickName()));
        if(nNode.getNewickLength() != null){
            ret.setTreeNodeLength(new TreeNodeLength(nNode.getNewickLength()));
        }
        ret.setOrder(nNode.getOrder());
        ret.setColor(new Color(nNode.getNewickName().getRgb()));
        List<NewickNode> tmp = new ArrayList<NewickNode>(nNode.getChildren());
        Collections.sort(tmp, new NewickNode.NodeComparator());
        for(NewickNode nNodeChild: tmp){
            TreeNode.Node retChild = toRecursively(nNodeChild);
            ret.addChild(retChild);
        }
        return ret;
    }
}
