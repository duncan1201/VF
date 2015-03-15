/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.resources;

import com.gas.common.ui.core.StringList;
import com.gas.common.ui.util.CipherUtil;
import com.gas.common.ui.util.FileHelper;
import com.gas.common.ui.util.StrUtil;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.Comment;
import com.gas.domain.core.as.util.AnnotatedSeqParser;
import com.gas.domain.core.as.util.AnnotatedSeqWriter;
import com.gas.domain.core.flexrs.FlexGenbankFormat;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.openide.util.Exceptions;

/**
 *
 * @author dq
 */
class Exporter {

    private File dir = null;
    private File dirOut = null;
    final String[] dirs = {
        "gateway\\entry",
        "gateway\\dest",
        "gateway\\donor",
        "topo\\ta",
        "topo\\dir",
        "topo\\blunt",
        "novagen\\duet",
        "novagen\\pet"
    };

    public Exporter(File dir, File dirOut) {
        this.dir = dir;
        this.dirOut = dirOut;
    }

    String createNameList() {
        StringBuilder ret = new StringBuilder();
        for (String dir : dirs) {
            List<File> files = getFiles(dir);
            StringList nameList = new StringList();
            for (File file : files) {
                AnnotatedSeq as = AnnotatedSeqParser.singleParse(file, new FlexGenbankFormat());

                String name = as.getName().trim();
                if (name.startsWith("\\")) {
                    name = name.substring(1);
                }
                if (name.endsWith("\\")) {
                    name = name.substring(0, name.length() - 1);
                }
                nameList.add(name);
            }
            ret.append(dir);
            ret.append("(" + nameList.size() + ")");
            ret.append("----------------\n");
            Collections.sort(nameList);
            for (String n : nameList) {
                ret.append(n);
                ret.append('\n');
            }
            ret.append('\n');
        }
        return ret.toString();
    }

    void export() {
        for (String dir : dirs) {
            List<AnnotatedSeq> ret = new ArrayList<AnnotatedSeq>();
            List<File> files = getFiles(dir);
            for (File file : files) {
                List<AnnotatedSeq> asList = AnnotatedSeqParser.parse(file, new FlexGenbankFormat());
                ret.addAll(asList);
            }
            postprocess(ret);
            write(dir, ret);
        }
    }

    private void write(String dirStr, List<AnnotatedSeq> asList) {
        File dir = new File(dirOut, dirStr);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        for (AnnotatedSeq as : asList) {
            String name = as.getName();
            name = StrUtil.replaceAll(name, "\\", "-");
            name = StrUtil.replaceAll(name, "/", "-");
            File file = new File(dir, String.format("%s.resource", name));
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
            String str = AnnotatedSeqWriter.toString(as);
            String encoded = CipherUtil.toHex(str.getBytes(Charset.forName("UTF-8")));
            FileHelper.toFile(file, encoded);
        }
    }

    private void postprocess(List<AnnotatedSeq> asList) {
        for (AnnotatedSeq as : asList) {
            Comment comment = as.getComment();

            comment.setData("This file is created by VectorFriends http://www.vectorfriends.com");
            as.getFetureSet().removeQualifierKeys("vntifkey", "invitrogen");

        }
    }

    private List<File> getFiles(String d) {

        File dirGateway = new File(dir, d);

        List<File> ret = new ArrayList<File>();
        File[] files = null;

        files = dirGateway.listFiles();
        for (File file : files) {
            String ext = FileHelper.getExt(file);
            if (!ext.equalsIgnoreCase("java")) {
                ret.add(file);
            }
        }

        return ret;
    }
}
