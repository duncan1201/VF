/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.primer3.core.util;

import com.gas.domain.core.primer3.Oligo;
import com.gas.domain.core.primer3.OligoElement;
import com.gas.domain.core.primer3.P3Output;
import java.io.InputStream;
import java.util.Iterator;
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
public class P3OutputParserTest {
    
    public P3OutputParserTest() {
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
     * Test of parse method, of class P3OutputParser.
     */
    @Test
    public void testParse_String() {

    }

    /**
     * Test of parse method, of class P3OutputParser.
     */
    @Test
    public void testParse_byteArr() {

    }

    /**
     * Test of parse method, of class P3OutputParser.
     */
    @Test
    public void testParse_File() {

    }

    /**
     * Test of parse method, of class P3OutputParser.
     */
    @Test
    public void testParse_InputStream() {
        System.out.println("parse");
        InputStream inputStream = P3OutputParserTest.class.getResourceAsStream("output.txt");
        P3Output expResult = null;
        P3Output result = new P3Output();
        P3OutputParser.parse(inputStream, result);
        // TODO review the generated test code and remove the default call to fail.
        //Assert.assertNotNull(oligo.getProductSize());
        //Assert.assertTrue(oligo.getProductSize() > 0);
        Assert.assertNotNull(result.getSequenceTemplate());
        Assert.assertTrue(result.getSequenceTemplate().length() > 0);
        Iterator<Oligo> itr = result.getOligos().iterator();
        while(itr.hasNext()){
            Oligo oligo = itr.next();
            if(oligo.getLeft() != null){
                OligoElement oeLeft = oligo.getLeft();                
                Assert.assertEquals(oeLeft.getSeqTemplate(), oeLeft.getSeq());                
            }
            
            if(oligo.getRight() != null){
                OligoElement oeRight = oligo.getRight();           
                Assert.assertEquals(oeRight.getSeqTemplate(), oeRight.getSeq());                
            }
        }
    }

    /**
     * Test of getOligoIndex method, of class P3OutputParser.
     */
    @Test
    public void testGetOligoIndex() {

    }

}
