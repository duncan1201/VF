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
public class ATOM implements Cloneable {

    public final static String RECORD_NAME = "ATOM ";
    private Integer hibernateId;
    private Set<Element> elements = new HashSet<Element>();

    @Override
    public ATOM clone() {
        ATOM ret = new ATOM();
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
        ret.trimToSize();
        return ret.toString();
    }

    public static class Element implements Cloneable {

        private Integer hibernateId;
        private Integer serial;
        private String name;
        private Character altLoc;
        private String resName;
        private Character chainId;
        private Integer resSeq;
        private Character iCode;
        private Float x;
        private Float y;
        private Float z;
        private Float occupancy;
        private Float tempFactor;
        private String element;
        private Integer charge;

        @Override
        public Element clone() {
            Element ret = new Element();
            ret.setAltLoc(altLoc);
            ret.setChainId(chainId);
            ret.setCharge(charge);
            ret.setElement(element);
            ret.setName(name);
            ret.setOccupancy(occupancy);
            ret.setResName(resName);
            ret.setResSeq(resSeq);
            ret.setSerial(serial);
            ret.setTempFactor(tempFactor);
            ret.setX(x);
            ret.setY(y);
            ret.setZ(z);
            ret.setiCode(iCode);
            return ret;
        }

        public Integer getHibernateId() {
            return hibernateId;
        }

        public void setHibernateId(Integer hibernateId) {
            this.hibernateId = hibernateId;
        }

        public Character getAltLoc() {
            return altLoc;
        }

        public void setAltLoc(Character altLoc) {
            this.altLoc = altLoc;
        }

        public Float getOccupancy() {
            return occupancy;
        }

        public void setOccupancy(Float occupancy) {
            this.occupancy = occupancy;
        }

        public Float getTempFactor() {
            return tempFactor;
        }

        public void setTempFactor(Float tempFactor) {
            this.tempFactor = tempFactor;
        }

        public Character getChainId() {
            return chainId;
        }

        public void setChainId(Character chainId) {
            this.chainId = chainId;
        }

        public Integer getCharge() {
            return charge;
        }

        public void setCharge(Integer charge) {
            this.charge = charge;
        }

        public String getElement() {
            return element;
        }

        public void setElement(String element) {
            this.element = element;
        }

        public Character getiCode() {
            return iCode;
        }

        public void setiCode(Character iCode) {
            this.iCode = iCode;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

        public Float getX() {
            return x;
        }

        public void setX(Float x) {
            this.x = x;
        }

        public Float getY() {
            return y;
        }

        public void setY(Float y) {
            this.y = y;
        }

        public Float getZ() {
            return z;
        }

        public void setZ(Float z) {
            this.z = z;
        }

        public void parse(String line) {
            serial = StrUtil.substring(line, 7, 11, Integer.class, false, false);
            name = StrUtil.substring(line, 13, 16, String.class, false, false);
            altLoc = StrUtil.substring(line, 17, 17, Character.class, false, false);
            resName = StrUtil.substring(line, 18, 20, String.class, false, false);
            chainId = StrUtil.substring(line, 22, 22, Character.class, false, false);
            resSeq = StrUtil.substring(line, 23, 26, Integer.class, false, false);
            iCode = StrUtil.substring(line, 27, 27, Character.class, false, false);
            x = StrUtil.substring(line, 31, 38, Float.class, false, false);
            y = StrUtil.substring(line, 39, 46, Float.class, false, false);
            z = StrUtil.substring(line, 47, 54, Float.class, false, false);
            occupancy = StrUtil.substring(line, 55, 60, Float.class, false, false);
            tempFactor = StrUtil.substring(line, 61, 66, Float.class, false, false);
            element = StrUtil.substring(line, 77, 78, String.class, false, false);
            charge = StrUtil.substring(line, 79, 80, Integer.class, false, false);

        }

        /*
         COLUMNS DATA TYPE FIELD DEFINITION
         -------------------------------------------------------------------------------------
         1 - 6 Record name "ATOM "
         7 - 11 Integer serial Atom serial number.
         13 - 16 Atom name Atom name.
         17 Character altLoc Alternate location indicator.
         18 - 20 Residue name resName Residue name.
         22 Character chainID Chain identifier.
         23 - 26 Integer resSeq Residue sequence number.
         27 AChar iCode Code for insertion of residues.
         31 - 38 Real(8.3) x Orthogonal coordinates for X in Angstroms.
         39 - 46 Real(8.3) y Orthogonal coordinates for Y in Angstroms.
         47 - 54 Real(8.3) z Orthogonal coordinates for Z in Angstroms.
         55 - 60 Real(6.2) occupancy Occupancy.
         61 - 66 Real(6.2) tempFactor Temperature factor.
         77 - 78 LString(2) element Element symbol, right-justified.
         79 - 80 LString(2) charge Charge on the atom.     
         */
        @Override
        public String toString() {
            StringBuilder ret = new StringBuilder();
            StrUtil.replace(ret, RECORD_NAME, 1, 6, true);
            StrUtil.replace(ret, serial, 7, 11, true);
            StrUtil.replace(ret, name, 13, 16, true);
            StrUtil.replace(ret, altLoc, 17, 17, true);
            StrUtil.replace(ret, resName, 18, 20, true);
            StrUtil.replace(ret, chainId, 22, 22, true);
            StrUtil.replace(ret, resSeq, 23, 26, true);
            StrUtil.replace(ret, iCode, 27, 27, true);
            StrUtil.replace(ret, x, 31, 38, true);
            StrUtil.replace(ret, y, 39, 46, true);
            StrUtil.replace(ret, z, 47, 54, true);
            StrUtil.replace(ret, occupancy, 55, 60, true);
            StrUtil.replace(ret, tempFactor, 61, 66, true);
            StrUtil.replace(ret, element, 77, 78, false);
            StrUtil.replace(ret, charge, 79, 80, true);
            StrUtil.replace(ret, '\n', 81, 81, true);
            ret.trimToSize();
            return ret.toString();
        }
    }

    public static class Sorter implements Comparator<Element> {

        @Override
        public int compare(Element o1, Element o2) {
            return o1.getSerial().compareTo(o2.getSerial());
        }
    }
}
