package com.gas.domain.core.as;

import com.gas.common.ui.util.BioUtil;
import com.gas.common.ui.util.StrUtil;
import com.gas.domain.core.ren.REN;
import com.gas.domain.core.ren.RMap;
import java.util.Locale;

public class Siquence implements Cloneable {

    private int hiberhateId;
    private String data;

    protected int getHibernateId() {
        return hiberhateId;
    }

    @Override
    public Siquence clone() {
        Siquence ret = new Siquence();
        ret.setData(data);
        return ret;
    }

    public String getData(int from, int to) {
        return getData(from, to, false);
    }

    /**
     * @param from: 1-based inclusively
     * @param to: 1-based inclusively
     */
    public String getData(int from, int to, boolean reverseComplement) {
        String ret = StrUtil.sub(data, from, to);
        if (reverseComplement) {
            ret = BioUtil.reverseComplement(ret);
        }
        return ret;
    }

    protected void setHibernateId(int hiberhateId) {
        this.hiberhateId = hiberhateId;
    }

    public void remove(int start, int end) {
        String newData = StrUtil.delete(data, start, end);
        setData(newData);
    }

    /**
     * @param start 1-based
     */
    public void replace(int start, int end, String replacement) {
        String newData = StrUtil.replace(data, start, end, replacement);
        setData(newData);
    }

    public void subsiqence(int start, int end) {
        String newData = StrUtil.sub(data, start, end);
        setData(newData);
    }

    public void linearize(RMap.Entry entry) {
        final int[] cutPos = entry.getDownstreamCutPos();
        final int cutType = entry.getDownstreamCutType();
        final int newStart = entry.getStart() + Math.min(cutPos[0], cutPos[1]);
        final int overhangLength = entry.getOverhangLength();
        StringBuilder newData = new StringBuilder();
        newData.append(new String(data.substring(newStart - 1)));
        newData.append(new String(data.substring(0, newStart - 1)));
        if(cutType != REN.BLUNT){
            newData.append(data.substring(newStart - 1, newStart - 1 + overhangLength));
        }
        newData.trimToSize();
        setData(newData.toString());

    }

    /**
     * @param pos: >=1 and <= length + 1; appends if if pos = length + 1
     */
    public void insert(int pos, String bases) {
        if (pos > data.length() + 1 || pos < 1) {
            throw new IllegalArgumentException(String.format("%d out of bound", pos));
        }
        String newBases;
        if (pos == data.length() + 1) { // append
            newBases = data + bases;
        } else {
            newBases = StrUtil.insert(data, pos, bases);
        }
        setData(newBases);
    }

    public String getData() {
        return data;
    }

    protected void setData(String data) {
        this.data = data.toUpperCase(Locale.ENGLISH);
    }
    
    public boolean containsUracil(){        
        if(data == null){
            return false;
        }else{
            return data.indexOf("u") > -1 || data.indexOf("U") > -1;            
        }
    }
}
