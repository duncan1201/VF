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
public class CharSet extends HashSet<Character> {

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        Iterator<Character> itr = iterator();
        while (itr.hasNext()) {
            Character c = itr.next();
            ret.append(c);
            if (itr.hasNext()) {
                ret.append(',');
            }
        }
        return ret.toString();
    }
}
