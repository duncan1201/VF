package com.gas.domain.core.as.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.logging.Logger;
import org.biojava.bio.seq.SequenceIterator;
import org.biojava.bio.seq.db.HashSequenceDB;
import org.biojava.bio.seq.db.SequenceDB;
import org.biojavax.Namespace;
import org.biojavax.RichObjectFactory;
import org.biojavax.bio.seq.RichSequence;
import org.biojavax.bio.seq.RichSequence.IOTools.SingleRichSeqIterator;
import org.biojavax.bio.seq.io.EMBLFormat;
import org.biojavax.bio.seq.io.FastaFormat;
import org.biojavax.bio.seq.io.GenbankFormat;
import org.biojavax.bio.seq.io.RichSequenceFormat;
import org.biojavax.bio.seq.io.RichStreamWriter;
import com.gas.common.ui.util.FileHelper;
import com.gas.domain.core.flexrs.FlexGenbankFormat;

public class RichSequenceWriter {

    public static String toString(RichSequence rs, RichSequenceFormat format) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        SequenceDB db = new HashSequenceDB();

        try {
            db.addSequence(rs);
            if (format instanceof FlexGenbankFormat) {
                Namespace ns = RichObjectFactory.getDefaultNamespace();
                SequenceIterator in = new SingleRichSeqIterator(rs);
                RichStreamWriter sw = new RichStreamWriter(os, format);
                sw.writeStream(in, ns);
            } else if (format instanceof FastaFormat) {
                RichSequence.IOTools.writeFasta(os, rs, RichObjectFactory.getDefaultNamespace());
            } else if (format instanceof EMBLFormat) {
                Namespace ns = RichObjectFactory.getDefaultNamespace();
                SequenceIterator in = new SingleRichSeqIterator(rs);
                RichStreamWriter sw = new RichStreamWriter(os, format);
                sw.writeStream(in, ns);
            } else if (format instanceof GenbankFormat) {
                throw new IllegalArgumentException("Please use FlexGenbankFormat");
            } else {
                throw new IllegalArgumentException(String.format("Type %s not supported", format.getClass().getName()));
            }
        } catch (Exception e) {
            System.out.println();
        }

        return os.toString();
    }

    public static void toFile(RichSequence rs, RichSequenceFormat format, File file) {
        String gb = toString(rs, format);
        FileHelper.toFile(file, gb);
    }

    public static void toFile(RichSequence rs, RichSequenceFormat format, String pathname) {
        toFile(rs, format, new File(pathname));
    }
}
