/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.nexus.api;

import com.gas.common.ui.core.CharList;
import com.gas.common.ui.core.StringList;
import com.gas.common.ui.util.StrUtil;
import com.gas.domain.core.nexus.BlkStmt;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author dq
 */
public class CharactersBlk {

    private Blk blk;

    public CharactersBlk(Blk blk) {
        this.blk = blk;
    }
    
    public LinkedHashMap<String, String> getMatrix(){
        LinkedHashMap<String, String> ret = new LinkedHashMap<String, String>();
        BlkStmt stmt = blk.getStmts().getBlkStmt("matrix");
        String args = stmt.getArgs();
        StringList lines = StrUtil.readLine(args);
        CharList whiteSpace = new CharList('\t', ' ');
        for(String line: lines){
            line = line.trim();
            int index = StrUtil.indexOf(line, whiteSpace);
            if(index < 0){
                continue;
            }
            String taxa = line.substring(0, index);
            String seq = line.substring(index + 1, line.length());
            ret.put(taxa, seq);
        }
        return ret;
    }

    public Integer getLength() {
        BlkStmt stmt = blk.getStmts().getBlkStmt("dimensions");
        String args = stmt.getArgs();
        String ncharStr = StrUtil.extract("nchar=(\\d+)", args);
        if(ncharStr != null && !ncharStr.isEmpty()){
            return Integer.parseInt(ncharStr);
        }else{
            return null;
        }
    }
}
