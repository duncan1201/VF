/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.core;

import java.util.Comparator;

/**
 *
 * @author dunqiang
 */
public class StringComparator implements Comparator<String> {

    boolean caseSensitive;
    boolean ascending;

    public StringComparator() {
        this(true, false);
    }

    public StringComparator(boolean ascending, boolean caseSensitive) {
        this.ascending = ascending;
        this.caseSensitive = caseSensitive;
    }

    @Override
    public int compare(String o1, String o2) {
        int ret = 0;
        if (caseSensitive) {
            ret = o1.compareTo(o2);
        } else {
            ret = o1.compareToIgnoreCase(o2);
        }

        if (!ascending) {
            ret *= -1;
        }
        return ret;
    }
}
