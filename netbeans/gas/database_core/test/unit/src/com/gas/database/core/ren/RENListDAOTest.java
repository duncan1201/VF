/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.database.core.ren;

import com.gas.database.core.service.HibernateConfigService;
import com.gas.domain.core.ren.RENList;
import java.util.List;
import java.util.Map;
import junit.framework.Assert;
import org.hibernate.cfg.Configuration;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author dunqiang
 */
public class RENListDAOTest {
    
    static Configuration config;
    static RENListDAO dao ;
    
    public RENListDAOTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        HibernateConfigService service = new HibernateConfigService();
        config = service.getDefaultConfiguration();
        
        dao = new RENListDAO();
        dao.setConfiguration(config);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testGetConfiguration() {
    }

    @Test
    public void testSetConfiguration() {
    }

    @Test
    public void testCreate() {
        //dao.create(null);
    }

    @Test
    public void testGetbyName() {
    }

    @Test
    public void testGetAll() {
        List<RENList> all = dao.getRENLists();
        Assert.assertTrue(all.size() > 0);        
    }
      
}
