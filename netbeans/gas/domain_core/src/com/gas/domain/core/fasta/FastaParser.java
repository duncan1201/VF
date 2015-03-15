/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.fasta;

import java.io.*;
import org.openide.util.Exceptions;

/**
 *
 * @author dq
 */
public class FastaParser {

    public Fasta parse(File file) {
        Fasta ret = null;
        try {
            FileInputStream in = new FileInputStream(file);
            ret = parse(in);
        } catch (FileNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        }
        return ret;
    }

    public Fasta parse(Class clazz, String name) {
        return parse(clazz.getResourceAsStream(name));
    }

    public Fasta parse(byte[] bytes) {
        ByteArrayInputStream s = new ByteArrayInputStream(bytes);
        return parse(s);
    }

    public Fasta parse(InputStream in) {
        Fasta ret = new Fasta();
        BufferedReader r = new BufferedReader(new InputStreamReader(in));
        String line = null;
        try {
            String header = null;
            while ((line = r.readLine()) != null) {
                line = line.trim();
                if (line.startsWith(">")) {
                    header = line.substring(1).trim();
                } else if (!line.isEmpty()) {
                    ret.add(header, line);
                }
            }
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return ret;
    }
}
