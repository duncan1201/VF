/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.as.util;

import com.gas.domain.core.as.FetureKey;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dq
 */
public class FetureKeyParser {

    private static final String OPTIONAL_Q = "Optional qualifiers";
    private static final String M_Q = "Mandatory qualifiers";

    public static List<FetureKey> parse(File file) {
        List<FetureKey> ret = new ArrayList<FetureKey>();
        try {
            FileInputStream inputStream = new FileInputStream(file);
            ret = parse(inputStream);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FetureKeyParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    public static List<FetureKey> parse(InputStream inputStream) {
        List<FetureKey> ret = new ArrayList<FetureKey>();
        BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        try {
            FetureKey fk = null;
            boolean qualifierBlock = false;
            line = r.readLine();
            while (line != null) {
                line = line.trim();
                if (line.startsWith("Feature Key")) {
                    //System.out.println(i++ + "" + line);
                    String name = line.substring("Feature Key".length()).trim();

                    if (fk != null) {
                        ret.add(fk);
                    }

                    fk = new FetureKey();
                    fk.setName(name);

                } else if (line.startsWith(OPTIONAL_Q) || line.startsWith(M_Q)) {
                    qualifierBlock = true;
                    String q = line.substring(line.indexOf('/') + 1, line.indexOf('=')).trim();
                    if (fk != null) {
                        fk.getQualifiers().add(q);
                    }
                } else if (line.startsWith("/") && qualifierBlock) {
                    int startIndex = line.indexOf("/") + 1;
                    int endIndex = line.indexOf("=");
                    if (endIndex < 0) {
                        endIndex = line.length();
                    }
                    String q = line.substring(startIndex, endIndex).trim();

                    if (fk != null) {
                        fk.getQualifiers().add(q);
                    }
                } else if (line.isEmpty()) {
                    qualifierBlock = false;
                }
                line = r.readLine();
            }
        } catch (IOException ex) {
            Logger.getLogger(FetureKeyParser.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ret;
    }
}
