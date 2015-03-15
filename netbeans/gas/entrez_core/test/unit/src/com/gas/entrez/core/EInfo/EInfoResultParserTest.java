/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.entrez.core.EInfo;

import com.gas.entrez.core.EInfo.api.EInfoResult;
import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author dunqiang
 */
public class EInfoResultParserTest {
    
    static EInfoResult einfo_all ;
    static EInfoResult einfo_pubmed ;
    
    public EInfoResultParserTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        einfo_all = EInfoResultParser.parse(EInfoResultParserTest.class, "einfo-all.xml");
        einfo_pubmed = EInfoResultParser.parse(EInfoResultParserTest.class, "einfo-pubmed.xml");
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testParse_InputStream() {
        Assert.assertTrue(einfo_all.getDbList().size() > 0);
        
        
        Assert.assertTrue(einfo_pubmed.getLinkList().size() > 0);
        EInfoResult.Field field = einfo_pubmed.getFieldList().get(0);
        Assert.assertTrue(field.getDesc() != null);
        Assert.assertTrue(field.getName() != null);
        Assert.assertTrue(field.getFullName() != null);
        Assert.assertTrue(field.getTermCount() != null);
        Assert.assertTrue(field.isDate() != null);
        Assert.assertTrue(field.isHidden() != null);
        Assert.assertTrue(field.isHierarchy() != null);
        Assert.assertTrue(field.isNumerical() != null);
        Assert.assertTrue(field.isSingleToken() != null);
        
        Assert.assertTrue(einfo_pubmed.getFieldList().size() > 0);
        EInfoResult.Link link = einfo_pubmed.getLinkList().get(0);
        Assert.assertTrue(link.getDesc() != null);
        Assert.assertTrue(link.getDbTo() != null);
        Assert.assertTrue(link.getMenu() != null);
        Assert.assertTrue(link.getName() != null);
    }
}
