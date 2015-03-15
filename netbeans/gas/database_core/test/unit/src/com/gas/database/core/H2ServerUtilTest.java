/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.database.core;

import com.gas.database.core.conn.DbConnSettingsService;
import com.gas.database.core.schema.api.IDatabaseSchemaUpgradeService;
import com.gas.database.core.service.HibernateConfigService;
import java.util.Properties;
import org.h2.tools.Server;
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
public class H2ServerUtilTest {
    
    public H2ServerUtilTest() {        
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
     * Test of startTcpServer method, of class H2ServerUtil.
     */
    @Test
    public void testStartTcpServer() {
        System.setProperty("h2.baseDir", "D:\\tmp\\");
        System.out.println("startTcpServer");        
        boolean result = H2ServerUtil.startTcpServer();
        System.out.println("result=" + result);
        boolean isRunning = H2ServerUtil.isRunning();
        System.out.println("isRunning=" + isRunning);
        H2ServerUtil.stopTcpServer();
        isRunning = H2ServerUtil.isRunning();
        System.out.println("isRunning=" + isRunning);
        
        DbConnSettingsService svc = new DbConnSettingsService();
        HibernateConfigService svc2 = new HibernateConfigService();
        Properties properties = svc2.getDefaultConfiguration().getProperties();
        boolean present = DBUtil.isTablePresent(properties, IDatabaseSchemaUpgradeService.TALLE_AS);
        System.out.println("present=" + present);
    }

}