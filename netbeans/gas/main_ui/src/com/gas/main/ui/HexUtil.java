/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui;

import java.io.UnsupportedEncodingException;
import org.openide.util.Exceptions;

/**
 *
 * @author dq
 */
/**
 * General utilities for the second chapter examples.
 */
class HexUtil {

    private static String digits = "0123456789abcdef";

    /**
     * Return length many bytes of the passed in byte array as a hex string.
     *
     * @param data the bytes to be converted.
     * @param length the number of bytes in the data block to be converted.
     * @return a hex representation of length bytes of data.
     */
    static String toHex(byte[] data, int length) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i != length; i++) {
            int v = data[i] & 0xff;
            buf.append(digits.charAt(v >> 4));
            buf.append(digits.charAt(v & 0xf));
        }
        return buf.toString();
    }

    /**
     * Return the passed in byte array as a hex string.
     *     
* @param data the bytes to be converted.
     * @return a hex representation of data.
     */
    static String toHex(byte[] data) {
        return toHex(data, data.length);
    }
    
    static String toHex(String data){
        String ret = null;
        try {
            byte[] bytes = data.getBytes("UTF-8");
            ret = toHex(data.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            Exceptions.printStackTrace(ex);
        } finally{
            return ret;
        }
        
    }
    
    static String toString(String hex){
        byte[] bytes = toBytes(hex);
        String ret = null;
        try {
            ret = new String(bytes, "UTF8");
        } catch (UnsupportedEncodingException ex) {
            Exceptions.printStackTrace(ex);
        }
        return ret;
    }

    static byte[] toBytes(String hex) {
        int len = hex.length();
        if(len % 2 != 0){
            //throw new IllegalArgumentException("length must be even");
            return null;
        }
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }
}
