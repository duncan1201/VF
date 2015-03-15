/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.database.core.backup;

import com.gas.database.core.backup.api.BackupOption;
import java.io.File;
import junit.framework.Assert;
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
public class BackupServiceTest {
    
    public BackupServiceTest() {
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
     * Test of saveBackupOption method, of class BackupService.
     */
    @Test
    public void testSaveBackupOption() {
        System.out.println("saveBackupOption");
        BackupOption expected = BackupOption.ALERT_BEFORE_CLOSING;
        BackupService instance = new BackupService();
        instance.saveBackupOption(expected);
        
        BackupOption actual = instance.getBackupOption();
        
        Assert.assertEquals("Same ", actual, expected);                
    }
}