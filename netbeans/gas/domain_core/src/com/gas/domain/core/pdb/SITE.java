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
public class SITE implements Cloneable {

    public final static String RECORD_NAME = "SITE ";
    private Integer hibernateId;
    private Set<Element> elements = new HashSet<Element>();

    @Override
    public SITE clone() {
        SITE ret = new SITE();
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
        private Integer seqNum;
        private String siteId;
        private Integer numRes;
        private String resName1;
        private Character chainId1;
        private Integer seq1;
        private Character iCode1;
        private String resName2;
        private Character chainId2;
        private Integer seq2;
        private Character iCode2;
        private String resName3;
        private Character chainId3;
        private Integer seq3;
        private Character iCode3;
        private String resName4;
        private Character chainId4;
        private Integer seq4;
        private Character iCode4;

        @Override
        public Element clone() {
            Element ret = new Element();
            ret.setChainId1(chainId1);
            ret.setChainId2(chainId2);
            ret.setChainId3(chainId3);
            ret.setChainId4(chainId4);
            ret.setNumRes(numRes);
            ret.setResName1(resName1);
            ret.setResName2(resName2);
            ret.setResName3(resName3);
            ret.setResName4(resName4);
            ret.setSeq1(seq1);
            ret.setSeq2(seq2);
            ret.setSeq3(seq3);
            ret.setSeq4(seq4);
            ret.setSeqNum(seqNum);
            ret.setSiteId(siteId);
            ret.setiCode1(iCode1);
            ret.setiCode2(iCode2);
            ret.setiCode3(iCode3);
            ret.setiCode4(iCode4);
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

        public Character getChainId3() {
            return chainId3;
        }

        public void setChainId3(Character chainId3) {
            this.chainId3 = chainId3;
        }

        public Character getChainId4() {
            return chainId4;
        }

        public void setChainId4(Character chainId4) {
            this.chainId4 = chainId4;
        }

        public Integer getHibernateId() {
            return hibernateId;
        }

        public void setHibernateId(Integer hibernateId) {
            this.hibernateId = hibernateId;
        }

        public Character getiCode1() {
            return iCode1;
        }

        public void setiCode1(Character iCode1) {
            this.iCode1 = iCode1;
        }

        public Character getiCode2() {
            return iCode2;
        }

        public void setiCode2(Character iCode2) {
            this.iCode2 = iCode2;
        }

        public Character getiCode3() {
            return iCode3;
        }

        public void setiCode3(Character iCode3) {
            this.iCode3 = iCode3;
        }

        public Character getiCode4() {
            return iCode4;
        }

        public void setiCode4(Character iCode4) {
            this.iCode4 = iCode4;
        }

        public Integer getNumRes() {
            return numRes;
        }

        public void setNumRes(Integer numRes) {
            this.numRes = numRes;
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

        public String getResName3() {
            return resName3;
        }

        public void setResName3(String resName3) {
            this.resName3 = resName3;
        }

        public String getResName4() {
            return resName4;
        }

        public void setResName4(String resName4) {
            this.resName4 = resName4;
        }

        public Integer getSeq1() {
            return seq1;
        }

        public void setSeq1(Integer seq1) {
            this.seq1 = seq1;
        }

        public Integer getSeq2() {
            return seq2;
        }

        public void setSeq2(Integer seq2) {
            this.seq2 = seq2;
        }

        public Integer getSeq3() {
            return seq3;
        }

        public void setSeq3(Integer seq3) {
            this.seq3 = seq3;
        }

        public Integer getSeq4() {
            return seq4;
        }

        public void setSeq4(Integer seq4) {
            this.seq4 = seq4;
        }

        public Integer getSeqNum() {
            return seqNum;
        }

        public void setSeqNum(Integer seqNum) {
            this.seqNum = seqNum;
        }

        public String getSiteId() {
            return siteId;
        }

        public void setSiteId(String siteId) {
            this.siteId = siteId;
        }

        /*
         Record Format
         COLUMNS DATA TYPE FIELD DEFINITION
         ---------------------------------------------------------------------------------
         1 - 6 Record name "SITE "
         8 - 10 Integer seqNum Sequence number.
         12 - 14 LString(3) siteID Site name.
         16 - 17 Integer numRes Number of residues that compose the site.
         19 - 21 Residue name resName1 Residue name for first residue that
         creates the site.
         23 Character chainID1 Chain identifier for first residue of site.
         24 - 27 Integer seq1 Residue sequence number for first residue
         of the site.
         28 AChar iCode1 Insertion code for first residue of the
         site.
         30 - 32 Residue name resName2 Residue name for second residue that
         creates the site.
         34 Character chainID2 Chain identifier for second residue of
         the site.
         35 - 38 Integer seq2 Residue sequence number for second
         residue of the site.
         39 AChar iCode2 Insertion code for second residue
         of the site.
         41 - 43 Residue name resName3 Residue name for third residue that
         creates the site.
         45 Character chainID3 Chain identifier for third residue
         of the site.
         46 - 49 Integer seq3 Residue sequence number for third
         residue of the site.
         PDB File Format v. 3.3
         Page 168
         50 AChar iCode3 Insertion code for third residue
         of the site.
         52 - 54 Residue name resName4 Residue name for fourth residue that
         creates the site.
         56 Character chainID4 Chain identifier for fourth residue
         of the site.
         57 - 60 Integer seq4 Residue sequence number for fourth
         residue of the site.
         61 AChar iCode4 Insertion code for fourth residue
         of the site.         
         */
        public void parse(String line) {
            seqNum = StrUtil.substring(line, 8, 10, Integer.class, false, false);
            siteId = StrUtil.substring(line, 12, 14, String.class, false, false);
            numRes = StrUtil.substring(line, 16, 17, Integer.class, false, false);

            resName1 = StrUtil.substring(line, 19, 21, String.class, false, false);
            chainId1 = StrUtil.substring(line, 23, 23, Character.class, false, false);
            seq1 = StrUtil.substring(line, 24, 27, Integer.class, false, false);
            iCode1 = StrUtil.substring(line, 28, 28, Character.class, false, false);

            resName2 = StrUtil.substring(line, 30, 32, String.class, false, false);
            chainId2 = StrUtil.substring(line, 34, 34, Character.class, false, false);
            seq2 = StrUtil.substring(line, 35, 38, Integer.class, false, false);
            iCode2 = StrUtil.substring(line, 39, 39, Character.class, false, false);

            resName3 = StrUtil.substring(line, 41, 43, String.class, false, false);
            chainId3 = StrUtil.substring(line, 45, 45, Character.class, false, false);
            seq3 = StrUtil.substring(line, 46, 49, Integer.class, false, false);
            iCode3 = StrUtil.substring(line, 50, 50, Character.class, false, false);

            resName4 = StrUtil.substring(line, 52, 54, String.class, false, false);
            chainId4 = StrUtil.substring(line, 56, 56, Character.class, false, false);
            seq4 = StrUtil.substring(line, 57, 60, Integer.class, false, false);
            iCode4 = StrUtil.substring(line, 61, 61, Character.class, false, false);
        }

        @Override
        public String toString() {
            StringBuilder ret = new StringBuilder();
            StrUtil.replace(ret, RECORD_NAME.trim(), 1, 6, true);

            StrUtil.replace(ret, seqNum, 8, 10, true);
            StrUtil.replace(ret, siteId, 12, 14, true);
            StrUtil.replace(ret, numRes, 16, 17, true);

            StrUtil.replace(ret, resName1, 19, 21, true);
            StrUtil.replace(ret, chainId1, 23, 23, true);
            StrUtil.replace(ret, seq1, 24, 27, true);
            StrUtil.replace(ret, iCode1, 28, 28, true);

            StrUtil.replace(ret, resName2, 30, 32, true);
            StrUtil.replace(ret, chainId2, 34, 34, true);
            StrUtil.replace(ret, seq2, 35, 38, true);
            StrUtil.replace(ret, iCode2, 39, 39, true);

            StrUtil.replace(ret, resName3, 41, 43, true);
            StrUtil.replace(ret, chainId3, 45, 45, true);
            StrUtil.replace(ret, seq3, 46, 49, true);
            StrUtil.replace(ret, iCode3, 50, 50, true);

            StrUtil.replace(ret, resName4, 52, 54, true);
            StrUtil.replace(ret, chainId4, 56, 56, true);
            StrUtil.replace(ret, seq4, 57, 60, true);
            StrUtil.replace(ret, iCode4, 61, 61, true);
            StrUtil.replace(ret, '\n', 81, 81, true);
            return ret.toString();
        }
    }

    public static class Sorter implements Comparator<Element> {

        @Override
        public int compare(Element o1, Element o2) {
            int ret = 0;
            String s1 = o1.getSiteId();
            String s2 = o2.getSiteId();
            ret = s1.compareTo(s2);
            if (ret == 0) {
                Integer sn1 = o1.getSeqNum();
                Integer sn2 = o2.getSeqNum();
                ret = sn1.compareTo(sn2);
            }
            return ret;
        }
    }
}
