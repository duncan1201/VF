/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.entrez.core.ESearch;

import com.gas.entrez.core.ESearch.api.ESearchCmdResult;
import com.gas.common.ui.util.XMLUtil;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import org.w3c.dom.Document;

/**
 *
 * @author dunqiang
 */
public class ESearchCmdResultParser {
    
    public static ESearchCmdResult parse(String xmlString){
        if(xmlString == null){
            return null;
        }
        ByteArrayInputStream inputStream = new ByteArrayInputStream(xmlString.getBytes());
        return parse(inputStream);
    }
    
    public static ESearchCmdResult parse(Class clazz, String resource){
        InputStream inputStream = clazz.getResourceAsStream(resource);
        ESearchCmdResult ret = parse(inputStream);
        return ret;
    }
    
    public static ESearchCmdResult parse(InputStream inputStream){
        ESearchCmdResult ret = new ESearchCmdResult();
        Document doc = XMLUtil.getDocument(inputStream);
        Integer count = XMLUtil.xPathInteger(doc, "./eSearchResult/Count/text()");
        ret.setCount(count);
        
        int retStart = XMLUtil.xPathInteger(doc, "./eSearchResult/RetStart/text()");
        ret.setRetStart(retStart);
        
        int retMax = XMLUtil.xPathInteger(doc, "./eSearchResult/RetMax/text()");
        ret.setRetMax(retMax);
        
        
        List<String> idList = XMLUtil.xPathStringList(doc, "./eSearchResult/IdList/Id");
        ret.setIdList(idList);
        
        String qt = XMLUtil.xPathString(doc, "./eSearchResult/QueryTranslation/text()");
        ret.setQueryTranlation(qt);
        
        return ret;
    }
}
