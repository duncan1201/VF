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
public class CONECT implements Cloneable {

    public static final String RECORD_NAME = "CONECT ";
    private Integer hibernateId;
    private Set<Element> elements = new HashSet<Element>();

    public CONECT() {
    }

    @Override
    public CONECT clone() {
        CONECT ret = new CONECT();
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

        private Integer hibernateId;
        private Integer serial;
        private String bondedSerials;

        public Element() {
        }

        @Override
        public Element clone() {
            Element ret = new Element();
            ret.setBondedSerials(bondedSerials);
            ret.setSerial(serial);
            return ret;
        }

        public Integer getHibernateId() {
            return hibernateId;
        }

        public void setHibernateId(Integer hibernateId) {
            this.hibernateId = hibernateId;
        }

        public String getBondedSerials() {
            return bondedSerials;
        }

        public void setBondedSerials(String bondedSerials) {
            this.bondedSerials = bondedSerials;
        }

        public Integer getSerial() {
            return serial;
        }

        public void setSerial(Integer serial) {
            this.serial = serial;
        }

        /*
         Record Format
         COLUMNS DATA TYPE FIELD DEFINITION
         -------------------------------------------------------------------------
         1 - 6 Record name "CONECT"
         7 - 11 Integer serial Atom serial number
         12 - 16 Integer serial Serial number of bonded atom
         17 - 21 Integer serial Serial number of bonded atom
         22 - 26 Integer serial Serial number of bonded atom
         27 - 31 Integer serial Serial number of bonded atom     
         */
        public void parse(String line) {
            serial = StrUtil.substring(line, 7, 11, Integer.class, false, false);
            bondedSerials = StrUtil.substring(line, 12, 31, String.class, false, false);
        }

        @Override
        public String toString() {
            StringBuilder ret = new StringBuilder();
            StrUtil.replace(ret, RECORD_NAME.trim(), 1, 6, true);
            StrUtil.replace(ret, serial, 7, 11, false);
            StrUtil.replace(ret, bondedSerials, 12, 31, false);
            StrUtil.replace(ret, '\n', 81, 81, false);
            return ret.toString();
        }
    }

    public static class Sorter implements Comparator<Element> {

        @Override
        public int compare(Element o1, Element o2) {
            int ret = 0;
            Integer s1 = o1.getSerial();
            Integer s2 = o2.getSerial();
            ret = s1.compareTo(s2);
            return ret;
        }
    }
}
