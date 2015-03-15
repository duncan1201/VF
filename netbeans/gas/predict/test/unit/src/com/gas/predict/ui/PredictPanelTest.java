/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.predict.ui;

import com.gas.predict.ui.orf.PredictPanel;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.util.AnnotatedSeqParser;
import com.gas.domain.core.flexrs.FlexGenbankFormat;
import com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author dq
 */
public class PredictPanelTest {
    
    private static AnnotatedSeq NM_001114;
    
    public PredictPanelTest() {
    }
    
    @BeforeClass
    public static void setUpClass() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(NimbusLookAndFeel.class.getName());
        NM_001114 = AnnotatedSeqParser.singleParse(PredictPanelTest.class, "NM_001114.gb", new FlexGenbankFormat());
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }

    /**
     * Test of getImageIcon method, of class PredictPanel.
     */
    @Test
    public void testGetImageIcon() {
        System.out.println("getImageIcon");
        MockMolPane molPane = new MockMolPane();
        molPane.setAs(NM_001114);
        PredictPanel instance = new PredictPanel();
        instance.refresh();
        molPane.add(instance, BorderLayout.CENTER);
        
//1. Create the frame.
        JFrame frame = new JFrame("MSAPaneTest");

//2. Optional: What happens when the frame closes?
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//3. Create components and put them in the frame.
//...create emptyLabel...
        frame.getContentPane().add(molPane, BorderLayout.CENTER);

//4. Size the frame.
        frame.pack();

//5. Show it.
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize( 280, size.height - 300 );
        frame.setVisible(true);        
        while(true){}             
    }
}
