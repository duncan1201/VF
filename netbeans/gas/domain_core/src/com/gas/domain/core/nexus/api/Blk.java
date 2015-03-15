/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.nexus.api;

import com.gas.domain.core.nexus.BlkStmt;
import com.gas.domain.core.nexus.BlkStmtList;
import java.util.Iterator;

/**
 *
 * @author dq
 */
public class Blk {
    
    private String name;
    private BlkStmtList stmts = new BlkStmtList();
    
    public Blk(){
    }
    
    public Blk(String name){
        this.name = name;
    }
    
    @Override
    public String toString(){
        StringBuilder ret = new StringBuilder();
        ret.append(String.format("begin %s;\n", name));
        Iterator<BlkStmt> itr = stmts.iterator();
        while(itr.hasNext()){
            BlkStmt statement = itr.next();
            ret.append(statement.toString());
            ret.append('\n');
        }
        ret.append("end;");
        return ret.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BlkStmtList getStmts() {
        return stmts;
    }

    public void setStmts(BlkStmtList statements) {
        this.stmts = statements;
    }        
    
    public boolean isDataBlock(){
        return name.equalsIgnoreCase("data");
    }
    
    public boolean isTaxaBlock(){
        return name.equalsIgnoreCase("taxa");
    }    
}
