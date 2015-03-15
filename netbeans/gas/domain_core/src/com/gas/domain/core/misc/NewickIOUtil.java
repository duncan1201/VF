/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.misc;

import com.gas.common.ui.core.IntList;
import com.gas.common.ui.util.StrUtil;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dq
 */
public class NewickIOUtil {

    protected static String removeComments(String str) {
        String ret = str.trim();
        int startIndex = 0;
        int openBracketIndex;
        while ((openBracketIndex = ret.indexOf("[", startIndex)) > -1) {
            Integer closeBracketIndex = StrUtil.indexOfClosingBracket(str, '[', 0);
            if (closeBracketIndex == null || closeBracketIndex < 0) {
                break;
            }

            ret = ret.substring(startIndex, openBracketIndex) + ret.substring(closeBracketIndex);
            startIndex = closeBracketIndex + 1;
        }
        return ret.toString();
    }

    /*
     *
     */
    protected static boolean isIntermediateLeaf(String str) {
        boolean ret;
        str = str.trim();
        str = StrUtil.removeBracketContent(str, '[');
        str = str.trim();
        int openBracketIndex = str.indexOf("(");
        ret = openBracketIndex == 0;

        return ret;
    }

    //(((NM_03:4.3,NM_06:-4.2):1.2,NM_07:-5.5):2.66,NM_04:4.7):1.9,NM_20:5.6,NM_05:-5.6
    //NM_20:5.6,(((NM_03:4.3,NM_06:-4.2):1.2,NM_07:-5.5):2.66,NM_04:4.7):1.9,NM_05:-5.6
    /*
     * Splits "A,B,C" to "A", "B", and "C"
     */
    protected static List<String> split(String str) {
        if (str.startsWith("(") && str.endsWith(")")) {
            throw new IllegalArgumentException("");
        }
        List<String> ret = new ArrayList();
        IntList commaIndices = StrUtil.getIndices(str, ',');
        List<Integer> seperator = new ArrayList<Integer>();
        LocInfoList locInfoList = createLocInfoList(str);

        for (int i = 0; i < commaIndices.size(); i++) {
            Integer commaIndex = commaIndices.get(i);
            if (!locInfoList.contains(commaIndex.intValue())) {
                seperator.add(commaIndex);
            }
        }

        Integer curIndex = null;
        Integer preIndex = 0;
        for (int i = 0; i < seperator.size(); i++) {
            curIndex = seperator.get(i);
            String split = str.substring(preIndex, curIndex);
            ret.add(split);
            preIndex = curIndex + 1;
        }
        if (curIndex != null) {
            String split = str.substring(curIndex + 1);
            ret.add(split);
        }

        return ret;
    }

    /*
     * (A):0.1->A
     */
    protected static String getChildrenStr(String str) {
        int openBracketIndex = str.indexOf("(");
        int closingBracketIndex = StrUtil.indexOfClosingBracket(str, '(', openBracketIndex);

        String ret = str.substring(openBracketIndex + 1, closingBracketIndex);
        return ret;
    }

    public static LocInfoList createLocInfoList(String str) {
        LocInfoList ret = new LocInfoList();
        for (int i = 0; i < str.length();) {
            char ch = str.charAt(i);
            if (isLeftBracket(ch)) {
                LocInfo locInfo = new LocInfo();
                locInfo.setBracket(ch);
                locInfo.setOpening(i);

                Integer closing = StrUtil.indexOfClosingBracket(str, ch, i);
                locInfo.setClosing(closing);

                ret.add(locInfo);
                i = closing + 1;

            } else {
                i++;
            }
        }
        return ret;
    }

    private static boolean isLeftBracket(char ch) {
        return ch == '(' || ch == '[' || ch == '{';
    }

    public static class LocInfoList extends ArrayList<LocInfo> {

        public boolean contains(int index) {
            boolean ret = false;
            for (int i = 0; i < size(); i++) {
                LocInfo locInfo = get(i);
                if (locInfo.contains(index)) {
                    ret = true;
                    break;
                }
            }
            return ret;
        }

        public Boolean after(int index) {
            Boolean ret = null;
            if (!isEmpty()) {
                ret = get(0).getOpening() > index;
            }
            return ret;
        }

        public Boolean before(int index) {
            Boolean ret = null;
            if (!isEmpty()) {
                ret = get(size() - 1).getClosing() < index;
            }
            return ret;
        }

        public LocInfoList subLocInfoList(Integer startIndex) {
            return subLocInfoList(startIndex, null);
        }

        public LocInfoList subLocInfoList(Integer startIndex, Integer endIndex) {
            LocInfoList ret = new LocInfoList();
            for (int i = 0; i < size(); i++) {
                LocInfo locInfo = get(i);
                if (endIndex != null) {
                    if (locInfo.after(startIndex) && locInfo.before(endIndex)) {
                        ret.add(locInfo);
                    }
                } else {
                    if (locInfo.after(startIndex)) {
                        ret.add(locInfo);
                    }
                }
            }
            return ret;
        }
    }

    public static class LocInfo {

        private Integer opening;
        private Integer closing;
        private Character bracket;

        public boolean properSubsetOf(LocInfo another) {
            boolean ret = another.getOpening() < getOpening() && another.getClosing() > getClosing();
            return ret;
        }

        public boolean contains(int index) {
            return getOpening() < index && getClosing() > index;
        }

        public boolean properSupersetOf(LocInfo another) {
            boolean ret = another.getOpening() > getOpening() && another.getClosing() < getClosing();;
            return ret;
        }

        public Character getBracket() {
            return bracket;
        }

        public void setBracket(Character bracket) {
            this.bracket = bracket;
        }

        public Integer getClosing() {
            return closing;
        }

        public void setClosing(Integer closing) {
            this.closing = closing;
        }

        public Integer getOpening() {
            return opening;
        }

        public void setOpening(Integer opening) {
            this.opening = opening;
        }

        public boolean after(int index) {
            return opening > index && closing > index;
        }

        public boolean before(int index) {
            return opening < index && closing < index;
        }
    }
}
