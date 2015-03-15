/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.nexus;

import com.gas.domain.core.nexus.data.NexusData;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import org.junit.*;

/**
 *
 * @author dq
 */
public class NexusIOUtilTest {

    public NexusIOUtilTest() {
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
     * Test of removeComments method, of class NexusIOUtil.
     */
    @Test
    public void testRemoveComments() {
        System.out.println("removeComments");
        InputStream inputStream = null;
        String expResult = "";
        //String result = NexusIOUtil.removeComments(inputStream);
    }

    /**
     * Test of getBlocks method, of class NexusIOUtil.
     */
    @Test
    public void testGetBlocks() {
        System.out.println("testGetBlocks");
        Class clazz = NexusData.class;
        String name = "dnaOutFile2.nex";
        List expResult = null;
        List<String> result = NexusIOUtil.getBlockStrs(clazz, name);
        System.out.print("");
    }

    @Test
    public void testGetCommands() {
        System.out.println("testGetCommands");
        Class clazz = NexusData.class;
        String name = "dnaOutFile2.nex";
        List<String> result = NexusIOUtil.getBlockStrs(clazz, name);
        Iterator<String> itr = result.iterator();
        while (itr.hasNext()) {
            String str = itr.next();
            List<String> statements = NexusIOUtil.getStatements(str);
            System.out.print("");
        }
    }
}
