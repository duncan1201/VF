/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.tigr;

import java.util.Iterator;

/**
 *
 * @author dq
 */
public class TigrProjectUtil {
    static void abc(TigrProject pt){
        Iterator<Kromatogram> itr = pt.getKromatogramItr();
        while(itr.hasNext()){
            Kromatogram g = itr.next();
            g.getFileName();
        }
    }
}
