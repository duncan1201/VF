/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.graphpane;

import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.util.AnnotatedSeqParser;
import com.gas.domain.core.flexrs.FlexGenbankFormat;
import com.gas.main.ui.data.Data;
import com.gas.main.ui.molpane.MolPane;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import org.biojava.bio.alignment.FlexibleAlignment;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author dq
 */
public class GraphPaneTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Test
    public void testCreateInstance() {
        GraphPane graphPane = new GraphPane();
        
        System.out.println("createInstance");
        AnnotatedSeq as = AnnotatedSeqParser.singleParse(Data.class, Data.NM_001114, new FlexGenbankFormat());
        graphPane.setAs(as);
                        //1. Create the frame.
        JFrame frame = new JFrame("MolPaneTest");

//2. Optional: What happens when the frame closes?
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//3. Create components and put them in the frame.
//...create emptyLabel...
        frame.getContentPane().add(graphPane, BorderLayout.CENTER);

//4. Size the frame.
        //frame.pack();

//5. Show it.
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screenSize.width - 200, screenSize.height - 200);
        frame.setVisible(true);        
        while(true){}        
    } 
}
