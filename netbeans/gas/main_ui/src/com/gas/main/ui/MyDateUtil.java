/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui;

/**
 *
 * @author dq
 */
class MyDateUtil {
    static final int MILLIS_PER_MINUTE = 1000 * 60;
    static final int MILLIS_PER_HOUR = 1000 * 3600;
    static final int MILLIS_PER_DAY = MILLIS_PER_HOUR * 24;
    
    static String toString(long time){
        StringBuilder ret = new StringBuilder();        
        long days = time / MILLIS_PER_DAY ;
        if(days > 0){
            ret.append(days);
            ret.append(days > 1 ? " days " : " day ");
        }
        time = modulus(time, MILLIS_PER_DAY);
        long hours = time / MILLIS_PER_HOUR;
        if(hours > 0){
            ret.append(hours);
            ret.append(hours > 1 ? " hours " : " hour ");
        }
        time = modulus(time, MILLIS_PER_HOUR);
        return ret.toString();
    }
    
    private static long modulus(long time, long divider){        
        while(time > divider){
            time = time - divider;
        }
        return time;
    }
}
