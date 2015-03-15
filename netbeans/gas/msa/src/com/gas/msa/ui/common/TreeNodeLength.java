/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui.common;

import com.gas.common.ui.util.MathUtil;
import com.gas.common.ui.util.ReflectHelper;
import com.gas.common.ui.util.StrUtil;
import com.gas.domain.core.misc.api.NewickLength;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dq
 */
public class TreeNodeLength {

    private Float length;
    private Float lengthMedian;
    private Float lengthMean;
    private HPD hpd;
    private final static String ATT_LENGTH = "length";
    private final static String ATT_LENGTH_MEAN = "lengthMean";
    private final static String ATT_LENGTH_MEDIAN = "lengthMedian";
    private final static String[] attributeNames = {ATT_LENGTH, ATT_LENGTH_MEAN, ATT_LENGTH_MEDIAN};

    public TreeNodeLength() {
    }

    public TreeNodeLength(NewickLength newickLength) {
        this.length = newickLength.getLength();
        this.lengthMean = newickLength.getLengthMean();
        this.lengthMedian = newickLength.getLengthMedian();
        if (newickLength.getHpd() != null) {
            this.hpd = new HPD(newickLength.getHpd().getFrom(), newickLength.getHpd().getTo());
        }
    }

    public String[] getAttributeNames() {
        return attributeNames;
    }

    public Object getAttribute(String name) {
        if (name.equals(ATT_LENGTH_MEDIAN)) {
            return getLengthMedian();
        }else if(name.equals(ATT_LENGTH)){
            return getLength();
        }else if(name.equals(ATT_LENGTH_MEAN)){
            return getLengthMean();
        }else{
            throw new IllegalArgumentException(String.format("attribute %s not recognized", name));
        }       
    }

    public Float getLengthMean() {
        return lengthMean;
    }

    public void setLengthMean(Float lengthMean) {
        this.lengthMean = lengthMean;
    }

    public Float getLengthMedian() {
        return lengthMedian;
    }

    public void setLengthMedian(Float lengthMedian) {
        this.lengthMedian = lengthMedian;
    }

    public Float getLength() {
        return length;
    }

    public void setLength(Float length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return length == null ? "" : length.toString();
    }

    protected static class HPD {

        private Float from;
        private Float to;

        public HPD(Float from, Float to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public String toString() {
            StringBuilder ret = new StringBuilder();
            ret.append(from);
            ret.append(',');
            ret.append(to);
            return ret.toString();
        }

        public String toString(int sigDigits) {
            StringBuilder ret = new StringBuilder();
            ret.append(MathUtil.toString(from, sigDigits));
            ret.append(',');
            ret.append(MathUtil.toString(to, sigDigits));
            return ret.toString();
        }
    }
}
