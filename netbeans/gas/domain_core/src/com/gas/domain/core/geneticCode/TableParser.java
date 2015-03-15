/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.geneticCode;

import com.gas.common.ui.core.StringList;
import com.gas.common.ui.util.StrUtil;
import com.gas.domain.core.geneticCode.api.Codon;
import com.gas.domain.core.geneticCode.api.GeneticCodeTable;
import com.gas.domain.core.geneticCode.api.GeneticCodeTableList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author dq
 */
public class TableParser {

    private final static String NAME_REG = "\"(.+)\"";
    private final static String ID_REG = "id(.+),";
    private final static String NCBIEAA_REG = "\"(.+)\"";
    private final static String SNCBIEAA_REG = "\"(.+)\"";
    private final static String BASE_REG = "Base[0-9]{1}(.+)";

    public static GeneticCodeTableList parse(Class clazz, String name) {
        return parse(clazz.getResourceAsStream(name));
    }

    public static GeneticCodeTableList parse(InputStream inputStream) {
        GeneticCodeTableList ret = new GeneticCodeTableList();
        try {
            String str = IOUtils.toString(inputStream);
            StringList tableStrs = toTablesStr(str);
            Iterator<String> itr = tableStrs.iterator();
            while (itr.hasNext()) {
                String tableStr = itr.next();
                GeneticCodeTable codeTable = parseTable(tableStr);
                ret.add(codeTable);
            }
            System.out.print("");
        } catch (IOException ex) {
            Logger.getLogger(TableParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    /*
     * name "Standard" , name "SGC0" , id 1 , ncbieaa
     * "FFLLSSSSYY**CC*WLLLLPPPPHHQQRRRRIIIMTTTTNNKKSSRRVVVVAAAADDEEGGGG",
     * sncbieaa
     * "---M---------------M---------------M----------------------------" --
     * Base1 TTTTTTTTTTTTTTTTCCCCCCCCCCCCCCCCAAAAAAAAAAAAAAAAGGGGGGGGGGGGGGGG --
     * Base2 TTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGG --
     * Base3 TCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAG
     */
    private static GeneticCodeTable parseTable(String tableStr) {

        GeneticCodeTable ret = new GeneticCodeTable();
        BufferedReader r = new BufferedReader(new StringReader(tableStr));
        String line = null;
        String ncbieaa = null, sncbieaa = null, base1 = null, base2 = null, base3 = null;
        try {
            while ((line = r.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("name")) {
                    if (ret.getName() == null) {
                        String name = StrUtil.extract(NAME_REG, line);
                        ret.setName(name);
                    }
                } else if (line.startsWith("id")) {
                    String idStr = StrUtil.extract(ID_REG, line);
                    if (!idStr.isEmpty()) {
                        ret.setId(Integer.parseInt(idStr));
                    }
                } else if (line.startsWith("ncbieaa")) {
                    ncbieaa = StrUtil.extract(NCBIEAA_REG, line);
                } else if (line.startsWith("sncbieaa")) {
                    sncbieaa = StrUtil.extract(SNCBIEAA_REG, line);
                } else if (line.startsWith("-- Base1")) {
                    base1 = StrUtil.extract(BASE_REG, line);
                } else if (line.startsWith("-- Base2")) {
                    base2 = StrUtil.extract(BASE_REG, line);
                } else if (line.startsWith("-- Base3")) {
                    base3 = StrUtil.extract(BASE_REG, line);
                }
            }
            addCodons(ret, ncbieaa, sncbieaa, base1, base2, base3);
        } catch (IOException ex) {
            Logger.getLogger(TableParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    private static void addCodons(GeneticCodeTable table, String ncbieaa, String sncbieaa, String base1, String base2, String base3) {
        for (int i = 0; i < ncbieaa.length(); i++) {

            String codonStr = base1.substring(i, i + 1) + base2.substring(i, i + 1) + base3.substring(i, i + 1);
            if (sncbieaa.charAt(i) == 'M') {
            }
            if (ncbieaa.charAt(i) == '*') {
            }
            Codon codon = new Codon();
            codon.setNucleotides(codonStr);
            codon.setStartCodon(sncbieaa.charAt(i) == 'M');
            codon.setStopCodon(ncbieaa.charAt(i) == '*');
            table.add(codon, ncbieaa.charAt(i));
        }
    }

    private static StringList toTablesStr(String str) {
        StringList ret = new StringList();
        int leftBracketIndex = str.indexOf("{");
        int endIndex = 0;
        while (leftBracketIndex > -1 && endIndex > -1) {
            endIndex = StrUtil.indexOfClosingBracket(str, '{', leftBracketIndex);
            if (endIndex > -1) {
                String subStr = str.substring(leftBracketIndex + 1, endIndex);
                ret.add(subStr);
                leftBracketIndex = str.indexOf('{', endIndex);
            }
        }
        return ret;
    }
}
