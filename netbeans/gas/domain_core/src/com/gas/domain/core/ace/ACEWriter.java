/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.ace;

import com.gas.common.ui.util.CommonUtil;
import com.gas.common.ui.util.StrUtil;
import com.gas.domain.core.ace.ACE.BS;
import com.gas.domain.core.ace.ACE.Contig;
import com.gas.domain.core.ace.ACE.Read;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.openide.util.Exceptions;

/**
 *
 * @author dq
 */
public class ACEWriter {

    public static byte[] toBytes(ACE ace) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        List<Contig> contigs = ace.getContigs();
        try {
            //AS <number of contigs> <total number of reads in ace file>
            out.write("AS ".getBytes());
            out.write(Integer.toString(contigs.size()).getBytes());
            out.write(new byte[]{' '});
            out.write(ace.getTotalNumberOfReads().toString().getBytes());
            out.write(new byte[]{'\n', '\n'});

            for (Contig contig : contigs) {

                //CO <contig name> <# of bases> <# of reads in contig> <# of base segments in contig> <U or C>
                out.write("CO ".getBytes());
                out.write(contig.getName().getBytes());
                out.write(new byte[]{' '});
                out.write(Integer.toString(contig.getSequence().length()).getBytes());
                out.write(new byte[]{' '});
                out.write(Integer.toString(contig.getReads().size()).getBytes());
                out.write(new byte[]{' '});
                out.write(Integer.toString(contig.getBaseSegments().size()).getBytes());
                out.write(new byte[]{' '});
                out.write(contig.getComplemented() ? 'C' : 'U');
                out.write(new byte[]{'\n'});

                String buStr = StrUtil.breakUp(contig.getSequence(), 50);
                out.write(buStr.getBytes());
                out.write(new byte[]{'\n', '\n'});

                String bq = contig.getBaseQualities();
                String breakedUpInts = CommonUtil.breakUpInts(bq, 50);
                out.write("BQ\n".getBytes());
                out.write(breakedUpInts.getBytes());
                out.write(new byte[]{'\n', '\n'});

                Iterator<Read> readItr = contig.getReads().values().iterator();
                while (readItr.hasNext()) {
                    Read read = readItr.next();
                    //AF <read name> <C or U> <padded start consensus position>
                    out.write("AF ".getBytes());
                    out.write(read.getName().getBytes());
                    out.write(new byte[]{' '});
                    out.write(read.getComplemented() ? 'C' : 'U');
                    out.write(new byte[]{' '});
                    out.write(read.getPaddedStart().toString().getBytes());
                    out.write(new byte[]{'\n'});
                }
                out.write(new byte[]{'\n'});

                //BS <padded start consensus position> <padded end consensus position> <read name>
                Iterator<BS> bsItr = contig.getBaseSegments().iterator();
                while (bsItr.hasNext()) {
                    BS bs = bsItr.next();
                    out.write("BS ".getBytes());
                    out.write(bs.getPaddedStart().toString().getBytes());
                    out.write(bs.getPaddedEnd().toString().getBytes());
                    out.write(bs.getReadName().getBytes());
                }

                //RD <read name> <# of padded bases> <# of whole read info items> <# of read tags>
                //atcgatctatct....
                //\n
                //QA <qual clipping start> <qual clipping end> <align clipping start> <align clipping end>
                readItr = contig.getReads().values().iterator();
                while (readItr.hasNext()) {
                    Read read = readItr.next();
                    String str = read.getSequence();
                    String bu = StrUtil.breakUp(str, 50);

                    //RD <read name> <# of padded bases> <# of whole read info items> <# of read tags>
                    //atcgatcg...
                    out.write("RD ".getBytes());
                    out.write(read.getName().getBytes());
                    out.write(new byte[]{' '});
                    out.write(Integer.toString(read.getSequence().length()).getBytes());
                    out.write(" 0 0\n".getBytes());
                    out.write(bu.getBytes());
                    out.write(new byte[]{'\n', '\n'});

                    //QA <qual clipping start> <qual clipping end> <align clipping start> <align clipping end>
                    //DS ...
                    out.write("QA ".getBytes());
                    out.write(read.getQualClippingStart().toString().getBytes());
                    out.write(new byte[]{' '});
                    out.write(read.getQualClippingEnd().toString().getBytes());
                    out.write(" 0 0\n".getBytes());
                    out.write(read.getDataSource().getBytes());
                    out.write(new byte[]{'\n', '\n'});

                }

                out.write(new byte[]{'\n'});
            }

        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return out.toByteArray();
    }

    public static String toString(ACE ace) {
        String ret = null;
        byte[] bytes = toBytes(ace);
        ret = new String(bytes);

        return ret.toString();
    }

    public static boolean toFile(ACE ace, File file) {
        boolean ret = false;
        byte[] bytes = toBytes(ace);
        try {
            FileUtils.writeByteArrayToFile(file, bytes);
            ret = true;
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return ret;
    }
}
