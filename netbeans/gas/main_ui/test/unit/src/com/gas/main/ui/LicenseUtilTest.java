/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui;

import com.gas.main.ui.LicenseUtil;
import com.gas.main.ui.LicenseService;
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
public class LicenseUtilTest {

    public LicenseUtilTest() {
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
     * Test of decrypt method, of class LicenseUtil.
     */
    @Test
    public void testDecrypt() {
        LicenseService ls = LicenseService.getInstance();
        //String secret = ls.createTrialLicense("Test@test.com");
        System.out.println("decrypt");
        String secret1 = "61cc0cb8971e0efe2ab9106af1542416f86712b074418a5a5e2b50a5f70265a2af1ac4b95baa044f5eb50108a651b35aaf1c9003cbf2d166e4cd848000706540";
        String secret2 = "4501780a096f8f6e349c9f13af91e3f02fe7cc93de7c96d0f55d8ea40e65ab637f6ddf76c72421ceb4e309778b1f654eba761706362fd6276b3f9ab3d7bb2a66";
        String s3 = "5ea55f96cfd5f53f93878a94c98056b820a954b0580805a9b264d81426a63eec67213bc3e6ca59fdd1b7706a155d3d2d7f8c6c85ec24c76a4ed9b8595436b76b";
        String expResult = "";
        String result = LicenseUtil.decrypt(s3);
        System.out.println();
// TODO review the generated test code and remove the default call to fail.
    }
}