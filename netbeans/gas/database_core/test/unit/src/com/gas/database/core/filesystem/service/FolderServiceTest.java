/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.database.core.filesystem.service;

import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.filesystem.Folder;
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
public class FolderServiceTest {
    
    public FolderServiceTest() {
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
     * Test of load method, of class FolderService.
     */
    @Test
    public void testLoad() {
        System.out.println("load");
        String absolutePath = "\\My Data\\Sample Data\\Nucleotides";
        FolderService instance = new FolderService();
        Folder expResult = null;
        Folder result = instance.loadWithData(absolutePath);
        AnnotatedSeq as = result.getElement("NM_008238", AnnotatedSeq.class);
        System.out.print("");
        
    }

}