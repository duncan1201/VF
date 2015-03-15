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
public class HELIX implements Cloneable {

    public static final String RECORD_NAME = "HELIX";
    private Integer hibernateId;
    private Set<Element> elements = new HashSet<Element>();

    @Override
    public HELIX clone() {
        HELIX ret = new HELIX();
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
        private Integer serNum;
        private String helixId;
        private String initResName;
        private Character initChainId;
        private Integer initSeqNum;
        private Character initICode;
        private String endResName;
        private Character endChainId;
        private Integer endSeqNum;
        private Character endICode;
        private Integer helixClass;
        private String comment;
        private Integer length;

        @Override
        public Element clone() {
            Element ret = new Element();
            ret.setComment(comment);
            ret.setEndChainId(endChainId);
            ret.setEndICode(endICode);
            ret.setEndResName(endResName);
            ret.setEndSeqNum(endSeqNum);
            ret.setHelixClass(helixClass);
            ret.setHelixId(helixId);
            ret.setInitChainId(initChainId);
            ret.setInitICode(initICode);
            ret.setInitResName(initResName);
            ret.setInitSeqNum(initSeqNum);
            ret.setLength(length);
            ret.setSerNum(serNum);
            return ret;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public Character getEndChainId() {
            return endChainId;
        }

        public void setEndChainId(Character endChainId) {
            this.endChainId = endChainId;
        }

        public Character getEndICode() {
            return endICode;
        }

        public void setEndICode(Character endICode) {
            this.endICode = endICode;
        }

        public String getEndResName() {
            return endResName;
        }

        public void setEndResName(String endResName) {
            this.endResName = endResName;
        }

        public Integer getEndSeqNum() {
            return endSeqNum;
        }

        public void setEndSeqNum(Integer endSeqNum) {
            this.endSeqNum = endSeqNum;
        }

        public Integer getHelixClass() {
            return helixClass;
        }

        public void setHelixClass(Integer helixClass) {
            this.helixClass = helixClass;
        }

        public String getHelixId() {
            return helixId;
        }

        public void setHelixId(String helixId) {
            this.helixId = helixId;
        }

        public Integer getHibernateId() {
            return hibernateId;
        }

        public void setHibernateId(Integer hibernateId) {
            this.hibernateId = hibernateId;
        }

        public Character getInitChainId() {
            return initChainId;
        }

        public void setInitChainId(Character initChainId) {
            this.initChainId = initChainId;
        }

        public Character getInitICode() {
            return initICode;
        }

        public void setInitICode(Character initICode) {
            this.initICode = initICode;
        }

        public String getInitResName() {
            return initResName;
        }

        public void setInitResName(String initResName) {
            this.initResName = initResName;
        }

        public Integer getInitSeqNum() {
            return initSeqNum;
        }

        public void setInitSeqNum(Integer initSeqNum) {
            this.initSeqNum = initSeqNum;
        }

        public Integer getLength() {
            return length;
        }

        public void setLength(Integer length) {
            this.length = length;
        }

        public Integer getSerNum() {
            return serNum;
        }

        public void setSerNum(Integer serNum) {
            this.serNum = serNum;
        }

        /*
         Record Format
         COLUMNS DATA TYPE FIELD DEFINITION
         -----------------------------------------------------------------------------------
         1 - 6 Record name "HELIX "
         8 - 10 Integer serNum Serial number of the helix. This starts
         at 1 and increases incrementally.
         12 - 14 LString(3) helixID Helix identifier. In addition to a serial
         number, each helix is given an
         alphanumeric character helix identifier.
         16 - 18 Residue name initResName Name of the initial residue.
         20 Character initChainID Chain identifier for the chain containing
         this helix.
         22 - 25 Integer initSeqNum Sequence number of the initial residue.
         26 AChar initICode Insertion code of the initial residue.
         28 - 30 Residue name endResName Name of the terminal residue of the helix.
         32 Character endChainID Chain identifier for the chain containing
         this helix.
         34 - 37 Integer endSeqNum Sequence number of the terminal residue.
         38 AChar endICode Insertion code of the terminal residue.
         39 - 40 Integer helixClass Helix class (see below).
         41 - 70 String comment Comment about this helix.
         72 - 76 Integer length Length of this helix.
         */
        @Override
        public String toString() {
            StringBuilder ret = new StringBuilder();
            StrUtil.replace(ret, RECORD_NAME, 1, 6, true);
            StrUtil.replace(ret, serNum, 8, 10, true);
            StrUtil.replace(ret, helixId, 12, 14, true);
            StrUtil.replace(ret, initResName, 16, 18, true);
            StrUtil.replace(ret, initChainId, 20, 20, true);
            StrUtil.replace(ret, initSeqNum, 22, 25, true);
            StrUtil.replace(ret, initICode, 26, 26, true);
            StrUtil.replace(ret, endResName, 28, 30, true);
            StrUtil.replace(ret, endChainId, 32, 32, true);
            StrUtil.replace(ret, endSeqNum, 34, 37, true);
            StrUtil.replace(ret, endICode, 38, 38, true);
            StrUtil.replace(ret, helixClass, 39, 40, true);
            StrUtil.replace(ret, comment, 41, 70, true);
            StrUtil.replace(ret, length, 72, 76, true);
            StrUtil.replace(ret, '\n', 81, 81, true);
            return ret.toString();
        }

        public void parse(String line) {
            serNum = StrUtil.substring(line, 8, 10, Integer.class, false, false);
            helixId = StrUtil.substring(line, 12, 14, String.class, false, false);
            initResName = StrUtil.substring(line, 16, 18, String.class, false, false);
            initChainId = StrUtil.substring(line, 20, 20, Character.class, false, false);
            initSeqNum = StrUtil.substring(line, 22, 25, Integer.class, false, false);
            initICode = StrUtil.substring(line, 26, 26, Character.class, false, false);
            endResName = StrUtil.substring(line, 28, 30, String.class, false, false);
            endChainId = StrUtil.substring(line, 32, 32, Character.class, false, false);
            endSeqNum = StrUtil.substring(line, 34, 37, Integer.class, false, false);
            endICode = StrUtil.substring(line, 38, 38, Character.class, false, false);
            helixClass = StrUtil.substring(line, 39, 40, Integer.class, false, false);
            comment = StrUtil.substring(line, 41, 70, String.class, false, false);
            length = StrUtil.substring(line, 72, 76, Integer.class, false, false);
        }
    }

    public static class Sorter implements Comparator<Element> {

        @Override
        public int compare(Element o1, Element o2) {
            int ret = 0;
            Integer s1 = o1.getSerNum();
            Integer s2 = o2.getSerNum();
            ret = s1.compareTo(s2);
            return ret;
        }
    }
}
