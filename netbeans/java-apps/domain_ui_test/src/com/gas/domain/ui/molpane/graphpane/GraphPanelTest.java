package com.gas.domain.ui.molpane.graphpane;


import com.gas.main.ui.molpane.graphpane.GraphPanel;
import com.gas.main.ui.molpane.graphpane.GraphPanelLayout;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.util.AnnotatedSeqParser;
import com.gas.domain.core.flexrs.FlexGenbankFormat;
import com.gas.domain.ui.shape.Arrow;
import java.awt.Color;
import java.awt.Dimension;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class GraphPanelTest extends JFrame {
    
// Constructor

    public GraphPanelTest(String appName) {
        // Everything is as usual here
        super(appName);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        GraphPanel panel = new GraphPanel();
        panel.setPreferredSize(new Dimension(300, 300));
        panel.setBackground(Color.WHITE);
        
        AnnotatedSeq NM_008238 = AnnotatedSeqParser.singleParse(GraphPanelTest.class, "NM_008238.gb", new FlexGenbankFormat());
        panel.setAs(NM_008238);
        
        GraphPanelLayout layout = (GraphPanelLayout)panel.getLayout();
        Map<String, List<Arrow>> arrowMap = panel.getFeatureArrowMap();
        
        //layout.setArrowMap(arrowMap);
        
        int length = NM_008238.getLength();
        //layout.setStart(1);
        //layout.setEnd(length);
        
        // listener

        
        this.setContentPane(panel);
        
        this.pack();

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

// The entry point of the application
    public static void main(String[] args) {
        GraphPanelTest writer = new GraphPanelTest("GraphLayeredPaneTest");
        writer.setVisible(true);
    }
}
