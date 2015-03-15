package com.gas.domain.ui.molpane.graphpane;

import com.gas.main.ui.molpane.graphpane.GraphPane;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.util.AnnotatedSeqParser;
import com.gas.domain.core.flexrs.FlexGenbankFormat;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class GraphScrollPaneTest extends JFrame {
    
    static int i = 0;

    public GraphScrollPaneTest(String appName) {
        // Everything is as usual here
        super(appName);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        
        final GraphPane pane = new GraphPane();
        pane.setPreferredSize(new Dimension(300, 300));
        pane.setBackground(Color.WHITE);
        
        final AnnotatedSeq NM_001206524 = AnnotatedSeqParser.singleParse(GraphScrollPaneTest.class, "NM_001206524.gb", new FlexGenbankFormat());
        final AnnotatedSeq NM_008238 = AnnotatedSeqParser.singleParse(GraphScrollPaneTest.class, "NM_008238.gb", new FlexGenbankFormat());
        final AnnotatedSeq NM_133503 = AnnotatedSeqParser.singleParse(GraphScrollPaneTest.class, "NM_133503.gb", new FlexGenbankFormat());
        
        
        pane.setAs(NM_001206524);

        panel.add(pane, BorderLayout.CENTER);
        JButton btn = new JButton("Switch");
        panel.add(btn, BorderLayout.SOUTH);
        btn.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                if(i % 2 == 0){
                    pane.setAs(NM_133503);
                }else{
                    pane.setAs(NM_008238);
                }
                //pane.getRowHeaderView().revalidate();
                //pane.getRowHeaderView().repaint();
                
                pane.revalidate();
                pane.repaint();
                
                //pane.getRowHeader().revalidate();
                //pane.getRowHeader().repaint();
                i++;
            }
        });
        
        this.setContentPane(panel);
        
        this.pack();

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

// The entry point of the application
    public static void main(String[] args) {
        GraphScrollPaneTest writer = new GraphScrollPaneTest("GraphPaneTest");
        writer.setVisible(true);
    }
}
