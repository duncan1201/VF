/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.entrez.core.EInfo.api;

import com.gas.entrez.core.EInfo.api.EInfoResult.Field;
import java.util.Comparator;

/**
 *
 * @author dq
 */
public class EInfoResultComparators {
    public static class FieldComparator implements Comparator<EInfoResult.Field>{

        @Override
        public int compare(Field o1, Field o2) {
            int ret = 0;
            if(o1.getFullName().equalsIgnoreCase("all fields")){
                ret = -1;
            }else if(o2.getFullName().equalsIgnoreCase("all fields")){
                ret = 1;
            }else{
                ret = o1.getFullName().compareToIgnoreCase(o2.getFullName());
            }
            
            return ret;
        }
    }
}
