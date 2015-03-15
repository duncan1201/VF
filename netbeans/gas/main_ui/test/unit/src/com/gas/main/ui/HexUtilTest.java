/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author dq
 */
public class HexUtilTest {
    
    public HexUtilTest() {
    }


    /**
     * Test of toHex method, of class HexUtil.
     */
    @Test
    public void testToHex_String() {
        System.out.println("toHex");
        String data = "title";
        String expResult = "7469746c65";
        String result = HexUtil.toHex(data);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of toString method, of class HexUtil.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        String hex = "7469746c65";
        String expResult = "title";
        String result = HexUtil.toString(hex);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }
    
    @Test
    public void testToBytes(){
        System.out.println("testToBytes");
        String hex = "5275220b0008ee1f43abfb230cd517f1101be18dd47c01e5daf8ebf06a80091870a0b71f84d2bbabb81fa288e769fe26ae2650c6936e8b3890b0618b15e0ba2f06a90017fdd5e757ee9c55b9793fd6f81ee709528905a4203faa9390437651e659248e98026f3427283e95416b30c45f356b8720094ebdf56a579e721116ec63";
        byte[] bytes = HexUtil.toBytes(hex);
        System.out.println(hex.length());
        System.out.println(bytes.length);
    }

}