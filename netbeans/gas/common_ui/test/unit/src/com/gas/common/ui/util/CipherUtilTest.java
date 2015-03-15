/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.util;

import java.util.Arrays;
import org.junit.Test;

/**
 *
 * @author dq
 */
public class CipherUtilTest {
    
    //@Test
    public void testShift(){
        System.out.println(8 >> 2);
        System.out.println(15 & 0x0f);
    }
    
    @Test
    public void testToHex(){
        System.out.println();
        byte[] bs = {1, '2', 'b', 'a', 127};
        String hex = CipherUtil.toHex(bs);
        byte[] bs2 = CipherUtil.toBytes(hex);
        boolean same = Arrays.equals(bs, bs2);
        System.out.println(same);
        System.out.println(0xf);
    }
}
