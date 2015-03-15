/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.linePlot;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 *
 * @author dq
 */
public class LinePlotCompMap extends HashMap<String, LinePlotComp> {

    public static final String GC = "GC";
    public static final String X = "?";

    public LinePlotCompMap() {
    }

    public void clearGC() {
        remove(GC);
    }

    public LinePlotComp getGC() {
        LinePlotComp ret = get(GC);
        if (ret == null) {
            ret = new LinePlotComp();
            put(GC, ret);
        }
        return ret;
    }
}
