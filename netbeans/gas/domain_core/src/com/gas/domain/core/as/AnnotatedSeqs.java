/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.as;

import com.gas.domain.core.fasta.Fasta;
import java.util.Date;

/**
 *
 * @author dq
 */
public class AnnotatedSeqs {
    private AnnotatedSeqs(){}
    
    public static AnnotatedSeq create(Fasta fasta){
        if(fasta == null || fasta.getRecords().size() == 0){
            throw new IllegalArgumentException("input cannot be null or empty");
        }
        AnnotatedSeq ret = new AnnotatedSeq();
        Fasta.Record record = fasta.getRecords().iterator().next();
        ret.setName(record.getDefinitionLine().getName());
        ret.setAccession("");
        ret.setSequence(record.getSequence());
        ret.setCircular(false);
        ret.getSequenceProperties().put(AsHelper.MOL_TYPE, AsHelper.DNA);
        ret.setLastModifiedDate(new Date());
        ret.setCreationDate(new Date());
        return ret;
    }
}
