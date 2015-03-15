/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.misc;

import java.util.List;
import org.junit.*;

/**
 *
 * @author dq
 */
public class NewickIOUtilTest {

    public NewickIOUtilTest() {
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
     * Test of split method, of class NewickIOUtil.
     */
    @Test
    public void testSplit() {
        System.out.println("split");
        String str = "NM_20:5.6,(((NM_03:4.3,NM_06:-4.2):1.2,NM_07:-5.5):2.66,NM_04:4.7):1.9,NM_05:-5.6";
        List<String> result = NewickIOUtil.split(str);
        for (String s : result) {
            System.out.println(s);
        }
    }

    @Test
    public void testSplit_leaf() {
        System.out.println("testSplit_leaf");
        String str = "NM_20:5.6";
        List expResult = null;
        List<String> result = NewickIOUtil.split(str);
        for (String s : result) {
            System.out.println(s);
        }
    }
}
