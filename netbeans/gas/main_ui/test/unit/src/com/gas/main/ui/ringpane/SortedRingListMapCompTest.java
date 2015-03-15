/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.ringpane;

import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.util.AnnotatedSeqParser;
import com.gas.domain.core.flexrs.FlexGenbankFormat;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author dq
 */
public class SortedRingListMapCompTest {
    
    public SortedRingListMapCompTest() {
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
     * Test of setRingMap method, of class SortedRingListMapComp.
     */
    @Test
    public void testSetRingMap() {
        System.out.println("setRingMap");
        RingListMapComp instance = new RingListMapComp();
        

        /* Set up frame */
        JFrame frame = new RingPaneTest();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        /* Set up scroll pane */
        JPanel panel = new JPanel(new BorderLayout());

        AnnotatedSeq as = AnnotatedSeqParser.singleParse(RingPaneTest.class, "M77789.gb", new FlexGenbankFormat());
        instance.getRingMap().create(as);
        Dimension dSize = instance.getDesiredDimension();
        instance.setMinimumSize(dSize);
        instance.setPreferredSize(dSize);
        panel.add(instance, BorderLayout.CENTER);


        /* pack and show frame */
        frame.add(panel);
        frame.pack();
        frame.setSize(180, 180 + 22);
        frame.setVisible(true);   
        while(true){}
    }

}
