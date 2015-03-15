/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.pdb;

import com.gas.common.ui.util.CommonUtil;
import com.gas.common.ui.util.StrUtil;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author dq
 */
public class OBSLTE implements Cloneable {

    public final static String RECORD_NAME = "OBSLTE";
    private Integer hibernateId;
    private Set<Element> elements = new HashSet<Element>();

    @Override
    public OBSLTE clone() {
        OBSLTE ret = new OBSLTE();
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
        for (Element e : eles) {
            String str = e.toString();
            ret.append(str);
        }
        return ret.toString();
    }

    public static class Element implements Cloneable {

        private Integer hibernateId;
        private Integer continuation;
        private String repDate;
        private String idCode;
        private String rIdCodes;

        @Override
        public Element clone() {
            Element ret = new Element();
            ret.setContinuation(continuation);
            ret.setIdCode(idCode);
            ret.setRepDate(repDate);
            ret.setrIdCodes(rIdCodes);
            return ret;
        }

        public Integer getContinuation() {
            return continuation;
        }

        public void setContinuation(Integer continuation) {
            this.continuation = continuation;
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

        public String getrIdCodes() {
            return rIdCodes;
        }

        public void setrIdCodes(String rIdCodes) {
            this.rIdCodes = rIdCodes;
        }

        public String getRepDate() {
            return repDate;
        }

        public void setRepDate(String repDate) {
            this.repDate = repDate;
        }

        /*
         COLUMNS DATA TYPE FIELD DEFINITION
         ---------------------------------------------------------------------------------------
         1 - 6 Record name "OBSLTE"
         9 - 10 Continuation continuation Allows concatenation of multiple records
         12 - 20 Date repDate Date that this entry was replaced.
         22 - 25 IDcode idCode ID code of this entry.
         32 - 35 IDcode rIdCode ID code of entry that replaced this one.
         37 - 40 IDcode rIdCode ID code of entry that replaced this one.
         42 - 45 IDcode rIdCode ID code of entry that replaced this one.
         47 - 50 IDcode rIdCode ID code of entry that replaced this one.
         52 - 55 IDcode rIdCode ID code of entry that replaced this one.
         57 - 60 IDcode rIdCode ID code of entry that replaced this one.
         62 - 65 IDcode rIdCode ID code of entry that replaced this one.
         67 - 70 IDcode rIdCode ID code of entry that replaced this one.         
         */
        public void parse(String line) {
            continuation = StrUtil.substring(line, 9, 10, Integer.class, false, false);
            repDate = StrUtil.substring(line, 12, 20, String.class, false, false);
            idCode = StrUtil.substring(line, 22, 25, String.class, false, false);
            rIdCodes = StrUtil.substring(line, 32, 70, String.class, false, false);
        }

        @Override
        public String toString() {
            StringBuilder ret = new StringBuilder();
            StrUtil.replace(ret, RECORD_NAME, 1, 6, true);
            StrUtil.replace(ret, continuation, 9, 10, true);
            StrUtil.replace(ret, repDate, 12, 20, true);
            StrUtil.replace(ret, idCode, 22, 25, true);
            StrUtil.replace(ret, rIdCodes, 32, 70, true);
            return ret.toString();
        }
    }
}
