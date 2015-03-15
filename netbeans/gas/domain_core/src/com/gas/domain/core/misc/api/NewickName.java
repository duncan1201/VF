/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.misc.api;

import com.gas.common.ui.core.IntList;
import com.gas.common.ui.util.ReflectHelper;
import com.gas.common.ui.util.StrUtil;
import com.gas.domain.core.misc.NewickIOUtil;
import com.gas.domain.core.misc.NewickIOUtil.LocInfoList;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author dq
 */
public class NewickName {

    private String name = "";
    private Float prob;
    private Float probStddev;
    private Integer rgb = Color.BLACK.getRGB();
    private static final String ATT_NAME = "name";
    private static final String ATT_PROB = "prob";
    private static final String ATT_RGB = "rgb";
    private static final String ATT_PROB_STD_DEV = "probStddev";

    public NewickName() {
    }

    /*
     * 1[&prob=1.0,prob_stddev=0.0,prob_range={1.0,1.0},prob(percent)="100",prob+-sd="100+-0"] or 
     * [attributes only]
     */
    public NewickName(String str) {

        NewickIOUtil.LocInfoList subList = NewickIOUtil.createLocInfoList(str);
        if (subList.isEmpty()) {
            name = str;
        } else {
            name = str.substring(0, subList.get(0).getOpening());
            String attributesStr = str.substring(subList.get(0).getOpening() + 1, subList.get(0).getClosing());
            parseNameAttributes(attributesStr);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getProb() {
        return prob;
    }

    public void setProb(Float prob) {
        this.prob = prob;
    }

    public Float getProbStddev() {
        return probStddev;
    }

    public Integer getRgb() {
        return rgb;
    }

    public void setRgb(Integer rgb) {
        this.rgb = rgb;
    }

    public void setProbStddev(Float probStddev) {
        this.probStddev = probStddev;
    }

    public List<String> getAttributeNames() {
        List<String> ret = new ArrayList<String>();
        ret.add(ATT_NAME);
        if (prob != null) {
            ret.add(ATT_PROB);
        }
        if (probStddev != null) {
            ret.add(ATT_PROB_STD_DEV);
        }
        if (rgb != null) {
            ret.add(ATT_RGB);
        }
        return ret;
    }

    public Object getAttribute(String name) {
        if (name.equals(ATT_NAME)) {
            return getName();
        } else if (name.equals(ATT_PROB)) {
            return getProb();
        } else if (name.equals(ATT_PROB_STD_DEV)) {
            return getProbStddev();
        } else if (name.equals(ATT_RGB)) {
            return getRgb();
        } else {
            throw new IllegalArgumentException(String.format("attribute %s not found", name));
        }
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append(name);
        List<String> attNames = getAttributeNames();
        attNames.remove("name");
        Iterator<String> itr = attNames.iterator();
        if (attNames.size() > 0) {
            ret.append('[');
        }
        while (itr.hasNext()) {
            final String attName = itr.next();
            Object att = getAttribute(attName);
            ret.append(attName);
            ret.append('=');
            ret.append(att);
            if (itr.hasNext()) {
                ret.append(',');
            }
        }
        if (attNames.size() > 0) {
            ret.append(']');
        }
        return ret.toString();
    }

    /*
     * &prob=1.000000000000000e+000,prob_stddev=0.000000000000000e+000,prob_range={1.000000000000000e+000,1.000000000000000e+000},prob(percent)="100",prob+-sd="100+-0"
     */
    private void parseNameAttributes(String str) {
        List<String> pairs = getKeyValuePairs(str);        

        for (String pair : pairs) {
            String[] splits = pair.split("=");
            if (splits[0].endsWith(ATT_PROB)) {
                setProb(Float.parseFloat(splits[1]));
            } else if (splits[0].endsWith(ATT_PROB_STD_DEV)) {
                setProbStddev(Float.parseFloat(splits[1]));
            } else if(splits[0].endsWith(ATT_RGB)){
                setRgb(Integer.parseInt(splits[1]));
            }
        }
    }
    
    private List<String> getKeyValuePairs(String str){
        LocInfoList locInfoList = NewickIOUtil.createLocInfoList(str);
        IntList commaList = StrUtil.getIndices(str, ',');
        int preIndex = 0;
        Integer index = null;
        List<String> ret = new ArrayList<String>();
        if (!commaList.isEmpty()) {
            for (int i = 0; i < commaList.size(); i++) {
                int commaIndex = commaList.get(i);
                if (!locInfoList.contains(commaIndex)) {
                    index = commaIndex;
                    if (preIndex != 0) {
                        preIndex++;
                    }
                    String nameAttStr = str.substring(preIndex, index);
                    ret.add(nameAttStr);
                    preIndex = index;
                }
            }
            if (index != null) {
                String nameAttStr = str.substring(index + 1);
                ret.add(nameAttStr);
            }
        } else {
            int equalIndex = str.indexOf("=");
            if(equalIndex > -1){
                ret.add(str);
            }
        }
        return ret;
    }
}
