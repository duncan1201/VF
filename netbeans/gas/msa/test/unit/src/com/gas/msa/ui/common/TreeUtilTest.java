/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui.common;

import com.gas.domain.core.misc.NewickParser;
import com.gas.domain.core.misc.api.Newick;
import com.gas.msa.ui.common.TreeNode.Node;
import com.gas.msa.ui.common.data.TreeData;
import org.junit.*;

/**
 *
 * @author dq
 */
public class TreeUtilTest {
    
    public TreeUtilTest() {
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
     * Test of to method, of class TreeUtil.
     */
    @Test
    public void testTo() {
        System.out.println("to");
        NewickParser parser = new NewickParser();
        Newick newick = parser.parse(TreeData.class, "people.newick");       
        Node expResult = null;
        TreeNode.Node result = TreeUtil.to(newick);
        System.out.print("");
    }
}
