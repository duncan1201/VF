/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.exceptionhandler;

import java.io.File;
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
public class EmailServiceTest {
    
    public EmailServiceTest() {
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
     * Test of sendEmail method, of class EmailService.
     */
    @Test
    public void testSendEmail() {
        System.out.println("sendEmail");
        String to = "bugs@vectorfriends.com";
        String subject = "test";
        String body = "this is a test";
        File attachment = new File("D:\\tmp\\test.txt");                
        EmailService instance = new EmailService();
        boolean expResult = true;
        boolean result = instance.sendEmail(to, subject, body, attachment);
        assertEquals(expResult, result);   
    }
    
    //@Test
    public void testSendEmail_no() {
        System.out.println("sendEmail");
        String to = "bugs@vectorfriends.com";
        String subject = "test";
        String body = "this is a test";
        File attachment = new File("D:\\tmp\\test.txt");                
        EmailService instance = new EmailService();
        boolean expResult = true;
        boolean result = instance.sendEmail(to, subject, body);
        assertEquals(expResult, result);        
    }    
}