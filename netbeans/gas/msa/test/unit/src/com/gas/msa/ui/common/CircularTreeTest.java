/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui.common;

import com.gas.domain.core.misc.NewickParser;
import com.gas.domain.core.misc.api.Newick;
import com.gas.msa.ui.common.data.TreeData;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import org.junit.*;

/**
 *
 * @author dq
 */
public class CircularTreeTest {

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

    //@Test
    public void testCircularTree_POSITISE() {
        NewickParser parser = new NewickParser();
        Newick newick = parser.parse(TreeData.class, TreeData.POSITISE);
        TreeNode.Node result = TreeUtil.to(newick);
        CircularTree tree = new CircularTree();
        tree.setRoot(result);
//1. Create the frame.
        JFrame frame = new JFrame("testCircularTree_POSITISE");

//2. Optional: What happens when the frame closes?
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//3. Create components and put them in the frame.
//...create emptyLabel...
        frame.getContentPane().add(tree, BorderLayout.CENTER);

//4. Size the frame.
        //frame.pack();

//5. Show it.
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(size.width / 2, size.height - 50);
        frame.setVisible(true);
        while (true) {
        }
    }
    
    @Test
    public void testCircularTree_PRIMATES() {
        NewickParser parser = new NewickParser();
        Newick newick = parser.parse(TreeData.class, TreeData.PRIMATES);
        TreeNode.Node result = TreeUtil.to(newick);
        CircularTree tree = new CircularTree();
        tree.setRoot(result);
//1. Create the frame.
        JFrame frame = new JFrame("testCircularTree_PRIMATES");

//2. Optional: What happens when the frame closes?
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//3. Create components and put them in the frame.
//...create emptyLabel...
        frame.getContentPane().add(tree, BorderLayout.CENTER);

//4. Size the frame.
        //frame.pack();

//5. Show it.
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(size.width / 2, size.height - 50);
        frame.setVisible(true);
        while (true) {
        }
    }    
}
