/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.pdb;

import com.gas.common.ui.util.CommonUtil;
import com.gas.common.ui.util.StrUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author dq
 */
public class KEYWDS implements Cloneable {

    public final static String RECORD_NAME = "KEYWDS ";
    private Integer hibernateId;
    private Set<Element> elements = new HashSet<Element>();

    @Override
    public KEYWDS clone() {
        KEYWDS ret = new KEYWDS();
        ret.setElements(CommonUtil.copyOf(elements));
        return ret;
    }

    public Set<Element> getElements() {
        return elements;
    }

    public void setElements(Set<Element> elements) {
        this.elements = elements;
    }

    public Integer getHibernateId() {
        return hibernateId;
    }

    public void setHibernateId(Integer hibernateId) {
        this.hibernateId = hibernateId;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        List<Element> eles = new ArrayList<Element>(elements);
        Collections.sort(eles, new Sorter());
        for (Element e : eles) {
            String str = e.toString();
            ret.append(str);
        }
        return ret.toString();
    }

    public static class Element implements Cloneable {

        private Integer hibernateId;
        private Integer continuation;
        private String keywds;

        @Override
        public Element clone() {
            Element ret = new Element();
            ret.setContinuation(continuation);
            ret.setKeywds(keywds);
            return ret;
        }

        public Integer getContinuation() {
            return continuation;
        }

        public void setContinuation(Integer continuation) {
            this.continuation = continuation;
        }

        public Integer getHibernateId() {
            return hibernateId;
        }

        public void setHibernateId(Integer hibernateId) {
            this.hibernateId = hibernateId;
        }

        public String getKeywds() {
            return keywds;
        }

        public void setKeywds(String keywds) {
            this.keywds = keywds;
        }

        /*
         * columns  Data TYPE       Field       Definition
         * ------------------------------------------------
         * 1-6      Record name     "KEYWDS"    
         * 9-10     Continuation    
         * 11-79    List            keywds      Comma-separated list of keywords
         */
        @Override
        public String toString() {
            StringBuilder ret = new StringBuilder();
            StrUtil.replace(ret, RECORD_NAME.trim(), 1, 6, true);
            StrUtil.replace(ret, continuation, 9, 10, true);
            StrUtil.replace(ret, keywds, 11, 79, true);
            StrUtil.replace(ret, '\n', 81, 81, true);
            return ret.toString();
        }

        public void parse(String line) {
            continuation = StrUtil.substring(line, 9, 10, Integer.class, false, false);
            keywds = StrUtil.substring(line, 11, 79, String.class, false, false);
        }
    }

    public static class Sorter implements Comparator<Element> {

        @Override
        public int compare(Element o1, Element o2) {
            int ret = 0;
            Integer c1 = o1.getContinuation();
            Integer c2 = o2.getContinuation();
            if (c1 == null) {
                ret = -1;
            } else if (c2 == null) {
                ret = 1;
            } else {
                ret = c1.compareTo(c2);
            }
            return ret;
        }
    }
}
