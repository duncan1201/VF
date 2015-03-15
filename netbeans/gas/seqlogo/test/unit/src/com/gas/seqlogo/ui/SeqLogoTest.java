/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.seqlogo.ui;

import com.gas.domain.core.fasta.Fasta;
import com.gas.domain.core.fasta.FastaParser;
import com.gas.seqlogo.service.SeqLogoService;
import com.gas.seqlogo.service.api.HeightsList;
import com.gas.seqlogo.service.data.DataDumb;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author dq
 */
public class SeqLogoTest {
    
    public SeqLogoTest() {
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
     * Test of paint method, of class SeqLogo.
     */
    @Test
    public void testPaint() {
        System.out.println("paint");
        Graphics g = null;
        SeqLogo instance = new SeqLogo();

        SeqLogoService service = new SeqLogoService();
        FastaParser parser = new FastaParser();
        Fasta fasta = parser.parse(DataDumb.class, DataDumb.VERY_SHORT_DNA[0]);
        HeightsList heightsList = service.calculateHeights(fasta);
        instance.setHeightsList(heightsList);
        
        JFrame frame = new JFrame( "Transform" );
        frame.addWindowListener( new WindowAdapter(){
            @Override
            public void windowClosing( WindowEvent e ){
                System.exit( 0 );
            }
        });                     
    
        frame.getContentPane().add( BorderLayout.CENTER, instance );
        
        frame.setSize( 550, 400 );
        frame.setVisible(true);
        while(true){}        
    }

}
