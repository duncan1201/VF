/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa;

import java.awt.BorderLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import org.junit.*;

/**
 *
 * @author dq
 */
public class MSAPanelTest {
    
    public MSAPanelTest() {
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
     * Test of getSelected method, of class MSAPanel.
     */
    @Test
    public void testGetSelected() {
        System.out.println("getSelected");
        MSAPanel instance = new MSAPanel(null, null);
        instance.setSelected((JComponent)instance.getMuscleUI());
        //Component expResult = null;
        //Component result = instance.getSelected();

        //1. Create the frame.
        JFrame frame = new JFrame("FrameDemo");

//2. Optional: What happens when the frame closes?
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//3. Create components and put them in the frame.
//...create emptyLabel...
        frame.getContentPane().add(instance, BorderLayout.CENTER);

//4. Size the frame.
        //frame.pack();

//5. Show it.
        frame.setSize( 550, 400 );
        frame.setVisible(true);        
        while(true){}
        
    }

}
