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
public class TER implements Cloneable {

    public final static String RECORD_NAME = "TER ";
    private Integer hibernateId;
    private Set<Element> elements = new HashSet<Element>();

    @Override
    public TER clone() {
        TER ret = new TER();
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
        private Integer serial;
        private String resName;
        private Character chainId;
        private Integer resSeq;
        private Character iCode;

        @Override
        public Element clone() {
            Element ret = new Element();
            ret.setChainId(chainId);
            ret.setResName(resName);
            ret.setResSeq(resSeq);
            ret.setSerial(serial);
            ret.setiCode(iCode);
            return ret;
        }

        public Character getChainId() {
            return chainId;
        }

        public void setChainId(Character chainId) {
            this.chainId = chainId;
        }

        public Integer getHibernateId() {
            return hibernateId;
        }

        public void setHibernateId(Integer hibernateId) {
            this.hibernateId = hibernateId;
        }

        public Character getiCode() {
            return iCode;
        }

        public void setiCode(Character iCode) {
            this.iCode = iCode;
        }

        public String getResName() {
            return resName;
        }

        public void setResName(String resName) {
            this.resName = resName;
        }

        public Integer getResSeq() {
            return resSeq;
        }

        public void setResSeq(Integer resSeq) {
            this.resSeq = resSeq;
        }

        public Integer getSerial() {
            return serial;
        }

        public void setSerial(Integer serial) {
            this.serial = serial;
        }

        /*
         COLUMNS DATA TYPE FIELD DEFINITION
         -------------------------------------------------------------------------
         1 - 6 Record name "TER "
         7 - 11 Integer serial Serial number.
         18 - 20 Residue name resName Residue name.
         22 Character chainID Chain identifier.
         23 - 26 Integer resSeq Residue sequence number.
         27 AChar iCode Insertion code.     
         */
        public void parse(String line) {
            serial = StrUtil.substring(line, 7, 11, Integer.class, false, false);
            resName = StrUtil.substring(line, 18, 20, String.class, false, false);
            chainId = StrUtil.substring(line, 22, 22, Character.class, false, false);
            resSeq = StrUtil.substring(line, 23, 26, Integer.class, false, false);
            iCode = StrUtil.substring(line, 27, 27, Character.class, false, false);
        }

        @Override
        public String toString() {
            StringBuilder ret = new StringBuilder();
            StrUtil.replace(ret, RECORD_NAME.trim(), 1, 6, true);
            StrUtil.replace(ret, serial, 7, 11, true);
            StrUtil.replace(ret, resName, 18, 20, true);
            StrUtil.replace(ret, chainId, 22, 22, true);
            StrUtil.replace(ret, resSeq, 23, 26, true);
            StrUtil.replace(ret, iCode, 27, 27, true);
            StrUtil.replace(ret, '\n', 81, 81, true);
            return ret.toString();
        }
    }

    public static class Sorter implements Comparator<Element> {

        @Override
        public int compare(Element o1, Element o2) {
            int ret = 0;
            return ret;
        }
    }
}
