/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.pdb.util;

import com.gas.domain.core.pdb.ATOM;
import com.gas.domain.core.pdb.COMPND;
import com.gas.domain.core.pdb.CONECT;
import com.gas.domain.core.pdb.CRYST1;
import com.gas.domain.core.pdb.DBREF;
import com.gas.domain.core.pdb.EXPDTA;
import com.gas.domain.core.pdb.FORMUL;
import com.gas.domain.core.pdb.HEADER;
import com.gas.domain.core.pdb.HELIX;
import com.gas.domain.core.pdb.HET;
import com.gas.domain.core.pdb.HETATM;
import com.gas.domain.core.pdb.HETNAM;
import com.gas.domain.core.pdb.JRNL;
import com.gas.domain.core.pdb.KEYWDS;
import com.gas.domain.core.pdb.MODEL;
import com.gas.domain.core.pdb.MODRES;
import com.gas.domain.core.pdb.MTRIX1;
import com.gas.domain.core.pdb.MTRIX2;
import com.gas.domain.core.pdb.MTRIX3;
import com.gas.domain.core.pdb.NUMMDL;
import com.gas.domain.core.pdb.ORIGX1;
import com.gas.domain.core.pdb.ORIGX2;
import com.gas.domain.core.pdb.ORIGX3;
import com.gas.domain.core.pdb.PDBDoc;
import com.gas.domain.core.pdb.SCALE1;
import com.gas.domain.core.pdb.SCALE2;
import com.gas.domain.core.pdb.SCALE3;
import com.gas.domain.core.pdb.SEQRES;
import com.gas.domain.core.pdb.SHEET;
import com.gas.domain.core.pdb.SITE;
import com.gas.domain.core.pdb.SOURCE;
import com.gas.domain.core.pdb.SSBOND;
import com.gas.domain.core.pdb.TER;
import com.gas.domain.core.pdb.TITLE;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dq
 */
public class PDBParser {

    private static CONECT conect(List<String> all) {
        CONECT ret = new CONECT();
        List<String> lines = Util.getLinesStartWith(all, CONECT.RECORD_NAME);
        for (String line : lines) {
            CONECT.Element ele = new CONECT.Element();
            ele.parse(line);
            ret.getElements().add(ele);
        }
        return ret;
    }

    private static CRYST1 cryst1(List<String> all) {
        List<String> lines = Util.getLinesStartWith(all, CRYST1.RECORD_NAME);
        CRYST1 ret = new CRYST1();
        ret.parse(lines.get(0));
        return ret;
    }

    private static SCALE1 scale1(List<String> all) {
        List<String> lines = Util.getLinesStartWith(all, SCALE1.RECORD_NAME);
        SCALE1 ret = new SCALE1();
        ret.parse(lines.get(0));
        return ret;
    }

    private static SCALE2 scale2(List<String> all) {
        List<String> lines = Util.getLinesStartWith(all, SCALE2.RECORD_NAME);
        SCALE2 ret = new SCALE2();
        ret.parse(lines.get(0));
        return ret;
    }

    private static SCALE3 scale3(List<String> all) {
        List<String> lines = Util.getLinesStartWith(all, SCALE3.RECORD_NAME);
        SCALE3 ret = new SCALE3();
        ret.parse(lines.get(0));
        return ret;
    }

    public static PDBDoc parse(Class clazz, String name) {
        InputStream inputStream = clazz.getResourceAsStream(name);
        return parse(inputStream);
    }

    private static HETNAM hetnam(List<String> all) {
        HETNAM ret = new HETNAM();
        Util util = new Util();
        List<String> lines = util.getLinesStartWith(all, "HETNAM");
        for (String line : lines) {
            HETNAM.Element element = new HETNAM.Element();
            element.parse(line);
            ret.getElements().add(element);
        }
        return ret;
    }

    private static HELIX helix(List<String> all) {
        HELIX ret = new HELIX();
        List<String> lines = Util.getLinesStartWith(all, HELIX.RECORD_NAME);
        for (String line : lines) {
            HELIX.Element ele = new HELIX.Element();
            ele.parse(line);
            ret.getElements().add(ele);
        }
        return ret;
    }

    private static HEADER header(List<String> all) {
        HEADER ret = new HEADER();
        List<String> lines = Util.getLinesStartWith(all, HEADER.RECORD_NAME);
        if (lines.size() > 0) {
            ret.parse(lines.get(0));
        }
        return ret;
    }

    private static TITLE title(List<String> all) {
        TITLE ret = new TITLE();
        List<String> lines = Util.getLinesStartWith(all, TITLE.RECORD_NAME);
        for (String line : lines) {
            TITLE.Element ele = new TITLE.Element();
            ele.parse(line);
            ret.getElements().add(ele);
        }
        return ret;
    }

