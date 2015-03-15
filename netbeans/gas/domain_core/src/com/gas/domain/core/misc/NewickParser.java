/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.misc;

import com.gas.common.ui.core.IntList;
import com.gas.common.ui.util.StrUtil;
import com.gas.domain.core.misc.NewickIOUtil.LocInfo;
import com.gas.domain.core.misc.NewickIOUtil.LocInfoList;
import com.gas.domain.core.misc.api.Newick;
import com.gas.domain.core.misc.api.NewickLength;
import com.gas.domain.core.misc.api.NewickName;
import com.gas.domain.core.misc.api.NewickNode;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.openide.util.Exceptions;

/**
 *
 * @author dq
 */
public class NewickParser {

    public Newick parse(Class clazz, String name) {
        return parse(clazz.getResourceAsStream(name));
    }

    public Newick parse(InputStream inputStream) {
        Newick ret = null;
        try {
            String str = IOUtils.toString(inputStream);
            ret = parse(str);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return ret;
    }

    public Newick parse(File file) {
        Newick ret = null;
        try {
            FileInputStream inputStream = new FileInputStream(file);
            ret = parse(inputStream);
        } catch (FileNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        }
        return ret;
    }

    public Newick parse(String str) {
        return parse(str, null);
    }

    public Newick parse(String str, Newick ret) {
        if (ret == null) {
            ret = new Newick();
        }
        str = str.trim();
        /*
        if (str.startsWith("[")) {
            Integer index = StrUtil.indexOfClosingBracket(str, '[', 0);
            str = str.substring(index + 1);
        }*/
        if (str.endsWith(";")) {
            str = str.substring(0, str.length() - 1).trim();
        }
        List<NewickNode> children = parseChildrenRecursively(str);
        ret.setRoot(children.get(0));
        return ret;
    }

    private List<NewickNode> parseChildrenRecursively(String str) {
        str = str.trim();
        if (str.isEmpty()) {
            return null;
        }
        List<NewickNode> ret = new ArrayList<NewickNode>();
        //System.out.println(String.format("<%s>", str));
        if (str.startsWith("(") && str.endsWith(")")) {
            NewickNode child = new NewickNode();
            ret.add(child);
            List<NewickNode> child2 = parseChildrenRecursively(str.substring(1, str.length() - 1));
            child.addChildren(child2);
        } else {
            List<String> splits = NewickIOUtil.split(str);
            if (!splits.isEmpty()) {
                for (String split : splits) {
                    List<NewickNode> children = parseChildrenRecursively(split);
                    ret.addAll(children);
                }
            } else {
                boolean intermediate = NewickIOUtil.isIntermediateLeaf(str);
                NewickNode leave = parseLeave(str, intermediate);
                ret.add(leave);
                if (intermediate) {
                    String childStr = NewickIOUtil.getChildrenStr(str);
                    List<NewickNode> children = parseChildrenRecursively(childStr);
                    leave.addChildren(children);
                }
            }

        }
        return ret;
    }

    /*
     * Adam:0.5 or (something):1 or (something)
     */
    private NewickNode parseLeave(String str, boolean intermediate) {
        NewickNode ret = new NewickNode();
        String name = null;
        Integer colonIndex = null;
        Integer openBracketIndex = null;
        Integer closingBracketIndex = null;
        LocInfoList locInfoList = NewickIOUtil.createLocInfoList(str);
        if (intermediate) {
            openBracketIndex = str.indexOf('(');
            closingBracketIndex = StrUtil.indexOfClosingBracket(str, '(', openBracketIndex);
            IntList colonList = StrUtil.getIndices(str, ':', closingBracketIndex);
            for (Integer i : colonList) {
                if (!locInfoList.contains(i.intValue())) {
                    colonIndex = i;
                    break;
                }
            }

        } else {
            colonIndex = str.lastIndexOf(':');
        }
        parseName(ret, str, locInfoList, colonIndex, intermediate);  
        if (colonIndex != null && colonIndex > -1) {                                 
            parseLength(ret, str, locInfoList, colonIndex);
        }
        return ret;
    }

    private void parseName(NewickNode node, String str, final LocInfoList locInfoList, final Integer colonIndex, boolean intermediate) {
        NewickName newickName = null;
        if(!intermediate){
            if(colonIndex != null){
                newickName = new NewickName(str.substring(0, colonIndex));
            }else{
                newickName = new NewickName(str);
            }
        }else{
            if(colonIndex != null){
                str = str.substring(0, colonIndex);
            }
            str = StrUtil.removeBracketContent(str, '(');
            newickName = new NewickName(str);            
        }
        node.setNewickName(newickName);
    }

    private void parseLength(NewickNode node, String str, final LocInfoList locInfoList, final Integer colonIndex) {
        NewickLength newickLength = new NewickLength();
        String lengthStr;
        if (locInfoList != null) {
            LocInfoList subList = locInfoList.subLocInfoList(colonIndex);
            if (subList.isEmpty()) {
                lengthStr = str.substring(colonIndex + 1);
            } else {
                lengthStr = str.substring(colonIndex + 1, subList.get(0).getOpening());
                LocInfo locInfo = subList.get(0);
                String attributes = str.substring(locInfo.getOpening() + 1, locInfo.getClosing());
                parseLengthAttributes(newickLength, attributes);
            }
        } else {
            lengthStr = str.substring(colonIndex + 1);
        }
        float length = Float.parseFloat(lengthStr);

        newickLength.setLength(length);
        node.setNewickLength(newickLength);
    }

    private void parseLengthAttributes(NewickLength newickLength, String attributeStr) {
        LocInfoList locInfoList = NewickIOUtil.createLocInfoList(attributeStr);
        IntList commaList = StrUtil.getIndices(attributeStr, ',');
        Integer preIndex = 0;
        Integer index = null;
        List<String> attStrList = new ArrayList<String>();
        for (int i = 0; i < commaList.size(); i++) {
            int commaIndex = commaList.get(i);
            if (!locInfoList.contains(commaIndex)) {
                index = commaIndex;
                String attStr = attributeStr.substring(preIndex, index);
                attStrList.add(attStr);
                preIndex = index + 1;
            }
        }
        if (index != null) {
            String attStr = attributeStr.substring(index + 1);
            attStrList.add(attStr);
        }

        for (String attStr : attStrList) {
            String[] splits = attStr.split("=");
            String name = splits[0];
            String value = splits[1];
            if (name.contains("length_median")) {
                newickLength.setLengthMedian(Float.parseFloat(value));
            } else if (name.contains("length_mean")) {
                newickLength.setLengthMean(Float.parseFloat(value));
            } else if (name.contains("length_95%HPD")) {
                newickLength.setHpd(value);
            }
        }

    }
}
