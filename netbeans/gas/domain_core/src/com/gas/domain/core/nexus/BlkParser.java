/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.nexus;

import com.gas.domain.core.nexus.api.Blk;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author dq
 */
public class BlkParser {
    public Blk parse(String blockStr){
        Blk ret = new Blk();
        List<String> statements = NexusIOUtil.getStatements(blockStr);
        String blkName = NexusIOUtil.getBlockName(blockStr);
        ret.setName(blkName);
        Iterator<String> itr = statements.iterator();
        while (itr.hasNext()) {
            String statementStr = itr.next().trim();
            if (statementStr.endsWith(";")) {
                statementStr = statementStr.substring(0, statementStr.length() - 1);
            }
            String cmdName = NexusIOUtil.getCmdName(statementStr);
            String args = NexusIOUtil.getArgs(statementStr);            
            BlkStmt statement = new BlkStmt();
            statement.setCmd(cmdName);
            statement.setArgs(args);  
            
            ret.getStmts().add(statement);
        }
        return ret;        
    }
}
