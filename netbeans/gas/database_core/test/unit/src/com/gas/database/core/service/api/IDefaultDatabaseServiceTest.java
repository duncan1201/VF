/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.database.core.service.api;

import com.gas.database.core.service.DefaultDatabaseService;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.openide.util.Exceptions;

/**
 *
 * @author dq
 */
public class IDefaultDatabaseServiceTest {
    
    public IDefaultDatabaseServiceTest() {
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
     * Test of initDefaultDatabaseSchema method, of class IDefaultDatabaseService.
     */
    @Test
    public void testInitDefaultDatabaseSchema() {
        System.out.println("initDefaultDatabaseSchema");
        IDefaultDatabaseService instance = new DefaultDatabaseService();
        boolean running = instance.isDefaultDatabaseRunning();
        System.out.println("running:"+running);
        if(!running){
            instance.startDefaultDatabaseServer();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        running = instance.isDefaultDatabaseRunning();
        System.out.println("running:"+running);
        instance.initDefaultDatabaseSchema();
        
    }


   
}
