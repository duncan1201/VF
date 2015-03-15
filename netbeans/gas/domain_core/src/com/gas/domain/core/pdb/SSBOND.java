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
public class SSBOND implements Cloneable {

    public final static String RECORD_NAME = "SSBOND ";
    private Integer hibernateId;
    private Set<Element> elements = new HashSet<Element>();

    @Override
    public SSBOND clone() {
        SSBOND ret = new SSBOND();
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
        private Integer serNum;
        private String resName1;
        private Character chainId1;
        private Integer seqNum1;
        private Character icode1;
        private String resName2;
        private Character chainId2;
        private Integer seqNum2;
        private Character icode2;
        private String sym1;
        private String sym2;
        private Float length;

        @Override
        public Element clone() {
            Element ret = new Element();
            ret.setChainId1(chainId1);
            ret.setChainId2(chainId2);
            ret.setIcode1(icode1);
            ret.setIcode2(icode2);
            ret.setLength(length);
            ret.setResName1(resName1);
            ret.setResName2(resName2);
            ret.setSeqNum1(seqNum1);
            ret.setSeqNum2(seqNum2);
            ret.setSerNum(serNum);
            ret.setSym1(sym1);
            ret.setSym2(sym2);
            return ret;
        }

        public Character getChainId1() {
            return chainId1;
        }

        public void setChainId1(Character chainId1) {
            this.chainId1 = chainId1;
        }

        public Character getChainId2() {
            return chainId2;
        }

        public void setChainId2(Character chainId2) {
            this.chainId2 = chainId2;
        }

        public Integer getHibernateId() {
            return hibernateId;
        }

        public void setHibernateId(Integer hibernateId) {
            this.hibernateId = hibernateId;
        }

        public Character getIcode1() {
            return icode1;
        }

        public void setIcode1(Character icode1) {
            this.icode1 = icode1;
        }

        public Character getIcode2() {
            return icode2;
        }

        public void setIcode2(Character icode2) {
            this.icode2 = icode2;
        }

        public Float getLength() {
            return length;
        }

        public void setLength(Float length) {
            this.length = length;
        }

        public String getResName1() {
            return resName1;
        }

        public void setResName1(String resName1) {
            this.resName1 = resName1;
        }

        public String getResName2() {
            return resName2;
        }

        public void setResName2(String resName2) {
            this.resName2 = resName2;
        }

        public Integer getSeqNum1() {
            return seqNum1;
        }

        public void setSeqNum1(Integer seqNum1) {
            this.seqNum1 = seqNum1;
        }

        public Integer getSeqNum2() {
            return seqNum2;
        }

        public void setSeqNum2(Integer seqNum2) {
            this.seqNum2 = seqNum2;
        }

        public Integer getSerNum() {
            return serNum;
        }

        public void setSerNum(Integer serNum) {
            this.serNum = serNum;
        }

        public String getSym1() {
            return sym1;
        }

        public void setSym1(String sym1) {
            this.sym1 = sym1;
        }

        public String getSym2() {
            return sym2;
        }

        public void setSym2(String sym2) {
            this.sym2 = sym2;
        }

        /*
         1 - 6 Record name "SSBOND"         
         8 - 10 Integer serNum Serial number.
         12 - 14 LString(3) "CYS" Residue name.
         16 Character chainID1 Chain identifier.
         18 - 21 Integer seqNum1 Residue sequence number.
         22 AChar icode1 Insertion code.
         26 - 28 LString(3) "CYS" Residue name.
         30 Character chainID2 Chain identifier.
         32 - 35 Integer seqNum2 Residue sequence number.
         36 AChar icode2 Insertion code.
         60 - 65 SymOP sym1 Symmetry operator for residue 1.
         67 - 72 SymOP sym2 Symmetry operator for residue 2.
         74 – 78 Real(5.2) Length Disulfide bond distance         
         */
        @Override
        public String toString() {
            StringBuilder ret = new StringBuilder();
            StrUtil.replace(ret, RECORD_NAME.trim(), 1, 6, true);
            StrUtil.replace(ret, serNum, 8, 10, true);
            StrUtil.replace(ret, resName1, 12, 14, true);
            StrUtil.replace(ret, chainId1, 16, 16, true);
            StrUtil.replace(ret, seqNum1, 18, 21, true);
            StrUtil.replace(ret, icode1, 22, 22, true);
            StrUtil.replace(ret, resName2, 26, 28, true);
            StrUtil.replace(ret, chainId2, 30, 30, true);
            StrUtil.replace(ret, seqNum2, 32, 35, true);
            StrUtil.replace(ret, icode2, 36, 36, true);
            StrUtil.replace(ret, sym1, 60, 65, true);
            StrUtil.replace(ret, sym2, 67, 72, true);
            StrUtil.replace(ret, length, 74, 78, true);
            StrUtil.replace(ret, '\n', 81, 81, true);
            return ret.toString();
        }

        public void parse(String line) {
            serNum = StrUtil.substring(line, 8, 10, Integer.class, false, false);
            resName1 = StrUtil.substring(line, 12, 14, String.class, false, false);
            chainId1 = StrUtil.substring(line, 16, 16, Character.class, false, false);
            seqNum1 = StrUtil.substring(line, 18, 21, Integer.class, false, false);
            icode1 = StrUtil.substring(line, 22, 22, Character.class, false, false);
            chainId2 = StrUtil.substring(line, 30, 30, Character.class, false, false);
            seqNum2 = StrUtil.substring(line, 32, 35, Integer.class, false, false);
            icode2 = StrUtil.substring(line, 36, 36, Character.class, false, false);
            sym1 = StrUtil.substring(line, 60, 65, String.class, false, false);
            sym2 = StrUtil.substring(line, 67, 72, String.class, false, false);
            length = StrUtil.substring(line, 67, 72, Float.class, false, false);
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
