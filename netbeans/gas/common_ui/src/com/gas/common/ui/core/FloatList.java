package com.gas.common.ui.core;

import java.util.ArrayList;
import java.util.Collection;

public class FloatList extends ArrayList<Float> {

    private Float max;
    private Float min;
    private boolean autoStatistics = true;

    public FloatList() {
    }

    public FloatList(int[] ints) {
        for (int i : ints) {
            add((float) i);
        }
    }

    public boolean areEquals(Float flt) {
        for (Float f : this) {
            if (!flt.equals(f)) {
                return false;
            }
        }
        return true;
    }

    public void setAll(Float f) {
        for (int i = 0; i < size(); i++) {
            set(i, f);
        }
    }

    public FloatList(Float f) {
        add(f);
    }

    public void setAutoStatistics(boolean autoStatistics) {
        this.autoStatistics = autoStatistics;
    }

    public void scale(float m) {
        boolean old = autoStatistics;
        setAutoStatistics(false);
        for (int i = 0; i < size(); i++) {
            Float s = get(i);
            set(i, s * m);
        }
        reinitMetaData();
        setAutoStatistics(old);
    }

    public void divide(float m) {
        boolean old = autoStatistics;
        setAutoStatistics(false);
        for (int i = 0; i < size(); i++) {
            Float s = get(i);
            set(i, s / m);
        }
        reinitMetaData();
        setAutoStatistics(old);
    }

    public void translate(float m) {
        boolean old = autoStatistics;
        setAutoStatistics(false);
        for (int i = 0; i < size(); i++) {
            Float s = get(i);
            set(i, s + m);
        }
        reinitMetaData();
        setAutoStatistics(old);
    }

    public Float first() {
        Float ret = null;
        if (!isEmpty()) {
            ret = get(0);
        }
        return ret;
    }

    public Float last() {
        Float ret = null;
        if (!isEmpty()) {
            ret = get(size() - 1);
        }
        return ret;
    }

    @Override
    public boolean add(Float f) {
        boolean ret = super.add(f);
        updateMetaData(f, true);
        return ret;
    }

    @Override
    public Float set(int index, Float f) {
        Float ret = super.set(index, f);
        updateMetaData(f, true);
        return ret;
    }

    @Override
    public Float remove(int i) {
        Float ret = super.remove(i);
        if (ret == max || ret == min) {
            reinitMetaData();
        }
        return ret;
    }

    @Override
    public void removeRange(int fromIndex, int toIndex) {
        super.removeRange(fromIndex, toIndex);
        reinitMetaData();
    }

    @Override
    public boolean addAll(Collection c) {
        boolean ret = super.addAll(c);
        for (Object o : c) {
            updateMetaData((Float) o, true);
        }
        return ret;
    }

    public Float secondToLast() {
        Float ret = null;
        if (size() > 1) {
            ret = get(size() - 2);
        }
        return ret;
    }

    public void removeLast() {
        if (!isEmpty()) {
            remove(size() - 1);
        }
    }

    private void updateMetaData(Float f, boolean add) {
        if (f == null || !autoStatistics) {
            return;
        }
        if (add) {
            if (max == null || max < f) {
                max = f;
            }
            if (min == null || min > f) {
                min = f;
            }
        } else {
            if (max.equals(f) || min.equals(f)) {
                reinitMetaData();
            }
        }
    }

    public void reinitMetaData() {
        max = null;
        min = null;
        for (int i = 0; i < size(); i++) {
            Float flt = get(i);
            if (flt == null) {
                continue;
            }
            if (max == null || max < flt) {
                max = get(i);
            }
            if (min == null || min > flt) {
                min = get(i);
            }
        }
    }

    private void resetMetaData() {
        max = null;
        min = null;
    }

    @Override
    public void clear() {
        super.clear();
        resetMetaData();
    }

    public Float getMax() {
        return max;
    }

    public Float getMin() {
        return min;
    }

    @Override
    public Float[] toArray() {
        return toArray(new Float[size()]);
    }

    public int[] toIntArray() {
        int[] ret = new int[size()];
        for (int i = 0; i < size(); i++) {
            ret[i] = Math.round(get(i));
        }
        return ret;
    }

    public float[] toPrimitiveArray() {
        float[] ret = new float[size()];
        for (int i = 0; i < size(); i++) {
            Float f = get(i);
            if (f != null) {
                ret[i] = f;
            }
        }
        return ret;
    }

    public void print() {
    }
}
