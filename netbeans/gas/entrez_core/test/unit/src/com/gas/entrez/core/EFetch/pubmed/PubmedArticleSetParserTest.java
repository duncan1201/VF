/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.entrez.core.EFetch.pubmed;


import com.gas.domain.core.pubmed.util.NCBIPubmedArticleSetParser;
import com.gas.domain.core.pubmed.PubmedArticle;
import com.gas.domain.core.pubmed.PubmedArticleSet;
import java.util.Iterator;
import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author dunqiang
 */
public class PubmedArticleSetParserTest {
    
    static PubmedArticleSet pubmed_17284678;
    
    public PubmedArticleSetParserTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        NCBIPubmedArticleSetParser parser = new NCBIPubmedArticleSetParser();
        pubmed_17284678 = parser.parse(PubmedArticleSetParserTest.class, "pubmed-17284678.xml");
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testParse_String() {
    }

    @Test
    public void testParse_Class_String() {
    }

    @Test
    public void testParse_InputStream() {
        Assert.assertTrue(pubmed_17284678.getArticles().size() > 0);
        Iterator<PubmedArticle> articleItr = pubmed_17284678.getArticles().iterator();
        while(articleItr.hasNext()){
            PubmedArticle article = articleItr.next();
            Assert.assertTrue(article.getArticleIdList().size() > 0);
        }
    }
}
