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
public class HETNAM implements Cloneable {

    public final static String RECORD_NAME = "HETNAM ";
    private Integer hibernateId;
    private Set<Element> elements = new HashSet<Element>();

    @Override
    public HETNAM clone() {
        HETNAM ret = new HETNAM();
        ret.setElements(CommonUtil.copyOf(elements));
        return ret;
    }

    public Integer getHibernateId() {
        return hibernateId;
    }

    public void setHibernateId(Integer hibernateId) {
        this.hibernateId = hibernateId;
    }

    public Set<Element> getElements() {
        return elements;
    }

    public void setElements(Set<Element> elements) {
        this.elements = elements;
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

        private int hibernateId;
        private Integer continuation;
        private String hetId;
        private String text;

        @Override
        public Element clone() {
            Element ret = new Element();
            ret.setContinuation(continuation);
            ret.setHetId(hetId);
            ret.setText(text);
            return ret;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public Integer getContinuation() {
            return continuation;
        }

        public void setContinuation(Integer continuation) {
            this.continuation = continuation;
        }

        public String getHetId() {
            return hetId;
        }

        public void setHetId(String hetId) {
            this.hetId = hetId;
        }

        public int getHibernateId() {
            return hibernateId;
        }

        public void setHibernateId(int hibernateId) {
            this.hibernateId = hibernateId;
        }

        /*
         * Record Format
         COLUMNS DATA TYPE FIELD DEFINITION
         ----------------------------------------------------------------------------
         1 - 6 Record name "HETNAM"
         9 - 10 Continuation continuation Allows concatenation of multiple records.
         12 - 14 LString(3) hetID Het identifier, right-justified.
         16 - 70 String text Chemical name.
         */
        public void parse(String line) {
            continuation = StrUtil.substring(line, 9, 10, Integer.class, false, false);
            hetId = StrUtil.substring(line, 12, 14, String.class, false, false);
            text = StrUtil.substring(line, 16, 70, String.class, false, false);
        }

        @Override
        public String toString() {
            StringBuilder ret = new StringBuilder();
            StrUtil.replace(ret, RECORD_NAME.trim(), 1, 6, true);
            StrUtil.replace(ret, continuation, 9, 10, true);
            StrUtil.replace(ret, hetId, 12, 14, true);
            StrUtil.replace(ret, text, 16, 70, true);
            StrUtil.replace(ret, '\n', 81, 81, true);
            return ret.toString();
        }
    }

    public static class Sorter implements Comparator<HETNAM.Element> {

        @Override
        public int compare(Element o1, Element o2) {
            int ret = 0;
            ret = o1.getHetId().compareTo(o2.getHetId());
            if (ret == 0) {
                Integer c1 = o1.getContinuation();
                Integer c2 = o2.getContinuation();
                if (c1 == null) {
                    ret = -1;
                } else if (c2 == null) {
                    ret = 1;
                } else {
                    ret = c1.compareTo(c2);
                }
            }
            return ret;
        }
    }
}
