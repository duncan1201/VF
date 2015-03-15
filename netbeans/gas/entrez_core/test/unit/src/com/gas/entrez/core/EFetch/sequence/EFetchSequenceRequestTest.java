/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.entrez.core.EFetch.sequence;

import com.gas.entrez.core.EFetch.EFetchService;
import com.gas.common.ui.util.FileHelper;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.entrez.core.ESearch.ESearchCmdResultParser;
import com.gas.entrez.core.ESearch.api.ESearchCmdResult;
import java.io.File;
import java.util.List;
import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author dq
 */
public class EFetchSequenceRequestTest {
    
    public EFetchSequenceRequestTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of sendRequest method, of class EFetchSequenceRequest.
     */
    @Test
    public void testSendRequest() throws Exception {
        System.out.println("sendRequest");
        ESearchCmdResult sResult = ESearchCmdResultParser.parse(EFetchSequenceRequestTest.class, "protein_search_3.xml");
        String idStr = sResult.getIdList(String.class);
        EFetchService request = new EFetchService();
        request.setIds(idStr);
        request.setDb("protein");
        request.setRettype(EFetchService.RETTYPE.gb);
        //request.setRetmode(EFetchSequenceRequest.RETMODE.text);
        List<AnnotatedSeq> ret = request.sendRequest(AnnotatedSeq.class);
        Assert.assertTrue(ret.size() == 3);
        // TODO review the generated test code and remove the default call to fail.
    }

}
