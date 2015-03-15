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
public class SHEET implements Cloneable {

    public final static String RECORD_NAME = "SHEET";
    private Integer hibernateId;
    private Set<Element> elements = new HashSet<Element>();

    @Override
    public SHEET clone() {
        SHEET ret = new SHEET();
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
        private Integer strand;
        private String sheetId;
        private Integer numStrands;
        private String initResName;
        private Character initChainId;
        private Integer initSeqNum;
        private Character initICode;
        private String endResName;
        private Character endChainId;
        private Integer endSeqNum;
        private Character endICode;
        private Integer sense;
        private String curAtom;
        private String curResName;
        private Character curChainId;
        private Integer curResSeq;
        private Character curICode;
        private String prevAtom;
        private String prevResName;
        private Character prevChainId;
        private Integer prevResSeq;
        private Character prevICode;

        @Override
        public Element clone() {
            Element ret = new Element();
            ret.setCurAtom(curAtom);
            ret.setCurChainId(curChainId);
            ret.setCurICode(curICode);
            ret.setCurResName(curResName);
            ret.setCurResSeq(curResSeq);
            ret.setCurResSeq(curResSeq);
            ret.setEndChainId(endChainId);
            ret.setEndICode(endICode);
            ret.setEndResName(endResName);
            ret.setEndSeqNum(endSeqNum);
            ret.setInitChainId(initChainId);
            ret.setInitICode(initICode);
            ret.setInitResName(initResName);
            ret.setInitSeqNum(initSeqNum);
            ret.setNumStrands(numStrands);
            ret.setPrevAtom(prevAtom);
            ret.setPrevChainId(prevChainId);
            ret.setPrevICode(prevICode);
            ret.setPrevResName(prevResName);
            ret.setPrevResSeq(prevResSeq);
            ret.setSense(sense);
            ret.setSheetId(sheetId);
            ret.setStrand(strand);
            return ret;
        }

        /*
         Record Format
         COLUMNS DATA TYPE FIELD DEFINITION
         -------------------------------------------------------------------------------------
         1 - 6 Record name "SHEET "
         8 - 10 Integer strand Strand number which starts at 1 for each
         strand within a sheet and increases by one.
         12 - 14 LString(3) sheetID Sheet identifier.
         15 - 16 Integer numStrands Number of strands in sheet.
         18 - 20 Residue name initResName Residue name of initial residue.
         22 Character initChainID Chain identifier of initial residue
         in strand.
         23 - 26 Integer initSeqNum Sequence number of initial residue
         in strand.
         27 AChar initICode Insertion code of initial residue
         in strand.
         29 - 31 Residue name endResName Residue name of terminal residue.
         33 Character endChainID Chain identifier of terminal residue.
         34 - 37 Integer endSeqNum Sequence number of terminal residue.
         38 AChar endICode Insertion code of terminal residue.
         39 - 40 Integer sense Sense of strand with respect to previous
         strand in the sheet. 0 if first strand,
         1 if parallel,and -1 if anti-parallel.
         42 - 45 Atom curAtom Registration. Atom name in current strand.
         46 - 48 Residue name curResName Registration. Residue name in current
         strand
         50 Character curChainId Registration. Chain identifier in
         current strand.
         51 - 54 Integer curResSeq Registration. Residue sequence number
         in current strand.
         55 AChar curICode Registration. Insertion code in
         current strand.
         57 - 60 Atom prevAtom Registration. Atom name in previous strand.
         PDB File Format v. 3.3
         Page 160
         61 - 63 Residue name prevResName Registration. Residue name in
         previous strand.
         65 Character prevChainId Registration. Chain identifier in
         previous strand.
         66 - 69 Integer prevResSeq Registration. Residue sequence number
         in previous strand.
         70 AChar prevICode Registration. Insertion code in
         previous strand.     
         */
        @Override
        public String toString() {
            StringBuilder ret = new StringBuilder();
            StrUtil.replace(ret, RECORD_NAME, 1, 6, true);
            StrUtil.replace(ret, strand, 8, 10, true);
            StrUtil.replace(ret, sheetId, 12, 14, true);
            StrUtil.replace(ret, numStrands, 15, 16, true);
            StrUtil.replace(ret, initResName, 18, 20, true);
            StrUtil.replace(ret, initChainId, 22, 22, true);
            StrUtil.replace(ret, initSeqNum, 23, 26, true);
            StrUtil.replace(ret, initICode, 27, 27, true);
            StrUtil.replace(ret, endResName, 29, 31, true);
            StrUtil.replace(ret, endSeqNum, 34, 37, true);
            StrUtil.replace(ret, endICode, 38, 38, true);
            StrUtil.replace(ret, sense, 39, 40, true);
            StrUtil.replace(ret, curAtom, 42, 45, true);
            StrUtil.replace(ret, curResName, 46, 48, true);
            StrUtil.replace(ret, curChainId, 50, 50, true);
            StrUtil.replace(ret, curResSeq, 51, 54, true);
            StrUtil.replace(ret, curICode, 55, 55, true);
            StrUtil.replace(ret, prevAtom, 57, 60, true);
            StrUtil.replace(ret, prevResName, 61, 63, true);
            StrUtil.replace(ret, prevChainId, 65, 65, true);
            StrUtil.replace(ret, prevResSeq, 66, 69, true);
            StrUtil.replace(ret, prevICode, 70, 70, true);
            StrUtil.replace(ret, '\n', 81, 81, true);
            return ret.toString();
        }

        public void parse(String line) {
            strand = StrUtil.substring(line, 8, 10, Integer.class, false, false);
            sheetId = StrUtil.substring(line, 12, 14, String.class, false, false);
            numStrands = StrUtil.substring(line, 15, 16, Integer.class, false, false);
            initResName = StrUtil.substring(line, 18, 20, String.class, true, false);
            initChainId = StrUtil.substring(line, 22, 22, Character.class, false, false);
            initSeqNum = StrUtil.substring(line, 23, 26, Integer.class, false, false);
            initICode = StrUtil.substring(line, 27, 27, Character.class, false, false);
            endResName = StrUtil.substring(line, 29, 31, String.class, false, false);
            endChainId = StrUtil.substring(line, 33, 33, Character.class, false, false);
            endSeqNum = StrUtil.substring(line, 34, 37, Integer.class, false, false);
            sense = StrUtil.substring(line, 39, 40, Integer.class, false, false);
            curAtom = StrUtil.substring(line, 42, 45, String.class, false, false);
            curResName = StrUtil.substring(line, 46, 48, String.class, false, false);
            curChainId = StrUtil.substring(line, 50, 50, Character.class, false, false);
            curResSeq = StrUtil.substring(line, 51, 54, Integer.class, false, false);
            curICode = StrUtil.substring(line, 55, 55, Character.class, false, false);
            prevAtom = StrUtil.substring(line, 57, 60, String.class, false, false);
            prevResName = StrUtil.substring(line, 61, 63, String.class, false, false);
            prevChainId = StrUtil.substring(line, 65, 65, Character.class, false, false);
            prevResSeq = StrUtil.substring(line, 66, 69, Integer.class, false, false);
            prevICode = StrUtil.substring(line, 70, 70, Character.class, false, false);
        }

        public String getCurAtom() {
            return curAtom;
        }

        public void setCurAtom(String curAtom) {
            this.curAtom = curAtom;
        }

        public Character getCurChainId() {
            return curChainId;
        }

        public void setCurChainId(Character curChainId) {
            this.curChainId = curChainId;
        }

        public Character getCurICode() {
            return curICode;
        }

        public void setCurICode(Character curICode) {
            this.curICode = curICode;
        }

        public String getCurResName() {
            return curResName;
        }

        public void setCurResName(String curResName) {
            this.curResName = curResName;
        }

        public Integer getCurResSeq() {
            return curResSeq;
        }

        public void setCurResSeq(Integer curResSeq) {
            this.curResSeq = curResSeq;
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

        public Integer getNumStrands() {
            return numStrands;
        }

        public void setNumStrands(Integer numStrands) {
            this.numStrands = numStrands;
        }

        public String getPrevAtom() {
            return prevAtom;
        }

        public void setPrevAtom(String prevAtom) {
            this.prevAtom = prevAtom;
        }

        public Character getPrevChainId() {
            return prevChainId;
        }

        public void setPrevChainId(Character prevChainId) {
            this.prevChainId = prevChainId;
        }

        public Character getPrevICode() {
            return prevICode;
        }

        public void setPrevICode(Character prevICode) {
            this.prevICode = prevICode;
        }

        public String getPrevResName() {
            return prevResName;
        }

        public void setPrevResName(String prevResName) {
            this.prevResName = prevResName;
        }

        public Integer getPrevResSeq() {
            return prevResSeq;
        }

        public void setPrevResSeq(Integer prevResSeq) {
            this.prevResSeq = prevResSeq;
        }

        public Integer getSense() {
            return sense;
        }

        public void setSense(Integer sense) {
            this.sense = sense;
        }

        public String getSheetId() {
            return sheetId;
        }

        public void setSheetId(String sheetId) {
            this.sheetId = sheetId;
        }

        public Integer getStrand() {
            return strand;
        }

        public void setStrand(Integer strand) {
            this.strand = strand;
        }
    }

    public static class Sorter implements Comparator<Element> {

        @Override
        public int compare(Element o1, Element o2) {
            int ret = 0;
            String s1 = o1.getSheetId();
            String s2 = o2.getSheetId();
            ret = s1.compareTo(s2);
            if (ret == 0) {
                Integer st1 = o1.getStrand();
                Integer st2 = o2.getStrand();
                ret = st1.compareTo(st2);
            }
            return ret;
        }
    }
}
