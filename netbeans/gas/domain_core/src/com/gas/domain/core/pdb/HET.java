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
public class HET implements Cloneable {

    public final static String RECORD_NAME = "HET ";
    private Integer hibernateId;
    private Set<Element> elements = new HashSet<Element>();

    @Override
    public HET clone() {
        HET ret = new HET();
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

        private int hibernateId;
        private String hetId;
        private Character chainId;
        private Integer seqNum;
        private Character iCode;
        private Integer numHetAtoms;
        private String text;

        @Override
        public Element clone() {
            Element ret = new Element();
            ret.setChainId(chainId);
            ret.setHetId(hetId);
            ret.setNumHetAtoms(numHetAtoms);
            ret.setSeqNum(seqNum);
            ret.setText(text);
            ret.setiCode(iCode);
            return ret;
        }

        public Character getChainId() {
            return chainId;
        }

        public void setChainId(Character chainId) {
            this.chainId = chainId;
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

        public Character getiCode() {
            return iCode;
        }

        public void setiCode(Character iCode) {
            this.iCode = iCode;
        }

        public Integer getNumHetAtoms() {
            return numHetAtoms;
        }

        public void setNumHetAtoms(Integer numHetAtoms) {
            this.numHetAtoms = numHetAtoms;
        }

        public Integer getSeqNum() {
            return seqNum;
        }

        public void setSeqNum(Integer seqNum) {
            this.seqNum = seqNum;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public void parse(String line) {
            hetId = StrUtil.substring(line, 8, 10, String.class, false, false);
            chainId = StrUtil.substring(line, 13, 13, Character.class, false, false);
            seqNum = StrUtil.substring(line, 14, 17, Integer.class, false, false);
            iCode = StrUtil.substring(line, 18, 18, Character.class, false, false);
            numHetAtoms = StrUtil.substring(line, 21, 25, Integer.class, false, false);
            text = StrUtil.substring(line, 31, 70, String.class, false, false);
        }

        /*
         * COLUMNS DATA TYPE FIELD DEFINITION
         ---------------------------------------------------------------------------------
         1 - 6 Record name "HET "
         8 - 10 LString(3) hetID Het identifier, right-justified.
         13 Character ChainID Chain identifier.
         14 - 17 Integer seqNum Sequence number.
         18 AChar iCode Insertion code.
         21 - 25 Integer numHetAtoms Number of HETATM records for the group
         present in the entry.
         31 - 70 String text Text describing Het group.
         */
        @Override
        public String toString() {
            StringBuilder ret = new StringBuilder();
            StrUtil.replace(ret, RECORD_NAME, 1, 6, true);
            StrUtil.replace(ret, hetId, 8, 10, true);
            StrUtil.replace(ret, chainId, 13, 13, true);
            StrUtil.replace(ret, seqNum, 14, 17, false);
            StrUtil.replace(ret, iCode, 18, 18, true);
            StrUtil.replace(ret, numHetAtoms, 21, 25, true);
            StrUtil.replace(ret, text, 31, 70, true);
            StrUtil.replace(ret, '\n', 81, 81, true);
            return ret.toString();
        }
    }

    public static class Sorter implements Comparator<Element> {

        @Override
        public int compare(Element o1, Element o2) {
            int ret = 0;
            String h1 = o1.getHetId();
            String h2 = o2.getHetId();
            ret = h1.compareTo(h2);
            if (ret == 0) {
                Character c1 = o1.getChainId();
                Character c2 = o2.getChainId();
                ret = c1.compareTo(c2);
                if (ret == 0) {
                }
            }
            return ret;
        }
    }
}
