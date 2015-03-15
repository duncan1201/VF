/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.matrix.api;

import com.gas.common.ui.core.StringList;
import java.util.ArrayList;

/**
 *
 * @author dq
 */
public class MatrixList extends ArrayList<Matrix> {

    public StringList getNames() {
        StringList ret = new StringList();
        for (Matrix m : this) {
            ret.add(m.getName());
        }
        return ret;
    }

    public Matrix getMatrix(String name) {
        return getMatrix(name, null);
    }

    public Matrix getMatrix(String name, Boolean dna) {
        Matrix ret = null;
        for (Matrix m : this) {
            if (dna != null) {
                if (m.getName().equalsIgnoreCase(name) && m.isDna() == dna.booleanValue()) {
                    ret = m;
                    break;
                }
            } else {
                if (m.getName().equalsIgnoreCase(name)) {
                    ret = m;
                    break;
                }
            }
        }
        return ret;
    }

    public MatrixList getDnaMatrices() {
        MatrixList ret = new MatrixList();
        for (Matrix m : this) {
            if (m.isDna()) {
                ret.add(m);
            }
        }
        return ret;
    }

    public MatrixList getProteinMatrices() {
        MatrixList ret = new MatrixList();
        for (Matrix m : this) {
            if (!m.isDna()) {
                ret.add(m);
            }
        }
        return ret;
    }
}
