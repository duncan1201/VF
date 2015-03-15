/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.entrez.core.EInfo;

import com.gas.entrez.core.EInfo.api.EInfoResult;
import java.util.List;
import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author dunqiang
 */
public class EInfoCmdTest {
    
    public EInfoCmdTest() {
    }
    
    @BeforeClass
    public static void setUpClass() throws Exception {
    }
    
    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Test
    public void testSendRequest() throws Exception {
        EInfoCmd cmd = new EInfoCmd();
        String strResult = cmd.sendRequest(String.class);
        EInfoResult result = EInfoResultParser.parse(strResult);
        
        List<String> db = result.getDbList();
        for (String d : db) {
            testSendRequest(d);
        }
        //FileHelper.toFile(new File("D:\\tmp\\all.xml"), strResult);

        Assert.assertTrue(result.getDbList().size() > 0);
    }
    
    private void testSendRequest(String db) throws Exception {
        EInfoCmd cmd = new EInfoCmd();
        cmd.setDb(db);
        
        String strResult = cmd.sendRequest(String.class);
        EInfoResult result = EInfoResultParser.parse(strResult);
        List<EInfoResult.Field> fields = result.getDateField();
        int s = result.getDateField().size();
        System.out.println("Name="+result.getDbName());
        System.out.println("Size="+s);
        for(EInfoResult.Field f: fields){
            System.out.print(f.getFullName());
            System.out.print('\t');
        }
        //FileHelper.toFile(new File("D:\\tmp\\"+db + "2.xml"), strResult);
        
        Assert.assertTrue(result.getDbList().isEmpty());
    }
    
    
}
