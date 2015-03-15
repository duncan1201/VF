/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.entrez.core.ESearch;

import com.gas.entrez.core.ESearch.api.ESearchCmdResult;
import java.io.InputStream;
import java.util.List;
import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author dunqiang
 */
public class ESearchCmdResultParserTest {
    
    public ESearchCmdResultParserTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testParse_String() {
    }

    @Test
    public void testParse_InputStream() {
        InputStream inputStream = ESearchCmdResultParserTest.class.getResourceAsStream("ESearchResult.xml");
        ESearchCmdResult result = ESearchCmdResultParser.parse(inputStream);
        Assert.assertTrue(result.getCount() > 0);
        Assert.assertTrue(result.getQueryTranlation().length() > 0);
        Assert.assertTrue(result.getIdList(List.class).size() > 0);
    }
}
