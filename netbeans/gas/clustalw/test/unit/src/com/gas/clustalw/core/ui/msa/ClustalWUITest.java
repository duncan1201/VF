/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.clustalw.core.ui.msa;

import com.gas.domain.core.msa.clustalw.ClustalwParam;
import com.gas.domain.core.msa.clustalw.GeneralParam;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import org.junit.*;

/**
 *
 * @author dq
 */
public class ClustalWUITest {
    
    public ClustalWUITest() {
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
     * Test of getMsaParams method, of class ClustalWUI.
     */
    @Test
    public void testGetMsaParams() {
        System.out.println("getMsaParams");
        ClustalWUI instance = new ClustalWUI(false, null, null);  
        ClustalwParam result = new ClustalwParam();
        result.getGeneralParam().setType(GeneralParam.TYPE.DNA);
        instance.setMsaParam(result);
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
        frame.setVisible(true);        
        while(true){}
    }
}
