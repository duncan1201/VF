/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui;

import java.util.Iterator;
import java.util.Map.Entry;

/**
 *
 * @author dunqiang
 */
public class SystemTest {
    public static void main(String[] args){
        Iterator<Entry<Object, Object>> entrySetItr = System.getProperties().entrySet().iterator();
        while(entrySetItr.hasNext()){
            Entry<Object, Object> entry = entrySetItr.next();
            System.out.println(entry.getKey() + "=" + entry.getValue());
        }
    }
}
