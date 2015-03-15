/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author dq
 */
public class CharList extends ArrayList<Character> {

    public CharList(String str) {
        for (int i = 0; i < str.length(); i++) {
            add(str.charAt(i));
        }
    }

    public CharList(char... chars) {
        for (char c : chars) {
            add(c);
        }
    }

    public CharList() {
    }

    public void toUppercase() {
        for (int i = 0; i < size(); i++) {
            Character ch = get(i);
            if (Character.isLowerCase(ch)) {
                set(i, ch.toString().toUpperCase(Locale.ENGLISH).charAt(0));
            }
        }
    }

    public CharList replace(char old, char newChar) {
        CharList ret = new CharList();
        for (int i = 0; i < size(); i++) {
            char _ch = get(i);
            if (_ch == old) {
                ret.add(newChar);
            } else {
                ret.add(_ch);
            }
        }
        return ret;
    }

    public CharSet intersect(char[] chars) {
        CharSet ret = new CharSet();
        for (char c : chars) {
            if (contains(c)) {
                ret.add(c);
            }
        }
        return ret;
    }

    public boolean containsIgnoreCase(Character ch) {
        boolean ret;
        if (Character.isLowerCase(ch)) {
            char _ch = ch.toString().toUpperCase(Locale.ENGLISH).charAt(0);
            ret = contains(_ch) || contains(ch);
        } else if (Character.isUpperCase(ch)) {
            char _ch = ch.toString().toLowerCase(Locale.ENGLISH).charAt(0);
            ret = contains(_ch) || contains(ch);
        } else {
            ret = contains(ch);
        }

        return ret;
    }

    public boolean containsIgnoreCase(Character... chrs) {
        boolean ret = true;
        for (Character ch : chrs) {
            ret = ret && containsIgnoreCase(ch);
            if (!ret) {
                break;
            }
        }
        return ret;
    }
   
    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        Iterator<Character> itr = iterator();
        while (itr.hasNext()) {
            Character c = itr.next();
            ret.append(c);
            if (itr.hasNext()) {
                ret.append(',');
            }
        }
        return ret.toString();
    }
}
