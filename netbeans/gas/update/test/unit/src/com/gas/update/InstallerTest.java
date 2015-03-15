/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.update;

import java.util.Collection;
import java.util.List;
import org.junit.*;
import static org.junit.Assert.*;
import org.netbeans.api.autoupdate.InstallSupport;
import org.netbeans.api.autoupdate.InstallSupport.Validator;
import org.netbeans.api.autoupdate.OperationContainer;
import org.netbeans.api.autoupdate.OperationSupport.Restarter;
import org.netbeans.api.autoupdate.UpdateUnitProvider;
import org.netbeans.api.autoupdate.UpdateUnitProviderFactory;

/**
 *
 * @author dq
 */
public class InstallerTest {
    
    public InstallerTest() {
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
     * Test of restored method, of class Installer.
     */
    @Test
    public void testRestored() {
        System.out.println("restored");
                    List<UpdateUnitProvider> providers = UpdateUnitProviderFactory.getDefault().getUpdateUnitProviders(true);
                    System.out.println();
    }
}
