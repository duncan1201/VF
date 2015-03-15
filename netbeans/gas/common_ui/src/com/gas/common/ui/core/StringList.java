/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.core;

import com.gas.common.ui.util.StrUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

/**
 *
 * @author dq
 */
public class StringList extends ArrayList<String> {

    private String longest;

    public StringList() {
    }

    public StringList(String... str) {        
        for (int i = 0; str != null && i < str.length; i++) {
            add(str[i]);
        }
    }

    public boolean isPrefixOf(String s, boolean ignoreCase) {
        boolean ret = false;
        if (ignoreCase) {
            s = s.toLowerCase(Locale.ENGLISH);
        }
        Iterator<String> itr = iterator();
        while (itr.hasNext()) {
            String str = itr.next();
            if (ignoreCase) {
                str = str.toLowerCase(Locale.ENGLISH);
            }
            if (s.startsWith(str)) {
                ret = true;
                break;
            }
        }
        return ret;
    }

    public StringList startsWith(String s) {
        StringList ret = new StringList();
        for (String str : this) {
            if (str.startsWith(s)) {
                ret.add(str);
            }
        }
        return ret;
    }

    public StringList intersect(StringList another) {
        StringList ret = new StringList();
        for (String str : this) {
            if (another.contains(str)) {
                ret.add(str);
            }
        }
        return ret;
    }

    private boolean isDuplicate(String str) {
        boolean ret = indexOf(str) > -1 && indexOf(str) != lastIndexOf(str);
        return ret;
    }

    public StringSet getDuplicates() {
        Set<String> uniques = new HashSet<String>(this);
        StringSet ret = new StringSet();
        Iterator<String> itr = uniques.iterator();
        while (itr.hasNext()) {
            String str = itr.next();
            boolean duplicate = isDuplicate(str);
            if (duplicate) {
                ret.add(str);
            }
        }
        return ret;
    }

    public StringList(Collection<String> all) {
        addAll(all);
    }

    /**
     *
     */
    public boolean startsWith(String t, boolean ignoreCase) {
        boolean ret = false;
        if (ignoreCase) {
            t = t.toUpperCase(Locale.ENGLISH);
        }
        Iterator<String> itr = iterator();
        while (itr.hasNext()) {
            String str = itr.next();
            if (ignoreCase) {
                str = str.toUpperCase(Locale.ENGLISH);
            }
            if (str.startsWith(t)) {
                ret = true;
                break;
            }
        }
        return ret;
    }

    public boolean containsIgnoreCase(String str) {
        boolean ret = false;
        for (int i = 0; i < size(); i++) {
            String s = get(i);
            if (s.equalsIgnoreCase(str)) {
                ret = true;
                break;
            }
        }
        return ret;
    }

    @Override
    public boolean add(String str) {
        boolean ret = super.add(str);
        updateStatistics(str);
        return ret;
    }

    public String longest() {
        return longest;
    }

    public void toUpperCase() {
        for (int i = 0; i < size(); i++) {
            String u = get(i).toUpperCase(Locale.ENGLISH);
            this.set(i, u);
        }
    }

    public String toHtmList() {
        return StrUtil.toHtmlList(this);
    }

    public int getMaxLength() {
        int ret = 0;
        if (longest != null) {
            ret = longest.length();
        }
        return ret;
    }

    private void updateStatistics(String str) {
        if (longest == null || longest.length() < str.length()) {
            longest = str;
        }
    }

    public void removeDuplicates() {
        Set<String> set = new HashSet<String>();
        set.addAll(this);

        this.clear();
        this.addAll(set);
    }
    
    public String toString(Character separator){
        return toString(separator.toString());
    }

    public String toString(String separator) {
        return toString(separator, false);
    }

    public String toString(String separator, boolean singleQuotes) {
        StringBuilder ret = new StringBuilder();
        Iterator<String> itr = iterator();
        while (itr.hasNext()) {
            String str = itr.next();
            if (singleQuotes) {
                ret.append(String.format("'%s'", str));
            } else {
                ret.append(str);
            }

            if (itr.hasNext()) {
                ret.append(separator);
            }
        }
        return ret.toString();
    }
    
    public void addAll(String[] strs){
        addAll(Arrays.asList(strs));
    }

    @Override
    public boolean addAll(Collection strs) {
        boolean ret = super.addAll(strs);
        for (Object o : strs) {
            updateStatistics(o.toString());
        }
        return ret;
    }
}
