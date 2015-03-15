/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.matrix.api;

import com.gas.common.ui.core.CharList;
import com.gas.common.ui.util.MathUtil;
import java.util.Locale;

/**
 *
 * @author dq
 */
public class Matrix {

    String name;
    boolean dna;
    short[] array;
    final static String nucleotides = "ABCDGHKMNRSTUVWXY";
    final static String aminoAcids = "ABCDEFGHIKLMNPQRSTVWXYZ";

    /**
     * @param name: must be unique
     */
    public Matrix(String name, boolean dna, short[] array) {
        this.name = name;
        this.dna = dna;
        this.array = array;
    }

    public String getName() {
        return name;
    }

    public boolean isDna() {
        return dna;
    }

    public Short getScore(Character r1, Character r2) {
        int index1;
        int index2;
        if (isDna()) {
            index1 = nucleotides.indexOf(r1.toString().toUpperCase(Locale.ENGLISH));
            index2 = nucleotides.indexOf(r2.toString().toUpperCase(Locale.ENGLISH));
        } else {
            index1 = aminoAcids.indexOf(r1.toString().toUpperCase(Locale.ENGLISH));
            index2 = aminoAcids.indexOf(r2.toString().toUpperCase(Locale.ENGLISH));
        }
        if (index1 < 0 || index2 < 0) {
            return null;
        }
        final int x = Math.max(index1, index2) + 1;
        final int y = Math.min(index1, index2) + 1;

        return _getScore(x, y);
    }

    /**
     * @param x: 1-based
     * @param y: 1-based
     */
    private Short _getScore(int x, int y) {
        Integer ret;
        if (x == 1) {
            ret = 0;
        } else {
            ret = MathUtil.sumOfNaturalNumbers(x - 1) - 1;
            ret += y;
        }
        return array[ret];
    }

    public int getSize() {
        if (isDna()) {
            return nucleotides.length();
        } else {
            return aminoAcids.length();
        }
    }

    public CharList getResidues() {
        if (isDna()) {
            return new CharList(nucleotides);
        } else {
            return new CharList(aminoAcids);
        }
    }
}
