/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.entrez.core.elink;

import com.gas.common.ui.util.FileHelper;
import com.gas.entrez.core.elink.api.ELinkResult;
import java.io.File;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author dunqiang
 */
public class ELinkCmdTest {
    
    public ELinkCmdTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testGetFromDb() {
    }

    @Test
    public void testSetFromDb() {
    }

    @Test
    public void testGetLinkName() {
    }

    @Test
    public void testSetLinkName() {
    }

    @Test
    public void testGetToDb() {
    }

    @Test
    public void testSetToDb() {
    }

    @Test
    public void testGetIds() {
    }

    @Test
    public void testSetIds() {
    }

    @Test
    public void testSendRequest_string() {
        ELinkCmd cmd = new ELinkCmd();
        cmd.setFromDb("bioproject");
        cmd.setToDb("nuccore");
        cmd.setIds("28697");
        String ret = cmd.sendRequest(String.class);
        
        //ElinkResultParser parser = new ElinkResultParser();
        //ELinkResult result = ElinkResultParser.parse(ret);
        System.out.println(ret);
    }
    
    @Test
    public void testSendRequest_ELinkResult() {
        ELinkCmd cmd = new ELinkCmd();
        cmd.setFromDb("bioproject");
        cmd.setToDb("nuccore");
        cmd.setIds("28697");
        //ELinkResult ret = cmd.sendRequest(ELinkResult.class);
        String ret = cmd.sendRequest(String.class);
        
        //ElinkResultParser parser = new ElinkResultParser();
        //ELinkResult result = ElinkResultParser.parse(ret);
        System.out.println(ret);
        FileHelper.toFile(new File("D:\\tmp\\bioproject_elink_01.xml"), ret);
    }    
}
