/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane;

import com.gas.database.core.service.api.IDefaultDatabaseService;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.util.AnnotatedSeqParser;
import com.gas.domain.core.flexrs.FlexGenbankFormat;
import com.gas.main.ui.data.Data;
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
public class MolPaneTest {

    private static IDefaultDatabaseService service = Lookup.getDefault().lookup(IDefaultDatabaseService.class);
    private static AnnotatedSeq NM_001114;
    private static AnnotatedSeq NM_001114_20_bp;
    private static AnnotatedSeq NM_001114_modified;
    private static AnnotatedSeq pUC19;
    private static AnnotatedSeq pUC19_modified;
    private static AnnotatedSeq pUC19_modified_2;
    private static AnnotatedSeq M13KE_MODIFIED;

    public MolPaneTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {

        UIManager.setLookAndFeel(NimbusLookAndFeel.class.getName());
        //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        NM_001114 = AnnotatedSeqParser.singleParse(Data.class, Data.NM_001114, new FlexGenbankFormat());
        NM_001114_modified = AnnotatedSeqParser.singleParse(Data.class, Data.NM_001114_modified, new FlexGenbankFormat());
        NM_001114_20_bp = AnnotatedSeqParser.singleParse(Data.class, Data.NM_001114_20_bp, new FlexGenbankFormat());
        
        
        pUC19 = AnnotatedSeqParser.singleParse(Data.class, Data.P_UC19, new FlexGenbankFormat());
        pUC19_modified = AnnotatedSeqParser.singleParse(Data.class, Data.P_UC19_MODIFIED, new FlexGenbankFormat());
        pUC19_modified_2 = AnnotatedSeqParser.singleParse(Data.class, Data.P_UC19_MODIFIED_2, new FlexGenbankFormat());
        
        M13KE_MODIFIED = AnnotatedSeqParser.singleParse(Data.class, Data.M13KE_MODIFIED, new FlexGenbankFormat());
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        service.stopDefaultDatabaseServer();
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of createInstance method, of class MolPane.
     */
    @Test
    public void testCreateInstance() {
        MolPane result = MolPane.createInstance();
        result.setAs(M13KE_MODIFIED);

        //1. Create the frame.
        JFrame frame = new JFrame("MolPaneTest");

//2. Optional: What happens when the frame closes?
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//3. Create components and put them in the frame.
//...create emptyLabel...
        frame.getContentPane().add(result, BorderLayout.CENTER);

//4. Size the frame.
        //frame.pack();

//5. Show it.
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screenSize.width - 10, screenSize.height - 250);
        frame.setVisible(true);
        while (true) {
        }
    }
}