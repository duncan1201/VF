/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.misc.api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author dq
 */
public class NewickLength {

    private Float length;
    private HPD hpd;
    private Float lengthMean;
    private Float lengthMedian;

    public NewickLength(){
        length = 0f;
    }
    
    public List<String> getAttributeNames() {
        List<String> ret = new ArrayList<String>();
        if (length != null) {
            ret.add("length");
        }
        if (hpd != null) {
            ret.add("HPD");
        }
        if (lengthMean != null) {
            ret.add("lengthMean");
        }
        if (lengthMedian != null) {
            ret.add("lengthMedian");
        }
        return ret;
    }

    public HPD getHpd() {
        return hpd;
    }

    public void setHpd(HPD hpd) {
        this.hpd = hpd;
    }

    /*
     * {3.209814417211061e-001,6.127540614698049e-001}
     */
    public void setHpd(String str) {
        this.hpd = new HPD(str);
    }

    public Float getLength() {
        return length;
    }

    public void setLength(Float length) {
        this.length = length;
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

    public static class HPD {

        private Float from;
        private Float to;

        /*
         * {3.209814417211061e-001,6.127540614698049e-001}
         */
        public HPD(String str) {
            str = str.substring(1, str.length() - 1);
            int index = str.indexOf(',');
            String fromStr = str.substring(0, index);
            String toStr = str.substring(index + 1);
            from = Float.parseFloat(fromStr);
            to = Float.parseFloat(toStr);
        }

        public Float getFrom() {
            return from;
        }

        public Float getTo() {
            return to;
        }

        @Override
        public String toString() {
            StringBuilder ret = new StringBuilder();
            ret.append(from);
            ret.append('-');
            ret.append(to);
            return ret.toString();
        }
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        if (length != null) {
            ret.append(length);
        }
        List<String> attNames = getAttributeNames();
        attNames.remove("length");
        if (attNames.size() > 0) {
            ret.append('[');
            Iterator<String> itr = attNames.iterator();
            while (itr.hasNext()) {
                String attName = itr.next();
                if (attName.equals("lengthMean")) {
                    ret.append("length_mean=" + getLengthMean());
                    if (itr.hasNext()) {
                        ret.append(',');
                    }
                } else if (attName.equals("lengthMedian")) {
                    ret.append("length_median=" + getLengthMean());
                    if (itr.hasNext()) {
                        ret.append(',');
                    }
                } else if (attName.equals("HPD")) {
                    ret.append(String.format("length_95%%HPD={%f,%f}", hpd.getFrom(), hpd.getTo()));
                    if (itr.hasNext()) {
                        ret.append(',');
                    }
                }
                ret.append("");
            }
            ret.append(']');
        }

        return ret.toString();
    }
}
