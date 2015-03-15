/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.as;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author dunqiang
 */
public class QualifierComparator implements Comparator<Qualifier> {

    final static Map<String, Integer> ORDER_MAP = new HashMap<String, Integer>();

    static {
        int i = 0;
        ORDER_MAP.put("organism", i++);
        ORDER_MAP.put("mol_type", i++);
        ORDER_MAP.put("gene", i++);
        ORDER_MAP.put("gene_synonym", i++);
        ORDER_MAP.put("standard_name", i++);
        ORDER_MAP.put("experiment", i++);
        ORDER_MAP.put("note", i++);
        ORDER_MAP.put("inference", i++);
        ORDER_MAP.put("number", i++);
        ORDER_MAP.put("codon_start", i++);
        ORDER_MAP.put("product", i++);
        ORDER_MAP.put("protein_id", i++);
        ORDER_MAP.put("db_xref", i++);
        ORDER_MAP.put("translation", i++);
        ORDER_MAP.put("chromosome", i++);
        ORDER_MAP.put("Sequence", i++);
        ORDER_MAP.put("TM", i++);
        ORDER_MAP.put("GC%", i++);
        ORDER_MAP.put("Max Complementarity", i++);
        ORDER_MAP.put("Max 3' Complementarity", i++);
        ORDER_MAP.put("Hairpin", i++);
        ORDER_MAP.put("Product Size", i++);
        ORDER_MAP.put("Problems", i++);
        ORDER_MAP.put("Monovalent cation conc.", i++);
        ORDER_MAP.put("Divalent cation conc.", i++);
        ORDER_MAP.put("dNTP conc.", i++);
        ORDER_MAP.put("Annealing oligo conc.", i++);
    }
    
    private Integer getIndex(String _key){
        Integer ret = null;
        Iterator<String> itr = ORDER_MAP.keySet().iterator();
        while(itr.hasNext()){
            String key = itr.next();
            if(key.equals(_key)){
                ret = ORDER_MAP.get(key);
                break;
            }
        }
        return ret;
    }

    @Override
    public int compare(Qualifier o1, Qualifier o2) {
        int ret = -1;
        Integer index1 = getIndex(o1.getKey());
        Integer index2 = getIndex(o2.getKey());

        if (index1 != null && index2 != null) {
            ret = index1.compareTo(index2);
        } else if (index1 == null) {
            ret = -1;
        } else if (index2 == null) {
            ret = 1;
        } else {
            ret = o1.getKey().compareToIgnoreCase(o2.getKey());
        }

        return ret;
    }
}
