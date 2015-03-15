/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.nexus;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author dq
 */
public class BlkStmtList extends ArrayList<BlkStmt>{
    
    public BlkStmtList getBlkStmts(String cmd){
        BlkStmtList ret = new BlkStmtList();
        Iterator<BlkStmt> itr = iterator();
        while(itr.hasNext()){
            BlkStmt stmt = itr.next();
            if(stmt.getCmd().equalsIgnoreCase(cmd)){
                ret.add(stmt);
            }
        }
        return ret;
    }
    
    public BlkStmt getBlkStmt(String cmd){
        BlkStmt ret = null;
        Iterator<BlkStmt> itr = iterator();
        while(itr.hasNext()){
            BlkStmt stmt = itr.next();
            if(stmt.getCmd().equalsIgnoreCase(cmd)){
                ret = stmt;
                break;
            }
        }
        return ret;
    }
}
