/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui.alignment;

import com.gas.database.core.msa.service.api.IConsensusService;
import com.gas.domain.core.fasta.Fasta;
import com.gas.domain.core.fasta.FastaParser;
import com.gas.domain.core.misc.api.INewickIOService;
import com.gas.domain.core.misc.api.Newick;
import com.gas.domain.core.msa.MSA;
import com.gas.msa.ui.data.Data;
import com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.UIManager;
import org.junit.*;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public class AlignPaneTest {
    
    private static IConsensusService cService = Lookup.getDefault().lookup(IConsensusService.class);
    public static MSA msaDNA ;
    public static MSA msaProtein;
    
    public AlignPaneTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        UIManager.setLookAndFeel(NimbusLookAndFeel.class.getName());
        FastaParser parser = new FastaParser();
        Fasta fasta = parser.parse(Data.class, Data.OUT_DNA_FASTA[0]);
        msaDNA = new MSA();
        msaDNA.setEntries(fasta);
        INewickIOService newickService = Lookup.getDefault().lookup(INewickIOService.class);
        Newick newick = newickService.parse(Data.class, Data.POSITISE_NEWICK[0]);
        newick.positiseLength();
        msaDNA.setNewick(newick); 
                
        fasta = parser.parse(Data.class, Data.OUT_AA_FASTA[0]);
        msaProtein = new MSA();
        msaProtein.setEntries(fasta);
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
     * Test of setMsa method, of class MSAPane.
     */
    //@Test
    public void testSetMsa_Protein() {
        System.out.println("testSetMsa_Protein");
        AlignPane instance = new AlignPane();
        instance.setMsa(msaProtein);

//1. Create the frame.
        JFrame frame = new JFrame("AlignPaneTest");

//2. Optional: What happens when the frame closes?
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//3. Create components and put them in the frame.
//...create emptyLabel...
        frame.getContentPane().add(instance, BorderLayout.CENTER);

//4. Size the frame.
        frame.pack();

//5. Show it.
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize( size.width - 250, size.height - 300 );
        frame.setVisible(true);        
        while(true){}        
    }    

    /**
     * Test of setMsa method, of class MSAPane.
     */
    @Test
    public void testSetMsa_DNA() {
        System.out.println("testSetMsa_DNA");
        AlignPane instance = new AlignPane();
        instance.setMsa(msaDNA);

//1. Create the frame.
        JFrame frame = new JFrame("FrameDemo");

//2. Optional: What happens when the frame closes?
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//3. Create components and put them in the frame.
//...create emptyLabel...
        frame.getContentPane().add(instance, BorderLayout.CENTER);

//4. Size the frame.
        frame.pack();

//5. Show it.
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize( size.width - 250, size.height - 250 );
        frame.setVisible(true);        
        while(true){}        
    }
}
