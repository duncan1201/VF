/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.pdb;

import com.gas.common.ui.util.CommonUtil;
import com.gas.common.ui.util.StrUtil;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author dq
 */
public class FORMUL implements Cloneable {

    public static final String RECORD_NAME = "FORMUL ";
    private Integer hibernateId;
    private Set<Element> elements = new HashSet<Element>();

    @Override
    public FORMUL clone() {
        FORMUL ret = new FORMUL();
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
        for (Element e : eles) {
            String str = e.toString();
            ret.append(str);
        }
        return ret.toString();
    }

    public static class Element implements Cloneable {

        private Integer hibernateId;
        private Integer compNum;
        private String hetId;
        private Integer continuation;
        private Character asterisk;
        private String text;

        @Override
        public Element clone() {
            Element ret = new Element();
            ret.setAsterisk(asterisk);
            ret.setCompNum(compNum);
            ret.setContinuation(continuation);
            ret.setHetId(hetId);
            ret.setText(text);
            return ret;
        }

        public Character getAsterisk() {
            return asterisk;
        }

        public void setAsterisk(Character asterisk) {
            this.asterisk = asterisk;
        }

        public Integer getCompNum() {
            return compNum;
        }

        public void setCompNum(Integer compNum) {
            this.compNum = compNum;
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

        public Integer getHibernateId() {
            return hibernateId;
        }

        public void setHibernateId(Integer hibernateId) {
            this.hibernateId = hibernateId;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public void parse(String line) {
            compNum = StrUtil.substring(line, 9, 10, Integer.class, false, false);
            hetId = StrUtil.substring(line, 13, 15, String.class, false, false);
            continuation = StrUtil.substring(line, 17, 18, Integer.class, false, false);
            asterisk = StrUtil.substring(line, 19, 19, Character.class, false, false);
            text = StrUtil.substring(line, 20, 70, String.class, false, false);
        }

        /*
         Record Format
         COLUMNS DATA TYPE FIELD DEFINITION
         -----------------------------------------------------------------------
         1 - 6 Record name "FORMUL"
         9 - 10 Integer compNum Component number.
         13 - 15 LString(3) hetID Het identifier.
         17 - 18 Integer continuation Continuation number.
         19 Character asterisk "*" for water.
         20 - 70 String text Chemical formula.         
         */
        @Override
        public String toString() {
            StringBuilder ret = new StringBuilder();
            StrUtil.replace(ret, RECORD_NAME.trim(), 1, 6, true);
            StrUtil.replace(ret, compNum, 9, 10, true);
            StrUtil.replace(ret, hetId, 13, 15, true);
            StrUtil.replace(ret, continuation, 17, 18, true);
            StrUtil.replace(ret, asterisk, 19, 19, true);
            StrUtil.replace(ret, text, 20, 70, true);
            StrUtil.replace(ret, '\n', 81, 81, true);
            return ret.toString();
        }
    }

    public static class Sorter implements Comparator<Element> {

        @Override
        public int compare(Element o1, Element o2) {
            int ret = 0;
            Integer c1 = o1.getCompNum();
            Integer c2 = o2.getCompNum();

            return ret;
        }
    }
}
