/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.seqlogo.ui;

import com.gas.msa.seqlogo.ui.Transform;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import org.junit.*;

/**
 *
 * @author dq
 */
public class TransformTest {
    
    public TransformTest() {
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
     * Test of main method, of class Transform.
     */
    @Test
    public void testMain() {
        System.out.println("main");
        JFrame frame = new JFrame( "Transform" );
        frame.addWindowListener( new WindowAdapter(){
            public void windowClosing( WindowEvent e ){
                System.exit( 0 );
            }
        });                     

        Transform applet = new Transform();
        frame.getContentPane().add( BorderLayout.CENTER, applet );
        
        frame.setSize( 550, 400 );
        frame.setVisible(true);
        while(true){}
    }
}
