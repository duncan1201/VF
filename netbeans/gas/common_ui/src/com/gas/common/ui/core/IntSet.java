/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.core;

import java.util.HashSet;
import java.util.Iterator;

/**
 *
 * @author dq
 */
public class IntSet extends HashSet<Integer> {

    public int[] toPrimitives() {
        int[] ret = new int[size()];
        Iterator<Integer> itr = iterator();
        int index = 0;
        while (itr.hasNext()) {
            Integer i = itr.next();
            ret[index] = i;
            index++;
        }
        return ret;
    }
}
