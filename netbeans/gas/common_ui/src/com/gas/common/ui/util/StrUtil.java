package com.gas.common.ui.util;

import com.gas.common.ui.core.CharList;
import com.gas.common.ui.core.IntList;
import com.gas.common.ui.core.StringList;
import java.awt.FontMetrics;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openide.util.Exceptions;

public class StrUtil {

    public static String trim(String str, Integer length) {
        String ret;
        if (length != null && str.length() > length) {
            ret = str.substring(0, length);
        } else {
            ret = str;
        }
        return ret;
    }

    public static String toString(InputStream inputStream) {
        StringBuilder ret = new StringBuilder();
        InputStreamReader reader = new InputStreamReader(inputStream);
        try {
            int data = reader.read();
            while (data != -1) {
                char c = (char) data;
                ret.append(c);
                data = reader.read();
            }
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return ret.toString();
    }

    public static int indexOf(String str, CharList charList) {
        int ret = -1;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            boolean contains = charList.containsIgnoreCase(c);
            if (contains) {
                ret = i;
                break;
            }
        }
        return ret;
    }

    public static String createString(String str, int repetition) {
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < repetition; i++) {
            ret.append(str);
        }
        return ret.toString();
    }

    public static String getNewName(String proposedName, StringList existingNames) {
        StringBuilder ret = new StringBuilder();
        StringList with = existingNames.startsWith(proposedName);
        if (with.isEmpty()) {
            ret.append(proposedName);
        } else {
            Integer numMax = null;
            for (String str : with) {
                int startIndex = str.lastIndexOf("(");
                int endIndex = str.lastIndexOf(")");
                if (startIndex < endIndex && startIndex > -1 && endIndex > -1) {
                    String numStr = str.substring(startIndex + 1, endIndex);
                    Integer num = CommonUtil.parseInt(numStr);
                    if (num != null) {
                        if (numMax == null || num > numMax) {
                            numMax = num;
                        }
                    }
                }
            }
            ret.append(String.format("%s(%d)", proposedName, numMax == null ? 1 : numMax + 1));

        }
        return ret.toString();
    }

    /**
     * @return 1-based
     */
    public static String delete(String orginal, int start, int end) {
        StringBuilder ret = new StringBuilder();
        if (start <= end) {
            ret.append(orginal.substring(0, start - 1));
            ret.append(orginal.substring(end));
        } else {
            ret.append(orginal.substring(end, start - 1));
        }
        return ret.toString();
    }

    /**
     * 1-based
     *
     * @param pos: >=1 && pos <= original.size + 1
     */
    public static String insert(String original, int pos, String a) {
        if (pos > original.length() + 1) {
            throw new IllegalArgumentException(String.format("Out of bounds: %d", pos));
        }
        StringBuilder ret = new StringBuilder();
        ret.append(original.substring(0, pos - 1));
        ret.append(a);
        if ((pos - 1) < original.length()) {
            ret.append(original.substring(pos - 1));
        }
        return ret.toString();
    }

    /**
     * @param start 1-based
     * @param end 1-based
     */
    public static String sub(String original, int startPos, int endPos) {
        StringBuilder ret = new StringBuilder();
        if (startPos <= endPos) {
            ret.append(original.substring(startPos - 1, endPos));
        } else {
            ret.append(original.substring(startPos - 1, original.length()));
            ret.append(original.substring(0, endPos));
        }
        return ret.toString();
    }

    public static String replaceAll(String str, String target, String replacement) {
        int index = str.indexOf(target);
        while (index > -1) {
            str = str.replace(target, replacement);
            index = str.indexOf(target);
        }
        return str;
    }

    /**
     * @param start 1-based
     * @param end 1-based
     * need to do the insertion first, then the deletion; otherwise, exception
     */
    public static String replace(String original, int start, int end, String replacement) {
        String ret = insert(original, end + 1, replacement);

        if (start <= end) {
            ret = delete(ret, start, end);
        } else {
            ret = delete(ret, start + replacement.length(), end);
        }
        return ret;
    }

    public static boolean isLowerCase(String str) {
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            boolean lowerCase = Character.isLowerCase(c);
            if (!lowerCase) {
                return false;
            }
        }
        return true;
    }

    public static boolean isUpperCase(String str) {
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            boolean uppercase = Character.isUpperCase(c);
            if (!uppercase) {
                return false;
            }
        }
        return true;
    }

    public static IntList getIndices(String str, char target) {
        return getIndices(str, target, 0, str.length() - 1);
    }

    public static IntList getIndices(String str, char target, int startIndex) {
        return getIndices(str, target, startIndex, str.length() - 1);
    }

    public static IntList getIndices(String str, char target, int startIndex, int endIndex) {
        IntList ret = new IntList();
        for (int i = startIndex; i < str.length() || i <= endIndex; i++) {
            char ch = str.charAt(i);
            if (ch == target) {
                ret.add(i);
            }
        }
        return ret;
    }

    /**
     *
     */
    public static float getSimilarity(final String one, final String another) {
        if (one.length() != another.length()) {
            throw new IllegalArgumentException("Lengths must be the same");
        }
        int count = 0;
        for (int i = 0; i < one.length(); i++) {
            one.substring(i, i + 1);
            final String a = one.substring(i, i + 1);
            final String b = another.substring(i, i + 1);
            if (a.equalsIgnoreCase(b)) {
                count++;
            }
        }
        float ret = count * 1.0f / one.length();
        return ret;
    }

    public static String getStr4Display(String str, FontMetrics fm, int maxStrWidth) {
        String ret = str;
        int strWidth = fm.stringWidth(ret);
        while (strWidth > maxStrWidth) {
            int index = ret.indexOf(Unicodes.HORIZONTAL_ELLIPSIS);
            if (index < 0) {
                ret = ret.substring(0, ret.length() - 1);
            } else {
                ret = ret.substring(0, index);
            }
            ret = ret + Unicodes.HORIZONTAL_ELLIPSIS;
            strWidth = fm.stringWidth(ret);
        }
        return ret;
    }
    
    public static String removeBracketContent(String str, Character bracket){
        int indexStart = str.indexOf(bracket.toString());        
        
        while(indexStart > -1){
            Integer indexEnd = indexOfClosingBracket(str, bracket, indexStart);
            if(indexEnd == null){
                return str;
            }
            if(indexEnd + 1 > str.length() - 1){
                str = str.substring(0, indexStart) ;
            }else{
                str = str.substring(0, indexStart) + str.substring(indexEnd + 1);
            }
            indexStart = str.indexOf("[");
        }
        return str;
    }

    /*
     * @param openBracketIndex: the index of the open bracket
     */
    public static Integer indexOfClosingBracket(String content, final char openBracket, final int openBracketIndex) {
        char[] openBrackets = {'(', '[', '{'};
        char[] closingBrackets = {')', ']', '}'};
        Integer ret = null;

        if (content.isEmpty()) {
            return ret;
        }
        Character closingBracket = null;
        for (int i = 0; i < openBrackets.length; i++) {
            if (openBracket == openBrackets[i]) {
                closingBracket = closingBrackets[i];
                break;
            }
        }
        if (closingBracket == null) {
            throw new IllegalArgumentException(openBracket + " not supported");
        }

        int openCount = 0;
        int closingCount = 0;
        for (int i = openBracketIndex + 1; i < content.length(); i++) {
            char c = content.charAt(i);
            if (openCount == closingCount) {
                if (c == closingBracket) {
                    ret = i;
                    break;
                } else if (c == openBracket) {
                    openCount++;
                }
            } else {
                if (c == closingBracket) {
                    closingCount++;
                } else if (c == openBracket) {
                    openCount++;
                }
            }
        }
        return ret;
    }

    public static String extract(String regex, String line) {
        return extract(regex, line, null);
    }
    
    public static int[] indexOfReg(String regex, String line){
        return indexOfReg(regex, line, true);
    }
    
    public static int[] indexOfReg(String regex, String line, boolean caseInsensitive){
        IntList ret = new IntList();
        Pattern pattern ;
        if(caseInsensitive){
            pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        }else{
            pattern = Pattern.compile(regex);
        }
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            
            ret.add(start);
            ret.add(end);      
        }
        return ret.toPrimitiveArray();
    }

    public static String extract(String regex, String line, Integer flag) {
        if (regex == null || line == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }
        String ret = null;
        Pattern pattern;
        if (flag != null) {
            pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | flag);
        } else {
            pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        }
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            int groupCount = matcher.groupCount();
            ret = matcher.group(groupCount);
            if (ret != null) {
                ret = ret.trim();
            }
        }
        return ret;
    }

    public static String reverse(String s) {
        StringBuilder ret = new StringBuilder(s);
        ret.reverse();
        return ret.toString();
    }

    public static String toString(List<Character> chars) {
        StringBuilder ret = new StringBuilder();
        for (Character c : chars) {
            ret.append(c);
        }
        return ret.toString();
    }

    public static String toString(List<String> list, String delimiter) {
        StringBuilder ret = new StringBuilder();
        Iterator<String> itr = list.iterator();
        while (itr.hasNext()) {
            String str = itr.next();
            ret.append(str);
            if (itr.hasNext()) {
                ret.append(delimiter);
            }
        }
        return ret.toString();
    }

    public static boolean containsIgnoreCase(String str, String target) {
        return str.toUpperCase(Locale.ENGLISH).contains(target.toUpperCase(Locale.ENGLISH));
    }

    public static boolean contains(String[] strs, String target, boolean ignoreCase) {
        boolean ret = false;
        for (String s : strs) {
            if (ignoreCase) {
                if (s.equalsIgnoreCase(target)) {
                    ret = true;
                    break;
                }
            } else if (s.equals(target)) {
                ret = true;
                break;
            }
        }
        return ret;
    }

    /*
     * @param startColumn 1-based @param endColumn 1-based
     */
    public static void insert(StringBuilder builder, String data, int startColumn, int endColumn, boolean leftJustified) {
        int charCount = builder.length();
        if (charCount < startColumn - 1) {
            char[] blanks = new char[startColumn - charCount - 1];
            Arrays.fill(blanks, ' ');
            builder.append(blanks);
            builder.append(data);
        } else if (charCount == startColumn - 1) {
            builder.append(data);
        } else {
            throw new IllegalArgumentException(String.format("charCount %d > startColumn %d", charCount, startColumn));
        }
    }

    public static <T> T substring(String str, int beginIndex, int endIndex, Class<T> retType) {
        return substring(str, beginIndex, endIndex, retType, true, true);
    }

    public static void replace(StringBuilder builder, Object content, int startIndex, int endIndex, boolean leftAlign) {
        if (content == null) {
            return;
        }
        replace(builder, content.toString(), startIndex, endIndex, leftAlign);
    }

    /*
     * @param startIndex: 1-based @param endIndex: 1-based
     */
    public static void replace(StringBuilder builder, String content, int startIndex, int endIndex, boolean leftAlign) {
        if (content == null) {
            return;
        }

        startIndex--;
        endIndex--;
        if (!leftAlign) {
            int newSize = endIndex - startIndex + 1;
            if (newSize > content.length()) {
                String blanks = blanks(newSize - content.length());
                content = blanks + content;
            }
        }
        if (startIndex > builder.length()) {
            char[] blanks = new char[startIndex - builder.length()];
            Arrays.fill(blanks, ' ');
            builder.append(blanks);
        }
        builder.replace(startIndex, endIndex, content);
    }

    private static String blanks(int num) {
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < num; i++) {
            ret.append(' ');
        }
        return ret.toString();
    }

    /*
     * @param beginIndex: 0-based, inclusive @param endIndex: 0-based, exclusive
     */
    public static <T> T substring(String str, Integer beginIndex, Integer endIndex, Class<T> retType, boolean zeroBased, boolean endExclusive) {
        T ret = null;
        if (!zeroBased) {
            beginIndex--;
            if (endIndex != null) {
                endIndex--;
            }
        }
        if (!endExclusive && endIndex != null) {
            endIndex++;
        }
        String subStr = null;
        if (endIndex != null) {
            subStr = str.substring(beginIndex, endIndex).trim();
        } else {
            subStr = str.substring(beginIndex).trim();
        }
        if (retType.isAssignableFrom(Integer.class)) {
            ret = (T) CommonUtil.parseInt(subStr);
        } else if (retType.isAssignableFrom(String.class)) {
            ret = (T) subStr;
        } else if (retType.isAssignableFrom(Character.class)) {
            Character ch = null;
            if (subStr.length() > 0) {
                ch = subStr.charAt(0);
            }
            ret = (T) ch;
        } else if (retType.isAssignableFrom(Float.class)) {
            Float ft = null;
            if (subStr.length() > 0) {
                ft = CommonUtil.parseFloat(subStr);
            }
            ret = (T) ft;
        } else {
            throw new IllegalArgumentException(String.format("Class '%s' not supported", retType.getName()));
        }
        return ret;
    }

    public static String insertFront(String str, char x, int num) {
        StringBuilder ret = new StringBuilder(str);
        ret = ret.reverse();
        for (int i = 0; i < num; i++) {
            ret.append(x);
        }
        ret = ret.reverse();
        return ret.toString();
    }

    public static String[] toUppercase(String... strs) {
        String[] ret = new String[strs.length];
        for (int i = 0; i < strs.length; i++) {
            String str = strs[i];
            ret[i] = str.toUpperCase(Locale.ENGLISH);
        }
        return ret;
    }

    public static String append(String str, char x, int num) {
        StringBuilder ret = new StringBuilder(str);
        for (int i = 0; i < num; i++) {
            ret.append(x);
        }
        return ret.toString();
    }

    public static String breakUp(String s, int perLine) {
        StringBuilder ret = new StringBuilder();
        Iterator<String> itr = toList(s, perLine).iterator();
        while (itr.hasNext()) {
            String line = itr.next();
            ret.append(line);
            if (itr.hasNext()) {
                ret.append('\n');
            }
        }
        return ret.toString();
    }

    public static String toMultiLine(String content, int perLine) {
        return toMultiLine(null, content, perLine);
    }

    public static String toMultiLine(String title, String content, int perLine) {
        StringBuilder ret = new StringBuilder();
        int length = 0;
        ret.append("<html>");
        if (title != null) {
            ret.append("<b>");
            ret.append(title);
            length += title.length();
            ret.append("</b>");
            ret.append(':');
            length += 1;
        }
        String[] splits = content.split(" ");

        for (String split : splits) {
            ret.append(split);
            ret.append(' ');
            length += split.length() + 1;
            if (length >= perLine) {
                ret.append("<br>");
                length = 0;
            }
        }
        ret.append("</html>");
        return ret.toString();
    }

    public static String toHtmlList(String... strs) {
        StringList strList = new StringList(strs);
        return toHtmlList(strList);
    }

    public static String toHtmlList(Collection<String> strs) {
        StringBuilder ret = new StringBuilder();
        if (!strs.isEmpty()) {
            ret.append("<ul>");
        }
        Iterator<String> itr = strs.iterator();
        while (itr.hasNext()) {
            String str = itr.next();
            ret.append("<li>");
            ret.append(str);
            ret.append("</li>");
        }
        if (!strs.isEmpty()) {
            ret.append("</ul>");
        }
        return ret.toString();
    }

    public static List<String> toList(String s, int perLine) {
        List<String> ret = new ArrayList<String>();

        for (int i = 0; i < s.length();) {
            int endIndex = Math.min(i + perLine, s.length());
            String subStr = s.substring(i, endIndex);
            ret.add(subStr);
            i += perLine;
        }
        return ret;
    }

    public static String firstToken(String line, String delimiter) {
        int index = line.indexOf(delimiter);
        if (index < 0) {
            return line;
        } else {
            return line.substring(0, index);
        }
    }

    public static List<String> tokenize(String str, String delimiter) {
        return tokenize(str, delimiter, false);
    }

    public static List<String> tokenize(String line, String delimiter, boolean includeDelim) {
        List<String> ret = new ArrayList<String>();
        StringTokenizer tokenizer = new StringTokenizer(line, delimiter, includeDelim);
        while (tokenizer.hasMoreTokens()) {
            String str = tokenizer.nextToken();
            ret.add(str);
        }
        return ret;
    }

    public static String uppercaseFirstLetter(String str) {
        StringBuilder ret = new StringBuilder();
        if (!str.isEmpty()) {
            ret.append(str.substring(0, 1).toUpperCase());
        }
        if (str.length() > 1) {
            ret.append(str.substring(1));
        }
        return ret.toString();
    }

    public static String capitalize(String str) {
        StringBuilder ret = new StringBuilder();
        if (!str.isEmpty()) {
            ret.append(str.substring(0, 1).toUpperCase());
        }
        if (str.length() > 1) {
            ret.append(str.substring(1).toLowerCase());
        }
        return ret.toString();
    }

    public static StringList readLine(String str) {
        StringList ret = new StringList();
        BufferedReader reader = new BufferedReader(new StringReader(str));
        try {
            String line = reader.readLine();
            while (line != null) {
                ret.add(line);
                line = reader.readLine();
            }
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            return ret;
        }
    }

    public static List<Character> toChars(String s) {
        List<Character> ret = new ArrayList<Character>(s.length());
        for (int i = 0; i < s.length(); i++) {
            ret.add(s.charAt(i));
        }
        return ret;
    }

    public static int compare(List<Character> list1, List<Character> list2) {
        int ret = 0;
        int size = Math.min(list1.size(), list2.size());
        for (int i = 0; i < size; i++) {
            Character c1 = list1.get(i);
            Character c2 = list2.get(i);
            ret = c1.compareTo(c2);
            if (ret != 0) {
                break;
            }
        }
        if (ret == 0) {
            Integer size1 = list1.size();
            Integer size2 = list2.size();
            ret = size1.compareTo(size2);
        }

        return ret;
    }
    
    public static boolean isLong(String str){
        boolean ret = false;
        if (str == null || str.isEmpty()) {
            return false;
        }
        try {
            Long.parseLong(str);
            ret = true;
        } catch (NumberFormatException e) {
            ret = false;
        }
        return ret;        
    }

    public static boolean isInteger(String str) {
        boolean ret;
        if (str == null || str.isEmpty()) {
            return false;
        }
        try {
            Integer.parseInt(str);
            ret = true;
        } catch (NumberFormatException e) {
            ret = false;
        }
        return ret;
    }

    public static Boolean isAlphabetic(char c) {
        Boolean ret = (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
        return ret;
    }
}