    private static COMPND compnd(List<String> all) {
        COMPND ret = new COMPND();
        List<String> lines = Util.getLinesStartWith(all, COMPND.RECORD_NAME);
        for (String line : lines) {
            COMPND.Element element = new COMPND.Element();
            element.parse(line);
            ret.getElements().add(element);
        }
        return ret;
    }

    private static SOURCE source(List<String> all) {
        SOURCE ret = new SOURCE();
        List<String> lines = Util.getLinesStartWith(all, SOURCE.RECORD_NAME);
        for (String line : lines) {
            SOURCE.Element ele = new SOURCE.Element();
            ele.parse(line);
            ret.getElements().add(ele);
        }
        return ret;
    }

    private static SEQRES seqres(List<String> all) {
        SEQRES ret = new SEQRES();
        List<String> lines = Util.getLinesStartWith(all, SEQRES.RECORD_NAME);
        for (String line : lines) {
            SEQRES.Element element = new SEQRES.Element();
            element.parse(line);
            ret.getElements().add(element);
        }
        return ret;
    }

    private static MODRES modres(List<String> all) {
        MODRES ret = new MODRES();
        List<String> lines = Util.getLinesStartWith(all, MODRES.RECORD_NAME, true);
        for (String line : lines) {
            MODRES.Element element = new MODRES.Element();
            element.parse(line);
            ret.getElements().add(element);
        }
        return ret;
    }

    private static HET het(List<String> all) {
        HET ret = new HET();
        List<String> lines = Util.getLinesStartWith(all, HET.RECORD_NAME, true);
        for (String line : lines) {
            HET.Element ele = new HET.Element();
            ele.parse(line);
            ret.getElements().add(ele);
        }
        return ret;
    }

    private static SITE site(List<String> all) {
        SITE ret = new SITE();
        List<String> lines = Util.getLinesStartWith(all, SITE.RECORD_NAME, true);
        for (String line : lines) {
            SITE.Element e = new SITE.Element();
            e.parse(line);
            ret.getElements().add(e);
        }
        return ret;
    }

    private static ORIGX1 origx1(List<String> all) {
        ORIGX1 ret = null;
        List<String> lines = Util.getLinesStartWith(all, ORIGX1.RECORD_NAME, true);
        if (lines.size() > 0) {
            ret = new ORIGX1();
            ret.parse(lines.get(0));
        }
        return ret;
    }

    private static ORIGX2 origx2(List<String> all) {
        ORIGX2 ret = null;
        List<String> lines = Util.getLinesStartWith(all, ORIGX2.RECORD_NAME, true);
        if (lines.size() > 0) {
            ret = new ORIGX2();
            ret.parse(lines.get(0));
        }
        return ret;
    }

    private static ORIGX3 origx3(List<String> all) {
        ORIGX3 ret = null;
        List<String> lines = Util.getLinesStartWith(all, ORIGX3.RECORD_NAME, true);
        if (lines.size() > 0) {
            ret = new ORIGX3();
            ret.parse(lines.get(0));
        }
        return ret;
    }

    private static KEYWDS keywds(List<String> all) {
        KEYWDS ret = new KEYWDS();
        List<String> lines = Util.getLinesStartWith(all, KEYWDS.RECORD_NAME, true);
        for (String line : lines) {
            KEYWDS.Element e = new KEYWDS.Element();
            e.parse(line);
            ret.getElements().add(e);
        }
        return ret;
    }

    public static <T> List<T> parse(String str, boolean all, Class<T> clazz) {
        if (!clazz.isAssignableFrom(PDBDoc.class)) {
            throw new IllegalArgumentException(String.format("Class '%s' not supported!", clazz.toString()));
        }
        List<T> ret = new ArrayList<T>();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(str.getBytes());
        ret.add((T) parse(inputStream, all));
        return ret;
    }

