/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.aln;

import com.gas.common.ui.util.StrUtil;
import java.io.*;
import java.util.Locale;
import java.util.StringTokenizer;
import org.openide.util.Exceptions;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IAlnIOService.class)
public class AlnIOService implements IAlnIOService {

    public boolean isAln(String content) {
        BufferedReader r = new BufferedReader(new StringReader(content));
        String line = null;
        try {
            while ((line = r.readLine()) != null) {
                line = line.trim().toUpperCase(Locale.ENGLISH);
                if (!line.isEmpty()) {
                    if (line.startsWith("CLUSTAL")) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return false;
    }

    @Override
    public Aln parse(File file) {
        Aln aln = null;
        try {
            FileInputStream stream = new FileInputStream(file);
            aln = parse(stream);
        } catch (FileNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        }
        return aln;
    }

    @Override
    public Aln parse(Class clazz, String name) {
        InputStream stream = clazz.getResourceAsStream(name);
        return parse(stream);
    }

    @Override
    public Aln parse(String str) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(str.getBytes());
        return parse(inputStream);
    }

    @Override
    public Aln parse(InputStream inputStream) {
        Aln ret = new Aln();
        BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        try {
            int nameLength;
            int basesLength = 0;
            StringTokenizer tokenizer;
            while ((line = r.readLine()) != null) {

                if (line.toUpperCase(Locale.ENGLISH).startsWith("CLUSTAL")) {
                    ret.setHeader(line);
                } else {

                    if (areBases(line)) {
                        tokenizer = new StringTokenizer(line, " ");
                        String name = tokenizer.nextToken();
                        String bases = tokenizer.nextToken();
                        basesLength = bases.length();

                        ret.add(name, bases);
                    } else {
                        if (ret.getConservationLength() < ret.getLength()) {
                            if (line.length() < basesLength) {
                                line = StrUtil.insertFront(line, ' ', basesLength - line.length());
                            } else {
                                line = line.substring(line.length() - basesLength, line.length());
                            }
                            ret.appendConservation(line);
                        }
                    }
                }

            }
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return ret;
    }

    private boolean areBases(String line) {
        StringTokenizer tokenizer = new StringTokenizer(line, " ");
        return tokenizer.countTokens() == 2 && line.indexOf("*") < 0 && line.indexOf(":") < 0 && line.indexOf(".") < 0;
    }
}
