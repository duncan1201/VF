/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.entrez.core.ESearch.api;

import com.gas.common.ui.util.StrUtil;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 *
 * @author dunqiang
 */
public class ESearchCmdResult {

    private int count;
    private int retMax;
    private int retStart;
    private List<String> idList = new ArrayList<String>();
    private String queryTranlation;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<String> getIdLists(int listSize) {
        List<String> ret = new ArrayList();
        Iterator<List<String>> itr = getIdListSet(listSize).iterator();
        while(itr.hasNext()){
            List<String> ids = itr.next();
            String concatenated = StrUtil.toString(ids, ",");
            ret.add(concatenated);
        }
        return ret;
    }

    public Set<List<String>> getIdListSet(int listSize) {
        Set<List<String>> ret = new HashSet<List<String>>();
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < idList.size();) {
            list.add(idList.get(i));
            i++;
            if (i % listSize == 0) {
                ret.add(list);
                list = new ArrayList<String>();
            }
        }
        if (list.size() > 0) {
            ret.add(list);
        }
        return ret;
    }

    public <T> T getIdList(Class<T> clazz) {
        T ret = null;
        if (clazz.isAssignableFrom(String.class)) {
            StringBuilder builder = new StringBuilder();
            Iterator<String> itr = idList.iterator();
            while (itr.hasNext()) {
                String id = itr.next();
                builder.append(id);
                if (itr.hasNext()) {
                    builder.append(',');
                }
            }
            ret = (T) builder.toString();
        } else if (clazz.isAssignableFrom(List.class)) {
            ret = (T) idList;
        } else {
            throw new IllegalArgumentException(String.format("Class '%s' not supported", clazz.toString()));
        }
        return ret;
    }

    public void setIdList(List<String> idList) {
        this.idList = idList;
    }

    public String getQueryTranlation() {
        return queryTranlation;
    }

    public void setQueryTranlation(String queryTranlation) {
        this.queryTranlation = queryTranlation;
    }

    public int getRetMax() {
        return retMax;
    }

    public void setRetMax(int retMax) {
        this.retMax = retMax;
    }

    public int getRetStart() {
        return retStart;
    }

    public void setRetStart(int retStart) {
        this.retStart = retStart;
    }
}
