package com.gas.domain.ui.molpane.txtpane;


import com.gas.main.ui.molpane.txtpane.BrickPane;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.util.AnnotatedSeqParser;
import com.gas.domain.core.flexrs.FlexGenbankFormat;
import javax.swing.JColorChooser;
import javax.swing.JFrame;

public class BrickPanel_test extends JFrame {

// Swing components
    private JColorChooser colorChooser;
    private JColorChooser highlightColorChooser;
// Constructor

    public BrickPanel_test(String appName) {
        // Everything is as usual here
        super(appName);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        AnnotatedSeq seq = AnnotatedSeqParser.singleParse(BrickPane_translation_test.class, "sequence.gb", new FlexGenbankFormat());
        
        BrickPane pane = BrickPane.createBrickPane();
        pane.setSeq(seq);
        //pane.setPreferredSize(new Dimension(500, 400));
             
        pane.setDoubleLine(true);
        
        this.setSize(550, 450);
        this.setContentPane(pane);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    

// The entry point of the application
    public static void main(String[] args) {
        BrickPanel_test writer = new BrickPanel_test("SVG Writer");
        writer.setVisible(true);
    }
}
