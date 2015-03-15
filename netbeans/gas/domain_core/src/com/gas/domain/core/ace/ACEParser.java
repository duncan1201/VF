/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.ace;

import com.gas.common.ui.util.CommonUtil;
import com.gas.domain.core.ace.ACE.Contig;
import com.gas.domain.core.ace.ACE.Read;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.openide.util.Exceptions;

/**
 *
 * @author dq
 */
public class ACEParser {

    public static ACE parse(File file) {
        ACE ret = null;
        try {
            FileInputStream inputStream = new FileInputStream(file);
            ret = parse(inputStream);
        } catch (FileNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        }
        return ret;
    }

    public static ACE parse(InputStream inputStream) {
        ACE ret = null;

        boolean CO = false;
        boolean BQ = false;
        boolean RD = false;
        String curReadName = null;

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(ACETags.AS_TAG)) {
                    ret = new ACE();
                    String[] splits = line.split(" ");
                } else if (line.startsWith(ACETags.CO_TAG)) {
                    String[] splits = line.split(" ");
                    String name = splits[1];
                    String c = splits[splits.length - 1];

                    ACE.Contig contig = new ACE.Contig();
                    contig.setName(name);
                    contig.setComplemented(c.equalsIgnoreCase("c"));

                    ret.getContigs().add(contig);
                    CO = true;
                } else if (line.startsWith(ACETags.BQ_TAG)) {
                    CO = false;
                    BQ = true;
                    RD = false;
                } else if (line.startsWith(ACETags.AF_TAG)) {
                    CO = false;
                    BQ = false;
                    RD = false;

                    String[] splits = line.split(" ");
                    String name = splits[1];
                    boolean c = splits[2].equalsIgnoreCase("c");
                    Integer paddedStart = CommonUtil.parseInt(splits[3]);
                    Contig contig = ret.getLastContig();
                    if (!contig.getReads().containsKey(name)) {
                        Read read = new Read();
                        contig.getReads().put(name, read);
                    }
                    Read read = contig.getReads().get(name);
                    read.setName(name);
                    read.setComplemented(c);
                    read.setPaddedStart(paddedStart);

                } else if (line.startsWith(ACETags.BS_TAG)) {
                    // do nothing
                } else if (line.startsWith(ACETags.RD_TAG)) {
                    CO = false;
                    BQ = false;
                    RD = true;
                    String[] splits = line.split(" ");
                    curReadName = splits[1];
                } else if (line.startsWith(ACETags.DS_TAG)) {
                    CO = false;
                    BQ = false;
                    RD = false;
                    Contig contig = ret.getLastContig();
                    Read read = contig.getReads().get(curReadName);
                    read.setDataSource(line);
                } else if (line.startsWith(ACETags.QA_TAG)) {

                    String[] splits = line.split(" ");
                    Integer qualClippingStart = CommonUtil.parseInt(splits[1]);
                    Integer qualClippingEnd = CommonUtil.parseInt(splits[2]);
                    Integer alignClippingStart = CommonUtil.parseInt(splits[3]);
                    Integer alignClippingEnd = CommonUtil.parseInt(splits[4]);

                    Read read = ret.getLastContig().getReads().get(curReadName);
                    read.setQualClippingStart(qualClippingStart);
                    read.setQualClippingEnd(qualClippingEnd);
                    read.setAlignClippingStart(alignClippingStart);
                    read.setAlignClippingEnd(alignClippingEnd);
                } else { // 
                    if (CO) {
                        if (!line.trim().isEmpty()) {
                            String cSeq = ret.getLastContig().getSequence();
                            ret.getLastContig().setSequence(cSeq + line.trim());
                        } else {
                            CO = false;
                            BQ = false;
                            RD = false;
                        }
                    } else if (BQ) {
                        if (!line.trim().isEmpty()) {
                            String cBq = ret.getLastContig().getBaseQualities();
                            ret.getLastContig().setBaseQualities(cBq + ' ' + line);
                        } else {
                            CO = false;
                            BQ = false;
                            RD = false;
                        }
                    } else if (RD) {
                        if (!line.trim().isEmpty()) {
                            Contig contig = ret.getLastContig();
                            Read read = contig.getReads().get(curReadName);
                            String cSeq = read.getSequence();
                            cSeq = cSeq == null ? "" : cSeq;
                            read.setSequence(cSeq + line.trim());
                        } else {
                            CO = false;
                            BQ = false;
                            RD = false;
                        }
                    }
                }
            }
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return ret;
    }
}
