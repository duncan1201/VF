package com.gas.domain.core.tasm;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.openide.util.Exceptions;

public class TasmParser {

    private final static Logger logger = Logger.getLogger(TasmParser.class.getName());
    // for contig
    private static final String SEQUENCE = "sequence";
    private static final String LSEQUENCE = "lsequence"; // for reads also
    private static final String QUALITY = "quality";
    private static final String ASMBL_ID = "asmbl_id";
    private static final String REDUNDANCY = "redundancy";
    private static final String PERC_N = "perc_N";
    private static final String SEQ_NO = "seq#";
    private static final String SEPERATOR = "|";
    private final static List<String> CONTIG_TAGS = new ArrayList<String>();

    static {
        CONTIG_TAGS.add(SEQUENCE);
        CONTIG_TAGS.add(LSEQUENCE);
        CONTIG_TAGS.add(QUALITY);
        CONTIG_TAGS.add(ASMBL_ID);
        CONTIG_TAGS.add(REDUNDANCY);
        CONTIG_TAGS.add(PERC_N);
        CONTIG_TAGS.add(SEQ_NO);
        CONTIG_TAGS.add(SEPERATOR);
    }
    // for reads
    public static final String SEQ_NAME = "seq_name";
    public static final String ASM_LEND = "asm_lend";
    public static final String ASM_REND = "asm_rend";
    public static final String SEQ_LEND = "seq_lend";
    public static final String SEQ_REND = "seq_rend";
    public static final String BEST = "best";
    public static final String OFFSET = "offset";

    public static List<Condig> parse(byte[] bytes) {
        List<Condig> ret = new ArrayList<Condig>();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        LineIterator itr = IOUtils.lineIterator(reader);
        List<String> lines = new ArrayList<String>();
        while (itr.hasNext()) {
            String line = itr.next();
            if (line.startsWith(SEPERATOR)) {
                Condig contig = parseContig(lines);
                if (contig != null) {
                    ret.add(contig);
                }
                lines.clear();
            } else {
                lines.add(line);
            }
        }
        if (lines.size() > 0) {
            Condig contig = parseContig(lines);
            if (contig != null) {
                ret.add(contig);
            }
        }
        return ret;
    }

    public static List<Condig> parse(File file) {
        List<Condig> ret = new ArrayList<Condig>();
        try {
            byte[] bytes = FileUtils.readFileToByteArray(file);
            ret = parse(bytes);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        return ret;
    }

    private static Condig parseContig(List<String> lines) {
        Condig ret = null;
        Rid read = null;
        for (String line : lines) {
            if (line.startsWith(SEQUENCE)) {
                ret = new Condig();
                String sub = line.substring(SEQUENCE.length()).trim();

                ret.setSequence(sub);
            } else if (line.startsWith(LSEQUENCE)) {
                String sub = line.substring(LSEQUENCE.length()).trim();

                if (ret.getLsequence() == null) {
                    ret.setLsequence(sub);
                } else if (read != null && read.getLsequence() == null) {
                    read.setLsequence(sub);
                    ret.getRids().add(read);
                } else {
                    logger.severe("LSEQUENCE ERROR");
                }
            } else if (line.startsWith(QUALITY)) {
                String sub = line.substring(QUALITY.length()).trim().substring(2);

                List<Integer> quals = new ArrayList<Integer>();
                for (int i = 0; i + 2 <= sub.length(); i += 2) {
                    int q = Integer.parseInt(sub.substring(i, i + 2), 16);
                    quals.add(q);
                }
                ret.setQualities(quals);
            } else if (line.startsWith(ASMBL_ID)) {
                String sub = line.substring(ASMBL_ID.length()).trim();
                ret.setAsmblId(Integer.parseInt(sub));
            } else if (line.startsWith(REDUNDANCY)) {
                String sub = line.substring(REDUNDANCY.length()).trim();
                ret.setRedundancy(Float.parseFloat(sub));
            } else if (line.startsWith(PERC_N)) {
                String sub = line.substring(PERC_N.length()).trim();
                ret.setPercentN(Float.parseFloat(sub));
            } else if (line.startsWith(SEQ_NO)) {
                String sub = line.substring(SEQ_NO.length()).trim();
                ret.setSeqNO(Integer.parseInt(sub));
            } else if (line.startsWith(SEQ_NAME)) {
                read = new Rid();
                String sub = line.substring(SEQ_NAME.length()).trim();
                read.setSeqName(sub);

            } else if (line.startsWith(ASM_LEND)) {
                String sub = line.substring(ASM_LEND.length()).trim();
                read.setAsm_lend(Integer.parseInt(sub));
            } else if (line.startsWith(ASM_REND)) {
                String sub = line.substring(ASM_REND.length()).trim();
                read.setAsm_rend(Integer.parseInt(sub));
            } else if (line.startsWith(SEQ_LEND)) {
                String sub = line.substring(SEQ_LEND.length()).trim();
                read.setSeq_lend(Integer.parseInt(sub));
            } else if (line.startsWith(SEQ_REND)) {
                String sub = line.substring(SEQ_REND.length()).trim();
                read.setSeq_rend(Integer.parseInt(sub));
            } else if (line.startsWith(BEST)) {
                String sub = line.substring(BEST.length()).trim();
                read.setBest(Integer.parseInt(sub));
            } else if (line.startsWith(OFFSET)) {
                String sub = line.substring(OFFSET.length()).trim();
                read.setOffset(Integer.parseInt(sub));
            }
        }
        return ret;
    }
}
