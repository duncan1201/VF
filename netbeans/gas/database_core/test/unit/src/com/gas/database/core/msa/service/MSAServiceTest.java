/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.database.core.msa.service;

import com.gas.domain.core.msa.MSA;
import com.gas.domain.core.nexus.api.INexusIOService;
import com.gas.domain.core.nexus.api.Nexus;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public class MSAServiceTest {
    
    public MSAServiceTest() {
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
     * Test of toNexus method, of class MSAService.
     */
    @Test
    public void testToNexus() {        
        System.out.println("toNexus");
        INexusIOService nexusService = Lookup.getDefault().lookup(INexusIOService.class);
        Nexus nexus = nexusService.parse(MSAServiceTest.class, "tree_tax_character.nex");     
        MSAService instance = new MSAService();
        MSA result = instance.toMSA(nexus);
    }

}