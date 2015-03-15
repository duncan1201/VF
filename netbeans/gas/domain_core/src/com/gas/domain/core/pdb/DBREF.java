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
public class DBREF implements Cloneable {

    public final static String RECORD_NAME = "DBREF ";
    private Integer hibernateId;
    private Set<Element> elements = new HashSet<Element>();

    @Override
    public DBREF clone() {
        DBREF ret = new DBREF();
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
        private String idCode;
        private Character chainId;
        private Integer seqBegin;
        private Character insertBegin;
        private Integer seqEnd;
        private Character insertEnd;
        private String database;
        private String dbAccession;
        private String dbIdCode;
        private Integer dbseqBegin;
        private Character idbnsBeg;
        private Integer dbseqEnd;
        private Character dbinsEnd;

        @Override
        public Element clone() {
            Element ret = new Element();
            ret.setChainId(chainId);
            ret.setDatabase(database);
            ret.setDbAccession(dbAccession);
            ret.setDbIdCode(dbIdCode);
            ret.setDbinsEnd(dbinsEnd);
            ret.setDbseqBegin(dbseqBegin);
            ret.setDbseqEnd(dbseqEnd);
            ret.setIdCode(idCode);
            ret.setIdbnsBeg(idbnsBeg);
            ret.setInsertBegin(insertBegin);
            ret.setInsertEnd(insertEnd);
            ret.setSeqBegin(seqBegin);
            ret.setSeqEnd(seqEnd);
            return ret;
        }

        public Character getChainId() {
            return chainId;
        }

        public void setChainId(Character chainId) {
            this.chainId = chainId;
        }

        public String getDatabase() {
            return database;
        }

        public void setDatabase(String database) {
            this.database = database;
        }

        public String getDbAccession() {
            return dbAccession;
        }

        public void setDbAccession(String dbAccession) {
            this.dbAccession = dbAccession;
        }

        public String getDbIdCode() {
            return dbIdCode;
        }

        public void setDbIdCode(String dbIdCode) {
            this.dbIdCode = dbIdCode;
        }

        public Character getDbinsEnd() {
            return dbinsEnd;
        }

        public void setDbinsEnd(Character dbinsEnd) {
            this.dbinsEnd = dbinsEnd;
        }

        public Integer getDbseqBegin() {
            return dbseqBegin;
        }

        public void setDbseqBegin(Integer dbseqBegin) {
            this.dbseqBegin = dbseqBegin;
        }

        public Integer getDbseqEnd() {
            return dbseqEnd;
        }

        public void setDbseqEnd(Integer dbseqEnd) {
            this.dbseqEnd = dbseqEnd;
        }

        public Integer getHibernateId() {
            return hibernateId;
        }

        public void setHibernateId(Integer hibernateId) {
            this.hibernateId = hibernateId;
        }

        public String getIdCode() {
            return idCode;
        }

        public void setIdCode(String idCode) {
            this.idCode = idCode;
        }

        public Character getIdbnsBeg() {
            return idbnsBeg;
        }

        public void setIdbnsBeg(Character idbnsBeg) {
            this.idbnsBeg = idbnsBeg;
        }

        public Character getInsertBegin() {
            return insertBegin;
        }

        public void setInsertBegin(Character insertBegin) {
            this.insertBegin = insertBegin;
        }

        public Character getInsertEnd() {
            return insertEnd;
        }

        public void setInsertEnd(Character insertEnd) {
            this.insertEnd = insertEnd;
        }

        public Integer getSeqBegin() {
            return seqBegin;
        }

        public void setSeqBegin(Integer seqBegin) {
            this.seqBegin = seqBegin;
        }

        public Integer getSeqEnd() {
            return seqEnd;
        }

        public void setSeqEnd(Integer seqEnd) {
            this.seqEnd = seqEnd;
        }

        /*
         Record Format
         COLUMNS DATA TYPE FIELD DEFINITION
         -----------------------------------------------------------------------------------
         1 - 6 Record name "DBREF "
         8 - 11 IDcode idCode ID code of this entry.
         13 Character chainID Chain identifier.
         15 - 18 Integer seqBegin Initial sequence number of the
         PDB sequence segment.
         19 AChar insertBegin Initial insertion code of the
         PDB sequence segment.
         21 - 24 Integer seqEnd Ending sequence number of the
         PDB sequence segment.
         25 AChar insertEnd Ending insertion code of the
         PDB sequence segment.
         27 - 32 LString database Sequence database name.
         34 - 41 LString dbAccession Sequence database accession code.
         43 - 54 LString dbIdCode Sequence database identification code.
         56 - 60 Integer dbseqBegin Initial sequence number of the
         database seqment.
         61 AChar idbnsBeg Insertion code of initial residue of
         the segment, if PDB is the reference.
         63 - 67 Integer dbseqEnd Ending sequence number of the
         database segment.
         68 AChar dbinsEnd Insertion code of the ending residue of
         the segment, if PDB is the reference.         
         */
        public void parse(String line) {
            idCode = StrUtil.substring(line, 8, 11, String.class, false, false);
            chainId = StrUtil.substring(line, 13, 13, Character.class, false, false);
            seqBegin = StrUtil.substring(line, 15, 18, Integer.class, false, false);
            insertBegin = StrUtil.substring(line, 19, 19, Character.class, false, false);
            seqEnd = StrUtil.substring(line, 21, 24, Integer.class, false, false);
            insertEnd = StrUtil.substring(line, 25, 25, Character.class, false, false);
            database = StrUtil.substring(line, 27, 32, String.class, false, false);
            dbAccession = StrUtil.substring(line, 34, 41, String.class, false, false);
            dbIdCode = StrUtil.substring(line, 43, 54, String.class, false, false);
            dbseqBegin = StrUtil.substring(line, 56, 60, Integer.class, false, false);
            idbnsBeg = StrUtil.substring(line, 61, 61, Character.class, false, false);
            dbseqEnd = StrUtil.substring(line, 63, 67, Integer.class, false, false);
            dbinsEnd = StrUtil.substring(line, 68, 68, Character.class, false, false);
        }

        @Override
        public String toString() {
            StringBuilder ret = new StringBuilder();
            StrUtil.replace(ret, RECORD_NAME.trim(), 1, 6, true);
            StrUtil.replace(ret, idCode, 8, 11, true);
            StrUtil.replace(ret, chainId, 13, 13, true);
            StrUtil.replace(ret, seqBegin, 15, 18, true);
            StrUtil.replace(ret, insertBegin, 19, 19, true);
            StrUtil.replace(ret, seqEnd, 21, 24, true);
            StrUtil.replace(ret, insertEnd, 25, 25, true);
            StrUtil.replace(ret, database, 27, 32, true);
            StrUtil.replace(ret, dbAccession, 34, 41, true);
            StrUtil.replace(ret, dbIdCode, 43, 54, true);
            StrUtil.replace(ret, dbseqBegin, 56, 60, true);
            StrUtil.replace(ret, idbnsBeg, 61, 61, true);
            StrUtil.replace(ret, dbseqEnd, 63, 67, true);
            StrUtil.replace(ret, dbinsEnd, 68, 68, true);
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
