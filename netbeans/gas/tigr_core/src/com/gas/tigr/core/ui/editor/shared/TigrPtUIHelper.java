/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.tigr.core.ui.editor.shared;

import com.gas.common.ui.util.StrUtil;
import com.gas.domain.core.tasm.Condig;
import com.gas.domain.core.tasm.Rid;
import com.gas.domain.core.tigr.Kromatogram;
import com.gas.domain.core.tigr.TigrProject;
import com.gas.tigr.core.ui.ckpanel.ChromatogramComp2;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author dq
 */
public class TigrPtUIHelper {
    public static List<ChromatogramComp2.Read> toReads(Condig condig){
        List<ChromatogramComp2.Read> ret = new ArrayList<ChromatogramComp2.Read>();
        Iterator<Rid> itr = condig.getRids().iterator();
        while(itr.hasNext()){
            Rid rid = itr.next();
            ChromatogramComp2.Read read = new ChromatogramComp2.Read();
            read.setName(rid.getSeqName());
            read.setBases(StrUtil.toChars(rid.getLsequence()));
            read.setOffset(rid.getOffset());
            read.setSeqLend(rid.getSeq_lend());
            read.setSeqRend(rid.getSeq_rend());
            read.setKromatogram(rid.getKromatogram());
            ret.add(read);
        }
        return ret;
    }
    
    public void aaa(List<ChromatogramComp2.Read> reads, TigrProject tigrPt){
        Map<String, Kromatogram> kMap = tigrPt.getKromatogramsMap();
        Iterator<ChromatogramComp2.Read> rItr = reads.iterator();
        while(rItr.hasNext()){
            ChromatogramComp2.Read read = rItr.next();
            String name = read.getName(); 
            
            Kromatogram k = kMap.get(name);
            read.setKromatogram(k);
        }
 
    }
    
    public Kromatogram bbb(Kromatogram in, String gappedSeq, int startPos){
        Kromatogram ret = new Kromatogram();
        
        return ret;
    }
}
