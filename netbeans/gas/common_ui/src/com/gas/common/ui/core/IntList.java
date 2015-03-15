package com.gas.common.ui.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IntList extends ArrayList<Integer> {

    private String name;
    private Integer min;
    private Integer max;
    private boolean autoStatistics = true;

    public IntList() {
    }
    
    public IntList(List<Integer> ints){
        for(Integer i: ints){
            add(i);
        }
    }

    public IntList(String[] ints) {
        for (String i : ints) {
            add(Integer.parseInt(i));
        }
    }

    public IntList(Integer[] ints) {
        for (Integer i : ints) {
            add(i);
        }
    }

    public IntList(int[] ints) {
        for (Integer i : ints) {
            add(i);
        }
    }

    public boolean isAutoStatistics() {
        return autoStatistics;
    }

    public void setAutoStatistics(boolean autoStatistics) {
        this.autoStatistics = autoStatistics;
    }

    @Override
    public void clear() {
        super.clear();
        min = max = null;
    }

    public void removeLast() {
        if (isEmpty()) {
            return;
        } else {
            remove(size() - 1);
        }
    }

    public String getName() {
        return name;
    }

    public void add(int[] ints) {
        for (int i : ints) {
            add(i);
        }
    }

    @Override
    public boolean add(Integer a) {
        boolean ret = super.add(a);
        if (min == null || min > a) {
            min = a;
        }
        if (max == null || max < a) {
            max = a;
        }
        return ret;
    }

    @Override
    public Integer remove(int i) {
        Integer ret = super.remove(i);
        if (autoStatistics) {
            recalculateMinMax();
        }
        return ret;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer first() {
        Integer ret = null;
        if (!isEmpty()) {
            ret = get(0);
        }
        return ret;
    }

    public Integer secondToLast() {
        Integer ret = null;
        if (size() > 1) {
            ret = get(size() - 2);
        }
        return ret;
    }

    public Integer last() {
        Integer ret = null;
        if (!isEmpty()) {
            ret = get(size() - 1);
        }
        return ret;
    }

    public Integer getMax() {
        return max;
    }

    public Integer getMin() {
        return min;
    }

    protected void recalculateMinMax() {
        Iterator<Integer> itr = iterator();
        while (itr.hasNext()) {
            Integer i = itr.next();
            if (min == null || min > i) {
                min = i;
            }
            if (max == null || max < i) {
                max = i;
            }
        }
    }
    
    public int[] toPrimitiveArray(){
        int[] ret = new int[size()];
        for(int i = 0; i < size(); i++){
            ret[i] = get(i);
        }
        return ret;
    }

    public String toString(String delimiter) {
        StringBuilder ret = new StringBuilder();
        Iterator<Integer> itr = iterator();
        while (itr.hasNext()) {
            Integer i = itr.next();
            ret.append(i.toString());
            if (itr.hasNext()) {
                ret.append(delimiter);
            }
        }
        return ret.toString();
    }
}
