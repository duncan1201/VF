package com.gas.domain.ui.molpane.txtpane;

import com.gas.main.ui.molpane.txtpane.BrickPane;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.util.AnnotatedSeqParser;
import com.gas.domain.core.as.util.AsHelper;
import com.gas.domain.core.flexrs.FlexGenbankFormat;
import com.gas.domain.core.ren.REN;
import com.gas.domain.core.ren.RENManager;
import com.gas.domain.core.ren.RMap;
import com.gas.enzyme.core.service.RMapService;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JFrame;

public class BrickPane_rmap_test extends JFrame {

    public BrickPane_rmap_test(String appName) {
        // Everything is as usual here
        super(appName);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        AnnotatedSeq seq = AnnotatedSeqParser.singleParse(BrickPane_rmap_test.class, "sequence.gb", new FlexGenbankFormat());

        RMapService rmapService = new RMapService();
        REN EcoRI = RENManager.getEnzyme("EcoRI");
        REN AvaI = RENManager.getEnzyme("AvaI");
        REN SmaI = RENManager.getEnzyme("SmaI");
        REN EcoRV = RENManager.getEnzyme("EcoRV");
        
        Set<REN> rens = new HashSet<REN>();
        rens.add(EcoRI);
        rens.add(AvaI);
        rens.add(SmaI);
        rens.add(EcoRV);
        
        RMap rmMap = rmapService.findRM(seq.getSiquence().getData(), rens, 1, 1, null, true);
        AsHelper.setRestrictionMap(seq, rmMap);
                
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
        BrickPane_rmap_test writer = new BrickPane_rmap_test("SVG Writer");
        writer.setVisible(true);
    }
}
