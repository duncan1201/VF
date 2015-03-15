/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.nexus.api;

import com.gas.common.ui.core.StringList;
import com.gas.domain.core.nexus.BlkStmt;
import com.gas.domain.core.nexus.BlkStmtList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author dq
 */
public class TreesBlk {
    
    private Blk blk;
    
    public TreesBlk(Blk blk){
        this.blk = blk;
    }
    
    public int getTreeCount(){
        BlkStmtList stmtList = blk.getStmts();
        BlkStmtList treeStmts = stmtList.getBlkStmts("tree");
        return treeStmts.size();
    }
    
    public Map<String, String> getTreeStrs(){
        Map<String, String> ret = new HashMap<String, String>();
        BlkStmtList stmtList = blk.getStmts();
        BlkStmtList treeStmts = stmtList.getBlkStmts("tree");
        for(BlkStmt stmt: treeStmts){
            String args = stmt.getArgs();
            int index = args.indexOf("=");
            if(index < 0){
                continue;
            }
            String treeName = args.substring(0, index).trim();
            String newickStr = args.substring(index + 1, args.length()).trim();
            ret.put(treeName, newickStr);
        }
        return ret;
    }
}
