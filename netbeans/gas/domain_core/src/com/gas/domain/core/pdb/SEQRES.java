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
public class SEQRES implements Cloneable {

    public final static String RECORD_NAME = "SEQRES";
    private Integer hibernateId;
    private Set<Element> elements = new HashSet<Element>();

    @Override
    public SEQRES clone() {
        SEQRES ret = new SEQRES();
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
        for (Element ele : eles) {
            String str = ele.toString();
            ret.append(str);
        }
        return ret.toString();
    }

    public static class Element implements Cloneable {

        private Integer hibernateId;
        private Integer serNum;
        private Character chainId;
        private Integer numRes;
        private String resNames;

        @Override
        public Element clone() {
            Element ret = new Element();
            ret.setChainId(chainId);
            ret.setNumRes(numRes);
            ret.setResNames(resNames);
            ret.setSerNum(serNum);
            return ret;
        }

        public Integer getHibernateId() {
            return hibernateId;
        }

        public void setHibernateId(Integer hibernateId) {
            this.hibernateId = hibernateId;
        }

        public Character getChainId() {
            return chainId;
        }

        public void setChainId(Character chainId) {
            this.chainId = chainId;
        }

        public Integer getNumRes() {
            return numRes;
        }

        public void setNumRes(Integer numRes) {
            this.numRes = numRes;
        }

        public String getResNames() {
            return resNames;
        }

        public void setResNames(String resNames) {
            this.resNames = resNames;
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
         -------------------------------------------------------------------------------------
         1 - 6 Record name "SEQRES"
         8 - 10 Integer serNum Serial number of the SEQRES record for the
         current chain. Starts at 1 and increments
         by one each line. Reset to 1 for each chain.
         12 Character chainID Chain identifier. This may be any single
         legal character, including a blank which is
         is used if there is only one chain.
         14 - 17 Integer numRes Number of residues in the chain.
         This value is repeated on every record.
         20 - 22 Residue name resName Residue name.
         24 - 26 Residue name resName Residue name.
         28 - 30 Residue name resName Residue name.
         32 - 34 Residue name resName Residue name.
         36 - 38 Residue name resName Residue name.
         40 - 42 Residue name resName Residue name.
         44 - 46 Residue name resName Residue name.
         48 - 50 Residue name resName Residue name.
         52 - 54 Residue name resName Residue name.
         56 - 58 Residue name resName Residue name.
         60 - 62 Residue name resName Residue name.
         64 - 66 Residue name resName Residue name.
         68 - 70 Residue name resName Residue name.         
         */
        public void parse(String line) {
            serNum = StrUtil.substring(line, 8, 10, Integer.class, false, false);
            chainId = StrUtil.substring(line, 12, 12, Character.class, false, false);
            numRes = StrUtil.substring(line, 14, 17, Integer.class, false, false);
            resNames = StrUtil.substring(line, 20, 70, String.class, false, false);
        }

        @Override
        public String toString() {
            StringBuilder ret = new StringBuilder();
            StrUtil.replace(ret, RECORD_NAME, 1, 6, true);
            StrUtil.replace(ret, serNum, 8, 10, true);
            StrUtil.replace(ret, chainId, 12, 12, true);
            StrUtil.replace(ret, numRes, 14, 17, true);
            StrUtil.replace(ret, resNames, 20, 70, true);
            StrUtil.replace(ret, '\n', 81, 81, true);
            return ret.toString();
        }
    }

    public static class Sorter implements Comparator<Element> {

        @Override
        public int compare(Element o1, Element o2) {
            int ret = 0;
            Character c1 = o1.getChainId();
            Character c2 = o2.getChainId();
            ret = c1.compareTo(c2);
            if (ret == 0) {
                Integer s1 = o1.getSerNum();
                Integer s2 = o2.getSerNum();
                ret = s1.compareTo(s2);
            }
            return ret;
        }
    }
}
