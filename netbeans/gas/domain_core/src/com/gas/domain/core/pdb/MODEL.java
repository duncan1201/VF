/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.pdb;

import com.gas.common.ui.util.StrUtil;
import java.util.List;

/**
 *
 * @author dq
 */
public class MODEL implements Cloneable {

    public final static String RECORD_NAME = "MODEL ";
    public final static String END_RECORD_NAME = "ENDMDL ";
    private Integer hibernateId;
    private Integer serial;
    private ATOM atom = new ATOM();
    private ANISOU anisou = new ANISOU();
    private TER ter = new TER();
    private HETATM hetatm = new HETATM();

    @Override
    public MODEL clone() {
        MODEL ret = new MODEL();
        if (anisou != null) {
            ret.setAnisou(anisou.clone());
        }
        if (atom != null) {
            ret.setAtom(atom.clone());
        }
        if (hetatm != null) {
            ret.setHetatm(hetatm.clone());
        }
        ret.setSerial(serial);
        if (ter != null) {
            ret.setTer(ter.clone());
        }
        return ret;
    }

    public Integer getHibernateId() {
        return hibernateId;
    }

    public void setHibernateId(Integer hibernateId) {
        this.hibernateId = hibernateId;
    }

    public ANISOU getAnisou() {
        return anisou;
    }

    public void setAnisou(ANISOU anisou) {
        this.anisou = anisou;
    }

    public ATOM getAtom() {
        return atom;
    }

    public void setAtom(ATOM atom) {
        this.atom = atom;
    }

    public TER getTer() {
        return ter;
    }

    public void setTer(TER ter) {
        this.ter = ter;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public HETATM getHetatm() {
        return hetatm;
    }

    public void setHetatm(HETATM hetatm) {
        this.hetatm = hetatm;
    }

    /*
     Record Format
     COLUMNS DATA TYPE FIELD DEFINITION
     -------------------------------------------------------------------------------------
     1 - 6 Record name "MODEL "
     11 - 14 Integer serial Model serial number.     
     */
    public void parse(String line) {
        serial = StrUtil.substring(line, 11, 14, Integer.class, false, false);
    }

    public void parse(List<String> lines) {
        for (String line : lines) {
            if (line.startsWith(RECORD_NAME)) {
                parse(line);
            } else if (line.startsWith(ATOM.RECORD_NAME)) {
                ATOM.Element e = new ATOM.Element();
                e.parse(line);
                atom.getElements().add(e);
            } else if (line.startsWith(TER.RECORD_NAME)) {
                TER.Element e = new TER.Element();
                e.parse(line);
                ter.getElements().add(e);
            } else if (line.startsWith(HETATM.RECORD_NAME)) {
                HETATM.Element e = new HETATM.Element();
                e.parse(line);
                hetatm.getElements().add(e);
            }
        }
    }

    private String toStringRecordStart() {
        StringBuilder ret = new StringBuilder();
        StrUtil.replace(ret, RECORD_NAME.trim(), 1, 6, true);
        StrUtil.replace(ret, serial, 11, 14, true);
        StrUtil.replace(ret, '\n', 81, 81, true);

        return ret.toString();
    }

    private String toStringRecordEnd() {
        StringBuilder ret = new StringBuilder();
        StrUtil.replace(ret, END_RECORD_NAME.trim(), 1, 6, true);
        StrUtil.replace(ret, '\n', 81, 81, true);

        return ret.toString();
    }

    public String toString(boolean headerFooter) {
        StringBuilder ret = new StringBuilder();

        if (headerFooter) {
            String recordStr = toStringRecordStart();
            ret.append(recordStr);
        }
        String atomStr = atom.toString();
        ret.append(atomStr);
        String terStr = ter.toString();
        ret.append(terStr);
        String hetatmStr = hetatm.toString();
        ret.append(hetatmStr);
        if (headerFooter) {
            String recordEndStr = toStringRecordEnd();
            ret.append(recordEndStr);
        }

        return ret.toString();
    }
}
