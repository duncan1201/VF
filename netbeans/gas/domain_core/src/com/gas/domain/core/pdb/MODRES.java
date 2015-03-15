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
public class MODRES implements Cloneable {

    public final static String RECORD_NAME = "MODRES ";
    private Integer hibernateId;
    private Set<Element> elements = new HashSet<Element>();

    @Override
    public MODRES clone() {
        MODRES ret = new MODRES();
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
        List<Element> eles = new ArrayList<Element>();
        Collections.sort(eles, new Sorter());
        for (Element e : eles) {
            String str = e.toString();
            ret.append(str);
        }
        return ret.toString();
    }

    public static class Element implements Cloneable {

        private Integer hibernateId;
        private Character idCode;
        private String resName;
        private Character chainId;
        private Integer seqNum;
        private Character iCode;
        private String stdRes;
        private String comment;

        @Override
        public Element clone() {
            Element ret = new Element();
            ret.setChainId(chainId);
            ret.setComment(comment);
            ret.setIdCode(idCode);
            ret.setResName(resName);
            ret.setSeqNum(seqNum);
            ret.setStdRes(stdRes);
            ret.setiCode(iCode);
            return ret;
        }

        public Character getChainId() {
            return chainId;
        }

        public void setChainId(Character chainId) {
            this.chainId = chainId;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
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

        public Character getIdCode() {
            return idCode;
        }

        public void setIdCode(Character idCode) {
            this.idCode = idCode;
        }

        public String getResName() {
            return resName;
        }

        public void setResName(String resName) {
            this.resName = resName;
        }

        public Integer getSeqNum() {
            return seqNum;
        }

        public void setSeqNum(Integer seqNum) {
            this.seqNum = seqNum;
        }

        public String getStdRes() {
            return stdRes;
        }

        public void setStdRes(String stdRes) {
            this.stdRes = stdRes;
        }

        /*
         Record Format
         COLUMNS DATA TYPE FIELD DEFINITION
         --------------------------------------------------------------------------------
         1 - 6 Record name "MODRES"
         8 - 11 IDcode idCode ID code of this entry.
         13 - 15 Residue name resName Residue name used in this entry.
         17 Character chainID Chain identifier.
         19 - 22 Integer seqNum Sequence number.
         23 AChar iCode Insertion code.
         25 - 27 Residue name stdRes Standard residue name.
         30 - 70 String comment Description of the residue modification.         
         */
        public void parse(String line) {
            idCode = StrUtil.substring(line, 8, 11, Character.class, false, false);
            resName = StrUtil.substring(line, 13, 15, String.class, false, false);
            chainId = StrUtil.substring(line, 17, 17, Character.class, false, false);
            seqNum = StrUtil.substring(line, 19, 22, Integer.class, false, false);
            iCode = StrUtil.substring(line, 23, 23, Character.class, false, false);
            stdRes = StrUtil.substring(line, 25, 27, String.class, false, false);
            comment = StrUtil.substring(line, 30, 70, String.class, false, false);
        }

        @Override
        public String toString() {
            StringBuilder ret = new StringBuilder();
            StrUtil.replace(ret, RECORD_NAME.trim(), 1, 6, true);
            StrUtil.replace(ret, idCode, 8, 11, true);
            StrUtil.replace(ret, resName, 13, 15, true);
            StrUtil.replace(ret, chainId, 17, 17, true);
            StrUtil.replace(ret, seqNum, 19, 22, true);
            StrUtil.replace(ret, stdRes, 25, 27, true);
            StrUtil.replace(ret, comment, 30, 70, true);
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
