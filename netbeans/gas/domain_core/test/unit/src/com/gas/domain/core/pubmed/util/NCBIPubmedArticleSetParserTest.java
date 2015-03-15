/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.pubmed.util;

import com.gas.domain.core.pubmed.PubmedArticle;
import com.gas.domain.core.pubmed.PubmedArticleSet;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author dq
 */
public class NCBIPubmedArticleSetParserTest {

    public NCBIPubmedArticleSetParserTest() {
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
     * Test of parse method, of class NCBIPubmedArticleSetParser.
     */
    @Test
    public void testParse_InputStream() throws IOException {
        System.out.println("parse");
        NCBIPubmedArticleSetParser instance = new NCBIPubmedArticleSetParser();
        InputStream inputStream = NCBIPubmedArticleSetParserTest.class.getResourceAsStream("test.xml");
        String str = IOUtils.toString(inputStream);
        PubmedArticle result = instance.singleParse(str);
        System.out.print("");
    }
}
