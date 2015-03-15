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
public class SOURCE implements Cloneable {

    public final static String RECORD_NAME = "SOURCE";
    private Integer hibernateId;
    private Set<Element> elements = new HashSet<Element>();

    @Override
    public SOURCE clone() {
        SOURCE ret = new SOURCE();
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
        private String srcName;

        @Override
        public Element clone() {
            Element ret = new Element();
            ret.setContinuation(continuation);
            ret.setSrcName(srcName);
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

        public String getSrcName() {
            return srcName;
        }

        public void setSrcName(String srcName) {
            this.srcName = srcName;
        }
        /*
         Record Format
         COLUMNS DATA TYPE FIELD DEFINITION
         --------------------------------------------------------------------------------------
         1 - 6 Record name "SOURCE"
         8 - 10 Continuation continuation Allows concatenation of multiple records.
         11 - 79 Specification srcName Identifies the source of the
         List macromolecule in a token: value format.         
         */

        public void parse(String line) {
            continuation = StrUtil.substring(line, 8, 10, Integer.class, false, false);
            srcName = StrUtil.substring(line, 11, 79, String.class, false, false);
        }

        @Override
        public String toString() {
            StringBuilder ret = new StringBuilder();
            StrUtil.replace(ret, RECORD_NAME, 1, 6, true);
            StrUtil.replace(ret, continuation, 8, 10, true);
            StrUtil.replace(ret, srcName, 11, 79, true);
            StrUtil.replace(ret, '\n', 81, 81, true);
            return ret.toString();
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
