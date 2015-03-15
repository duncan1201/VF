/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor.as.lineage;

import com.gas.common.ui.util.UIUtil;
import com.gas.database.core.as.service.AnnotatedSeqService;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.msa.clustalw.ClustalwParam;
import com.gas.domain.core.msa.clustalw.GeneralParam;
import java.awt.BorderLayout;
import java.awt.Color;
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
public class AncestorsCompTest {

    public AncestorsCompTest() {
    }
    static AnnotatedSeq ligated;
    static AnnotatedSeq ligated_1;
    static AnnotatedSeq digested;

    @BeforeClass
    public static void setUpClass() {
        String absolutePath = "\\My Data\\Sample Data\\Nucleotides\\digest-ligate\\Ligated Sequence";
        AnnotatedSeqService instance = new AnnotatedSeqService();
        ligated = instance.getFullByAbsolutePath(absolutePath);

        absolutePath = "\\My Data\\Sample Data\\Nucleotides\\digest-ligate\\Ligated Sequence(1)";
        ligated_1 = instance.getFullByAbsolutePath(absolutePath);

        absolutePath = "\\My Data\\Sample Data\\Nucleotides\\digest-ligate\\Digestion of NM_053051 by HindIII fragment 1";
        digested = instance.getFullByAbsolutePath(absolutePath);
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testSomeMethod() {
        System.out.println("testSomeMethod");
        AncestorsComp instance = new AncestorsComp();
        instance.setBackground(Color.WHITE);
        instance.as = ligated;

        JScrollPane scrollPane = new JScrollPane(instance);

//1. Create the frame.
        JFrame frame = new JFrame("FrameDemo");

//2. Optional: What happens when the frame closes?
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//3. Create components and put them in the frame.
//...create emptyLabel...
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

//4. Size the frame.
        //frame.pack();

//5. Show it.
        frame.setSize(750, 450);
        frame.setVisible(true);
        while (true) {
        }
    }
}