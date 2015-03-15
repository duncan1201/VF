/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.as;

import com.gas.common.ui.core.StringList;
import com.gas.common.ui.util.CommonUtil;
import com.gas.common.ui.util.StrUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author dq
 */
public class StructuredComment implements Cloneable {
    
    public static final String START_REG = "##(.+)-START##";
    public static final String END_REG = "##(.+)-END##";
    public static String REG = START_REG + "|" + END_REG;
    private Map<String, String> data = new HashMap<String, String>();
    private String name;
    private Integer hibernateId;

    public StructuredComment() {
    }

    public StructuredComment(String str) {
        name = StrUtil.extract(START_REG, str);
        str = str.replaceAll(REG, "");

        StringList lines = StrUtil.readLine(str);
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }
            List<String> tokens = StrUtil.tokenize(line, "::");
            if (tokens.size() == 2) {
                data.put(tokens.get(0).trim(), tokens.get(1).trim());
            }
        }
    }

    @Override
    public StructuredComment clone() {
        StructuredComment ret = CommonUtil.cloneSimple(this);
        ret.setData(CommonUtil.copyOf(data));
        return ret;
    }
    
    public void touchAll(){
        Iterator<String> itr = data.keySet().iterator();
        while(itr.hasNext()){
            itr.next();
        }
    }

    public Integer getHibernateId() {
        return hibernateId;
    }

    public void setHibernateId(Integer hibernateId) {
        this.hibernateId = hibernateId;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append(String.format("##%s-START##", name));
        Set<String> keys = data.keySet();
        List<String> keyList = new ArrayList<String>(keys);
        Collections.sort(keyList);
        if (!keyList.isEmpty()) {
            ret.append('\n');
        }
        for (String k : keyList) {
            ret.append(k);
            ret.append('\t');
            ret.append("::");
            ret.append(data.get(k));
            ret.append('\n');
        }
        ret.append(String.format("##%s-END##", name));
        return ret.toString();
    }

    public static String removeStructuredComments(String commentStr) {

        List<String> structures = getStrings(commentStr);
        for (String structure : structures) {
            commentStr = StrUtil.replaceAll(commentStr, structure, "");
        }
        return commentStr;
    }

    public static List<StructuredComment> parse(String commentStr) {
        List<StructuredComment> ret = new ArrayList<StructuredComment>();
        List<String> structures = getStrings(commentStr);
        for (String str : structures) {
            StructuredComment sc = new StructuredComment(str);
            ret.add(sc);
        }
        return ret;
    }

    public static List<String> getStrings(String commentStr) {
        List<String> ret = new ArrayList<String>();

        int[] indices = StrUtil.indexOfReg(REG, commentStr);
        if (indices.length > 0 && indices.length % 4 == 0) {
            for (int i = 0; i < indices.length; i += 4) {
                String str = commentStr.substring(indices[i], indices[i + 3]);
                ret.add(str);
            }
        }
        return ret;
    }

    public static String toString(Collection<StructuredComment> scs) {
        StringBuilder ret = new StringBuilder();
        Iterator<StructuredComment> itr = scs.iterator();
        while (itr.hasNext()) {
            StructuredComment sc = itr.next();
            ret.append(sc.toString());
            if (itr.hasNext()) {
                ret.append('\n');
            }
        }
        return ret.toString();
    }
}
