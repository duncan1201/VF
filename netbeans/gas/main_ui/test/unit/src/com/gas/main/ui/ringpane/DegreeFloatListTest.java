/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.ringpane;

import org.junit.Test;

/**

 @author dq
 */
public class DegreeFloatListTest {
    
    @Test
    public void testCount(){
        DegreeFloatList list = new DegreeFloatList();
        list.add(1f);
        list.add(320f);
        list.add(350f);
        //list.add(2f);
        //list.add(34f);
        
        System.out.println(list.count());
    }
}
