package com.gas.domain.ui.molpane.txtpane;



import com.gas.main.ui.molpane.txtpane.BrickPane;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.TranslationResult;
import com.gas.domain.core.as.util.AnnotatedSeqParser;
import com.gas.domain.core.as.util.AsHelper;
import com.gas.domain.core.flexrs.FlexGenbankFormat;
import com.gas.translation.core.TranslationService;
import javax.swing.JFrame;
import org.biojava.bio.symbol.TranslationTable;

public class BrickPane_translation_test extends JFrame {


    public BrickPane_translation_test(String appName) {
        // Everything is as usual here
        super(appName);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        AnnotatedSeq seq = AnnotatedSeqParser.singleParse(BrickPane_translation_test.class, "sequence.gb", new FlexGenbankFormat());
        
        String translation = AsHelper.getCDSTranslation(seq);
        TranslationService tService = new TranslationService();
        int frame = 2;
        TranslationResult tr = tService.translate(seq.getSiquence().getData(), frame, TranslationTable.UNIVERSAL);
        TranslationResult tr2 = tService.translate(seq.getSiquence().getData(), 1, TranslationTable.UNIVERSAL);
        //System.out.println(tr.getData());
        /*
        for(int i = 0; i < tr.getData().length(); i++){
            int from = i * 3 + 1 + frame - 1;
            int to = from + 2;
            System.out.println(from + "-" + to + ":" + tr.getData().charAt(i));
        }
         */
        AsHelper.addTranslationResult(seq, tr);
        AsHelper.addTranslationResult(seq, tr2);
        
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
        BrickPane_translation_test writer = new BrickPane_translation_test("SVG Writer");
        writer.setVisible(true);
    }
}
