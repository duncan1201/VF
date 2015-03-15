/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.common;

import com.gas.database.core.msa.service.api.CounterList;
import com.gas.database.core.msa.service.api.Counter;
import com.gas.database.core.msa.service.api.ICounterListService;
import com.gas.domain.core.fasta.Fasta;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service=ICounterListService.class)
public class CounterListService implements ICounterListService {
    
    @Override
    public CounterList createCounterList(Fasta fasta){
        Map<String, String> map = fasta.toMap();
        CounterList ret = _createCounterList(new ArrayList<String>(map.values()));
        
        return ret;
    }
    
    @Override
    public CounterList createCounterList(Map<String,String> map){
        return _createCounterList(new ArrayList<String>(map.values()));
    }
    
    private CounterList _createCounterList(List<String> msa){
        CounterList ret = new CounterList();
        if(msa.isEmpty()){
            return ret;
        }
        
        int length = msa.get(0).length();
        for(int baseIndex = 0; baseIndex < length; baseIndex++){
            Counter c = new Counter();
            c.setPos(baseIndex + 1);
            for(int seqIndex = 0; seqIndex < msa.size(); seqIndex++){
                char ch = msa.get(seqIndex).charAt(baseIndex);
                c.increaseCount(ch);
            }
            ret.add(c);
        }
        return ret;
    }
}
