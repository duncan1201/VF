/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.tigr;

import com.gas.domain.core.fasta.Fasta;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author dq
 */
public class KromatogramList extends ArrayList<Kromatogram> implements Cloneable {
    
    public KromatogramList(){}
    
    public KromatogramList(Collection<Kromatogram> kromatograms){
        addAll(kromatograms);
    }
    
    public Fasta toFasta(){        
        Fasta fasta = new Fasta();
        for(Kromatogram k: this){
            Fasta.Record r = k.toFastaRecord();
            fasta.getRecords().add(r);
        }
        return fasta;
    }
    
    @Override
    public KromatogramList clone(){
        KromatogramList ret = new KromatogramList();
        for(Kromatogram k: this){
            ret.add(k.clone());
        }
        return ret;
    }
}
