/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.nexus;

import com.gas.common.ui.util.StrUtil;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author dq
 */
public class NexusIOUtil {

    public static List<String> getBlockStrs(Class clazz, String name) {
        return getBlockStrs(clazz.getResourceAsStream(name));
    }

    public static String getCmdName(String statementStr) {
        String cmdName = StrUtil.extract("(\\S+)\\W", statementStr);
        if (cmdName == null) {
            cmdName = "";
        }
        return cmdName;
    }

    public static String getArgs(String statementStr) {
        statementStr = statementStr.trim();
        if (statementStr.isEmpty()) {
            return "";
        }
        if (statementStr.endsWith(";")) {
            statementStr = statementStr.substring(0, statementStr.length() - 1);
        }
        String cmd = getCmdName(statementStr);
        int index = statementStr.indexOf(cmd);
        if (index < 0) {
            return "";
        }
        return statementStr.substring(index + cmd.length());
    }
    
    public void abc(Map<String, String> seqs){
        StringBuilder ret = new StringBuilder();
        ret.append("");
        Iterator<String> itr = seqs.keySet().iterator();
        while(itr.hasNext()){
            String key = itr.next();
            String seq = seqs.get(key);
            
        }
    }

    public static List<String> getBlockStrs(InputStream inputStream) {
        String str = StrUtil.toString(inputStream);
        List<String> ret = getBlocks(str);

        return ret;
    }

    public static String getBlockName(String blockStr) {
        String reg = "begin(.+);";
        String ret = StrUtil.extract(reg, blockStr);
        return ret;
    }

    public static BlkStmt parseBlkStatement(String statementStr) {
        BlkStmt ret = new BlkStmt();
        statementStr = statementStr.trim();
        if (statementStr.isEmpty()) {
            return ret;
        }
        if (statementStr.endsWith(";")) {
            statementStr = statementStr.substring(0, statementStr.length() - 1);
        }
        return ret;
    }

    public static List<String> getStatements(String blockStr) {
        List<String> ret = new ArrayList<String>();
        String reg = ";$";
        Pattern pattern = Pattern.compile(reg, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(blockStr);
        int stmtStart = 0;
        int stmtEnd;
        while (matcher.find()) {
            stmtEnd = matcher.end();
            String stmt = blockStr.substring(stmtStart, stmtEnd);
            stmt = stmt.trim();
            if (!stmt.toUpperCase(Locale.ENGLISH).startsWith("END") && !stmt.toUpperCase(Locale.ENGLISH).startsWith("BEGIN")) {
                ret.add(stmt);
            }
            stmtStart = stmtEnd;
        }
        return ret;
    }

    public static List<String> getBlocks(String nexusStr) {
        List<String> ret = new ArrayList<String>();
        final String BEGIN_REG = "begin.+;";
        final String END_REG = "end;";
        Pattern beginPattern = Pattern.compile(BEGIN_REG, Pattern.CASE_INSENSITIVE);
        Matcher beginMatcher = beginPattern.matcher(nexusStr);

        Pattern endPattern = Pattern.compile(END_REG, Pattern.CASE_INSENSITIVE);
        Matcher endMatcher = endPattern.matcher(nexusStr);
        int beginStart = 0;
        while (beginMatcher.find(beginStart)) {
            int beginMatchedStart = beginMatcher.start();
            if (endMatcher.find(beginMatchedStart)) {
                int endMatchedEnd = endMatcher.end();
                ret.add(nexusStr.substring(beginMatchedStart, endMatchedEnd));
                beginStart = endMatchedEnd;
            }
        }
        return ret;
    }
}
