/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.ren;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author dq
 */
public class RENSet extends HashSet<REN> {

    public Set<String> getNames() {
        Set<String> ret = new HashSet<String>();
        Iterator<REN> itr = iterator();
        while (itr.hasNext()) {
            REN ren = itr.next();
            ret.add(ren.getName());
        }
        return ret;
    }
}
