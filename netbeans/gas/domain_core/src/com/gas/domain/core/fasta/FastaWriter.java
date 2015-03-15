/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.fasta;

import com.gas.common.ui.util.StrUtil;
import com.gas.domain.core.fasta.Fasta;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.openide.util.Exceptions;

/**
 *
 * @author dq
 */
public class FastaWriter {

    public static <T> T to(Fasta fasta, Class<T> retType) {
        return to(fasta, 60, retType);
    }

    public static <T> T to(Fasta fasta, int basePerLine, Class<T> retType) {
        T ret = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            Iterator<Fasta.Record> itr = fasta.getRecords().iterator();
            while (itr.hasNext()) {
                Fasta.Record record = itr.next();
                outputStream.write('>');
                outputStream.write(record.getDefinitionLine().getData().getBytes());

                outputStream.write('\n');
                List<String> seqList = StrUtil.toList(record.getSequence(), basePerLine);
                for (String sub : seqList) {
                    outputStream.write(sub.getBytes());
                    outputStream.write('\n');
                }

            }
        } catch (IOException ioe) {
            Exceptions.printStackTrace(ioe);
        }

        if (retType.isAssignableFrom(String.class)) {
            ret = (T) outputStream.toString();
        } else if (retType.isAssignableFrom(byte[].class)) {
            ret = (T) outputStream.toByteArray();
        } else if (retType.isAssignableFrom(ByteArrayOutputStream.class)) {
            ret = (T) outputStream;
        } else {
            throw new IllegalArgumentException(String.format("Class '%s' not supported", retType.toString()));
        }
        return ret;
    }
}
