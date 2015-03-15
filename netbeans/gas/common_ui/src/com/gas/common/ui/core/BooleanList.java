/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.core;

import java.util.ArrayList;

/**
 *
 * @author dq
 */
public class BooleanList extends ArrayList<Boolean> {

    private int trueCount;
    private int falseCount;

    public BooleanList() {
    }

    public BooleanList(Boolean b) {
        add(b);
    }

    public int getTrueCount() {
        return trueCount;
    }

    public int getFalseCount() {
        return falseCount;
    }

    @Override
    public final boolean add(Boolean b) {
        boolean ret = super.add(b);
        if (b != null) {
            if (b.booleanValue()) {
                trueCount++;
            } else {
                falseCount++;
            }
        }
        return ret;
    }

    public void removeFirst() {
        if (!isEmpty()) {
            remove(0);
        }
    }

    @Override
    public Boolean remove(int index) {
        Boolean ret = super.remove(index);
        if (ret.booleanValue()) {
            trueCount--;
        } else {
            falseCount--;
        }
        return ret;
    }
}
