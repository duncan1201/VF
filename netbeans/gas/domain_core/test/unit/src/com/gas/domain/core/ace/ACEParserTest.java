/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.ace;

import com.gas.domain.core.ace.ACE.Contig;
import java.io.File;
import java.util.Iterator;
import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author dq
 */
public class ACEParserTest {

    public ACEParserTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testParse_File() {
        System.out.println("parse");
        File file = new File("D:\\tmp\\tigr\\454Contigs.ace");

        ACE result = ACEParser.parse(file);

        Iterator<Contig> itr = result.getContigs().iterator();
        while (itr.hasNext()) {
            Contig contig = itr.next();
            Assert.assertNotNull(contig.getName());
            Assert.assertNotNull(contig.getComplemented());
            Assert.assertNotNull(contig.getBaseQualities());
            Assert.assertNotNull(contig.getSequence());
        }
    }
}
