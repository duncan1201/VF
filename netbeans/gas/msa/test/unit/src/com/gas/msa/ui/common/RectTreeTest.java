/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui.common;

import com.gas.domain.core.misc.NewickParser;
import com.gas.domain.core.misc.api.Newick;
import com.gas.msa.ui.common.data.TreeData;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import org.junit.*;

/**
 *
 * @author dq
 */
public class RectTreeTest {

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
    public void testRectTree_POSITISE() {
        NewickParser parser = new NewickParser();
        Newick newick = parser.parse(TreeData.class, TreeData.POSITISE);  
        newick.positiseLength();
        TreeNode.Node result = TreeUtil.to(newick);
        RectTree rectTree = new RectTree();
        
        rectTree.setFontSize(12.0f);
        rectTree.setRoot(result);
        rectTree.setNodeShape(ITree.SHAPE.NONE);
        
        String[] lengthAttNames = rectTree.getLengthAttributeNames();
//1. Create the frame.
        JFrame frame = new JFrame("testRectTree_POSITISE");

//2. Optional: What happens when the frame closes?
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//3. Create components and put them in the frame.
//...create emptyLabel...
        frame.getContentPane().add(rectTree, BorderLayout.CENTER);

//4. Size the frame.
        //frame.pack();

//5. Show it.
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(size.width - 300, size.height - 300);
        frame.setVisible(true);
        while(true){}
    }       
    
    @Test
    public void testRectTree_PRIMATES() {
        NewickParser parser = new NewickParser();
        Newick newick = parser.parse(TreeData.class, TreeData.PRIMATES);  
        newick.positiseLength();
        TreeNode.Node result = TreeUtil.to(newick);
        RectTree rectTree = new RectTree();
        
        rectTree.setFontSize(12.0f);
        rectTree.setRoot(result);
        rectTree.setNodeShape(ITree.SHAPE.NONE);
        
        rectTree.setTransform(ITree.TRANSFORM.NONE);
//1. Create the frame.
        JFrame frame = new JFrame("testRectTree_POSITISE");

//2. Optional: What happens when the frame closes?
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//3. Create components and put them in the frame.
//...create emptyLabel...
        frame.getContentPane().add(rectTree, BorderLayout.CENTER);

//4. Size the frame.
        //frame.pack();

//5. Show it.
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(size.width - 300, size.height - 300);
        frame.setVisible(true);
        while(true){}
    }         
    
    //@Test
    public void testRectTree_negative() {
        NewickParser parser = new NewickParser();
        Newick newick = parser.parse(TreeData.class, TreeData.NEGATIVE);  
        newick.positiseLength();
        TreeNode.Node result = TreeUtil.to(newick);
        RectTree rectTree = new RectTree();
        rectTree.setRoot(result);
//1. Create the frame.
        JFrame frame = new JFrame("testRectTree_negative");

//2. Optional: What happens when the frame closes?
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//3. Create components and put them in the frame.
//...create emptyLabel...
        frame.getContentPane().add(rectTree, BorderLayout.CENTER);

//4. Size the frame.
        //frame.pack();

//5. Show it.
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(size.width - 300, size.height - 300);
        frame.setVisible(true);
        while(true){}
    }    

    //@Test
    public void testRectTree_people() {
        NewickParser parser = new NewickParser();
        Newick newick = parser.parse(TreeData.class, TreeData.PEOPLE);       
        TreeNode.Node result = TreeUtil.to(newick);
        RectTree rectTree = new RectTree();
        rectTree.setRoot(result);
//1. Create the frame.
        JFrame frame = new JFrame("testRectTree");

//2. Optional: What happens when the frame closes?
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//3. Create components and put them in the frame.
//...create emptyLabel...
        frame.getContentPane().add(rectTree, BorderLayout.CENTER);

//4. Size the frame.
        //frame.pack();

//5. Show it.
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(size.width - 300, size.height - 300);
        frame.setVisible(true);
        while(true){}
    }
}
