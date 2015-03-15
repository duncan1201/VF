/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.entrez.core.EInfo;

import com.gas.entrez.core.EInfo.api.EInfoResult;
import com.gas.common.ui.util.XMLUtil;
import com.gas.entrez.core.EInfo.api.EInfoResultComparators;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author dunqiang
 */
public class EInfoResultParser {

    public static EInfoResult parse(File file){
        EInfoResult ret = null;
        try {
            FileInputStream inputStream = new FileInputStream(file);
            ret = parse(inputStream);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(EInfoResultParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }
    
    public static EInfoResult parse(String xml) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(xml.getBytes(Charset.forName("UTF-8")));
        return parse(inputStream);
    }

    public static EInfoResult parse(Class clazz, String resource) {
        InputStream inputStream = clazz.getResourceAsStream(resource);
        return parse(inputStream);
    }

    public static EInfoResult parse(InputStream inputStream) {
        EInfoResult ret = new EInfoResult();
        Document doc = XMLUtil.getDocument(inputStream);
    
        String dbName = XMLUtil.xPathString(doc, "./eInfoResult/DbInfo/DbName/text()");
       
        ret.setDbName(dbName);
        
        String desc = XMLUtil.xPathString(doc, "./eInfoResult/DbInfo/Description/text()");
        ret.setDesc(desc);
     
        
        Integer count = XMLUtil.xPathInteger(doc, "./eInfoResult/DbInfo/Count/text()");
        if(count != null){
            ret.setCount(count);
        }                 
        
        List<String> dbNames = XMLUtil.xPathStringList(doc, "./eInfoResult/DbList/DbName");
        if (dbNames != null && !dbNames.isEmpty()) {
            ret.setDbList(dbNames);
        }

        NodeList nodeList = XMLUtil.xPathNodeList(doc, "./eInfoResult/DbInfo/FieldList/Field");
        if (nodeList != null) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                EInfoResult.Field field = parseField(nodeList.item(i));
                ret.getFieldList().add(field);
            }
            List<EInfoResult.Field> fields = ret.getFieldList();
            Collections.sort(fields, new EInfoResultComparators.FieldComparator());
            ret.setFieldList(fields);
        }

        nodeList = XMLUtil.xPathNodeList(doc, "./eInfoResult/DbInfo/LinkList/Link");
        if (nodeList != null) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                EInfoResult.Link link = parseLink(nodeList.item(i));
                ret.getLinkList().add(link);
            }
        }
        return ret;
    }

    private static EInfoResult.Link parseLink(Node node) {
        EInfoResult.Link ret = new EInfoResult.Link();
        String name = XMLUtil.xPathString(node, "./Name/text()");
        ret.setName(name);
        String menu = XMLUtil.xPathString(node, "./Menu/text()");
        ret.setMenu(menu);
        String desc = XMLUtil.xPathString(node, "./Description/text()");
        ret.setDesc(desc);
        String dbTo = XMLUtil.xPathString(node, "./DbTo/text()");
        ret.setDbTo(dbTo);
        return ret;
    }

    private static EInfoResult.Field parseField(Node node) {
        EInfoResult.Field ret = new EInfoResult.Field();
        String name = XMLUtil.xPathString(node, "./Name/text()");
        ret.setName(name);
        String fullName = XMLUtil.xPathString(node, "./FullName/text()");
        ret.setFullName(fullName);
        String desc = XMLUtil.xPathString(node, "./Description/text()");
        ret.setDesc(desc);
        int termCount = XMLUtil.xPathInteger(node, "./TermCount/text()");
        ret.setTermCount(termCount);
        boolean isDate = XMLUtil.xPathBoolean(node, "./IsDate/text()");
        ret.setDate(isDate);
        boolean isNumerical = XMLUtil.xPathBoolean(node, "./IsNumerical/text()");
        ret.setNumerical(isNumerical);
        boolean isSingleToken = XMLUtil.xPathBoolean(node, "./SingleToken/text()");
        ret.setSingleToken(isSingleToken);
        boolean isHierarchy = XMLUtil.xPathBoolean(node, "./Hierarchy/text()");
        ret.setHierarchy(isHierarchy);
        boolean isHidden = XMLUtil.xPathBoolean(node, "./IsHidden/text()");
        ret.setHidden(isHidden);

        return ret;
    }
}
