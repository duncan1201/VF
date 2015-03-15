/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.dynamicTable;

import com.gas.common.ui.util.CommonUtil;
import java.util.Comparator;

/**
 *
 * @author dq
 */
public class Comparators {

    private Comparators(){}
    
    protected static class LengthColumnComparator implements Comparator<String> {

        @Override
        public int compare(String o1, String o2) {
            Integer i1 = CommonUtil.parseInt(o1);
            Integer i2 = CommonUtil.parseInt(o2);
            if (i1 == null && i2 == null) {
                return 0;
            } else if (i1 == null) {
                return 1;
            } else if (i2 == null) {
                return -1;
            } else {
                return i1.compareTo(i2);
            }
        }
    }
}
