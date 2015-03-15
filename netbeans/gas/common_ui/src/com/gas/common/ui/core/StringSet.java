/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.core;

import com.gas.common.ui.util.StrUtil;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;

/**
 *
 * @author dq
 */
public class StringSet extends HashSet<String> {

    public StringSet() {
    }

    public StringSet(String[] strs) {
        for (String s : strs) {
            add(s);
        }
    }

    public void toUpperCase() {
        StringSet tmp = new StringSet();

        Iterator<String> itr = iterator();
        while (itr.hasNext()) {
            String str = itr.next();
            String uStr = str.toUpperCase(Locale.ENGLISH);
            tmp.add(uStr);
        }

        this.clear();
        this.addAll(tmp);
    }

    public String getLongest() {
        String ret = null;
        Iterator<String> itr = iterator();
        while (itr.hasNext()) {
            String str = itr.next();
            if (ret == null || ret.length() < str.length()) {
                ret = str;
            }
        }
        return ret;
    }

    public String toHtmlList() {
        return StrUtil.toHtmlList(this);
    }

    public String toString(Character delimiter) {
        StringBuilder ret = new StringBuilder();
        Iterator<String> itr = iterator();
        while (itr.hasNext()) {
            String str = itr.next();
            ret.append(str);
            if (itr.hasNext()) {
                ret.append(delimiter);
            }
        }
        return ret.toString();
    }
}
