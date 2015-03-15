/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.vector.advanced;

import com.gas.domain.core.pubmed.PubmedArticle;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.*;

/**
 *
 * @author dq
 */
public class CitationImportServiceTest {

    public CitationImportServiceTest() {
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
     * Test of receive method, of class CitationImportService.
     */
    @Test
    public void testReceive() {
        String line = "afaa sdf<ArticleTitle>Engineering mammalian cells for solid-state sensor applications.</ArticleTitle>sa sdf";

        String regex = "<ArticleTitle>(.+)</ArticleTitle>";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        System.out.println(matcher.find());
        String ret = matcher.group(1);
        System.out.println(ret);
        File file = new File("C:\\VNTI Database\\Citation\\6b.mlo");
        CitationImportService instance = new CitationImportService();
        PubmedArticle expResult = null;
        PubmedArticle result = instance.receive(file);
        System.out.print("");
    }
}