    public static PDBDoc parse(String str) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(str.getBytes());
        return parse(inputStream, true);
    }

    public static PDBDoc parse(File file) {
        PDBDoc ret = null;
        try {
            FileInputStream inputStream = new FileInputStream(file);
            ret = parse(inputStream);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PDBParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    private static MTRIX1 mtrix1(List<String> all) {
        MTRIX1 ret = null;
        List<String> lines = Util.getLinesStartWith(all, MTRIX1.RECORD_NAME);
        if (lines.size() > 0) {
            ret = new MTRIX1();
            ret.parse(lines.get(0));
        }
        return ret;
    }

    private static MTRIX2 mtrix2(List<String> all) {
        MTRIX2 ret = null;
        List<String> lines = Util.getLinesStartWith(all, MTRIX2.RECORD_NAME);
        if (lines.size() > 0) {
            ret = new MTRIX2();
            ret.parse(lines.get(0));
        }
        return ret;
    }

    private static MTRIX3 mtrix3(List<String> all) {
        MTRIX3 ret = null;
        List<String> lines = Util.getLinesStartWith(all, MTRIX3.RECORD_NAME);
        if (lines.size() > 0) {
            ret = new MTRIX3();
            ret.parse(lines.get(0));
        }
        return ret;
    }

    private static NUMMDL nummdl(List<String> all) {
        NUMMDL ret = null;
        List<String> lines = Util.getLinesStartWith(all, NUMMDL.RECORD_NAME);
        if (lines.size() > 0) {
            ret = new NUMMDL();
            ret.parse(lines.get(0));
        }
        return ret;
    }

    private static SSBOND ssbond(List<String> all) {
        SSBOND ret = new SSBOND();
        List<String> lines = Util.getLinesStartWith(all, SSBOND.RECORD_NAME);
        for (String line : lines) {
            SSBOND.Element e = new SSBOND.Element();
            e.parse(line);
            ret.getElements().add(e);
        }
        return ret;
    }

    private static JRNL jrnl(List<String> all) {
        JRNL ret = new JRNL();
        List<String> lines = Util.getLinesStartWith(all, JRNL.RECORD_NAME);
        for (String line : lines) {
            JRNL.Element e = new JRNL.Element();
            e.parse(line);
            ret.getElements().add(e);
        }
        return ret;
    }

    private static DBREF dbref(List<String> all) {
        DBREF ret = new DBREF();
        List<String> lines = Util.getLinesStartWith(all, DBREF.RECORD_NAME);
        for (String line : lines) {
            DBREF.Element e = new DBREF.Element();
            e.parse(line);
            ret.getElements().add(e);
        }
        return ret;
    }

    public static PDBDoc parse(InputStream inputStream) {
        return parse(inputStream, true);
    }

    public static PDBDoc parse(InputStream inputStream, boolean all) {
        PDBDoc ret = new PDBDoc();

        List<String> lines = new ArrayList<String>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }

            HEADER header = header(lines);
            ret.setHeader(header);

            TITLE title = title(lines);
            ret.setTitle(title);

            COMPND compnd = compnd(lines);
            ret.setCompnd(compnd);

            SOURCE source = source(lines);
            ret.setSource(source);

            KEYWDS keywds = keywds(lines);
            ret.setKeywds(keywds);

            NUMMDL nummdl = nummdl(lines);
            ret.setNummdl(nummdl);

            JRNL jrnl = jrnl(lines);
            ret.setJrnl(jrnl);

            DBREF dbref = dbref(lines);
            ret.setDbref(dbref);

            SEQRES seqres = seqres(lines);
            ret.setSeqRes(seqres);

            MODRES modres = modres(lines);
            ret.setModres(modres);

            HET het = het(lines);
            ret.setHet(het);

            HETNAM hetnam = hetnam(lines);
            ret.setHetnam(hetnam);

            HELIX helix = helix(lines);
            ret.setHelix(helix);

            SHEET sheet = sheet(lines);
            ret.setSheet(sheet);

            SSBOND ssbond = ssbond(lines);
            ret.setSsbond(ssbond);

            SITE site = site(lines);
            ret.setSite(site);

            CRYST1 cryst1 = cryst1(lines);
            ret.setCryst1(cryst1);

            ORIGX1 origx1 = origx1(lines);
            ret.setOrigx1(origx1);

            ORIGX2 origx2 = origx2(lines);
            ret.setOrigx2(origx2);

            ORIGX3 origx3 = origx3(lines);
            ret.setOrigx3(origx3);

            SCALE1 scale1 = scale1(lines);
            ret.setScale1(scale1);

            SCALE2 scale2 = scale2(lines);
            ret.setScale2(scale2);

            SCALE3 scale3 = scale3(lines);
            ret.setScale3(scale3);

            MTRIX1 mtrix1 = mtrix1(lines);
            ret.setMtrix1(mtrix1);

            MTRIX2 mtrix2 = mtrix2(lines);
            ret.setMtrix2(mtrix2);

            MTRIX3 mtrix3 = mtrix3(lines);
            ret.setMtrix3(mtrix3);


            if (nummdl != null && nummdl.getModelNumber() > 1 && all) {
                modelMultiple(lines, ret);
            } else {
                modelSingle(lines, ret);
            }

            /*
             ATOM atom = atom(lines);
             ret.setAtom(atom);
            
             TER ter = ter(lines);
             ret.setTer(ter);
            
             HETATM hetatm = hetatm(lines);
             ret.setHetatm(hetatm);
            
             */
            EXPDTA expdta = expdta(lines);
            ret.setExpdta(expdta);

            FORMUL formul = formul(lines);
            ret.setFormul(formul);

            CONECT conect = conect(lines);
            ret.setConect(conect);

        } catch (IOException ex) {
            Logger.getLogger(PDBParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    private static void modelSingle(List<String> all, PDBDoc doc) {
        List<String> lines = Util.getLinesStartWith(all, new String[]{ATOM.RECORD_NAME.trim(), TER.RECORD_NAME.trim(), HETATM.RECORD_NAME.trim()});
        MODEL model = new MODEL();
        model.parse(lines);
        doc.getModels().add(model);
    }

    private static void modelMultiple(List<String> all, PDBDoc doc) {
        NUMMDL nummdl = doc.getNummdl();
        for (int i = 0; i < nummdl.getModelNumber(); i++) {
            List<String> lines = Util.getLinesBetween(all, MODEL.RECORD_NAME, MODEL.END_RECORD_NAME);
            MODEL model = new MODEL();
            model.parse(lines);
            doc.getModels().add(model);
        }
    }

    private static HETATM hetatm(List<String> all) {
        HETATM ret = new HETATM();
        List<String> lines = Util.getLinesStartWith(all, HETATM.RECORD_NAME);
        for (String line : lines) {
            HETATM.Element e = new HETATM.Element();
            e.parse(line);
            ret.getElements().add(e);
        }
        return ret;
    }

    private static SHEET sheet(List<String> all) {
        SHEET ret = new SHEET();
        List<String> lines = Util.getLinesStartWith(all, SHEET.RECORD_NAME);
        for (String line : lines) {
            SHEET.Element element = new SHEET.Element();
            element.parse(line);
            ret.getElements().add(element);
        }
        return ret;
    }

    private static EXPDTA expdta(List<String> all) {
        EXPDTA ret = new EXPDTA();
        List<String> lines = Util.getLinesStartWith(all, EXPDTA.RECORD_NAME);
        for (String line : lines) {
            EXPDTA.Element element = new EXPDTA.Element();
            element.parse(line);
            ret.getElements().add(element);
        }
        return ret;
    }

    private static FORMUL formul(List<String> all) {
        FORMUL ret = new FORMUL();
        List<String> lines = Util.getLinesStartWith(all, FORMUL.RECORD_NAME);
        for (String line : lines) {
            FORMUL.Element element = new FORMUL.Element();
            element.parse(line);
            ret.getElements().add(element);
        }
        return ret;
    }

    private static MODEL model(List<String> all) {
        MODEL ret = new MODEL();
        List<String> lines = Util.getLinesBetween(all, MODEL.RECORD_NAME, MODEL.END_RECORD_NAME);
        if (lines.size() > 0) {
            ret.parse(lines.get(0));
        }
        return ret;
    }

    private static class Util {

        public static List<String> getLinesStartWith(List<String> all, String prefix) {
            return getLinesStartWith(all, prefix, true);
        }

        public static List<String> getLinesStartWith(List<String> all, String[] prefixes) {
            List<String> ret = new ArrayList<String>();
            List<String> pList = Arrays.asList(prefixes);
            for (int i = 0; i < all.size(); i++) {
                String line = all.get(i);
                String[] splits = line.split(" ");
                if (splits.length > 0) {
                    if (pList.contains(splits[0])) {
                        ret.add(line);
                        all.remove(i);
                        i--;
                    }
                }

            }
            return ret;
        }

        public static List<String> getLinesBetween(List<String> all, String startPrefix, String endPrefix) {
            List<String> ret = new ArrayList<String>();
            boolean startFound = false;
            boolean endFound = false;
            boolean canCopy = false;
            for (int i = 0; i < all.size(); i++) {
                String line = all.get(i);
                if (!startFound && line.startsWith(startPrefix)) {
                    startFound = true;
                    canCopy = true;
                } else if (!endFound && line.startsWith(endPrefix)) {
                    if (startFound) {
                        endFound = true;
                    }
                }

                if (canCopy) {
                    ret.add(line);
                    all.remove(i);
                    i--;
                }
                if (startFound && endFound) {
                    canCopy = false;
                }
            }
            return ret;
        }

        public static List<String> getLinesStartWith(List<String> all, String prefix, boolean remove) {
            List<String> ret = new ArrayList<String>();
            for (int i = 0; i < all.size(); i++) {
                String line = all.get(i);
                if (line.startsWith(prefix)) {
                    if (remove) {
                        all.remove(i);
                        i--;
                    }
                    ret.add(line);
                }
            }
            return ret;
        }
    }
}
