package com.gas.domain.core.as.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.biojavax.bio.seq.io.RichSequenceFormat;

import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.flexrs.IFlexRichSequence;
import java.io.ByteArrayInputStream;

public class AnnotatedSeqParser {

    public static <T> List<T> parse(String str, RichSequenceFormat format, boolean includeSeqData, Class<T> clazz) {
        if (!clazz.isAssignableFrom(AnnotatedSeq.class)) {
            throw new IllegalArgumentException(String.format("class '%s' not supported", clazz.toString()));
        }
        List<T> ret = new ArrayList<T>();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(str.getBytes());
        List<AnnotatedSeq> asList = parse(inputStream, format);
        for (AnnotatedSeq as : asList) {
            ret.add((T) as);
        }
        return ret;
    }

    public static <T> List<T> parse(String str, RichSequenceFormat format, Class<T> clazz) {
        return parse(str, format, true, clazz);
    }

    public static List<AnnotatedSeq> parse(File file, RichSequenceFormat format) {
        List<AnnotatedSeq> ret = new ArrayList<AnnotatedSeq>();
        try {
            FileInputStream inputStream = new FileInputStream(file);
            ret = parse(inputStream, format);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AnnotatedSeqParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    public static AnnotatedSeq singleParse(File file, RichSequenceFormat format, String accession) {
        AnnotatedSeq ret = null;
        List<AnnotatedSeq> seqList = parse(file, format);
        for (AnnotatedSeq as : seqList) {
            if (as.getAccession().equalsIgnoreCase(accession)) {
                ret = as;
                break;
            }
        }
        return ret;
    }

    public static List<AnnotatedSeq> parse(String str, RichSequenceFormat format) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(str.getBytes());
        return parse(inputStream, format);
    }

    public static List<AnnotatedSeq> parse(InputStream inputStream, RichSequenceFormat format) {
        List<AnnotatedSeq> ret = new ArrayList<AnnotatedSeq>();
        List<IFlexRichSequence> rsList = RichSequenceParser.parse(inputStream, format);
        for (IFlexRichSequence rs : rsList) {
            String name = rs.getName();
            AnnotatedSeq as = FlexRichSequence2AnnotatedSeqConverter.to(name, new Date(), new Date(), rs);
            ret.add(as);
        }
        return ret;
    }

    public static AnnotatedSeq singleParse(Class clazz, String resource, RichSequenceFormat format) {
        InputStream inputStream = clazz.getResourceAsStream(resource);
        return singleParse(inputStream, format);
    }

    public static AnnotatedSeq singleParse(File file, RichSequenceFormat format) {
        AnnotatedSeq ret = null;
        try {
            FileInputStream inputStream = new FileInputStream(file);
            ret = singleParse(inputStream, format);
        } catch (Exception e) {
            e.getStackTrace();
        }
        return ret;
    }

    public static AnnotatedSeq singleParse(InputStream inputStream, RichSequenceFormat format) {
        List<AnnotatedSeq> list = parse(inputStream, format);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }
}
