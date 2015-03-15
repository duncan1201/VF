/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author dunqiang
 */
public class TreeSetTest {
    public static void main(String[] args){
        Set<Integer> test = new TreeSet<Integer>();
        test.add(100);
        test.add(50);
        test.add(75);
        
        Iterator<Integer> itr = test.iterator();
        while(itr.hasNext()){
            int _int = itr.next();
            System.out.println(_int);
        }

        int radix = Character.MAX_RADIX;
        String hexStr = Integer.toString(9999, Character.MAX_RADIX);        
        System.out.println();
        int parsed = Integer.parseInt(hexStr, Character.MAX_RADIX);
        System.out.println();
    }
}
