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
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 *
 * @author dq
 */
public class TITLE implements Cloneable {

    public final static String RECORD_NAME = "TITLE";
    private Integer hibernateId;
    private Set<Element> elements = new HashSet<Element>();
    private transient String title;

    @Override
    public TITLE clone() {
        TITLE ret = new TITLE();
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
        List<Element> e = new ArrayList<Element>(elements);
        Collections.sort(e, new Sorter());
        Iterator<Element> itr = e.iterator();
        StringBuilder ret = new StringBuilder();
        while (itr.hasNext()) {
            Element ele = itr.next();
            String eleStr = ele.toString();
            ret.append(eleStr);
        }
        return ret.toString();
    }

    public String getTitle() {
        StringBuilder ret = new StringBuilder();
        if (title == null) {
            List<Element> e = new ArrayList<Element>(elements);
            Collections.sort(e, new Sorter());
            Iterator<Element> itr = e.iterator();

            while (itr.hasNext()) {
                Element ele = itr.next();
                String eleStr = ele.getTitle().trim();
                ret.append(eleStr);
                if (itr.hasNext()) {
                    ret.append(' ');
                }
            }
            title = ret.toString();
        }
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        List<Element> e = new ArrayList<Element>(elements);
        Collections.sort(e, new Sorter());
        Iterator<Element> itr = e.iterator();

        if (itr.hasNext()) {
            Element ele = itr.next();
            ele.setTitle(title);
        }
    }

    public static class Element implements Cloneable {

        private Integer hibernateId;
        private Integer continuation;
        private String title;

        @Override
        public Element clone() {
            Element ret = new Element();
            ret.setContinuation(continuation);
            ret.setTitle(title);
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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        @Override
        public String toString() {
            StringBuilder ret = new StringBuilder();
            StrUtil.replace(ret, RECORD_NAME, 1, 6, true);
            StrUtil.replace(ret, continuation, 9, 10, true);
            StrUtil.replace(ret, title, 11, 80, true);
            StrUtil.replace(ret, '\n', 81, 81, true);
            return ret.toString();
        }

        /*
         Record Format
         COLUMNS DATA TYPE FIELD DEFINITION
         ----------------------------------------------------------------------------------
         1 - 6 Record name "TITLE "
         9 - 10 Continuation continuation Allows concatenation of multiple records.
         11 - 80 String title Title of the experiment.         
         */
        public void parse(String line) {
            continuation = StrUtil.substring(line, 9, 10, Integer.class, false, false);
            title = StrUtil.substring(line, 11, 80, String.class, false, false);
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
