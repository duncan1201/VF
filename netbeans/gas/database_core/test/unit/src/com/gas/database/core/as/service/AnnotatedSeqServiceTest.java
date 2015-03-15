/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.database.core.as.service;

import com.gas.domain.core.as.AnnotatedSeq;
import java.util.List;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author dq
 */
public class AnnotatedSeqServiceTest {
    
    public AnnotatedSeqServiceTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of get method, of class AnnotatedSeqService.
     */
    //@Test
    public void testGet() {
        System.out.println("get");
        String absolutePath = "\\My Data\\Sample Data\\Nucleotides\\digest-ligate\\Ligated Sequence";
        AnnotatedSeqService instance = new AnnotatedSeqService();
        AnnotatedSeq expResult = null;
        AnnotatedSeq result = instance.getByAbsolutePath(absolutePath, null);
        instance.buildFamilyTree(result, null);
        int count = result.getOperation().getLeafParticipantCount();
        System.out.print("");
    }
    
}