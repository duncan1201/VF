/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.tigr.core.ui.editor.k;

import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.tigr.Kromatogram;
import com.gas.domain.core.tigr.util.KromatogramParser;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author dq
 */
public class ChromatogramPanelTest {
    
    public ChromatogramPanelTest() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of initComp method, of class ChromatogramPanel.
     */
    /*
    public void testInitComp() {
        System.out.println("testInitComp");
        Kromatogram kromatogram = KromatogramParser.parse(new File("D:\\tmp\\abi\\Demo Projects\\ONE2R.abi"));
        
        Integer traceLength = kromatogram.getTraceLength();
        ChromatogramPanel instance = new ChromatogramPanel();
        instance.setKromatogram(kromatogram);
        UIUtil.setPreferredWidth(instance.chromatogramComp, Math.round(traceLength * 2.2f));
        JScrollPane scrollPane = new JScrollPane(instance);
        
        JFrame frame = new JFrame("testInitComp");
        
        frame.getContentPane().add(scrollPane);

        frame.setSize(300, 600);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(d.width / 2 - 400, d.height / 2 - 300);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        while (true) {
        }   
    }*/
}